<template>
  <article>
    <h2>{{title}}</h2>

    <div class="toolbar">
      <span v-if="author">作者: {{author}}</span> &nbsp;
      <a :href="source" v-if="source" target="_blank">

        <svg width="16px" height="16px" viewBox="0 0 24 24" style="cursor:pointer;vertical-align: middle;">
          <g stroke-width="2.1" stroke="#666" fill="none" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="17 13.5 17 19.5 5 19.5 5 7.5 11 7.5"></polyline>
            <path d="M14,4.5 L20,4.5 L20,10.5 M20,4.5 L11,13.5"></path>
          </g>
        </svg>
        来源</a>
      <FontSize style="float:right;clear:both"></FontSize>
    </div>
    <slot></slot>
  </article>
</template>

<script>
import FontSize from './AdjustFontSize.vue';

export default { 
  props: {
    title: String, author: String, source: String
  },
  components: { FontSize }
}
</script>

<style lang="less" scoped>
@import "../style/common-functions.less";

article {
  & > p {
    .aj-text-function();
    text-indent: 2em;
  }
  & > img,
  & > p > img {
    max-width: 80%;
    margin: 0 auto;
    display: block;
  }

  h2 {
    .aj-text-title();
  }

  .toolbar {
    height: 15px;
    margin: 30px 0;
    font-size: 0.8rem;
    text-align: right;
  }
}
</style>