<template>
  <!-- 顶部工具条 -->
  <div class="topbar">
    <div class="right">
      <a title="说明文档" href="javascript:alert('TODO')">
        <i class="ivu-icon ivu-icon-md-help-circle"></i>
      </a>
    </div>
    <div class="title">UI-Designer</div>
    <div class="btns">
      <ul class="right">
        <li @click="$refs.Code.isShowCodeEdit = true">
          <Tooltip content="代码">
            <Icon size="25" type="md-code-working" />
          </Tooltip>
        </li>
        <li @click="$refs.Perview.isShowPerview = true">
          <Tooltip content="预览">
            <Icon size="25" type="md-eye" />
          </Tooltip>
        </li>
      </ul>
      <ul>
        <li @click="save">
          <Tooltip content="保存">
            <Icon size="25" type="md-cloud-download" />
          </Tooltip>
        </li>
        <li>
          <Tooltip content="撤销">
            <Icon size="25" type="md-undo" />
          </Tooltip>
        </li>
        <li>
          <Tooltip content="恢复">
            <Icon size="25" type="md-redo" />
          </Tooltip>
        </li>
        <li @click="$parent.STAGE_RENDER.delWidget()">
          <Tooltip content="删除">
            <Icon size="25" type="md-close" />
          </Tooltip>
        </li>
        <li @click="clearStage">
          <Tooltip content="清空">
            <Icon size="25" type="md-trash" />
          </Tooltip>
        </li>
        <li @click="reload">
          <Tooltip content="刷新">
            <Icon size="25" type="md-refresh" />
          </Tooltip>
        </li>
      </ul>
    </div>

    <Code ref="Code" :meta-data="RenderedMeta" />
    <Perview ref="Perview" :meta-data="RenderedMeta" />
  </div>
</template>

<script>
import Code from './code.vue';
import Perview from './perview.vue';

export default {
  components: { Code, Perview },
  data() {
    return {
      RenderedMeta: []
    };
  },
  mounted() {
    this.RenderedMeta = this.$parent.$refs.StageRender.RenderedMeta;
    metaDataBackup = JSON.stringify(this.RenderedMeta); // 复位刷新用

    // this.$nextTick(() => {
    document.onkeydown = (e) => { (e.key == 'Delete') && this.$parent.STAGE_RENDER.delWidget(); };
    // });
  },
  methods: {
    save() {
      alert('TODO');
    },

    /**
     * 清空舞台
     */
    clearStage() {
      this.$Modal.confirm({
        title: "操作提示：", content: "亲：清空后将无法恢复，您确定要清空吗？", okText: "我确定", cancelText: "再想想",
        onOk: () => {
          this.$parent.STAGE_RENDER.ActiveWdiget = null;
          this.$parent.STAGE_RENDER.RenderedMeta = [];
        },
      });
    },

    /**
     * 刷新
     */
    reload() {
      this.$parent.STAGE_RENDER.ActiveWdiget = null;
      this.$parent.STAGE_RENDER.RenderedMeta = JSON.parse(metaDataBackup);
    }
  }
}

let metaDataBackup = "";
</script>

<style lang="less" scoped>
.topbar {
  width: 100%;
  height: 60px;
  line-height: 60px;
  box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.1);
  text-align: left;
  padding: 0 15px;
  position: relative;
  z-index: 999;
  background-color: white;

  .title {
    color: #47b2ff;
    font-size: 25px;
    font-weight: 700;
    width: 310px;
    float: left;
  }

  .btns {
    ul {
      &.right {
        float: right;
      }
      li {
        display: inline-block;
        cursor: pointer;
        width: 48px;
        text-align: center;

        transition: background-color ease-in 200ms;

        &:hover {
          background-color: #e7e8eb;
        }
      }
    }
  }

  .right {
    float: right;
    width: 300px;
    font-size: 24px;
    text-align: right;
  }
}
</style>