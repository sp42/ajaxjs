<template>
  <div>
    <FromRenderer ref="FromRenderer" :cfg="cfg" />
    <div class="btns" v-if="isShowBtns">
      <Button type="primary" v-if="status === 1" @click="create">创建</Button>
      <Button type="primary" v-if="status === 2" @click="update">保存</Button>

      <Button @click="resetFields" v-if="status !== 0">重置</Button>
      <Button @click="$router.back()">返回</Button>
    </div>
  </div>
</template>

<script lang="ts" src="./form-loader.ts"></script>

<style lang="less" scoped>
.btns {
  text-align: center;
  padding: 20px 0;

  Button {
    width: 150px;
    letter-spacing: 3px;
    margin-right: 30px;
  }
}
</style>