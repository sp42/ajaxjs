"use strict";
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        var ui;
        (function (ui) {
            var imgBox = function (img, size) { return aj.apply({ src: "../../asset/images/workflow/" + img }, size); };
            function init(data) {
                // 生成 box
                var states = aj.wf.data.states, paths = aj.workflow.data.paths;
                for (var i in data.states) {
                    var state = data.states[i];
                    var box = void 0;
                    switch (state.type) { // 
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
            ui.init = init;
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
                var vueObj = new Vue({ mixins: [aj.svg.BaseRect], data: { vBox: box } });
                vueObj.PAPER = aj.svg.PAPER;
                vueObj.svg = raphaelObj;
                // 登记注册
                aj.svg.Mgr.register(vueObj);
                if (type == 'img')
                    vueObj.resize = false; // 图片禁止放大缩小
                if (aj.workflow.isREAD_ONLY)
                    vueObj.resize = vueObj.isDrag = false;
                return vueObj;
            }
            setTimeout(function () {
                aj.svg.PAPER = window.PAPER = aj.svg.Mgr.initSVG(document.body.$(".canvas"));
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
                // @ts-ignore
                init(TEST_DATA);
            }, 800);
            // 菜单选中的
            document.body.$('.components ul li.selectable', function (li) {
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
        })(ui = wf.ui || (wf.ui = {}));
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
