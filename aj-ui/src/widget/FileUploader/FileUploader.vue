<template>
  <div class="aj-file-uploader">
    <input type="file" :id="'uploadInput_' + radomId" @change="onUploadInputChange" :accept="accpectFileType" />

    <label class="pseudoFilePicker" :for="'uploadInput_' + radomId">
      <div @drop="onDrop" ondragover="event.preventDefault();">
        <div>+</div>点击选择文件<br />或拖放到此
      </div>
    </label>

    <div class="msg" v-if="errMsg != ''">
      允许类型：{{limitFileType || '无限制'}}
      <br />
      允许大小：{{limitSize ? changeByte(limitSize * 1024) : '无限制'}}
      <span class="slot"></span>
    </div>
    <div class="msg" v-if="errMsg == ''">
      {{fileName}}<div v-if="fileSize">{{changeByte(fileSize)}}</div>
      <button @click.prevent="doUpload">{{progress && progress !== 100 ? '上传中 ' + progress + '%': '上传'}}</button>
    </div>
  </div>
</template>

<script lang="ts" src="./FileUploader.ts"></script>

<style lang="less" src="./FileUploader.less"></style>