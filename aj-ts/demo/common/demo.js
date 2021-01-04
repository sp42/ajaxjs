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
    template: `
        <ul>
            <li>
                <a href="/index.html">Getting Start</a>
            </li>
            <li>
                <a href="/demo/base/index.html">Base</a>
            </li>
            <li>
                <a href="/demo/modal/index.html">Modal</a>
            </li>
            <li>
                <a href="?g=pages">Widget</a>
            </li>
            <li>
                <a href="?g=tab">Tab</a>
            </li>
            <li>
                <a href="/demo/form/index.html">Form</a>
            </li>
            <li>
                <a href="?g=list">List</a>
            </li>
            <li>
                <a href="?g=tree">Tree</a>
            </li>
            <li>
                <a href="/demo/carousel/index.html">Carousel</a>
            </li>
            <li>
                <a href="?g=navmenu">Menu</a>
            </li>

            <li>
                <a href="?g=css">CSS Layout</a>
            </li>
        </ul>
    `
})