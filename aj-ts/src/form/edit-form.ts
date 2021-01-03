/**
    新建、编辑都同一表单
 */
Vue.component('aj-edit-form', {
    template: `
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
        getInfoApi: { type: String, required: true }// 获取实体详情的接口地址 
    },
    data() {
        return {
            id: 0,
            info: {},		// 实体
        }
    },
    mounted() {
        aj.xhr.form(this.$el, j => {
            if (j) {
                if (j.isOk) {
                    var msg = (this.isCreate ? "新建" : "保存") + this.uiName + "成功";
                    aj.alert.show(msg);
                    this.$parent.close();
                } else
                    aj.alert.show(j.msg);
            }
        }, {
            beforeSubmit(form, json) {
                //json.content = App.$refs.htmleditor.getValue({cleanWord : eval('${aj_allConfig.article.cleanWordTag}'), encode : true});
            }
        });
    },
    methods: {
        load(id, cb) { // 加载数据，获取实体详情
            this.id = id;

            aj.xhr.get(this.getInfoApi + id + "/", j => {
                this.info = j.result;
                cb && cb(j);
            });
        },
        close() {
            if (this.$parent.$options._componentTag === 'aj-layer') {
                this.$parent.close();
            } else
                history.back();
        },
        del() {
            var id = this.$el.$('input[name=id]').value, title = this.$el.$('input[name=name]').value;

            aj.showConfirm('请确定删除记录：\n' + title + ' ？', () =>
                aj.xhr.dele('../' + id + '/', j => {
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