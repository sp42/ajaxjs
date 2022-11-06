<template>
  <!-- 自定义 DML -->
  <tr>
    <td class="first" style="padding-top: 30px; padding-bottom: 30px">
      <input type="checkbox" v-model="dml.enable" />

      <select style="margin: 10px 0 0 15px; width: 120px" v-model="dml.type">
        <option value="getOne">查询单行操作</option>
        <option value="getRows">查询多行操作</option>
        <option value="getRowsPage">查询多行操作（分页）</option>
        <option value="insert">新增操作</option>
        <option value="update">修改操作</option>
        <option value="delete">删除操作</option>
        <option value="batch-update">批量更新（TODO）</option>
        <option value="batch-delete">批量删除（TODO）</option>
        <option value="stored-procedure">存储过程（TODO）</option>
      </select>

      <div style="text-align:center;margin-top:5px;"><a @click="del">删除</a></div>

    </td>
    <td>
      <Input type="text" placeholder="操作名称（必填）也是接口 url 的一部分" v-model="dml.dir" style="width:270px" />

    </td>
    <td>
      <SqlEditor />
    </td>
    <td>
      <ApiUrl v-if="dml.enable" :method="method" :can-set-sub-dir="false" />
    </td>
  </tr>
</template>

<script>
import Common from './common';
import ApiUrl from '../info/api-url.vue';
import SqlEditor from '../info/sql-editor';

export default {
  mixins: [Common],
  components: { ApiUrl, SqlEditor },
  props: {
    index: Number
  },
  data() {
    let method;

    switch (this.dml.type) {
      case 'info':
      case 'list':
        method = 'get';
        break;
      case 'insert':
        method = 'post';
        break;
      case 'update':
        method = 'put';
        break;
      case 'delete':
        method = 'delete';
        break;
    }
    return {
      name: '',
      method: method
    };
  },
  methods: {
    del() {
      this.$delete(this.$parent.allDml.others, this.index);
    }
  }
};
</script>

<style lang="less" scoped>
textarea {
  padding: 4px 7px;
  font-size: 14px;
  border: 1px solid #dcdee2;
  border-radius: 4px;
  color: #515a6e;
  background-color: #fff;
  background-image: none;
}
select {
  //line-height: 100%;
  padding: 0.4em 2.3em;
  margin: 10px auto;
  color: #333;
  letter-spacing: 0.3em;
  border-radius: 4px;
  border: 1px solid #ccc;
  border-bottom-color: #b4b4b4;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);
  text-shadow: 0 1px 0 rgba(255, 255, 255, 0.8);
  background-color: lightgray;
  background-repeat: repeat-x;
  transition: background-image 0.5s linear 1s;
  cursor: pointer;
  outline: 0;
  padding: 1px 4px;
  -moz-appearance: none;
  -webkit-appearance: none;
  border-color: #abadb3;
  background: white
    url("data:image/gif;base64,R0lGODlhCwAGAIABAGBgYP///yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0MzQyLCAyMDEwLzAxLzEwLTE4OjA2OjQzICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjc1MEE0M0UzRTU5RDExRTVBNjNDRDVBNzRBRkUxMjYxIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjc1MEE0M0U0RTU5RDExRTVBNjNDRDVBNzRBRkUxMjYxIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6NzUwQTQzRTFFNTlEMTFFNUE2M0NENUE3NEFGRTEyNjEiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6NzUwQTQzRTJFNTlEMTFFNUE2M0NENUE3NEFGRTEyNjEiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAABACwAAAAACwAGAAACD0R+hqDB6B5radL1ILynAAA7")
    no-repeat right;
  min-height: 30px;
  font-size: 0.8rem;

  // .gradient (top, rgba(255, 255, 255, 1) 0%, rgba(239, 239, 239, 1) 60%, rgba(225, 223, 226, 1) 100%);

  // img {
  //     vertical-align: middle;
  // }

  // &:hover {
  //     color       : black;
  //     border-color: lighten(black, 65%);
  // }

  // &:active {
  //     box-shadow  : inset 0 3px 5px rgba(0, 0, 0, .2);
  //     border-color: lighten(black, 55%);
  // }

  // &:focus {
  //     border-color: lighten(black, 60%);
  // }
}
</style>