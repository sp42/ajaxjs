<template>
  <!-- 右边 tab -->
  <div class="right-tab tab">
    <ul @click="onTabClk">
      <li class="active">
        <Icon size="23" type="ios-list-box-outline" />
        <br />
        属性
      </li>
      <li>
        <Icon size="23" type="ios-cog-outline" />
        <br />
        数据
      </li>
    </ul>
    <div class="content">
      <section class="show">
        <h3 style="padding:20px 0;">属性面板</h3>
        <FormPanel v-if="type == 'Form'" />
        <InputPane v-if="type == 'input_text'" />
        <FormItemPane v-if="type == 'FormItem'" />
        <ButtonPane v-if="type == 'Button'" />

        <Form v-if="type=='text'" label-position="top">
          <FormItem label="文本">
            <Input type="textarea" v-model="meta.text" class="input-text" size="small" placeholder="请输入 placeholder" />
          </FormItem>
        </Form>
        <span v-if="!type"><br /> 当前未选择任何控件</span>
      </section>
      <section>

      </section>
    </div>
  </div>
</template>

<script lang="ts" src="./tab-right.ts"></script>

<style lang="less" scoped src="./tab.less"></style>
<style lang="less" scoped src="../meta/panel/common.less"></style>

<style lang="less">
.left-tab .content {
  .ivu-tabs-bar {
    margin-bottom: 0;
  }

  .ivu-collapse {
    border: 0;
  }
  .ivu-collapse > .ivu-collapse-item {
    border-top: 0;
  }
}

.component-list {
  .ivu-collapse-header {
    text-align: left;
  }
}
</style>