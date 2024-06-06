<template>
  <div class="main">
    <Menu
      class="left-panel"
      :open-names="SHOW_MOUDLE.expandMenu"
      v-if="SHOW_MOUDLE"
    >
      <h3>{{ SHOW_MOUDLE.title }}</h3>
      <Submenu name="portal-1" v-if="SHOW_MOUDLE.portal">
        <template slot="title">网站管理</template>
        <MenuItem name="1-1" @click.native="load = 'WebsiteConfig'"
          >站点管理</MenuItem
        >
        <MenuItem name="1-2" @click.native="load = 'OrgIndex'"
          >栏目管理</MenuItem
        >
      </Submenu>

      <Submenu name="portal-2" v-if="SHOW_MOUDLE.portal">
        <template slot="title">内容管理</template>
        <MenuItem name="2-1" @click.native="showList(103, '图文')"
          >图文列表</MenuItem
        >
        <MenuItem name="8-5" @click.native="routeTo('list?id=103&title=图文')"
          >CMS 图文</MenuItem
        >
        <MenuItem name="2-2" @click.native="showList(123)">附件列表</MenuItem>
        <MenuItem name="2-3" @click.native="load = 'TagMgr'"
          >标签/点赞/收藏管理</MenuItem
        >
      </Submenu>

      <!--   <Submenu name="3">
        <template slot="title">商城管理</template>
        <MenuItem name="3-1" @click.native="load='UserListIndex'">商品管理</MenuItem>
        <MenuItem name="3-2" @click.native="load='OrgIndex'">订单管理</MenuItem>
      </Submenu> -->

      <Submenu name="user-1" v-if="SHOW_MOUDLE.user">
        <template slot="title">用户组织管理</template>
        <MenuItem name="4-1" @click.native="load = 'UserListIndex'"
          >用户列表管理</MenuItem
        >
        <MenuItem name="4-2" @click.native="load = 'OrgIndex'"
          >组织机构管理</MenuItem
        >
        <MenuItem name="4-3" @click.native="load = 'RBAC'">权限管理</MenuItem>
      </Submenu>

      <Submenu name="user-2" v-if="SHOW_MOUDLE.user">
        <template slot="title">认证管理</template>
        <MenuItem name="user-2-1" @click.native="showList(130, '客户端应用')"
          >客户端应用</MenuItem
        >
        <MenuItem name="user-2-2" @click.native="showList(129, '租户管理')"
          >租户管理</MenuItem
        >
        <MenuItem name="user-2-3" @click.native="showList(131, 'Token 列表')"
          >Token 管理</MenuItem
        >
      </Submenu>

      <Submenu name="5" v-if="SHOW_MOUDLE.system">
        <template slot="title">系统管理</template>
        <MenuItem name="5-1" @click.native="load = 'DataDict'"
          >数据字典</MenuItem
        >
        <MenuItem name="5-2" @click.native="showList(125)">操作日志</MenuItem>
        <MenuItem name="5-3" @click.native="load = 'SysConfig'"
          >配置参数</MenuItem
        >
        <MenuItem name="5-4" @click.native="load = 'DeveloperTools'"
          >开发者工具</MenuItem
        >
      </Submenu>

      <Submenu name="model" v-if="SHOW_MOUDLE.model">
        <template slot="title">业务建模</template>
        <MenuItem name="6-1" @click.native="load = 'DataSource'"
          >数据源管理</MenuItem
        >
        <!--         <MenuItem name="6-2" @click.native="load='DataService'">数据服务</MenuItem> -->
        <MenuItem name="6-5" @click.native="load = 'DataService'"
          >数据服务</MenuItem
        >
        <MenuItem name="6-3" @click.native="load = 'ModelMgr'"
          >模型管理</MenuItem
        >
        <MenuItem name="6-4" @click.native="load = 'FactoryList'"
          >列表生成器</MenuItem
        >
        <!--         <MenuItem name="6-5" @click.native="load='DataServiceIndex'">Api Selector</MenuItem>
        <MenuItem name="6-8" @click.native="routeTo('api-helper')">Api Helper</MenuItem> -->
      </Submenu>
    </Menu>

    <div class="right-panel">
      <div
        v-if="!load"
        class="center"
        style="text-align: center; margin-top: 25%"
      >
        Welcome!
      </div>

      <span v-if="load == 'DataSource'">
        <h1 class="page-title">数据源管理</h1>
        <DataSource :api-root="apiRoot" />
      </span>

      <!-- <DataService v-if="load == 'DataService'" :api-root="apiRoot" /> -->

      <DataService v-if="load == 'DataService'" />

      <span v-if="load == 'ModelMgr'">
        <h1 class="page-title">模型管理</h1>
        <ModelMgr :api-root="apiRoot" />
      </span>

      <span v-if="load == 'FactoryList'">
        <h1 class="page-title">列表生成器</h1>
        <FactoryList :api-root="apiRoot" />
      </span>

      <FactoryListLoader v-if="load === 'UserList'" id="5" />
      <FactoryListLoader v-if="load === 'UserLog'" id="6" />

      <span v-if="load === 'showList'">
        <h1 class="page-title">{{ listTitle }}</h1>
        <FactoryListLoader :id="listId" style="margin: 1%" />
      </span>

      <WebsiteConfig v-if="load == 'WebsiteConfig'" />
      <DataDict v-if="load == 'DataDict'" />
      <SysConfig v-if="load == 'SysConfig'" />
      <RBAC v-if="load == 'RBAC'" />
      <DeveloperTools v-if="load == 'DeveloperTools'" />

      <span v-if="load == 'UserListIndex'">
        <h1 class="page-title">用户列表管理</h1>
        <UserListIndex v-if="load == 'UserListIndex'" />
      </span>

      <OrgIndex v-if="load == 'OrgIndex'" />
      <ApiHelper v-if="load == 'api-helper'" />
      <DataServiceIndex v-if="load == 'DataServiceIndex'" />
      <Permission v-if="load == 'Permission'" />
    </div>
  </div>
</template>

<script src="./index.js"></script>

<style>
.home h2,
.home p {
  max-width: 800px;
  margin: 10px auto;
}

html,
body,
.main > .ivu-menu {
  height: 100%;
}

/* 分页控件有点问题，修改下 */
.ivu-mt.ivu-text-right {
  text-align: right;
  margin-top: 20px;
}

h1.page-title {
  margin: 0 0 2% 1%;
  padding-bottom: 1%;
  border-bottom: 1px solid #eee;
  color: #2f518c;
  letter-spacing: 2px;
  height: 9%;
  line-height: 100px;
}
</style>

<style lang="less" scoped src="./index.less"></style>