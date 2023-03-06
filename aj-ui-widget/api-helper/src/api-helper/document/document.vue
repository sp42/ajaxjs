<template>
  <!-- 渲染文档内容 -->
  <div>
    <Button @click="value3 = true" type="primary">编辑文档</Button>
    <Drawer title="编辑文档" v-model="value3" width="1090" :mask-closable="false" :styles="styles">
      <Form :model="documnetObj">
        <Row :gutter="32">
          <Col span="16">
          <FormItem label="接口名称" label-position="top">
            <Input v-model="documnetObj.name" placeholder="请输入接口名称" />
          </FormItem>
          </Col>
          <Col span="8">
          <FormItem label="分类" label-position="top">
            <Select v-model="documnetObj.catalogId" placeholder="接口分类">
              <Option value="private">Private</Option>
              <Option value="public">Public</Option>
            </Select>
          </FormItem>
          </Col>
        </Row>

        <FormItem label="接口说明" label-position="top">
          <Input type="textarea" v-model="documnetObj.description" :rows="4" placeholder="接口说明" />
        </FormItem>

        <Row :gutter="32">
          <Col span="15">
          <FormItem label="对应 UI" label-position="top">
            <!-- <FileUploader :is-img-upload="true" :is-show-btn="false" ref="imgUploader" /> -->
          </FormItem>

          </Col>
          <Col span="6">

          <FormItem label="接口编码状态" label-position="top">
            <Select v-model="documnetObj.codeState" placeholder="请选择接口状态">
              <Option value="INITED">初始化</Option>
              <Option value="DEFINED">已定义</Option>
              <Option value="DONE">已完成</Option>
            </Select>
          </FormItem>

          <FormItem label="是否在文档汇总中展示" label-position="right">
            <i-Switch v-model="documnetObj.isShow" />
          </FormItem>
          </Col>
        </Row>
      </Form>
      <div class="demo-drawer-footer">
        <Button type="success" style="margin-right: 8px" @click="perview = true" icon="ios-eye">预览文档</Button>
        <Button type="primary" style="margin-right: 8px" @click="save" icon="md-create">保存</Button>
        <Button @click="value3 = false">关闭</Button>
      </div>
    </Drawer>

    <Modal v-model="perview" title="预览文档" width="1200" ok-text="关闭" cancel-text="">
      <div style="max-height: 600px;overflow-y:scroll;">
        <Render />
      </div>
    </Modal>
  </div>
</template>

<script lang="ts" src="./document.ts"></script>

<style lang="less" scoped>
.demo-drawer-footer {
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;
}
</style>
