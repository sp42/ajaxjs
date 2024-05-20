// 密码，密码正确情况无视白名单和超时设置，且支持自定义短链接，
const password = typeof (PASSWORD) != "undefined" ? PASSWORD : 'BricRoot';
// 短链超时，单位毫秒，支持整数乘法，0表示不设置超时，
const shorten_timeout = typeof (SHORTEN_TIMEOUT) != "undefined" ? SHORTEN_TIMEOUT.split("*").reduce((a, b) => parseInt(a) * parseInt(b), 1) : '0';
// 默认短链key的长度，遇到重复时会自动延长，
const default_len = typeof (DEFAULT_LEN) != "undefined" ? parseInt(DEFAULT_LEN) : 6;
// 为true开启演示，否则无密码且非白名单请求不受理，是则允许访客试用，超时后失效，
const demo_mode = typeof (DEMO_MODE) != "undefined" ? DEMO_MODE === 'true' : true;
// 为true自动删除超时的演示短链接记录，否则仅是标记过期，以便在后台查询历史记录，
const remove_completely = typeof (REMOVE_COMPLETELY) != "undefined" ? REMOVE_COMPLETELY === 'true' : true;
// 白名单中的域名无视超时，json数组格式，写顶级域名就可以，自动通过顶级域名和所有二级域名，
const white_list = JSON.parse(typeof (WHITE_LIST) != "undefined" ? WHITE_LIST : `[""]`);
const CHECK_URL = /^http(s)?:\/\/(.*@)?([\w-]+\.)*[\w-]+([_\-.,~!*:#()\w\/?%&=]*)?$/;
const common_head = {
  headers: {
    "content-type": "text/html;charset=UTF-8",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "POST",
  },
};

// 随机字符串
function randomString(len) {
  let $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
  let maxPos = $chars.length;
  let result = '';

  for (let i = 0; i < len; i++)
    result += $chars.charAt(Math.floor(Math.random() * maxPos));

  return result;
}

// 检查域名是否在白名单中，参数只包含域名部分，
function checkWhite(url) {
  let host = new URL(url).host;

  return white_list.some(h => host == h || host.endsWith('.' + h));
}

async function md5(message) {
  const msgUint8 = new TextEncoder().encode(message) // encode as (utf-8) Uint8Array
  const hashBuffer = await crypto.subtle.digest('MD5', msgUint8) // hash the message
  const hashArray = Array.from(new Uint8Array(hashBuffer)) // convert buffer to byte array

  return hashArray.map(b => b.toString(16).padStart(2, '0')).join('') // convert bytes to hex string
}

async function checkHash(url, hash) {
  if (!hash)
    return false;

  return (await md5(url + password)) == hash;
}

/**
 * 保存短链

 * @param {string} url 需要保存的原始 URL。
 * @param {string} key 用户指定的短链 key，如果为空则自动生成。
 * @param {boolean} admin 表示是否是管理员操作。管理员有权限覆盖已存在的链接。
 * @param {number} len 生成的随机字符串长度。如果未指定，则使用默认长度。
 * @returns {Promise<string>} 成功保存后返回短链的 key。
 */
async function saveUrl(url, key, admin, len) {
  console.log('>>>>>>>>>>' + url)
  len = len || default_len;// 如果未指定长度，则使用默认长度
  let override = admin && key;// 判断是否需要覆盖旧链接,密码正确且指定了 key 的情况直接覆盖旧值

  if (!override) // 如果不是管理员或没有提供key，则生成新的随机 key
    key = randomString(len); // 密码不正确情况无视指定 key

  let isExists = await loadUrl(key); // 检查当前key是否已存在
  console.log("key exists " + key + " " + isExists);

  if (override || !isExists) {  // 如果可以覆盖或key不存在，则保存新链接
    let mode = 3;

    if (admin)// 如果是管理员，则设置模式为0
      mode = 0;

    let value = `${mode};${Date.now()};${url}`; // 组合链接信息，准备保存

    if (remove_completely && mode != 0 && !checkWhite(url)) { // 检查是否需要设置链接过期时间
      let ttl = Math.max(60, shorten_timeout / 1000); // 计算过期时间，利用expirationTtl实现过期记录自动删除，低于60秒会报错
      console.log("key auto remove: " + key + ", " + ttl + "s");

      // 保存链接并返回key
      return await LINKS.put(key, value, { expirationTtl: ttl }), key;
    } else
      return await LINKS.put(key, value), key;
  } else
    return await saveUrl(url, key, admin, len + 1); // 如果 key 已存在，尝试增加长度后重新生成 key 并保存
}

/**
 * 加载短链

 * @param {string} key - 短链的键值。
 * @returns {string|null} 如果找到有效的短链地址，则返回该地址；如果未找到或短链已超时，则返回 null。
 */
async function loadUrl(key) {
  let value = await LINKS.get(key); // 通过键值获取短链对应的长链地址

  if (!value)  // 如果没有找到对应的长链地址，则返回 null
    return null;

  let list = value.split(';'), url; // 分割长链地址字符串为数组，根据分号分割
  console.log("value split " + list);

  // 如果数组只有一个元素，直接使用该元素作为url；否则，解析模式、创建时间和实际url
  if (list.length == 1)
    url = list[0]; // 老数据暂且正常跳转
  else {
    url = list[2];
    const mode = parseInt(list[0]);
    const createTime = parseInt(list[1]);

    // 检查短链是否超时，若超时且不在白名单内，则返回 null
    if (mode != 0 && shorten_timeout > 0 && Date.now() - createTime > shorten_timeout) {
      if (checkWhite(url))
        console.log('white list');
      else {
        console.log("shorten timeout"); // 超时和找不到做同样的处理
        return null;
      }
    }
  }

  return url;
}

/**
 * 处理客户端请求，并根据请求类型执行相应的操作。
 *
 * @param {Request} request 客户端发起的请求对象。
 * @returns {Response} 根据不同的请求方法返回不同的响应对象。
 */
async function handleRequest(request) {
  if (request.method === "POST") {// 处理 POST 请求
    let req = await request.json(); // 解析请求体为 JSON 格式
    let url = req["url"];
    let admin = await checkHash(url, req["hash"]); // 检查哈希值

    console.log("url " + req["url"]); // 打印请求的URL和是否为管理员
    console.log("admin " + admin);

    if (!CHECK_URL.test(url) || (!admin && !demo_mode && !checkWhite(url)))    // 非演示模式下，非白名单地址当成地址不合法处理
      return new Response(`{"status":500, "key": "Error: Url illegal."}`, common_head);

    let randomKey = await saveUrl(req["url"], req["key"], admin);// 保存 URL 并返回随机键

    return new Response(`{"status":200, "key":"/${randomKey}"}`, common_head);
    // TODO state of KV to check
    // return new Response(`{"status":200,"key":": Error:Reach the KV write limitation."}`, common_head);
  } else if (request.method === "OPTIONS")
    return new Response('', common_head); // 处理预检请求

  // GET Request
  let requestURL = new URL(request.url);
  let path = requestURL.pathname.split("/")[1];
  console.log('::::::::::::::::::' + path);

  if (!path)   // 如果没有路径参数，返回 HTML 内容
    return new Response('hihi', { headers: { "content-type": "text/html;charset=UTF-8" } });

  let url = await loadUrl(path); // 加载短链对应的完整 URL

  if (!url) // 如果找不到短链或者短链已超时，返回404页面
    return new Response('<!DOCTYPE html><body><h1>404 Not Found.</h1><p>The url you visit is not found.</p></body>',
      { headers: { "content-type": "text/html;charset=UTF-8" }, status: 404 });

  return Response.redirect(url, 302);  // 重定向到加载的完整 URL
}

addEventListener("fetch", async event => event.respondWith(handleRequest(event.request)));