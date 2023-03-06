<template>
  <Form label-position="top">
    <FormItem label="输入框类型">
      <Select class="select tooltip" size="small" v-model="$parent.props.type">
        <Option value="default">default - 默认</Option>
        <Option value="primary">primary - 主要的</Option>
        <Option value="dashed">dashed - 虚线框</Option>
        <Option value="text">text - 文本</Option>
      </Select>
      <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="按钮类型，可选值为 default、primary、dashed、text、info、success、warning、error或者不设置"></i>
    </FormItem>
    <FormItem label="按钮文本">
      <Input type="text" v-model="$parent.meta.text" class="input-text" size="small" placeholder="请输入 placeholder" />
    </FormItem>
  </Form>
</template>

<style lang="less" scoped src="./common.less"></style>