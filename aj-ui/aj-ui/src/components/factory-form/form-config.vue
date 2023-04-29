<template>
  <!-- 详细配置 -->
  <Tabs style="min-height:600px" :animated="false">
    <TabPane label="表单配置">
      <Form :label-width="120" label-colon>
        <FormItem label="记录 id">{{$parent.$parent.$parent.id}}</FormItem>
        <Row>
          <Col span="12">
          <FormItem label="数据源">
            {{cfg.dataBinding.datasourceName}}#{{cfg.dataBinding.datasourceId}}
          </FormItem>
          </Col>
          <Col span="12">
          <FormItem label="绑定表名">
            {{cfg.dataBinding.tableName}}
          </FormItem>
          </Col>
          <FormItem label="Label 宽度"><Input type="number" v-model="cfg.labelWidth" :number="true" /></FormItem>
        </Row>
      </Form>
    </TabPane>

    <TabPane label="数据绑定">
      <Form :label-width="120" label-colon>
        <Divider size="small">获取记录接口</Divider>

        <ApiBinding :cfg="cfg.dataBinding" />
        <FormItem label="RESTful 创建/更新接口">
          <i-Switch v-model="cfg.isRESTful_writeApi" />
        </FormItem>

        <Divider size="small">更新记录接口</Divider>
        <ApiBinding :cfg="cfg.updateApi" />

        <Divider size="small" v-if="cfg.isRESTful_writeApi">创建记录接口</Divider>
        <ApiBinding v-if="!cfg.isRESTful_writeApi" :cfg="cfg.createApi" />
      </Form>
    </TabPane>

    <TabPane label="状态控制">
      <p align="center">控制字段在不同状态下是否显示，打勾表示隐藏。</p>
      <br />
      <table align="center" width="90%" class="aj-table">
        <tr>
          <th></th>
          <th>当查看时</th>
          <th>当新增时</th>
          <th>当修改时</th>
        </tr>
        <tbody v-if="cfg.fields && cfg.fields.length">
          <tr v-for="(item, key) in cfg.fields" :key="key">
            <td align="center" style="width:130px">
              {{item.name}} {{item.label}}
            </td>
            <td>
              <div class="checkbox_holder">
                <Checkbox size="small" />
              </div>
            </td>
            <td>
              <div class="checkbox_holder">
                <Checkbox size="small" />
              </div>
            </td>
            <td>
              <div class="checkbox_holder">
                <Checkbox size="small" />
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </TabPane>
  </Tabs>

</template>

<script lang="ts">
import ListSelector from "../factory-list/list-selector.vue";
import ApiBinding from "../widget/api-binding.vue";

export default {
  components: { ListSelector, ApiBinding },
  props: {
    cfg: Object,
    apiRoot: { type: String, required: true }, // API 选择器需要这个属性
  },
  data() {
    return {};
  },
  methods: {},
  mounted() {
    // debugger
  },
};
</script>

<style lang="less" scope>
// 快速制作1px 表格边框，为需要设置的 table 元素加上 border 的class即可
.aj-table {
  border: 1px lightgray solid;
  border-collapse: collapse;
  border-spacing: 0;

  th {
    background-color: #efefef;
    letter-spacing: 3px;
  }

  td,
  th {
    border: 1px lightgray solid;
    line-height: 160%;
    height: 120%;
    padding: 6px;
  }

  tr {
    // .transition (background-color 400ms ease-out);

    &:nth-child(odd) {
      background: #f5f5f5;
      box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8) inset;
    }

    &:hover {
      background-color: #fbf8e9;
    }
  }
}

.checkbox_holder {
  width: 12px;
  margin: 0 auto;
}
</style>