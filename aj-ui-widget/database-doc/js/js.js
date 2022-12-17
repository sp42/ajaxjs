myHTMLInclude();

Vue.use(VueCodemirror);

new Vue({
    el: 'body>nav',
    data: {

    },
    methods: {
        showAbout() {
            this.$Modal.success({
                title: '数据库文档浏览器',
                content: `<p>
                    <object id="svg1" data="asset/database.svg" type="image/svg+xml" width="32" style="vertical-align: middle;"></object>
                    一个简单的数据库文档浏览器，<a href="https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-ui-widget/database-doc" target="_blank">主页</a>。</p><p>Powered by AJAXJS Framework.</p><p>ver 2022.10.31</p>`
            });
        },
    }
});

MAIN = new Vue({
    el: 'body>div>menu',
    data: {
        cached: true,
        showSqlEditor: false,
        sql: `SELECT * FROM user\n`,
        cmOption: {
            tabSize: 4,
            styleActiveLine: true,
            lineNumbers: true,
            mode: 'text/x-mysql',
            // theme: "monokai"
        },

        java: {
            showJavaEditor: false,
            code: '',
            name: ''
        },
        cmOptionJava: {
            tabSize: 4,
            styleActiveLine: true,
            lineNumbers: true,
            indentWithTabs: true,
            smartIndent: true,
            lineNumbers: true,
            lineWrapping: true,
            matchBrackets: true,
            autofocus: true,
            mode: 'text/x-java',
            // theme: "monokai"
        },

        codeGenerator: {
            showCfg: false,
            data: {
                packageName: '',
                author: '',
                isGetter: true
            }
        },
        dataSource: {
            isShowDataSource: false
        },

        list: DOC_DATA
    },
    methods: {
        batchDownload(item) {
            console.log(item)

            var zip = new JSZip();

            item.tableInfo.forEach(tableInfo => {
                let code = beanGen(tableInfo);
                // var blob = new Blob([code], { type: 'text/txt,charset=UTF-8' });
                var filename = firstLetterUpper(toHump(tableInfo.name)) + '.java';

                zip.file(filename, code);// 创建一个被用来打包的名为Hello.txt的文件
            });

            zip.generateAsync({ type: "blob" }).then(function (blob) {// 把打包内容异步转成blob二进制格式
                openDownloadDialog(blob, item.name + '.zip');
            });
        },
        showSQL(sql) {
            this.sql = sql;
            this.showSqlEditor = true;
        },
        showJava(tableInfo) {
            this.java.name = tableInfo.name;
            this.java.code = beanGen(tableInfo);
            this.java.showJavaEditor = true;
        },

        downloadJava() {
            var blob = new Blob([this.java.code], { type: 'text/txt,charset=UTF-8' });
            var filename = firstLetterUpper(toHump(this.java.name)) + '.java';

            openDownloadDialog(blob, filename);
        },
        copySQL() {
            aj.copyToClipboard(this.sql);
            this.$Message.success('复制成功');
        },
        copyJava() {
            aj.copyToClipboard(this.java);
            this.$Message.success('复制成功');
        },
        onChangeDatasource(datasource) {
            this.$Loading.start();
            this.dataSource.isShowDataSource = false;
			let api = window.API_ROOT ? API_ROOT + "/make_database_doc" : '../../make_database_doc';
			
            aj.xhr.postJson(api, datasource, j => {
                if (j.status === 1) {
                    setTimeout(() => {
                        this.$Loading.finish();
                        this.$Message.success('切换数据源成功');
                        setTimeout('location.reload()', 1000);
                    }, 500);
                }
            });
        }
    }
});

onscroll = (event) => {
    event.stopPropagation();
    event.preventDefault();

    window.scrollTo(0, 0); // scroll parent window to anchor in iframe
    return false;
};