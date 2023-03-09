import RequestBody from "./request-body.vue";
import Document from "./document/document.vue";
import InputTable from "./common/input-table.vue";

import { codemirror } from "vue-codemirror";
import "codemirror/addon/selection/active-line.js";
import "codemirror/addon/edit/closebrackets.js";
import "codemirror/mode/javascript/javascript";
import "codemirror/addon/lint/json-lint";
import "codemirror/lib/codemirror.css";
import "codemirror/addon/hint/show-hint.css";
import "codemirror/theme/base16-light.css";

import xhr from "./xhr";

export default {
    components: { RequestBody, Document, InputTable, codemirror },
    mixins: [xhr],
    data() {
        return {
            mainTab: 'form',
            url: {
                prefix: "https://dd.com",
                // main: "https://jsonplaceholder.typicode.com/posts",
                main: "https://httpbin.org/post",
            },
            httpMethod: "POST",

            cmOption: {
                tabSize: 4,
                styleActiveLine: true,
                lineNumbers: true,
                mode: "application/json",
                // theme: "monokai"
            },

            requestParams: {
                form: {
                    contentType: 'application/x-www-form-urlencoded',
                    data: [
                        {
                            enable: true,
                            key: "param1",
                            value: "bar",
                            desc: "",
                        },
                        {
                            enable: true,
                            key: "param2",
                            value: "foo",
                            desc: "",
                        },
                        // {
                        //     enable: true,
                        //     key: "",
                        //     value: "",
                        //     desc: "",
                        // },
                    ]
                },
                head: [
                    {
                        enable: true,
                        key: "",
                        value: "",
                        desc: "",
                    },
                ],
                raw: {
                    contentType: 'application/json',
                    json: '{ "title": "My post title", "body": "This is the body of my post." }',
                },
                queryString: [
                    {
                        enable: true,
                        key: "",
                        value: "",
                        desc: "",
                    },
                ],
            },

            requestAll: "",
            responseHead: "",
            responseBody: "",
        };
    },
    mounted() {

    },
    methods: {
        load() {
            let params, contentType;

            if ('form' === this.mainTab) {
                contentType = this.requestParams.form.contentType;
                params = json2fromParams(this.requestParams.form.data);
            } else if ('raw' === this.mainTab) {
                contentType = this.requestParams.raw.contentType;
                params = this.requestParams.raw.json;
            }

            this.doRequest(this.httpMethod, this.url.main, params, {
                header: {
                    'content-type': contentType
                }
            });
        },
        authToken() {
            // 读取粘贴板
            try {
                navigator.clipboard.readText().then((v) => {
                    console.log("获取剪贴板成功：", v);
                    this.requestParams.head.unshift({
                        enable: true,
                        key: "Authorization",
                        value: "Bearer " + v,
                        desc: "认证用的 token",
                    });
                }).catch((v) => {
                    console.log("获取剪贴板失败: ", v);
                });
            } catch (e) {
                console.log(e);
                this.$Message.error('不支持读取粘贴板');
            }
        },

        formatJs() {
            let json = this.requestParams.raw.json;
            json = JSON.stringify(JSON.parse(json), null, 4);
            this.requestParams.raw.json = json;
        }
    },
};

function json2fromParams(arr) {
    let _arr = [];
    arr.forEach(({ key, value }) => _arr.push(key + '=' + encodeURIComponent(value)));

    return _arr.join('&');
}