<template>
  <router-view />
</template>

<script>
export default {
  created() {
    window.config = { dsApiRoot: 'http://127.0.0.1:8088' };
    // window.config = { dsApiRoot: '/entity' };
    document.title = "My UI library";
  }
}
</script>