<template>
  <ListRenderer ref="renderer" :api-root="apiRoot" :col="rendererColDef" :cfg="cfg" :initTableData="initTableData" :search-fields="searchFields" />
</template>

<script lang="ts">
import ListRenderer from "./list-factory-renderer.vue";
import ListFactoryCode from "./list-factory";

/**
 * 列表加载器
 */
export default {
  components: { ListRenderer },
  props: {
    id: { type: Number, required: false },
  },
  data() {
    return {
      initTableData: [], // 预览用的表格数据
      rendererColDef: [] as iViewTableColumn[], // 渲染器的列定义
      searchFields: [],
      selectedTable: {} as SelectedTable,
      cfg: {},
      fields: [],
      apiRoot: window["config"].dsApiRoot,
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    load(): void {
      let dummyProps = { apiRoot: window["config"].dsApiRoot };
      let dummyData = ListFactoryCode.data.call(dummyProps);
      dummyData.id = this.id || Number(this.$route.query.id);
      Object.assign(this, dummyData);
      ListFactoryCode.methods.getData.call(this, () =>
        ListFactoryCode.methods.doRenderer.call(this)
      ); // 加载元数据之后转换为 iview 配置
    },
  },
  watch: {
    $route(): void {
      this.load();
    },
    id(id: number): void {
      this.load();
    },
  },
};
</script>

