/**
 * 通用的打开下载对话框方法，没有测试过具体兼容性
 * https://www.cnblogs.com/liuxianan/p/js-download.html
 * 
 * ref 这应该是你见过的最全前端下载总结 https://juejin.cn/post/6844903763359039501
 * 
 * @param url 下载地址，也可以是一个blob对象，必选
 * @param saveName 保存文件名，可选
 */
function openDownloadDialog(url, saveName) {
    if (typeof url == 'object' && url instanceof Blob)
        url = URL.createObjectURL(url); // 创建blob地址

    var aLink = document.createElement('a');
    aLink.href = url;
    aLink.download = saveName || ''; // HTML5新增的属性，指定保存文件名，可以不要后缀，注意，file:///模式下不会生效

    var event;
    if (window.MouseEvent) event = new MouseEvent('click');
    else {
        event = document.createEvent('MouseEvents');
        event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
    }

    aLink.dispatchEvent(event);
}
