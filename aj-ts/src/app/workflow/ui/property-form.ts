namespace aj.wf.ui {
    // 表单通用属性
    let propertyForm = {
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
        mounted() {
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
        mounted() {
            // debugger;// transition 连线 expr 属性在 json 里面丢失
        }
    });

    interface PropertyForm extends Vue {
        /**
         * 
         */
        selected: string;

        /**
         * 
         */
        cop: SvgComp;
    }

    export var PropertyForm: PropertyForm = <PropertyForm>new Vue({
        el: '.property',
        data: {
            selected: '', 	// 组件类型
            cop: null		// 组件
        },
        methods: {
            show() {
                return aj.svg.Mgr.selectedComponent != null;
            }
        },
        computed: {
            currentForm(this: PropertyForm) {
                if (!this.cop)
                    return '';

                return 'aj-wf-' + this.cop.type + '-form';
            }
        }
    });
}