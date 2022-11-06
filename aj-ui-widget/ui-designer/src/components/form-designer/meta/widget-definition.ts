/**
 * 定义所有的组件
 */

const base_often: WidgetDefinition[] = [
    {
        name: '文字', eng_name: "Text", type: "text", icon: 'ios-barcode-outline', text: '（请替换文字）', isHtmlTag: true
    },
    {
        name: '按钮', eng_name: "Button", type: "Button", icon: 'logo-youtube', text: '按钮'
    },
    {
        name: '分割线', eng_name: "Divider", type: "divider", icon: 'ios-more'
    },
];

const form_often: WidgetDefinition[] = [
    {
        name: '文本框', eng_name: "Text Field", type: "input_text", icon: 'ios-create-outline',
    },
    {
        name: '多行文本', eng_name: "Text Area", type: "input_textarea", icon: 'ios-paper',
    },
    {
        name: '单选框', eng_name: "Radio", type: "radio", icon: 'md-radio-button-on',
    },
    {
        name: '多选框', eng_name: "CheckBox", type: "checkbox", icon: 'md-checkbox-outline',
    },
    {
        name: '日期选择器', eng_name: "DatePicker", type: "DatePicker", icon: 'ios-calendar-outline',
    },
];

const layout: WidgetDefinition[] = [
    {
        name: '栅格', eng_name: "Column", type: "Row", icon: 'ios-pause-outline',
    },
    {
        name: '表单容器', eng_name: "Form", type: "Form", icon: 'ios-list-box-outline',
    },
    {
        name: '卡片容器', eng_name: "Card", type: "card-container", icon: 'ios-albums-outline',
    },
    {
        name: 'Tab 容器', eng_name: "Tab", type: "Tabs", icon: 'ios-folder-outline',
    },
    {
        name: '折叠面板', eng_name: "Collapse", type: "table-layout", icon: 'ios-options-outline',
    },
    {
        name: 'Div 元素', eng_name: "Div", type: "div", icon: 'ios-photos-outline', isHtmlTag: true
    },
    {
        name: '带标题容器', eng_name: "Fieldset", type: "fieldset", icon: 'ios-card-outline', isHtmlTag: true
    },
    {
        name: '表格容器', eng_name: "Table", type: "table-layout", icon: 'md-grid', isHtmlTag: true
    },

];

const base: WidgetDefinition[] = base_often.concat(layout).concat([
    {
        name: '对话框', eng_name: "Modal", type: "Modal", icon: 'md-browsers', isHide: false
    },
    {
        name: '列表', eng_name: "List", type: "List", icon: 'ios-list',
    },
]);

const form: WidgetDefinition[] = JSON.parse(JSON.stringify(form_often)).concat([
    {
        name: '密码框', eng_name: "Password", type: "input_password", icon: 'ios-code-working', isHide: false
    },
    {
        name: '开关', eng_name: "Switch", type: "Switch", icon: 'ios-switch', isHide: false
    },
    {
        name: '下拉选择', eng_name: "Select", type: "Select", icon: 'md-arrow-dropdown',
    },
    {
        name: '自动完成', eng_name: "AutoCom.", type: "AutoComplete", icon: 'ios-arrow-down',
    },
    {
        name: '超文本编辑器', eng_name: "HtmlEditor", type: "HtmlEditor", icon: 'ios-paper-outline',
    },
    {
        name: '文件上传', eng_name: "FileUpload", type: "FileUpload", icon: 'ios-cloud-upload-outline',
    },
    {
        name: '时间选择器', eng_name: "TimePicker", type: "TimePicker", icon: 'ios-time-outline',
    },
    {
        name: '滑块', eng_name: "Slider", type: "Slider", icon: 'md-remove',
    },
    {
        name: '颜色选择器', eng_name: "ColorPicker", type: "ColorPicker", icon: 'ios-color-palette',
    },
    {
        name: '评分', eng_name: "Rate", type: "Rate", icon: 'md-star',
    },
]);

const adv: WidgetDefinition[] = [
    {
        name: '表格', eng_name: "Table", type: "Table", icon: 'ios-apps-outline', isHide: false
    },
    {
        name: '树形控件', eng_name: "Tree", type: "Tree", icon: 'md-git-merge',
    },
    {
        name: '走马灯', eng_name: "Carousel", type: "Carousel", icon: 'logo-buffer',
    },
    {
        name: '级联选择', eng_name: "Cascader", type: "Cascader", icon: 'md-list',
    },
];

const typeMap: {} = {}, namesMap: {} = {}, group = [], typeChineseMap = {};

const _ex = { base_often, form_often, layout, typeMap, namesMap, base, form, adv, typeChineseMap };

for (let i in _ex) {
    if (i === 'typeMap' || i === 'namesMap' || i === 'typeChineseMap')
        continue;
    let g: WidgetDefinition[] = _ex[i];

    g.forEach((item) => {
        typeMap[item.type] = item;                          // 类型为 key
        namesMap[item.name + " " + item.eng_name] = item;   // 为快速搜索用
        typeChineseMap[item.type] = item.name;              // 根据 type 找到中文名
    });
}

export default _ex;