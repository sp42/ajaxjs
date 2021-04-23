"use strict";
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        var imgBox = function (img, size) { return aj.apply({ src: "../asset/images/workflow/" + img }, size); };
        /**
         * JSON 渲染为图形
         *
         * @param data
         */
        function init(data) {
            var states = wf.DATA.states;
            for (var i in data.states) {
                var state = data.states[i], box = void 0;
                switch (state.type) { // 生成 box
                    case 'start':
                        box = createRect(imgBox('start.png', state.attr), 'img');
                        break;
                    case 'decision':
                        box = createRect(imgBox('decision.png', state.attr), 'img');
                        break;
                    case 'end':
                        box = createRect(imgBox('end.png', state.attr), 'img');
                        break;
                    default:
                        box = createRect(state.attr);
                        box.text = state.props.displayName;
                }
                box.type = state.type;
                box.ref = i;
                box.rawData = state;
                box.init();
                states[i] = box;
            }
            // 连线
            var paths = wf.DATA.paths;
            for (var i in data.paths) {
                var pathCfg = data.paths[i], from = states[pathCfg.from], to = states[pathCfg.to], path = new aj.svg.Path(from.svg, to.svg, pathCfg);
                path.id = i;
                // path.wfData = pathCfg.props;
                paths[i] = path;
                pathCfg.dots && pathCfg.dots.length && path.restore(pathCfg.dots);
            }
        }
        wf.init = init;
        /**
         * 创建图形基类的工厂函数
         *
         * @param box
         * @param type
         */
        function createRect(box, type) {
            var raphaelObj;
            switch (type) {
                case 'img':
                    raphaelObj = aj.svg.PAPER.image().attr(box).addClass('baseImg');
                    break;
                default:
                    raphaelObj = aj.svg.PAPER.rect().attr(box).attr({ fill: "90-#fff-#F6F7FF" }).addClass('rectBaseStyle');
            }
            var vueObj = new Vue({
                mixins: [aj.svg.BaseRect],
                data: { vBox: box }
            });
            vueObj.PAPER = aj.svg.PAPER;
            vueObj.svg = raphaelObj;
            // 登记注册
            wf.Mgr.register(vueObj);
            if (type == 'img')
                vueObj.resize = false; // 图片禁止放大缩小
            if (wf.isREAD_ONLY)
                vueObj.resize = vueObj.isDrag = false;
            return vueObj;
        }
        setTimeout(function () {
            var el = document.body.$(".canvas");
            //@ts-ignore
            aj.svg.PAPER = window.PAPER = Raphael(el, el.clientWidth, el.clientHeight);
            // @ts-ignore
            init(TEST_DATA);
            aj.wysiwyg.statusBar.showMsg('欢迎来到 工作流流程设计器');
        }, 800);
        // 菜单选中的
        document.body.$('.components ul li.selectable', function (li) {
            li.onclick = function (ev) {
                var el = ev.target, selected = el.parentNode.$(aj.SELECTED_CSS);
                if (selected)
                    selected.classList.remove(aj.SELECTED);
                el.classList.add(aj.SELECTED);
                // 切换模式
                if (el.classList.contains('pointer'))
                    wf.Mgr.currentMode = wf.SELECT_MODE.POINT_MODE;
                if (el.classList.contains('path'))
                    wf.Mgr.currentMode = wf.SELECT_MODE.PATH_MODE;
            };
        });
        document.addEventListener('click', function (ev) {
            var el = ev.target, 
            // @ts-ignore
            isSVGAElement = !!el.ownerSVGElement; // 点击页面任何一个元素，若为 SVG 且是组件，使其选中的状态
            if (isSVGAElement && el.id.indexOf('ajSVG') != -1) {
                //@ts-ignore
                var component = wf.Mgr.allComps[el.id];
                if (!component)
                    throw '未登记组件 ' + el.id;
                wf.Mgr.setSelectedComponent(component);
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
            if (ev.keyCode == 46 && wf.Mgr.selectedComponent) {
                wf.Mgr.selectedComponent.remove();
                wf.Mgr.selectedComponent = null;
            }
        });
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
