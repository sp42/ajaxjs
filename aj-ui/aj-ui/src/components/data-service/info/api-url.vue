<template>
  <!-- 生成 API 接口地址 -->
  <span class="api-url">
    <span v-html="getMethod()"></span>&nbsp;
    <a :href="getUrl(true)" target="_blank" class="api-link">
      {{ getUrl() | shortUrl }}<span v-show="!isShowSubDir">{{$parent.dml.dir ? '/' : ''}}{{ $parent.dml.dir }}</span>
    </a>

    <span v-show="isShowSubDir">&nbsp;
      <!-- <input type="text" size="10" :value="dir" @input="$emit('update:dir', $event.target.value)" />&nbsp; -->
      <input type="text" size="10" v-model="$parent.dml.dir" />&nbsp;
      <a @click="isShowSubDir = false" style="color:#7ace0a">确定</a> |
      <a @click="cancelOrDel" style="color:red">{{$parent.dml.dir ? '删除':'取消'}}</a>
    </span>
    <br /> <br />
    <div>
      <a @click="isShowSubDir = true" v-if="canSetSubDir">设置子目录</a> <span v-if="canSetSubDir"> | </span>
      <!-- <a href="javascript:alert('TODO');">测试</a> |  -->
      <a @click="writeCopy">复制</a>
    </div>
  </span>
</template>

<script lang="ts">
export default {
  props: {
    canSetSubDir: { type: Boolean, default: true },
    method: { type: String, default: "get" }, // HTTP 方法
  },
  data() {
    return {
      isShowSubDir: false,
    };
  },
  filters: {
    /**
     * 完整 http 地址，不显示全部
     */
    shortUrl(v: string): string {
      if (v.indexOf("http") != -1) {
        let arr: string[] = v.split("/");
        arr.shift();
        arr.shift();
        arr.shift();

        v = arr.join("/");
      }

      if (v[0] != "/") v = "/" + v;

      return v;
    },
  },
  methods: {
    /**
     * 渲染 HTTP 方法
     */
    getMethod(): string {
      switch (this.method) {
        case "delete":
          return '<span style="color: red;">DELETE</span>';
        case "put":
          return '<span style="color: blue;">PUT</span>';
        case "post":
          return '<span style="color: rgb(224, 60, 254);">POST</span>';
        case "get":
        default:
          return '<span style="color: rgb(20, 215, 20);">GET</span>';
      }
    },

    /**
     * 返回 API 地址
     *
     * @param isSubDir 是否包含子目录
     * @returns API 地址
     */
    getUrl(isSubDir: boolean): string {
      let ctxPath: string = this.$parent.table.apiRoot;
      let API_URL_ROOT: string = "/api";
      let perfixUrl: string = ctxPath + API_URL_ROOT;

      if (this.$parent.table.dataSourceUrlDir)
        perfixUrl += "/" + this.$parent.table.dataSourceUrlDir;

      perfixUrl += "/" + this.$parent.table.urlDir;

      if (isSubDir) perfixUrl += "/" + this.$parent.dml.dir;

      if (perfixUrl[perfixUrl.length - 1] === "/")
        // 去掉最后一个 斜杠
        perfixUrl = perfixUrl.substr(0, perfixUrl.length - 1);

      return perfixUrl;
    },

    /**
     * 删除子目录
     */
    cancelOrDel(): void {
      this.isShowSubDir = false;
      if (this.$parent.dml.dir) this.$parent.dml.dir = "";
    },

    /**
     * 复制接口地址
     */
    writeCopy(ev: Event): void {
      let a: HTMLLinkElement =
        // @ts-ignore
        ev.target.parentNode.parentNode.querySelector(".api-link");
      let text: string = a.href;

      text && this.$Copy({ text: a.href });
    },
  },
};
</script>

<style lang="less">
// api 预览连接
.api-url {
  & > span {
    vertical-align: top;
  }
  & > a {
    word-break: break-all;
    display: inline-block;
  }

  div {
    text-align: center;
  }

  input {
    border: 1px solid lightgray;
    outline: none;
    border-radius: 2px;
    padding: 2px;
    &:focus {
      border-color: gray;
    }
  }
}
</style>