<%@page pageEncoding="UTF-8"%>
<h4>ajaxjs-base.js</h4>
<p>全局变量是 ajaxjs 或者 aj，两者等价，后者是前者的简写方式。</p>
<pre class="prettyprint">
源码目录
- js  // js 源码
    -- ajaxjs-base.js # 基础库
    -- widgets 组件目录
        --- msg.js
        --- ……（其他组件）
- less // LESS 样式库
    -- common_style.less 样式函数库
    -- public.less 通用样式
    -- admin.less 后台用样式
    -- moblie.less 移动端使用样式
    -- widgets 组件目录
        --- msg.less
        --- ……（其他组件样式）
-- output// 生产环境用
    -- all.css less 编译后的样式结果，所有的样式，已压缩
    -- all.js 所有 js，，已压缩
</pre>

<p>一个 js/less 文件可能包含多个组件，但 js 与 less 之间总是对应着的，命名也是。</p>


<h4>查找元素</h4>
<p>没有使用 jQ，而是使用原生 document.querySelector()，然后封装一下。</p>

<pre class="prettyprint">ajaxjs = aj = function(cssSelector, fn) {
    return Element.prototype.$.apply(document, arguments);
}</pre>
<ul>
	<li><p>参数 cssSelector：String，必填，CSS 选择器</p></li>
	<li><p>参数 fn : Function，可选，送入该参数表示使用 querySelectorAll 查询多个 dom 元素，故这是个遍历器函数，参数列表如 item、index、array</p></li>
</ul>

<p>例子：</p>
<pre class="prettyprint">var div  = aj('.foo); // 返回符合 className=foo 的元素，单个元素
var divs = aj('div', function(item, index){...}); // 遍历所有的 div 元素，并返回该 div 集合。</pre>


<h4>对子元素查找</h4>
<p>使用 $() 方法，参数列表跟上个方法一样。例子：</p>

<pre class="prettyprint">div.$('a').onclick = fn;</pre>



<h4>DOM 操作</h4>
<p>都是修改 Element 原型对象，添加方法。</p>

<pre class="prettyprint">Element.prototype.die(); // 删除元素自己
Element.prototype.up = function(tagName : String, className: String); // 查找父元素，支持 标签名称 或 样式名称，任选其一而不能同时传。
Element.prototype.insertAfter = function(newElement : Element); // 在当前元素后面追加 newElement</pre>


<h4>函数式风格应用</h4>
<p>函数委托，参见<a href="http://blog.csdn.net/zhangxin09/article/details/8508128">详细教程</a>。</p>

<pre class="prettyprint">Function.prototype.delegate(args ...); // 可以预先指定函数的参数
Function.prototype.after = function(composeFn : Function, isForceCall : Boolean, scope : Object) {...}; // 设置一个后置函数</pre>


<h4>XHR</h4>
<p>XHR 就是 Ajax 异步请求。REST API 支持提供下面方法：</p>

<pre class="prettyprint">aj.xhr.get (url : String, callback : Function, queryParams: Object); // GET 请求
aj.xhr.post(url : String, callback : Function, queryParams: Object); // POST 表单请求
aj.xhr.put (url : String, callback : Function, queryParams: Object); // PUT 表单请求
aj.xhr.dele(url : String, callback : Function, queryParams: Object); // DLETE 请求</pre>


<p>其中回调函数第一个参数为 JSON 或 HTML 文本，第二个参数为 XHR 原生对象。</p>

<h4>表单提交</h4>
<p>对于表单，可直接使用 aj.xhr.form(formEl) 绑定表单实现 ajax 提交。该方法自动绑定表单，url 地址取决于 Form 的 action 元素，方法取决于 method 元素（这是尊重 HTML 标签的体现）。例如，</p>

<pre class="prettyprint">
ajaxjs.xhr.form(aj('form')); // 默认的响应信息

// 或者自定义提交后的回调
ajaxjs.xhr.form(aj('form'), function(json) {
    if(json.isOk)
        location.assign(json.newlyId);
});

// 加入提交前的拦截器
ajaxjs.xhr.form(aj('form'), cb, {
    beforeSubmit : function(form, json) {... return true/fasle;} // 返回 false 阻止提交表单
});</pre>


<p>虽然使用了 FormData 收集表单数据，但是 POST 和 PUT 都是一般的 x-www-form-urlencoded 表单请求，而不是 H5 的 FormData（文件上传的那种）。提交的时候会进行 encodeURIComponent() 编码。</p>

<p>下面返回 json 对象，你可以指定 cfg.ignoreField 忽略某个字段。</p>

<pre class="prettyprint">ajaxjs.xhr.serializeForm = function(form, cfg);</pre>