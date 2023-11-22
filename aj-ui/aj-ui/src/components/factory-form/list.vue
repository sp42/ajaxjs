<template>
  <div>
    <SearchPanel :search="list.search" :cmp="this">
      <Input suffix="ios-search" v-model="list.search.name" @on-enter="getData" clearable placeholder="请输入模型名称" />
    </SearchPanel>
    <br />
    <div>
      <!--  <TagListPanel @tag_checked="tag_checked" /> -->

      <Card :bordered="false" dis-hover>
        <div style="margin-bottom:20px;">
          <Tooltip class="ivu-ml" content="刷新" placement="top" style="float:right;padding-top:10px;">
            <Icon size="20" type="ios-refresh" @click="getData" style="cursor: pointer;" />
          </Tooltip>
          <Button type="primary" icon="md-add" @click="$router.push({ path: 'factory-form-info'})">新建模型</Button>
        </div>

        <Table :columns="list.columns" :data="list.data" :loading="list.loading" height="500">
          <template slot-scope="{ row, index }" slot="action">
            <a @click="openPerview(row)">预览表单</a>
            <Divider type="vertical" />
            <a style="color:green;" @click="goInfo(row.id, 'factory-form-info')">编辑</a>
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

    <FormPerviewLoader ref="FormPerviewLoader" />
  </div>
</template>

<script>
import List from "../widget/list";
import TagListPanel from "../widget/tag-list-panel.vue";
import CommonFactory from "../widget/factory-list-mixins";
import SearchPanel from "../widget/search-panel.vue";
import FormPerviewLoader from "./loader.vue";
import { xhr_get } from "../../util/xhr";

export default {
  components: { SearchPanel, TagListPanel, FormPerviewLoader },
  mixins: [CommonFactory],
  data() {
    return {
      API: this.api || `${this.apiRoot}/common_api/widget_config`,
      listParams: "q_type=FORM",
      list: {
        columns: [
          List.id,
          {
            title: "模型名称",
            key: "name",
            minWidth: 130,
            ellipsis: true,
            tooltip: true,
          },
          { title: "数据模型（表名）", key: "tableName", width: 180 },
          {
            title: "接口地址",
            minWidth: 190,
            key: "apiUrl",
            ellipsis: true,
            tooltip: true,
          },
          List.createDate,
          // List.status,
          { title: "操作", slot: "action", align: "center", width: 260 },
        ],
        data: [],
        total: 0,
        start: 0,
        limit: 9,
        loading: false,
        search: {
          name: "",
        },
      },
    };
  },

  mounted() {
    this.getData();
  },

  methods: {
    tag_checked(checkedList) {
      console.log(checkedList);
    },

    /**
     * 预览
     *
     * @param row
     */
    openPerview(row) {
      let loader = this.$refs.FormPerviewLoader;
      if (loader && loader.$refs.FromRenderer)
        loader.$refs.FromRenderer.data = {}; // 清除之前的数据

      xhr_get(`${this.API}/${row.id}`, (j) => {
        let r = j.data;

        if (r) {
          loader.name = r.name;
          loader.cfg = r.config; // 数据库记录转换到 配置对象;
          loader.isShow = true;
        } else {
          this.$Message.error("未有任何配置");
          loader.cfg = {};
        }
      });
    },
  },
};
</script>