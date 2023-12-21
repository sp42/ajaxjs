import { codemirror } from "vue-codemirror";
import 'codemirror/lib/codemirror.css';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/mode/sql/sql.js'
import 'codemirror/addon/selection/active-line.js'
import 'codemirror/addon/edit/closebrackets.js'
import 'codemirror/theme/monokai.css'// 编辑的主题文件
import 'codemirror/theme/base16-light.css'
import './code-prettify';
import { copyToClipboard } from '../../util/utils';
import tips from "../widget/tips.vue";
import api from "./api.vue";

import { formatSql } from './format-sql.js';

export default {
    components: { codemirror, tips, api },
    props: {
        data: {
            type: Object,
            require: true,
            default() {
                return {
                    data: {},
                };
            },
        },
    },
    data() {
        return {
            currentData: this.data.data,
            editorData: {
                // 当前编辑器数据，根据不同类型的
                type: "info",
                isCustomSql: true,
                sql: "",
            },
            cmOption: {
                tabSize: 4,
                styleActiveLine: true,
                lineNumbers: true,
                mode: "text/x-mysql",
                // theme: "monokai"
            },
        };
    },
    methods: {
        togglePanel() {
            let config = this.$el.querySelector(".config");
            if (config.style.height == "300px") {
                config.style.height = "0";
            } else config.style.height = "300px";
        },
        copySql() {
            copyToClipboard(this.editorData.sql);
            this.$Message.success("复制 SQL 代码成功");
        },
        formatSql() {
            let key = this.getTypeKey();
            this.currentData[key] = this.editorData.sql = formatSql(this.editorData.sql);
            // console.log();
        },

        getTypeKey() {
            let key;
            switch (this.editorData.type) {
                case "info":
                    key = "infoSql";
                    break;
                    v;
                case "list":
                    key = "listSql";
                    break;
                case "create":
                    key = "createSql";
                    break;
                case "update":
                    key = "updateSql";
                    break;
                case "delete":
                    key = "deleteSql";
                    break;
            }

            return key;
        }

    },
    watch: {
        "editorData.type"(v) {
            let key = this.getTypeKey();
            this.editorData.isCustomSql = !!this.currentData[key];

            if (this.editorData.isCustomSql)
                this.editorData.sql = this.currentData[key];
            else
                this.editorData.sql = "";
        },
        "editorData.isCustomSql"(v) {
            let key = this.getTypeKey();

            if (v) {
                switch (this.editorData.type) {
                    case "info":
                        this.currentData[key] = this.editorData.sql = "SELECT * FROM xxx";
                        break;
                        v;
                    case "list":
                        this.currentData[key] = this.editorData.sql = "SELECT * FROM xxx";
                        break;
                    case "create":
                        this.currentData[key] = this.editorData.sql = "SELECT * FROM xxx";
                        break;
                    case "update":
                        this.currentData[key] = this.editorData.sql = "SELECT * FROM xxx";
                        break;
                    case "delete":
                        this.currentData[key] = this.editorData.sql = "DELETE * FROM xxx";
                        break;
                }
            } else {
                this.currentData[key] = null;
                this.editorData.sql = "";
            }
        },
    },
};

function to(s) {
    if (s) {
        s = s.replace(/&/g, '&amp;');
        s = s.replace(/</g, '&lt;');
        s = s.replace(/>/g, '&gt;');
        s = s.replace(/ /g, '&nbsp;');
    }

    return s;
}
