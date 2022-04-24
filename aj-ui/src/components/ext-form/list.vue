<template>
  <div>
    <SearchPanel :search="list.search" :cmp="this">
      <Input suffix="ios-search" clearable placeholder="请输入名称的关键字" v-model="list.search.name" @on-enter="getData" />
      <Input suffix="ios-search" clearable placeholder="请输入表名的关键字" v-model="list.search.tableName" />
    </SearchPanel>

    <br />
    <div>
      <TagListPanel>
        数据源： <br />
        <Select v-model="datasource.id" style="margin: 10px 0 30px 0;">
          <Option v-for="item in datasource.list" :key="item.id" :value="item.id">{{ item.name }}</Option>
        </Select>
      </TagListPanel>

      <Card :bordered="false" dis-hover style="float:left; width: 85%">
        <div style="margin-bottom:20px;">
          <Tooltip class="ivu-ml" content="刷新" placement="top" style="float:right;padding-top:10px;">
            <Icon size="20" type="ios-refresh" @click="getData" style="cursor: pointer;" />
          </Tooltip>
          <Button type="primary" icon="md-add" @click="isShowFieldsSelect = true">新建扩展表单</Button>
        </div>

        <Table :columns="list.columns" :data="list.data" :loading="list.loading" height="500">
          <template slot-scope="{ row, index }" slot="action">
            <a @click="openDemo(row)">预览</a>
            <Divider type="vertical" />
            <a onclick="alert('TODO')">静态化</a>
            <Divider type="vertical" />
            <a @click="openInfo(row)">编辑</a>
            <Divider type="vertical" />
            <Poptip confirm transfer title="是否要删除此行？" @on-ok="deleteInfo(row.id, index)">
              <a style="color:red;">删除</a>
            </Poptip>
          </template>
        </Table>

        <div class="ivu-mt ivu-text-right">
          <Page :total="list.total" :current.sync="list.pageNo" show-total show-sizer :page-size="list.limit" @on-page-size-change="handleChangePageSize" />
        </div>
      </Card>
    </div>
    <Modal v-model="perview.isShow" :title="'预览 ' + perview.title + ' 表单渲染'" :transfer="false" cancel-text="" width="700">
      <Renderer :data="perview.data" />
    </Modal>

    <Modal v-model="isShowEdit" :title="'编辑 ' + perview.title" cancel-text="" width="1360">
      <Info ref="WidgetFactory" :api="API" :api-root="apiRoot" :is-ext-form-mode="true"></Info>
    </Modal>

    <Modal v-model="isShowFieldsSelect" title="选择表单 JSON 保存所在的字段" width="1000" @on-ok="getPersistenceInfo">
      <DataModelSelector ref="DataModelSelector" :api-root="apiRoot" :is-single-select="true"></DataModelSelector>
    </Modal>
  </div>
</template>

<script lang="ts" src="./list.ts"></script>