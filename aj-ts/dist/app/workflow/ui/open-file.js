"use strict";
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        var ui;
        (function (ui) {
            /**
             * 打开流程定义列表对话框
             */
            ui.openDlg = new Vue({
                el: '.openDlg',
                methods: {
                    show: function () {
                        this.$refs.layer.show();
                        this.$refs.layer.$children[0].ajaxGet();
                    },
                    open: function (id) {
                        Mgr.clearStage();
                        // 定义实体
                        aj.xhr.get(this.ajResources.ctx + '/admin/workflow/process/' + id + '/', function (j) {
                            ui.DefInfo.info = j.result;
                            ui.DefInfo.isCreate = false;
                            // @ts-ignore
                            document.body.$('.defName').innerHTML = j.result.displayName;
                        });
                        // 定义 json
                        aj.xhr.get(this.ajResources.ctx + '/admin/workflow/process/getJson/' + id + '/', function (j) {
                            wf.DATA.JSON_DATA = j;
                            init(j);
                        });
                        this.$refs.layer.close();
                        FB.statusBar.showMsg("打开工作流流程成功，读取流程定义成功");
                    }
                }
            });
        })(ui = wf.ui || (wf.ui = {}));
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
