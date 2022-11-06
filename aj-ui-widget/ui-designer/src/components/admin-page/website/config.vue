<template>
  <Tabs value="name1" style="margin:20px" :animated="false">
    <TabPane label="网站信息" name="name1">
      <FormLoader formid="1" entityid="websiteInfo" class="form" />
    </TabPane>

    <TabPane label="网站结构控制" name="name3">标签三的内容</TabPane>
    <TabPane label="页面编辑" name="name2">
      <FormLoader formid="2" class="form" />
    </TabPane>
  </Tabs>
</template>

<script>
import FormLoader from '../../factory-form/form-loader.vue';

export default {
  components: { FormLoader }
}
</script>

<style lang="less" scoped>
.form {
  width: 60%;
  margin: 30px auto;
}
</style>