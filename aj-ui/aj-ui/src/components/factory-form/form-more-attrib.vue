<template>
  <Form :label-width="100" style="padding:0 10% 0 0" label-colon>
     <Row>
      <Col span="12">
      <FormItem label="是否禁用">
        <i-Switch v-model="row.ext_attribs.disabled" />
        &nbsp;<Tooltip content="disabled 设置输入框为禁用状态" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
      </FormItem>
      </Col>
      <Col span="12">
      <FormItem label="是否只读">
        <i-Switch v-model="row.ext_attribs.isReadonly" />
        &nbsp;<Tooltip content="readonly 设置输入框为只读" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
      </FormItem>
      </Col>
    </Row>

    <!-- 输入框 -->
    <Row v-if="isInput()">
      <Col span="12">
      <FormItem label="最大长度">
        <Input type="text" class="more-attrib input small" v-model="row.ext_attribs.maxlength" placeholder="最大输入长度" /> &nbsp;
        <Tooltip content="maxlength 最大输入长度" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
      </FormItem>
      </Col>
      <Col span="12">
      </Col>
    </Row>

    <Row v-if="isInput()">
      <Col span="12">
      <FormItem label="清空按钮">
        <i-Switch v-model="row.ext_attribs.clearable" />
        &nbsp;<Tooltip content="clearable 是否显示清空按钮" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
      </FormItem>
      </Col>
      
      <Col span="12">
      <FormItem label="显示边框">
        <i-Switch v-model="row.ext_attribs.border" />
        &nbsp;<Tooltip content="border 是否显示边框" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
      </FormItem>
      </Col>
    </Row>

    <Row v-if="isInput()">
      <Col span="12">
      <FormItem label="头部图标">
        <IconSelector />
      </FormItem>
      </Col>
      <Col span="12">
      <FormItem label="尾部图标">
        <IconSelector />
      </FormItem>
      </Col>
    </Row>

    <!-- 密码输入框 -->
    <FormItem v-if="row.uiType == 18" label="允许显示/隐藏密码">
      <Switch v-model="row.ext_attribs.password" />
      &nbsp;<Tooltip content="开启属性 password 可以切换显示隐藏密码" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
    </FormItem>

    <!-- 多行文本 -->
    <FormItem v-if="row.uiType == 6" label="行数/高度">
      <Input type="number" class="more-attrib input small" v-model="row.ext_attribs.rows" placeholder="文本域默认行数" />&nbsp;
      <Tooltip content="rows 文本域默认行数" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
    </FormItem>

    <Row v-if="row.uiType == 6">
      <Col span="12">
      <FormItem label="字数统计">
        <Switch v-model="row.ext_attribs.showWordLimit" />&nbsp;
        <Tooltip content="show-word-limit 是否显示输入字数统计，可以配合 maxlength 使用" placement="right" :transfer="true"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
      </FormItem>
      </Col>
      <Col span="12">
      <FormItem label="自适应内容高度">
        <Switch v-model="row.ext_attribs.autosize" />&nbsp;
        <Tooltip content="autosize 自适应内容高度" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
      </FormItem>
      </Col>
    </Row>

    <FormItem v-if="row.uiType == 6" label="行数/高度">
      <Input type="number" class="more-attrib input small" v-model="row.ext_attribs.rows" placeholder="文本域默认行数" />&nbsp;
      <Tooltip content="rows 文本域默认行数" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip>
    </FormItem>

    <!-- 单选框 -->
    <FormItem v-if="row.uiType == 2 || row.uiType == 3 || row.uiType == 4" label="候选数据">
      <CandidateData ref="CandidateData" />
    </FormItem>

  </Form>
</template>

<script lang="ts">
import IconSelector from "../widget/icon-select.vue";
import CandidateData from "./candidate-data.vue";

export default {
  components: { IconSelector, CandidateData },
  props: {
    row: {
      type: Object,
      default() {
        return { ext_attribs: {} };
      },
    },
  },
  methods: {
    // edit(row: any): void {
    //   if (!row.ext_attribs) 
    //     row.ext_attribs = {};

    //   this.row = row;


    //   this.$nextTick(() => {
    //     if (row.uiType == 2 || row.uiType == 3 || row.uiType == 4) {
    //       if (!row.ext_attribs.candidateData)
    //         row.ext_attribs.candidateData = [];

    //       this.$refs.CandidateData.staticData = row.ext_attribs;
    //       // console.log(this.$refs.CandidateData.staticData);
    //     }
    //   });
    // },

    /**
     * 是否输入框类型
     */
    isInput(): boolean {
      let row = this.row;
      return (
        row.uiType == 1 ||
        row.uiType == 18 ||
        row.uiType == 11 ||
        row.uiType == 6 ||
        row.uiType == 16 ||
        row.uiType == 12 ||
        row.uiType == 13
      );
    },
  },
  watch: {
    row(row) {
      // debugger
      if (!row.ext_attribs) 
        row.ext_attribs = { disabled: false};
      
      this.$nextTick(() => {
        if (row.uiType == 2 || row.uiType == 3 || row.uiType == 4) {
          if (!row.ext_attribs.candidateData)
            row.ext_attribs.candidateData = [];

          this.$refs.CandidateData.staticData = row.ext_attribs;
          // console.log(this.$refs.CandidateData.staticData);
        }
      });
      // if (!v.ext_attribs.hasOwnProperty("placeholder")) {
      //   v.ext_attribs.placeholder = "";
      // }
    },
  },
};
</script>

<style lang="less" scoped>
.more-attrib.input {
  width: 260px;
}

.more-attrib.input.small {
  width: 100px;
}
</style>