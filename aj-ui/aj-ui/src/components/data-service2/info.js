import { codemirror } from "vue-codemirror";
import 'codemirror/lib/codemirror.css';
import 'codemirror/addon/display/autorefresh';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/mode/sql/sql.js'
import 'codemirror/addon/selection/active-line.js'
import 'codemirror/addon/edit/closebrackets.js'
import 'codemirror/theme/monokai.css'// 编辑的主题文件
import 'codemirror/theme/base16-light.css'
import './code-prettify';
import tips from "../widget/tips.vue";
import api from "./api.vue";
import { copyToClipboard } from '../../util/utils';
import { isDev } from '../../util/utils';
import { formatSql } from './format-sql.js';

export default {
    components: { codemirror, tips, api },
    props: {
        data: {
            type: Object,
            required: true,
            default() {
                return {
                    data: {},
                };
            },
        },
        type: {
            type: String,
            required: false
        },
    },
    data() {
        let data = this.data.data;
        let isSignle = data.type && data.type === 'SINGLE';
        console.log(data)

        return {
            isSignle: isSignle,
            currentData: data,
            editorData: {
                // 当前编辑器数据，根据不同类型的
                type: isSignle ? "sql" : "infoSql",
                isCustomSql: isSignle ? true : !!data.infoSql,
                sql: isSignle ? data.sql : data.infoSql,
            },
            cmOption: {
                tabSize: 4,
                styleActiveLine: true,
                lineNumbers: true,
                mode: "text/x-mysql",
                autoRefresh: true, // 重点是这句，为true
                // theme: "monokai"
            },
        };
    },
    mounted() {
        setTimeout(() => this.$refs.cm.refresh(), 500);// 加载codemirror编辑器必须点击一下代码才能正常显示并且代码向左偏移
    },
    methods: {
        parentDir() {
            let dir = this.data.id.split('/')[0];

            if (dir.indexOf(':') != -1) {
                let arr = dir.split(':');
                dir = arr.pop();
            }

            return dir;
        },
        togglePanel() {
            let config = this.$el.querySelector(".config");
            if (config.style.height == "300px") config.style.height = "0";
            else config.style.height = "300px";
        },
        copySql() {
            copyToClipboard(this.editorData.sql);
            this.$Message.success("复制 SQL 代码成功");
        },
        formatSql() {
            this.editorData.sql = formatSql(this.editorData.sql);
        },
        getApiPrefix() {
            let obj;

            if (this.data.id.indexOf('/') != -1)
                obj = this.data.parentNode.parentNode;
            else
                obj = this.data.parentNode;

            let url = isDev ? obj.apiPrefixDev : obj.apiPrefixProd;

            if (!url.endsWith("/")) url += "/";
            url += "common_api/";

            return url;
        },

        /**
         * 切换编辑器不同的类型
         * 
         * @param {String} type 
         */
        setEditorData(type) {
            this.editorData.type = type;
            let sql = this.currentData[type];

            if (sql) {
                this.editorData.sql = sql;
                this.editorData.isCustomSql = true;
            } else
                this.editorData.sql = "";
        }
    },
    watch: {
        "editorData.isCustomSql"(v) {
            if (v) {

            } else {
                this.editorData.sql = "";
                if (this.currentData[this.editorData.type])
                    this.currentData[this.editorData.type] = '__NULL_STRING__';
            }
        },
        // 同步到 data
        "editorData.sql"(v) {
            this.currentData[this.editorData.type] = this.editorData.sql;
        }
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
