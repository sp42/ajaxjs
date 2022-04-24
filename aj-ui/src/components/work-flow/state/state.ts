import { BaseState } from './base';
import { ResizeControl } from '../svg/resize';
import { createTextNode } from '../svg/utils';

/**
 * 一个流程节点
 */
export class State extends BaseState {
    constructor(PAPER: any, ref: string, data: JsonState) {
        let svgR: Raphael = PAPER.rect().attr(data.attr).attr({ fill: "90-#fff-#F6F7FF" }).addClass('rectBaseStyle');
        super(PAPER, ref, data, svgR);

        if (data.text && data.text.text) {
            this.text = createTextNode(data.text.text, 0, 0);
            this.text.setXY_vBox(this.vBox);
        }

        if (this.resize) {
            this.resizeController = new ResizeControl(this);
            this.resizeController.renderer();
        }
    }
}
