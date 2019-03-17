// 后台头部导航
Vue.component('ajaxjs-admin-header', {
	props : {
		isCreate : Boolean,	// true=新建/fasle=编辑
		uiName : String,	// 实体名称
		infoId : {			// 实体 id
	      type: Number,
	      required: false
	    }
	},
	template : 	
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
	props : {
		isCreate : Boolean // true=新建/fasle=编辑
	},
	template : 
		'<div class="ajaxjs-admin-info-btns">\
			<button><img :src="ajResources.commonAsset + \'/icon/save.gif\'" /> {{isCreate ? "新建":"保存"}}</button>\
			<button onclick="this.up(\'form\').reset();return false;">复 位</button>\
			<button v-if="!isCreate" v-on:click.prevent="del()">\
				<img :src="ajResources.commonAsset + \'/icon/delete.gif\'" /> 删 除\
			</button><slot></slot>\
		</div>',
	methods : {
		del : function () {
			if (confirm('确定删除？'))
				ajaxjs.xhr.dele('.', function(json) {
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
	props : {
		label : {
			type : String,
			required : false
		},
		catelogId :{		//
			type: Number,
			required: false
		},
		selectedCatelogId :{ // 已选中的分类 id
			type: Number,
			required: false
		},
		noCatelog :{
			type : Boolean, // 是否不需要 分类下拉
			default : false
		}
	},
	template: 
		'<div class="aj-admin-filter-panel">\
			<form action="?" method="GET">\
				<input type="hidden" name="searchField" value="name" />\
				<input type="text" name="searchValue" placeholder="请输入正文之关键字" style="float: inherit;" class="ajaxjs-inputField" />\
				<button style="margin-top: 0;" class="ajaxjs-btn">搜索</button>\
			</form>\
			<span v-if="!noCatelog">{{label||\'分类\'}}：<aj-tree-catelog-select :is-auto-jump="true" :catelog-id="catelogId" :selected-catelog-id="selectedCatelogId"></aj-tree-catelog-select></span>\
		</div>'
});

aj.admin = {
	del : function(id, title) {
		if (confirm('请确定删除记录：\n' + title + ' ？')) {
			ajaxjs.xhr.dele('../' + id + '/', function(json) {
				if (json.isOk) {
					alert('删除成功！');
					location.reload();
				}
			});
		}
	},
	setStatus : function(id, status) {
		ajaxjs.xhr.post('../setStatus/' + id + '/', function(json) {
			if (json.isOk) {

			}
		}, {
			status : status
		});
	}
};