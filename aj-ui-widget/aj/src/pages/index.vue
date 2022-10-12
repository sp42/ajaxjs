<template>
  <div class="demo">
    <menu>
      <a href="#" @click="show = 'intro'">简介</a> |
      <!-- <a href="#" @click="show = 'baseFn'">基础函数</a> | -->
      <a href="#" @click="show = 'widget'">页面 widget</a> |
      <a href="#" @click="show = 'form'">表单组件</a> |
      <a href="#" @click="show = 'html-editor'">HTML 编辑器</a> |
      <a href="#" @click="show = 'calendar'">日历选择器</a> |
      <a href="#" @click="show = 'play-ground'">试验场</a>
    </menu>
    <table class="all-center" v-if="show == 'intro'">
      <tr>
        <td>
          <h1>ajaxjs.js 前端组件库</h1>
          <ul>
            <li>
              这是一个个人练手项目，组件不是很全，只有顶部导航列出那些。要求大而全的请参考
              element/ant/iview
            </li>
            <li>
              轻量级，原先打算做一个纯原生的 UI 库，啥都不依赖，后面入坑 vue
              才只依赖 vue 2
            </li>
            <li>
              简单易懂的代码，不折腾。当然了，是 typescript。不过我没有用 class
              风格，只是加入参数类型、返回值类型，这就足够了
            </li>
            <li>CSS 基于 less.js</li>
          </ul>
          <p class="p">安装 npm i @ajaxjs/ui</p>

          <p>
            <a
              href="https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-ui-widget/aj"
              >源码</a
            >
            |
            <a
              target="_blank"
              href="//shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22"
              >QQ 群：3150067</a
            >
            | <a href="mailto:frank@ajaxjs.com">Email</a> |
            <a target="_blank" href="http://blog.csdn.net/zhangxin09"
              >作者博客</a
            >
            <center>©1999-2022 Frank Cheung</center>
          </p>
        </td>
      </tr>
    </table>

    <DemoWidget v-if="show == 'widget'" />
    <Form v-if="show == 'form'" />
    <HtmlEditor v-if="show == 'html-editor'" />
    <Calendar v-if="show == 'calendar'" />
    <PlayGround v-if="show == 'play-ground'" />
  </div>
</template>

<script>
import DemoWidget from "./widgets.vue";
import Form from "./form.vue";
import HtmlEditor from "./html-editor.vue";
import Calendar from "./calendar.vue";
import PlayGround from "./play-ground.vue";

export default {
  components: { DemoWidget, Form, HtmlEditor, Calendar, PlayGround },
  data() {
    return {
      show: "intro",
    };
  },
};
</script>

<style lang="less">
@import "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css";

a {
  color: gray;
}
a:hover {
  color: black;
}
.demo {
  font-size: 11pt;
  menu {
    margin: 0;
    padding: 0;
    line-height: 40px;
    height: 40px;
    // width: 300px;
    text-align: center;
    border-bottom: 1px solid lightgray;
  }
}

table.all-center {
  width: 100%;
  font-size: 11pt;
  td {
    vertical-align: middle;
    text-align: center;
  }
}

h1 {
  margin: 20px;
}

.home {
  & > h2 {
    margin: 30px auto;
  }
  & > h2,
  & > p {
    margin: 20px auto;
    width: 600px;
    text-align: justify;
  }
}
</style>

<style lang="less" scoped>
h1 {
  margin: 30px;
}

ul {
  max-width: 500px;
  margin: 20px auto;
  color: gray;
  li {
    text-align: left;
    list-style-type: initial;
    padding-bottom: 10px;
  }
}

p.p {
  .aj-text-function();
  max-width: 500px;
  margin: 20px auto;
}

center {
  margin: 20px;
  font-size: 9pt;
  color: lightgray;
}
</style>