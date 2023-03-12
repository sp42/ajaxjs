<template>
  <form class="aj-widget-baidu-search" method="GET" action="http://www.baidu.com/baidu" onsubmit="//return g(this);">
    <input type="text" name="word" placeholder="请输入搜索之关键字" />
    <input name="tn" value="bds" type="hidden" />
    <input name="cl" value="3" type="hidden" />
    <input name="ct" value="2097152" type="hidden" />
    <input name="si" :value="getSiteDomainName" type="hidden" />
    <div class="searchBtn" onclick="this.parentNode.submit();"></div>
  </form>
</template>

<script lang="ts">
/**
 * Baidu 自定义搜索
 */
export default {
  props: {
    siteDomainName: String,
  },
  computed: {
    getSiteDomainName(): string {
      return this.siteDomainName || location.host || document.domain;
    },
  },
};
</script>

<style lang="less" scoped>
.aj-widget-baidu-search {
  padding: 0;
  margin: 0;

  input[type="text"] {
    border-radius: 5px;
    border: 1px solid gray;
    padding: 0 2px;
    height: 22px;
  }

  .searchBtn {
    background: url("data:image/gif;base64,R0lGODlhEAAQAGYAACH5BAEAAAAALAAAAAAQABAApgEAAOD3//n9/+3//936/xt7uX+x1KAQCyeHwsj1/6QcGZDd+fX+/+oOAFW05FCUxMXs+9P0/+T0+7fv/5rm/6fn/bHr/jCBugt0t0CKv47e+4fW9CaEv6fb8w9/weteSfkEAOsJC8xBKehVP+FZPxF1thZ4uMHx/1FBZQdssQ51uLrk9zlumyB3tDBRir3x/7nm+WK75DKb0z2k2XTG6mXD7Gq/6Euq20yk1EWp20ay5Umv4liw3dUcAKowMBqJycEzB8EqANoZACOa2KQNCp4NCgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAeWgACCDwUIhgaCiYoFNBQWFQs4F4qJBQsRAZkBFTwZlBcbEQMCpAMDCwiUBRQEpCcTCQEwN4iJHBYSAgmwBAEQObWCBRoBAi8JvQMdMsEAGTEQAQTJAw4qlAAmNSsM3RIOPyzYACUzOjY7Qx4uIQfjDykYGC0oHyAN7uOKCiMNQvn6BCkg0SNIkYCJFIgAAhChDyII9QUCADs=")
      no-repeat center;
    width: 25px;
    height: 25px;
    cursor: pointer;
    display: inline-block;
    vertical-align: bottom;
  }
}
</style>