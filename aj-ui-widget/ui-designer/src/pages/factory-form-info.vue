<template>
  <Card :bordered="true" dis-hover style="margin:10px;">
    <FactoryFormAdminInfo :api-root="apiRoot" style="margin: 15px;" />
  </Card>
</template>

<script>
import FactoryFormAdminInfo from '../components/factory-form/form-factory.vue';

export default {
  data() {
    return { apiRoot: window.config.dsApiRoot };
  },
  components: { FactoryFormAdminInfo }
}
</script>