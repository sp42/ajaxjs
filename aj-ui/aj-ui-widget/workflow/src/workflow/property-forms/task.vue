<template>
  <table>
    <tr>
      <td class="key">id</td>
      <td><input type="text" v-model="cop.id" title="全局唯一 id" /></td>
    </tr>
    <tr>
      <td class="key">名称</td>
      <td><input type="text" v-model="cop.rawData.displayName.value" title="显示名称" /></td>
    </tr>
    <tr>
      <td class="key">表单</td>
      <td><input type="text" v-model="cop.rawData.form && cop.rawData.form.value" title="用户参与的表单任务对应的URL" /></td>
    </tr>
    <tr>
      <td class="key">参与者</td>
      <td><input type="text" v-model="cop.rawData.assignee && cop.rawData.assignee.value" title="任务参与者变量" /></td>
    </tr>
    <tr>
      <td class="key">参与者处理类</td>
      <td><input type="text" v-model="cop.id" title="绑定的表单" /></td>
    </tr>
    <tr>
      <td class="key">任务类型</td>
      <td>
        <select class="aj-select" v-model="cop.rawData.taskType && cop.rawData.taskType.value">
          <option value="Major">主办任务</option>
          <option value="Aidant">协办任务</option>
        </select>
      </td>
    </tr>
    <tr>
      <td class="key">参与类型</td>
      <td>
        <select class="aj-select" v-model="cop.rawData.performType && cop.rawData.performType.value" title="任务参与类型（针对多个参与者），ANY为其中一个参与者完成即往下流转；ALL为所有参与者完成才往下流转">
          <option value="ANY">普通参与</option>
          <option value="ALL">会签参与</option>
        </select>
      </td>
    </tr>

    <tr>
      <td class="key">提醒时间</td>
      <td><input type="text" v-model="cop.id" title="任务参与者变量" /></td>
    </tr>
    <tr>
      <td class="key">重复提醒间隔</td>
      <td><input type="text" v-model="cop.id" title="任务参与者变量" /></td>
    </tr>
    <tr>
      <td class="key">期望完成时间</td>
      <td><input type="text" v-model="cop.id" title="任务参与者变量" /></td>
    </tr>
    <tr>
      <td class="key">是否自动执行</td>
      <td><input type="checkbox" title="超时是否自动执行" /></td>
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
import Vue from "vue";
import PropertyFormCommon from './mixins';

export default Vue.extend({
  name: 'aj-wf-task-form',
  mixins: [PropertyFormCommon],
  
});
</script>