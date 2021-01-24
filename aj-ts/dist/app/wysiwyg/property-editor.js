"use strict";
propertyEditor = new Vue({
    el: '.right .bottom',
    data: {
        name: '',
        placeHolder: ''
    },
    watch: {
        placeHolder: function (v) {
            if (FB.center.focusEl) {
                FB.center.focusEl.placeholder = v;
            }
        }
    }
});
