export default {
    data() {
        return {
            styleWidth: 0,// 样式参数值
            styleHeight: 0,
            styleLeft: 0,
            styleTop: 0,
            sideLeft: 0,  // 四条边定位坐标
            sideRight: 0,
            sideUp: 0,
            sideDown: 0,
            fixLeft: 0,   // top 和 left 定位参数
            fixTop: 0,
            fn: null,

            LockX: false,// 是否锁定水平方向拖放
            LockY: false,// 是否锁定垂直方向拖放
            Lock: false,// 是否锁定

            scale: {                // 按比例缩放
                enable: true,      // 是否按比例缩放
                ratio: 0,           // 设置比例(宽/高)
                left: 0,
                top: 0,
            },

            min: {                  // 最小控制
                enable: false,
                height: 100,
                width: 200,
            },
            max: {                  // 最大控制
                enable: false,
                height: 300,
                width: 500,
            }

        };
    },
    methods: {
        /**
         * 准备拖动
         */
        start(e: MouseEvent, fn: Function): void {
            this.styleWidth = this.$el.clientWidth;
            this.styleHeight = this.$el.clientHeight;
            this.styleLeft = this.$el.offsetLeft;         // 记录鼠标相对拖放对象的位置
            this.styleTop = this.$el.offsetTop;

            this.sideLeft = e.clientX - this.styleWidth;
            this.sideRight = e.clientX + this.styleWidth;
            this.sideUp = e.clientY - this.styleHeight;
            this.sideDown = e.clientY + this.styleHeight;

            this.fixLeft = this.styleWidth + this.styleLeft;
            this.fixTop = this.styleHeight + this.styleTop;
            this.fn = fn;

            if (this.scale.enable) {
                this.scale.ratio = Math.max(this.scale.ratio, 0) || this.styleWidth / this.styleHeight;        //设置比例
                this.scale.left = this.styleLeft + this.styleWidth / 2;// left 和 top 的定位坐标
                this.scale.top = this.styleTop + this.styleHeight / 2;
            }

            document.addEventListener("mousemove", this.resize);// mousemove 时移动 mouseup 时停止
            document.addEventListener("mouseup", this.stop);
        },

        // 缩放
        resize(e: MouseEvent): void {
            window.getSelection().removeAllRanges(); // 清除选择
            this.fn(e);

            if (this.min.enable) {
                if (this.styleHeight <= this.min.height)
                    return;
                if (this.styleWidth <= this.min.width)
                    return;
            }
            if (this.max.enable) {
                if (this.styleHeight >= this.max.height)
                    return;
                if (this.styleWidth >= this.max.width)
                    return;
            }

            let s = this.$el.style;
            s.width = this.styleWidth + "px";
            s.height = this.styleHeight + "px";
            s.top = this.styleTop + "px";
            s.left = this.styleLeft + "px";
        },

        // 停止缩放
        stop(): void {
            document.removeEventListener("mousemove", this.resize);
            document.removeEventListener("mouseup", this.stop);
        },

        //缩放程序

        //上
        up(e: MouseEvent): void {
            this.styleHeight = Math.max(this.sideDown - e.clientY, 0);
            this.styleTop = this.fixTop - this.styleHeight;
            this.styleLeft = this.scale.left - this.styleWidth / 2;
            console.log(this.styleLeft)
        },

        //下
        down(e: MouseEvent): void {
            this.styleHeight = Math.max(e.clientY - this.sideUp, 0);
            this.styleLeft = this.scale.left - this.styleWidth / 2;

        },

        //右
        right(e: MouseEvent): void {
            this.styleWidth = Math.max(e.clientX - this.sideLeft, 0);
        },

        //左
        left(e: MouseEvent): void {
            this.styleWidth = Math.max(this.sideRight - e.clientX, 0);
            this.styleLeft = this.fixLeft - this.styleWidth;
        },

        //右下
        rightDown(e: MouseEvent): void {
            this.right(e);
            this.down(e);
        },

        //右上
        rightUp(e: MouseEvent): void {
            this.right(e);
            this.up(e);
        },

        //左下
        leftDown(e: MouseEvent): void {
            this.left(e);
            this.down(e);
        },

        //左上
        leftUp(e: MouseEvent): void {
            this.left(e);
            this.up(e);
        },
    },
};