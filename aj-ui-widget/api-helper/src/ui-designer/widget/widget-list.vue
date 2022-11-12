<template>
  <!-- 左侧的控件选择 -->
  <draggable v-model="widgetDef" v-bind="{ sort: false, group: { name: 'AnyDrop', pull: 'clone', put: false }}" animation="300" force-fallback="true" handle="li" chosen-class="chosen" ghost-class="ghost" @start="mainCmp.STAGE_RENDER.ActiveWdiget = null" :clone="onWidgetDrop">
    <transition-group tag="ul">
      <li v-for="(item) in widgetDef" :key="item.name" :class="'fromDefine ' + item.type" :style="'display:' + (item.isHide ? 'none' : 'inline-block')">
        <Icon :type="item.icon" size="25" /><br /> {{item.eng_name}} <br /> <span class="Chinese">{{item.name}}</span>
      </li>
    </transition-group>
  </draggable>
</template>

<script lang="ts">
import draggable from "vuedraggable";
import CommFn from "../common-function";
import OnDrop from "../meta/widget-on-drop";

export default {
  components: { draggable },
  props: {
    mainCmp: { required: true },
    widgetDef: { type: Array, required: true },
  },
  methods: {
    /**
     * WidgetDefinition 转换为 RenderedMeta
     */
    onWidgetDrop(def: WidgetDefinition): RenderedMeta {
      console.log("WidgetDefinition 转换为 RenderedMeta", def);

      let _def: RenderedMeta = {
        ...def,
        uid: CommFn.getUid(),
      };

      let d: WidgetDefinition = <WidgetDefinition>(<unknown>_def);
      delete d.eng_name;
      delete d.icon;
      delete d.isHtmlTag;

      let fn: Function | null = OnDrop[def.type];
      fn && fn.call(this, _def);

      // if (def.type == "Row") {
      //   // 特定控件的某些扩展，复合控件
      //   _def.children = [
      //     {
      //       type: "Col",
      //       name: "栅格列容器",
      //       uid: CommFn.getUid(),
      //       children: [],
      //     },
      //     {
      //       type: "Col",
      //       name: "栅格列容器",
      //       uid: CommFn.getUid(),
      //       children: [],
      //     },
      //   ];
      // }
      // if (def.type == "Tabs") {
      //   // 特定控件的某些扩展，复合控件
      //   _def.children = [
      //     {
      //       type: "TabPane",
      //       name: "TabPane",
      //       uid: CommFn.getUid(),
      //       props: {
      //         label: "Tab 1",
      //       },
      //       children: [],
      //     },
      //     {
      //       type: "TabPane",
      //       name: "TabPane",
      //       uid: CommFn.getUid(),
      //       props: {
      //         label: "Tab 2",
      //       },
      //       children: [],
      //     },
      //   ];
      // }

      return _def;
    },
  },
};
</script>

<style lang="less" scoped>
ul {
  text-align: left;
}
li {
  cursor: move;
  // float: left;
  display: inline-block;
  background-color: rgba(23, 35, 61, 0.05);
  padding-top: 10px;
  border-radius: 4px;
  width: 30%;
  height: 73px;
  margin: 0 3% 1% 0;
  overflow: hidden;
  font-size: 8pt;
  text-align: center;
  &:hover {
    color: #467de3;
    span.Chinese {
      color: #467de3;
    }
  }
}
span.Chinese {
  color: gray;
}
</style>