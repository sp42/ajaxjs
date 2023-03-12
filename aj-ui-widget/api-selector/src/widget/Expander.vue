<template>
  <div class="aj-expander" :style="'height:' + (expended ? openHeight : closeHeight) + 'px;'">
    <div :class="expended ? 'closeBtn' : 'openBtn'" @click="expended = !expended;"></div>
    <slot></slot>
  </div>
</template>

<script lang="ts">
/**
 * 展开闭合器
 */
export default {
  props: {
    openHeight: { type: Number, default: 200 },
    closeHeight: { type: Number, default: 50 },
  },
  data(): any {
    return {
      expended: false,
    };
  },
};
</script>

<style lang="less" scoped>
.aj-expander {
  overflow: hidden;
  position: relative;
  clear: both;
  transition: height 200ms ease 0s;

  .mask-layer {
    position: absolute;
    bottom: 0;
    left: 0;
    height: 23px;
    width: 100%;
    background: -webkit-gradient(
      linear,
      center top,
      center bottom,
      from(transparent),
      to(white)
    );
  }

  .openBtn {
    background: url("https://ajaxjs.nos-eastchina1.126.net/images/openBtn.png");
  }

  .closeBtn {
    background: url("https://ajaxjs.nos-eastchina1.126.net/images/closeBtn.png");
  }

  .closeBtn,
  .openBtn {
    width: 45px;
    height: 32px;
    position: absolute;
    cursor: pointer;
    top: 0px;
    right: 0px;
  }
}
</style>