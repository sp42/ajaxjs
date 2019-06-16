function check(num, pos) {
	num = num >>> pos;
	return (num & 1) === 1;
}

function set(num, pos, v) {
	var old = check(num, pos);
	
	if(v && !old) {
		num = num + (1 << pos);
	} else if (old) {
		num = num - (1 << pos);
	} else {
		alert('sth wrong');
	}
	
	return num;
}

var num = 0;
num = set(num, 1, true);//设置[权限项60]为true
num = set(num, 3, true);//设置[权限项3]为true
num = set(num, 5, true);//设置[权限项3]为true
num = set(num, 6, true);//设置[权限项3]为true
num = set(num, 8, true);//设置[权限项3]为true

//alert(check(num, 60));//检查[权限项60]的权限
//alert(check(num, 1));//检查[权限项1]的权限
//alert(check(num, 3));//检查[权限项3]的权限

 
// 功能权限控件
Vue.component("aj-admin-role-check-right", {
	template : '<div>\
		  <label><input type="checkbox" name="enable" v-model="enabled" @change="userEnableClick($event)" /> 启 用</label> \
		  <span :class="\'crud\' + (this.enabled ? \'\' : \' disabled\')" v-if="this.setRightValue || this.setRightValue === 0">\
		  <label><input type="checkbox" name="read"   v-model="allowRead"    @change="crudClick($event)"  /> 查 询</label> \
		  <label><input type="checkbox" name="create" v-model="allowCreate"  @change="crudClick($event)"  /> 新 增</label> \
		  <label><input type="checkbox" name="update" v-model="allowUpdate"  @change="crudClick($event)"  /> 修 改</label> \
		  <label><input type="checkbox" name="delete" v-model="allowDelete"  @change="crudClick($event)" /> 删 除</label></span>\
		</div>',
	props : {
		isEnable : Boolean, // 是否启用
		setRightValue : Number,// 操作权限值
		resId : Number // 资源权限值
	},
	data (){
		return {
			enabled : this.isEnable,
			rightValue  :  this.setRightValue,
			allowRead   : (this.setRightValue & 1) === 1,
			allowCreate : (this.setRightValue & 2) === 2,
			allowUpdate : (this.setRightValue & 4) === 4,
			allowDelete : (this.setRightValue & 8) === 8
		};
	},
	mounted (){
	},
	methods : {
		toggleRight (val, right) {
			if(val === false && ((this.rightValue & right) === right)) { // 有权限
				this.rightValue -= right;
			} 
			
			if(val === true && ((this.rightValue & right) !== right)) {
				this.rightValue += right;
			}
		},
		userEnableClick(e) { // 用户点击事件，不是来自数据的变化，修改立刻被保存到服务端
//			debugger;
			var isEnable = e.target.checked, resId = this.resId, userGroupId = VUE.userGroupId; // 全局变量
			if(userGroupId && resId) {				
				aj.xhr.post('../user_group/updateResourceRightValue', json => {
					aj.msg.show(json.msg);
				}, {
					userGroupId : userGroupId,
					isEnable : isEnable,
					resId : resId
				});
			}
		}
	},
	watch : {
		isEnable() {
			this.enabled = this.isEnable;
		},
		enabled () {
			var crudInputs = this.$el.querySelectorAll('.crud input');
			for(var i = 0, j = crudInputs.length; i < j; i++)
				crudInputs[i].disabled = !this.enabled;
		},
		allowRead (val) {
			this.toggleRight(val, 1);
		},
		allowCreate (val) {
			this.toggleRight(val, 2);
		},
		allowUpdate (val) {
			this.toggleRight(val, 4);
		},
		allowDelete (val) {
			this.toggleRight(val, 8);
		}
	}
});
