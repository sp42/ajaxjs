/**
 * 原生 JS 框架
 */
jsp = {};

/**
 * 日期格式化
 * @param 	{String} format
 * @return	{String}
*/
Date.prototype.format = function (format) {
    var $1, o = {
        "M+": this.getMonth() + 1,		// 月份，从0开始算
        "d+": this.getDate(),   		// 日期
        "h+": this.getHours(),   		// 小时
        "m+": this.getMinutes(), 		// 分钟
        "s+": this.getSeconds(), 		// 秒钟
								        // 季度 quarter
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()	// 千秒
    };
    var key, value;
 
    if (/(y+)/.test(format)) {
        $1 = RegExp.$1, 
        format = format.replace($1, String(this.getFullYear()).substr(4 - $1));
    }
 
    for (key in o) { // 如果没有指定该参数，则子字符串将延续到 stringvar 的最后。
        if (new RegExp("(" + key + ")").test(format)) {
            $1		= RegExp.$1,
	    	value	= String(o[key]),
	    	value	= $1.length == 1 ? value : ("00" + value).substr(value.length),
    		format	= format.replace($1, value);
        }
    }
    return format;
}