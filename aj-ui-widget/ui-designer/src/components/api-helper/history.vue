<template>
  <div>
    <div class="toolbar">
      <Button size="small" type="error" style="float:right;" :disabled="!historyList.length" @click="historyList=[];">清空历史记录</Button>
      <span v-if="historyList.length">共 {{historyList.length}} 笔记录</span>
    </div>

    <ul style="max-height:500px">
      <li v-for="history in historyList" :key="history.id" @click="select(history)">
        <span v-if="history.name">{{history.name}}
          <br /></span>
        <span style="font-family:Courier New">
          <HttpMethod :type="history.httpMethod" />{{history.url}}
        </span>
        <br />
        <div style="font-family:Arial;font-size:3px;color:gray;float:right;margin-top:4px;">{{history.time}}ms</div>
        <span style="font-family:Courier New;font-size:5px;color:lightgray">{{history.date}}</span>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import HttpMethod from "./common/http-method.vue";

export default {
  components: { HttpMethod },
  data() {
    return {
      historyList: [
        {
          id: 1,
          httpMethod: "GET",
          name: "fgfgf",
          url: "http://qq.com",
          time: 203,
          date: "2022-3-3 3:23",
          stateCode: 200,
        },
        {
          id: 2,
          httpMethod: "POST",
          url: "http://qq.com/gggggggggggggggggggggggggggggggggggggggggggggggggggggggggg",
          time: 203,
          date: "2022-3-3 3:23",
          stateCode: 200,
        },
        {
          id: 3,
          httpMethod: "PUT",
          name: "fgfgf",
          url: "http://qq.com",
          time: 203,
          date: "2022-3-3 3:23",
          stateCode: 200,
        },
        {
          id: 4,
          httpMethod: "DELETE",
          name: "获取新闻详情",
          url: "http://qq.com",
          time: 203,
          date: "2022-3-3 3:23",
          stateCode: 200,
        },
      ] as API_HELPER_HISTORY[],
    };
  },

  methods: {
    select(h: any): void {
      alert(h);
    },
  },
};
</script>

<style lang="less" scoped>
.toolbar {
  padding: 0 6px 5px 6px;
  border-bottom: 1px solid #f0f0f0;
  box-sizing: content-box;
  overflow: hidden;
  margin: 0 8px;
}
ul {
  overflow-y: auto;
  &::-webkit-scrollbar {
    width: 6px;
  }
  &::-webkit-scrollbar-thumb {
    border-radius: 8px;
    background: rgba(23, 35, 61, 0.15);
  }
  &::-webkit-scrollbar-track {
    background-color: transparent;
    border-radius: 8px;
  }
}

li {
  padding: 10px 5px;
  margin: 10px 5px;
  border: 1px solid #f0f0f0;
  border-radius: 5px;
  overflow: hidden;
  max-width: 290px;
  text-overflow: ellipsis; //溢出用省略号显示
  white-space: nowrap; //溢出不换行
  transition: border-color ease-in 400ms;
  cursor: pointer;
  &:hover {
    border-color: lightgray;
  }
}
</style>