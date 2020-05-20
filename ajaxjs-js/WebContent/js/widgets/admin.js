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
		isCreate : {
			type: Boolean, // true=新建/fasle=编辑
			default:false
		}
	},
	template: 
		'<div class="ajaxjs-admin-info-btns">\
			<button><img :src="ajResources.commonAsset + \'/icon/save.gif\'" /> {{isCreate ? "新建":"保存"}}</button>\
			<button onclick="this.up(\'form\').reset();return false;">复 位</button>\
			<button v-if="!isCreate" v-on:click.prevent="del()">\
				<img :src="ajResources.commonAsset + \'/icon/delete.gif\'" /> 删 除\
			</button>\
			<button onclick="history.back();return false;">返回</button><slot></slot>\
		</div>',
	methods: {
		del() {
			if (confirm('确定删除？'))
				ajaxjs.xhr.dele('.', json => {
					if (json && json.isOk) {
						alert(json.msg);
						location.assign('../list/');
					}
				});
		}
	}
});

// 搜索、分类下拉
Vue.component('aj-admin-filter-panel', {
	props: {
		label : {
			type: String,
			required : false
		},
		catalogId :{		//
			type: Number,
			required: false
		},
		selectedCatalogId :{ // 已选中的分类 id
			type: Number,
			required: false
		},
		noCatalog : {
			type: Boolean, // 是否不需要 分类下拉
			default : false
		},
		searchFieldValue : { // 搜索哪个字段？默认为 name
			required: false,
			default : 'name'
		}
	},
	template: 
		'<div class="aj-admin-filter-panel">\
			<form action="?" method="GET">\
				<input type="hidden" name="searchField" :value="searchFieldValue" />\
				<input type="text" name="keyword" placeholder="请输入搜索之关键字" style="float: inherit;" class="ajaxjs-inputField" />\
				<button style="margin-top: 0;" class="ajaxjs-btn">搜索</button> &nbsp;\
			</form><slot></slot>\
			<span v-if="!noCatalog">{{label||\'分类\'}}：\
				<aj-tree-catelog-select :is-auto-jump="true" :catalog-id="catalogId" :selected-catalog-id="selectedCatalogId"></aj-tree-catelog-select></span>\
		 </div>'
});

aj.admin = {
	del(id, title) {
		if (confirm('请确定删除记录：\n' + title + ' ？')) {
			ajaxjs.xhr.dele('../' + id + '/', json=> {
				if (json.isOk) {
					aj.alert('删除成功！');
					location.reload();
				}
			});
		}
	},
	setStatus(id, status) {
		ajaxjs.xhr.post('../setStatus/' + id + '/', json => {
			if (json.isOk) {

			}
		}, {
			status : status
		});
	}
};