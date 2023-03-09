//  XHR 发送组件
export default {
    data() {
        return {
            loading: false,
            response: {
                readyState: 0,
                status: 0,
                elapsed: 0,
            },
        };
    },

    methods: {
        doRequest(method, url, params, cfg) {
            let el = new Date();
            let xhr = new XMLHttpRequest();
            xhr.open(method, url);
            xhr.timeout = 5000; // 设置超时时间为5秒
            xhr.ontimeout = () => this.loading = false;// 请求超时后的处理
            xhr.onreadystatechange = () => {
                this.loading = true;
                this.response.readyState = xhr.readyState;
                this.response.status = xhr.status;

                if (xhr.readyState === 4) {
                    try {
                        if (!xhr.responseText) {
                            this.$Message.error('服务端返回空的字符串');
                            this.loading = false;

                            return;
                        }

                        // 跨域可能不能获取完整的响应头 https://qzy.im/blog/2020/09/can-not-get-response-header-using-javascript-in-cors-request/
                        let heads = xhr.getAllResponseHeaders();
                        heads = heads.split(';').join('\n');
                        this.responseHead = heads;

                        let parseContentType = cfg && cfg.parseContentType;
                        switch (parseContentType) {
                            case "text":
                                data = responseText;
                                break;
                            case "xml":
                                data = xhr.responseXML;
                                break;
                            case "json":
                            default:
                                this.responseBody = JSON.stringify(JSON.parse(xhr.responseText), null, 2);
                        }
                    } catch (e) {
                        alert("HTTP 请求错误:\n" + e + "\nURL: " + url); // 提示用户 异常
                    } finally {
                        this.loading = false;
                        this.response.elapsed = new Date() - el;
                    }
                }
            };

            let requestAll = 'HEAD \n' + method.toUpperCase() + ' ' + url + '\n';

            if (cfg && cfg.header) {
                for (let i in cfg.header) {
                    requestAll += i + " : " + cfg.header[i] + '\n';
                    xhr.setRequestHeader(i, cfg.header[i]);
                }
            }

            if (params)
                requestAll += 'BODY:\n' + params;

            this.requestAll = requestAll;
            xhr.send(params || null);
        },

        formatStatusCode() {
            let code = this.response.status;
            let str = code + '';

            if (str[0] === '2')
                return `<span style="color:green">${code}</span>`;
            else if (str[0] === '4' || str[0] === '5')
                return `<span style="color:red">${code}</span>`;
            else
                return str;
        }
    },
};

function json2fromParams(param) {
    let result = "";

    for (let name in param) {
        if (typeof param[name] != "function")
            result += "&" + name + "=" + encodeURIComponent(param[name]);
    }

    return result.substring(1);
}
