<template>
  <div>
     <SearchPanel :search="list.search" :cmp="this">
      <Input suffix="ios-search" v-model="list.search.name" @on-enter="getData" clearable placeholder="请输入列表名称" />
    </SearchPanel> 
    <br />
    <div>
      <!--     <TagListPanel /> -->

      <!--     <Card :bordered="false" dis-hover style="float:left; width: 85%"> -->
      <Card :bordered="false" dis-hover>
        <div style="margin-bottom:20px;">
          <Tooltip class="ivu-ml" content="刷新" placement="top" style="float:right;padding-top:10px;">
            <Icon size="20" type="ios-refresh" @click="getData" style="cursor: pointer;" />
          </Tooltip>
          <Button type="primary" icon="md-add" @click="$router.push({ path: 'factory-list-info'})">新建列表</Button>
        </div>

        <Table :columns="list.columns" :data="list.data" :loading="list.loading" height="500">
          <template slot-scope="{ row, index }" slot="action">
            <a @click="openDemo(row.id)">预览</a>
            <Divider type="vertical" />
            <a style="color:green;" @click="goInfo(row.id, 'factory-list-info')">编辑</a>
            <Divider type="vertical" />
            <Poptip confirm transfer title="是否要删除此行？" @on-ok="deleteInfo(row.id, index)">
              <a style="color:red;">删除</a>
            </Poptip>
          </template>
        </Table>
        <div class="ivu-mt ivu-text-right">
          <Page :total="list.total" :current.sync="list.pageNo" show-total show-sizer :page-size="list.limit" @on-page-size-change="handleChangePageSize" />
        </div>
      </Card>
    </div>

    <Modal v-model="isShowEdit" title="编辑" width="1300" ok-text="关闭" cancel-text="">
      <ListFactory ref="WidgetFactory" :api-root="apiRoot" :api="api" />
    </Modal>
  </div>
</template>

<script lang="ts">
import List from '../widget/list';
import TagListPanel from '../widget/tag-list-panel.vue';
import CommonFactory from '../widget/factory-list-mixins';
import SearchPanel from '../widget/search-panel.vue';
import ListFactory from './list-factory.vue';

/**
 * 管理界面列表
 */
export default {
    components: { ListFactory, SearchPanel, TagListPanel },
    mixins: [CommonFactory],
    data() {
        return {
            // @ts-ignore
            API: this.api || `${this.apiRoot}/common_api/widget_config`,
            listParams: 'q_type=LIST',
            list: {
                columns: [
                    List.id,
                    { title: '列表名称', key: 'name', minWidth: 130, ellipsis: true, tooltip: true },
                    {
                        title: '关联数据库', render(h: Function, params: any) {
                            if (params.row.datasourceName)
                                return h('span', params.row.datasourceName + "/" + params.row.tableName);
                            else
                                return h('span', params.row.tableName);
                        }, width: 280, ellipsis: true
                    },
                    {
                        title: '接口地址', minWidth: 230, render: (h: Function, params: any) => h('span', params.row.config.dataBinding.url),
                        ellipsis: true, tooltip: true
                    },
                    List.createDate,
                    // List.status,
                    { title: '操作', slot: 'action', align: 'center', width: 260 }
                ],
                data: [],
                total: 0,
                start: 0,
                limit: 9,
                loading: false,
                search: { name: '' },
            } as TableListConfig
        };
    },

    mounted() {
        // this.openInfo({ name: 'test', id: 105 });
        // this.openDemo(104)
        this.getData();
    },

    methods: {
        /**
         * 预览
         * 
         * @param id 
         */
        openDemo(id: number): void { // 直接调用，不另外新建一套预览。内部逻辑复杂
            this.$refs.WidgetFactory.id = id;
            this.$refs.WidgetFactory.getData(() => this.$refs.WidgetFactory.doRenderer());
        },
    },
};
</script>