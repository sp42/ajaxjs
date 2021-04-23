namespace aj.wf {
    /**
     * 一个流程节点
     */
    class State extends BaseState {
        constructor(PAPER: any, type: string, ref: string, rawData: JsonState, vBox: VBox) {
            let svg: Raphael = PAPER.rect().attr(vBox).attr({ fill: "90-#fff-#F6F7FF" }).addClass('rectBaseStyle');
            super(PAPER, type, ref, rawData, vBox, svg);
        }
    }
}