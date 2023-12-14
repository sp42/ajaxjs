<template>
  <div class="table-selector">
    <Row style="margin:20px 0;" type="flex" justify="center" align="middle">
      <i-Col v-if="isCrossDb" span="6" style="padding-right:10px;">
        <i-Select placeholder="请选择数据库名" v-model="databaseName">
          <i-Option v-for="d in databaseList" :key="d" :value="d">{{d}}</i-Option>
        </i-Select>
      </i-Col>
      <i-Col :span="isCrossDb ? 6 : 12">
        <i-Input search enter-button placeholder="按表名模糊搜索" @on-keyup="search" ref="inputEl"></i-Input>
      </i-Col>
      <i-Col span="12">
        &nbsp;&nbsp;&nbsp;<a @click="resetData">重置</a>
      </i-Col>
    </Row>

    <i-Table :columns="list_column" :data="data" width="100%" @on-selection-change="onSelect" max-height="300" :loading="loading" class="table-selector-table">
      <template slot-scope="{row}" slot="select">
        <a @click="$emit('on-select', row, databaseName)">选择</a>
      </template>
    </i-Table>
    <br />

    <div class="ivu-mt ivu-text-right" v-if="!isFilter">
      <Page :total="total" :current.sync="current" show-total :page-size="pageSize" />
    </div>
  </div>
</template>

<script>
import { xhr_get, xhr_post, getPageList } from '../../util/xhr';

// 选择表
export default {
  data() {
    return {
      // isCrossDb: false,
      list_column: [
        {
          title: "#",
          // key: 'id',
          width: 60,
          type: "index",
          // type: 'selection'
        },
        {
          title: "表名",
          key: "tableName",
        },
        {
          title: "说明",
          key: "comment",
        },
        {
          title: "操作",
          slot: "select",
        },
      ],
      listData: [], // 显示数据
      data: [], // 全部数据
      total: 0,
      current: 1,
      pageSize: 5,
      isFilter: false,
      start: 0,
      searchKeyword: "",
      loading: false,
      databaseList: [],
      databaseName: "",
    };
  },
  props: {
    isCrossDb: { type: Boolean, required: true },
    dataSourceId: { type: Number, required: true },
  },
  mounted() {
    // this.isCrossDb = true;
    this.getData();
  },
  methods: {
    getData() {
      this.loading = true;
      let url =`${window.config.dsApiRoot}/datasource/${this.dataSourceId}/get_all_tables?start=${this.start}&limit=${this.pageSize}`;
      // let url = `${DS_CONFIG.API_ROOT}/admin0/${this.dataSourceId}/getAllTables?start=${this.start}&limit=${this.pageSize}`;

      if (this.searchKeyword) url += `&tableName=${this.searchKeyword}`;
      if (this.databaseName) url += `&dbName=${this.databaseName}`;

     xhr_get(url, (j) => {
        if (j.status) {
          this.data = j.data.rows;
          this.total = j.data.total;
        } else this.$Message.error(j.message);

        this.loading = false;
      });
    },
    handleChangePageSize() {
      let start = (this.current - 1) * this.pageSize,
        end = start + this.pageSize;

      this.listData = this.data.slice(start, end);
    },
    onSelect(arr) {
      arr.forEach((item) => {
        this.data.forEach((_item) => {});
      });
    },
    resetData() {
      this.searchKeyword = "";

      this.start = 0;
      this.getData();
      this.$refs.inputEl.$el.querySelector("input").value = "";
    },
    search(ev) {
      let input = ev.target,
        v = input.value;

      if (v) {
        this.searchKeyword = v;
        this.start = 0;
        this.current = 1;
        this.getData();
      }
    },
    onkeypress(ev) {
      let input = ev.target,
        v = input.value;

      if (v) {
        this.isFilter = true;
        let arr = this.data.filter((item) => item.tableName.indexOf(v) != -1);
        this.listData = arr;
      } else {
        this.resetData();
      }
    },
  },
  watch: {
    /**
     * 分页
     *
     * @param v
     */
    current(v) {
      this.start = (v - 1) * this.pageSize;
      this.getData();
    },
    dataSourceId(id) {
      if (id) {
        if (this.isCrossDb)
          aj.xhr.get(
            `${DS_CONFIG.API_ROOT}/admin/${this.dataSourceId}/get_databases`,
            (j) => {
              if (j.status) {
                // 过滤 mysql 自带的库
                let not = ["information_schema", "performance_schema", "sys", "mysql"];
                let arr = j.data.filter((db) => !not.includes(db));
                this.databaseList = arr;

                // 默认选中第一个
                this.start = 0;
                this.current = 1;
                this.databaseName = arr[0];
              } else this.$Message.error(j.message);
            }
          );
        else {
          this.databaseName = null;
          this.getData();
        }
      }
    },
    databaseName(v) {
      this.getData(); // 会重复请求
    },
  },
};
</script>