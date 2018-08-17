// 后台头部导航
Vue.component('ajaxjs-admin-header', {
	props : {
		isCreate : Boolean, // true=新建/fasle=编辑
		uiName : String,
		infoId : {
	      type: Number,
	      required: false
	    }
	},
	template : 	
		'<header class="top">\
			<div>\
				<a v-if="isCreate" href="../"> {{zx}}</a> <span v-if="isCreate">|</span>\
				<a :href="isCreate ? \'list/\' : \'../list/\'">{{uiName}}列表</a> | \
				<a href="#" target="_blank"><img width="12" src="data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==" /> 新窗口打开</a>\
			</div>\
			<fieldset>\
				<legend>{{isCreate ? "新建":"编辑"}}{{uiName}} ：<span v-if="infoId">#{{infoId}}</span></legend>\
			</fieldset>\
		</header>'
});

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