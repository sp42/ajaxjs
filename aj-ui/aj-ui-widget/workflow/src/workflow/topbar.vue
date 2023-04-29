<template>
  <div>
    <span class="right">
      <a @click="getParent().codeMode = !getParent().codeMode" v-bind:class="{ active: !getParent().codeMode }">设计</a>
      |
      <a @click="getParent().codeMode = !getParent().codeMode" v-bind:class="{ active: getParent().codeMode }">代码</a>
    </span>

    <a @click="getParent().clearStage()">新建流程</a> <a @click="isShowOpen = true">打开流程</a>

    <Modal v-model="isShowOpen" title="打开一个工作流的流程" width="860" cancel-text="">
      dfdf
    </Modal>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isShowOpen: false
    };
  },
  methods: {
    getParent() {
      return this.$parent.$parent;
    }
  }
}
</script>