<template>
  <span><span class="httpMethod" :class="type">{{type}}</span></span>
</template>

<script>
export default {
  props: {
    type: {
      type: String,
      required: true,
    },
  },
};
</script>

<style scoped>
.httpMethod {
  border: 1px solid;
  border-radius: 3px;
  padding: 0 3px;
  border: 1px solid;
  border-radius: 3px;
  display: inline-block;
  margin-right: 3px;
  font-size: 5px;
  line-height: 15px;
  height: 14px;
}

.GET {
  border-color: #19be6b;
  background-color: #f5fffa;
  color: #19be6b;
}

.POST {
  border-color: #2db7f5;
  background-color: #e8f2fc;
  color: #2db7f5;
}

.PUT {
  border-color: #f90;
  background-color: #fcf3e8;
  color: #f90;
}

.DELETE {
  border-color: #ff4f4f;
  background-color: #ffeeee;
  color: #ff4f4f;
}
</style>