<template>
  <div class="data-service">
    <nav>
      <div style="float:right;">
        数据源：<span>{{dataSource.name}}</span> | <a href="https://framework.ajaxjs.com" target="_blank">帮助</a> | <a @click="showAbout">关于</a>
      </div>
      <img src="~@/assets/icon.png" width="16" style="vertical-align: middle;" /> 数据服务 Data Service
    </nav>
    <Split v-model="split1" style="border-top: 1px solid lightgray;">
      <div slot="left" class="split-pane-left">
        <div class="search-panel ">
          <i-Input suffix="ios-search" placeholder="搜索数据服务……" style="width: 90%" />
        </div>
        <Tree ref="treeCmp" :data="treeData" :load-data="loadTreeData" style="height: 93%;overflow-y: auto;margin-left: 10px;" @on-contextmenu="handleContextMenu" @on-select-change="openTab">
          <template slot="contextMenu">
            <Dropdown-Item @click.native="handleContextMenuCreate" style="color:green">新建项目</Dropdown-Item>
            <Dropdown-Item @click.native="handleContextMenuEdit">编辑项目</Dropdown-Item>
            <Dropdown-Item @click.native="handleContextMenuDelete" style="color: #ed4014">删除项目</Dropdown-Item>
          </template>
        </Tree>
      </div>
      <div slot="right" class="split-pane-right">
        <ul class="toolbar">
          <li style="float:right" @click="refreshConfig">
            <div>
              <div class="icon">
                <Icon type="md-refresh" size="25" style="color:darkcyan" />
              </div>
              <span class="text">刷新配置</span>
            </div>
          </li>
          <li style="float:right" @click="dataSource.isShowDataSource=true">
            <div>
              <div class="icon">
                <Icon type="md-swap" size="25" style="color:blueviolet" />
              </div>
              <span class="text">数据源</span>
            </div>
          </li>
          <li @click="isShowSelectTable=true">
            <div>
              <div class="icon">
                <Icon type="md-add" size="25" style="color:green" />
              </div>
              <span class="text">新建服务</span>
            </div>
          </li>
          <li @click="del($event)" :class="{disabled: activeTab == 'index'}">
            <div>
              <div class="icon">
                <Icon type="md-close" size="25" style="color:red" />
              </div>
              <span class="text">删除</span>
            </div>
          </li>
          <li @click="saveDML" :class="{disabled: activeTab == 'index'}">
            <div>
              <div class="icon">
                <Icon type="ios-create" size="25" style="color:#f90" />
              </div>
              <span class="text">保存</span>
            </div>
          </li>

          <li @click="refreshTree">
            <div>
              <div class="icon">
                <Icon type="md-refresh" size="25" style="color:burlywood" />
              </div>
              <span class="text">重新加载</span>
            </div>
          </li>
        </ul>
        <Tabs ref="tab" class="content mainTab" name="mainTab" :value="activeTab" :animated="false" type="card" @on-click="onTabClick" @on-tab-remove="onTabClose">
          <tab-pane label="首页" name="index" :index="0" :closable="false" tab="mainTab" style="padding: 0 10px">
            <h1> Welcome to DataService</h1>
          </tab-pane>
          <tab-pane v-for="tab in mainTabs" :key="tab.label" :label="tab.label" :name="tab.name" :index="tab.index" :closable="tab.closable" tab="mainTab" style="padding: 0 10px;">
            <!--服务配置-->
            <info ref="info" :data="tab.data" />
          </tab-pane>
        </Tabs>
      </div>
    </Split>

    <Modal title="数据源配置" v-model="dataSource.isShowDataSource" ok-text="关闭" cancel-text="" width="900">
      <Datasource @change_datasource="changeDatasource"></Datasource>
    </Modal>

    <Modal title="编辑项目" v-model="project.isShowEditProjectWin" ok-text="保存" @on-ok="saveProject" cancel-text="" width="600">
      <i-Form ref="editForm" :model="project.form.data" :rules="project.form.rules" :label-width="120" style="margin-right:100px">
        <form-item label="项目名称" prop="name">
          <i-Input v-model="project.form.data.name" placeholder="项目名称" />
        </form-item>
        <form-item label="项目简介" prop="content">
          <i-Input type="textarea" :autosize="{minRows: 2,maxRows: 5}" v-model="project.form.data.content" />
        </form-item>
      </i-Form>
    </Modal>

    <!--选择表格-->
    <Modal title="从数据库的表定义选择" v-model="isShowSelectTable" ok-text="保存" cancel-text="" width="900">
      <table-selector :dsid="1" :is-cross-db="dataSource.crossDb" :data-source-id="dataSource.id"></table-selector>
    </Modal>

    <!--               <Modal v-model="showFields" title="选择字段" cancel-text="" width="370">
                    <ul class="showFieldsList">
                      <li v-for="(v, k) in fields" :key="k">
                        <label>
                          <div class="fieldsComments">{{table.fieldsComments[k]}}</div>
                          <a @click="table.fieldsMapping[toField] = k; showFields = false;">{{k}}</a>
                        </label>
                      </li>
                    </ul>
                  </Modal> -->
  </div>

</template>

<script src="./data-service.js"></script>

<style lang="less" scoped src="./data-service.less"></style>
<style lang="less">
.http-method {
  padding: 0px 2px;
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