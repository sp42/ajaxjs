<template>
  <Form ref="create" :model="table" :rules="createRules" :label-width="120">
    <FormItem label="URL 前缀" prop="urlRoot">
      <Input v-model="table.urlRoot" placeholder="以 http://…… 开头，如不指定为 localhost" />
    </FormItem>

    <FormItem label="描述：" prop="name">
      <Input v-model="table.name" maxlength="100" show-word-limit type="textarea" :rows="2" placeholder="描述" />
    </FormItem>
    <FormItem label="主键生成策略：" prop="keyGen">
      <Select v-model="table.keyGen">
        <Option :value="3">UUID</Option>
        <Option :value="1">自增</Option>
        <Option :value="2">雪花</Option>
      </Select>
    </FormItem>
    <Divider style="color:gray" size="small">字段/参数映射 Mapping</Divider>
    <FormItem label="唯一主键" prop="idField">
      <Input v-model="table.fieldsMapping.id" type="text" placeholder="不填写则默认为 id" style="width:260px;" /> 
      <a @click="showFields=true; toField = 'id'" style="float:right">选择字段 &nbsp;&nbsp;</a>
    </FormItem>
    <FormItem label="创建日期的字段" prop="idField">
      <Input v-model="table.fieldsMapping.createDate" type="text" placeholder="不填写则默认为 createDate" style="width:260px;" :disabled="!allDml.create.autoDate" />
      <a @click="showFields=true; toField = 'createDate'" style="float:right" :disabled="!allDml.create.autoDate">选择字段 &nbsp;&nbsp;</a>
    </FormItem>

    <FormItem label="修改日期的字段" prop="idField">
      <Input v-model="table.fieldsMapping.updateDate" type="text" placeholder="不填写则默认为 updateDate" style="width:260px;" :disabled="!allDml.create.autoDate" />
      <a @click="showFields=true; toField = 'updateDate'" style="float:right" :disabled="!allDml.create.autoDate">选择字段 &nbsp;&nbsp;</a>
    </FormItem>
    <FormItem label="UUID 的字段" prop="idField">
      <Input v-model="table.fieldsMapping.uuid" type="text" placeholder="不填写则默认为 uid" style="width:260px;" :disabled="!allDml.create.addUuid" />
      <a @click="showFields=true; toField = 'uuid'" :disabled="!allDml.create.addUuid" style="float:right">选择字段 &nbsp;&nbsp;</a>
    </FormItem>

    <!--     <FormItem label="生成 UUID 算法" prop="idField">
      <Select style="width:260px;" :disabled="!allDml.create.addUuid">
        <Option>GUID</Option>
        <Option>雪花算法</Option>
      </Select>
    </FormItem> -->

    <FormItem label="逻辑删除的状态" prop="idField">
      <Input v-model="table.fieldsMapping.delStatus" type="text" placeholder="不填写则默认为 stat" style="width:260px;" :disabled="allDml.delete.isPhysicallyDelete" />
      <a @click="showFields=true; toField = 'delStatus'" style="float:right" :disabled="allDml.delete.isPhysicallyDelete">选择字段 &nbsp;&nbsp;</a>
    </FormItem>

    <!-- <FormItem label="分类标签" prop="tags">
      <TagsPicker />
    </FormItem> -->

    <Modal v-model="showFields" title="选择字段" cancel-text="" width="370">
      <ul class="showFieldsList">
        <li v-for="(v, k) in fields" :key="k">
          <label>
            <div class="fieldsComments">{{table.fieldsComments[k]}}</div> <a @click="table.fieldsMapping[toField] = k; showFields = false;">{{k}}</a>
          </label>
        </li>
      </ul>
    </Modal>
  </Form>
</template>

<script>
// import TagsPicker from '../../widget/tag-list-picker.vue';

/**
 * 其他配置的对话框
 */
export default {
  // components: { TagsPicker },
  props: {
    fields: { type: Object, required: true },
    table: { type: Object, required: true },
    allDml: Object
  },

  data() {
    return {
      showFields: false,
      // fields: JSON.parse(this.table.fields),
      toField: '', // 要保存的字段
      createRules: {
        urlRoot: [{ required: false, message: 'API 接口的前缀', trigger: 'blur' }],
        content: [{ required: false, message: '该组接口之描述', trigger: 'blur' }]
      }
    };
  },
  
  computed: {
    isUpdateDate() {
      return !this.allDml.create.autoDate && !this.allDml.update.isUpdateDate;
    }
  }
};
</script>