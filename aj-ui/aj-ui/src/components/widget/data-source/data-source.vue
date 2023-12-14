<template>
  <div class="datasource" style="overflow: hidden">
    <div class="left">
      <ul>
        <li v-for="datasource in datasources" :key="datasource.id" @click="active(datasource)" :class="{ actived: activedItem == datasource.id }">
          <span>
            <Icon type="md-trash" class="del-icon" title="删除此数据源" @click="del(datasource.id, datasource.name)" />
          </span>
          {{datasource.name}}
        </li>
      </ul>
      <a class="add" @click="add">+ 新建……</a>
    </div>
    <div class="right">
      <i-Form ref="editForm" :model="form.data" :rules="form.rules" :label-width="120" style="margin-right:100px">
        <form-item label="数据源名称" prop="name">
          <i-Input v-model="form.data.name" placeholder="数据源名称" />
        </form-item>
        <form-item label="数据源编码" prop="urlDir">
          <i-Input v-model="form.data.urlDir" placeholder="数据源编码全局唯一不重复" />
        </form-item>
        <form-item label="数据库类型" prop="type">
          <i-Select v-model="form.data.type">
            <i-Option v-for="(value, key) in DBType" :value="key" :key="key">{{ value }}</i-Option>
          </i-Select>
        </form-item>
        <form-item label="连接地址" prop="url">
          <i-Input v-model="form.data.url" maxlength="200" show-word-limit type="textarea" :rows="4" placeholder="数据库连接 URL，注意无须携带用户名和密码，且不需要 URL 转义" />
        </form-item>
        <form-item label="登录用户" prop="username">
          <i-Input v-model="form.data.username" placeholder="请输入数据库用户账号" />
        </form-item>
        <form-item label="登录密码" prop="password">
          <i-Input v-model="form.data.password" type="password" password placeholder="请输入账号密码" />
        </form-item>
        <form-item label="是否跨库" prop="crossDb">
          <i-switch v-model="form.data.crossDb" />
        </form-item>
      </i-Form>

      <div align="center">
        <i-Button v-if="!activedItem" icon="md-checkmark" @click="create">保存新建</i-Button>
        <i-Button v-if="activedItem" @click="update">保存</i-Button>&nbsp;&nbsp;
        <i-Button v-if="activedItem" @click="test">测试连接</i-Button>&nbsp;&nbsp;
        <i-Button v-if="activedItem" @click="$emit('change_datasource', form.data)">切换</i-Button>
      </div>
      <!--                 <i-Input v-model="editing.name" ref="inputName">
                <span slot="prepend">环境名称</span>
            </i-Input>
            <br />
            <i-Input v-model="editing.urlPrefix">
                <span slot="prepend">URL前缀</span>
            </i-Input> -->
    </div>
  </div>
</template>

<script src="./data-source.js"></script>
<style lang="less" scoped src="./data-source.less"></style>