<template>
  <div>
    <SearchPanel :search="list.search" :cmp="this">
      <Input suffix="ios-search" placeholder="请输入名称的关键字" />
    </SearchPanel>
    <br />
    <div>
      <TagListPanel v-if="isShowTag">
        <span v-if="!isEmbed">
          数据源：<br />
          <Select v-model="datasource.id" style="margin: 10px 0 30px 0;">
            <Option v-for="item in datasource.list" :key="item.id" :value="item.id">{{ item.name }}</Option>
          </Select>
        </span>
      </TagListPanel>

      <Card :bordered="false" dis-hover :style="{float:'left', width: isShowTag? '85%':'100%'}">
        <div style="margin: 20px 0;text-align: left;">
          <Tooltip class="ivu-ml" content="刷新" placement="top" style="float:right">
            <Icon size="20" type="ios-refresh" @click="getData" style="cursor: pointer;" />
          </Tooltip>
          <Button type="primary" icon="md-add" style="margin: -4px 0" @click="showSelectTable">新建数据服务</Button>
          <Button style="margin-left: 10px" @click="refreshServerSide">刷新配置</Button>
        </div>

        <Table :columns="list.columns" :data="list.data" height="500">
          <template slot-scope="{ row }" slot="urlDir">
            <span v-if="!isEmbed">/{{dataSourceUrlDir}}</span>/{{row.urlDir}}
          </template>

          <template slot-scope="{ row, index }" slot="action">
            <a @click="openInfo(row)">配置</a>
            <Divider type="vertical" />
            <Poptip confirm transfer title="是否要删除此行？" @on-ok="handleDelete(index)">
              <a style="color:red;">删除</a>
            </Poptip>
          </template>
        </Table>

        <div class="ivu-mt ivu-text-right">
          <Page :total="list.total" :current.sync="list.current" show-total show-sizer :page-size="list.limit" @on-page-size-change="handleChangePageSize" />
        </div>
      </Card>
    </div>

    <!-- 选择表的窗体 -->
    <Modal v-model="selectTable.isShowDlg" title="选择77表" width="800" cancel-text="" ref="selectTable">
      <list-filter ref="selectTableList" :list-column="selectTable.column" :api-url="selectTable.api" @on-select="create" :dsid="datasource.id" />
    </Modal>
  </div>
</template>

<script lang="ts" src="./data-service.ts"></script>