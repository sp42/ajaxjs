<template>
  <Tabs value="name1" :animated="false" style="padding:1% 3%;">
    <TabPane label="服务端监控" name="name1">
      <SystemWatch />
    </TabPane>
    <TabPane label="实时日志" name="name2">
      <p>在这里可以实时浏览 Tomcat 日志</p>
      <br />
      <div class="log-container" style="height: 450px; overflow-y: scroll; background: #333; color: #aaa; padding: 10px; margin: 0 auto;">
        <div></div>
      </div>
    </TabPane>
    <TabPane label="数据库管理" name="name3">
    </TabPane>
  </Tabs>
</template>

<script>
import SystemWatch from './system-watch.vue';

export default {
  components: {
    SystemWatch
  },
  mounted() {
    let logContainer = this.$el.querySelector('.log-container');
    let div = this.$el.querySelector('.log-container div');
    console.log(logContainer.scrollTop)

    new WebSocket('ws://' + document.location.host + '/tomcat_log').onmessage = function (event) {
      //   $("#log-container div").append(event.data);
      div.innerHTML += event.data;// 接收服务端的实时日志并添加到HTML页面中

      //   $("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
      logContainer.scrollTop = div.clientHeight - logContainer.clientHeight; // 滚动条滚动到最低部
    };
  }
}
</script>