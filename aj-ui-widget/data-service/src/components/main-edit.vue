<template>
  <div class="main-edit-panel">
    <ul class="command-list">
      <li
        v-for="(item, k, index) in currentData.data"
        :key="index"
        v-if="k != 'others'"
        v-html="getDml(item, k)"
        :class="{ selected: dmlSelected == index }"
        @click="selectItem(item, index, k)"
      ></li>

      <li
        v-for="(item, k, index) in currentData.others"
        :key="index"
        v-html="getDml(item, k)"
        :class="{ selected: dmlSelected == index }"
        @click="selectItem(item, index, k)"
      ></li>
      <!--                           <li>
                                        GET /foo
                                    </li>
                                    <li>
                                        POST /foo
                                    </li>
                                    <li>
                                        GET /foo
                                    </li>
                                    <li>
                                        POST /foo
                                    </li>
                                    <li>
                                        GET /foo
      </li>-->
      <li>+ 新建命令</li>
    </ul>
    <div class="code-panel">
      <!-- SQL 源码编辑器-->
      <codemirror class="code-editor" ref="cm" v-model="code" :options="cmOption"></codemirror>
      <!-- {{currentType}}----
      {{currentDML}}----------->

      <div>
        <span style="float: right">
          <label>
            <input type="checkbox" v-model="currentDML.enable" /> 启用
          </label>
        </span>
        命令名称
        <i-input type="text" placeholder="自定义命令必填" style="width: 200px" size="small"></i-input>
        <br />
        <br />

        <span v-if="currentType == 'create'">
          <label>
            <input type="checkbox" :value="true" /> 生成唯一 uid
            <tips text="唯一 id，通过 uuid 生成不重复 id"></tips>
          </label>
          &nbsp;&nbsp;&nbsp;
          <label>
            <input type="checkbox" :value="true" /> 插入创建/修改日期
          </label>
        </span>

        <div style="float: right">
          <span v-if="currentType == 'create'">
            <a>生成创建语句</a> |
          </span>
          <span v-if="currentType == 'delete'">
            <a>生成删除语句</a> |
            <!-- <a>生成“物理删除”语句</a> |
            <a>生成“逻辑删除”语句</a>-->
            <!--  <tips text="在逻辑上数据是被删除的，但数据本身依然存在库中（仅仅是更新状态字段为已删除）"></tips> | -->
          </span>

          <a href="javascript:alert('暂未实现')">格式化</a> |
          <a @click="copySql">复制</a>
        </div>命令类型
        <Radio-Group type="button">
          <Radio label="单个查询" title="查询单笔记录，通常返回一个记录的详细信息（info）。"></Radio>
          <Radio label="列表查询" title="返回列表记录（list）"></Radio>
          <Radio label="创建记录" title="执行 SQL INSERT 操作新增一笔记录（CREATE）。"></Radio>
          <Radio label="修改记录" title="指定记录 id，修改记录字段（UPDATE）。"></Radio>
          <Radio label="删除记录" title="删除单笔记录，若成功返回 true。"></Radio>
        </Radio-Group>

        <div style="margin: 20px 0">
          <!--         &nbsp;&nbsp;&nbsp;
                                            <label>
                                                <input type="checkbox" :value="true" /> 当有 id 参数时采用“修改”的规则
                                                <tips text="CreateOrSave，同一个接口处理"></tips>
          </label>-->
          <br />
          <br />
          <Divider style="color: gray" size="small">API 接口</Divider>

          <!-- <api></api> -->
        </div>
      </div>
    </div>
  </div>
</template>