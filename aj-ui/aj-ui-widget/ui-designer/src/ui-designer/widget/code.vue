<template>
  <Drawer title="代码编辑器" width="1344" v-model="isShowCodeEdit">
    <codemirror ref="myCm" v-model="code" :options="cmOption" style="height: 100%;" />
  </Drawer>
</template>

<script>
import { codemirror } from 'vue-codemirror';
import 'codemirror/addon/selection/active-line.js'
import 'codemirror/addon/edit/closebrackets.js'
import 'codemirror/mode/javascript/javascript';
import 'codemirror/addon/lint/json-lint';
import 'codemirror/lib/codemirror.css';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/theme/base16-light.css'
// import 'codemirror/theme/monokai.css'// 编辑的主题文件

// let jsonData = '[{"items":[{"market_type":"forexdata","symbol":"XAUUSD"},{"market_type":"forexdata","symbol":"UKOIL"},{"market_type":"forexdata","symbol":"CORN"}],"name":""},{"items":[{"market_type":"forexdata","symbol":"XAUUSD"},{"market_type":"forexdata","symbol":"XAGUSD"},{"market_type":"forexdata","symbol":"AUTD"},{"market_type":"forexdata","symbol":"AGTD"}],"name":"贵金属"},{"items":[{"market_type":"forexdata","symbol":"CORN"},{"market_type":"forexdata","symbol":"WHEAT"},{"market_type":"forexdata","symbol":"SOYBEAN"},{"market_type":"forexdata","symbol":"SUGAR"}],"name":"农产品"},{"items":[{"market_type":"forexdata","symbol":"UKOIL"},{"market_type":"forexdata","symbol":"USOIL"},{"market_type":"forexdata","symbol":"NGAS"}],"name":"能源化工"}]'
// jsonData = JSON.stringify(JSON.parse(jsonData), null, 2);

export default {
  components: { codemirror },
  props: {
    metaData: { type: Array, required: true }
  },
  data() {
    return {
      isShowCodeEdit: false,
      code: JSON.stringify(this.metaData, null, 2),
      cmOption: {
        styleActiveLine: true, // 高亮选中行
        lineNumbers: true, // 显示行号
        line: true,
        mode: 'application/json',
        // hint.js options
        hintOptions: { completeSingle: false }, // 当匹配只有一项的时候是否自动补全
      }
    };
  },
  methods: {
    refresh() {
      this.code = JSON.stringify(this.metaData, null, 2);
      setTimeout(() => this.$refs.myCm.codemirror.refresh(), 1);
    },
  },
  watch: {
    metaData: {
      deep: true,
      handler(n) {
        this.code = JSON.stringify(n, null, 2);
      }
    },
    isShowCodeEdit(v) {
      v && this.refresh();
    }
  }
}
</script>

<style lang="less">
.CodeMirror {
  height: 100% !important;
  //   overflow-y: scroll !important;
}
</style>