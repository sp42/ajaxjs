namespace aj.svg {
    /**
     * 基础图形组件
     */
    export abstract class BaseComponent {
        public id: string = "";

        /**
         * 组件类型
         */
        public type: string | undefined;

        /**
         * Raphael SVG 实例
         */
        public svg: any;

        /**
         * 箱子
         */
        public vbox: VBox | undefined;

        /**
         * 显示
         */
        public abstract show(): void;

        /**
         * 隐藏
         */
        public abstract hide(): void;

        /**
         * 删除这个组件
         */
        public abstract remove(): void;


        /**
         * 序列化这个组件到 JSON
         */
        public abstract toJson(): string;
    }
}