/**
 * Undo/Redo 管理器
 */

const MAX_STEPS = 50;
const STACK = [];

function undo(): void { }

function redo(): void { }

export default {
    watch: {
        metaData: {
            deep: true,
            handler(n: any): void {
                if (n) {
                    // make a snapshot
                    if (STACK.length < MAX_STEPS) {
                        STACK.push(JSON.stringify(n))
                    }
                }
            }
        }
    }
};