<template>
  <Collapse simple accordion class="component-list" value="1">
    <Panel name="1">
      属性面板
      <p slot="content">
        <!-- 表单配置 可参考 http://www.form-create.com/designer/ -->
      <Form :label-width="80">
        <Divider orientation="center" size="small">表单配置</Divider>
        <FormItem label="标签位置">
          <RadioGroup v-model="$parent.props.labelPosition" size="small">
            <Radio label="left">左</Radio>
            <Radio label="right">右</Radio>
            <Radio label="top">顶部</Radio>
          </RadioGroup>
          <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="表单域标签的位置，可选值为 left、right、top"></i>
        </FormItem>
        <FormItem label="标签宽度">
          <Input type="number" v-model="$parent.props.labelWidth" style="width:50%;" size="small" /> &nbsp;
          <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="表单域标签的宽度，所有的 FormItem 都会继承 Form 组件的 label-width 的值"></i>
        </FormItem>
        <FormItem label="校验错误信息">
          <i-switch v-model="$parent.props.showMessage" size="default">
            <span slot="open">是</span>
            <span slot="close">否</span>
          </i-switch>
          &nbsp;
          <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="是否显示校验错误信息"></i>
        </FormItem>
        <FormItem label="添加冒号">
          <i-switch v-model="$parent.props.labelColon" size="default">
            <span slot="open">是</span>
            <span slot="close">否</span>
          </i-switch>
          &nbsp;
          <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="是否自动在 label 名称后添加冒号"></i>
        </FormItem>

        <Divider orientation="center">数据绑定</Divider>

        <div style="margin:20px auto;text-align: center;">
          <Button size="small" icon="md-add" @click="isShowApiSelector = true">添加 API</Button>&nbsp;
          <Checkbox v-model="form_databinding.isCreateOrUpdate">创建合并修改</Checkbox>
        </div>

        <FormItem label="读 Read">
          <Select class="select" v-model="crudApi.read" @on-change="getInfoApi">
            <Option v-for="(item, index) in apiList" :key="index" :value="index">{{item.method}} {{item.url}}</Option>
          </Select><a href="javascript:void(0);" :disabled="crudApi.read === -1" @click="read=true">请求</a>
        </FormItem>

        <FormItem v-if="form_databinding.isCreateOrUpdate" label="Create/Update">
          <Select class="select" v-model="crudApi.delete">
            <Option v-for="(item, index) in apiList" :key="index" :value="index">{{item.method}} {{item.url}}</Option>
          </Select>

          <a href="javascript:void(0);" :disabled="crudApi.read === -1">创</a><span style="color:#ccc">|</span><a href="javascript:void(0);" :disabled="crudApi.read === -1">改</a>
        </FormItem>

        <FormItem v-if="!form_databinding.isCreateOrUpdate" label="创建 Create">
          <Select class="select" v-model="crudApi.create">
            <Option v-for="(item, index) in apiList" :key="index" :value="index">{{item.method}} {{item.url}}</Option>
          </Select><a href="javascript:void(0);" :disabled="crudApi.create === -1" @click="del">请求</a>
        </FormItem>

        <FormItem v-if="!form_databinding.isCreateOrUpdate" label="修改 Update">
          <Select class="select" v-model="crudApi.update">
            <Option v-for="(item, index) in apiList" :key="index" :value="index">{{item.method}} {{item.url}}</Option>
          </Select><a href="javascript:void(0);" :disabled="crudApi.update === -1" @click="del">请求</a>
        </FormItem>

        <FormItem label="删除 Delete">
          <Select class="select" v-model="crudApi.delete">
            <Option v-for="(item, index) in apiList" :key="index" :value="index">{{item.method}} {{item.url}}</Option>
          </Select><a href="javascript:void(0);" :disabled="crudApi.delete === -1" @click="del">请求</a>
        </FormItem>
      </Form>

      <Modal v-model="read" title="GET 单笔记录请求" ok-text="发送请求" @on-ok="doRead">
        <br />
        <div style="margin-bottom:20px;">1、请补充实体之 id 参数，它是 URL 一部分，例如 <code>/1001</code> 或者 <code>?id=1001</code>。</div>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{infoUrl}} <Input type="text" size="small" style="width:200px" placeholder="请填写 id" /> <br /><br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="" target="_blank">浏览器打开此接口</a>
        <br />
        <br />
        <div style="margin-bottom:20px;">2、请填写实体于 JSON 结果所在的字段。若不指定则默认最外层就是。 </div>
        <div style="margin-left:20px;"><code>function callback(json) {</code></div>
        <div style="margin-left:40px;"><code>json.</code>&nbsp;<Input type="text" size="small" style="width:100px" placeholder="json 字段" /></div>
        <div style="margin-left:20px;"><code>}</code></div>
        <br />
      </Modal>

      <!-- API 选择器 -->
      <Modal v-model="isShowApiSelector" title="API 选择器" cannel-text="" width="480">
        <ApiSelector :api-root="getApi()" @apiselected="rendererApiList"></ApiSelector>
      </Modal>
      </p>
    </Panel>
    <Panel name="2">
      P2
      <p slot="content">
        P2
      </p>
    </Panel>
  </Collapse>
</template>

<script lang="ts" src="./form-config.ts"></script>