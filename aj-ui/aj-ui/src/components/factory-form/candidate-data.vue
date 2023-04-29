<template>
  <div>
    <RadioGroup v-model="type">
      <Radio :label="1">
        从 API 接口获取数据 <Tooltip content="rows 文本域默认行数" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline"></i></Tooltip><br />
        <span>{{api ||'未指定 API'}}</span> &nbsp;<Button size="small" :disabled="type != 1">选择 API</Button>
      </Radio>

      <br />
      <Radio :label="2">
        指定数据 <Tooltip content="相当于静态数据" placement="right"><i class="ivu-icon ivu-icon-ios-help-circle-outline" /></Tooltip><br />
      </Radio>
    </RadioGroup>

    <div :class="{box:true, disabled: type == 1}">
      <ul>
        <li>
          <div class="id"><b>值</b></div>
          <div class="name"><b>外显名称</b></div>
          <div class="isChecked"><b>删除</b></div>
        </li>
        <li v-for="(item, index) in staticData.candidateData" :key="index">
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
  </div>
</template>

<script lang="ts">
/**
 * 候选数据
 */
export default {
  data(): any {
    return {
      type: 2,
      api: "",
      staticData: {
        candidateData: [
          { value: 1, name: "男" },
          { value: 2, name: "女", checked: true },
          { value: 0, name: "未知", checked: false },
        ],
      },
    };
  },
  methods: {
    add(): void {
      /* 这里不能分配数组，那样无法渲染。必须在  row.ext_attribs.candidateData = []; 定义 */
      // if (!this.staticData.candidateData)
      //     this.staticData.candidateData = [];
      this.staticData.candidateData.push({ value: "", name: "" });

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
      this.staticData.candidateData.splice(index, 1);
    },
  },
};
</script>

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