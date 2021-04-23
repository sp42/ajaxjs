namespace aj.wf.ui {
    /**
     * 打开流程定义列表对话框
     */
    export let openDlg: Vue = new Vue({
        el: '.openDlg',
        methods: {
            show(this: Vue): void {
                this.$refs.layer.show();
                this.$refs.layer.$children[0].ajaxGet();
            },
            open(this: Vue, id: number): void {
                Mgr.clearStage();

                // 定义实体
                xhr.get(this.ajResources.ctx + '/admin/workflow/process/' + id + '/', (j: RepsonseResult) => {
                    DefInfo.info = j.result;
                    DefInfo.isCreate = false;
                    // @ts-ignore
                    (<HTMLElement>document.body.$('.defName')).innerHTML = j.result.displayName;
                });

                // 定义 json
                xhr.get(this.ajResources.ctx + '/admin/workflow/process/getJson/' + id + '/', j => {
                    DATA.JSON_DATA = j;
                    init(j);
                });

                this.$refs.layer.close();

                FB.statusBar.showMsg("打开工作流流程成功，读取流程定义成功");
            }
        }
    });
}