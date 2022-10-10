<template>
  <div class="home">
    <h2>HTML 编辑器</h2>
    <p>HTML 编辑器，又叫富文本编辑器（Rich Text Editor），目的就是让用户可以在浏览器上编辑各种格式的文本，类似于 Word “即见即所得”那种可视化的特性，HTML 编辑器能直观地编辑文本并同时产生预期的效果。
      不同的是 Word 最终成品是 *.doc 文件，而 HTML 编辑器的保存格式是 HTML。实际上，称为“富文本编辑器”或者更合适一些，盖因这类编辑器，最终的格式不一定是 HTML，可以被设计保存为 md（Markdown）或其他的格式。</p>
    <p>
      <a href="https://zhangxin.blog.csdn.net/article/details/113867893" target="_blank">查看教程</a>
    </p>
    <p>需要引入图标库；HTML 外层要有 textarea 元素包裹着。</p>

    <textarea style="display:none;">
        <h2>什么是Lorem Ipsum?</h2>
        <p>Lorem Ipsum，也称乱数假文或者哑元文本， 是印刷及排版领域所常用的虚拟文字。由于曾经一台匿名的打印机刻意打乱了一盒印刷字体从而造出一本字体样品书，Lorem Ipsum从西元15世纪起就被作为此领域的标准文本使用。它不仅延续了五个世纪，还通过了电子排版的挑战，其雏形却依然保存至今。在1960年代，”Leatraset”公司发布了印刷着Lorem Ipsum段落的纸张，从而广泛普及了它的使用。最近，计算机桌面出版软件”Aldus PageMaker”也通过同样的方式使Lorem Ipsum落入大众的视野。</p>

        <h2>我们为何用它？</h2>
        <p>无可否认，当读者在浏览一个页面的排版时，难免会被可阅读的内容所分散注意力。Lorem Ipsum的目的就是为了保持字母多多少少标准及平均的分配，而不是“此处有文本，此处有文本”，从而让内容更像可读的英语。如今，很多桌面排版软件以及网页编辑用Lorem Ipsum作为默认的示范文本，搜一搜“Lorem Ipsum”就能找到这些网站的雏形。这些年来Lorem Ipsum演变出了各式各样的版本，有些出于偶然，有些则是故意的（刻意的幽默之类的）。</p>
    </textarea>

    <HtmlEditor :v-model="v" style="width:800px;margin:0 auto" @on-change="v=$event" :is-ionicons="false" />

    <br />
    <br />
    <div style="margin:0 auto;width:800px;">
      {{v}}
    </div>
  </div>
</template>

<script>
import HtmlEditor from "@/widget/HtmlEditor/HtmlEditor.vue";

export default {
  components: { HtmlEditor },
  data() {
    return {
      v: "",
    };
  },
  mounted() {
    this.v = this.$el.querySelector("textarea").value;
  },
};
</script>