<template>
  <ConfigTable class="form-factory" :fields="cfg.fields" is-enable-live-perview>
    <template slot="topbar">
      <a @click="jsonBased.isShowJsonBased = true">从 JSON 新建</a> | <a @click="view = 'model'" :class="{selected : view == 'model'}">模型视图</a> | <a @click="view = 'form'" :class="{selected : view == 'form'}">表单视图</a> |
    </template>

    <template slot="table-header">
      <div class="input-width">说明</div>
      <div class="input-width" :class="{hide: view != 'model'}">数据类型</div>
      <div class="number-width">数据长度</div>
      <div class="checkbox-width">必填</div>
      <div class="number-width" :class="{hide: view != 'model'}">跨表字段？</div>
      <div class="number-width" :class="{hide: view != 'model'}">是否主键</div>
      <div class="input-width" :class="{hide: view != 'model'}">默认值</div>
      <div class="input-width" :class="{hide: view != 'form'}">渲染器</div>
      <div class="input-width-small" :class="{hide: view != 'form'}">布局</div>
      <div class="input-width-small" :class="{hide: view != 'form'}">JSON 类型</div>
      <div class="input-width" :class="{hide: view != 'form'}">正则验证</div>
      <div class="input-width" :class="{hide: view != 'form'}">验证提示</div>
    </template>

    <template slot="table-fields" slot-scope="scope">
      <div class="input-width">
        <Input type="text" size="small" v-model="scope.item.name" placeholder="名称为必填项" />
      </div>
      <div class="input-width"><Input type="text" size="small" v-model="scope.item.label" /></div>
      <div class="input-width"><Input type="text" size="small" v-model="scope.item.comment" /></div>

      <div class="input-width" :class="{hide: view != 'model'}" style="text-align: left;">
        <Select size="small" title="表单控件类型" v-model="scope.item.uiType" transfer>
          <OptionGroup label="字符类型">
            <Option :value="1">VARCHAR</Option>
            <Option :value="18">TEXT</Option>
            <Option :value="11">LONG TEXT</Option>
          </OptionGroup>
          <OptionGroup label="整形数">
            <Option :value="16">TINYINT</Option>
            <Option :value="12">INT</Option>
            <Option :value="15">BIGINT</Option>
          </OptionGroup>
          <OptionGroup label="浮点数">
            <Option :value="16">FLOAT</Option>
            <Option :value="12">DOUBLE</Option>
            <Option :value="15">DECIMAL</Option>
          </OptionGroup>
          <OptionGroup label="其他">
            <Option :value="16">BOOLEAN</Option>
            <Option :value="16">JSON</Option>
          </OptionGroup>
        </Select>
      </div>

      <div class="number-width">
        <Input size="small" type="number" v-model="scope.item.length" placeholder="最大长度" />
      </div>

      <div class="checkbox-width">
        <Checkbox size="small" v-model="scope.item.isNull" />
      </div>
      <div class="number-width" :class="{hide: view != 'model'}">
        <Checkbox size="small" v-model="scope.item.isCrossTable" />
      </div>
      <div class="number-width" :class="{hide: view != 'model'}">
        <Checkbox size="small" v-model="scope.item.isKey" />
      </div>

      <div class="input-width" :class="{hide: view != 'model'}">
        <Input size="small" type="text" v-model="scope.item.defaultValue" placeholder="选填" />
      </div>

      <div class="input-width" :class="{hide: view != 'form'}" style="text-align: left;">
        <Select size="small" title="表单控件类型" v-model="scope.item.uiType" transfer>
          <OptionGroup label="常用表单组件">
            <Option :value="1">文本输入框 Input</Option>
            <Option :value="18">密码输入框 Password</Option>
            <Option :value="11">数字输入框 Number</Option>
            <Option :value="6">多行文本框 Textarea</Option>
            <Option :value="2">下拉列表 Select</Option>
            <Option :value="3">单选框 Radio</Option>
            <Option :value="4">多选框 Checkbox</Option>
            <Option :value="7">开关 Switch</Option>
            <Option :value="10"> 滑块 Slider</Option>
            <Option :value="5">日期选择器 DatePicker</Option>
            <Option :value="18">时间上传器 TimePicker</Option>
            <Option :value="8">富文本编辑器 HTML Editor</Option>
            <Option :value="17">图片上传器 Img Uploader</Option>
            <Option :value="9">文件上传器 File Uploader</Option>
            <Option :value="20">只读的短日期</Option>
            <Option :value="21">只读的长日期</Option>
          </OptionGroup>
          <OptionGroup label="业务组件">
            <Option :value="16">手机输入框</Option>
            <Option :value="12">电邮输入框</Option>
            <Option :value="15">性别选择</Option>
            <Option :value="13">身份证输入框</Option>
            <Option :value="14">省市区联动</Option>
            <Option :value="19">实体状态</Option>
          </OptionGroup>
        </Select>
      </div>

      <div class="input-width-small" :class="{hide: view != 'form'}">
        <Select size="small" v-model="scope.item.uiLayout" transfer>
          <Option :value="1">单列</Option>
          <Option :value="2">双列</Option>
          <Option :value="3">三列</Option>
        </Select>
      </div>
      
      <div class="input-width-small" :class="{hide: view != 'form'}">
        <Select size="small" v-model="scope.item.jsonType" transfer>
          <Option value="string">文本</Option>
          <Option value="long_string">长文本</Option>
          <Option value="number">数字</Option>
          <Option value="boolean">布尔值</Option>
        </Select>
      </div>

      <div class="input-width" :class="{hide: view != 'form'}">
        <Input size="small" type="text" v-model="scope.item.regexp" placeholder="校验的正则表达式" />
      </div>

      <div class="input-width" :class="{hide: view != 'form'}">
        <Input size="small" type="text" v-model="scope.item.validMsg" placeholder="验证错误时的信息" />
      </div>
    </template>

    <FormPerviewLoader ref="FormPerviewLoader" />

    <template slot="live-perview">
      <FromRenderer ref="FromRenderer" :cfg="cfg" :fields="cfg.fields" />
    </template>

    <template slot="config-panel">
      <ConfigPanel :cfg="cfg" :api-root="apiRoot" />
    </template>

    <template slot="more-attrib" slot-scope="scope">
      <MoreAttrib v-if="scope.row && scope.row.ext_attribs " :row="scope.row" />
    </template>
    
    <Modal v-model="jsonBased.isShowJsonBased" title="根据 JSON 定义创建表单" width="650" @on-ok="parseJsonBased">
      <p style="margin-top:0;">先输入接口地址，按“下载”获取 JSON</p>
      <Input type="text" v-model="cfg.dataBinding.url" style="width:520px" size="small" /> <Button size="small" @click="downloadJson">下载</Button>
      <p>或者直接粘贴 JSON 也可以</p>
      <Input type="textarea" v-model="jsonBased.jsonStr" :rows="15" style="width:86%;" />
      <p>JSON 为多层结构，须指定某个对象，这里指定一个字段</p>
      <!-- <Input type="text" v-model="cfg.jsonBased.key" style="width:86%;" size="small" placeholder="JSON 里面的某个 key，如 foo.bar.xyz" /> -->
    </Modal>
  </ConfigTable>

</template>

<script lang="ts" src="./form-factory.ts"></script>

<style lang="less">
.form-factory {
  margin: 10px auto;
  width: 98%;
  .center input {
    text-align: center;
  }

  fieldset.panel {
    margin: 20px 0;
    border: 1px solid lightgray;
    border-radius: 5px;
    padding: 10px 20px;
    legend {
      margin-left: 8px;
      letter-spacing: 5px;
    }
  }

  .hide {
    display: none;
  }
}

.uiType {
  width: 180px;
}

.uiLayout {
  width: 100px;
}
</style>

<style lang="less" scoped>
a.selected {
  font-weight: bold;
}

p {
  margin: 15px 0;
}
</style>
