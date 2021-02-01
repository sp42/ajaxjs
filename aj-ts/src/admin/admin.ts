namespace aj.admin {
	export var helper = {
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
	template: html`
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
	template: html`
		<div style="float:left;margin-top: .5%;">
			<a :href="'?downloadXSL=true&' + params" download>
				<i class="fa fa-file-excel-o" aria-hidden="true" style="color:#0bac00;"></i> 下载 Excel 格式
			</a>
		</div>
	`,
	props: { params: String }// 参数
});

Vue.component('aj-admin-control', {
	template: html`
		<td>
			<slot></slot>
			<a v-if="preview" :href="ajResources.ctx + preview + id + '/'" target="_blank">浏览</a>
			<a :href="'../' + id +'/'"><img :src="ajResources.commonAssetIcon + '/update.gif'" style="vertical-align: sub;" /> 编辑</a>
			<a href="javascript:;" @click="del(id, name)"><img :src="ajResources.commonAssetIcon + '/delete.gif'" style="vertical-align: sub;" /> 删除</a>
		</td>
	`,
	props: {
		id: String,			// 实体 id
		name: String, 		// 实体的名称
		preview: String		// 浏览的链接 
	},
	methods: {
		del(id: string, name: string): void {
			aj.admin.helper.del(id, name);
		}
	}
});

/**
 * 后台头部导航
 */
Vue.component('aj-admin-header', {
	template: html`	
		<header class="aj-admin-header">
			<div>
				<slot name="btns"></slot>
				<a href="#" target="_blank">
					<img width="12"
						src="data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==" />
					新窗口打开
				</a>
			</div>
			<fieldset>
				<legend>
					<slot name="title">
						{{isCreate ? "新建":"编辑"}}{{uiName}} ：<span v-if="infoId">No.{{infoId}}</span>
					</slot>
				</legend>
			</fieldset>
		</header>
    `,
	props: {
		isCreate: Boolean,	// true=新建/fasle=编辑
		uiName: String,	    // 实体名称
		infoId: Number		// 实体 id
	}
});

/**
 * 搜索、分类下拉
 */
Vue.component('aj-admin-filter-panel', {
	template: html`
		<div class="aj-admin-filter-panel">
			<form action="?" method="GET">
				<input type="hidden" name="searchField" :value="searchFieldValue" />
				<input type="text" name="keyword" placeholder="请输入搜索之关键字" style="float: inherit;" class="aj-input" />
				<button style="margin-top: 0;" class="aj-btn">搜索</button> &nbsp;
			</form>
			<slot></slot>
			<span v-if="!noCatalog">{{label || '分类'}}：
				<aj-tree-like-select :is-auto-jump="true" :catalog-id="catalogId" :selected-catalog-id="selectedCatalogId"></aj-tree-like-select>
			</span>
		</div>
    `,
	props: {
		label: { type: String, required: false },
		catalogId: { type: Number, required: false },
		selectedCatalogId: { type: Number, required: false },   // 已选中的分类 id
		noCatalog: { type: Boolean, default: false },           // 是否不需要 分类下拉
		searchFieldValue: { required: false, default: 'name' }  // 搜索哪个字段？默认为 name
	}
});

/**
 * 后台增加、编辑、复位、删除按钮
 */
Vue.component('aj-admin-info-btns', {
	template: html`
		<div class="aj-admin-info-btns">
			<button><img :src="ajResources.commonAssetIcon + '/save.gif'" /> {{isCreate ? "新 建":"保 存"}}</button>
			<button onclick="this.up('form').reset();return false;">复 位</button>
			<button v-if="!isCreate" v-on:click.prevent="del">
				<img :src="ajResources.commonAssetIcon + '/delete.gif'" /> 删 除
			</button>
			<button onclick="history.back();return false;">返 回</button>
			<slot></slot>
		</div>
	`,
	props: {
		isCreate: { type: Boolean, default: false },    // true=新建/fasle=编辑
		listUrl: { type: String, default: '../list/' }  // 成功删除后跳转的地址
	},
	methods: {
		/**
		 * 执行删除
		 */
		del(): void {
			// TODO
			if (confirm('确定删除？'))
				aj.xhr.dele('.', (j: RepsonseResult) => {
					if (j && j.isOk)
						//@ts-ignore
						location.assign(this.listUrl);
				});
		}
	}
});