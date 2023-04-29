<template>
  <Card :bordered="false" dis-hover>
    <div style="margin: 20px 0;text-align: left;">
      <Tooltip class="ivu-ml" content="刷新" placement="top" style="float:right">
        <Icon size="20" type="ios-refresh" @click="getData" style="cursor: pointer;" />
      </Tooltip>
      <Button type="primary" icon="md-add" style="margin: -4px 0" @click="create">新增数据源</Button>
    </div>

    <Table :columns="list.columns" :data="list.data">
      <template slot-scope="{ row, index }" slot="action">
        <a @click="edit(row, index)">编辑</a>
        <Divider type="vertical" />
        <Poptip confirm transfer title="是否要删除此行？" @on-ok="del(index)">
          <a style="color:red;">删除</a>
        </Poptip>
      </template>
    </Table>

    <Modal v-model="form.isShow" :title="(form.isCreate ? '新建' : '修改') + '数据源 DataSource'" :loading="form.isLoading" ok-text="保存" @on-ok="createOrUpdate">
      <Form ref="editForm" :model="form.data" :rules="form.createRules" :label-width="120">
        <FormItem label="数据源名称：" prop="name">
          <Input v-model="form.data.name" placeholder="数据源名称或者简单的描述" />
        </FormItem>
        <FormItem label="数据源编码：" prop="urlDir">
          <Input v-model="form.data.urlDir" placeholder="数据源编码全局唯一不重复" />
        </FormItem>
        <FormItem label="数据库类型：" prop="type">
          <Select v-model="form.data.type">
            <Option v-for="(value, key) in DBType" :value="value" :key="value">{{ key }}</Option>
          </Select>
        </FormItem>
        <FormItem label="连接地址：" prop="url">
          <Input v-model="form.data.url" maxlength="200" show-word-limit type="textarea" :rows="4" placeholder="数据库连接 URL，注意无须携带用户名和密码，且不需要 URL 转义" />
        </FormItem>
        <FormItem label="登录用户：" prop="username">
          <Input v-model="form.data.username" placeholder="请输入数据库用户账号" />
        </FormItem>
        <FormItem label="登录密码：" prop="password">
          <Input v-model="form.data.password" type="password" password placeholder="请输入账号密码" />
        </FormItem>
        <FormItem label="是否跨库：" prop="crossDB">
           <i-switch v-model="form.data.crossDB" />
        </FormItem>
      </Form>
    </Modal>
  </Card>
</template>

<script lang="ts" src="./datasource.ts"></script>