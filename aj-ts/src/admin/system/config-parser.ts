/*
 * 读取 json scheme 文件，解析它，渲染成为表单的 UI 
 */
namespace aj.admin.system.configParser {
	/**
	 * 说明配置的节点
	 */
	interface ConfigScheme {
		/**
		 * 完整 JSONPath 路径
		 */
		id: string;

		/**
		 * 配置名称
		 */
		name: string;

		/**
		 * 配置说明
		 */
		tip?: string;

		/**
		 * 配置值的类型
		 */
		type?: "boolean" | "string" | "number";

		/**
		 * 渲染的控件类型
		 */
		ui?: "text" | "input_text" | "textarea" | "radio" | "htmlEditor";

		value: any;
	}

	/**
	 * 总体的控件
	 */
	interface JsonForm extends Vue {
		/**
		 *  输入的配置规则，配置说明的 JSON Tree
		 */
		scheme: tree.JsonMap<ConfigScheme>;

		/**
		 * 输入的配置数据
		 */
		config: JsonParam;

		/**
		 * 访问节点的路径
		 */
		path: string;

		/**
		 * 渲染
		 * 
		 * @param path 
		 */
		doRender(path: string): void;

		controls: [];
	}

	interface Editor {
		configObj: JsonParam;
	}

	let baseFormControl = {
		props: {
			configObj: { type: Object, default() { return {}; } }
		}
	};

	let formGetOptions = {
		data(this: Editor) {
			let options: { [key: string]: string } = {}, // key 为 value 值，name 为显示文字
				option: string[] = <string[]>this.configObj.option;

			if (option) {
				for (let i = 0, j = option.length; i < j; i++) {
					let arr: string[] = option[i].split("=");
					options[arr[0]] = arr[1];
				}
			} else  // 默认值的情况
				options = { true: "是", false: "否" };

			if (this.configObj.value) {
				this.configObj.checked = {}; // 选中的
				this.configObj.checked[<string>this.configObj.value] = true;
			}

			return { options: options };
		}
	};

	let shortHandsMap: JsonParam = {
		text: '<aj-json-form-input-text  :config-obj="configObj" type="text"></aj-json-form-input-text>',
		number: '<aj-json-form-input-text  :config-obj="configObj" type="number"></aj-json-form-input-text>',
		radio: '<aj-json-form-input-radio :config-obj="configObj"></aj-json-form-input-radio>',
		checkbox: '<aj-json-form-input-checkbox :config-obj="configObj"></aj-json-form-input-checkbox>',
		select: '<aj-json-form-select 	   :config-obj="configObj"></aj-json-form-select>',
		htmlEditor: '<aj-form-html-editor ref="htmlEditor" :fieldName="configObj.id"><textarea class="hide" :name="configObj.id">{{configObj.value}}</textarea></aj-form-html-editor>',
	};

	Vue.component("aj-json-form-input-text", {
		template: html`
			<input :type="type" :name="configObj.id" :value="configObj.value" :placeholder="configObj.placeholder"
				:size="configObj.size" />`,
		mixins: [baseFormControl],
		props: {
			type: { type: String, default: "text" }
		}
	});

	Vue.component("aj-json-form-input-radio", {
		template: html`
			<span>
				<label v-for="(value, key) in options">
					<input type="radio" :name="configObj.id" :value="key" :checked="getChecked(key)" /> {{value}}
				</label>
			</span>`,
		mixins: [baseFormControl, formGetOptions],
		methods: {
			getChecked(this: Editor, key: any): boolean {
				/*			if(this.configObj.id =='forDelevelopers.enableWebSocketLogOutput')
							debugger;*/
				//console.log(":::::::::::"+this.configObj.value)
				if (this.configObj.value === true && key === 'true')
					return true;
				if (!this.configObj.value && key === 'false')
					return true;

				return false;
			}
		}
	});

