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
                var id = "ajSVG-" + comp.id;
                comp.svg.node.id = id;
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
        /**
         * Main 程序
         */
        var Main = /** @class */ (function () {
            function Main() {
                this.selectedComponent = null;
            }
            /**
             * 设置选中的组件
             *
             * @param cop
             */
            Main.prototype.setSelectedComponent = function (cop) {
                this.selectedComponent = cop;
            };
            /**
             * 获取选中的组件
             *
             * @returns
             */
            Main.prototype.getSelectedComponent = function () {
                return this.selectedComponent;
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
            return Main;
        }());
        // @ts-ignore
        Main.prototype = new Vue({
            data: {
                selectedComponent: null,
                currentNode: null,
                currentMode: SELECT_MODE.POINT_MODE,
            },
            watch: {
                selectedComponent: function (newCop, old) {
                    // newCop && newCop.resizeController && newCop.resizeController.showBox();
                    // old && old.resizeController && old.resizeController.hideBox();
                    // if (newCop instanceof svg.Path)
                    //     newCop.show();
                    // if (old instanceof svg.Path)
                    //     old.hide();
                    if (newCop && newCop.rawData)
                        console.log(newCop.rawData);
                    // if (newCop)
                    // 	setTypeName(newCop);
                }
            }
        });
        wf.main = new Main();
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
