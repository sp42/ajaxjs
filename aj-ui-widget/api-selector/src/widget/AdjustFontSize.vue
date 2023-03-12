<template>
  <div class="aj-adjust-font-size">
    <span>字体大小</span>
    <ul @click="onClk">
      <li><label><input type="radio" name="fontSize" /> 小</label></li>
      <li><label><input type="radio" name="fontSize" /> 中</label></li>
      <li><label><input type="radio" name="fontSize" /> 大</label></li>
    </ul>
  </div>
</template>

<script lang="ts">
/**
 * 调整正文字体大小
 */
export default {
  props: {
    articleTarget: { type: String, default: "article p" }, // 正文所在的位置，通过 CSS Selector 定位
  },
  methods: {
    onClk(ev: Event): void {
      let el: Element = ev.target as Element;
      let setFontSize = (fontSize: string): void => {
        document.body
          .querySelectorAll(this.$props.articleTarget)
          .forEach((p: HTMLParagraphElement) => (p.style.fontSize = fontSize));
      };

      if (el.tagName == "LABEL" || el.tagName == "INPUT") {
        if (el.tagName != "LABEL") el = el.parentNode as Element;

        console.log(el);
        if (el.innerHTML.indexOf("大") != -1) setFontSize("12pt");
        else if (el.innerHTML.indexOf("中") != -1) setFontSize("10.5pt");
        else if (el.innerHTML.indexOf("小") != -1) setFontSize("9pt");
      }
    },
  },
};
</script>

<style lang="less" scoped>
.aj-adjust-font-size {
  width: 210px;
  font-size: 0.8rem;
  padding: 2px 0;

  span {
    float: left;
    width: 35%;
    display: block;
  }

  ul {
    width: 65%;
    float: right;
    cursor: pointer;

    li {
      display: block;
      float: right;
      width: 33%;
    }
  }
}
</style>