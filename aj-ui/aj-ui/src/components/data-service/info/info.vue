<template>
  <div>
    <div>
      <b>实体表名：</b> {{ table.tableName }} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>URL 目录：</b>
      <span v-if="!!table.dataSourceUrlDir">/{{table.dataSourceUrlDir}}/ </span>
      <Input type="text" v-model="table.urlDir" style="width: 200px" placeholder="该实体的 URL 目录，必填的" />
      &nbsp;&nbsp;&nbsp;
      <Button type="primary" @click="saveOrUpdate">
        <Icon type="md-checkmark" /> 保存
      </Button>
      &nbsp;&nbsp;&nbsp;
      <a @click="$emit('input', 'foo');$router.back();" style="float: right;padding-right: 10px;padding-top: 10px;">返回</a>
      &nbsp;&nbsp;&nbsp;
      <a @click="isShowCfgDlg = true">其他配置</a>
    </div>
    <br />

    <table border="0" width="100%" align="center" class="ds-info-table">
      <tr>
        <th width="230">操作</th>
        <th width="330">配置</th>
        <th width="430">SQL 语句</th>
        <th>API 接口</th>
      </tr>
      <!-- 单个记录查询 -->
      <tr is="Info" :table="table" :dml="allDml.info"></tr>

      <!-- ----列表查询---- -->
      <tr is="List" :table="table" :dml="allDml.list"></tr>

      <!-- -----新增实体------ -->
      <tr is="Create" :table="table" :dml="allDml.create"></tr>

      <!-- -----修改实体------ -->
      <tr is="Update" :table="table" :dml="allDml.update"></tr>

      <!-------------- 删除---------- -->
      <tr is="Delete" :table="table" :dml="allDml.delete"></tr>

      <!--------------自定义操作 ----------->
      <tr is="Custom" v-for="(item, i) in allDml.others" :table="table" :dml="item" :index="i" :key="i"></tr>

      <!--------------新增自定义操作 ----------->
      <tr>
        <td class="first">
          <Icon type="ios-add-circle" style="color:green;" />
          <a @click="addOther"> 新增自定义操作</a>
        </td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
    </table>

    <Modal v-model="isShowCfgDlg" title="其他配置" ok-text="确定" cancel-text="" width="500">
      <CfgPane :table="table" :all-dml="allDml" :fields="fields" />
    </Modal>
  </div>
</template>

<script lang="ts" src="./info.ts"></script>

<style lang="less" src="./info.less"></style>