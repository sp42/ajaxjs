<template>
  <!-- 左边 tab -->
  <div class="left-tab tab">
    <ul @click="onTabClk">
      <li class="active">
        <Icon size="20" type="ios-crop" />
        <br />
        布局
      </li>
      <li>
        <Icon size="20" type="md-contract" />
        <br />
        控件
      </li>
      <li>
        <Icon size="20" type="ios-keypad" />
        <br />
        模板
      </li>
    </ul>
    <div class="content">
      <section class="show">
        <AutoComplete clearable class="auto-complete" placeholder="搜索布局名称……" :data="widgetAutoCompleteData" :filter-method="filterMethod" @on-select="onWidgetSearched" @on-clear="resetSearch('widgetList')" />
        <WidgetList :main-cmp="mainCmp" :widget-def="widgetDef.layout" style="padding:0 16px;" />
      </section>

      <section>
        <AutoComplete clearable class="auto-complete" placeholder="搜索组件名称……" :data="widgetAutoCompleteData" :filter-method="filterMethod" @on-select="onWidgetSearched" @on-clear="resetSearch('widgetList')" />
        <Collapse v-model="widgetListPane" simple accordion class="component-list">
          <Panel name="0">
            常用组件
            <div slot="content" style="clear:both;">
              <h3>基础</h3>
              <WidgetList :main-cmp="mainCmp" :widget-def="widgetDef.base_often" />
              <h3>表单</h3>
              <WidgetList :main-cmp="mainCmp" :widget-def="widgetDef.form_often" />

            </div>
          </Panel>

          <Panel name="1">
            基础组件
            <p slot="content">
              <WidgetList :main-cmp="mainCmp" :widget-def="widgetDef.base" />
            </p>
          </Panel>
          <Panel name="2">
            表单组件
            <p slot="content">
              <WidgetList :main-cmp="mainCmp" :widget-def="widgetDef.form" />
            </p>
          </Panel>
          <Panel name="3">
            高级组件
            <p slot="content">
              <WidgetList :main-cmp="mainCmp" :widget-def="widgetDef.adv" />
            </p>
          </Panel>
        </Collapse>
      </section>

      <section>
        <Tabs :animated="false">
          <TabPane label="公共模板">
            <AutoComplete class="auto-complete" placeholder="搜索模板……" style="padding: 10px;" />
            <Collapse simple accordion class="component-list" value="0">
              <Panel name="0">
                表单模板
                <p slot="content">表单模板</p>
              </Panel>
              <Panel name="1">
                列表模板
                <p slot="content">列表模板</p>
              </Panel>
              <Panel name="2">
                详情模板
                <p slot="content">详情</p>
              </Panel>
            </Collapse>
          </TabPane>
          <TabPane label="项目模板">
            项目模板
          </TabPane>
        </Tabs>
      </section>
    </div>
  </div>
</template>

<script lang="ts" src="./tab-left.ts"></script>

<style lang="less" scoped src="./tab.less"></style>

<style lang="less">
.component-list {
  .ivu-collapse-header {
    text-align: left;
  }
}

.ivu-select-item {
  text-align: left;
}

.ivu-auto-complete.ivu-select-dropdown {
  max-height: 400px;
}
</style>