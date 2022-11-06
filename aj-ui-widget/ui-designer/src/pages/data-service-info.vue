
<template>
  <Info :api-root="apiRoot" style="margin: 1%;" />
</template>

<script>
import Info from '../components/data-service/info/info.vue';

export default {
  data() {
    return { apiRoot: window.config.dsApiRoot };
  },
  components: { Info }
}
</script>