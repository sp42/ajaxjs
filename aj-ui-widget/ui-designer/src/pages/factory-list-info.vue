<template>
  <div>
    <Card :bordered="true" dis-hover style="margin:10px;">
      <FactoryListAdminInfo :api-root="apiRoot" style="margin: 15px;" />
    </Card>
  </div>
</template>

<script>
import FactoryListAdminInfo from '../components/factory-list/list-factory.vue';

export default {
  data() {
    return { apiRoot: window.config.dsApiRoot };
  },
  components: { FactoryListAdminInfo }
}
</script>