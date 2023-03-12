<script lang="ts" src="./stage-render.ts"></script>

<style lang="less" scoped>
.input {
  max-width: 400px;
  margin-right: 10px;
}
</style>

<style lang="less">
// 当容器没有任何元素的时候，默认变矮，很难拖入进来等下，于是把它撑高
.form-designer .stage .ivu-form .ivu-form-item-content > div {
  min-height: 30px;
}
</style>