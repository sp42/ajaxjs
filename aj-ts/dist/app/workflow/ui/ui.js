"use strict";
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        setTimeout(function () {
            var el = document.body.$(".canvas");
            //@ts-ignore
            aj.svg.PAPER = window.PAPER = Raphael(el, el.clientWidth, el.clientHeight);
            // @ts-ignore
            init(TEST_DATA);
            aj.wysiwyg.statusBar.showMsg('欢迎来到 工作流流程设计器');
        }, 800);
        /**
         * JSON 渲染为图形
         *
         * @param data
         */
        function init(data) {
            var states = wf.DATA.STATES;
            for (var i in data.states) {
                var stateData = data.states[i], state = void 0;
                switch (stateData.type) { // 生成 box
                    case 'start':
                    case 'decision':
                    case 'end':
                        state = new wf.ImageState(aj.svg.PAPER, i, stateData);
                        break;
                    default:
                        state = new wf.State(aj.svg.PAPER, i, stateData);
                }
                if (wf.isREAD_ONLY)
                    state.resize = state.isDrag = false;
                states[i] = state;
            }
            // 连线
            var paths = wf.DATA.PATHS;
            for (var i in data.paths) {
                var pathData = data.paths[i], from = states[pathData.from], to = states[pathData.to], path = new aj.svg.Path(i, from.svg, to.svg, pathData);
                paths[i] = path;
                pathData.dots && pathData.dots.length && path.restore(pathData.dots);
            }
        }
        // 菜单选中的
        document.body.$('.components ul li.selectable', function (li) {
            li.onclick = function (ev) {
                var el = ev.target, selected = el.parentNode.$(aj.SELECTED_CSS);
                if (selected)
                    selected.classList.remove(aj.SELECTED);
                el.classList.add(aj.SELECTED);
                // 切换模式
                if (el.classList.contains('pointer'))
                    wf.main.currentMode = wf.SELECT_MODE.POINT_MODE;
                if (el.classList.contains('path'))
                    wf.main.currentMode = wf.SELECT_MODE.PATH_MODE;
            };
        });
        document.addEventListener('click', function (ev) {
            var el = ev.target, 
            // @ts-ignore
            isSVGAElement = !!el.ownerSVGElement; // 点击页面任何一个元素，若为 SVG 且是组件，使其选中的状态
            if (isSVGAElement && el.id.indexOf('ajComp') != -1) {
                var component = wf.DATA.ALL_COMPS[el.id];
                if (!component)
                    throw '未登记组件 ' + el.id;
                wf.main.setSelectedComponent(component);
            }
        });
        /**
         * 删除： 删除状态时，触发removerect事件，连接在这个状态上当路径监听到这个事件，触发removepath删除自身；
         * 删除路径时，触发removepath事件
         */
        document.addEventListener('keydown', function (ev) {
            if (wf.isREAD_ONLY)
                return;
            // 键盘删除节点
            if (ev.keyCode == 46 && wf.main.vue.selectedComponent) {
                wf.main.selectedComponent.remove();
                wf.main.selectedComponent = null;
            }
        });
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
