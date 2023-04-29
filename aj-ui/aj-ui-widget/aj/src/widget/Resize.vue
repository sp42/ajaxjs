<template>
  <!-- 拖拉缩放效果 -->
  <div class="resize-box">
    <div class="rRightDown" @mousedown="start($event, rightDown)"> </div>
    <div class="rLeftDown" @mousedown="start($event, leftDown)"> </div>
    <div class="rRightUp" @mousedown="start($event, rightUp)"> </div>
    <div class="rLeftUp" @mousedown="start($event, leftUp)"> </div>
    <div class="rRight" @mousedown="start($event, right)"> </div>
    <div class="rLeft" @mousedown="start($event, left)"> </div>
    <div class="rDown" @mousedown="start($event, down)"></div>
    <div class="rUp" @mousedown="start($event, up)"> </div>
  </div>
</template>

<script lang="ts" src="./Resize.ts"></script>

<style lang="less" scoped>
.rRightDown,
.rLeftDown,
.rLeftUp,
.rRightUp,
.rRight,
.rLeft,
.rUp,
.rDown {
  position: absolute;
  background: #c00;
  width: 7px;
  height: 7px;
  z-index: 5;
  font-size: 0;
}

.rLeftDown, .rRightUp {
  cursor: ne-resize;
}

.rRightDown, .rLeftUp {
  cursor: nw-resize;
}

.rRight, .rLeft {
  cursor: e-resize;
}

.rUp, .rDown {
  cursor: n-resize;
}

.rLeftDown {
  left: -4px;
  bottom: -4px;
}

.rRightUp {
  right: -4px;
  top: -4px;
}

.rRightDown {
  right: -4px;
  bottom: -4px;
  background-color: 0f;
}

.rLeftUp {
  left: -4px;
  top: -4px;
}

.rRight {
  right: -4px;
  top: 50%;
  margin-top: -4px;
}

.rLeft {
  left: -4px;
  top: 50%;
  margin-top: -4px;
}

.rUp {
  top: -4px;
  left: 50%;
  margin-left: -4px;
}

.rDown {
  bottom: -4px;
  left: 50%;
  margin-left: -4px;
}

.resize-box {
  border: 1px solid gray;
  width: 100px;
  height: 60px;
  top: 150px;
  left: 550px;
  background: #fff;
  position: absolute;
}
</style>