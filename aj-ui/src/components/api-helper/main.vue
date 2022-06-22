<template>
  <div style="clear: both">
    <section class="url-bar">
      <Input v-model="url.main" style="width: 70%">
      <span slot="prepend">
        <Select v-model="httpMethod" style="width: 80px">
          <Option value="GET">GET</Option>
          <Option value="POST">POST</Option>
          <Option value="PUT">PUT</Option>
          <Option value="DELETE">DELETE</Option>
          <Option value="PATCH">PATCH</Option>
          <Option value="HEAD">HEAD</Option>
          <Option value="OPTION">OPTION</Option>
        </Select>
        <Divider type="vertical" />{{ url.prefix }}
      </span>
      </Input>
      <Button style="width: 8%" type="primary">发送</Button>
      <Button style="width: 8%">保存</Button>
    </section>

    <Document />

    <Tabs name="request" class="request" value="name2" :animated="false">
      <TabPane label="头部参数" name="name1" tab="request">标签一的内容</TabPane>
      <TabPane label="请求体" name="name2" tab="request">
        <RequestBody />
      </TabPane>
      <TabPane tab="request" label="查询参数" name="name3">标签三的内容</TabPane>
      <TabPane tab="request" label="路径参数" name="name4">标签三的内容</TabPane>
    </Tabs>

    <Tabs class="response" name="response" value="requestHead" :animated="false">
      <TabPane tab="response" label="请求头部" name="requestHead">标签一的内容</TabPane>
      <TabPane tab="response" label="请求内容" name="requestBody">标签二的内容</TabPane>
      <TabPane tab="response" label="返回头部" name="responseHead">标签三的内容</TabPane>
      <TabPane tab="response" label="返回内容" name="responseContent">标签三的内容</TabPane>
    </Tabs>
  </div>
</template>

<script>
import RequestBody from "./request-body.vue";
import Document from "./document/document.vue";

export default {
  components: { RequestBody, Document },
  data() {
    return {
      url: {
        prefix: "https://dd.com",
        main: "/dfdf",
      },
      httpMethod: "GET",
    };
  },
};
</script>


<style lang="less" scoped>
.request {
  clear: both;
}

.url-bar > * {
  float: left;
  margin-right: 10px;
}
</style>