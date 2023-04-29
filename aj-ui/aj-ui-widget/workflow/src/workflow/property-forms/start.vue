<template>
  <table>
    <tr>
      <td class="key">id</td>
      <td><input type="text" v-model="cop.id" title="全局唯一 id" /></td>
    </tr>
    <tr>
      <td class="key">前置拦截器</td>
      <td><input type="text" name="preInterceptors" /></td>
    </tr>
    <tr>
      <td class="key">后置拦截器</td>
      <td><input type="text" name="postInterceptors" /></td>
    </tr>
  </table>
</template>

<script>
import PropertyFormCommon from './mixins';

export default {
  name: 'aj-wf-start-form',
  mixins: [PropertyFormCommon]
}
</script>