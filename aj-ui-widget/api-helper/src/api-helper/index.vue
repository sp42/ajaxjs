<template>
  <div style="clear: both;margin: 30px; border:1px solid lightgray;padding:10px 5px">
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
          <Divider type="vertical" />
          {{ url.prefix }}
        </span>
      </Input>
      <Button style="width: 8%" type="primary">发送</Button>
      <Button style="width: 8%">保存</Button>
    </section>

    <Document />

    <Tabs name="request" class="request" value="raw" :animated="false">
      <TabPane label="请求体 Form" name="form" tab="request">
        <div style="margin:10px 0;">
          content-type
          <Select
            style="width:260px;"
            size="small"
            transfer
            v-model="requestParams.form.contentType"
          >
            <Option value="application/x-www-form-urlencoded">application/x-www-form-urlencoded</Option>
            <Option value="multipart/form-data">multipart/form-data</Option>
          </Select>
        </div>
        <InputTable :data="requestParams.form.data" />
      </TabPane>
      <TabPane label="请求体 Raw" name="raw" tab="request">
        <div style="margin:10px 0;">
          <a style="float:right;" @click="formatJs">格式化</a>
          content-type
          <Select
            style="width:260px;"
            size="small"
            transfer
            v-model="requestParams.raw.contentType"
          >
            <Option value="application/json">application/json</Option>
            <Option value="application/x-www-form-urlencoded">application/x-www-form-urlencoded</Option>
            <Option value="multipart/form-data">multipart/form-data</Option>
            <Option value="text/plain">text/plain</Option>
            <Option value="text/plain">text/xml</Option>
          </Select>
        </div>

        <!-- JSON 源码编辑器-->
        <codemirror
          class="code-editor"
          ref="cm"
          v-model="requestParams.raw.json"
          :options="cmOption"
          style="height:200px;"
        ></codemirror>
      </TabPane>
      <TabPane tab="request" label="查询参数 Query" name="name3">
        <InputTable :data="requestParams.queryString" />
      </TabPane>
      <TabPane label="头部参数" name="head" tab="request">
        <InputTable :data="requestParams.head" />
        <div style="float:right;margin-top:10px;">
          <a @click="authToken">一键生成 Bear Token</a>
        </div>
      </TabPane>
    </Tabs>

    <Tabs class="response" name="response" value="responseContent" :animated="false">
      <TabPane tab="response" label="返回内容" name="responseContent">
        <!-- JSON 源码编辑器-->
        <codemirror
          class="code-editor"
          ref="cm"
          v-model="responseBody"
          :options="cmOption"
          style="height:300px;"
        ></codemirror>
      </TabPane>
      <TabPane tab="response" label="返回头部" name="responseHead">
        <Input type="textarea" :rows="4" :readonly="true" v-model="responseHead" />
      </TabPane>
      <TabPane tab="response" label="请求信息" name="requestHead">
        <Input type="textarea" :rows="4" :readonly="true" v-model="requestAll" />
      </TabPane>
    </Tabs>
  </div>
</template>

<script src="./index.js"></script>

<style lang="less" scoped>
.request {
  clear: both;
}

.url-bar > * {
  float: left;
  margin-right: 10px;
}
</style>