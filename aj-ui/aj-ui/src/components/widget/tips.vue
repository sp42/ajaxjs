<template>
  <i style="cursor: pointer;" class="ivu-icon ivu-icon-ios-help-circle-outline" :title="text"></i>
</template>

<script>
export default {
  props: {
    text: { type: String, required: true },
  },
};
</script>
