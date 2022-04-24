<template>
  <div style="padding:2%">
    <h2>数据字典</h2>
    <br />
    <DataDict name="数据字典" :list="list" :api="api" :form-fields="formFields" />
  </div>
</template>

<script>
import DataDict from '../../tree-table/index.vue';

export default {
  components: { DataDict },
  data() {
    const api = window['config'].dsApiRoot + '/api/cms/sys_datadict';

    return {
      list: [
        {
          title: '名称',
          key: 'name',
          align: 'left',
          tree: true,
          maxWidth: 200
        },
        {
          title: '值（value）',
          key: 'value',
          align: 'center',
        },
        {
          title: '说明',
          align: 'center',
          key: 'content'
        },
        {
          title: '扩展属性',
          align: 'center',
          key: 'extraData'
        },
        {
          title: '创建日期',
          align: 'center',
          width: 150,
          render(h, { row }) {
            if (row.createDate) {
              let arr = row.createDate.split(':');
              arr.pop();

              return h('span', arr.join(':'));
            }
          },
          // key: 'createDate'
        },
      ],

      api: Object.freeze({
        list: api + '/list',
        create: api,
        update: api,
        delete: api,
      }),
      formFields: [{
        name: '值（Value）',
        key: 'value'
      }]
    };
  }
}
</script>