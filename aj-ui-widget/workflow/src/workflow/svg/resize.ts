import { BaseState } from "../state/base";

/**
 * 改变大小的边框
 */
export class ResizeControl {
    /**
     * 是否显示边框
     */
    private isBorder: boolean = true;

    /**
     * 所服务的那个组件
     */
    private rectComp: BaseState;

    /**
     * 虚拟的一个控制框
     */
    private vBox: VBox;

    /**
     * 保存所有控制点的 map
     */
    private allDots: { [key: string]: any } = {};

    /**
     * 外边距，0=重合
     */
    private static margin: number = 10;

    private border: any;

    private dotX: number = 0;

    private dotY: number = 0;

    /**
     * 创建一个 Resize 控制器
     * 
     * @param rectComp 所服务的那个组件
     */
    constructor(rectComp: BaseState) {
        this.rectComp = rectComp;
        this.vBox = rectComp.vBox; // 虚拟的一个控制框
    }

    /**
     * 该方法只执行一次
     */
    renderer(): void {
        let allDots: { [key: string]: any } = {}, // 保存所有控制点的 map
            self = this;

        this.allDots = allDots;
        this.dotX = this.dotY = 0;

        let bdragStart = function (this: Raphael) {// 一定要用 fn，why？
            self.dotX = Number(this.attr('x'));
            self.dotY = Number(this.attr('y'));
        }

        let PAPER = this.rectComp.PAPER;
        allDots['t'] = PAPER.rect().addClass('resizeDot').attr({ cursor: 's-resize' }).drag((dx: number, dy: number) => this.dotMove(dx, dy, 't'), bdragStart);     // 上
        allDots['lt'] = PAPER.rect().addClass('resizeDot').attr({ cursor: 'nw-resize' }).drag((dx: number, dy: number) => this.dotMove(dx, dy, 'lt'), bdragStart);  // 左上
        allDots['l'] = PAPER.rect().addClass('resizeDot').attr({ cursor: 'w-resize' }).drag((dx: number, dy: number) => this.dotMove(dx, dy, 'l'), bdragStart);     // 左
        allDots['lb'] = PAPER.rect().addClass('resizeDot').attr({ cursor: 'sw-resize' }).drag((dx: number, dy: number) => this.dotMove(dx, dy, 'lb'), bdragStart);  // 左下
        allDots['b'] = PAPER.rect().addClass('resizeDot').attr({ cursor: 's-resize' }).drag((dx: number, dy: number) => this.dotMove(dx, dy, 'b'), bdragStart);     // 下
        allDots['rb'] = PAPER.rect().addClass('resizeDot').attr({ cursor: 'se-resize' }).drag((dx: number, dy: number) => this.dotMove(dx, dy, 'rb'), bdragStart);  // 右下
        allDots['r'] = PAPER.rect().addClass('resizeDot').attr({ cursor: 'w-resize' }).drag((dx: number, dy: number) => this.dotMove(dx, dy, 'r'), bdragStart);     // 右
        allDots['rt'] = PAPER.rect().addClass('resizeDot').attr({ cursor: 'ne-resize' }).drag((dx: number, dy: number) => this.dotMove(dx, dy, 'rt'), bdragStart);  // 右上

        this.hideBox();
        this.setDotsPosition();
        this.resize();
    }

    /**
     * 
     * @param isBorder 
     */
    enableBorder(isBorder: boolean): void {
        this.isBorder = isBorder;
    }

    /**
     * 
     */
    setDotsPosition(): void {
        let vBox: VBox = this.vBox,
            x: number = vBox.x - ResizeControl.margin,
            y: number = vBox.y - ResizeControl.margin,
            width: number = vBox.width + ResizeControl.margin * 2,
            height: number = vBox.height + ResizeControl.margin * 2,
            _bw: number = 2.5,
            allDots: { [key: string]: any } = this.allDots;

        allDots['t'].attr({ x: x + width / 2 - _bw, y: y - _bw }); 			// 上
        allDots['lt'].attr({ x: x - _bw, y: y - _bw }); 				    // 左上
        allDots['l'].attr({ x: x - _bw, y: y - _bw + height / 2 }); 	    // 左
        allDots['lb'].attr({ x: x - _bw, y: y - _bw + height }); 	        // 左下
        allDots['b'].attr({ x: x - _bw + width / 2, y: y - _bw + height }); // 下
        allDots['rb'].attr({ x: x - _bw + width, y: y - _bw + height }); 	// 右下
        allDots['r'].attr({ x: x - _bw + width, y: y - _bw + height / 2 }); // 右
        allDots['rt'].attr({ x: x - _bw + width, y: y - _bw }); 			// 右上
    }

    /**
     * 定位各个点的坐标
     */
    private resize(): void {
        this.setDotsPosition();
        this.rectComp.svg.attr(this.vBox);
        this.updateBorder();
    }

    /**
     * 显示
     */
    showBox(): void {
        this.border && this.border.show();  // 显示边框

        for (let i in this.allDots) // 逐个点显示
            this.allDots[i].show();
    }

    /**
     * 隐藏
     */
    hideBox(): void {
        this.border && this.border.hide();

        for (let i in this.allDots)
            this.allDots[i].hide();
    }

    /**
     * 
     * @param dx 移动距离（移动宽度）
     * @param dy 移动距离（移动高度）
     * @param type 类型
     */
    private dotMove(dx: number, dy: number, type: string): void {
        let x: number = this.dotX + dx,
            y: number = this.dotY + dy,
            vBox: VBox = this.vBox;

        // console.log('-----------------') console.log(this.dotY) console.log(dy)
        switch (type) {
            case 't':
                // console.log(vBox.y) console.log(y) console.log(vBox.y - y)
                vBox.height += vBox.y - y; vBox.y = y;
                break;
            case 'lt':
                vBox.width += vBox.x - x; vBox.height += vBox.y - y;
                vBox.x = x; vBox.y = y;
                break;
            case 'l':
                vBox.width += vBox.x - x;
                vBox.x = x;
                break;
            case 'lb':
                vBox.height = y - vBox.y; vBox.width += vBox.x - x;
                vBox.x = x;
                break;
            case 'b':
                vBox.height = y - vBox.y;
                break;
            case 'rb':
                vBox.height = y - vBox.y; vBox.width = x - vBox.x;
                break;
            case 'r':
                vBox.width = x - vBox.x;
                break;
            case 'rt':
                vBox.width = x - vBox.x; vBox.height += vBox.y - y;
                vBox.y = y;
                break;
        }

        this.resize();
    }

    /**
     * 边框路径的描边
     */
    updateBorder(): void {
        if (!this.isBorder)
            return;

        if (!this.border)
            this.border = this.rectComp.PAPER.path('M0 0L1 1').hide(); // 边框

        let vBox: VBox = this.vBox,
            x: number = vBox.x - ResizeControl.margin,
            y: number = vBox.y - ResizeControl.margin,
            width: number = vBox.width + ResizeControl.margin * 2,
            height: number = vBox.height + ResizeControl.margin * 2,
            str = 'M' + x + ' ' + y + 'L' + x + ' ' + (y + height) + 'L' + (x + width) + ' ' + (y + height) + 'L' + (x + width) + ' ' + y + 'L' + x + ' ' + y;

        this.border.attr({ path: str });
    }
}
