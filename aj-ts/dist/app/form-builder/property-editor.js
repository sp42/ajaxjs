"use strict";
var aj;
(function (aj) {
    var fb;
    (function (fb) {
        fb.PropertyEditor = new Vue({
            el: '.right .bottom',
            data: {
                name: '',
                placeHolder: ''
            },
            watch: {
                placeHolder: function (v) {
                    if (aj.wysiwyg.center.focusEl)
                        // @ts-ignore
                        aj.wysiwyg.center.focusEl.placeholder = v;
                }
            }
        });
    })(fb = aj.fb || (aj.fb = {}));
})(aj || (aj = {}));
