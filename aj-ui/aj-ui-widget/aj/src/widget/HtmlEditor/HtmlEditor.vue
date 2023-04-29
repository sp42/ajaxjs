<template>
  <div class="aj-form-html-editor">
    <ul class="toolbar">
      <li class="dorpdown">
        <i title="字体" class="text-icon">A</i>
        <div class="fontfamilyChoser" @click="onFontfamilyChoserClk">
          <a style="font-family: '宋体'">宋体</a>
          <a style="font-family: '黑体'">黑体</a>
          <a style="font-family: '楷体'">楷体</a>
          <a style="font-family: '隶书'">隶书</a>
          <a style="font-family: '幼圆'">幼圆</a>
          <a style="font-family: 'Microsoft YaHei'">Microsoft YaHei</a>
          <a style="font-family: Arial">Arial</a>
          <a style="font-family: 'Arial Narrow'">Arial Narrow</a>
          <a style="font-family: 'Arial Black'">Arial Black</a>
          <a style="font-family: 'Comic Sans MS'">Comic Sans MS</a>
          <a style="font-family: Courier">Courier</a>
          <a style="font-family: System">System</a>
          <a style="font-family: 'Times New Roman'">Times New Roman</a>
          <a style="font-family: Verdana">Verdana</a>
        </div>
      </li>
      <li class="dorpdown">
        <i title="字号" class="text-icon">H</i>
        <div class="fontsizeChoser" @click="onFontsizeChoserClk">
          <a style="font-size: xx-small; ">极小</a>
          <a style="font-size: x-small;  ">特小</a>
          <a style="font-size: small;    ">小</a>
          <a style="font-size: medium;   ">中</a>
          <a style="font-size: large;    ">大</a>
          <a style="font-size: x-large;  ">特大</a>
          <a style="font-size: xx-large; line-height: 140%">极大</a>
        </div>
      </li>
      <li @click="onCmdClk"><i title="加粗" class="bold text-icon">B</i></li>
      <li @click="onCmdClk"><i title="斜体" class="italic text-icon" style="font-style:italic">I</i></li>
      <li @click="onCmdClk"><i title="下划线" class="underline text-icon" style="text-decoration: underline;">U</i></li>
      <li @click="onCmdClk"><i title="左对齐"   :class="'justifyleft   '+ (isIonicons ? 'ivu-icon ivu-icon-bingo-menu-fold' : 'fontAwesome fa-align-left')"></i></li>
      <li @click="onCmdClk"><i title="中间对齐" :class="'justifycenter   '+ (isIonicons ? 'ivu-icon ivu-icon-md-menu' : 'fontAwesome fa-align-center')"></i></li>
      <li @click="onCmdClk"><i title="右对齐"   :class="'justifyright   '+ (isIonicons ? 'ivu-icon ivu-icon-bingo-menu-unfold' : 'fontAwesome fa-align-right')"></i></li>
      <li @click="onCmdClk"><i title="数字编号" :class="'insertorderedlist   '+ (isIonicons ? 'ivu-icon ivu-icon-md-list' : 'fontAwesome fa-list-ol')"></i></li>
      <li @click="onCmdClk"><i title="项目编号" :class="'insertunorderedlist '+ (isIonicons ? 'ivu-icon ivu-icon-ios-list' : 'fontAwesome fa-list-ul')"></i></li>
      <li @click="onCmdClk"><i title="增加缩进" :class="'outdent '+ (isIonicons ? 'ivu-icon ivu-icon-ios-return-left'  : 'fontAwesome fa-outdent')"></i></li>
      <li @click="onCmdClk"><i title="减少缩进" :class="'indent  '+ (isIonicons ? 'ivu-icon ivu-icon-ios-return-right' : 'fontAwesome fa-indent')"></i></li>
      <li class="dorpdown">
        <i title="字体颜色" :class="isIonicons ? 'ivu-icon ivu-icon-md-brush' : 'fontAwesome fa-paint-brush'"></i>
        <div class="colorPicker" v-html="createColorPickerHTML()" @click="format('foreColor', $event.target.title)"></div>
      </li>
      <li class="dorpdown">
        <i title="背景颜色" :class="isIonicons ? 'ivu-icon ivu-icon-ios-brush-outline' : 'fontAwesome fa-pencil'"></i>
        <div class="colorPicker" v-html="createColorPickerHTML()" @click="format('backColor', $event.target.title)"></div>
      </li>
      <li @click="createLink"><i title="增加链接" :class="isIonicons ? 'ivu-icon ivu-icon-ios-link'  : 'fontAwesome fa-link'"></i></li>
      <li @click="insertImage"><i title="增加图片" :class="isIonicons ? 'ivu-icon ivu-icon-md-images' : 'fontAwesome fa-regular fa-image'"></i></li>
      <li @click="saveRemoteImage2Local"><i title="一键存图" :class="isIonicons ? 'ivu-icon ivu-icon-md-photos' : 'fontAwesome fa-camera'"></i></li>
      <li @click="cleanHTML"><i title="清理 HTML" :class="isIonicons ? 'ivu-icon ivu-icon-md-done-all' : 'fontAwesome fa-eraser'"></i></li>
      <li @click="isShowCode = !isShowCode"><i title="切换到代码" :class="(isIonicons ? 'ivu-icon ivu-icon-md-code' : 'fontAwesome fa-code')+ ' switchMode'"></i></li>
    </ul>

    <div class="editorBody">
      <iframe srcdoc="<html><body></body></html>"></iframe>
      <textarea></textarea>
    </div>
    <!-- <aj-form-popup-upload ref="uploadLayer" :upload-url="uploadImageActionUrl"></aj-form-popup-upload> -->
  </div>
</template>

<script lang="ts" src="./HtmlEditor.ts"></script>

<style lang="less" src="./HtmlEditor.less"></style>