<template>
  <span>
    <FormItem label="HTTP 方法">
      <Select v-model="cfg.httpMethod" size="small" style="width:150px;margin-right:20px;">
        <Option value="GET">GET</Option>
        <Option value="POST">POST</Option>
        <Option value="PUT">PUT</Option>
        <Option value="DELETE">DELETE</Option>
        <Option value="PATCH">PATCH</Option>
        <Option value="OPTION">OPTION</Option>
      </Select>
      <Button size="small" @click="api">选择 API</Button>
    </FormItem>

    <FormItem label="API 地址">
      <Input size="small" v-model="cfg.url" placeholder="http(s)://……" />
    </FormItem>

    <FormItem label="固定参数">
      <Input size="small" v-model="cfg.baseParams" placeholder="JSON 格式" />
      <br />
      <Button size="small" @click="isShowDymaicArgs=true">动态获取参数</Button>
    </FormItem>

    <Modal v-model="isShowDymaicArgs" title="动态获取参数" width="800">
      <codemirror v-model="cfg.beforeRequest" :options="cmOption" style="border:1px solid lightgray;" />
    </Modal>
  </span>
</template>

<script>
import ApiSelector from '../api-selector/index.vue';
import { codemirror } from 'vue-codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/addon/selection/active-line.js';
import 'codemirror/addon/edit/closebrackets.js';
import 'codemirror/theme/base16-light.css';
import 'codemirror/addon/display/autorefresh'


export default {
  components: { ApiSelector, codemirror },
  props: {
    cfg: { type: Object, required: true, default() { return {}; } }
  },
  data() {
    return {
      method: 'GET',
      isShowDymaicArgs: false,
      code: '{}',
      cmOption: {
        autoRefresh: true, // 重点是这句，为true
        styleActiveLine: true, // 高亮选中行
        lineNumbers: true, // 显示行号
        line: true,
        lint: true,
        mode: 'application/json',
        hintOptions: { completeSingle: false },// 当匹配只有一项的时候是否自动补全
        //   theme: 'monokai', // 主题 
      }
    };
  },
  methods: {
    api() {
      let p = this.$parent;

      while (p) {
        p = p.$parent;
        if (p.$refs.ApiSelector) {
          p.isShowApiSelect = true;
          break;
        }
      }
    }
  }
}

export function test() {
  alert(99)
}
</script>