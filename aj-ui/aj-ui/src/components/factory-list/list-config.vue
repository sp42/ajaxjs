<template>
  <!-- 详细配置 -->
  <Tabs style="min-height:600px" :animated="false">
    <TabPane label="列表配置">
      <Form :label-width="120" label-colon>
<!--         <FormItem label="记录 id">{{$parent.id}}</FormItem>
        <Row>
          <Col span="12">
          <FormItem label="数据源">
            {{parent.datasourceName}}#{{parent.datasourceId}}
          </FormItem>
          </Col>
          <Col span="12">
          <FormItem label="绑定表名">
            {{parent.tableName}}
          </FormItem>
          </Col>
        </Row> -->

        <FormItem label="绑定的表单">{{getFormConfig()}} <Button size="small" @click="$refs.SelectForm.isShowListModal = true">选择表单</Button></FormItem>

        <FormItem label="分页">
          <Checkbox v-model="listCfg.isPage">是否分页
            <i class="ivu-icon ivu-icon-ios-help-circle-outline" title="不分页则一次性查询所有数据，适合数据量较少的列表"></i>
          </Checkbox>
          分页参数：
          <RadioGroup v-model="listCfg.page">
            <Radio :label="1" :disabled="!listCfg.isPage">start/limit</Radio>
            <Radio :label="2" :disabled="!listCfg.isPage">pageNo/pageSize</Radio>
          </RadioGroup>
        </FormItem>

        <EventGroup :cfg="listCfg" type="toolbarButtons" name="工具条按钮" style="margin:0 auto;width:65%" />
        <br />
        <EventGroup :cfg="listCfg" type="actionButtons" name="操作按钮" style="margin:0 auto;width:65%" />

        <!-- 选择哪张表单绑定 -->
        <ListSelector ref="SelectForm" title="表单配置" :API="apiRoot + '/common_api/widget_config/list?q_type=LIST'" @on-select="onFormSelected($event)" :columns="formSelectorCols" />

        <FormPerviewLoader ref="FormPerviewLoader" />
      </Form>
    </TabPane>

    <TabPane label="数据绑定">
      <Form :label-width="120" label-colon>
        <Divider size="small">获取列表数据接口</Divider>
        <ApiBinding :cfg="listCfg.dataBinding" />

        <Divider size="small">删除实体接口</Divider>
        <ApiBinding :cfg="listCfg.deleteDataBinding" />
      </Form>
    </TabPane>
  </Tabs>

</template>

<script lang="ts" src="./list-config.ts"></script>