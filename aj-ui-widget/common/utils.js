if (!window.aj)
    aj = {};

/**
* 复制文字到剪切板
* 
* @param {*} text 
*/
aj.copyToClipboard = function (text) {
    if (navigator.clipboard) {
        // clipboard api 复制
        navigator.clipboard.writeText(text);
    } else {
        var textarea = document.createElement('textarea');
        document.body.appendChild(textarea);
        // 隐藏此输入框
        textarea.style.position = 'fixed';
        textarea.style.clip = 'rect(0 0 0 0)';
        textarea.style.top = '10px';
        // 赋值
        textarea.value = text;
        // 选中
        textarea.select();
        // 复制
        document.execCommand('copy', true);
        // 移除输入框
        document.body.removeChild(textarea);
    }
}

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
; (() => {
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
                    alert('AJAX 错误:\n' + e + '\nThe url is:' + cb.url); // 提示用户 异常
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

    // 业务处理
    function businessCheck(json) {
        if (json && !json.status)
            alert(json.message || '操作失败');
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

        cfg.header = { "Content-Type": "application/x-www-form-urlencoded" };
        request(method, url, params, cb, cfg);
    }

    function json(method, url, params, cb, cfg) {
        if (typeof params != 'string' && !(params instanceof FormData))
            params = JSON.stringify(params);

        if (!cfg)
            cfg = {};

        cfg.header = { "Content-Type": "application/json" };
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

})();

/**
 * 通用的打开下载对话框方法，没有测试过具体兼容性
 * https://www.cnblogs.com/liuxianan/p/js-download.html
 * 
 * ref 这应该是你见过的最全前端下载总结 https://juejin.cn/post/6844903763359039501
 * 
 * @param url 下载地址，也可以是一个blob对象，必选
 * @param saveName 保存文件名，可选
 */
function openDownloadDialog(url, saveName) {
    if (typeof url == 'object' && url instanceof Blob)
        url = URL.createObjectURL(url); // 创建blob地址

    var aLink = document.createElement('a');
    aLink.href = url;
    aLink.download = saveName || ''; // HTML5新增的属性，指定保存文件名，可以不要后缀，注意，file:///模式下不会生效

    var event;
    if (window.MouseEvent) event = new MouseEvent('click');
    else {
        event = document.createEvent('MouseEvents');
        event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
    }

    aLink.dispatchEvent(event);
}

function getQueryParam(variable, isParent) {
    var query = (isParent ? parent.location : window.location).search.substring(1);
    var vars = query.split("&");

    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) { return pair[1]; }
    }

    return (false);
}