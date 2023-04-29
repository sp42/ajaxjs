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

<script src="./table-selector.js"></script>

<style lang="less" scoped>
.table-selector .ivu-table-body table {
    font-size: 10.5pt;
}

.table-selector {
    .ivu-page-next,
    .ivu-page-prev {
        padding-top: 7px;
    }
}
</style>
