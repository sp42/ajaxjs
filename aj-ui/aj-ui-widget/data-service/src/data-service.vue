<template>
  <div class="data-service">
    <nav>
      <div style="float:right;">
        数据源：
        <span>{{dataSource.name}}</span> |
        <a href="https://framework.ajaxjs.com" target="_blank">帮助</a> |
        <a @click="showAbout">关于</a>
      </div>
      <img src="./assets/icon/icon.png" width="16" style="vertical-align: middle;" /> 数据服务 Data Service
    </nav>
    <Split v-model="split1" style="border-top: 1px solid lightgray;">
      <div slot="left" class="split-pane-left">
        <div class="search-panel">
          <i-Input suffix="ios-search" placeholder="搜索数据服务……" style="width: 90%" />
        </div>
        <Tree
          :data="project.treeData"
          :load-data="loadTreeData"
          style="margin-left: 10px;height: 93%;overflow: auto;"
          @on-contextmenu="handleContextMenu"
        >
          <template slot="contextMenu">
            <Dropdown-Item @click.native="handleContextMenuCreate" style="color:green">新建项目</Dropdown-Item>
            <Dropdown-Item @click.native="handleContextMenuEdit">编辑项目</Dropdown-Item>
            <Dropdown-Item @click.native="handleContextMenuDelete" style="color: #ed4014">删除项目</Dropdown-Item>
          </template>
        </Tree>
      </div>
      <div slot="right" class="split-pane-right">
        <Toolbar />
        <div class="content">
          <Tabs
            class="mainTab"
            name="mainTab"
            :value="activeTab"
            :animated="false"
            type="card"
            @on-click="ifAdd"
          >
            <tab-pane
              label="首页"
              name="index"
              :index="0"
              :closable="true"
              tab="mainTab"
              style="padding: 0 10px"
            >
              <h1>Welcome to DataService</h1>
            </tab-pane>
            <tab-pane
              v-for="tab in mainTabs"
              :key="tab.label"
              :label="tab.label"
              :name="tab.name"
              :index="tab.index"
              :closable="tab.closable"
              tab="mainTab"
              style="padding: 0 10px;"
            >
              <Info :current-data="currentData" />
              <Edit :current-data="currentData" :current-dml="currentDML" :main="{}" />
            </tab-pane>
          </Tabs>
        </div>

        <!-- <div class="status-bar">Sss</div> -->
      </div>
    </Split>

    <Modal title="数据源配置" v-model="dataSource.isShowDataSource" ok-text="关闭" cancel-text width="900">
      <Datasource @change_datasource="changeDatasource"></Datasource>
    </Modal>

    <Modal
      title="编辑项目"
      v-model="project.isShowEditProjectWin"
      ok-text="保存"
      @on-ok="saveProject"
      cancel-text
      width="600"
    >
      <i-Form
        ref="editForm"
        :model="project.form.data"
        :rules="project.form.rules"
        :label-width="120"
        style="margin-right:100px"
      >
        <form-item label="项目名称" prop="name">
          <i-Input v-model="project.form.data.name" placeholder="项目名称" />
        </form-item>
        <form-item label="项目简介" prop="content">
          <i-Input
            type="textarea"
            :autosize="{minRows: 2,maxRows: 5}"
            v-model="project.form.data.content"
          />
        </form-item>
      </i-Form>
    </Modal>

    <Modal title="从数据库的表定义选择" v-model="isShowSelectTable" ok-text="保存" cancel-text width="900">
      <TableSelector :is-cross-db="dataSource.crossDb" :data-source-id="dataSource.id" />
    </Modal>
  </div>
</template>

<script src="./data-service.js"></script> 

<style lang="less" scoped>
nav {
  height: 30px;
  line-height: 30px;
  padding: 0 15px;
  border-bottom: 1px solid white;
  background-color: #e3e3e3;
  background-image: linear-gradient(#fefefe, #d8d8d8);
  text-shadow: 1px 1px 1px white;
}

.split-pane-right {
  padding-left: 5px;
}

.content {
  border-top: 1px solid lightgray;
  padding: 1%;
}

.search-panel {
  border-bottom: 1px solid lightgray;
  background-image: linear-gradient(#fefefe, #e6e6e6);
  height: 66px;
  padding-top: 14px;
  text-align: center;
}

.status-bar {
  position: absolute;
  bottom: 0;
  left: 0;
}

table.layout {
  border-collapse: collapse;
  border-spacing: 0;

  td {
    border-collapse: collapse;
    border-spacing: 0;
  }
}

.command-list {
  .selected {
    font-weight: bold;
  }
}
</style>
<style lang="less">
html,
body,
.data-service {
  overflow: hidden;
  height: 100%;
}

.http-method {
  padding: 1px 6px;
  margin-right: 5px;
  border-radius: 3px;
  font-size: 8px;
  vertical-align: middle;

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
</style>