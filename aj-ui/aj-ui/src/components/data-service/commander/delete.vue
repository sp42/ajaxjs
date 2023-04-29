<template>
  <tr>
    <td class="first">
      <label><input type="checkbox" v-model="dml.enable" /> 删除实体
        <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="删除单笔记录，若成功返回 true"></i>
      </label>
    </td>
    <td style="text-align: left">
      <fieldset :disabled="!dml.enable">
        <label><input type="radio" v-model="dml.isPhysicallyDelete" :value="true" @change="onDeleteTypeChange" />
          物理删除 </label>
        <label> <input type="radio" v-model="dml.isPhysicallyDelete" :value="false" @change="onDeleteTypeChange" /> 逻辑删除
          <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="在逻辑上数据是被删除的，但数据本身依然存在库中（仅仅是更新状态字段为已删除）"></i>
        </label>
      </fieldset>
    </td>
    <td>
      <sql-editor />
    </td>
    <td>
      <api-url v-if="dml.enable" method="delete" />
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
    onDeleteTypeChange() {
      let tableCfg = this.table as DataService_TableConfig;

      if (this.dml.isPhysicallyDelete) {
        this.dml.sql = `DELETE FROM ${tableCfg.tableName} WHERE ${this.getIdField()} = #{id}`;
      } else {
        let DELETED_CONST: number = 2; // 标记删除的常量，暂时写死
        this.dml.sql = `UPDATE ${tableCfg.tableName} SET \`${tableCfg.fieldsMapping.delStatus}\` = ${DELETED_CONST} WHERE ${this.getIdField()} IN (\${id})`;
      }
    },
  },
  watch: {
    "table.fieldsMapping.id"(v, old) {
      v && old && this.onDeleteTypeChange();
    },
    "table.fieldsMapping.delStatus"(v, old) {
      v && old && this.onDeleteTypeChange(); // old = undefined 时 表示初始化的，不修改 sql。当 old 有值时表示后面的操作
    },
  },
};
</script>