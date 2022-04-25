/*
 * 读取 json scheme 文件，解析它，渲染成为表单的 UI 
 */

aj.baseFormControl = {
	props: {
		configObj: {
			type: Object, default() { return {};}
		}
	}	
};

aj.formGetOptions = {
	data() {
		var options = {}; // key 为 value 值，name 为显示文字
		var option = this.configObj.option;
		
		if(option) {
			for(var i = 0, j = option.length; i < j; i++) {
				var arr = option[i].split("=");
				options[arr[0]] = arr[1];
			}
		} else  // 默认值的情况
			options = {true: "是", false: "否"};

		if(this.configObj.value) {			
			this.configObj.checked = {}; // 选中的
			this.configObj.checked[this.configObj.value] = true;
		}
		
		return {options: options};
	}		
};

aj.shortHandsMap = {
	text: 		'<aj-json-form-input-text  :config-obj="configObj" type="text"></aj-json-form-input-text>',
	number:		'<aj-json-form-input-text  :config-obj="configObj" type="number"></aj-json-form-input-text>',
	radio: 		'<aj-json-form-input-radio :config-obj="configObj"></aj-json-form-input-radio>',
	checkbox: 	'<aj-json-form-input-checkbox :config-obj="configObj"></aj-json-form-input-checkbox>',
	select: 	'<aj-json-form-select 	   :config-obj="configObj"></aj-json-form-select>',
	htmlEditor: '<aj-form-html-editor ref="htmlEditor" :fieldName="configObj.id"><textarea class="hide" :name="configObj.id">{{configObj.value}}</textarea></aj-form-html-editor>',
};

Vue.component("aj-json-form-input-text", {
	template: '<input :type="type" :name="configObj.id" :value="configObj.value" :placeholder="configObj.placeholder" :size="configObj.size" />',
	mixins: [aj.baseFormControl],
	props: {
		type: {
			default: "text", type: String
		}
	}
});

Vue.component("aj-json-form-input-radio", {
	template: '<span><label v-for="(value, key) in options"><input type="radio" :name="configObj.id" :value="key" :checked="getChecked(key)" /> {{value}} </label> </span>',
	mixins: [aj.baseFormControl, aj.formGetOptions],
	methods: {
		getChecked(key) {
/*			if(this.configObj.id =='forDelevelopers.enableWebSocketLogOutput')
			debugger;*/
			//console.log(":::::::::::"+this.configObj.value)
			if(this.configObj.value === true && key === 'true')
				return true;
			if(!this.configObj.value && key === 'false')
				return true;
			
			return false;
		}
	}
});

Vue.component("aj-json-form-input-checkbox", {
	template: '<span>\
		<input type="hidden" :name="configObj.id" :value="configObj.value" />\
		<label v-for="(value, key) in options"><input type="checkbox" :value="key" v-model="checked" /> {{value}} </label> </span>',
	mixins: [aj.baseFormControl, aj.formGetOptions],
	data(){
		return {checked: []};
	},
	mounted() {
		this.configObj.option.forEach(i => {
			var arr = i.split("="), key = arr[0];
			
			if(this.getChecked(key))
				this.checked.push(key)
		});
		
	},
	methods: {
		getChecked(key) {
			var v = this.configObj.value;
			key = Number(key);
			
			return (key & v) === key;
		}
	},
	watch: {
		checked(checked) {
			var i = 0;
			checked.forEach(v => {
				i += Number(v);
			});
			
			this.configObj.value = i;
		}
	}
});

Vue.component("aj-json-form-select", {
	template: '<select :name="configObj.id"><option v-for="(value, key) in options" :value="key" :selected="getChecked(key)">{{value}}</option></select>',
	mixins: [aj.baseFormControl, aj.formGetOptions],
	methods: {
		getChecked(key) {
			if(this.configObj.value === true && key === 'true')
				return true;
			if(this.configObj.value === false && key === 'false')
				return true;
			
			return false;
		}
	}
});

Vue.component("aj-json-form", {
	template:
		'<form method="POST" action="."><div v-for="control in controls">\
			<div class="label">{{control.config.name}}</div>\
				<div class="input">\
					<component v-bind:is="control" :config-obj="control.config"></component>\
					<div class="sub">{{control.config.tip}}</div>\
				</div>\
			</div>\
			<section class="aj-btnsHolder">\
				<button>\
					<img :src="ajResources.commonAsset + \'/icon/save.gif\'" /> 修改\
				</button>\
				<button onclick="this.up(\'form\').reset();return false;">复 位</button>\
			</section>\
		</form>',
	props: {
		scheme: {					// 输入 JSON 数据规则
			required: true, type: Object
		},
		config: {					// 输入 JSON 数据
			required: true, type: Object
		},
		path: {
			required: true, type: String
		}
	},
	data() {
		return {controls: []};
	},
	mounted(){
		this.doRender(this.path);
		var self = this;
		
		aj.xhr.form(this.$el, null, { beforeSubmit(form, json) {
				// 同步 html editor
				self.$children.forEach(i => {
					var editor = i.$refs.htmlEditor;
					
					if(editor) {
						console.log(i.$refs.htmlEditor)
						editor.setMode();
						json[editor.$props.fieldName] = editor.getValue();
						editor.setMode();
					}
				});
			}
		});
	},
	methods: {
		doRender(path) {
			var node = this.findNode(this.scheme, path.split("."));
			
			for(var i in node) {
				var control = node[i];
				
				if(!control.name) { // 如果没有 name 表示这是一个父亲节点
					this.doRender(path + "." + i);
					continue;
				} 
				
				control.id = path + "." + i;
				
				var value = this.findNode(this.config, path.split(".")) || {};
				control.value = value[i] || "";
				
				var ui = control.ui || control.type || 'text';
				var template = aj.shortHandsMap[ui] || '<div>找不到对应的 ' + ui +' 组件</div>';
				
				if(typeof(template) === 'function') 
					template = template(control);
		
				this.controls.push({config: control, mixins: [aj.baseFormControl], template: template});
			}
		},
		findNode(obj, queen) {
			if(!queen.shift) 
				return null;
			
			var first = queen.shift();
 
			for(var i in obj) {
				if(i === first) {
					var target = obj[i];
					
					if(queen.length == 0) // 找到了
						return target;
					 else 
						return arguments.callee(obj[i], queen);
				}
			}
		},
	}
});

