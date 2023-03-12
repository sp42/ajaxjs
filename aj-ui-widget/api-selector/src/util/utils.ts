/**
 * 通用工具类
 */

const env = process.env.NODE_ENV;

/**
 * 是否调试模式中
 * 
 * 打包成组件之后不能用
 * 
 * @returns 
 */
export function isDebug(): boolean {
    return env === 'development';
}

/**
* 日期格式化。详见博客文章：http://blog.csdn.net/zhangxin09/archive/2011/01/01/6111294.aspx
* e.g: new Date().format("yyyy-MM-dd hh:mm:ss")
*
* @param 	{String} format
* @return	{String}
*/
export function dateFormat(this: Date, format: string): string {
    let $1, o: any = {
        "M+": this.getMonth() + 1,		// 月份，从0开始算
        "d+": this.getDate(),   		// 日期
        "h+": this.getHours(),   		// 小时
        "m+": this.getMinutes(), 		// 分钟
        "s+": this.getSeconds(), 		// 秒钟
        // 季度 quarter
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()	// 千秒
    };

    if (/(y+)/.test(format)) {
        $1 = RegExp.$1, format = format.replace($1, String(this.getFullYear()).substr(4 - $1));
    }

    let key: string, value: string;
    for (key in o) { // 如果没有指定该参数，则子字符串将延续到 stringvar 的最后。
        if (new RegExp("(" + key + ")").test(format)) {
            $1 = RegExp.$1,
                value = String(o[key]),
                value = $1.length == 1 ? value : ("00" + value).substr(value.length),
                format = format.replace($1, value);
        }
    }

    return format;
}

/**
 * 日期格式化
 * @author meizz
 * @param date  日期，必须为 Date 类型
 * @param fmt   格式模板
 * @returns 格式化后的字符串
 */
export function dateFormat2(date: Date, fmt: string): string {
    let o: { [key: string]: number } = {
        "M+": date.getMonth() + 1,                  // 月份   
        "d+": date.getDate(),                       // 日   
        "h+": date.getHours(),                      // 小时   
        "m+": date.getMinutes(),                    // 分   
        "s+": date.getSeconds(),                    // 秒   
        "q+": Math.floor((date.getMonth() + 3) / 3),// 季度   
        "S": date.getMilliseconds()                 // 毫秒   
    };

    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));

    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) {
            let obj = (RegExp.$1.length == 1) ? o[k] : ("00" + o[k]).substr(("" + o[k]).length);
            // @ts-ignore
            fmt = fmt.replace(RegExp.$1, obj);
        }

    return fmt;
}

/**
 * 并行和串行任务 
 * 
 * @author https://segmentfault.com/a/1190000013265925
 * @param arr 
 * @param finnaly 
 */
export function parallel(arr: [], _finally: Function) {
    let fn: Function, index: number = 0;
    // @ts-ignore
    let statusArr = Array(arr.length).fill().map(() => ({ isActive: false, data: null }));

    let isFinished = function () {
        return statusArr.every((item: any) => item.isActive === true);
    }

    let resolve = function (index: number): Function {
        return function (data: any) {
            statusArr[index].data = data;
            statusArr[index].isActive = true;
            let isFinish = isFinished();

            if (isFinish) {
                let datas = statusArr.map((item: any) => item.data);

                _finally(datas);
            }
        };
    };

    // @ts-ignore
    while ((fn = arr.shift())) {
        fn(resolve(index));// 给 resolve 函数追加参数,可以使用 bind 函数实现,这里使用了柯里化
        index++;
    }
}

/**
 * 函数节流
 * 
 * @author https://www.cnblogs.com/moqiutao/p/6875955.html
 * @param fn 
 * @param delay 
 * @param mustRunDelay 
 */
export function throttle(fn: Function, delay: number, mustRunDelay: number): Function {
    var timer: number, t_start: number;

    return function () {
        var t_curr = +new Date();
        window.clearTimeout(timer);

        if (!t_start)
            t_start = t_curr;

        if (t_curr - t_start >= mustRunDelay) {
            // @ts-ignore
            fn.apply(this, arguments);
            t_start = t_curr;
        } else {
            var args = arguments;
            // @ts-ignore
            timer = window.setTimeout(() => fn.apply(this, args), delay);
        }
    };
}