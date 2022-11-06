<template>
  <!-- 参数控制 -->
  <Form :label-width="80">
    <FormItem label="参数名称">
      <Input v-model="args.name" placeholder="请输入参数的变量名，必填" />
    </FormItem>

    <FormItem label="参数类型">
      <Select>
        <Option></Option>
      </Select>
    </FormItem>

    <FormItem label="是否必填">
      <Checkbox v-model="single">必填</Checkbox>
    </FormItem>

    <FormItem label="参数值">
      <Select>
        <Option></Option>
      </Select>
    </FormItem>

    <FormItem label="校验正则">
      <Input v-model="args.name" placeholder="请输入参数的变量名，必填" />
    </FormItem>

    <FormItem label="参数说明">
      <Input v-model="args.name" type="textarea" :rows="4" placeholder="请输入参数的变量名，必填" />
    </FormItem>

  </Form>
</template>

<script lang="ts">
export default {
  data(): {} {
    return {
      args: {} as API_HELPER_ARGUMENT,
    };
  },
};
</script>

<style lang="less" scoped>
</style>