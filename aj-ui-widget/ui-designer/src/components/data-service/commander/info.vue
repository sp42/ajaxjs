<template>
  <tr>
    <td class="first">
      <label>
        <input type="checkbox" v-model="dml.enable" /> 单个记录查询
        <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="查询单笔记录，通常返回一个记录的详细信息（info）。"></i>
      </label>

      <Modal v-model="showFields" title="选择字段" @on-ok="selectField" :transfer="false" width="370">
        <ul class="showFieldsList">
          <li v-for="(v, k) in fields" :key="k">
            <label>
              <input type="checkbox" v-model="fields[k]" :checked="v" />
              <div class="fieldsComments">{{table.fieldsComments[k]}}</div> {{k}}
            </label>
          </li>
        </ul>
      </Modal>
    </td>
    <td>
      <fieldset :disabled="!dml.enable">
        <label>
          <input type="checkbox" v-model="dml.isReadAllField" :value="true" /> 返回表所有字段
        </label>
        &nbsp;&nbsp;&nbsp;

        <a :class="{ disable: dml.isReadAllField }" @click="showFields = true">选择字段</a>
        <br />
        <label>
          <input type="checkbox" v-model="dml.isQuerySearch" :value="true" /> 允许条件搜索
        </label>
        <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="在 QueryString 上添加 where 参数传入自定义的搜索条件，
条件为 SQL 标准查询语句，例如：
?where=(id = 1001 OR name = 'Tom') AND stat = 1
对于 SQL 本身已有的 WHERE 语句，采用 AND 并的关系来进行拼接"></i>
      </fieldset>
    </td>
    <td>
      <sql-editor />
    </td>
    <td>
      <api-url v-if="dml.enable" method="get" />
    </td>
  </tr>
</template>

<script lang="ts">
import Common from "./common";
import ApiUrl from "../info/api-url.vue";
import SqlEditor from "../info/sql-editor.vue";

export default {
  mixins: [Common],
  components: { ApiUrl, SqlEditor },

  methods: {
    getSql(): void {
      let tableCfg = this.table as DataService_TableConfig;

      if (this.dml.isReadAllField)
        this.dml.sql = `SELECT * FROM ${tableCfg.tableName} WHERE ${this.getIdField()} = #{id}`;
      else {
        let arr: string[] = this.dml.fields.map(
          (item: string) => "`" + item + "`"
        );
        this.dml.sql = `SELECT ${arr.join(", ")} FROM ${tableCfg.tableName} WHERE ${this.getIdField()} = #{id}`;
      }
    },
  },
  watch: {
    "dml.isReadAllField"(v): void {
      this.getSql();
    },
    "table.fieldsMapping.id"(v, old): void {
      v && old && this.getSql(); // old = undefined 时 表示初始化的，不修改 sql。当 old 有值时表示后面的操作
    },
    table: {
      // 异步的数据，且 fields 是数组（不能直接 watch），于是通过 watch 同步
      deep: true,
      handler(v) {
        if (v.fields) {
          this.fields = JSON.parse(v.fields);

          if (this.dml.fields && this.dml.fields.length)
            this.dml.fields.forEach(
              (item: string) => (this.fields[item] = true)
            );
        }
      },
    },
  },
};
</script>