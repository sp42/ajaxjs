<template>
  <div>
    <span style="float:right">
      <a :disabled="!$parent.dml.enable" @click="showEdit = true">编辑 SQL</a> &nbsp;
      <a :disabled="!$parent.dml.enable" @click="writeCopy">复制</a>
    </span>

    <textarea :value="$parent.dml.sql" :disabled="!$parent.dml.enablee" cols="60" rows="5"></textarea>

    <pre class="prettyprint lang-sql" :class="{disabled: !$parent.dml.enable}" v-html="getSql()"></pre>

    <Modal v-model="showEdit" title="编辑 SQL" :transfer="false" width="1300">
      <codemirror ref="myCm" v-model="$parent.dml.sql" :options="cmOption" />
    </Modal>
  </div>
</template>

<script>
import './code-prettify';
import { codemirror } from 'vue-codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/mode/sql/sql.js'
import 'codemirror/addon/selection/active-line.js'
import 'codemirror/addon/edit/closebrackets.js'
import 'codemirror/theme/monokai.css'// 编辑的主题文件
import 'codemirror/theme/base16-light.css'

function to(s) {
  if (s) {
    s = s.replace(/&/g, '&amp;');
    s = s.replace(/</g, '&lt;');
    s = s.replace(/>/g, '&gt;');
    s = s.replace(/ /g, '&nbsp;');
  }

  return s;
}

export default {
  components: { codemirror },
  data() {
    return {
      showEdit: false,
      code: 'SELECT * FROM foo',
      cmOption: {
        styleActiveLine: true, // 高亮选中行
        lineNumbers: true, // 显示行号
        line: true,
        mode: 'text/x-mysql',
        hintOptions: { completeSingle: false },// 当匹配只有一项的时候是否自动补全
        theme: 'monokai', // 主题 
      }
    };
  },
  methods: {
    writeCopy(ev) {
      let textarea = ev.target.parentNode.parentNode.querySelector('textarea');
      let text = textarea.value;
      text && this.$Copy({ text: text });
    },
    getSql() {
      // return PR.prettyPrintOne(to(this.parentValue), 'sql');
      return PR.prettyPrintOne(to(this.$parent.dml.sql), 'sql');
    }
  },
  watch: {
    showEdit(v) {// vue codemirror 初始隐藏,后续点击才显示；需要点击一下才会显示代码
      if (v)
        setTimeout(() => this.$refs.myCm.codemirror.refresh(), 1);
    }
  }
};
</script>

<style lang="less" scoped>
textarea {
  display: none;
  padding: 4px 7px;
  font-size: 14px;
  border: 1px solid #dcdee2;
  border-radius: 4px;
  color: #515a6e;
  background-color: #fff;
  background-image: none;

  &:focus {
    outline: 0;
    border-color: #57a3f3;
    box-shadow: 0 0 0 2px rgba(45, 140, 240, 0.2);
  }
}
</style>
<style lang="less">
.CodeMirror {
  height: 600px;
}
pre.prettyprint {
  // padding: 4px 9px;
  margin: 5px 0;
  min-height: 80px;
  max-height: 100px;
  overflow-y: auto;
  font-family: "Courier New", Courier, monospace;
  border: 1px solid #dcdee2 !important;
  border-radius: 4px;
  background-color: #fff;
  width: 400px;
  white-space: pre-wrap;
  word-wrap: break-word;
  // &:hover {
  //     outline: 0;
  //     border-color: #57a3f3;
  //     // box-shadow: 0 0 0 1px rgba(45, 140, 240, 0.2);
  // }

  &.disabled,
  &.disabled * {
    color: lightgray !important;
  }

  .kwd {
    color: #d63948;
  }
}
</style>

