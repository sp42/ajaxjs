//定义几个全局变量
var AppUrl = "http://127.0.0.1:8302";//整个页面的请求页面

/**
 * 表格隔行变色
 */
function altRows() {
    var rows = $("tr");
    for (i = 0; i < rows.length; i++) {
        if (i % 2 == 0) {
            rows[i].className = "evenrowcolor";
        } else {
            rows[i].className = "oddrowcolor";
        }
    }
}

/**
 * 获取地址栏 url 的参数信息
 * @param name
 * @returns {*}
 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}

/**
 * 获取地址栏 url 的参数信息
 * @param name
 * @returns {*}
 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}
