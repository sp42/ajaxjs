<template>
  <div>
    <div style="margin-bottom: 20px;">
      <span style="float:right;">
        <Button @click="showCreate" v-if="!isPickup" type="primary" icon="ios-add" style="margin-right: 10px;">增加权限</Button>
        <Button @click="getData" icon="ios-refresh">刷新</Button>
      </span>
      <Input style="width: 30%;" @on-search="doSearch" search enter-button placeholder="搜索权限的名称或者说明" />
    </div>
    <Table border :columns="columnsDef" :data="list.data">
      <template slot-scope="{ row }" slot="action">
        <a v-if="isPickup" style="margin-right: 5px" @click="onPickup(row)">选择</a>
        <span v-if="!isPickup">
          <Poptip confirm title="确定删除？" @on-ok="doDelete(row.id)">
            <a style="margin-right: 5px;color:red" icon="ios-trash">删除</a>
          </Poptip>
          |
          <a style="margin-right: 5px;color:green;" @click="edit(row.id)" icon="ios-edit">编辑</a>
        </span>
      </template>
    </Table>

    <Page style="margin:20px auto;text-align: center;" :total="list.total" :current.sync="list.current" :page-size="list.limit" @on-page-size-change="handleChangePageSize" size="small" show-total show-elevator show-sizer />

    <Modal v-model="isShowEditWin" :title="isCreate ? '创建权限' : '编辑权限' + permissionData.id" width="600" @on-ok="save">
      <Form :model="permissionData" :rules="ruleValidate" :label-width="100" style="margin-right: 10%;margin-left: 3%;">
        <FormItem label="权限名称" prop="name">
          <Input v-model="permissionData.name" placeholder="请输入权限名称……"></Input>
        </FormItem>
        <FormItem label="权限编码">
          <Input v-model="permissionData.code" placeholder="请输入权限编码……"></Input>
        </FormItem>
        <FormItem label="权限说明">
          <Input type="textarea" :rows="4" v-model="permissionData.content" placeholder="请输入权限说明……"></Input>
        </FormItem>
        <FormItem label="权限状态">
          <label><input type="radio" v-model="permissionData.stat" value="0" /> 启用</label> &nbsp;
          <label><input type="radio" v-model="permissionData.stat" value="2" /> 禁用</label>
        </FormItem>
        <FormItem v-if="!isCreate" style="color:gray;">
          创建于 {{ permissionData.createDate | formatDate }} 修改于 {{ permissionData.updateDate | formatDate }}
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script lang="ts" src="./permission-list.ts"></script>