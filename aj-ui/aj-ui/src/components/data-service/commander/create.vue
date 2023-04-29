<template>
  <tr>
    <td class="first">
      <label>
        <input type="checkbox" v-model="dml.enable" /> 创建记录
        <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="执行 SQL INSERT 操作新增一笔记录（CREATE）。"></i>
      </label>
    </td>
    <td style="text-align: left">
      <fieldset :disabled="!dml.enable">
        <label><input type="checkbox" v-model="dml.isDynamicParse" :value="true" /> 动态解析字段
          <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="读取所有的请求参数，转化为 SQL 字段和 INSERT 语句插入
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
          <input type="checkbox" v-model="dml.addUuid" :value="true" /> 生成唯一 uid
          <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="唯一 id，通过 uuid 生成不重复 id"></i>
        </label>
        &nbsp;&nbsp;&nbsp;
        <label>
          <input type="checkbox" v-model="dml.autoDate" :value="true" /> 插入创建/修改日期
        </label>
        <br />
        <label>
          <input type="checkbox" v-model="dml.createOrSave" :value="true" /> 当有 id 参数时采用“修改”的规则
          <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="CreateOrSave，同一个接口处理"></i>
        </label>
      </fieldset>
    </td>
    <td>
      <sql-editor />
    </td>
    <td>
      <api-url v-if="dml.enable" method="post" />
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
      let fieldsList: string, valuesList: string;

      if (this.dml.isDynamicParse) {
        this.dml.sql = `INSERT INTO ${tableCfg.tableName} 
<foreach collection="params" index="key" open="(" close=")" separator=",">\${key}</foreach> 
VALUES <foreach collection="params" index="key" open="(" close=")" separator=",">#{params[\${key}]}</foreach>`;
      } else {
        let length = this.dml.fields.length,
          arr = new Array(length);
        for (let i = 0; i < length; i++)
          arr[i] = "`" + this.dml.fields[i] + "`";

        fieldsList = arr.join(", ");
        valuesList = new Array(this.dml.fields.length + 1).join("?,  ");
        valuesList = valuesList.trim();
        valuesList = valuesList.substr(0, valuesList.length - 1);

        this.dml.sql = `INSERT INTO ${tableCfg.tableName} (${fieldsList}) VALUES (${valuesList})`;
      }
    },
  },
  watch: {
    "dml.isDynamicParse"(v): void {
      this.getSql();
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