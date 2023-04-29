<template>
  <Tabs value="name1" :animated="false" style="padding:1% 3%;">
    <TabPane label="组织列表" name="name1">
      <p>按分级组织树结构对机构、部门、人员进行管理</p>
      <TreeTable name="组织机构" :list="list" :api="api" :form-fields="formFields" />
    </TabPane>
    <TabPane label="职务列表" name="name2">
      <p>按按照职务层级关系配置职务，关联相关用户</p>
      <TreeTable name="职务" :list="list" :api="api" :form-fields="formFields" />
    </TabPane>
    <TabPane label="岗位列表" name="name3">
      <p>从整体角度概览配置岗位，关联相关用户</p>
      <ListLoader :id="120" />
    </TabPane>
    <TabPane label="工作群组列表" name="name4">
      <p>从群组分类的角度概览配置群组，关联相关用户</p>
      <ListLoader :id="120" />
    </TabPane>
    <TabPane label="任职列表" name="name5">
      <p>反映组织、职务和岗位的用户视图</p>
      <ListLoader :id="120" />
    </TabPane>
  </Tabs>
</template>

<script>
import ListLoader from '../../../../components/factory-list/list-loader.vue';
import TreeTable from '../../../tree-table/index.vue';

export default {
  components: { ListLoader, TreeTable },
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

<style scoped>
p {
  padding-bottom: 1%;
  color: gray;
}
</style>