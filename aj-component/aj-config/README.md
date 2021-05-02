
# 组件使用方式

1. 通过 Maven 引入依赖
1. 初始化
1. 安排好后台管理页面，设置 Controller 及相关权限，引入页面

# UI 界面
本模块提供完整 UI 界面给用户直接使用，无须再次开发。前端页面以 HTML 片段提供，你所需要执行的是：

1. 页面片断为 JSP，直接嵌入到你的母版页面中。使用 include 指令即可 `<%@ include file="head.jsp"%>`；
1. 因为只是 HTML 标签，还需依赖这些 JS/CSS 库：Font Awesome 图标字体库、Vue.js 2.x 和 Ajaxjs UI。

下面是库的 CDN 地址：

- Font Awesome： `<link href="https://lib.baomitu.com/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet" />`
- Vue.js 2.x `<script src="//lib.baomitu.com/vue/2.6.12/vue.min.js"></script>`
- Ajaxjs UI `<script src="//lib.baomitu.com/vue/2.6.12/vue.min.js"></script>`