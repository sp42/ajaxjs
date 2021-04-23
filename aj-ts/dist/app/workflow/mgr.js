"use strict";
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        // 定义常量
        var SELECT_MODE;
        (function (SELECT_MODE) {
            /**
             * 选中的模式，点选类型
             */
            SELECT_MODE[SELECT_MODE["POINT_MODE"] = 1] = "POINT_MODE";
            /**
             * 选中的模式，添加路径类型
             */
            SELECT_MODE[SELECT_MODE["PATH_MODE"] = 2] = "PATH_MODE";
        })(SELECT_MODE = wf.SELECT_MODE || (wf.SELECT_MODE = {}));
        /**
         * 只读模式不可编辑
         */
        wf.isREAD_ONLY = false;
        wf.DATA = {
            states: {},
            paths: {},
            JSON_DATA: {}
        };
        var name = { start: '开始节点', end: '结束节点', task: '任务节点', decision: '抉择节点', transition: '变迁路径' };
        /**
         *
         * @param cop
         */
        function setTypeName(cop) {
            // ui.PropertyForm.cop = cop;
            // ui.PropertyForm.selected = name[cop.type];
        }
        var uid = 0;
        wf.Mgr = new Vue({
            data: {
                selectedComponent: null,
                currentNode: null,
                currentMode: SELECT_MODE.POINT_MODE,
            },
            watch: {
                selectedComponent: function (newCop, old) {
                    newCop && newCop.resizeController && newCop.resizeController.showBox();
                    old && old.resizeController && old.resizeController.hideBox();
                    if (newCop instanceof aj.svg.Path)
                        newCop.show();
                    if (old instanceof aj.svg.Path)
                        old.hide();
                    if (newCop && newCop.rawData)
                        console.log(newCop.rawData);
                    // if (newCop)
                    // 	setTypeName(newCop);
                }
            },
            created: function () {
                this.allComps = {}; // key=uid, value = 图形实例 component
            },
            methods: {
                // 设置选中的组件
                setSelectedComponent: function (cop) {
                    this.selectedComponent = cop;
                },
                // 获取选中的组件
                getSelectedComponent: function () {
                    return this.selectedComponent;
                },
                // 创建下个组件的 id
                nextId: function () {
                    return ++uid;
                },
                // 登记组件
                register: function (vueObj) {
                    vueObj.id = this.nextId();
                    vueObj.svg.node.id = "ajSVG-" + vueObj.id;
                    this.allComps[vueObj.svg.node.id] = vueObj;
                },
                // 注销组件
                unregister: function (id) {
                    delete this.allComps[id];
                },
                // 清除桌布
                clearStage: function () {
                    this.setSelectedComponent(null);
                    aj.svg.PAPER.clear();
                    for (var i in wf.DATA)
                        wf.DATA[i] = {};
                }
            }
        });
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
