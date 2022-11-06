<template>
  <Form label-position="top">
    <FormItem label="标签文本">
      <Input type="text" v-model="$parent.props.label" class="input-text" size="small" placeholder="请输入标签文本" />
    </FormItem>
  </Form>
</template>

<style lang="less" scoped src="./common.less"></style>