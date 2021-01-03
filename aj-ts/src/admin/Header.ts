/**
 * 后台头部导航
 */
Vue.component('ajaxjs-admin-header', {
    template: `	
        <header class="ajaxjs-admin-header">
            <div>
                <slot name="btns"></slot>
                <a href="#" target="_blank">
                    <img width="12" src="data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==" /> 
                    新窗口打开
                </a>
            </div>
            <fieldset>
                <legend>
                    <slot name="title">
                        {{isCreate ? "新建":"编辑"}}{{uiName}} ：<span v-if="infoId">No.{{infoId}}</span>
                    </slot>
                </legend>
            </fieldset>
        </header>
    `,
    props: {
        isCreate: Boolean,	// true=新建/fasle=编辑
        uiName: String,	    // 实体名称
        infoId: Number		// 实体 id
    }
});