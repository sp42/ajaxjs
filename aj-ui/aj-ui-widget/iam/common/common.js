function myHTMLInclude() {
    var z, i, a, file, xhttp;
    z = document.getElementsByTagName("*");

    for (i = 0; i < z.length; i++) {
        if (z[i].getAttribute("w3-include-html")) {
            a = z[i].cloneNode(false);
            file = z[i].getAttribute("w3-include-html");
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (xhttp.readyState == 4 && xhttp.status == 200) {
                    a.removeAttribute("w3-include-html");
                    a.innerHTML = xhttp.responseText;
                    z[i].parentNode.replaceChild(a, z[i]);
                    myHTMLInclude();
                }
            }
            xhttp.open("GET", file, false);
            xhttp.send();
            return;
        }
    }
}

if (!window.aj)
    aj = {};

function request(method, url, params, cb, cfg) {
    var xhr = new XMLHttpRequest();
    xhr.open(method, url);
    xhr.onreadystatechange = function () {
        if (this.readyState === 4) {
            var responseText = this.responseText.trim();
            if (!responseText) {
                alert('服务端返回空的字符串!');
                return;
            }

            var data = null;
            try {
                var parseContentType = cfg && cfg.parseContentType;
                switch (parseContentType) {
                    case 'text':
                        data = responseText;
                        break;
                    case 'xml':
                        data = this.responseXML;
                        break;
                    case 'json':
                    default:
                        data = JSON.parse(responseText);
                        businessCheck(data);
                }
            } catch (e) {
                console.log(e);
                // alert('AJAX 错误:\n' + e + '\nThe url is:' + cb.url); // 提示用户 异常
            }

            cb && cb(data, this);
            /*				if (this.status === 200) {
                            } else if (this.status === 500) {
                            }*/
        }
    }

    if (cfg && cfg.header) {
        for (var i in cfg.header) {
            xhr.setRequestHeader(i, cfg.header[i]);
        }
    }

    xhr.send(params || null);
}

function json2fromParams(param) {
    let result = "";

    for (let name in param) {
        if (typeof param[name] != "function")
            result += "&" + name + "=" + encodeURIComponent(param[name]);
    }

    return result.substring(1);
}

function form(method, url, params, cb, cfg) {
    if (typeof params != 'string' && !(params instanceof FormData))
        params = json2fromParams(params);

    if (!cfg)
        cfg = {};

    if (!cfg.header)
        cfg.header = { "Content-Type": "application/x-www-form-urlencoded" };
    else
        Object.assign(cfg.header, { "Content-Type": "application/x-www-form-urlencoded" });

    request(method, url, params, cb, cfg);
}

aj.xhr = {
    get(url, cb, cfg) {
        request('GET', url, null, cb, cfg);
    },
    postForm(url, params, cb, cfg) {
        form("POST", url, params, cb, cfg);
    },
    postJson(url, params, cb, cfg) {
        json("POST", url, params, cb, cfg);
    },
    putForm(url, params, cb, cfg) {
        form("PUT", url, params, cb, cfg);
    },
    putJson(url, params, cb, cfg) {
        json("PUT", url, params, cb, cfg);
    },
    del(url, cb, cfg) {
        request('DELETE', url, null, cb, cfg);
    },
    formData(form) {
        if (!window.FormData)
            throw 'The version of your browser is too old, please upgrade it.';

        if (typeof form == 'string')
            form = document.querySelector(form);
        var json = {};

        var formData = new FormData(form);
        formData.forEach(function (value, key) {
            json[key] = value;
        });

        return json;
    }
};

// 业务处理
function businessCheck(json) {
    if (json && !json.status)
        alert(json.message || '操作失败');
}

function convertKeysToUnderscore(obj) {
    const convertedObj = {};

    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            const convertedKey = key.replace(/([A-Z])/g, "_$1").toLowerCase();
            convertedObj[convertedKey] = obj[key];
        }
    }

    return convertedObj;
}


const accessToken = localStorage.getItem("accessToken");

window.JWT_TOKEN = JSON.parse(accessToken);

if(location.href.indexOf('jsp') != -1 && !accessToken) {
  //  alert('你未登录！');
   //  location.assign('index.jsp');
    //location.assign('${pageContext.request.contextPath}');
}


const apiPrefix = 'http://192.168.1.107:8888/iam/admin_api/common/user';