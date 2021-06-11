/* ----------------------------------------工具函数---------------------------------- */
// 使的 SVG 图形可以添加样式类
// @ts-ignore
Raphael.el.addClass = function (className: string): object {
    this.node.setAttribute("class", className);
    return this;
}

namespace aj.svg {
    /**
     * Raphael.js 桌布
     */
    export let PAPER: any;

    export class Utils {
        /**
         * 计算矩形中心到点 p 的连线与矩形的交叉点
         * 
         * @param rect 矩形
         * @param p    点 p
         */
        public static connPoint(rect: VBox, p: Point): Point {
            let start: Point = p,
                end: Point = { x: rect.x + rect.width / 2, y: rect.y + rect.height / 2 },
                tag: number = (end.y - start.y) / (end.x - start.x); // 计算正切角度
            tag = isNaN(tag) ? 0 : tag;

            let rectTag: number = rect.height / rect.width,
                // 计算箭头位置
                xFlag: number = start.y < end.y ? -1 : 1,
                yFlag: number = start.x < end.x ? -1 : 1,
                arrowTop: number = 0, arrowLeft: number = 0;

            // 按角度判断箭头位置
            if (Math.abs(tag) > rectTag && xFlag == -1) {// top 边
                arrowTop = end.y - rect.height / 2;
                arrowLeft = end.x + xFlag * rect.height / 2 / tag;
            } else if (Math.abs(tag) > rectTag && xFlag == 1) {// bottom 边
                arrowTop = end.y + rect.height / 2;
                arrowLeft = end.x + xFlag * rect.height / 2 / tag;
            } else if (Math.abs(tag) < rectTag && yFlag == -1) {// left 边
                arrowTop = end.y + yFlag * rect.width / 2 * tag;
                arrowLeft = end.x - rect.width / 2;
            } else if (Math.abs(tag) < rectTag && yFlag == 1) {// right 边
                arrowTop = end.y + rect.width / 2 * tag;
                arrowLeft = end.x + rect.width / 2;
            }

            return { x: arrowLeft, y: arrowTop };
        }

        /**
         * 求两个点的中间点
         * 
         * @param p1 
         * @param p2 
         */
        public static center(p1: Point, p2: Point): Point {
            return {
                x: (p1.x - p2.x) / 2 + p2.x,
                y: (p1.y - p2.y) / 2 + p2.y
            };
        }

        /**
         * 三个点是否在一条直线上
         * 
         * @param p1 
         * @param p2 
         * @param p3 
         */
        public static isLine(p1: Point, p2: Point, p3: Point): boolean {
            let s: number,
                p2y: number;

            if ((p1.x - p3.x) == 0)
                s = 1;
            else
                s = (p1.y - p3.y) / (p1.x - p3.x);

            p2y = (p2.x - p3.x) * s + p3.y;

            if ((p2.y - p2y) < 10 && (p2.y - p2y) > -10) {
                p2.y = p2y;

                return true;
            }

            return false;
        }

        /**
         * 画箭头
         * 
         * @param p1 开始位置
         * @param p2 结束位置
         * @param r  前头的边长
         * @returns 箭头的三个点
         */
        public static arrow(p1: Point, p2: Point, r: number): Point[] {
            let atan: number = Math.atan2(p1.y - p2.y, p2.x - p1.x) * (180 / Math.PI),
                centerX: number = p2.x - r * Math.cos(atan * (Math.PI / 180)),
                centerY: number = p2.y + r * Math.sin(atan * (Math.PI / 180)),
                x2: number = centerX + r * Math.cos((atan + 120) * (Math.PI / 180)),
                y2: number = centerY - r * Math.sin((atan + 120) * (Math.PI / 180)),
                x3: number = centerX + r * Math.cos((atan + 240) * (Math.PI / 180)),
                y3: number = centerY - r * Math.sin((atan + 240) * (Math.PI / 180));

            return [p2, { x: x2, y: y2 }, { x: x3, y: y3 }];
        }
    }

    /**
     * 文字对象
     */
    export interface TextSvgComp extends Vue {
        /**
         * 设置文字坐标
         * 
         * @param this 
         * @param x 
         * @param y 
         */
        setXY(this: TextSvgComp, x: number, y: number): void;

        setXY_vBox( vBox: VBox): void;
    }

    /**
     * 创建文本节点，返回 vue 实例
     * 
     * @param _text 
     * @param x 
     * @param y 
     */
    export function createTextNode(_text: string | any, x: number, y: number): TextSvgComp {
        let text: SVGTextElement = document.createElementNS("http://www.w3.org/2000/svg", "text");
        text.textContent = '{{value}}';
        text.setAttributeNS(null, "x", String(x));
        text.setAttributeNS(null, "y", String(y));
        (<Element>document.body.$('svg')).appendChild(text);

        return <TextSvgComp>new Vue({
            el: text,
            data: typeof _text == 'string' ? { value: _text } : _text,
            methods: {
                /**
                 * 设置文字坐标
                 * 
                 * @param this 
                 * @param x 
                 * @param y 
                 */
                setXY(this: TextSvgComp, x: number, y: number): void {
                    this.$el.setAttributeNS(null, "x", String(x));
                    this.$el.setAttributeNS(null, "y", String(y));
                },

                /**
                 * 返回文字坐标
                 * 
                 * @param this 
                 */
                getXY(this: TextSvgComp): Point {
                    return {
                        x: Number(this.$el.getAttribute('x')),
                        y: Number(this.$el.getAttribute('y'))
                    };
                },

                /**
                 * 在一个 box 中居中定位文字
                 * 
                 * @param vBox 
                 */
                setXY_vBox(this: TextSvgComp, vBox: VBox): void {
                    let w: number = vBox.x + vBox.width / 2,
                        h: number = vBox.y + vBox.height / 2,
                        textBox: DOMRect = this.$el.getBoundingClientRect();

                    this.setXY(w - textBox.width / 2, h + 5);
                }
            }
        });
    }
}