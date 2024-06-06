function onSelect() {
    let old = document.querySelector('.selected');
    old.classList.remove('selected');

    this.classList.add('selected');
}

document.querySelectorAll('menu li').forEach(item => {
    item.onclick = onSelect;
});

/**
 * 获取登录信息
 * 
 * @param {String} loginUrl 跳转地址
 * @returns 
 */
function getLoginInfo(loginUrl) {
    const token = getQueryParam("token");
    let accessToken = localStorage.getItem("accessToken");

    if (!accessToken && !token) {
        alert('你未登录！');
        location.assign(loginUrl);
    }

    if (token) {
        accessToken = decodeURIComponent(token);
        localStorage.setItem("accessToken", accessToken);

        // 只需要第一次的参数，之后不需要，现在清除       
        const url = new URL(location.href); // 创建一个包含查询参数的URL
        const params = new URLSearchParams(url.search);// 获取URL中的查询参数
        params.delete('token');// 删除名为'b'的参数
        url.search = params.toString();// 更新URL的查询参数

        location.assign(url.href);
    }

    // window.JWT_TOKEN = JSON.parse(accessToken);

    // 将 JWT Token 拆分为三个部分
    const tokenParts = accessToken.split('.')
    const payload = JSON.parse(atob(tokenParts[1])); // 解析载荷

    return payload;
}
// 后台服务登录地址，由 AJ-IAM SDK 提供的接口
let loginUrl = 'http://localhost:8302/user/login?web_url=http://127.0.0.1:8080/dev-plat/';

// 链接：生产 or 调试
if (location.origin.indexOf('admin.ajaxjs.com') != -1) {
    document.querySelector("a.portal-link").href = '../admin?m=portal';
    document.querySelector("a.model-link").href = '../admin?m=model';
    document.querySelector("a.iam-link").href = '../admin?m=user';

    loginUrl = 'https://base.gzdesign.cc/user/login?web_url=https://admin.ajaxjs.com/plat';
}

const payload = getLoginInfo(loginUrl);

// 用户租户 id，如果没有则为 superAdmin
let tenantId = null;
let r = payload.aud.match(/(?<=tenantId=)\d+/);

if (r && r[0]) {
    tenantId = Number(r[0]);
}

new Vue({
    el: '#user-bar',
    data: {
        jwtToken: payload,
        loginUrl: loginUrl
    },
    methods: {
        logout() {
            // 通知内嵌的网页登出
            let iframe = document.querySelector('iframe');
            iframe.contentWindow.postMessage('doLogout', '*');

            localStorage.removeItem("accessToken");
            location.assign(loginUrl);
        }
    }
});

