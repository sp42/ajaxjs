<template>
  <Card :bordered="false" dis-hover style="float:left; width: 14%;margin-right: 1%;min-height:640px;">
    <slot></slot>
    <div class="ivu-pb">
      <Icon type="md-pricetags" />
      <span class="ivu-ml-8">分类标签</span>
    </div>
    <div v-for="(item, index) in list" :key="index" @click="tagClk(item)">
      <p class="tags" :class="{'select-tags': item.checked}">
        <span class="tags-span">{{item.name}}</span>
      </p>
    </div>
  </Card>
</template>

<script>
import { xhr_get } from '../../util/xhr';
import { isDebug } from '../../util/utils';

export default {
  data() {
    return {
      list: [
        {
          // tagIndex: 1, name: 'dfdsf'
        }
      ]
    };
  },
  mounted() {
    let API = window.config.dsApiRoot;

    xhr_get(`${API}/api/bdp/sys_tag/list`, (j) => {
      if (j.isOk)
        this.list = j.result.map((item) => {
          item.checked = false;
          return item;
        });
      else j.msg && this.$Message.warning(j.msg);
    },
      { start: 0, limit: 99 }
    );
  },
  methods: {
    tagClk(tag) {
      tag.checked = !tag.checked;
      let checkedList = this.list.filter((item) => item.checked);
      this.$emit('tag_checked', checkedList);
    }
  }
};
</script>


<style lang="less" scoped>
.form-item {
  /deep/.ivu-form-item {
    margin-bottom: 0;
  }
}
.tags {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px;
  margin-bottom: 8px;
  cursor: pointer;

  &-span {
    width: 100%;
    padding-left: 8px;
  }
}
.select-tags {
  background-color: rgba(45, 140, 240, 0.1);
  border-radius: 2px;
  color: #2db7f5;
}
.select-tags:after {
  content: "\2714";
}
.select-tags::before {
  content: "\2022";
}
</style>