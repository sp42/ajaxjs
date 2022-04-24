<template>
  <div class="ide-layout">
    <div class="components">
      <h3 class="title">组件 Components</h3>
      <slot name="component"></slot>
    </div>

    <div class="center">
      <header>
        <div style="float:right;"><a href="https://gitee.com/sp42_admin/aj-utils/wikis/pages?sort_id=5382722" target="_blank">帮助</a> <a @click="showAbout()">关于</a></div>
        <h3 class="title">{{ title }}</h3>
        <div class="top-bar">
          <slot name="top-bar"></slot>
        </div>
      </header>

      <div class="stage aj-form-row-holder">
        <slot name="stage"></slot>
      </div>
    </div>

    <div class="right">
      <slot name="property"></slot>
    </div>

  </div>
</template>

<script>
/**
 * IDE 布局
 */
export default {
  props: {
    title: String,
  },
  methods: {
    showAbout() {
      this.$Modal.info({
        title: '关于 Aj-workflow',
        content: `<p>Version 1.0, <br />免费、开源的轻量级工作流方案，<br />Frank Chueng 荣誉出品</p><p>Powered by Ajaxjs Framework. 
        <a href="https://framework.ajaxjs.com/" target="_blank">https://framework.ajaxjs.com/</a></p>`
      });
    },
  },
};
</script>

<style lang="less" src="./ide-layout.less"></style>