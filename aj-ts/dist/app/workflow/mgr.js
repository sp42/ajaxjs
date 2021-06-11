"use strict";
/**
 * 组件管理器
 */
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        var ComMgr;
        (function (ComMgr) {
            /**
             * id 记数器
             */
            var uid = 0;
            /**
             * 生成下一个 id
             *
             * @returns 下一个 id
             */
            function nextId() {
                return ++uid;
            }
            ComMgr.nextId = nextId;
            /**
             * 登记组件
             *
             * @param vueObj
             */
            function register(comp) {
                var id = "ajComp-" + comp.id;
                // comp.svg.node.id = id;
                var w = comp;
                w.svg.node.id = id;
                wf.DATA.ALL_COMPS[id] = comp;
            }
            ComMgr.register = register;
            /**
             * 注销组件
             *
             * @param id
             */
            function unregister(id) {
                delete wf.DATA.ALL_COMPS[id];
            }
            ComMgr.unregister = unregister;
        })(ComMgr = wf.ComMgr || (wf.ComMgr = {}));
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
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
            STATES: {},
            PATHS: {},
            ALL_COMPS: {},
            JSON_DATA: {}
        };
        var MAIN_DATA = {
            selectedComponent: null,
            currentNode: null,
            currentMode: SELECT_MODE.POINT_MODE,
        };
        /**
         * Main 程序
         */
        var Main = /** @class */ (function () {
            function Main() {
                /**
                 * 无 UI Vue 实例
                 */
                this.vue = new Vue({
                    data: MAIN_DATA,
                    watch: {
                        selectedComponent: function (newCop, old) {
                            wf.main.updateSelectedComponent(newCop, old);
                        }
                    }
                });
            }
            /**
             * 设置选中的组件
             *
             * @param cop
             */
            Main.prototype.setSelectedComponent = function (cop) {
                MAIN_DATA.selectedComponent = cop;
            };
            /**
             * 获取选中的组件
             *
             * @returns
             */
            Main.prototype.getSelectedComponent = function () {
                return MAIN_DATA.selectedComponent;
            };
            /**
             * 清除桌布
             */
            Main.prototype.clearStage = function () {
                this.setSelectedComponent(null);
                aj.svg.PAPER.clear();
                for (var i in wf.DATA)
                    // @ts-ignore
                    wf.DATA[i] = {};
            };
            Main.prototype.updateSelectedComponent = function (newCop, old) {
                var _a, _b;
                if (newCop) {
                    if (newCop.hasOwnProperty('resizeController')) {
                        var state = newCop;
                        (_a = state.resizeController) === null || _a === void 0 ? void 0 : _a.showBox();
                    }
                    if (newCop instanceof aj.svg.Path)
                        newCop.show();
                    setTypeName(newCop);
                    if (newCop.rawData)
                        console.log(newCop.rawData);
                }
                if (old) {
                    if (old.hasOwnProperty('resizeController')) {
                        var state = old;
                        (_b = state.resizeController) === null || _b === void 0 ? void 0 : _b.hideBox();
                    }
                    if (old instanceof aj.svg.Path)
                        old.hide();
                }
            };
            return Main;
        }());
        /**
         *
         * @param cop
         */
        function setTypeName(cop) {
            // ui.PropertyForm.cop = cop;
            // ui.PropertyForm.selected = name[cop.type];
        }
        // 单例
        wf.main = new Main();
        var name = { start: '开始节点', end: '结束节点', task: '任务节点', decision: '抉择节点', transition: '变迁路径' };
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
