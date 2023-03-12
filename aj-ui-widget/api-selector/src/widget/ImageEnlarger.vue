<template>
  <a class="aj-img-thumb" :href="imgUrl" v-if="imgUrl" target="_blank">
    <img :src="imgUrl" @mouseenter="current = imgUrl" @mouseleave="current = null" />
  </a>
</template>

<script>
/**
 * 悬浮显示大图
 */
export default {
  props: {
    imgUrl: { type: String, required: true },// 图片地址
  },

  data() {
    return { current: '' }
  },
  watch: {
    current(v) {
      let el = document.querySelector("body > div.aj-image-large-view");

      if (v) {
        el.querySelector('img').src = v;
        setTimeout(() => el.style.display = 'block', 700);
      } else
        el.style.display = 'none';
    }
  },
  mounted() {
    // 初始化
    let div = document.querySelector("body > div.aj-image-large-view");

    if (!div) {
      div = document.createElement("div");
      div.className = "aj-image-large-view";
      div.innerHTML = "<img />";

      document.body.appendChild(div);
    }

    this.$el.addEventListener("mousemove", throttle(e => {
      if (this.imgUrl) {
        let w = 0, imgWidth = div.querySelector("img").clientWidth;

        if (imgWidth > e.pageX)
          w = imgWidth;

        div.style.top = e.pageY + 20 + "px";
        div.style.left = e.pageX - div.clientWidth + w + "px";
      }
    }, 50, 5000), false);
  }
};

/**
 * 函数节流
 *
 * @author https://www.cnblogs.com/moqiutao/p/6875955.html
 * @param fn
 * @param delay
 * @param mustRunDelay
 */
function throttle(fn, delay, mustRunDelay) {
  var timer, t_start;
  return function () {
    var _this = this;
    var t_curr = +new Date();
    window.clearTimeout(timer);

    if (!t_start)
      t_start = t_curr;
      
    if (t_curr - t_start >= mustRunDelay) {
      // @ts-ignore
      fn.apply(this, arguments);
      t_start = t_curr;
    } else {
      var args = arguments;
      // @ts-ignore
      timer = window.setTimeout(function () {
        return fn.apply(_this, args);
      }, delay);
    }
  };
}
</script>

<style lang="less">
.aj-img-thumb img {
  max-width: 50px;
  max-height: 60px;
  vertical-align: middle;
}

.aj-image-large-view {
  position: fixed;
  max-width: 400px;
  transition: top ease-in 200ms, left ease-in 200ms;

  img {
    width: 100%;
  }
}
</style>