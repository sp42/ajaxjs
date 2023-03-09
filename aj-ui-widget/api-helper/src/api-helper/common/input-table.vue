<template>
  <table class="input-table">
    <thead>
      <th width="50"></th>
      <th>Key</th>
      <th>Value</th>
      <th>说明</th>
      <th>操作</th>
    </thead>
    <tr v-for="(item, index) in tableData" :key="index" :class="{disable: !item.enable}">
      <td align="center">
        <input type="checkbox" v-model="item.enable" />
      </td>
      <td>
        <input @focus="onInputFocus" @blur="onInoutBlur" @input="onInput(index)" v-model="item.key" />
      </td>
      <td>
        <input @focus="onInputFocus" @blur="onInoutBlur" v-model="item.value" />
      </td>
      <td>
        <input @focus="onInputFocus" @blur="onInoutBlur" v-model="item.desc" />
      </td>
      <td align="center">
        <Icon type="md-trash" class="delBtn" title="删除" @click="delRow(index)" />
      </td>
    </tr>
  </table>
</template>

<script>
export default {
  data() {
    return {
      tableData: this.data,
    };
  },
  props: {
    data: { type: Array, required: true },
  },
  methods: {
    onInputFocus(e) {
      let input = e.target;
      if (
        input.parentNode &&
        input.parentNode.parentNode &&
        input.parentNode.parentNode.tagName == "TR"
      ) {
        let tr = input.parentNode.parentNode;
        tr.classList.add("highlight");
      }
    },
    onInoutBlur(e) {
      let input = e.target;
      if (
        input.parentNode &&
        input.parentNode.parentNode &&
        input.parentNode.parentNode.tagName == "TR"
      ) {
        let tr = input.parentNode.parentNode;
        tr.classList.remove("highlight");
      }
    },
    onInput(index) {
      if (index + 1 == this.tableData.length) {
        // 最后一行
        this.tableData.push({
          enable: true,
          key: "",
          value: "",
          desc: "",
        });
      }
    },
    delRow(index) {
      if (this.tableData.length == 1) {
      } else {
        this.$delete(this.tableData, index);
      }
    },
  },
};
</script>

<style lang="less" scoped>
.input-table {
  width: 100%;
  border-collapse: collapse;

  input {
    border: 1px solid transparent;
    outline: none;
    padding: 0px 3px;
    width: 100%;
  }

  input:focus {
    border: 1px solid lightgray !important;
    background-color: white !important;
  }

  tr {
    &.highlight {
      background-color: #f9f9f9;

      input {
        border-color: #f9f9f9;
        background-color: #f9f9f9;
      }
    }

    &.disable {
      input {
        color: lightgray;
      }
    }
  }

  td,
  th {
    padding: 5px 5px;
    border: 1px solid lightgray;
  }
}

.delBtn {
  cursor: pointer;
}
</style>