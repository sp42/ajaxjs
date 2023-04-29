<template>
  <Form :label-width="120" style="padding:0 10% 0 0">
    <FormItem label="自定义渲染" v-if="row.render == 'render'">

      <!--  <Radio v-model="row.isCode" value="false">自定义键值对</Radio> -->
      <label>
        <input type="radio" v-model="row.isCode" :value="false" /> 自定义键值对
      </label>

      <!-- <div :class="{'box':true, 'disabled': row2.isCode}"> row 问题：第一次有效，之后无效了 -->
      <div :class="{box:true}">
        <ul>
          <li>
            <div class="id"><b>值</b></div>
            <div class="name"><b>外显名称</b></div>
            <div class="isChecked"><b>删除</b></div>
          </li>
          <li v-for="(item, index) in row.customRenderKV" :key="index">
            <div class="id">
              <input v-model="item.value" />
            </div>
            <div class="name"><input v-model="item.name" /></div>
            <div class="isChecked">
              <!--  <div>
              <input type="checkbox" v-model="item.checked" />
            </div> -->
              <div><a title="删除数据" @click="del(index)">⨯</a></div>
            </div>
          </li>
        </ul>
        <div class="plus" title="添加数据" @click="add">✚</div>
      </div>

      <!-- <Radio v-model="row.isCode" value="true">自定义渲染函数</Radio> -->
      <label>
        <input type="radio" v-model="row.isCode" :value="true" /> 自定义渲染函数
      </label>

      <Input type="textarea" v-model="row.customRender" :rows="5" :disabled="!row.isCode" />
    </FormItem>

    <FormItem label="自定义点击事件" v-if="row.render == 'clk_event'">
      <Input type="textarea" v-model="row.clkEvent" :rows="5" />
    </FormItem>
  </Form>
</template>

<script lang="ts">
// import input from "../form-designer/meta/panel/input.vue";
export default {
  // components: { input },
  props: {
    row: {
      type: Object,
      default() {
        return {};
      },
    },
  },
  data() {
    return { row2: this.row };
  },
  methods: {
    isCode(): boolean {
      return this.row.isCode;
    },

    add(): void {
      if (!this.row.customRenderKV) this.row.customRenderKV = [];

      this.row.customRenderKV.push({ value: "", name: "" });

      this.$forceUpdate();
      // 输入框获取焦点
      setTimeout(() => {
        let arr: any[] = this.$el.querySelectorAll(".id input");
        arr[arr.length - 1].focus();
      }, 500);
    },

    /**
     * 删除
     *
     * @param index
     */
    del(index: number): void {
      this.row.customRenderKV.splice(index, 1);
    },
  },
};
</script>

<style lang="less" scoped>
.more-attrib.input {
  width: 260px;
}
.more-attrib.input.small {
  width: 100px;
}
</style>

<style lang="less" scoped>
ul {
  width: 350px;
  max-height: 400px;
  // margin: 30px 0;
  border: 1px solid lightgray;
  overflow-x: auto;
}
li {
  list-style: none;
  &:hover {
    color: black;
    .isChecked {
      a {
        display: inline;
      }
    }
  }

  &:nth-child(odd) {
    background-color: #f0f0f0;
  }

  & > div {
    display: inline-block;
    width: 33%;
    text-align: center;

    div {
      width: 20px;
      display: inline-block;
    }

    input {
      border: none;
      text-align: center;
      width: 90%;
      background-color: transparent;
      outline: none;
    }
  }
  .isChecked {
    a {
      // display: none;
      color: red;
    }
  }
}

.box {
  position: relative;
  .plus {
    color: green;
    position: absolute;
    right: 0;
    bottom: 0;
    cursor: pointer;
  }
  &.disabled {
    filter: grayscale(1) opacity(0.4);
  }
}
</style>