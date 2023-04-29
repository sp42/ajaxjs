<template>
  <ul class="aj-accordion-menu" @click="onClk">
    <slot></slot>
  </ul>
</template>

<script lang="ts">
/**
 * 内部子菜单的高亮
 *
 * @param ev
 */
function highlightSubItem(ev: Event) {
  let li: Element,
    el: Element = ev.target as Element;

  if (el.tagName == "A" && el.getAttribute("target")) {
    li = el.parentNode as Element;
    li.querySelectorAll("li").forEach((_el: Element) => {
      if (_el == li) _el.classList.add("selected");
      else _el.classList.remove("selected");
    });
  }
}

export default {
  methods: {
    onClk(ev: Event): void {
      let children: HTMLCollection = this.$el.children;
      highlightSubItem(ev);
      let _btn: Element = ev.target as Element;

      if (
        _btn &&
        _btn.tagName == "H3" &&
        (_btn.parentNode as Element).tagName == "LI"
      ) {
        _btn = _btn.parentNode as Element;

        for (let btn: Element, i: number = 0, j = children.length; i < j; i++) {
          btn = children[i];
          let ul = btn.querySelector("ul");

          if (btn == _btn) {
            if (btn.className.indexOf("pressed") != -1) {
              btn.classList.remove("pressed"); // 再次点击，隐藏！
              if (ul) ul.style.height = "0px";
            } else {
              if (ul) ul.style.height = ul.scrollHeight + "px";
              btn.classList.add("pressed");
            }
          } else {
            btn.classList.remove("pressed");
            if (ul) ul.style.height = "0px";
          }
        }
      } else return;
    },
  },
};
</script>

<style lang="less" scoped>
@import "../style/common-functions.less";

// 折叠菜单 Accordion Menu
.aj-accordion {
  & > li h3 {
    cursor: pointer;
  }

  .pressed {
    & h3 {
      color: black;
    }
  }

  & > li > ul {
    .transition (height .5s cubic-bezier(0, 1, 0.5, 1));;
    overflow: hidden;
  }

  ul {
    height: 0;
  }
}

.aj-accordion-menu {
  .aj-accordion ();
  overflow: hidden;

  & > li {
    border-top: 1px solid white;
    border-bottom: 1px solid lightgray;

    &.pressed {
      border-top: 0;
      border-bottom: 1px solid lightgray;
      box-shadow: inset 0px 10px 15px -15px gray;

      h3 {
        font-weight: bold;
      }
    }

    ul {
      li {
        // list-style-type: disc;
        padding-left: 15%;

        a {
          width: 100%;
          display: block;
        }

        &.selected {
          a {
            color: black;
            font-weight: bold;
          }
        }
      }
    }

    h3,
    li {
      padding: 5px 0 5px 15px;
      letter-spacing: 2px;
      line-height: 20px;
      color: #939da8;
      font-size: 12px;

      &:hover,
      a:hover {
        color: black;
      }
    }
  }
}
</style>