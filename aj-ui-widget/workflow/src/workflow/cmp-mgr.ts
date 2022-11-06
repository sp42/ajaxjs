/**
 * 组件管理器
 */
export default {
    /**
     * 所有组件
     */
    ALL_COMPS: {},

    /**
     * 生成下一个 id
     * 
     * @returns 下一个 id
     */
    nextId(): number {
        return ++uid;
    },

    /**
     * 登记组件
     * 
     * @param vueObj 
     */
    register(comp: Component): void {
        let id: string = "ajComp-" + comp.id;
        // comp.svg.node.id = id;

        let w: Warpper = <Warpper><unknown>comp;
        w.svg.node.id = id;

        this.ALL_COMPS[id] = comp;
    },

    /**
     * 注销组件
     * 
     * @param id 
     */
    unregister(id: number): void {
        delete this.ALL_COMPS[id];
    }
}

/**
* id 记数器
*/
let uid: number = 0;