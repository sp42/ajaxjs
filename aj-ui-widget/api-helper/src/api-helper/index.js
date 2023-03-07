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

export default {
    components: { RequestBody, Document, InputTable, codemirror },
    data() {
        return {
            url: {
                prefix: "https://dd.com",
                main: "/dfdf",
            },
            httpMethod: "GET",

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
                            key: "",
                            value: "",
                            desc: "",
                        },
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
                    json: '{"name": "Jack"}',
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

            requestAll: "dfdfd",
            responseHead: "dffdfd",
            responseBody: "ddddddd",
        };
    },
    methods: {
        authToken() {
            let token = ""; // 读取粘贴板
            this.requestParams.head.unshift({
                enable: true,
                key: "Authorization",
                value: "Bearer " + token,
                desc: "认证用的 token",
            });
        },

        formatJs() {
            let json = this.requestParams.raw.json;
            json = JSON.stringify(JSON.parse(json), null, 4);
            this.requestParams.raw.json = json;
        }
    },
};