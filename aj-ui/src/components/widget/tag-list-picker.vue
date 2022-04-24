<template>
  <Select v-model="tagsTotal" filterable multiple allow-create placeholder="请选择分类标签" style="width:100%" @on-create="addTag">
    <Option v-for="item in list" :value="item.tagIndex" :key="item.tagIndex">{{ item.name }}</Option>
  </Select>
</template>

<script>
import { xhr_get } from '../../util/xhr';
import { isDebug } from '../../util/utils';

export default {
  data() {
    return {
      tagsTotal: 0,
      Idx: 0,
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
            return item;
          });
        else j.msg && this.$Message.warning(j.msg);
      },
      { start: 0, limit: 99 }
    );
  },
  methods: {
    addTag(val) {
      //   if (this.Idx === 0 && this.list[0].tagIndex === '') {
      //     this.list.splice(0, 1);
      //     this.Idx++;
      //   }

      this.list.push({ name: val });
    }
  }
};
</script>