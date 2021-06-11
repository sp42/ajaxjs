/**
 * 包装对象
 */
interface Warpper {
    /**
     * 来自 Raphael 的对象
     */
     svg: Raphael;

     /**
      * Vue 实例，可选
      */
     vue?: Vue;
 
 
     /**
      * 文字对象
      */
     text?: aj.svg.TextSvgComp;
}
