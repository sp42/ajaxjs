"use strict";
setTimeout(function () {
    PAPER = aj.svg.Mgr.initSVG(aj(".canvas"));
    //MyBOX = PAPER.rect().attr( {x: 50, y: 20, width: 500, height: 200, fill: "90-#fff-#F6F7FF"} );
    // vueObj1 = aj.svg.createBaseComponent(PAPER, {x: 50, y: 20, width: 500, height: 200, fill: "90-#fff-#F6F7FF"});
    //vueObj1.isDrag = false;
    /*	vueObj1.init();
        
        console.assert(vueObj1.id);
    
        vueObj2 = aj.svg.createBaseComponent(PAPER, {x: 50, y: 590, width: 300, height: 100, fill: "90-#fff-#F6F7FF"});
        vueObj2.text = "Hello World!";
        vueObj2.init();
        setTimeout(()=>vueObj2.pos(100, 600), 1000);
        
        PATH = new aj.svg.Path(vueObj1.svg, vueObj2.svg, 'text');
        PATH.restore([{x:306,y:363},{x:450,y:442}]);
    
        vueObj3 = aj.svg.createBaseComponent(PAPER, {x: 110, y: 369, width: 48, height: 48, src: "../asset/images/workflow/start.png"}, 'img');
        vueObj3.init();
    
        PATH2 = new aj.svg.Path(vueObj3.svg, vueObj1.svg);*/
    aj.svg.startup.init(TEST_DATA);
}, 800);
aj.svg.startup = {
    init: function (data) {
        // 生成 box
        var imgBox = function (img, size) { return aj.apply({ src: "../../asset/images/workflow/" + img }, size); };
        var states = aj.wf.data.states, paths = aj.workflow.data.paths;
        for (var i in data.states) {
            var state = data.states[i];
            var box;
            switch (state.type) { // 
                case 'start':
                    box = aj.svg.createRect(PAPER, imgBox('start.png', state.attr), 'img');
                    break;
                case 'decision':
                    box = aj.svg.createRect(PAPER, imgBox('decision.png', state.attr), 'img');
                    break;
                case 'end':
                    box = aj.svg.createRect(PAPER, imgBox('end.png', state.attr), 'img');
                    break;
                default:
                    box = aj.svg.createRect(PAPER, state.attr);
                    box.text = state.props.displayName;
            }
            box.type = state.type;
            box.id = i;
            box.rawData = state;
            box.wfData = state.props;
            box.init();
            states[i] = box;
        }
        // 连线
        for (var i in data.paths) {
            var pathCfg = data.paths[i];
            var from = states[pathCfg.from], to = states[pathCfg.to];
            var path = new aj.svg.Path(from.svg, to.svg, pathCfg.text.text);
            path.id = i;
            path.rawData = pathCfg;
            path.wfData = pathCfg.props;
            paths[i] = path;
            if (pathCfg.dots && pathCfg.dots.length)
                path.restore(pathCfg.dots);
        }
    }
};
// 菜单选中的
aj('.components ul li.selectable', function (li) {
    li.onclick = function (e) {
        var el = e.target;
        var selected = el.parentNode.$('.selected');
        if (selected)
            selected.classList.remove('selected');
        el.classList.add('selected');
        // 切换模式
        if (el.classList.contains('pointer'))
            aj.svg.Mgr.currentMode = aj.workflow.POINT_MODE;
        if (el.classList.contains('path'))
            aj.svg.Mgr.currentMode = aj.workflow.PATH_MODE;
    };
});
// 表单通用属性
aj.workflow.propertyForm = {
    props: {
        cop: Object
    }
};
Vue.component('aj-wf-start-form', {
    template: aj('.startEndForm'),
    mixins: [aj.workflow.propertyForm]
});
Vue.component('aj-wf-end-form', {
    template: aj('.startEndForm'),
    mixins: [aj.workflow.propertyForm]
});
Vue.component('aj-wf-task-form', {
    template: aj('.taskForm'),
    mixins: [aj.workflow.propertyForm]
});
Vue.component('aj-wf-custom-form', {
    template: aj('.startForm'),
    mixins: [aj.workflow.propertyForm]
});
Vue.component('aj-wf-subprocess-form', {
    template: aj('.startForm'),
    mixins: [aj.workflow.propertyForm]
});
Vue.component('aj-wf-decision-form', {
    template: aj('.decisionForm'),
    mixins: [aj.workflow.propertyForm],
    mounted: function () {
        // debugger;
    }
});
Vue.component('aj-wf-fork-form', {
    template: aj('.startForm'),
    mixins: [aj.workflow.propertyForm]
});
Vue.component('aj-wf-join-form', {
    template: aj('.startForm'),
    mixins: [aj.workflow.propertyForm]
});
Vue.component('aj-wf-transition-form', {
    template: aj('.transitionForm'),
    mixins: [aj.workflow.propertyForm],
    mounted: function () {
        // debugger;// transition 连线 expr 属性在 json 里面丢失
    }
});
aj.workflow.propertyEditor = new Vue({
    el: '.property',
    data: {
        selected: '',
        cop: null // 组件
    },
    methods: {
        show: function () {
            return aj.svg.Mgr.selectedComponent != null;
        }
    },
    computed: {
        currentForm: function () {
            if (!this.cop)
                return '';
            return 'aj-wf-' + this.cop.type + '-form';
        }
    }
});
// 流程定义列表对话框
aj.workflow.openDlg = new Vue({
    el: '.openDlg',
    methods: {
        show: function () {
            this.$refs.layer.show();
            this.$refs.layer.$children[0].ajaxGet();
        },
        open: function (id) {
            aj.svg.Mgr.clearStage();
            // 定义实体
            aj.xhr.get(this.ajResources.ctx + '/admin/workflow/process/' + id + '/', function (j) {
                var result = j.result;
                aj.workflow.defInfo.info = result;
                aj.workflow.defInfo.isCreate = false;
                aj('.defName').innerHTML = result.displayName;
            });
            // 定义 json
            aj.xhr.get(this.ajResources.ctx + '/admin/workflow/process/getJson/' + id + '/', function (j) {
                aj.wf.data.JSON_DATA = j;
                aj.svg.startup.init(j);
            });
            this.$refs.layer.close();
            FB.statusBar.showMsg("打开工作流流程成功，读取流程定义成功");
        }
    }
});
// 流程定义
aj.workflow.defInfo = new Vue({
    el: '.defInfo',
    data: {
        info: {},
        isCreate: false
    },
    methods: {
        saveOrUpdate: function () {
            this.toXML();
        },
        toXML: function () {
            var xml = ["<process "];
            var clearInfo = { name: this.info.name, displayName: this.info.displayName, instanceUrl: this.info.instanceUrl, expireTime: this.info.expireTime };
            // 流程定义的属性
            for (var attrib in clearInfo) {
                var value = clearInfo[attrib];
                if ((attrib == "name" || attrib == "displayName") && !value) {
                    aj.alert("流程定义名称、显示名称不能为空");
                    return;
                }
                if (value)
                    xml.push(' ' + attrib + '="' + value + '"');
            }
            xml.push(">\n");
            var arr = [], node, states = aj.workflow.data.states, paths = aj.workflow.data.paths, pathToXml = aj.svg.serialize.path.toXml;
            for (var i in states) {
                node = states[i];
                if (!node)
                    continute;
                xml.push(aj.svg.serialize.rect.toBeforeXml(node));
                for (var i2 in paths) {
                    var transition = paths[i2];
                    if (!transition)
                        continute;
                    if (node.id == transition.from().vue.id) {
                        var transitionXml = pathToXml(transition);
                        if (!transitionXml) {
                            aj.alert("连接线名称不能为空");
                            return;
                        }
                        else {
                            arr.push(transition.id); // arr 用于判别是否重复
                            xml.push("\n" + transitionXml);
                        }
                    }
                }
                xml.push("\n </" + node.type + ">\n");
            }
            arr = arr.sort();
            for (var i = 0; i < arr.length; i++) {
                if (arr[i] == arr[i + 1]) {
                    aj.alert("连接线名称不能重复[" + arr[i] + "]");
                    return;
                }
            }
            xml.push("</process>");
            console.log(xml.join(''));
            alert(xml.join(''));
        }
    }
});
