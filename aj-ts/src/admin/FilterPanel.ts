/**
 * 搜索、分类下拉
 */
Vue.component('aj-admin-filter-panel', {
    template: `
        <div class="aj-admin-filter-panel">
            <form action="?" method="GET">
                <input type="hidden" name="searchField" :value="searchFieldValue" />
                <input type="text" name="keyword" placeholder="请输入搜索之关键字" style="float: inherit;" class="aj-input" />
                <button style="margin-top: 0;" class="aj-btn">搜索</button> &nbsp;
            </form><slot></slot>
            <span v-if="!noCatalog">{{label || '分类'}}：
                <aj-tree-catelog-select :is-auto-jump="true" :catalog-id="catalogId" :selected-catalog-id="selectedCatalogId"></aj-tree-catelog-select></span>
         </div>
    `,
    props: {
        label: { type: String, required: false },
        catalogId: { type: Number, required: false },
        selectedCatalogId: { type: Number, required: false },   // 已选中的分类 id
        noCatalog: { type: Boolean, default: false },           // 是否不需要 分类下拉
        searchFieldValue: { required: false, default: 'name' }  // 搜索哪个字段？默认为 name
    }
});