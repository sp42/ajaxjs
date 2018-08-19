// 后台头部导航
Vue.component('ajaxjs-admin-header', {
	props : {
		isCreate : Boolean,
		uiName : String,
		infoId : {
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
		'<div>\
			<button class="ajaxjs-btn"><img src="' + '' + 'save.gif" /> {{isCreate ? "新建":"编辑"}}</button>\
			<button v-if="!isCreate" class="ajaxjs-btn" onclick="this.up(\'form\').reset();return false;">复 位</button>\
			<button v-if="!isCreate" class="ajaxjs-btn" v-on:click.prevent="del()">\
				<img src="' + '' + 'delete.gif" /> 删 除\
			</button><slot></slot>\
		</div>',
	methods : {
		del : function () {
			if (confirm('确定删除 \n${info.name}？'))
				ajaxjs.xhr.dele('delete.do', function(json) {
					if (json && json.isOk) {
						alert(json.msg);
						location.assign('../list/list.do');
					}
				});
		}
	}
});

Vue.component('aj-admin-filter-panel', {
	props : {
		catelogId :{
			type:Number,
			required:true
		},
		selectedCatelogId :{
			type:Number,
			required:false
		}
	},
	template: 
		'<div class="aj-admin-filter-panel">\
			<form action="?" method="GET">\
				<input type="hidden" name="searchField" value="content" />\
				<input type="text" name="searchValue" placeholder="请输入正文之关键字" style="float: inherit;" class="ajaxjs-inputField" />\
				<button style="margin-top: 0;" class="ajaxjs-btn">搜索</button>\
			</form>\
			栏目： <select @change="onSelected($event);" class="ajaxjs-select" style="width: 200px;"></select></div>',
	
	mounted : function() {
		aj.xhr.get(this.ajResources.ctx + "/admin/catelog/getListAndSubByParentId", this.load.bind(this), 
				{parentId : this.catelogId});
	},
	methods: {
		load : function(json) {
			var catalogArr = json.result;
			var selectUI = new ajaxjs.tree.selectUI();
			
			var select = aj('select');
			selectUI.renderer(catalogArr, select, this.selectedCatelogId, {makeAllOption : false});
		},
		
		onSelected : function (e) {
			var el = e.target, catalogId = el.selectedOptions[0].value;
			if (catalogId == '全部分类')
				location.assign(location.origin + location.pathname); // todo
			else
				location.assign('?catalogId=' + catalogId);
		}
	}
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