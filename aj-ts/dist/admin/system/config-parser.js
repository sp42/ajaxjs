"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
/*
 * 读取 json scheme 文件，解析它，渲染成为表单的 UI
 */
var aj;
(function (aj) {
    var admin;
    (function (admin) {
        var system;
        (function (system) {
            var configParser;
            (function (configParser) {
                var baseFormControl = {
                    props: {
                        configObj: { type: Object, default: function () { return {}; } }
                    }
                };
                var formGetOptions = {
                    data: function () {
                        var options = {}; // key 为 value 值，name 为显示文字
                        var option = this.configObj.option;
                        if (option) {
                            for (var i = 0, j = option.length; i < j; i++) {
                                var arr = option[i].split("=");
                                options[arr[0]] = arr[1];
                            }
                        }
                        else // 默认值的情况
                            options = { true: "是", false: "否" };
                        if (this.configObj.value) {
                            this.configObj.checked = {}; // 选中的
                            this.configObj.checked[this.configObj.value] = true;
                        }
                        return { options: options };
                    }
                };
                var shortHandsMap = {
                    text: '<aj-json-form-input-text  :config-obj="configObj" type="text"></aj-json-form-input-text>',
                    number: '<aj-json-form-input-text  :config-obj="configObj" type="number"></aj-json-form-input-text>',
                    radio: '<aj-json-form-input-radio :config-obj="configObj"></aj-json-form-input-radio>',
                    checkbox: '<aj-json-form-input-checkbox :config-obj="configObj"></aj-json-form-input-checkbox>',
                    select: '<aj-json-form-select 	   :config-obj="configObj"></aj-json-form-select>',
                    htmlEditor: '<aj-form-html-editor ref="htmlEditor" :fieldName="configObj.id"><textarea class="hide" :name="configObj.id">{{configObj.value}}</textarea></aj-form-html-editor>',
                };
                Vue.component("aj-json-form-input-text", {
                    template: html(__makeTemplateObject(["\n\t\t\t<input :type=\"type\" :name=\"configObj.id\" :value=\"configObj.value\" :placeholder=\"configObj.placeholder\"\n\t\t\t\t:size=\"configObj.size\" />"], ["\n\t\t\t<input :type=\"type\" :name=\"configObj.id\" :value=\"configObj.value\" :placeholder=\"configObj.placeholder\"\n\t\t\t\t:size=\"configObj.size\" />"])),
                    mixins: [baseFormControl],
                    props: {
                        type: { type: String, default: "text" }
                    }
                });
                Vue.component("aj-json-form-input-radio", {
                    template: html(__makeTemplateObject(["\n\t\t\t<span>\n\t\t\t\t<label v-for=\"(value, key) in options\">\n\t\t\t\t\t<input type=\"radio\" :name=\"configObj.id\" :value=\"key\" :checked=\"getChecked(key)\" /> {{value}}\n\t\t\t\t</label>\n\t\t\t</span>"], ["\n\t\t\t<span>\n\t\t\t\t<label v-for=\"(value, key) in options\">\n\t\t\t\t\t<input type=\"radio\" :name=\"configObj.id\" :value=\"key\" :checked=\"getChecked(key)\" /> {{value}}\n\t\t\t\t</label>\n\t\t\t</span>"])),
                    mixins: [baseFormControl, formGetOptions],
                    methods: {
                        getChecked: function (key) {
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
                    template: html(__makeTemplateObject(["\n\t\t\t<span>\n\t\t\t\t<input type=\"hidden\" :name=\"configObj.id\" :value=\"configObj.value\" />\n\t\t\t\t<label v-for=\"(value, key) in options\">\n\t\t\t\t\t<input type=\"checkbox\" :value=\"key\" v-model=\"checked\" /> {{value}}\n\t\t\t\t</label>\n\t\t\t</span>"], ["\n\t\t\t<span>\n\t\t\t\t<input type=\"hidden\" :name=\"configObj.id\" :value=\"configObj.value\" />\n\t\t\t\t<label v-for=\"(value, key) in options\">\n\t\t\t\t\t<input type=\"checkbox\" :value=\"key\" v-model=\"checked\" /> {{value}}\n\t\t\t\t</label>\n\t\t\t</span>"])),
                    mixins: [baseFormControl, formGetOptions],
                    data: function () {
                        return { checked: [] };
                    },
                    mounted: function () {
                        var _this = this;
                        this.configObj.option.forEach(function (i) {
                            var arr = i.split("="), key = arr[0];
                            if (_this.getChecked(key))
                                _this.checked.push(key);
                        });
                    },
                    methods: {
                        getChecked: function (_key) {
                            var v = this.configObj.value, key = Number(_key);
                            return (key & v) === key;
                        }
                    },
                    watch: {
                        checked: function (checked) {
                            var i = 0;
                            checked.forEach(function (v) { return i += Number(v); });
                            this.configObj.value = i;
                        }
                    }
                });
                Vue.component("aj-json-form-select", {
                    template: html(__makeTemplateObject(["\n\t\t\t<select :name=\"configObj.id\">\n\t\t\t\t<option v-for=\"(value, key) in options\" :value=\"key\" :selected=\"getChecked(key)\">{{value}}</option>\n\t\t\t</select>"], ["\n\t\t\t<select :name=\"configObj.id\">\n\t\t\t\t<option v-for=\"(value, key) in options\" :value=\"key\" :selected=\"getChecked(key)\">{{value}}</option>\n\t\t\t</select>"])),
                    mixins: [baseFormControl, formGetOptions],
                    methods: {
                        getChecked: function (key) {
                            if (this.configObj.value === true && key === 'true')
                                return true;
                            if (this.configObj.value === false && key === 'false')
                                return true;
                            return false;
                        }
                    }
                });
                Vue.component("aj-json-form", {
                    template: html(__makeTemplateObject(["\n\t\t\t<form method=\"POST\" action=\".\">\n\t\t\t\t<div v-for=\"control in controls\">\n\t\t\t\t\t<div class=\"label\">{{control.config.name}}</div>\n\t\t\t\t\t<div class=\"input\">\n\t\t\t\t\t\t<component v-bind:is=\"control\" :config-obj=\"control.config\"></component>\n\t\t\t\t\t\t<div class=\"sub\">{{control.config.tip}}</div>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t\t<section class=\"aj-btnsHolder\">\n\t\t\t\t\t<button> \u4FEE\u6539 </button>\n\t\t\t\t\t<button onclick=\"this.up('form').reset();return false;\">\u590D \u4F4D</button>\n\t\t\t\t</section>\n\t\t\t</form>"], ["\n\t\t\t<form method=\"POST\" action=\".\">\n\t\t\t\t<div v-for=\"control in controls\">\n\t\t\t\t\t<div class=\"label\">{{control.config.name}}</div>\n\t\t\t\t\t<div class=\"input\">\n\t\t\t\t\t\t<component v-bind:is=\"control\" :config-obj=\"control.config\"></component>\n\t\t\t\t\t\t<div class=\"sub\">{{control.config.tip}}</div>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t\t<section class=\"aj-btnsHolder\">\n\t\t\t\t\t<button> \u4FEE\u6539 </button>\n\t\t\t\t\t<button onclick=\"this.up('form').reset();return false;\">\u590D \u4F4D</button>\n\t\t\t\t</section>\n\t\t\t</form>"])),
                    props: {
                        scheme: { required: true, type: Object },
                        config: { required: true, type: Object },
                        path: { required: true, type: String }
                    },
                    data: function () {
                        return { controls: [] };
                    },
                    mounted: function () {
                        this.doRender(this.path);
                        var self = this;
                        aj.xhr.form(this.$el, undefined, {
                            beforeSubmit: function (form, json) {
                                // 同步 html editor
                                self.$children.forEach(function (i) {
                                    var editor = i.$refs.htmlEditor;
                                    if (editor) {
                                        console.log(i.$refs.htmlEditor);
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
                        doRender: function (path) {
                            var node = aj.tree.findNodesHolder(this.scheme, path.split("."));
                            if (node) {
                                // console.log(path)
                                // console.log(node)
                                for (var i in node) {
                                    var control = node[i];
                                    if (!control.name) { // 如果没有 name 表示这是一个父亲节点
                                        this.doRender(path + "." + i);
                                        continue;
                                    }
                                    control.id = path + "." + i;
                                    var value = aj.tree.findNodesHolder(this.config, path.split(".")) || {}; // 找到配置值
                                    control.value = value[i] || "";
                                    var ui = control.ui || control.type || 'text', template = shortHandsMap[ui] || "<div>\u627E\u4E0D\u5230\u5BF9\u5E94\u7684 " + ui + " \u7EC4\u4EF6</div>";
                                    if (typeof (template) === 'function')
                                        template = template(control);
                                    this.controls.push({ config: control, mixins: [baseFormControl], template: template });
                                }
                            }
                        }
                    }
                });
            })(configParser = system.configParser || (system.configParser = {}));
        })(system = admin.system || (admin.system = {}));
    })(admin = aj.admin || (aj.admin = {}));
})(aj || (aj = {}));
