/**
 * 表单工具函数
 */
namespace aj.form.utils {
    /**
     * 获取表单控件的值
     * 
     * @param el 
     * @param cssSelector 
     */
    export function getFormFieldValue(_el: HTMLElement, cssSelector: cssSelector): string {
        let el = _el.$(cssSelector);

        if (el)
            return (<HTMLInputElement>el).value;
        else
            throw `找不到${cssSelector}元素`;
    }


    // 指定 id 的那个 option 选中
    export function selectOption(this: Vue, id: string): void {
        this.$el.$('option', (i: HTMLOptionElement) => {
            if (i.value == id)
                i.selected = true;
        });
    }
}