<template>
  <div style="padding:2%">
    <h2>用户角色管理</h2>
    <UserRole name="角色" :list="UserRole_List" />
  </div>
</template>

<script>
import UserRole from '../../components/tree-table/index.vue';
import { UserRole_List } from '../../components/admin-page/tree-like-cmp';

export default {
  components: { UserRole },
  data() {
    return {
      UserRole_List,
    };
  }
}
</script>