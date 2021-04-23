/**
 * 显示控件
 */
declare interface IDisplayControl extends SharpAction, Component, DargDrop {
    /**
     * Vue 实例，可选
     */
    vue?: Vue;

    /**
     * 来自 Raphael 的对象
     */
    svg: Raphael;
}