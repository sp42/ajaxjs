namespace aj.admin.treeLike {
    interface AdminTreeLike extends Vue {
        /**
         * 渲染
         */
        render(): void;

        /**
         * 刷新
         * 
         * @param j 
         */
        refresh(j: RepsonseResult): void;

        /**
         * 选中的分类 id
         */
        selectedId: number;

        /**
         * 选中的分类名称
         */
        selectedName: string;
    }

    new Vue({
        el: '.tree-like',
        data: {
            selectedId: 0,
            selectedName: ''
        },
        mounted(this: AdminTreeLike): void {
            // 新增顶级分类
            xhr.form(".createTopNode", j => {
                (<HTMLInputElement>document.querySelector(".createTopNode input[name=name]")).value = '';
                this.refresh(j);
            }, { noFormValid: true });

            // 添加子分类
            xhr.form(".createUnderNode", j => {
                (<HTMLInputElement>document.querySelector(".createUnderNode input[name=name]")).value = '';
                this.refresh(j);
            }, { noFormValid: true });

            // 修改名称
            xhr.form(<HTMLFormElement>this.$el.$('.rename'), j => {
                this.$refs.layer.close();
                this.refresh(j);
            });

            this.render();
        },
        methods: {
            onChange(this: AdminTreeLike, ev: Event): void {
                let selectEl: HTMLSelectElement = <HTMLSelectElement>ev.target,
                    option: HTMLOptionElement = selectEl.selectedOptions[0],
                    id: string = option.value,
                    // pid: string = <string>option.dataset['pid'],
                    name: string = option.innerHTML.replace(/&nbsp;|└─/g, '');

                this.selectedId = Number(id);
                this.selectedName = name;
            },
            rename(this: AdminTreeLike): void {
                if (!this.selectedId) {
                    aj.alert('未选择任何分类');
                    return;
                }

                this.$refs.layer.show();
            },
            // 删除
            dele(this: AdminTreeLike): void {
                if (!this.selectedId) {
                    aj.alert('未选择任何分类');
                    return;
                }

                showConfirm(`确定删除该分类[${this.selectedName}]？<br />[${this.selectedName}]下所有的子节点也会随着一并全部删除。`,
                    () => xhr.dele(this.selectedId + "/", this.refresh)
                );
            },

            refresh(this: AdminTreeLike, j: RepsonseResult): void {
                if (j.isOk) {
                    aj.alert(j.msg);
                    this.render();
                } else
                    aj.alert(j.msg);
            },

            render(this: Vue): void {
                let select: HTMLSelectElement = <HTMLSelectElement>this.$el.$('select');
                select.innerHTML = '';
                xhr.get('.', j => tree.rendererOption(<tree.TreeNode[]>j.result, select));
            }
        }
    });
}