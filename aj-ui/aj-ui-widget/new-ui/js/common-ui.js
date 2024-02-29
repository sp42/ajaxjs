/**
 * 轮播图（透明渐变）
 */
Vue.component('aj-carousel', {
    template: `<div class="carousel">
    <div class="carousel-inner">
        <div class="carousel-item" v-for="(slide, index) in items" :key="index" :class="{ active: index === currentIndex }">
            <img :src="slide.image" :alt="slide.alt" />
        </div>
    </div>
    <div class="carousel-indicators">
      <button v-for="(item, index) in items" :key="index" @click="select(index)" class="carousel-indicator" :class="{ active: index === currentIndex }"></button>
    </div>
    <button class="carousel-control-prev" @click="prev">‹</button>
    <button class="carousel-control-next" @click="next">›</button>
  </div>`,
    props: {
        items: {
            type: Array,
            required: true
        },
        interval: {
            type: Number,
            default: 5000
        }
    },
    data() {
        return {
            currentIndex: 0
        }
    },
    watch: {
        currentIndex() {
            this.clearInterval();
            this.startInterval();
        }
    },
    mounted() {
        this.startInterval();
    },
    methods: {
        select(index) {
            this.currentIndex = index
        },
        prev() {
            this.currentIndex = (this.currentIndex - 1 + this.items.length) % this.items.length
        },
        next() {
            this.currentIndex = (this.currentIndex + 1) % this.items.length
        },
        startInterval() {
            this.intervalId = setInterval(() => {
                this.next()
            }, this.interval);
        },
        clearInterval() {
            if (this.intervalId)
                clearInterval(this.intervalId);
        }
    }
});

/**
 * 调整正文字体大小
 */
Vue.component('aj-adjust-font-size', {
    template: `<div class="aj-adjust-font-size">
    <span>字体大小</span>
    <ul @click="onClk">
      <li><label><input type="radio" name="fontSize" /> 小</label></li>
      <li><label><input type="radio" name="fontSize" /> 中</label></li>
      <li><label><input type="radio" name="fontSize" /> 大</label></li>
    </ul>
  </div>`,
    props: {
        articleTarget: { type: String, default: "article p" }, // 正文所在的位置，通过 CSS Selector 定位
    },
    methods: {
        onClk(ev) {
            let el = ev.target;
            let setFontSize = (fontSize) => {
                document.body.querySelectorAll(this.$props.articleTarget).forEach((p) => (p.style.fontSize = fontSize));
            };

            if (el.tagName == "LABEL" || el.tagName == "INPUT") {
                if (el.tagName != "LABEL") el = el.parentNode;

                if (el.innerHTML.indexOf("大") != -1) setFontSize("14pt");
                else if (el.innerHTML.indexOf("中") != -1) setFontSize("10.5pt");
                else if (el.innerHTML.indexOf("小") != -1) setFontSize("9pt");
            }
        }
    }
});