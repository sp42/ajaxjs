<template>
  <ul class="aj-opacity-banner">
    <slot></slot>
  </ul>
</template>

<script lang="ts">
/**
 * 渐显 banner
 * 注意：定时器保存在 DOM 元素的属性上，是否会内存泄漏呢？
 */
export default {
  props: {
    delay: { default: 3000 }, // 延时
    fps: { default: 25 }, // 帧速
  },
  data(): any {
    return {
      active: 0, // 当前索引
    };
  },
  mounted(): void {
    this.list = this.$el.querySelectorAll("li"); // 各帧
    this.list[0].style.opacity = "1";
    this.run();
  },
  methods: {
    /**
     * 播放动画
     *
     * @param this
     */
    run(): void {
      this.timer = window.setInterval(() => {
        let active: number = this.active;
        this.clear();
        active += 1;
        this.active = active % this.list.length;
        this.animate(100);
      }, this.delay);
    },

    /**
     * 下一帧
     *
     * @param this
     */
    per(): void {
      let active: number = this.active;
      this.clear();
      active -= 1;
      active = active % this.list.length;

      if (active < 0) active = this.list.length - 1;

      this.active = active;
      this.animate(100);
    },

    /**
     * 内容淡出
     */
    clear(): void {
      this.animate(0);
    },

    /**
     *
     * @param params
     */
    animate(params: number): void {
      var el: HTMLLIElement = this.list[this.active],
        fps: number = 1000 / this.fps;
      // @ts-ignore
      window.clearTimeout(el.timer);

      window.setTimeout(function loop() {
        var i: number = getOpacity(el);
        let speed: number = (params - i) / 8;
        speed = speed > 0 ? Math.ceil(speed) : Math.floor(speed);
        // console.log("i=" + i + "; speed="+ speed+"; s="+s+"; k="+k);
        i += speed;
        el.style.opacity = String(i / 100);
        // @ts-ignore
        window.clearTimeout(el.timer);
        // params.complete && params.complete.call(elem);
        // @ts-ignore
        el.timer = window.setTimeout(loop, fps);
      }, fps);
    },
  },
};

/**
 * 获取元素的透明度
 *
 * @param el
 */
function getOpacity(el: Element): number {
  let v = Number(getComputedStyle(el)["opacity"]);
  v *= 100;

  return parseFloat(v + "") || 0;
}
</script>

<style lang="less" scoped>
.aj-opacity-banner {
  position: relative;
  min-height: 90px;

  li {
    position: absolute;
    top: 0;
    left: 0;
    opacity: 0;
    width: 100%;
  }

  img {
    // border-top:solid 20px #00416d;
    width: 100%;
  }
}
</style>