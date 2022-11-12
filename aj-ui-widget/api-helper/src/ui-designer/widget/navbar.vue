<template>
  <!-- 导航条 -->
  <nav>
    <Icon class="right" type="md-search" @click="isShowSearch=true" />
    Root
    <span v-html="activeCmpLinks.length ? activeCmpLinks: ''"></span>
    <!--     <span v-for="(item, index) in activeCmpLinks" :key="index">
      <span v-if="item" style="color:lightgray;">&gt;</span>
      <a v-if="item" href="javascript:void(0);">{{item.type}}#{{item.uid}}</a>
    </span> -->

    <Modal v-model="isShowSearch" width="680" ok-text="" cancel-text="" footer-hide>
      <br />
      <Input style="width:550px" search placeholder="输入搜索条件，按回车搜索" /> &nbsp;&nbsp;&nbsp;
      <Checkbox>正则</Checkbox>
    </Modal>
  </nav>
</template>

<script >
import fns from '../common-function';

export default {
  data() {
    let arr = [null, null];
    for (let i = 0; i < 99; i++)
      arr[i] = null;

    return {
      activeCmpLinks: [],
      isShowSearch: false,
    };
  },
  mounted() {
    // react array so WIRED!!!!
    let m = this.$Message;
    window.selectWidget = function (uid) {
      let el = document.querySelector(`[data-uid='${uid}']`);
      // console.log(el)
      if (!el)
        m.info('代码未设置 uid，找不到元素');
      else
        el.click();
    }
  },
  methods: {
    render(cmp) {
      let links = [];
      fns.find(this.$parent.STAGE_RENDER.RenderedMeta, cmp, (parentNode, arr, i, stack) => links = stack);
      links.push(cmp);

      // links.forEach((link, i) => {
      //   console.log(i)
      //   this.$set(this.activeCmpLinks, i, link)
      // });

      // for (let i = 0; i < 99; i++) {
      //   let link = links[i];
      //   if (link) {
      //     console.log(i, link)
      //     this.$set(this.activeCmpLinks, i, link);
      //   }
      //   else
      //     this.$set(this.activeCmpLinks, i, {});
      // }

      // let foo = [{ "type": "form", "uid": 122 }]

      // this.activeCmpLinks.concat(foo)
      // this.$set( this.activeCmpLinks,foo)
      // this.activeCmpLinks = foo;

      // console.log([{ type: 'inpu_text', uid: 22 }, { type: 'inpu_text', uid: 22 }], JSON.parse(JSON.stringify(links)))


      let activeCmpLinks = '';
      links.forEach(item => activeCmpLinks += `<span style="color:lightgray;">&gt;</span> <a href="javascript:selectWidget(${item.uid});">${item.type}#${item.uid}</a> `);
      this.activeCmpLinks = activeCmpLinks;
    }
  }
}

</script>
<style lang="less" scoped>
nav {
  width: 100%;
  height: 24px;
  line-height: 28px;
  background-color: white;
  margin-bottom: 20px;
  text-align: left;
  font-size: 10px;
  padding: 0 15px;
  color: gray;
  a {
    color: gray;
    &:hover {
      color: black;
    }
  }
  .right {
    float: right;
    margin-top: 8px;
    cursor: pointer;
  }
}
</style>