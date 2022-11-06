<template>
  <div class="table" :class="mode">
    <header>
      <ul>
        <li class="check"><input type="checkbox" /></li>
        <li class="name">参数变量名</li>
        <li class="type">类型</li>
        <li class="value">参数值</li>
        <li class="default">值示例</li>
        <li class="required">必填</li>
        <li class="comment">说明</li>
        <li class="action">操作</li>
      </ul>
    </header>

    <ul class="tableBody">
      <li>
        <div class="row check">
          <input type="checkbox" />
        </div>
        <div class="row name">
          <Input value="dfgg" size="small" />
        </div>
        <div class="row type">
          <Select size="small" transfer>
            <Option value="string">string</Option>
            <Option value="number">number</Option>
            <Option value="boolean">boolean</Option>
            <Option value="object">object</Option>
            <Option value="array">array</Option>
            <Option value="null">null</Option>
          </Select>
        </div>
        <div class="row value">
          <Input value="dfgg" size="small" />
        </div>

        <div class="row comment">
          <Input value="dfgg" size="small" />
        </div>
        <div class="row action"></div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import "./api-helper";

export default {
  props: {
    mode: String,
  },
  data() {
    return {
      //   mode: "SCHEME",
    } as API_HELPER_TABLE;
  },
};
</script>

<style lang="less" scoped>
.table {
  width: 100%;

  .check {
    width: 50px;
  }
  .name {
    width: 200px;
  }
  .type {
    width: 100px;
  }
  .value {
    width: 200px;
  }
  .comment {
    width: 300px;
  }
  .action {
    width: 100px;
  }

  .row {
    display: inline-block;
    padding: 0 4px;
    box-sizing: border-box;
  }
}

.SCHEME {
  .default,
  .required {
    display: none;
  }
}

header {
  background-color: #f8f8f9;
  li {
    // text-align: center;
    padding: 6px 14px;
    display: inline-block;
    font-weight: bold;
    box-sizing: border-box;
  }
}

.tableBody {
  li {
    padding: 10px;
    box-sizing: border-box;
  }
}
</style>