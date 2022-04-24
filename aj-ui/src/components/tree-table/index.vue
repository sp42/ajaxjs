<!-- 树状结构的表格  -->
<template>
  <div>
    <Button type="primary" style="margin-bottom:10px;" @click="createTop">新建顶级{{name}}</Button>

    <Table row-key="id" :columns="columns16" :data="data" border size="small">
      <template slot="action" slot-scope="{ row }">
        <Button size="small" icon="md-add" type="success" @click="createTreeNode(row.name, row.id, row.parentId)">新建</Button>
        <Button size="small" icon="md-create" type="info" @click="editTreeNode(row)">编辑</Button>
        <Poptip transfer placement="left">
          <Button size="small" icon="md-trash" type="error">删除</Button>
          <div slot="content">
            <b>请选择删除模式：</b>
            <p>- 删除该{{name}}，如有子{{name}}调整到原父亲{{name}}上；</p>
            <p>- 删除该{{name}}以及所有子{{name}}，删除该节点并包括下级{{name}}</p>
            <br />
            <div style="text-align:center">
              <Button size="small" type="primary" @click="delTreeNode(row)">删除该{{name}}</Button>
              <Button size="small" type="primary" @click="delAllTreeNode">删除所有{{name}}</Button>
            </div>
          </div>
        </Poptip>
      </template>
    </Table>

    <Modal v-model="edit.isShowEdit" :title="getTitle" width="650" ok-text="保存" @on-ok="save">
      <p v-if="isCreateTop">你将新建顶级的{{name}}。</p>
      <p v-if="!isUpdate && !isCreateTop">你将在{{name}}[{{edit.parent}}]下面传新建子{{name}}。</p>

      <Form :label-width="100" v-model="edit.row" style="margin:5%">
        <FormItem :label="'父亲' + name" v-show="!isCreateTop">
          <TreeSelector ref="selectParent" :tree-json="data" />
        </FormItem>
        <FormItem :label="name + '名称'" prop="name">
          <Input type="text" v-model="edit.row.name" placeholder="名称为必填" />
        </FormItem>

        <FormItem v-for="(item, index) in formFields" :key="index" :label="item.name" :prop="item.prop">
          <Input type="text" v-model="edit.row[item.key]" :placeholder="item.placeholder" />
        </FormItem>

        <FormItem :label="name + '说明'" prop="content">
          <Input type="textarea" v-model="edit.row.content" :rows="4" placeholder="简介、说明、备注，选填" />
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script lang="ts" src="./index.ts"></script>

<style scoped>
Button {
  margin-right: 10px;
}
</style>