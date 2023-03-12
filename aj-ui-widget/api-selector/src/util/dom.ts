/**
 * 向父级元素递归搜索
 * 
 * @param _el       当前所在元素
 * @param tagName   目标标签名称
 * @param className 目标元素样式类
 * @returns 目标元素，找不到为 null
 */
export function up(_el: Element, tagName: string, className: string): Element | null {
    if (tagName && className)
        throw '只能任选一种参数，不能同时传';

    let el: Element = _el.parentNode as Element;
    tagName = tagName && tagName.toUpperCase();

    while (el) {
        if (tagName && el.tagName == tagName)
            return el;

        if (className && el.className && ~el.className.indexOf(className))
            return el;

        el = <Element>el.parentNode;
    }

    return null;
}

/**
 * 加载脚本
 * 
 * @param url   脚本地址
 * @param id    脚本元素 id，可选的
 * @param cb    回调函数，可选的
 */
export function loadScript(url: string, id?: string, cb?: (ev: Event) => any): void {
    let script: HTMLScriptElement = document.createElement("script");
    script.src = url;

    if (cb)
        script.onload = cb;

    if (id)
        script.id = id;

    document.getElementsByTagName("head")[0].appendChild(script);
}