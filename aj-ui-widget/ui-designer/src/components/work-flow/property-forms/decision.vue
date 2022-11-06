<template>
  <table>
    <tr>
      <td class="key">id</td>
      <td><input type="text" v-model="cop.id" /></td>
    </tr>
    <tr>
      <td class="key">决策表达式</td>
      <td><input type="text" v-model="cop.rawData.expr && cop.rawData.expr.value" /></td>
    </tr>
    <tr>
      <td class="key">处理类名称</td>
      <td><input type="text" v-model="cop.rawData.handleClass && cop.rawData.handleClass.value" /></td>
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
  name: 'aj-wf-decision-form',
  mixins: [PropertyFormCommon]
}
</script>