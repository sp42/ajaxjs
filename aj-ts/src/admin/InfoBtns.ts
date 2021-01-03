/**
 * 后台增加、编辑、复位、删除按钮
 */
Vue.component('ajaxjs-admin-info-btns', {
    props: {
        isCreate: { type: Boolean, default: false },    // true=新建/fasle=编辑
        listUrl: { type: String, default: '../list/' }  // 成功删除后跳转的地址
    },
    template: `
		<div class="ajaxjs-admin-info-btns">
			<button><img :src="ajResources.commonAsset + '/icon/save.gif'" /> {{isCreate ? "新建":"保存"}}</button>
			<button onclick="this.up('form').reset();return false;">复 位</button>
			<button v-if="!isCreate" v-on:click.prevent="del">
				<img :src="ajResources.commonAsset + '/icon/delete.gif'" /> 删 除
			</button>
			<button onclick="history.back();return false;">返回</button><slot></slot>
        </div>
    `,
    methods: {
        /**
         * 执行删除
         */
        del(): void {
            // TODO
            if (confirm('确定删除？'))
                aj.xhr.dele('.', (j: RepsonseResult) => {
                    if (j && j.isOk)
                        location.assign(this.listUrl);
                });
        }
    }
});