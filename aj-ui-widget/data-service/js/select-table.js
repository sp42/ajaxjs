Vue.component('select-table', {
    template: `<div>ddddddddddddddddddd
    <Row style="margin:20px 0;" type="flex" justify="center" align="middle">
      <Col v-if="isCrossDb" span="6" style="padding-right:10px;">
      <Select placeholder="请选择数据库名" v-model="databaseName">
        <Option v-for="d in databaseList" :key="d" :value="d">{{d}}</Option>
      </Select>
      </Col>
      <Col :span="isCrossDb ? 6 : 12">
      <Input search enter-button placeholder="按表名模糊搜索" @on-keyup="search" ref="inputEl" />
      </Col>
      <Col span="12">
      &nbsp;&nbsp;&nbsp;<a @click="resetData">重置</a>
      </Col>
    </Row>

    <Table :columns="list_column" :data="data" width="100%" @on-selection-change="onSelect" max-height="500">
      <template slot-scope="{row}" slot="select">
        <a @click="$emit('on-select', row, databaseName)">选择</a>
      </template>
    </Table>

    <div class="ivu-mt ivu-text-right" v-if="!isFilter">
      <Page :total="total" :current.sync="current" show-total :page-size="pageSize" />
    </div>
  </div>`,
    data() {
        return {
            isCrossDb: false,
            list_column: [
                {
                    title: "#",
                    // key: 'id',
                    width: 60,
                    type: "index",
                    // type: 'selection'
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

            databaseList: [],
            databaseName: "",
        }
    },
    props: {
        listColumn: { type: Array, required: true },
        apiUrl: { type: String, required: true },
        dsid: {},
    },
    methods: {
        getData() {
            let p = {
                start: this.start,
                limit: this.pageSize,
            };

            if (this.searchKeyword) p.tablename = this.searchKeyword;
            if (this.databaseName) p.dbName = this.databaseName;

            xhr_get(this.apiUrl, getPageList(this, this), p);
        },
        handleChangePageSize() {
            let start = (this.current - 1) * this.pageSize,
                end = start + this.pageSize;

            this.listData = this.data.slice(start, end);
        },
        onSelect(arr) {
            arr.forEach((item) => {
                this.data.forEach((_item) => { });
            });
        },
        resetData() {
            this.searchKeyword = "";
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
        isCrossDb(v) {
            if (v) {
                xhr_get(this.$parent.$parent.API + "/getDatabases",
                    (j) => {
                        if (j.result) {
                            this.databaseList = j.result;
                        } else this.$Message.warning("获取数据库名失败");
                    },
                    { datasourceId: this.dsid }
                );
            }
        },
        databaseName(v) {
            this.getData();
        },
    },
});