	Vue.component("aj-json-form-input-checkbox", {
		template: html`
			<span>
				<input type="hidden" :name="configObj.id" :value="configObj.value" />
				<label v-for="(value, key) in options">
					<input type="checkbox" :value="key" v-model="checked" /> {{value}}
				</label>
			</span>`,
		mixins: [baseFormControl, formGetOptions],
		data() {
			return { checked: [] };
		},
		mounted(): void {
			this.configObj.option.forEach((i: string) => {
				let arr: string[] = i.split("="),
					key: string = arr[0];

				if (this.getChecked(key))
					this.checked.push(key)
			});
		},
		methods: {
			getChecked(this: Editor, _key: string): boolean {
				let v = this.configObj.value,
					key: number = Number(_key);

				// @ts-ignore
				return (key & v) === key;
			}
		},
		watch: {
			checked(this: Editor, checked: string[]): void {
				let i: number = 0;
				checked.forEach((v: string) => i += Number(v));

				this.configObj.value = i;
			}
		}
	});

	Vue.component("aj-json-form-select", {
		template: html`
			<select :name="configObj.id">
				<option v-for="(value, key) in options" :value="key" :selected="getChecked(key)">{{value}}</option>
			</select>`,
		mixins: [baseFormControl, formGetOptions],
		methods: {
			getChecked(this: Editor, key: string): boolean {
				if (this.configObj.value === true && key === 'true')
					return true;
				if (this.configObj.value === false && key === 'false')
					return true;

				return false;
			}
		}
	});

	Vue.component("aj-json-form", {
		template: html`
			<form method="POST" action=".">
				<div v-for="control in controls">
					<div class="label">{{control.config.name}}</div>
					<div class="input">
						<component v-bind:is="control" :config-obj="control.config"></component>
						<div class="sub">{{control.config.tip}}</div>
					</div>
				</div>
				<section class="aj-btnsHolder">
					<button> 修改 </button>
					<button onclick="this.up('form').reset();return false;">复 位</button>
				</section>
			</form>`,
		props: {
			scheme: { required: true, type: Object },// 输入 JSON 数据规则
			config: { required: true, type: Object },// 输入 JSON 数据
			path: { required: true, type: String }
		},
		data() {
			return { controls: [] };
		},
		mounted(this: JsonForm): void {
			this.doRender(this.path);
			let self: JsonForm = this;

			xhr.form(<HTMLFormElement>this.$el, undefined, {
				beforeSubmit(form: HTMLFormElement, json: StringJsonParam): boolean {
					// 同步 html editor
					self.$children.forEach(i => {
						let editor = i.$refs.htmlEditor;

						if (editor) {
							console.log(i.$refs.htmlEditor)
							editor.setMode();
							json[editor.$props.fieldName] = editor.getValue();
							editor.setMode();
						}
					});

					return true;
				}
			});
		},
		methods: {
			doRender(this: JsonForm, path: string): void {
				let node: tree.JsonMap<ConfigScheme> = <tree.JsonMap<ConfigScheme>>tree.findNodesHolder(this.scheme, path.split("."));

				if (node) {
					// console.log(path)
					// console.log(node)
					for (let i in node) {
						let control: ConfigScheme = <ConfigScheme>node[i];

						if (!control.name) { // 如果没有 name 表示这是一个父亲节点
							this.doRender(path + "." + i);
							continue;
						}

						control.id = path + "." + i;

						let value: any = tree.findNodesHolder(this.config, path.split(".")) || {}; // 找到配置值
						control.value = value[i] || "";

						let ui = control.ui || control.type || 'text',
							template: string = <string>shortHandsMap[ui] || `<div>找不到对应的 ${ui} 组件</div>`;

						if (typeof (template) === 'function')
							template = (<Function>template)(control);

						// @ts-ignore
						this.controls.push({ config: control, mixins: [baseFormControl], template: template });
					}
				}
			}
		}
	});
}