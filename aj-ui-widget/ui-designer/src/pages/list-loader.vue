<template>
  <div>
    <h1>{{title}}</h1>
    <Card :bordered="true" dis-hover style="margin-top:10px;">
      <ListLoader />
    </Card>
  </div>
</template>

<script>
import ListLoader from '@/components/factory-list/list-loader.vue';

export default {
  components: { ListLoader },
  data() {
    return {
      title: ''
    }
  },
  mounted() {
    let query = this.$route.query;
    if (query.title) this.title = query.title;
  },
  watch: {
    $route() {
      let query = this.$route.query;
      if (query.title) this.title = query.title;
    },
  },
};
</script>

<style lang="css" scoped>
h1{
    margin:2px 10px;
}
</style>

<style lang="css">
.ivu-text-right{
    padding-top: 15px;
    text-align: right;
}
</style>