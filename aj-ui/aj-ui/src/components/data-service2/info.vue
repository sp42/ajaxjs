<template>
  <div style="height:100%">
    <ul class="line-layout">
      <!--       <li>
        <i-Select v-model="datasource.id" placeholder="请选择数据源">
          <Option v-for="item in datasource.list" :key="item.id" :value="item.id">{{
                                            item.name }}</Option>
        </i-Select>
      </li> -->
      <li class="top_namespace" v-if="data.id.indexOf('/') != -1 ">
        {{parentDir()}}/
      </li>

      <li>
        <i-input placeholder="相当于接口的 URL 目录，必填的" v-model="currentData.namespace" size="small">
          <span slot="prepend">命名空间</span>
        </i-input>
        {{type}}
      </li>
      <li>
        <i-input placeholder="接口的说明" v-model="currentData.name" size="small">
          <span slot="prepend">说明</span>
        </i-input>
      </li>
      <!-- <li style="padding-top: 5px;">数据源： {{currentData.datasourceName}}</li> -->
      <li v-if="!isSignle">
        <i-input placeholder="数据库表名" v-model="currentData.tableName" size="small">
          <span slot="prepend">数据库表名</span>
        </i-input>
      </li>
    </ul>

    <fieldset>
      <legend>更多设置
        <Icon type="ios-arrow-down" style="cursor: pointer;" @click="togglePanel" />
      </legend>
    </fieldset>

    <div class="config" style="height:0px">
      <Row type="flex" justify="center" align="middle">
        <i-Col span="10" style="padding-right:20px;">
          <i-input placeholder="Java Bean 的类全称" v-model="currentData.clzName">
            <span slot="prepend">Bean 类型</span>
          </i-input>
          <br />
          <i-input placeholder="逗号隔开多个标签" v-model="currentData.tags">
            <span slot="prepend">标签</span>
          </i-input>
        </i-Col>
        <i-Col span="14">
          <i-input v-model="currentData.content" maxlength="200" show-word-limit type="textarea" :rows="4" placeholder="描述" />
        </i-Col>
      </Row>
      <br />
      <Divider style="color:gray" size="small">通用字段/参数映射 Mapping</Divider>
      <Row type="flex" justify="center" align="middle" :gutter="16">
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 id" v-model="currentData.idField">
            <span slot="prepend">唯一主键字段</span>
          </i-input>
        </i-Col>
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 createDate" v-model="currentData.createDate">
            <span slot="prepend">创建日期字段</span>
          </i-input>
        </i-Col>
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 updateDate" v-model="currentData.updateDate">
            <span slot="prepend">修改日期字段</span>
          </i-input>
        </i-Col>
      </Row>
      <br />
      <Row type="flex" justify="center" align="middle" :gutter="16">
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 id" v-model="currentData.idField">
            <span slot="prepend">创建人字段</span>
          </i-input>
        </i-Col>
        <i-Col span="8">
          <i-input placeholder="不填写则默认为 createDate" v-model="currentData.createDate">
            <span slot="prepend">修改人字段</span>
          </i-input>
        </i-Col>
        <i-Col span="8">
        </i-Col>
      </Row>
      <br />
      <i-Form>
        <div style="margin-top: 15px;text-align: right;color:lightgray;">
          id#{{currentData.id}} 创建人： admin 创建日期：{{currentData.createDate}} 修改人： admin 修改日期 {{currentData.updateDate}}
        </div>
      </i-Form>
    </div>

    <div class="main-edit-panel">
      <ul class="command-list" v-if="'SINGLE' != data.data.type">
        <li @click="setEditorData('infoSql')" :class="{selected: editorData.type == 'infoSql'}">
          <span style="color: green;">GET</span> Info
          <tips text="查询单笔记录，通常返回一个记录的详细信息（info）。" />
        </li>
        <li @click="setEditorData('listSql')" :class="{selected: editorData.type == 'listSql'}">
          <span style="color: green;">GET</span> List
          <tips text="返回列表记录（list），也可作自动分页。" />
        </li>
        <li @click="setEditorData('createSql')" :class="{selected: editorData.type == 'createSql'}">
          <span style="color: burlywood;">POST</span> Create
          <tips text="执行 SQL INSERT 操作新增一笔记录（CREATE）。" />
        </li>
        <li @click="setEditorData('updateSql')" :class="{selected: editorData.type == 'updateSql'}">
          <span style="color: blueviolet;">PUT</span> Update
          <tips text="指定记录 id，修改记录字段（UPDATE）。" />
        </li>
        <li @click="setEditorData('deleteSql')" :class="{selected: editorData.type == 'deleteSql'}">
          <span style="color: red;">DELETE</span> Delete
          <tips text="删除单笔记录，若成功返回 true。" />
        </li>
      </ul>

      <div :class="{'code-panel': true, 'all-width': 'SINGLE' === data.data.type}">
        <!-- SQL 源码编辑器-->
        <codemirror class="code-editor" ref="cm" v-model="editorData.sql" :options="cmOption" v-show="editorData.isCustomSql"></codemirror>
        <!--  {{editorData.sql}} -->
        <div>
          <span v-if="!isSignle" style="float: right;"><label><input type="checkbox" /> 启用</label></span>

          <!-- 用不了 iview 的 RadioGroup -->
          <span v-if="'SINGLE' != data.data.type">
            <label><input type="radio" v-model="editorData.isCustomSql" :value="true" /> 自定义 SQL</label>&nbsp;&nbsp;
            <label><input type="radio" v-model="editorData.isCustomSql" :value="false" /> 默认逻辑</label>
          </span>
          <br />
          <br />

          <span v-if="editorData.type == 'createSql' && !editorData.isCustomSql">
            <label><input type="checkbox" :value="true" /> 记录创建日期</label> &nbsp;&nbsp;
            <label><input type="checkbox" :value="true" /> 记录创建人</label> &nbsp;&nbsp;
            <br />
            <br />
            <label> 主键生成策略
              <Select v-model="currentData.idType" style="width:200px">
                <Option :value="3">UUID</Option>
                <Option :value="1">自增</Option>
                <Option :value="2">雪花</Option>
              </Select>
            </label>

            &nbsp;&nbsp;&nbsp;
            <label>
              <input type="checkbox" :value="true" /> 双主键，生成唯一 uid
              <tips text="唯一 id，通过 uuid 生成不重复 id" />
            </label>
            &nbsp;&nbsp;
            <Select v-model="currentData.idType" style="width:200px">
              <Option :value="3">UUID</Option>
              <Option :value="2">雪花</Option>
            </Select>
          </span>

          <span v-if="editorData.type == 'updateSql' && !editorData.isCustomSql">
            <label><input type="checkbox" :value="true" /> 记录修改日期</label> &nbsp;&nbsp;
            <label><input type="checkbox" :value="true" /> 记录修改人</label>
          </span>

          <span v-if="editorData.type == 'deleteSql' && !editorData.isCustomSql">
            <label title="物理删除"><input type="radio" v-model="currentData.hasIsDeleted" :value="false" /> 物理删除</label>
            <label title="在逻辑上数据是被删除的，但数据本身依然存在库中（仅仅是更新状态字段为已删除）"><input type="radio" v-model="currentData.hasIsDeleted" :value="true" /> 逻辑删除</label> &nbsp;

            <span v-if="currentData.hasIsDeleted"> 删除的字段
              <Input type="text" v-model="currentData.delField" style="width:80px" size="small" />
            </span>
          </span>

          <div style="float:right">
            <a @click="formatSql">格式化</a> |
            <a @click="copySql">复制</a>
          </div>

          <br />
          <br />
          <div style="margin:20px 0">
            <Divider style="color:gray" size="small">API 接口</Divider>

            <api :api-prefix="getApiPrefix()" />
            <br />
            <api :api-prefix="getApiPrefix()" :page="true" v-if="editorData.type == 'listSql'" />
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script src="./info.js"></script>

<style lang="less" scoped src="./info.less"></style>