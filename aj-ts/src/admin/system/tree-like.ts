interface AdminTreeLike {

}

new Vue({
    el: '.tree-like',
    data: {
        selectedId: 0,
        selectedName: ''
    },
    mounted(): void {
        // 新增顶级${uiName}
        aj.xhr.form(".createTopNode", json => {
            (<HTMLInputElement>document.querySelector(".createTopNode input[name=name]")).value = '';
            this.refresh(json);
        });

        // 在${uiName}下添加子${uiName}
        aj.xhr.form(".createUnderNode", json => {
            (<HTMLInputElement>document.querySelector(".createUnderNode input[name=name]")).value = '';
            this.refresh(json);
        });

        // 修改名称
        aj.xhr.form(this.$el.$('.rename'), json => {
            this.$refs.layer.close();
            this.refresh(json);
        });

        this.render();
    },
    methods: {
        onChange(ev: Event): void {
            let selectEl: HTMLSelectElement = <HTMLSelectElement>ev.target,
                option = selectEl.selectedOptions[0],
                id = option.value, pid = option.dataset['pid'],
                name = option.innerHTML.replace(/&nbsp;|└─/g, '');

            this.selectedId = id;
            this.selectedName = name;
        },
        rename(): void {
            if (!this.selectedId) {
                aj.alert('未选择任何分类');
                return;
            }

            this.$refs.layer.show();
        },
        // 删除
        dele(): void {
            if (!this.selectedId) {
                aj.alert('未选择任何分类');
                return;
            }

            aj.showConfirm('确定删除该${uiName}[{0}]？<br />[{0}]下所有的子节点也会随着一并全部删除。'.replace(/\{0\}/g, this.selectedName),
                () => aj.xhr.dele("" + this.selectedId + "/", this.refresh)
            );
        },

        refresh(json: RepsonseResult): void {
            if (json.isOk) {
                aj.alert(json.msg);
                this.render();
            } else
                aj.alert(json.msg);
        },

        render(this: Vue): void {
            let select: HTMLSelectElement = <HTMLSelectElement>this.$el.$('select');
            select.innerHTML = '';
            aj.xhr.get('.', j => aj.list.tree.rendererOption(<TreeNode[]>j.result, select));
        }
    }
});