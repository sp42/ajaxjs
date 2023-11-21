<template>
  <!-- 搜索表单 -->
  <Card :bordered="false" dis-hover class="common-search-panel" style="text-align:left">
    <span style="float:right;">
     <Button type="primary" icon="ios-search" @click="$parent.getData()">查询</Button>
      <Button style="margin-left:10px" @click="reset">重置</Button>
    </span>
    <slot />
  </Card>
</template>

<script>
export default {
  props: {
    search: Object,
    cmp: Object,
  },
  methods: {
    reset() {
      for (let i in this.search) this.search[i] = "";

      this.$parent.getData();
    },
  },
};
</script>

<style lang="less">
.common-search-panel .ivu-input-type-text {
  width: 280px;
  margin: 0 10px 16px 0;
}
</style>