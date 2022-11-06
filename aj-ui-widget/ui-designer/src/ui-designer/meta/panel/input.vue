<template>
  <Form label-position="top">
    <FormItem label="输入框类型">
      <Select class="select tooltip" size="small" v-model="$parent.props.type">
        <Option value="text">text - 文本</Option>
        <Option value="password">password - 密码</Option>
      </Select>
      <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="输入框类型，可选值为 text、password、textarea、url、email、date、number、tel"></i>
    </FormItem>
    <FormItem label="占位文本">
      <Input type="text" v-model="$parent.props.placeholder" class="input-text" size="small" placeholder="请输入 placeholder" />
    </FormItem>
  </Form>
</template>

<script lang="ts">
import comm from "./common-mixins";

export default {
  mixins: [comm],

};
</script>
<style lang="less" scoped src="./common.less"></style>