namespace aj {
	export var admin = {
		/**
		 * 删除
		 * 
		 * @param id 
		 * @param title 
		 */
		del(id: string, title: string): void {
			aj.showConfirm('请确定删除记录：\n' + title + ' ？', () => {
				aj.xhr.dele('../' + id + '/', (j: RepsonseResult) => {
					if (j.isOk) {
						aj.msg.show('删除成功！');
						setTimeout(() => location.reload(), 1500);
					} else {
						aj.alert('删除失败！');
					}
				});
			});
		},
		setStatus(id: string, status: number): void {
			aj.xhr.post('../setStatus/' + id + '/', (j: RepsonseResult) => {
				if (j.isOk) {
				}
			}, { status: status + "" });
		},

		/**
		 * 创建之后转向编辑界面
		 * 
		 * @param j 
		 */
		defaultAfterCreate(j: RepsonseResult): void {
			if (j && j.msg)
				aj.alert(j.msg);

			//@ts-ignore
			window.isCreate && j && j.isOk && setTimeout(() => location.assign(j.newlyId + "/"), 2000);
		}
	};
}

Vue.component('aj-admin-state', {
	template: `
		<div>
			<div class="label">状态：</div>
			<label>
				<input name="stat" value="1" type="radio" :checked="checked == 1" /> 上线中 
			</label> 
			<label>
				<input name="stat" value="0" type="radio" :checked="checked == 0" /> 已下线 
			</label> 
			<label>
				<input name="stat" value="2" type="radio" :checked="checked == 2" /> 已删除
			</label>
		</div>
	`,
	props: { checked: Number }	// 哪个选中了？

});

Vue.component('aj-admin-xsl', {
	template: `
		<div style="float:left;margin-top: .5%;">
			<a :href="'?downloadXSL=true&' + params" download>
				<i class="fa fa-file-excel-o" aria-hidden="true" style="color:#0bac00;"></i> 下载 Excel 格式
			</a>
		</div>
	`,
	props: { params: String }// 参数
});

Vue.component('aj-admin-control', {
	template: `
		<td> <slot></slot>
			<a v-if="preview" :href="ajResources.ctx + preview + id + '/'" target="_blank">浏览</a>
			<a :href="'../' + id +'/'"><img :src="ajResources.commonAsset + '/icon/update.gif'" style="vertical-align: sub;" /> 编辑</a>
			<a href="javascript:;" @click="del(id, name)"><img :src="ajResources.commonAsset + '/icon/delete.gif'" style="vertical-align: sub;" /> 删除</a>
		</td>
	`,
	props: {
		id: String,			// 实体 id
		name: String, 		// 实体的名称
		preview: String		// 浏览的链接 
	},
	methods: {
		del(id: string, name: string): void {
			aj.admin.del(id, name);
		}
	}
});
