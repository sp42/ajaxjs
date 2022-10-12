<template>
  <div class="home">
    <h2>日历控件</h2>

    <p>
      开门见山，先说明一下这日历组件的核心算法不是笔者原创的，而是来自于早期<a href="https://www.cnblogs.com/cloudgamer/articles/1274459.html" target="_blank">大神
      cloudgamer 的作品</a>，在这里向前辈致敬！ 组件本身的原理不算复杂难懂，也就寥寥
      140 行代码（原 JavaScript），足以展现日历渲染的原理。
    </p>
    <p>
      <a
        href="https://blog.csdn.net/zhangxin09/article/details/89681010"
        target="_blank"
        >查看教程</a
      >
    </p>
    <p>
      <Calendar style="margin:20px auto;" @pickdate="showDate" />
    </p>
  </div>
</template>
<script>
import Calendar from "@/widget/calendar/Calendar.vue";

export default {
  components: { Calendar },
  methods:{
    showDate(date) {
        alert(date);
    }
  }
};
</script>