<template>
  <div>
    <Divider size="small">{{name}} <a style="color:green;" title="增加按钮" @click="add">✚</a></Divider>
    <ul :class="{custom:true}">
      <li>
        <div class="id"><b>按钮文本</b></div>
        <div class="name"><b>点击事件</b></div>
        <div class="isChecked"><b>删除</b></div>
      </li>
      <li v-for="(item, index) in cfg[type]" :key="index">
        <div class="text"> <input v-model="item.text" /></div>
        <div class="event"><a @click="editEvent(index)">编辑</a></div>
        <div class="action"><a title="删除按钮" @click="cfg[type].splice(index, 1)">⨯</a></div>
      </li>
    </ul>

    <Modal v-model="isShowEditEvent" title="编辑事件" @on-ok="saveEvent">
      <p>请直接输入 JavaScript 事件代码。事件函数说明：</p>
      <ul class="fn-help">
        <li>第一个参数为 <code>row: Entity</code>，是当前点击所在行的实体数据对象；</li>
        <li>第二个参数为 <code>index: number</code>，是当前点击所在行的索引；</li>
        <li>函数 <code>this</code> 参数为当前 table 实例</li>
      </ul>
      <code>function onClick(row: Entity, index: number): void {</code>
      <codemirror ref="eventCode" :options="cmOption" style="border:1px solid lightgray;margin:1% 0;margin-left:20px;width:93%" />
     <!--  <Input ref="eventCode" type="textarea" :rows="20" style="margin-left:20px;width:93%" /> -->
      <div style="clear: both"></div>
      <code>}</code>
    </Modal>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

import { codemirror } from 'vue-codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/theme/ambiance.css';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/addon/selection/active-line.js';
import 'codemirror/addon/edit/closebrackets.js';
import 'codemirror/theme/base16-light.css';
import 'codemirror/addon/display/autorefresh'

export default Vue.extend({
  components:{codemirror},
  props: {
    name: String,
    type: String,
    cfg: {
      type: Object,
      default() {
        return {};
      },
    },
  },
  data() {
    return {
      isShowEditEvent: false,
      editIndex: 0,
      cmOption: Object.freeze({
        autoRefresh: true, // 重点是这句，为true
        styleActiveLine: true, // 高亮选中行
        lineNumbers: true, // 显示行号
        line: true,
        lint: true,
        mode: "javascript",
        hintOptions: { completeSingle: false }, // 当匹配只有一项的时候是否自动补全
        theme: "ambiance", // 主题
      }),
    };
  },
  methods: {
    add() {
      this.cfg[this.type].push({ text: "请输入文本" });
    },
    editEvent(index: number): void {
      this.isShowEditEvent = true;
      this.editIndex = index;
      // this.$refs.eventCode.$el.querySelector("textarea").value = this.cfg[this.type][this.editIndex].event || "";
      this.$refs.eventCode.cminstance.setValue(this.cfg[this.type][this.editIndex].event || "");
    },
    saveEvent(): void {
      // let value: string = this.$refs.eventCode.$el.querySelector("textarea").value;
      let value: string = this.$refs.eventCode.cminstance.getValue();
      this.cfg[this.type][this.editIndex].event = value;
    },
  },
});
</script>

<style scoped>
code {
  background-color: rgb(250, 212, 212);
  color: rgb(252, 41, 41);
  padding: 2px 5px;
  margin: 3px 0;
}

.fn-help {
  margin: 2%;
  margin-left: 5%;
}

.fn-help li {
  list-style: disc;
  margin-top: 3px;
}
</style>