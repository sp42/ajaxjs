<template>
  <div class="api">
    <span :class="'httpMethod '+getHttpMethod()" style="font-weight: bold;">
      {{getHttpMethod()}}
    </span>
    {{getUrl()}}
    <a :href="getUrl()" target="_blank" title="打开连接">&#x1f517;</a>&nbsp;
    <a href="javascript:void(0)" @click="copy" title="复制">📄</a>&nbsp;
    <a href="javascript:alert('TODO')" style="text-decoration:underline;">测试</a>
  </div>
</template>
<script>
import { copyToClipboard } from "../../util/utils";

export default {
  props: {
    page: {
      type: Boolean,
      require: false,
    },
  },
  data() {
    return {
      httpMethod: this.$parent.editorData.type,
    };
  },
  methods: {
    getUrl() {
      let url = "http://localhost/" + this.$parent.data.id;

      if (this.page) url += "/page";
      else {
        switch (this.$parent.editorData.type) {
          case "info":
          case "delete":
            url += "/{id}";
            break;
          case "list":
            url += "/list";
            break;
          case "create":
          case "update":
        }
      }

      return url;
    },
    copy() {
      copyToClipboard(this.getUrl());
      this.$Message.success("复制成功");
    },
    getHttpMethod() {
      switch (this.$parent.editorData.type) {
        case "info":
        case "list":
          return "GET";
        case "create":
          return "POST";
        case "update":
          return "PUT";
        case "delete":
          return "DELETE";
      }
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

  .POST {
    color: burlywood;
  }

  .PUT {
    color: blueviolet;
  }

  .DELETE {
    color: red;
  }
}
</style>