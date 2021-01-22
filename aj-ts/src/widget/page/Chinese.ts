namespace aj.widget.page.TraditionalChinese {
    /*
        正体中文
        <span>
             <a href="javascript:;" onclick="aj.widget.page.TraditionalChinese.toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>
            /<a href="javascript:;" onclick="aj.widget.page.TraditionalChinese.toChinese(this);" class="Chinese">正体中文</a>
        </span>
    */

    const TraditionalChinese = 1, SimpleChinese = 2;
    type Chinese = number;

    /**
     * 
     * 默认是否正体中文：true为正体；false简体。 HTTP
     * 头读取：Request.ServerVariables("http_accept_language")
     * 
     * @return {Boolean}
     */
    function getClientLanguage(): Chinese {
        // @ts-ignore
        let s = navigator.userLanguage || navigator.language;
        switch (s.toLowerCase()) {
            case 'zh-tw':
                return TraditionalChinese;
            case 'zh-cn':
                return SimpleChinese;
            default:
                return 0;
        }
    }

    var Cookie = {
        set(name: string, val: string): void {
            let exp = new Date();
            exp.setDate(exp.getDate() + 600 * 1000);
            document.cookie = name + "=" + escape(val) + ";expires=" + exp.toUTCString();
        },
        del(name: string): void {
            document.cookie = name + "=;expires=" + (new Date(0)).toUTCString();
        },

        get(name: string): string {
            let cookieArray = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
            return cookieArray != null ? unescape(cookieArray[2]) : "";
        }
    };

    /**
     * 转换对象，使用递归，逐层剥到文本
     * 
     * @param {HTMLElement} obj 从document.body开始，
     */
    function walk(el: HTMLElement, coverntFn: Function): void {
        let chidlNodes: NodeListOf<ChildNode> = el.childNodes;
        let node: HTMLElement;

        for (var i = 0, j = chidlNodes.length; i < j; i++) {
            node = <HTMLElement>chidlNodes.item(i);
            // || (node == $$.big5.el)
            if (("||BR|HR|TEXTAREA|".indexOf("|" + node.tagName + "|")) > 0)
                continue;

            if (node.title)
                node.title = coverntFn(node.title);
            // @ts-ignore
            else if (node.alt)
                // @ts-ignore
                node.alt = coverntFn(node.alt);
            // @ts-ignore
            else if (node.tagName == "INPUT" && node.value != "" && node.type != "text" && node.type != "hidden")
                // @ts-ignore
                node.value = coverntFn(node.value);
            else if (node.nodeType == 3)
                // @ts-ignore
                node.data = coverntFn(node.data);
            else
                walk(node, coverntFn);
        }
    }

    /**
     * 一对一替换字符
     * 
     * @param text      输入的文本
     * @param oldChars  原来所在的字符集
     * @param newChars  要替换到的新字符集
     */
    function translateText(text: string, oldChars: string, newChars: String): string {
        let str: string[] = [];
        let char: string, charIndex: number, result: string;

        for (let i = 0, j = text.length; i < j; i++) {
            char = text.charAt(i);
            charIndex = oldChars.indexOf(char);
            result = newChars.charAt(charIndex);

            str.push(charIndex != -1 ? result : char);// 匹配不到，用原来的字符
        }

        return str.join('');
    }

    let isLoaded = false;

    function loadChars(cb: Function, el: HTMLElement) {
        if (isLoaded)
            cb();
        else
            aj.loadScript("https://framework.ajaxjs.com/src/widget/page/ChineseChars.js", "", () => {
                isLoaded = true;
                cb();
            });

        (<HTMLElement>el.up('span')?.$(aj.SELECTED_CSS)).classList.remove(aj.SELECTED);
        el.classList.add(aj.SELECTED);
    }

    let self = aj.widget.page.TraditionalChinese;
    let currentLanguageState: Chinese;      // 当前语言选择
    const cookieName = 'ChineseType';

    currentLanguageState = getClientLanguage();

    export function toSimpleChinese(el: HTMLElement): void {
        if (currentLanguageState === SimpleChinese) // 已经是，无须进行
            return;

        loadChars(() => {
            //@ts-ignore
            walk(document.body, translateText.delegate(null, self.正体中文, self.简化中文));
            currentLanguageState = SimpleChinese;
            Cookie.set(cookieName, currentLanguageState + "");
        }, el);
    }

    export function toChinese(el: HTMLElement): void {
        if (currentLanguageState === TraditionalChinese) // 已经是，无须进行
            return;

        loadChars(() => {
            //@ts-ignore
            walk(document.body, translateText.delegate(null, self.简化中文, self.正体中文));
            currentLanguageState = TraditionalChinese;
            Cookie.set(cookieName, currentLanguageState + "");
        }, el);
    }

    /**
     * 初始化
     */
    export function init(): void {
        let valueInCookie: string | Chinese = Cookie.get(cookieName);

        if (valueInCookie)
            valueInCookie = Number(valueInCookie);

        // 浏览器是繁体中文的，或者 Cookie 设置了是正体的，进行转换（当然默认文本是简体的）
        if (currentLanguageState == TraditionalChinese || valueInCookie == TraditionalChinese)
            toChinese(<HTMLElement>document.querySelector(".Chinese"));
    }
}