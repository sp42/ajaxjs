<template>
  <tr>
    <td class="first">
      <label><input type="checkbox" v-model="dml.enable" /> 修改记录
        <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="指定记录 id，修改记录字段（UPDATE）"></i>
      </label>
    </td>
    <td style="text-align: left">
      <fieldset :disabled="!dml.enable">
        <label><input type="checkbox" v-model="dml.isDynamicParse" :value="true" /> 动态解析字段
          <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="读取所有的请求参数，转化为 SQL 字段的 UPDATE 语句插入
这是一种简单粗暴的方式，慎用！"></i>
        </label>
        &nbsp;&nbsp;&nbsp;

        <a :class="{ disable: dml.isDynamicParse }" @click="showFields = true">选择字段</a>

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
        <label>
          <input type="checkbox" v-model="dml.isUpdateDate" :value="true" /> 更新修改日期
        </label>
      </fieldset>
    </td>
    <td>
      <sql-editor />
    </td>
    <td>
      <api-url v-if="dml.enable" method="put" />
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
      let pairs: string;

      if (this.dml.isDynamicParse) {
        this.dml.sql = `UPDATE ${tableCfg.tableName} SET 
<foreach collection="params" index="key" item="value" separator=",">
    <if test="key != 'id'">\${key} = #{value}</if>
</foreach> 
WHERE ${this.getIdField()} = #{params[id]};`;
      } else {
        let length = this.dml.fields.length, arr = new Array(length);
        for (let i = 0; i < length; i++)
          arr[i] = "`" + this.dml.fields[i] + "` = ?";

        pairs = arr.join(", ");

        this.dml.sql = `UPDATE ${tableCfg.tableName} SET ${pairs} WHERE ${this.getIdField()} = #{id}`;
      }
    },
  },
  watch: {
    "dml.isDynamicParse"(v): void {
      this.getSql();
    },
    "table.fieldsMapping.id"(v, old): void {
      v && old &&this.getSql();// old = undefined 时 表示初始化的，不修改 sql。当 old 有值时表示后面的操作
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