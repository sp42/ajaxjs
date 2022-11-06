<template>
  <!-- 表单设计器 -->
  <div class="form-designer">
      <div class="property-panel" >
      <div class="tab">
        <div>
          <FormConfigCmp class="actived" :form_cfg="form_cfg" :form_databinding="form_databinding"></FormConfigCmp>
          <div>
            <Form :label-width="80">
              <Divider orientation="center">属性配置</Divider>
              <FormItem label="已选定：">{{getTypeName()}}</FormItem>
              <div v-if="widgetCfg.type && widgetCfg.type.indexOf('input') != -1">
                <FormItem label="标签名称">
                  <Input type="text" class="input" v-model="widgetCfg.label" />
                </FormItem>
                <FormItem label="输入类型">
                  <Select v-model="widgetCfg.type">
                    <Option value="input_text">文本 text</Option>
                    <Option value="input_password">密码 password</Option>
                    <Option value="input_textarea">多行文本 textarea</Option>
                    <Option>网址 url</Option>
                    <Option>邮件 mail</Option>
                    <Option>数字 number</Option>
                    <Option>日期date</Option>
                  </Select>
                </FormItem>

                <!--   <Divider orientation="center">专项属性</Divider> -->
                <FormItem label="标签名称" v-if="widgetCfg.type === 'input_password'">
                  <Input type="text" class="input" v-model="widgetCfg.label" />
                </FormItem>

                <Divider orientation="center">数据绑定</Divider>

                <FormItem label="标签名称">
                  <AutoComplete v-model="value3" :data="data3" :filter-method="filterMethod" placeholder="选择绑定的字段名" style="width:80%;margin-right:10px;"></AutoComplete>

                  <!-- 辅助提示文字 -->
                  <Tooltip content="content of tooltip" placement="top">
                    <Icon type="ios-help-circle-outline" />
                  </Tooltip>
                </FormItem>
                <Divider orientation="center">表单验证</Divider>
              </div>
            </Form>
          </div>

        </div>
        <header @click="onTabClk">
          <div class="actived">表单配置</div>
          <div>属性配置</div>
        </header>
      </div>
    </div>

 
  </div>
</template>