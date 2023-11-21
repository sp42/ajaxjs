<template>
  <div class="main">
    <Menu :theme="theme2" style="float: left;">

      <Submenu name="1">
        <template slot="title">网站管理</template>
        <MenuItem name="1-1" @click.native="load='WebsiteConfig'">站点管理</MenuItem>
        <MenuItem name="1-2" @click.native="load='OrgIndex'">栏目管理</MenuItem>
      </Submenu>

      <Submenu name="2">
        <template slot="title">内容管理</template>
        <MenuItem name="2-1" @click.native="showList(103, '图文')">图文列表</MenuItem>
        <MenuItem name="8-5" @click.native="routeTo('list?id=103&title=图文')">CMS 图文</MenuItem>
        <MenuItem name="2-2" @click.native="showList(123)">附件列表</MenuItem>
        <MenuItem name="2-3" @click.native="load='TagMgr'">标签/点赞/收藏管理</MenuItem>
      </Submenu>

      <Submenu name="3">
        <template slot="title">商城管理</template>
        <MenuItem name="3-1" @click.native="load='UserListIndex'">商品管理</MenuItem>
        <MenuItem name="3-2" @click.native="load='OrgIndex'">订单管理</MenuItem>
      </Submenu>

      <Submenu name="4">
        <template slot="title">用户组织管理</template>
        <MenuItem name="4-1" @click.native="load='UserListIndex'">用户列表管理</MenuItem>
        <MenuItem name="4-2" @click.native="load='OrgIndex'">组织机构管理</MenuItem>
        <MenuItem name="4-3" @click.native="load='RBAC'">权限管理</MenuItem>
      </Submenu>

      <Submenu name="5">
        <template slot="title">系统管理</template>
        <MenuItem name="5-1" @click.native="load='DataDict'">数据字典</MenuItem>
        <MenuItem name="5-2" @click.native="showList(125)">操作日志</MenuItem>
        <MenuItem name="5-3" @click.native="load='SysConfig'">配置参数</MenuItem>
        <MenuItem name="5-4" @click.native="load='DeveloperTools'">开发者工具</MenuItem>
      </Submenu>

      <Submenu name="6">
        <template slot="title">业务建模</template>
        <MenuItem name="6-1" @click.native="load='DataSource'">数据源</MenuItem>
        <MenuItem name="6-2" @click.native="load='DataService'">数据服务</MenuItem>
        <MenuItem name="6-2" @click.native="load='DataServiceIndex'">数据服务 New</MenuItem>
        <MenuItem name="6-3" @click.native="load='ModelMgr'">模型管理</MenuItem>
        <MenuItem name="6-4" @click.native="load='FactoryList'">列表生成器</MenuItem>
        <MenuItem name="6-8" @click.native="routeTo('api-helper')">Api Helper</MenuItem>
      </Submenu>
    </Menu>
    <div style="float:left;width:83%;height: 100%;">
      <div v-if="!load" class="center" style="text-align:center;margin-top:25%;">Welcome!</div>
      <DataSource v-if="load == 'DataSource'" :api-root="apiRoot" />
      <DataService v-if="load == 'DataService'" :api-root="apiRoot" />
      <FactoryList v-if="load == 'FactoryList'" :api-root="apiRoot" />
      <ModelMgr v-if="load == 'ModelMgr'" :api-root="apiRoot" />

      <FactoryListLoader v-if="load === 'UserList'" id="5" />
      <FactoryListLoader v-if="load === 'UserLog'" id="6" />
      <FactoryListLoader v-if="load === 'showList'" :id="listId" style="margin:1%;" />

      <WebsiteConfig v-if="load == 'WebsiteConfig'" />
      <DataDict v-if="load == 'DataDict'" />
      <SysConfig v-if="load == 'SysConfig'" />
      <RBAC v-if="load == 'RBAC'" />
      <DeveloperTools v-if="load == 'DeveloperTools'" />
      <UserListIndex v-if="load == 'UserListIndex'" />
      <OrgIndex v-if="load == 'OrgIndex'" />
      <ApiHelper v-if="load == 'api-helper'" />
      <DataServiceIndex v-if="load == 'DataServiceIndex'" />

    </div>
  </div>
</template>
<script>
import DataSource from '../components/data-service/datasource/datasource.vue';
import DataService from '../components/data-service/data-service.vue';
import DataServiceIndex from '../components/data-service/index/index.vue';
import FactoryList from '../components/factory-list/list.vue';
import FactoryListLoader from '../components/factory-list/list-loader.vue';
import ModelMgr from '../components/factory-form/list.vue';
import DeveloperTools from '../components/admin-page/developer-tools/index.vue';
import DataDict from '../components/admin-page/system/data-dict.vue';
import SysConfig from '../components/admin-page/system/config.vue';
import RBAC from '../components/admin-page/user/rbac/rbac.vue';
import UserListIndex from '../components/admin-page/user/user-list-index.vue';
import OrgIndex from '../components/admin-page/user/org/index.vue';
import WebsiteConfig from '../components/admin-page/website/config.vue';
import ApiHelper from '../components/api-helper/api-helper.vue';

export default {
  components: { DataServiceIndex, DataSource, DataService,  FactoryList, FactoryListLoader,  ModelMgr, WebsiteConfig, DeveloperTools, DataDict, SysConfig, RBAC, UserListIndex, OrgIndex,ApiHelper },
  data() {
    return {
      load: 'FactoryList',
      apiRoot: window.config.dsApiRoot,
      theme2: 'light',
      listId: 0
    }
  },
  methods: {
    routeTo(route) {
      location.hash = '#/' + route;
    },
    open(route) {
      window.open('#/' + route);
    },
    showList(id, title) {
      this.load = 'showList';
      this.listId = id;
    }
  }

}
</script>

<style>

.home h2,
.home p {
  max-width: 800px;
  margin: 10px auto;
}
</style>
