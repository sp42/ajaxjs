<template>
  <div>
    <SearchPanel :search="list.search" :cmp="this">
      <Input suffix="ios-search" v-model="list.search.name" @on-enter="getData" clearable placeholder="请输入列表名称" />
    </SearchPanel>
    <br />
    <div>
      <!--     <TagListPanel /> -->

      <!--     <Card :bordered="false" dis-hover style="float:left; width: 85%"> -->
      <Card :bordered="false" dis-hover>
        <div style="margin-bottom:20px;">
          <Tooltip class="ivu-ml" content="刷新" placement="top" style="float:right;padding-top:10px;">
            <Icon size="20" type="ios-refresh" @click="getData" style="cursor: pointer;" />
          </Tooltip>
          <Button type="primary" icon="md-add" @click="$router.push({ path: 'factory-list-info'})">新建列表</Button>
        </div>

        <Table :columns="list.columns" :data="list.data" :loading="list.loading" height="500">
          <template slot-scope="{ row, index }" slot="action">
            <a @click="openDemo(row.id)">预览</a>
            <Divider type="vertical" />
            <a style="color:green;" @click="goInfo(row.id, 'factory-list-info')">编辑</a>
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

    <Modal v-model="isShowEdit" title="编辑" width="1300" ok-text="关闭" cancel-text="">

      <ListFactory ref="WidgetFactory" :api-root="apiRoot" :api="api" />

    </Modal>
  </div>
</template>

<script lang="ts" src="./list.ts"></script>