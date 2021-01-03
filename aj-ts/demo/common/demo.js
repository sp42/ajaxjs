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
                <a href="?g=tab">Tab</a>
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
                <a href="?g=form">Form</a>
            </li>
            <li>
                <a href="?g=formcontrol">Form Control</a>
            </li>
            <li>
                <a href="?g=list">List</a>
            </li>
            <li>
                <a href="?g=pages">PageTools</a>
            </li>

            <li>
                <a href="?g=marquee">Marquee</a>
            </li>
            <li>
                <a href="?g=css">CSS Layout</a>
            </li>
        </ul>
    `
})