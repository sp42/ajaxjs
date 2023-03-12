<template>
  <div class="api">
    <span :class="httpMethod" style="font-weight: bold;">{{httpMethod}}</span>
    {{getUrl()}}
    <a :href="getUrl()" target="_blank" title="打开连接">&#x1f517;</a>
    <a href="javascript:void(0)" @click="copy" title="复制">📄</a>
    <a href="javascript:alert('TODO')" style="text-decoration:underline;">测试</a>
  </div>
</template>

<script>
export default {
  data() {
    return {
      httpMethod: "GET",
    };
  },
  methods: {
    getUrl() {
      return "http://ssssssssss.com";
    },
    copy() {
      aj.copyToClipboard(this.getUrl());
      this.$Message.success("复制成功");
    },
  },
};
</script>

<style lang="less" scoped>
.api {
  border-left: 4px solid lightgray;
  padding-left: 15px;
  font-family: "Courier New", Courier, monospace;
  font-size: 0.95rem;

  .GET {
    color: green;
  }

  .PUT {
    color: blue;
  }

  .POST {
    color: rgb(224, 60, 254);
  }

  .DELETE {
    color: rgb(20, 215, 20);
  }
}
</style>