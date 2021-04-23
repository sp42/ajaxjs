"use strict";
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        var ui;
        (function (ui) {
            // 表单通用属性
            var propertyForm = {
                props: {
                    cop: Object
                }
            };
            Vue.component('aj-wf-start-form', {
                template: document.body.$('.startEndForm'),
                mixins: [propertyForm]
            });
            Vue.component('aj-wf-end-form', {
                template: document.body.$('.startEndForm'),
                mixins: [propertyForm]
            });
            Vue.component('aj-wf-task-form', {
                template: document.body.$('.taskForm'),
                mixins: [propertyForm]
            });
            Vue.component('aj-wf-custom-form', {
                template: document.body.$('.startForm'),
                mixins: [propertyForm]
            });
            Vue.component('aj-wf-subprocess-form', {
                template: document.body.$('.startForm'),
                mixins: [propertyForm]
            });
            Vue.component('aj-wf-decision-form', {
                template: document.body.$('.decisionForm'),
                mixins: [propertyForm],
                mounted: function () {
                    // debugger;
                }
            });
            Vue.component('aj-wf-fork-form', {
                template: document.body.$('.startForm'),
                mixins: [propertyForm]
            });
            Vue.component('aj-wf-join-form', {
                template: document.body.$('.startForm'),
                mixins: [propertyForm]
            });
            Vue.component('aj-wf-transition-form', {
                template: document.body.$('.transitionForm'),
                mixins: [propertyForm],
                mounted: function () {
                    // debugger;// transition 连线 expr 属性在 json 里面丢失
                }
            });
            // export let PropertyForm: PropertyForm = <PropertyForm>new Vue({
            //     el: '.property',
            //     data: {
            //         selected: '', 	// 组件类型
            //         cop: null		// 组件
            //     },
            //     methods: {
            //         show(): boolean {
            //             return Mgr.selectedComponent != null;
            //         }
            //     },
            //     computed: {
            //         currentForm(this: PropertyForm): string {
            //             if (!this.cop)
            //                 return '';
            //             return `aj-wf-${this.cop.type}-form`;
            //         }
            //     }
            // });
        })(ui = wf.ui || (wf.ui = {}));
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
