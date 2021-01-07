namespace aj.widget.page.TraditionalChinese {

    /*
        正体中文
        <span>
            <a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>
            /<a href="javascript:;" onclick="toChinese(this); class="Chinese"">正体中文</a>
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
}