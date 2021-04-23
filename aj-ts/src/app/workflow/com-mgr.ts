/**
 * 组件管理器
 */
namespace aj.wf.ComMgr {
    /**
     * id 记数器
     */
    let uid: number = 0;

    /**
     * 生成下一个 id
     * @returns 下一个 id
     */
    export function nextId(): number {
        return ++uid;
    }
}