<template>
  <div>
    <!--服务配置-->
    <ul class="line-layout">
      <li>
        <i-input ref="inputName" placeholder="必填的" v-model="currentData.name">
          <span slot="prepend">命令名称</span>
        </i-input>
      </li>
      <li>
        <i-input ref="inputName" placeholder="实体的 URL 目录，必填的" v-model="currentData.urlDir">
          <span slot="prepend">URL 目录</span>
        </i-input>
      </li>
      <li style="padding-top: 5px;">数据源： {{currentData.datasourceName}}</li>
      <li style="padding-top: 5px;">数据库表名： {{currentData.tableName}}</li>
    </ul>

    <fieldset>
      <legend>
        更多设置
        <Icon type="ios-arrow-down" style="cursor: pointer;" @click="togglePanel" />
      </legend>
    </fieldset>

    <div class="config" style="height:0px">
      <Row type="flex" justify="center" align="middle">
        <i-Col span="10" style="padding-right:20px;">
          <i-input placeholder="不填则按照项目的设置。以 http://…… 开头" v-model="currentData.urlRoot">
            <span slot="prepend">API 地址前缀</span>
          </i-input>
          <br />
          <i-input placeholder="逗号隔开多个标签" v-model="currentData.tags">
            <span slot="prepend">标签</span>
          </i-input>
        </i-Col>
        <i-Col span="14">
          <i-input
            v-model="currentData.content"
            maxlength="200"
            show-word-limit
            type="textarea"
            :rows="4"
            placeholder="描述"
          />
        </i-Col>
      </Row>
      <br />
      <Divider style="color:gray" size="small">通用字段/参数映射 Mapping</Divider>
      <br />
      <Row type="flex" justify="center" align="middle" :gutter="16">
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 id" v-model="table.fieldsMapping.id">
            <span slot="prepend">唯一主键</span>
          </i-input>
        </i-Col>
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 createDate" v-model="table.fieldsMapping.createDate">
            <span slot="prepend">创建日期字段</span>
          </i-input>
        </i-Col>
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 updateDate" v-model="table.fieldsMapping.updateDate">
            <span slot="prepend">修改日期字段</span>
          </i-input>
        </i-Col>
      </Row>
      <br />
      <Row type="flex" justify="center" align="middle" :gutter="16">
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 id" v-model="table.fieldsMapping.id">
            <span slot="prepend">唯一主键</span>
          </i-input>
        </i-Col>
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 createDate" v-model="table.fieldsMapping.createDate">
            <span slot="prepend">创建日期字段</span>
          </i-input>
        </i-Col>
        <i-Col span="8">
          <a>复制项目设置</a>
        </i-Col>
      </Row>
      <!--       <FormItem label="主键生成策略：" prop="keyGen">
                                        <Select v-model="table.keyGen">
                                            <Option :value="3">UUID</Option>
                                            <Option :value="1">自增</Option>
                                            <Option :value="2">雪花</Option>
                                        </Select>
      </FormItem>-->
      <!--                   <FormItem label="唯一主键" prop="idField">
                                    <i-input v-model="currentData.fieldsMapping.id" type="text" placeholder="不填写则默认为 id"
                                        style="width:260px;" />
                                    <a @click="showFields=true; toField = 'id'" style="float:right">选择字段
                                        &nbsp;&nbsp;</a>
      </FormItem>-->
      <!--                        <FormItem label="创建日期的字段" prop="idField">
                                    <i-input v-model="table.fieldsMapping.createDate" type="text"
                                        placeholder="不填写则默认为 createDate" style="width:260px;"
                                        :disabled="!allDml.create.autoDate" />
                                    <a @click="showFields=true; toField = 'createDate'" style="float:right"
                                        :disabled="!allDml.create.autoDate">选择字段 &nbsp;&nbsp;</a>
      </FormItem>-->

      <!--                                 <FormItem label="修改日期的字段" prop="idField">
                                    <i-input v-model="table.fieldsMapping.updateDate" type="text"
                                        placeholder="不填写则默认为 updateDate" style="width:260px;"
                                        :disabled="!allDml.create.autoDate" />
                                    <a @click="showFields=true; toField = 'updateDate'" style="float:right"
                                        :disabled="!allDml.create.autoDate">选择字段 &nbsp;&nbsp;</a>
      </FormItem>-->
      <!--             <FormItem label="UUID 的字段" prop="idField">
                                        <i-input v-model="table.fieldsMapping.uuid" type="text"
                                            placeholder="不填写则默认为 uid" style="width:260px;"
                                            :disabled="!allDml.create.addUuid" />
                                        <a @click="showFields=true; toField = 'uuid'" :disabled="!allDml.create.addUuid"
                                            style="float:right">选择字段 &nbsp;&nbsp;</a>
      </FormItem>-->

      <!--     <FormItem label="生成 UUID 算法" prop="idField">
                                      <Select style="width:260px;" :disabled="!allDml.create.addUuid">
                                        <Option>GUID</Option>
                                        <Option>雪花算法</Option>
                                      </Select>
      </FormItem>-->

      <!-- 
                                <FormItem label="逻辑删除的状态" prop="idField">
                                    <i-input v-model="table.fieldsMapping.delStatus" type="text"
                                        placeholder="不填写则默认为 stat" style="width:260px;"
                                        :disabled="allDml.delete.isPhysicallyDelete" />
                                    <a @click="showFields=true; toField = 'delStatus'" style="float:right"
                                        :disabled="allDml.delete.isPhysicallyDelete">选择字段 &nbsp;&nbsp;</a>
      </FormItem>-->

      <!-- <FormItem label="分类标签" prop="tags">
                                      <TagsPicker />
      </FormItem>-->

      <Modal v-model="showFields" title="选择字段" cancel-text width="370">
        <ul class="showFieldsList">
          <li v-for="(v, k) in fields" :key="k">
            <label>
              <div class="fieldsComments">{{table.fieldsComments[k]}}</div>
              <a @click="table.fieldsMapping[toField] = k; showFields = false;">{{k}}</a>
            </label>
          </li>
        </ul>
      </Modal>

      <div style="margin-top: 15px;text-align: right;color:lightgray;">
        id#{{currentData.id}} 创建人： admin 创建日期：{{currentData.createDate}} 修改人： admin 修改日期
        {{currentData.updateDate}}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showFields: false,
      fields: [],
    };
  },
  props: {
    currentData: { type: Object, required: true },
    table: {
      type: Object,
      required: false,
      default() {
        return {
          fieldsMapping: {},
        };
      },
    },
  },
  methods: {
    togglePanel() {
      let config = this.$el.querySelector(".config");

      if (config.style.height == "300px") {
        config.style.height = "0";
      } else config.style.height = "300px";
    },
  },
};
</script>

<style lang="less" scoped>
fieldset {
  margin-top: 20px;
  margin-bottom: 0px;
  border: 0;
  border-top: 1px solid lightgray;
  padding: 10px 20px 0 20px;

  legend {
    margin-left: 8px;
    font-weight: bold;
    letter-spacing: 5px;
  }
}

ul.line-layout {
  li {
    display: inline-block;
    min-width: 100px;
    vertical-align: top;
    margin-right: 20px;
  }
}

.config {
    transition: height 200ms ease;
    overflow: hidden;
}
</style>