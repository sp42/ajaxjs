<template>
  <div>
    <!-- 搜索表单 -->
    <div style="margin:10px 0 30px;" v-if="searchFields && searchFields.length">
      <span style="float:right;margin-top:10px;">
        <Button type="primary" icon="ios-search">查询</Button>
        <Button style="margin-left:10px">重置</Button>
      </span>

      <span v-for="(item, index) in searchFields" :key="index">
        <DatePicker v-if="item.type == 'date'" type="date" class="searchFieldInput" :placeholder="item.field" :name="item.name" />
        <Input v-else suffix="ios-search" class="searchFieldInput" :placeholder="'请输入' + item.field + '关键字'" :name="item.name" />
      </span>
    </div>

    <!-- 功能按钮 -->
    <Row type="flex" align="middle" slot="title" v-if="cfg.toolbarButtons && cfg.toolbarButtons.length">
      <Col span="12">
      <Button v-for="(item, index) in cfg.toolbarButtons" :key="index" style="margin-right:10px"  @click="btnClk(item.event)">{{item.text}}</Button>
      <!-- <Button :disabled="true" icon="md-delete" style="margin: 0 10px">删除</Button> -->
      </Col>
      <Col span="12" style="text-align:right;">
      <Tooltip class="ivu-ml" content="刷新" placement="top">
        <Icon size="20" type="ios-refresh" @click="getData" style="cursor: pointer;" />
      </Tooltip>
      </Col>
    </Row>

    <Table :loading="list.loading" :data="list.data" :columns="col" style="margin-top:20px;">
      <template slot-scope="{ row, index }" slot="action">
        <span v-for="(item, _index) in cfg.actionButtons" :key="_index">
          <a @click="btnClk(item.event, row, index)">{{item.text}}</a>  <Divider v-if="_index != (cfg.actionButtons.length - 1)" type="vertical" />
        </span>
      </template>
    </Table>

    <div class="ivu-mt ivu-text-right" v-if="cfg.isPage">
      <Page :total="list.total" :current.sync="list.pageNo" show-total show-sizer :page-size="list.pageSize" />
    </div>

    <Modal v-model="isShowForm" :title="form.title" width="1100" ok-text="保存" @on-ok="formSave">
      <FromRenderer ref="FromRenderer" :cfg="form.cfg" :fields="form.fields" />
    </Modal>
  </div>
</template>

<script lang="ts" src="./list-factory-renderer.ts"></script>

<style lang="less" scoped>
.searchFieldInput {
  width: 240px;
  margin: 10px 10px 0px 0;
}
</style>