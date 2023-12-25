<template>
    <div class="api-selector" style="margin:0 auto;min-width:245px;">
      <Tabs value="name1">
        <TabPane label="接口数据" name="name1">
          <header>
            <div class="add" title="选择 API" @click="showSelector=true">+</div><span>数据源</span>
          </header>
          <Tree ref="treeAllApi" :data="treeAllApi" class="apiTree" empty-text="暂无选择 API，请点击上方 “+” 添加 API "></Tree>
        </TabPane>
        <TabPane label="静态数据" name="name2">静态数据 TODO</TabPane>
      </Tabs>

      <!-- 右键菜单 -->
      <Dropdown transfer ref="contentFileMenu" style="display: none;" trigger="click">
        <DropdownMenu slot="list" ref="pp">
          <DropdownItem @click.native="handleContextMenuTest">测试</DropdownItem>
          <Divider style="margin:0" />
          <DropdownItem @click.native="handleContextMenuDelete" style="color: #ed4014">移除</DropdownItem>
        </DropdownMenu>
      </Dropdown>

      <Modal v-model="showSelector" title="选择 API" ok-text="保存选择" @on-ok="selectHandler" cancel-text="关闭" width="470">
        <Tabs v-model="selectedTab" :animated="false">
          <TabPane label="数据服务" name="dataservice">
            <AutoComplete v-model="SwaggerFilter.keyword" :data="SwaggerFilter.data" :filter-method="auotCompleteFilter" @on-select="filterHandler" icon="ios-search" placeholder="请输入 API 之 URL"></AutoComplete>
            <Tree ref="treeDataservice" :data="DataServiceTreeData" :load-data="loadTablesCfg" show-checkbox class="apiTree"></Tree>
          </TabPane>

          <TabPane label="Swagger API" name="swagger">
            <AutoComplete v-model="SwaggerFilter.keyword" :data="SwaggerFilter.data" :filter-method="auotCompleteFilter" @on-select="filterHandler" icon="ios-search" placeholder="请输入 API 之 URL"></AutoComplete>
            <Tree ref="treeSwagger" :data="SwaggerTreeData" show-checkbox class="apiTree" @on-select-change="treeSwaggerHandler"></Tree>
          </TabPane>
        </Tabs>
      </Modal>
    </div>
</template>
<script lang="ts" src="./api-list.ts"></script>

<style lang="less" scoped>
.api-selector {
    .add:hover {
        color: #2d8cf0;
    }
    header {
        height: 38px;
        line-height: 38px;
        border-bottom: 1px solid lightgray;
        padding: 0 5px;
        .add {
            float: right;
            font-size: 30px;
            padding: 0;
            cursor: pointer;
        }
    }

    .ivu-tabs-bar {
        margin-bottom: 0;
    }

    .ivu-menu-submenu-title {
        border-bottom: 1px solid lightgray;
    }

    .ivu-tree-title {
        // overflow: hidden;
        // text-overflow: ellipsis;
        // white-space: nowrap;
        // max-width: 200px;
    }
    .ivu-tree li ul {
        padding-left: 8px;
    }
}

.http-method {
    padding: 1px 5px;
    margin-right: 5px;
    border-radius: 3px;
    font-size: 8px;

    &.get {
        color: #3175fe;
        border: 1px solid #3175fe;
    }

    &.post {
        color: green;
        border: 1px solid green;
    }
    &.put {
        color: rgb(224, 60, 254);
        border: 1px solid rgb(224, 60, 254);
    }
    &.delete {
        color: red;
        border: 1px solid red;
    }
}

.apiTree {
    max-height: 500px;
    overflow-y: auto;
    margin-top: 5px;
}

.api-type {
    padding: 1px 6px;
    margin-right: 5px;
    border-radius: 3px;
    font-size: 8px;
    border: 1px solid gray;
    &.swagger {
        border-color: #ffe1b3;
        background-color: #fcffe4;
        color: #fdc06e;
    }
    &.dataservice {
        padding: 1px 4px;
        border-color: #dc9ad8;
        background-color: #fae4f8;
        color: #dc9ad8;
    }
}
</style>