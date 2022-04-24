<template>
  <Modal v-model="isShowListModal" :title="'选择'+ title" ok-text="关闭" cancel-text="" width="800">
    <Row style="margin:20px 0;" type="flex" align="middle">
      <Col span="12">
      <Input search enter-button placeholder="按名称模糊搜索" ref="inputEl" />
      </Col>
      <Col span="12">
      &nbsp;&nbsp;&nbsp;<a>重置</a>
      </Col>
    </Row>

    <Table :columns="list.columns" :data="list.data" max-height="500">
    </Table>
    <div class="ivu-mt ivu-text-right">
      <Page show-total :total="list.total" :current.sync="list.pageNo" :page-size="list.pageSize" />
    </div>
  </Modal>
</template>

<script>
import ListMixins from '../widget/common-list-mixins';

export default {
  mixins: [ListMixins],
  props: {
    title: String,
    columns: Array
  },
  data() {
    return {
      isShowListModal: false,
      listColumns: []
    }
  },
  mounted() {
    this.columns.forEach((item) => this.list.columns.push(item));

    let self = this;
    this.list.columns.push({
      title: '选择',
      width: 100,
      render: (h, params) => {
        return h('a', {
          on: {
            'click': () => {
              self.$emit('on-select', params.row);
              // this.cfg.fields.splice(params.index, 1);
            }
          }
        }, '选择');
      }
    });

    this.getData();
  }
}
</script>