<!-- 演示预览用的 loader -->
<template>
  <Modal v-model="isShow" :title="'预览表单 ' + name" cancel-text="" width="900">
    <FromRenderer ref="FromRenderer" :cfg="cfg" />

    <div slot="footer">
      <Input type="text" v-model="loadFormId" style="margin-right:10px;width:120px;" placeholder="实体 id" />
      <Button style="margin-right:10px;" @click="loadInfo">加载表单内容</Button>
      <Button style="margin-right:10px;" :disabled="loadFormId != 0" @click="loadInfo">创建</Button>
      <Button style="margin-right:10px;" :disabled="loadFormId == 0 ">修改</Button>
      <Button type="primary" @click="isShow = false">关闭</Button>
    </div>
  </Modal>
</template>

<script lang="ts" src="./loader.ts"></script>