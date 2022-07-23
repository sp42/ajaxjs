<template>
  <div class="container">
    <div class="left">
      <Tabs value="history" :animated="false">
        <TabPane label="收藏" name="bookmark">
          <Bookmark />
        </TabPane>
        <TabPane label="历史" name="history">
          <History />
        </TabPane>
      </Tabs>
    </div>

    <div class="right">
      <nav>
        MyAPI
        <Icon type="md-help-circle" style="margin: 10px 10px 0 10px; float: right; cursor: pointer" size="19" @click="isShowAbout = true" />
        <Dropdown style="margin-left: 10px; float: right" @on-click="onSelectEnvClk" on-click="onSelectEnvClk">
          <Button size="small" style="width: 150px">
            选择环境...
            <Icon type="ios-arrow-down"></Icon>
          </Button>
          <DropdownMenu slot="list" style="width: 150px">
            <DropdownItem>驴打滚</DropdownItem>
            <DropdownItem>炸酱面</DropdownItem>
            <DropdownItem disabled>豆汁儿</DropdownItem>
            <DropdownItem>冰糖葫芦</DropdownItem>
            <DropdownItem name="config" divided style="color: #2d8cf0">配置环境...</DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </nav>
      <Tabs class="mainTab" name="mainTab" :value="activeTab" :animated="false" type="card" @on-click="ifAdd">
        <TabPane v-for="tab in mainTabs" :key="tab.label" :label="tab.label" :name="tab.name" :index="tab.index" :closable="tab.closable" tab="mainTab" style="padding: 0 10px">
          <Main />
        </TabPane>
      </Tabs>
    </div>
    <Modal title="配置环境" v-model="isShowEnv" ok-text="保存" cancel-text="" width="900">
      <Env />
    </Modal>

    <Modal v-model="isShowAbout" title="关于" cancel-text="">
      ajaxjs.com 倾情奉献
    </Modal>
    <Modal v-model="isShowArgs" title="参数详情" cancel-text="">
      <Argument />
    </Modal>
  </div>
</template>

<script lang="ts">
import RequestBody from "./request-body.vue";
import Main from "./main.vue";
import Env from "./env.vue";
import History from "./history.vue";
import Argument from "./common/arg.vue";
import Bookmark from "./bookmark.vue";

const addTabBtn: any = { label: "+", name: "addTab", closable: false };

export default {
  components: { RequestBody, Main, Env, History, Argument, Bookmark },
  data() {
    return {
      activeTab: "tab1",
      mainTabs: [
        {
          label: "获取新闻详情",
          name: "tab1",
          closable: true,
        },
        addTabBtn,
      ],
      isShowEnv: false,
      isShowAbout: false,
      isShowArgs: false,
    };
  },
  methods: {
    ifAdd(name: string): void {
      if (name === "addTab") {
        this.mainTabs.pop(); // 先删除，再增加

        let tabName: string = "tab" + (this.mainTabs.length + 1);
        this.mainTabs.push({
          label: "新 API",
          name: tabName,
          closable: true,
        });

        setTimeout(() => {
          this.mainTabs.push(addTabBtn);
          this.activeTab = tabName;
        }, 100);
      }
    },

    onSelectEnvClk(name: string): void {
      if (name === "config") {
        this.isShowEnv = true;
      }
    },
  },
};

var oReq = new XMLHttpRequest();
oReq.open("GET", "https://www.qq.com/");
oReq.send();
</script>

<style>
.mainTab
  > .ivu-tabs-bar
  > .ivu-tabs-nav-container
  > .ivu-tabs-nav-wrap
  > .ivu-tabs-nav-scroll {
  padding-left: 20px;
  margin-top: 3px;
}
</style>

<style lang="less" src="../common-css/left-right-layout.less"></style>

<style lang="less" scoped>
nav {
  height: 36px;
  line-height: 36px;
  padding: 0 10px;
  background-color: #f5f5f5;
  border-bottom: 1px solid lightgray;
}
</style>