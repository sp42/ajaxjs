<template>
  <Tabs value="name1" :animated="false" style="padding:1% 3%;">
    <TabPane label="用户列表一览" name="name1">
      <ListLoader :id="121" />
    </TabPane>
    <TabPane label="登录日志" name="name2">
      <ListLoader :id="120" />
    </TabPane>
  </Tabs>
</template>

<script>
import ListLoader from '../../../components/factory-list/list-loader.vue';

export default {
  components: { ListLoader }
}
</script>