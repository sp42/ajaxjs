Vue.component('aj-layer', {
    template: html`<div class="modal-mask">
      <div class="modal-container">
        <!-- 弹出层内容 -->
        <slot></slot>
      </div>
  </div>`
});

Vue.component('aj-confirm', {
    template: html`<div class="modal-mask">
	      <div class="modal-container comfirm">
	        	<h2>{{ title }}</h2>
		        <p>{{ message }}</p>
		        <div class="confirm-buttons">
		          <button @click="confirm">确定</button>
		          <button @click="cancel">取消</button>
		        </div>
	        </div>
	      </div>
	  </div>`,
    props: {
        title: {
            type: String,
            default: '确认'
        },
        message: {
            type: String,
            default: '确定执行此操作吗？'
        },
        confirmHandler: { // 执行函数名称
            type: Function
        },
        state: { // 执行函数名称
            type: String
        }
    },
    data() {
        return {
            isShow: false
        };
    },
    methods: {
        confirm() {
            if (this.confirmHandler)
                this.confirmHandler();
            else
                this.$parent.confirm();

            this.cancel();

        },
        cancel() {
            if (this.state)
                this.$parent[this.state] = false;
            else
                this.$parent.isShow = false;
        }
    }
});

/**
 * 轮播图（透明渐变）
 */
Vue.component('aj-carousel', {
    template: html`<div class="carousel">
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


Vue.component('aj-process-line', {
    template: html`<div class="aj-process-line">
    <div class="process-line">
      <div v-for="(item, index) in items" :key="index" :class="{current: index == current, done: index < current}">
        <span>{{index + 1}}</span>
        <p>{{item}}</p>
      </div>
    </div>
  </div>`,
    props: {
        items: {
            type: Array,
            default() {
                return ["Step 1", "Step 2", "Step 3"];
            },
        },
    },
    data() {
        return {
            current: 0,
        };
    },
    methods: {
        /**
         *
         * @param i
         */
        go(i) {
            this.current = i;
        },

        /**
         *
         */
        perv() {
            let perv = this.current - 1;
            if (perv < 0) perv = this.items.length - 1;

            this.go(perv);
        },

        /**
         *
         */
        next() {
            let next = this.current + 1;
            if (this.items.length == next) next = 0; // 循环

            this.go(next);
        },
    },
});

Vue.component('aj-list', {
    template: html`<div class="aj-list">
        <table align="center">
            <thead>
                <tr>
                    <slot name="header"></slot>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item) in data" :key="item.id">
                    <slot :data="item"></slot>
                </tr>
            <tbody>
        </table>
     <ul class="pager">
        <li :class="{disabled: currentPage === 1}">
            <a href="###" @click="goToPreviousPage">上一页</a>
        </li>
        <li v-for="(item, index) in paginatedData" :key="index" :class="{actived: currentPage === (index + 1)}">
            <a href="###" @click="setPageNo(index + 1)">{{index + 1}}</a>
        </li>
        <li :class="{disabled: currentPage === totalPages}">
            <a href="###" @click="goToNextPage">下一页</a>
        </li>
    </ul>
    <div class="b">共{{total}}条记录，每页<input type="text" v-model="itemsPerPage" size="1" />记录，</div>
  </div>`,
    props: {
        items: {
            type: Array,
            default() {
                return ["Step 1", "Step 2", "Step 3"];
            },
        },
    },
    data() {
        return {
            total: 200,
            data: [], // 从MySQL获取的所有数据
            itemsPerPage: 10, // 每页显示条目数
            currentPage: 1, // 当前页数
            start: 0,
        };
    },
    computed: {
        paginatedData() {
            if (this.itemsPerPage <= 0)
                this.itemsPerPage = 5;

            const start = (this.currentPage - 1) * this.itemsPerPage;
            const end = start + this.itemsPerPage;

            this.start = start;

            return new Array(this.totalPages);
        },
        totalPages() {
            return Math.ceil(this.total / this.itemsPerPage);
        },
    },
    methods: {
        goToPreviousPage() {
            if (this.currentPage > 1)
                // this.currentPage -= 1;
                this.setPageNo(this.currentPage - 1);

        },
        goToNextPage() {
            if (this.currentPage < this.totalPages)
                // this.currentPage += 1;
                this.setPageNo(this.currentPage + 1);

        },
        setPageNo(pageNo) {
            this.currentPage = pageNo;
            this.fetchDataFromMySQL();
        },
        fetchDataFromMySQL() {
            const start = (this.currentPage - 1) * this.itemsPerPage;

            aj.xhr.get(`https://iam.ajaxjs.com/iam/common_api/user_login_log/page?start=${start}&limit=${this.itemsPerPage}`, (j) => {
                this.data = j.data.rows;
                this.total = j.data.total;
            }, new aj.aj_iam.Sdk().getAuthHeader());
        },
    },
    mounted() {
        this.fetchDataFromMySQL();
    },
});
