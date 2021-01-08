function loadScript(url ) {
    var script  = document.createElement("script");
    script.src = url;

    document.getElementsByTagName("head")[0].appendChild(script);
}

loadScript("https://ajaxjs.nos-eastchina1.126.net/lib/code-prettify.min.js");

new Vue({
    el: '.copyright div',
    template: '<span>©AJAXJS CopyRight</span>'
});

new Vue({
    el: '.box > div > menu > span',
    template: '<ul>\
            <li>\
                <a href="/index.html">Getting Start</a>\
            </li>\
            <li>\
                <a href="/demo/base/index.html">Base</a>\
            </li>\
            <li>\
                <a href="/demo/widget/index.html">Widget</a>\
            </li>\
            <li>\
                <a href="/demo/form/index.html">Form</a>\
            </li>\
            <li>\
                <a href="/demo/list/index.html">List</a>\
            </li>\
            <li>\
                <a href="/demo/grid/index.html">Grid</a>\
            </li>\
            <li>\
                <a href="/demo/carousel/index.html">Carousel</a>\
            </li>\
            <li>\
                <a href="/demo/css/index.html">CSS</a>\
            </li>\
            <li>\
                <a href="/demo/misc/index.html">Misc.</a>\
            </li>\
        </ul>'
})