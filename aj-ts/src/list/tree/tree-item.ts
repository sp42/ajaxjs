namespace aj.list.tree {
    /**
     * 注意递归组件的使用
     */
    export class TreeItem extends VueComponent {
        name = 'aj-tree-item';
        template = html`
            <li>
                <div :class="{bold: isFolder, node: true}" @click="toggle">
                    <span>········</span>{{model.name}}
                    <span v-if="isFolder">[{{open ? '-' : '+'}}]</span>
                </div>
                <ul v-show="open" v-if="isFolder" :class="{show: open}">
                    <aj-tree-item class="item" v-for="(model, index) in model.children" :key="index" :model="model">
                    </aj-tree-item>
                    <li v-if="allowAddNode" class="add" @click="addChild">+</li>
                </ul>
            </li>
        `;

        props = {
            model: Object,
            allowAddNode: { type: Boolean, default: false }// 是否允许添加新节点
        };

        model = { children: [] };

        open = false;

        allowAddNode = false;

        // isFolder = false;

        computed = {
            isFolder(this: TreeItem): boolean {
                return !!(this.model.children && this.model.children.length);
            }
        };

        /**
         * 点击节点时的方法
         * 
         * @param this 
         */
        toggle(this: TreeItem): void {
            //@ts-ignore
            if (this.isFolder)
                this.open = !this.open;

            this.BUS && this.BUS.$emit('tree-node-click', this.model);
        }

        /**
         * 变为文件夹
         * 
         * @param this 
         */
        changeType(this: TreeItem): void {
            //@ts-ignore
            if (!this.isFolder) {
                Vue.set(this.model, 'children', []);
                this.addChild();
                this.open = true;
            }
        }

        addChild(this: TreeItem): void {
            this.model.children.push({
                //@ts-ignore
                name: 'new stuff'
            });
        }
    }

    new TreeItem().register();
}
