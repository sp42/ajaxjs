<template>
  <div>
    <Row type="flex" align="middle" style="margin:20px;">
      <Col span="12">
      模型名称：<Input suffix="ios-search" placeholder="请输入" style="width: 400px" />
      </Col>
      <Col span="12" class="text-right">
      <Button type="primary" icon="ios-search" @click="handleAdd">查询</Button>
      <Button style="margin-left:10px" @click="handleAdd">重置</Button>
      </Col>
    </Row>

    <Card :bordered="false" dis-hover>
      <Row type="flex" align="middle" slot="title">
        <Col span="12">
        <Button :disabled="addNew" type="primary" icon="md-add" style="margin:-4px 0" @click="handleAdd">新增</Button>
        </Col>
        <Col span="12" class="text-right">
        <Tooltip class="ivu-ml" content="刷新" placement="top">
          <i-link @click.native="getData">
            <Icon size="20" type="ios-refresh" />
          </i-link>
        </Tooltip>
        </Col>
      </Row>

      <Table :data="tableData" :columns="columns">
        <template slot-scope="{ row, index }" slot="tableName">
          <Input type="text" v-model="editTableName" v-if="editIndex === index" placeholder="表名，必填" />
          <span v-else>{{ row.tableName }}</span>
        </template>

        <template slot-scope="{ row, index }" slot="fieldName">
          <Input type="text" v-model="editFieldName" v-if="editIndex === index" placeholder="JSON 所在的字段，必填" />
          <span v-else>{{ row.fieldName }}</span>
        </template>

        <template slot-scope="{ row, index }" slot="action">
          <div v-if="editIndex === index">
            <Button @click="handleSave(index)" type="success" ghost size="small"> {{ addNew ? '添加' : '保存'}} </Button>
            <Button @click="handleCancel" v-if="addNew" type="primary" ghost size="small" class="ivu-ml">取消</Button>
          </div>
          <div v-else>
            <!--  <a  @click="handleEdit(row, index)">启用</a> -->

            <a style="color:green;" @click="openDemo(row, index)">演示</a>
            <Divider type="vertical" />
            <a @click="openInfo(row, row.id)">编辑</a>
            <Divider type="vertical" />
            <Poptip confirm transfer title="是否要删除此行？" @on-ok="handleDelete(index)">
              <a style="color:red;">删除</a>
            </Poptip>
          </div>
        </template>
      </Table>

      <div class="ivu-mt ivu-text-right">
        <Page :total="total" :current.sync="current" show-total show-sizer :page-size="9" />
      </div>
    </Card>

    <Modal v-model="showDemo" :title="'演示 ' + demoTitle + ' 表单渲染'" :transfer="false" cancel-text="" width="700">
      <Renderer :data="demoData" />
    </Modal>

    <Modal v-model="showInfo" :title="'编辑 ' + demoTitle" :transfer="true" cancel-text="" width="1300">
      <Info ref="info" :api-root="apiRoot"></Info>
    </Modal>
  </div>
</template>

<script lang="ts" src="./ext-form.ts"></script>