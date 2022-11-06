<template>
  <div class="config-table">
    <div :class="{main: true, small: isShowLivePerview}">

      <span style="float:right">
        <!--  <Button :disabled="isDisabled" class="rightMargin" icon="ios-exit-outline" @click="$parent.exportCmp">静态化组件</Button> -->
        <span v-if="isEnableLivePerview">实时预览 </span>

        <i-Switch v-if="isEnableLivePerview" v-model="isShowLivePerview" class="rightMargin" />
        <Button :disabled="isDisabled" class="rightMargin" icon="ios-eye" @click="$parent.doRenderer">预览</Button>
        <Button :disabled="isDisabled" icon="ios-create-outline" type="primary" @click="$parent.save">保存</Button>
      </span>

      <Button class="rightMargin" icon="md-list" @click="isShowFieldsSelect = true" title="根据数据库各个字段导入列表的元数据">选择数据库</Button>
      <!--    <span style="color:gray;font-size:8pt;">或</span>&nbsp;
      <Button class="rightMargin" icon="md-git-network" @click="$parent.isShowApiSelect = true" title="根据 API 接口返回的 JSON 导入列表的元数据并数据绑定">选择接口</Button> -->

      <Button class="rightMargin" :disabled="$parent.editIndex != -1" icon="md-add" @click="$parent.addRow">新增字段</Button>
      <Button class="rightMargin" @click="isShowMoreConfig = true">选项配置</Button>
      说明：<Input type="text" placeholder="请输入说明，必填" style="width:260px" class="rightMargin" v-model="$parent.name" />
      <slot name="topbar" />
      <a @click="$router.back();">返回</a>

      <ul class="table">
        <header>
          <li>
            <div class="action" style="float:righty">操作</div>
            <div class="isShow checkbox-width">显示</div>
            <div class="input-width">字段名</div>
            <div class="input-width">名称</div>
            <slot name="table-header" />
          </li>
        </header>

        <draggable tag="section" handle=".handleSort" v-model="fields" chosenClass="chosen" forceFallback="true" group="people" animation="1000">
          <li v-for="(item, index) in fields" :key="index">
            <div class="action">
              <div v-if="$parent.editIndex === index">
                <a @click="$parent.saveAddRow">添加</a>
                <Divider type="vertical" />
                <a @click="cancelAddRow" style="color:green;">取消</a>
              </div>
              <div v-else>
                <!--  <a @click="$parent.moreAttrib(item, index)" title="进入详细的属性（attribute）配置">更多</a> -->
                <a @click="isShowMoreAttrib = true;currentRow = item;" title="进入详细的属性（attribute）配置">更多</a>
                <Divider type="vertical" />
                <a @click="moreAttrib(row, index)" class="handleSort" style="cursor: move;color:green" title="点击鼠标不放拖动以排序">排序</a>
                <Divider type="vertical" />
                <Poptip confirm transfer title="是否要删除此行？" @on-ok="fields.splice(index, 1);">
                  <a style="color:red;">删除</a>
                </Poptip>
              </div>
            </div>
            <div class="isShow checkbox-width">
              <Checkbox size="small" v-model="item.isShow" />
            </div>
            <slot name="table-fields" :item="item" />
          </li>
        </draggable>
      </ul>
    </div>

    <div :class="{'live-perview': true, hide: !isShowLivePerview}">
      <slot name="live-perview" />
    </div>

    <slot />

    <Modal v-model="isShowFieldsSelect" title="选择数据库字段" width="1000" @on-ok="$parent.fieldsToCfg($refs.DataModelSelector.getSelected())">
      <DataModelSelector ref="DataModelSelector" :api-root="$parent.apiRoot" />
    </Modal>

    <Modal v-model="isShowApiSelect" title="选择 API 接口" width="500">
      <ApiSelector ref="ApiSelector" :api-root="$parent.apiRoot" />
    </Modal>

    <Modal v-model="isShowMoreConfig" title="选项配置" cancel-text="" width="600">
      <slot name="config-panel" />
    </Modal>

    <Modal v-model="isShowMoreAttrib" title="更多属性" cancel-text="" width="550">
      <slot name="more-attrib" :row="currentRow" />
    </Modal>
  </div>
</template>

<script>
import draggable from "vuedraggable";
import DataModelSelector from '../data-model-selector/index.vue';
import ApiSelector from '../api-selector/index.vue';

export default {
  components: { draggable, DataModelSelector, ApiSelector },
  props: {
    columns: Array, fields: Array, isEnableLivePerview: Boolean
  },
  data() {
    return {
      isShowLivePerview: false,
      isShowMoreConfig: false,
      isShowMoreAttrib: false,// 是否显示更多的配置（某个属性的）
      isShowFieldsSelect: false,
      isShowApiSelect: false,
      currentRow: {}
    };
  },
  computed: {
    isDisabled() {
      return !this.$parent.cfg.fields.length;
    }
  },
  methods: {
    /**
     * 取消新增
     */
    cancelAddRow() {
      this.$parent.editIndex = -1;
      this.$parent.cfg.fields.pop();
    },
  },
  watch: {
    fields(n) {
      this.$parent.cfg.fields = n;
    },
    // isShowLivePerview(n) {
    //   if(n) {
    //     this.$el.query
    //   }
    // }
  }
}
</script>

<style lang="less" src="./config-table.less"></style>