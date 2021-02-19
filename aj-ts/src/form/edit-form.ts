namespace aj.form {
    /**
        新建、编辑都同一表单
     */
    export interface EditForm extends Vue, Ajax {
        id: string;
        info: Object;
    }

    Vue.component('aj-edit-form', {
        template: html`
            <form class="aj-table-form" :action="getInfoApi + (isCreate ? '' : info.id + '/')" :method="isCreate ? 'POST' : 'PUT'">
                <h3>{{isCreate ? "新建" : "编辑" }}{{uiName}}</h3>
                <!-- 传送 id 参数 -->
                <input v-if="!isCreate" type="hidden" name="id" :value="info.id" />
                <slot v-bind:info="info"></slot>
                <div class="aj-btnsHolder">
                    <button><img :src="ajResources.commonAsset + '/icon/save.gif'" /> {{isCreate ? "新建":"保存"}}</button>
                    <button onclick="this.up(\'form\').reset();return false;">复 位</button>
                    <button v-if="!isCreate" v-on:click.prevent="del()">
                        <img :src="ajResources.commonAsset + '/icon/delete.gif'" /> 删 除
                    </button>
                    <button @click.prevent="close">关闭</button>
                </div>
            </form>
        `,
        props: {
            isCreate: Boolean,                          // 是否新建模式
            uiName: String,
            apiUrl: { type: String, required: true }// 获取实体详情的接口地址 
        },
        data() {
            return {
                id: 0,
                info: {},		// 实体
            }
        },
        mounted(): void {
            xhr.form(this.$el, (j: RepsonseResult) => {
                if (j) {
                    if (j.isOk) {
                        let msg = (this.isCreate ? "新建" : "保存") + this.uiName + "成功";
                        aj.msg.show(msg);
                        this.$parent.close();
                    } else
                        aj.msg.show(j.msg);
                }
            });
        },
        methods: {
            load(this: EditForm, id: string, cb: Function): void { // 加载数据，获取实体详情
                this.id = id;

                xhr.get(this.apiUrl + id + "/", (j: RepsonseResult) => {
                    this.info = j.result;
                    cb && cb(j);
                });
            },
            close(this: EditForm): void {
                if (this.$parent.$options._componentTag === 'aj-layer')
                    //@ts-ignore
                    this.$parent.close();
                else
                    history.back();
            },

            /**
             * 执行删除
             * 
             * @param this 
             */
            del(this: EditForm): void {
                let id: string = utils.getFormFieldValue(this.$el, 'input[name=id]'),
                    title: string = utils.getFormFieldValue(this.$el, 'input[name=name]');

                aj.showConfirm(`请确定删除记录：\n${title}？`, () =>
                    xhr.dele(`../${id}/`, (j: RepsonseResult) => {
                        if (j.isOk) {
                            aj.msg.show('删除成功！');
                            //setTimeout(() => location.reload(), 1500);
                        } else
                            aj.alert('删除失败！');
                    })
                );
            }
        }
    });
}