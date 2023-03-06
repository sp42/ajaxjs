/**
 * 获取某个 Cookie
 * 
 * @param name 键名称 
 * @returns 值
 */
export function getCookie(name: string): string | null {
    let arr: RegExpMatchArray, reg: RegExp = new RegExp("(^| )" + name + "=([^;]*)(;|$)");

    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

/**
 * 设置某个 Cookie
 * 
 * @param name  键名称 
 * @param value 值
 */
export function setCookie(name: string, value: string): void {
    let days: number = 2, exp: Date = new Date();
    exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
    // @ts-ignore
    document.cookie = name + "=" + escape(value) + ";path=/;expires=" + exp.toGMTString();
}

/**
 * 清空 Cookie
 */
export function delCookie(): void {
    let keys = document.cookie.match(/[^ =;]+(?==)/g);

    if (keys) {
        let date: string = new Date(0).toUTCString();
        for (let i = keys.length; i--;) {
            document.cookie = keys[i] + '=0;path=/;expires=' + date; // 清除当前域名下的,例如：m.ratingdog.cn
            document.cookie = keys[i] + '=0;path=/;domain=' + document.domain + ';expires=' + date; // 清除当前域名下的，例如 .m.ratingdog.cn
            document.cookie = keys[i] + '=0;path=/;domain=' + location.host + ';expires=' + date; // 清除一级域名下的或指定的，例如 .ratingdog.cn
        }
    }
}