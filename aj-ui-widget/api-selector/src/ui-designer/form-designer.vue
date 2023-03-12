<template>
  <!-- 表单设计器 -->
  <div class="form-designer">
    <TopBar />
    <div class="main-body">
      <LeftTab :main-cmp="this" />

      <div class="stage">
        <NavBar ref="NavBar" />
        <StageRender ref="StageRender" />
      </div>

      <RightTab ref="RightTab" />
    </div>

    <!-- 悬浮提示 -->
    <div class="mouse-hover">
      <div class="info">
        <div></div>
      </div>
    </div>

    <!-- 选中高亮框 -->
    <div class="on-selected-border">
      <div class="info">
        <div></div>
        <ul>
          <li title="请点击 控件本体 不放进行移动">
            <Icon size="8" type="ios-move" style="cursor: move;" @click="$Message.info('请点击 控件本体 不放进行移动')" />
          </li>
          <li title="复制">
            <Icon size="8" type="ios-copy" @click="$refs.StageRender.copyWidget()" />
          </li>
          <li title="删除">
            <Icon size="8" type="ios-trash" @click="$refs.StageRender.delWidget()" />
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import TopBar from "./widget/topbar.vue";
import LeftTab from "./widget/tab-left.vue";
import RightTab from "./widget/tab-right.vue";
import NavBar from "./widget/navbar.vue";
import StageRender from "./core/stage-render.vue";

export default {
  components: { TopBar, LeftTab, RightTab, NavBar, StageRender },
  props: {
    initMetaData: { type: Array, required: true },
  },
  data() {
    return {
      STAGE_RENDER: null, // 舞台核心，这是一个 Vue 组件 shorthand for this.$refs.StageRender
      //   widgetCfg: {} as FormWidget,
      // form_databinding: {} as FormDataBinding,
    };
  },
  mounted() {
    this.STAGE_RENDER = this.$refs.StageRender;
    this.$el.querySelector(".stage > div").onscroll = () => {
      // 避免露馅 取消高亮
      if (this.$refs.StageRender.ActiveWdiget)
        this.$refs.StageRender.ActiveWdiget = null;
    };
  },
  // watch: {
  //   // initConfigData 父组件会延时生成 initConfigData，但此子组件已完成初始化了，所以 configData 不会改变。于是这里控制一下
  //   initConfigData(n) {
  //     this.configData = n;
  //   },
  // },
  // methods: {
  //   /**
  //    * 拖动完毕，添加控件
  //    *
  //    * @param e
  //    */
  //   onDrapAdd(e: any): void {
  //     let index: number = e.oldIndex;
  //     let target: string = e.from.className;

  //     let widgetDef: WidgetDefinition;
  //     let _widgetDef = this.widgetDef[target][index];

  //     if (_widgetDef) widgetDef = <WidgetDefinition>_widgetDef;
  //     else throw "找不到组件配置！";

  //     let widget: FormWidgetBase = {
  //       type: widgetDef.type,
  //       label: widgetDef.eng_name,
  //       key: `${widgetDef.type}_${new Date().getTime()}`,
  //     };

  //     this.$set(this.configData, e.newIndex, widget);
  //     this.onDdEnd({ newIndex: e.newIndex });
  //   },

  //   /**
  //    * 拖动完毕，添加控件
  //    *
  //    * @param e
  //    */
  //   onDrapNewAdd(e: any): void {
  //     // let index: number = e.oldIndex;
  //     if (!e.item.classList.contains("fromDefine"))
  //       // 根据定义新建的。如果不是，不执行下面处理
  //       return;

  //     let target: string = e.item.className.replace("fromDefine ", "");

  //     if (!target) {
  //       alert("代码未设置 class");
  //       return;
  //     }

  //     let widgetDef: WidgetDefinition;
  //     let _widgetDef = this.widgetDef.typeMap[target];

  //     if (_widgetDef) widgetDef = <WidgetDefinition>_widgetDef;
  //     else throw "找不到组件配置！";

  //     console.log(e);
  //   },
  // },
};
</script>

<style lang="less" scope src="./form-designer.less"></style>
