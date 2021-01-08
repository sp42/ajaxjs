/**
 * Baidu 自定义搜索
 */
interface BaiduSearch {
    siteDomainName: string;
}

Vue.component('aj-widget-baidu-search', {
    template: `
        <div class="aj-widget-baidu-search"><form method="GET" action="http://www.baidu.com/baidu" onsubmit="//return g(this);">
            <input type="text" name="word" placeholder="请输入搜索之关键字" />
            <input name="tn" value="bds" type="hidden" />
            <input name="cl" value="3" type="hidden" />
            <input name="ct" value="2097152" type="hidden" />
            <input name="si" :value="getSiteDomainName" type="hidden" />
            <div class="searchBtn" onclick="this.parentNode.submit();"></div>
        </form></div>
    `,
    props: ['siteDomainName'],
    computed: {
        getSiteDomainName(this: BaiduSearch): string {
            return this.siteDomainName || location.host || document.domain;
        }
    }
});