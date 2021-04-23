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
        })(ComMgr = wf.ComMgr || (wf.ComMgr = {}));
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
