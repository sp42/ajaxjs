// 后台头部导航
Vue.component('ajaxjs-admin-header', {
	props : {
		isCreate: Boolean,	// true=新建/fasle=编辑
		uiName: String,	// 实体名称
		infoId: {			// 实体 id
	      type: Number,
	      required: false
	    }
	},
	template: 	
		'<header class="ajaxjs-admin-header">\
			<div>\
				<slot name="btns"></slot>\
				<a href="#" target="_blank"><img width="12" src="data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==" /> 新窗口打开</a>\
			</div>\
			<fieldset>\
				<legend><slot name="title">{{isCreate ? "新建":"编辑"}}{{uiName}} ：<span v-if="infoId">No.{{infoId}}</span></slot></legend>\
			</fieldset>\
		</header>'
})

// 后台增加、编辑、复位、删除按钮
Vue.component('ajaxjs-admin-info-btns', {
	props: {
		isCreate: {
			type: Boolean, // true=新建/fasle=编辑
			default:false
		}
	},
	template: 
		'<div class="ajaxjs-admin-info-btns">\
			<button><img :src="ajResources.commonAsset + \'/icon/save.gif\'" /> {{isCreate ? "新建":"保存"}}</button>\
			<button onclick="this.up(\'form\').reset();return false;">复 位</button>\
			<button v-if="!isCreate" v-on:click.prevent="del">\
				<img :src="ajResources.commonAsset + \'/icon/delete.gif\'" /> 删 除\
			</button>\
			<button onclick="history.back();return false;">返回</button><slot></slot>\
		</div>',
	methods: {
		del() {
			if (confirm('确定删除？'))
				aj.xhr.dele('.', j => {
					if (j && j.isOk) {
						location.assign('../list/');
					}
				});
		}
	}
});

// 搜索、分类下拉
Vue.component('aj-admin-filter-panel', {
	props: {
		label: {
			type: String, required : false
		},
		catalogId:{		//
			type: Number, required: false
		},
		selectedCatalogId: { // 已选中的分类 id
			type: Number, required: false
		},
		noCatalog: {// 是否不需要 分类下拉
			type: Boolean,  default : false
		},
		searchFieldValue: { // 搜索哪个字段？默认为 name
			required: false, default: 'name'
		}
	},
	template: 
		'<div class="aj-admin-filter-panel">\
			<form action="?" method="GET">\
				<input type="hidden" name="searchField" :value="searchFieldValue" />\
				<input type="text" name="keyword" placeholder="请输入搜索之关键字" style="float: inherit;" class="aj-input" />\
				<button style="margin-top: 0;" class="aj-btn">搜索</button> &nbsp;\
			</form><slot></slot>\
			<span v-if="!noCatalog">{{label||\'分类\'}}：\
				<aj-tree-catelog-select :is-auto-jump="true" :catalog-id="catalogId" :selected-catalog-id="selectedCatalogId"></aj-tree-catelog-select></span>\
		 </div>'
});

Vue.component('aj-admin-state', {
	template: '<div>\
					<div class="label">状态：</div>\
					<label>\
						<input name="stat" value="1" type="radio" :checked="checked == 1"/> 上线中 \
					</label> \
					<label>\
						<input name="stat" value="0" type="radio" :checked="checked == 0" /> 已下线 \
					</label> \
					<label>\
						<input name="stat" value="2" type="radio" :checked="checked == 2" /> 已删除\
					</label>\
				</div>',
	props: {
		checked: Number// 哪个选中了？
	}
});

Vue.component('aj-admin-xsl', {
	template: '<div style="float:left;margin-top: .5%;">\
			<a :href="\'?downloadXSL=true&\' + params" download>\
				<img :src="ajResources.commonAsset + \'/icon/excel.png\'" width="16" style="vertical-align: middle;" /> 下载 Excel 格式\
			</a>\
		</div>',
	props: {
		params: String // 参数
	}
});

Vue.component('aj-admin-control', {
	template: '<td> <slot></slot>\
			<a v-if="preview" :href="ajResources.ctx + preview + id + \'/\'" target="_blank">浏览</a>\
			<a :href="\'../\' + id +\'/\'"><img :src="ajResources.commonAsset + \'/icon/update.gif\'" style="vertical-align: sub;" /> 编辑</a>\
			<a href="javascript:;" @click="del(id, name)"><img :src="ajResources.commonAsset + \'/icon/delete.gif\'" style="vertical-align: sub;" /> 删除</a>\
		</td>',
	props: {
		id: String,// 
		preview: String,// 浏览的链接 
		name: String // 实体的名称
	},
	methods: {
		del(id, name) {
			aj.admin.del(id, name);
		}
	}
});

aj.admin = {
	del(id, title) {
		aj.showConfirm('请确定删除记录：\n' + title + ' ？', () => {
			aj.xhr.dele('../' + id + '/', j => {
				if (j.isOk) {
					aj.msg.show('删除成功！');
					setTimeout(() => location.reload(), 1500);
				} else {
					aj.alert('删除失败！');
				}
			});
		});
	},
	setStatus(id, status) {
		aj.xhr.post('../setStatus/' + id + '/', j => {
			if (j.isOk) {

			}
		}, {
			status : status
		});
	},
	
	// 创建之后转向编辑界面
	defaultAfterCreate(j) {
		if(j && j.msg)
			aj.alert.show(j.msg);
				
		window.isCreate && j && j.isOk && setTimeout(() => location.assign(j.newlyId + "/"), 2000);
	}
};