<template>
  <div>
    <IdeLayout title="Workflow Designer">
      <template v-slot:component>
        <Toolbox />
      </template>

      <template v-slot:top-bar>
        <Topbar />
      </template>

      <template v-slot:stage>
        <div v-show="!codeMode" class="canvas"></div>
        <div class="codeEdit" v-show="codeMode">
          <codemirror v-model="jsonCode" :options="cmOption" style="border:1px solid lightgray;" />
        </div>
      </template>

      <template v-slot:property>
        <!-- 属性修改面板 -->
        <div class="bottom">

          <div class="property" v-if="isShowProperty()">
            <h3 class="title propertyEditorTitle">
              <div class="right" style="float:right;">
                <a @click="selectedComponent = null">撤销选择</a>
              </div>
              已选择【{{ selectedComponent.type }}】
            </h3>
            <div class="form">
              <component :is="currentForm" :cop="selectedComponent"></component>
              <div style="padding: 2%; text-align: center">
                <a href="#">删除</a> <a href="#">复制</a>
              </div>
            </div>
            <div class="bottom"></div>
          </div>

        </div>
      </template>
    </IdeLayout>
  </div>
</template>

<script lang="ts" src="./index.ts"></script>

<style lang="less" src="./index.less"></style>