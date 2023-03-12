<template>
  <ul class="toolbar">
    <li style="float:right" @click="$parent.$parent.refreshConfig">
      <div>
        <div class="icon">
          <Icon type="md-refresh" size="25" style="color:darkcyan" />
        </div>
        <span class="text">刷新配置</span>
      </div>
    </li>
    <li style="float:right" @click="$parent.$parent.dataSource.isShowDataSource=true">
      <div>
        <div class="icon">
          <Icon type="md-swap" size="25" style="color:blueviolet" />
        </div>
        <span class="text">数据源</span>
      </div>
    </li>
    <li @click="$parent.$parent.isShowSelectTable=true">
      <div>
        <div class="icon">
          <Icon type="md-add" size="25" style="color:green" />
        </div>
        <span class="text">新建服务</span>
      </div>
    </li>
    <li>
      <div>
        <div class="icon">
          <Icon type="md-close" size="25" style="color:red" />
        </div>
        <span class="text">删除</span>
      </div>
    </li>
    <li>
      <div>
        <div class="icon">
          <Icon type="ios-add" size="25" style="color:cadetblue" />
        </div>
        <span class="text">新建命令</span>
      </div>
    </li>
    <li @click="$parent.$parent.saveDML">
      <div>
        <div class="icon">
          <Icon type="ios-create" size="25" style="color:#f90" />
        </div>
        <span class="text">保存</span>
      </div>
    </li>
  </ul>
</template>

<script>
export default {
  methods: {
    refreshConfig() {
      this.$parent.refreshConfig();
    },
  },
};
</script>

<style lang="less" scoped>
.toolbar {
  border-bottom: 1px solid white;
  padding: 2px 15px;
  padding-top: 12px;
  background-color: #eaeaea;
  background-image: linear-gradient(#fefefe, #e6e6e6);
  text-shadow: 1px 1px 1px white;

  li {
    width: 50px;
    text-align: center;
    list-style: none;
    color: gray;

    display: inline-block;
    margin-right: 20px;

    &:hover {
      .text {
        color: black;
      }

      .icon {
        border-color: gray;
      }

      cursor: pointer;
    }

    .icon {
      width: 32px;
      border-radius: 3px;
      box-shadow: 1px 1px 1px white;
      border: 1px solid lightgray;
      margin: 0 auto;
      margin-bottom: 5px;
    }

    .text {
      font-size: 0.73rem;
    }
  }
}
</style>