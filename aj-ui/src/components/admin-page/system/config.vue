<template>
  <Tabs value="name4" style="margin:20px">
    <TabPane label="Web 安全" name="name4">
      <FormLoader formid="17" entityid="webSecurity" class="form" />
    </TabPane>
  </Tabs>
</template>

<script>
import FormLoader from '../../../components/factory-form/form-loader.vue';

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