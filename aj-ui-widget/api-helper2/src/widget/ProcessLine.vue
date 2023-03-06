<template>
  <div class="aj-process-line">
    <div class="process-line">
      <div v-for="(item, index) in items" :key="index" :class="{current: index == current, done: index < current}">
        <span>{{index + 1}}</span>
        <p>{{item}}</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
/**
 * 进度条
 */
export default {
  props: {
    items: {
      type: Array,
      default(): string[] {
        return ["Step 1", "Step 2", "Step 3"];
      },
    },
  },
  data():any {
    return {
      current: 0,
    };
  },
  methods: {
    /**
     *
     * @param i
     */
    go(i: number): void {
      this.current = i;
    },

    /**
     *
     */
    perv(): void {
      let perv: number = this.current - 1;
      if (perv < 0) perv = this.items.length - 1;

      this.go(perv);
    },

    /**
     *
     */
    next(): void {
      let next: number = this.current + 1;
      if (this.items.length == next) next = 0; // 循环

      this.go(next);
    },
  },
};
</script>

<style lang="less" scoped>
.aj-process-line {
  display: flex;
  padding: 5%;
  justify-content: center;

  .process-line {
    font-size: 18px;
    color: lightgray;
    font-weight: bold;

    & > div {
      float: left;
      width: 156px;
      text-align: center;
      position: relative;

      &.current {
        color: black;

        span {
          background-color: gray;
        }
      }

      &.done {
        //				color: lighten(@mainColor, 80%);
        span {
          //					background-color: lighten(@mainColor, 50%);
        }

        &::after {
          //					background-color: lighten(@mainColor, 50%);
        }
      }

      p {
        font-size: 1rem;
        letter-spacing: 3px;
        padding-top: 5%;
      }

      span {
        display: inline-block;
        width: 32px;
        height: 32px;
        border-radius: 50%;
        color: #fff;
        line-height: 32px;
        font-size: 16px;
        background-color: lightgray;
        position: relative;
        z-index: 1;
      }

      &:last-child::after {
        display: none;
      }

      &::after {
        content: "";
        position: absolute;
        top: 14px;
        left: 94px;
        width: 124px;
        height: 4px;
        background-color: lightgray;
      }
    }
  }
}
</style>