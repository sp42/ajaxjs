"use strict";
Vue.component('aj-carousel', {
    mixins: [aj.carousel.base],
    template: "\n\t\t<div class=\"aj-carousel aj-carousel-tab\">\n\t\t\t<header>\n\t\t\t\t<ul>\n\t\t\t\t\t<li v-for=\"(item, index) in items\" :class=\"{'active': index === selected}\" @click=\"changeTab(index);\">\n\t\t\t\t\t\t{{item.name}}\n\t\t\t\t\t</li>\n\t\t\t\t</ul>\n\t\t\t</header>\n\t\t\t<div>\n\t\t\t\t<div v-for=\"(item, index) in items\" :class=\"{'active': index === selected}\" v-html=\"item.content\"></div>\n\t\t\t</div>\n\t\t</div>\t\n\t",
    data: function () {
        return {
            items: this.initItems || [
                {
                    name: '杜甫：望岳',
                    content: "\u5CB1\u5B97\u592B\u5982\u4F55\uFF0C\u9F4A\u9B6F\u9752\u672A\u4E86\u3002<br>\n\t\t\t\t\t\t\t\t\t\t\t     \u9020\u5316\u9418\u795E\u79C0\uFF0C\u9670\u967D\u5272\u660F\u66C9\u3002<br>\n\t\t\t\t\t\t\t\t\t\t\t     \u8569\u80F8\u751F\u5C64\u4E91\uFF0C\u6C7A\u7725\u5165\u6B78\u9CE5\uFF0C<br>\n\t\t\t\t\t\t\t\t\t\t\t     \u6703\u7576\u51CC\u7D55\u9802\uFF0C\u4E00\u89BD\u773E\u5C71\u5C0F\u3002"
                },
                { name: '资质证照', content: '之所以如此，端在于中国传统中存在着发达的契约。予谓不信，可看看早年由福建师范大学内部印行的两册本《明清福建经济契约文书选集》，中国社会科学出版社出版的《自贡盐业契约档案选集》，花山文艺出版社出版的共二十册的《徽州千年契约文书》;再看看近些年由安徽师范大学出版社出版的十卷本的《千年徽州契约文书集萃》，广西师范大学出版社出版的四辑共四十册《徽州文书》、三辑共三十册的《清水江文书》，民族出版社出版的多卷本《贵州清水江流域明清契约文书》，凤凰出版社出版的《敦煌契约文书辑校》，天津古籍出版社出版的《清代宁波契约文书辑校》，浙江大学出版社出版的《清代浙东契约文书辑选》……不难发现，中国传统契约文书的整理出版，完全可称为如雨后春笋般在迅速成长!' },
                { name: '资质证照', content: '笔者出于个人兴趣，在关注这些已经整理出版的、卷帙浩繁的契约文献的同时，也游走各地，或亲自搜集各类契约文书，或到一些地方档案馆查看其业已编辑成册、内部印行的传统契约文书，如在台湾宜兰、高雄，山东青岛、威海，贵州锦屏、从江等地档案机构或民间都见到过相关契约文献。记忆尤深的一次，是我和几位学生共游山东浮来山。在一处值班室里，居然发现有人以清代契约文本粘糊墙壁!足见只要我们稍加留意，在这个文明发展历经数千年的国度，不时可以发现一些令人称心的古代契约文献。' }
            ]
        };
    }
});
