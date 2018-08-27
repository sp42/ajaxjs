
ajaxjs = aj = function(cssSelector, fn) {
return Element.prototype.$.apply(document, arguments);
}
Element.prototype.$ = function(cssSelector, fn) {
if (typeof fn == 'function') {
var children = this.querySelectorAll(cssSelector);
if (children && fn)
Array.prototype.forEach.call(children, fn);
return children;
} else {
return this.querySelector.apply(this, arguments);
}
}
Element.prototype.die = function() {
this.parentNode.removeChild(this);
}
Element.prototype.up = function(tagName, className) {
if (tagName && className)
throw '只能任选一种参数，不能同时传';
var el = this.parentNode;
tagName = tagName && tagName.toUpperCase();
while (el) {
if (tagName && el.tagName == tagName)
return el;
if (className && el.className && ~el.className.indexOf(className))
return el;
el = el.parentNode;
}
return null;
}
Element.prototype.insertAfter = function(newElement) {
var targetElement = this, parent = targetElement.parentNode;
if (parent.lastChild == targetElement)
parent.insertBefore(newElement);
else
parent.insertBefore(newElement, targetElement.nextSibling);
}
ajaxjs.apply = function(a, b) {
for ( var i in b)
a[i] = b[i];
}
Function.prototype.delegate = function() {
var self = this, scope = this.scope, args = arguments, aLength = arguments.length, fnToken = 'function';
return function() {
var bLength = arguments.length, Length = (aLength > bLength) ? aLength : bLength;
for (var i = 0; i < Length; i++)
if (arguments[i])
args[i] = arguments[i]; 
args.length = Length; 
for (var i = 0, j = args.length; i < j; i++) {
var _arg = args[i];
if (_arg && typeof _arg == fnToken && _arg.late == true)
args[i] = _arg.apply(scope || this, args);
}
return self.apply(scope || this, args);
};
};
Function.prototype.after = function(composeFn, isForceCall, scope) {
var self = this;
return function() {
var result = self.apply(scope || this, arguments);
if (isForceCall) {
return composeFn.call(this, result);
}
return result && (typeof result.pop != 'undefined') && (typeof result.pop != 'unknown') ? composeFn.apply(this, result) : composeFn.call(this, result);
};
}
ajaxjs.ua = (function() {
var ua = navigator.userAgent.toLowerCase();
if (/msie 8/.test(ua) || /msie 9/.test(ua))
alert('ie 版本太低（要求 ie >= 10）或者你处于 360 的兼容模式下，请切换到极速模式 Not support this browser!');
return {
isWebkit : /webkit/.test(ua),
isIOS : /ios/.test(ua),
isAndroid : /android/.test(ua),
isFirefox : /firefox/.test(ua)
};
})();
function tppl(tpl, data) {
var fn = function(d) {
var i, k = [], v = [];
for (i in d) {
k.push(i);
v.push(d[i]);
}
;
return (new Function(k, fn.$)).apply(d, v);
};
if (!fn.$) {
var tpls = tpl.split('[:');
fn.$ = "var $=''";
for (var t = 0; t < tpls.length; t++) {
var p = tpls[t].split(':]');
if (t != 0) {
fn.$ += '=' == p[0].charAt(0) ? "+(" + p[0].substr(1) + ")" : ";" + p[0].replace(/\r\n/g, '') + "$=$"
}
fn.$ += "+'" + p[p.length - 1].replace(/\'/g, "\\'").replace(/\r\n/g, '\\n').replace(/\n/g, '\\n').replace(/\r/g, '\\n') + "'";
}
fn.$ += ";return $;";
}
return data ? fn(data) : fn;
}
function Step() {
var steps = Array.prototype.slice.call(arguments), pending, counter, results, lock;
function next() {
counter = pending = 0;
if (steps.length === 0) {
if (arguments[0])
throw arguments[0];
return;
}
var fn = steps.shift();
results = [];
try {
lock = true;
var result = fn.apply(next, arguments);
} catch (e) {
next(e);
}
if (counter > 0 && pending == 0) {
next.apply(null, results);
} else if (result !== undefined) {
next(undefined, result);
}
lock = false;
}
next.parallel = function() {
var index = 1 + counter++;
pending++;
return function() {
pending--;
if (arguments[0]) 
results[0] = arguments[0];
results[index] = arguments[1];
if (!lock && pending === 0) 
next.apply(null, results);
};
};
next.group = function() {
var localCallback = next.parallel();
var counter = 0;
var pending = 0;
var result = [];
var error = undefined;
function check() {
if (pending === 0) 
localCallback(error, result);
}
process.nextTick(check); 
return function() {
var index = counter++;
pending++;
return function() {
pending--;
if (arguments[0]) 
error = arguments[0];
result[index] = arguments[1];
if (!lock) 
check();
};
};
};
next();
}
Step.fn = function StepFn() {
var steps = Array.prototype.slice.call(arguments);
return function() {
var args = Array.prototype.slice.call(arguments);
var toRun = [ function() {
this.apply(null, args);
} ].concat(steps);
if (typeof args[args.length - 1] === 'function') 
toRun.push(args.pop());
Step.apply(null, toRun);
}
}
ajaxjs.params = {
json2url : function(json, appendUrl) {
var params = [];
for ( var i in json)
params.push(i + '=' + json[i]);
params = params.join('&');
if (appendUrl) 
params = ~appendUrl.indexOf('?') ? appendUrl + '&' + params : appendUrl + '?' + params;
return params;
},
get : function(search, hash) {
search = search || window.location.search;
hash = hash || window.location.hash;
var fn = function(str, reg) {
if (str) {
var data = {};
str.replace(reg, function($0, $1, $2, $3) {
data[$1] = $3;
});
return data;
}
};
return {
search : fn(search, new RegExp("([^?=&]+)(=([^&]*))?", "g")) || {},
hash : fn(hash, new RegExp("([^#=&]+)(=([^&]*))?", "g")) || {}
};
}
};
if (!window.URLSearchParams) { 
URLSearchParams = function() {
}
}
ajaxjs.xhr = {
json2url : ajaxjs.params.json2url,
request : function(url, cb, args, cfg, method) {
var params = this.json2url(args), xhr = new XMLHttpRequest();
method = method.toUpperCase();
if (method == 'POST' || method == 'PUT') {
xhr.open(method, url);
} else
xhr.open(method, url + (params ? '?' + params : ''));
cb.url = url; 
xhr.onreadystatechange = this.callback.delegate(null, cb, cfg && cfg.parseContentType);
if (method == 'POST' || method == 'PUT') {
xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
xhr.send(params);
} else {
xhr.send(null);
}
},
callback : function(event, cb, parseContentType) {
if (this.readyState === 4 && this.status === 200) {
var responseText = this.responseText.trim();
try {
if (!responseText)
throw '服务端返回空的字符串!';
var data = null;
switch (parseContentType) {
case 'text':
data = this.responseText;
break;
case 'xml':
data = this.responseXML;
break;
case 'json':
default:
data = JSON.parse(responseText);
}
} catch (e) {
alert('AJAX 错误:\n' + e + '\nThe url is:' + cb.url); 
}
if (!cb)
throw '你未提供回调函数';
cb(data, this);
}
if (this.readyState === 4 && this.status == 500)
alert('服务端 500 错误！');
},
jsonp : function(url, params, cb, cfg) {
var globalMethod_Token = 'globalMethod_' + parseInt(Math.random() * (200000 - 10000 + 1) + 10000);
if (!window.$$_jsonp)
window.$$_jsonp = {};
window.$$_jsonp[globalMethod_Token] = cb;
params = params || {};
params[cfg && cfg.callBackField || 'callBack'] = '$$_jsonp.' + globalMethod_Token;
var scriptTag = document.createElement('script');
scriptTag.src = this.json2url(params, url);
document.getElementsByTagName('head')[0].appendChild(scriptTag);
},
form : function(form, cb, cfg) {
cb = cb || ajaxjs.xhr.defaultCallBack;
cfg = cfg || {};
if(!form) return;
if (typeof form == 'string')
form = aj(form);
if (!form.action)
throw 'Please fill the url in ACTION attribute.';
var method = form.getAttribute('method').toLowerCase();
cfg.method = method || cfg.method || 'post';
form.addEventListener('submit', function(e, cb, cfg) {
e.preventDefault();
var form = e.target;
var json = ajaxjs.xhr.serializeForm(form, cfg);
if (cfg && cfg.beforeSubmit && cfg.beforeSubmit(form, json) === false)
return;
if (cfg && cfg.method == 'put')
ajaxjs.xhr.put(form.action, cb, json);
else
ajaxjs.xhr.post(form.action, cb, json);
}.delegate(null, cb, cfg));
}
};
ajaxjs.xhr.get = ajaxjs.xhr.request.delegate(null, null, null, null, 'GET');
ajaxjs.xhr.post = ajaxjs.xhr.request.delegate(null, null, null, null, 'POST');
ajaxjs.xhr.put = ajaxjs.xhr.request.delegate(null, null, null, null, 'PUT');
ajaxjs.xhr.dele = ajaxjs.xhr.request.delegate(null, null, null, null, 'DELETE');
ajaxjs.xhr.serializeForm = function(form, cfg) {
var json = {};
if (window.FormData && FormData.prototype.forEach) { 
var formData = new FormData(form);
formData.forEach(function(value, name) {
if (cfg && cfg.ignoreField != name) 
json[name] = encodeURIComponent(value);
});
} else {
for (var i = 0, len = form.elements.length; i < len; i++) {
var formElement = form.elements[i], name = formElement.name, value = formElement.value;
if (formElement.name === '' || formElement.disabled || (cfg && cfg.ignoreField != name))
continue;
switch (formElement.nodeName.toLowerCase()) {
case 'input':
switch (formElement.type) {
case 'text':
case 'hidden':
case 'password':
json[name] = encodeURIComponent(value);
break;
case 'checkbox':
case 'radio':
if (formElement.checked)
json[name] = encodeURIComponent(value);
break;
}
break;
case 'textarea':
case 'select':
json[name] = encodeURIComponent(value);
break;
}
}
}
return json;
}
ajaxjs.xhr.defaultCallBack = function(json) {
if (json) {
if (json.isOk) {
ajaxjs.alert.show(json.msg || '操作成功！');
} else {
ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
}
} else {
ajaxjs.alert.show('ServerSide Error!');
}
}
ajaxjs.throttle = {
event : {},
handler : [],
init : function(fn, eventType) {
eventType = eventType || 'resize'; 
this.handler.push(fn);
if (!this.event[eventType])
this.event[eventType] = true;
(function() {
var me = arguments.callee;
window.addEventListener(eventType, function() {
window.removeEventListener(eventType, arguments.callee); 
var args = arguments;
window.setTimeout(function() {
for (var i = 0, j = ajaxjs.throttle.handler.length; i < j; i++) {
var obj = ajaxjs.throttle.handler[i];
if (typeof obj == 'function') {
obj.apply(this, args);
} else if (typeof obj.fn == 'function' && !obj.executeOnce) {
obj.fn.call(obj);
}
}
me(); 
}, 300); 
});
})();
var obj = fn;
if (typeof obj == 'function') {
obj();
} else if (typeof obj.fn == 'function' && !obj.done) {
obj.fn.call(obj);
}
}
};
ajaxjs.throttle.onEl_in_viewport = function(el, fn) {
setTimeout(function() { 
UserEvent2.onWinResizeFree({
executeOnce : false,
fn : function() {
var scrollTop = document.body.scrollTop, docHeight = window.innerHeight;
var elTop = el.getBoundingClientRect().top, isFirstPage = docHeight > elTop, isInPage = scrollTop > elTop;
if (isFirstPage || isInPage) {
this.executeOnce = true;
fn();
}
}
}, 'scroll');
}, 1500);
}
ajaxjs.throttle.onEl_in_viewport.actions = [];

Vue.component('aj-form-calendar', {
data: function() {
return {
year : 2017,
month : 2,
day : 1
};
},
props : {
},
template : 
'<div class="aj-form-calendar">\
<div class="selectYearMonth">\
<a href="#" @click="setYear(\'preYear\')" class="preYear">&lt;</a> \
<select>\
<option value="1">一月</option><option value="2">二月</option><option value="3">三月</option><option value="4">四月</option>\
<option value="5">五月</option><option value="6">六月</option><option value="7">七月</option><option value="8">八月</option>\
<option value="9">九月</option><option value="10">十月</option><option value="11">十一月</option><option value="12">十二月</option>\
</select>\
<a href="#" @click="setYear(\'nextYear\')" class="nextYear">&gt;</a>\
</div>\
<div class="showCurrentYearMonth">\
<span class="showYear">{{year}}</span>/<span class="showMonth">{{month}}</span>\
</div>\
<table>\
<thead>\
<tr><td>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr>\
</thead>\
<tbody @click="pickDay($event);"></tbody>\
</table>\
</div>',
mounted : function () {
aj.apply(this, this.getDate('now'));
this.render();
},
methods : {
render : function () {
var el = this.$el;
var arr = this.getDateArr();
this.days = [];
var frag = document.createDocumentFragment();
while (arr.length) {
var row = document.createElement("tr"); 
for (var i = 1; i <= 7; i++) { 
var cell = document.createElement("td");
if (arr.length) {
var d = arr.shift();
if (d) {
cell.innerHTML = d;
cell.className = 'day day_' + this.year + '-' + this.month + '-' + d;
cell.title = this.year + '-' + this.month + '-' + d;
this.days[d] = cell;
var on = new Date(this.year, this.month - 1, d);
if (this.isSameDay(on, this.date)) {
cell.classList.add('onToday');
this.onToday && this.onToday(cell);
}
this.selectDay && this.onSelectDay && this.isSameDay(on, this.selectDay) && this.onSelectDay(cell);
}
}
row.appendChild(cell);
}
frag.appendChild(row);
}
var tbody = el.$("table tbody");
tbody.innerHTML = '';
tbody.appendChild(frag);
this.onFinish && this.onFinish();
},
setYear : function(type) {
this.year = this.getDate(type).year;
},
nextYear : function() {
},
getDate: function (dateType) {
var now = new Date(), date, nowYear = now.getFullYear(), nowMonth = now.getMonth() + 1;
switch (dateType) {
case 'now':
date = now;
break;
case 'preMonth':
date = new Date(nowYear, nowMonth - 2, 1);
break;
case 'nextMonth':
date = new Date(nowYear, nowMonth, 1);
break;
case 'preYear':
date = new Date(nowYear - 1, nowMonth - 1, 1);
break;
case 'nextYear':
date = new Date(nowYear + 1, nowMonth - 1, 1);
break;
}
return {
date : date,
year : date.getFullYear(),
month : date.getMonth() + 1,
};
},
getDateArr: function () {
var arr = [];
for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
arr.push(0);
for (var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
arr.push(i);
return arr;
},
pickDay : function(e) {
var el = e.target, date = el.title;
this.$emit('pick-date', date);
return date;
},
isSameDay: function (d1, d2) {
return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
}
}
});
Vue.component('aj-form-calendar-input', {
data : function(){
return {
date : this.fieldValue
}
},
props : {
fieldName : { 
type: String,
required: true
},
fieldValue : {
type: String,
required: false
}
},
template : 
'<div class="aj-form-calendar-input">\
<div class="icon"><div class="menu"></div></div>\
<input placeholder="请输入日期" :name="fieldName" :value="date" type="text" />\
<aj-form-calendar @pick-date="recEvent"></aj-form-calendar>\
</div>',
methods : {
recEvent: function(date) {
this.date = date;
}
}
});
Vue.component('aj-form-html-editor', {
template : '',
props : {
fieldName : { 
type: String,
required: true
},
content : { 
type: String,
required: false
},
basePath : { 
type: String,
required: false,
default : ''
}
},
beforeCreate : function() {
var xhr = new XMLHttpRequest();
xhr.open("GET", this.ajResources.libraryUse + '/htmleditor-tag.htm', false);
xhr.send(null);
this.$options.template = xhr.responseText;
},
mounted : function() {
var el = this.$el;
this.iframeEl = el.$('iframe');
this.sourceEditor = el.$('textarea');
this.iframeWin = this.iframeEl.contentWindow;
this.mode = 'iframe'; 
this.toolbarEl = el.$('.toolbar');
this.iframeWin.onload = function() {
this.iframeDoc = this.iframeWin.document;
this.iframeDoc.designMode = 'on';
this.iframeBody = this.iframeDoc.body;
this.sourceEditor.value && this.setValue(this.sourceEditor.value);
}.bind(this);
},
methods: {
onToolBarClk : function(e) {
var el = e.target, clsName = el.className;
clsName = clsName.split(' ').shift();
switch(clsName) {
case 'createLink':
var result = prompt("请输入 URL 地址");
if(result)
this.format("createLink", result);
break;
case 'insertImage':
if(window.isCreate)
aj.alert.show('请保存记录后再上传图片。');
else {
var self = this;
App.$refs.uploadLayer.show(function(imgUrl) {
if(imgUrl)
self.format("insertImage", imgUrl);
});
}
break;
case 'switchMode':
this.setMode();
break;
default:
this.format(clsName);
}
},
format : function (type, para) {
this.iframeWin.focus();
if (!para) {
if (document.all) 
this.iframeDoc.execCommand(type);
else 
this.iframeDoc.execCommand(type, false, false);
} else 
this.iframeDoc.execCommand(type, false, para);
this.iframeWin.focus();
},
insertEl : function (html) {
this.iframeDoc.body.innerHTML = html;
},
setValue : function(v) {
var self = this;
setTimeout(function() {
self.iframeWin.document.body.innerHTML = v;
}, 500);
},
getValue : function(cfg) {
var result = this.iframeBody.innerHTML;
if(cfg && cfg.cleanWord)
result = this.cleanPaste(result);
if(cfg && cfg.encode)
result = encodeURIComponent(result);
return result;
},
cleanPaste : function(html) {
html = html.replace(/<(\/)*(\\?xml:|meta|link|span|font|del|ins|st1:|[ovwxp]:)((.|\s)*?)>/gi, ''); 
html = html.replace(/(class|style|type|start)=("(.*?)"|(\w*))/gi, ''); 
html = html.replace(/<style(.*?)style>/gi, ''); 
html = html.replace(/<script(.*?)script>/gi, ''); 
html = html.replace(/<!--(.*?)-->/gi, ''); 
return html;
},
setMode : function () {
if (this.mode == 'iframe') {
this.iframeEl.classList.add('hide');
this.sourceEditor.classList.remove('hide');
this.sourceEditor.value = this.iframeBody.innerHTML;
this.mode = 'text';
this.grayImg(true);
} else {
this.iframeEl.classList.remove('hide');
this.sourceEditor.classList.add('hide');
this.iframeBody.innerHTML = this.sourceEditor.value;
this.mode = 'iframe';
this.grayImg(false);
}
},
grayImg: function (isGray) {
this.toolbarEl.$('span', function(item) {
if(item.className.indexOf('switchMode') != -1) {
item.style.color = isGray ? 'red' : '';
} else {
item.style.filter = isGray ? 'grayscale(100%)' : '';
}
});
},
onFontfamilyChoserClk : function(e) {
var el = e.target; 
this.format('fontname', el.innerHTML);
var menuPanel = el.parentNode;
menuPanel.style.display = 'none';
setTimeout(function() {
menuPanel.style.display = '';
}, 300);
},
onFontsizeChoserClk : function(e) {	
var el = e.target;
for(var els = e.currentTarget.children, i = 0, j = els.length; i < j; i++)
if(el == els[i]) break;
this.format('fontsize', i);
},
onFontColorPicker: function(e) {
var color = e.target.title;
this.format('foreColor', color);
},
onFontBgColorPicker: function(e) {
var color = e.target.title;
this.format('backColor', color);
},
createColorPickerHTML : function() {
var cl = ['00', '33', '66', '99', 'CC', 'FF'], a, b, c, d, e, f, i, j, k, T;
var h = '<div class="colorhead"><span class="colortitle">颜色选择</span></div>\
<div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>';// 创建body [6 x 6的色盘]
for (var i = 0; i < 6; ++i) {
h += '<td><table class="colorpanel" cellspacing="0" cellpadding="0">';
for (var j = 0, a = cl[i]; j < 6; ++j) {
h += '<tr>';
for (var k = 0, c = cl[j]; k < 6; ++k) {
b = cl[k];
e = k == 5 && i != 2 && i != 5 ? ';border-right:none;' : '';
f = j == 5 && i < 3 ? ';border-bottom:none' : '';
d = '#' + a + b + c;
T = document.all ? '&nbsp;' : '';
h += '<td unselectable="on" style="background-color: ' + d + e + f + '" title="' + d + '">' + T + '</td>'; 
}
h += '</tr>';
}
h += '</table></td>';
if (cl[i] == '66') h += '</tr><tr>';
}
h += '</tr></table></div>';
return h;
}
}
});
ajaxjs.formValid = function FormValid(formEl, cfg) {
this.cfg = cfg;
formEl = typeof formEl == 'string' ? document.querySelector(formEl) : formEl;
var items = formEl.querySelectorAll('input[type=text], input[type=password], input[type=number]');
for(var i = 0 , j = items.length; i < j; i++) {
var el = items[i];
el.oninvalid = this.onInvalid.bind(this);
}
}
ajaxjs.formValid.prototype.onInvalid = function (e) {
e.preventDefault();
var el = e.target;
el.style.borderColor = 'red';
var msg = el.parentNode.querySelector('.errMsg');
if(!msg) {
msg = document.createElement('div');
msg.className = 'errMsg';
var b = el.getBoundingClientRect();
if(this.cfg && this.cfg.alignRight) { 
msg.style.top = (b.top + 5) + 'px';
var moveLeft = 0;
if(el.dataset.erruileft)
moveLeft = Number(el.dataset.erruileft);
msg.style.left = (b.left + el.clientWidth + moveLeft + 10) + 'px';
} else {
msg.style.left = b.left + 'px';
msg.style.top = (b.top + 28) + 'px';
}
el.insertAfter(msg);
}
var m;
if(el.validity.patternMismatch)
m = '该内容格式不正确';
if(el.validity.valueMissing)
m = '该内容不可为空';
msg.innerHTML = m;
setTimeout(function() {
msg.die();
el.style.borderColor = '#abadb3';
}, 3000);
}

Vue.component('aj-page-list', {
data : function() {
return {
pageSize : this.initPageSize,
total : 0,
totalPage :0,
pageStart: 0,
currentPage : 0,
result : [],
baseParam: {}
};
},
props : {
apiUrl : {	
type : String,
required : true
},
initPageSize : {
type : Number,
required : false,
default : 5
}
},
template : 
'<div class="aj-page-list">\
<ul><li v-for="(item, index) in result">\
<slot v-bind="item">\
<a href="#" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>\
</slot>\
</li></ul>\
<footer>\
<a v-if="pageStart > 0" href="#" @click="previousPage()">上一页</a> \
<a v-if="(pageStart > 0 ) && (pageStart + pageSize < total)" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\
<a v-if="pageStart + pageSize < total" href="#" @click="nextPage()">下一页</a>\
<div class="info">\
<input type="hidden" name="start" :value="pageStart" />\
页数：{{currentPage}}/{{totalPage}} 记录数：{{pageStart}}/{{total}}\
每页记录数： <input size="2" title="输入一个数字确定每页记录数" type="text" :value="pageSize" @change="onPageSizeChange($event)" />\
跳转： <select @change="jumpPageBySelect($event);">\
<option :value="n" v-for="n in totalPage">{{n}}</option>\
</select>\
</div>\
</footer>\
</div>',
mounted : function() {
ajaxjs.xhr.get(this.$props.apiUrl, function(json) {
aj.apply(this, json);
this.count();
}.bind(this), {
limit : this.pageSize
});
},
created : function(){
this.BUS.$on('base-param-change', this.onBaseParamChange.bind(this));
},
methods : {
count: function () {
var totalPage = this.total / this.pageSize, yushu = this.total % this.pageSize;
this.totalPage = parseInt(yushu == 0 ? totalPage : totalPage + 1);
this.currentPage = (this.pageStart / this.pageSize) + 1;
},
previousPage : function() {
this.pageStart -= this.pageSize;
this.currentPage = (this.pageStart / this.pageSize) + 1;
this.ajaxGet();
},
nextPage : function() {
this.pageStart += this.pageSize;
this.currentPage = (this.pageStart / this.pageSize) + 1;
this.ajaxGet();
},
onPageSizeChange : function(e) {
this.pageSize = Number(e.target.value);
this.count();
this.ajaxGet();
},
ajaxGet : function () {
var params = {};
aj.apply(params, {
start : this.pageStart, limit : this.pageSize
});
this.baseParam && aj.apply(params, this.baseParam);
ajaxjs.xhr.get(this.$props.apiUrl, function(json) {
aj.apply(this, json);
this.count();
}.bind(this), params);
},
jumpPageBySelect : function (e) {
var selectEl = e.target;
this.currentPage = selectEl.options[selectEl.selectedIndex].value;
},
onBaseParamChange : function(params) {
aj.apply(this.baseParam, params);
this.pageStart = 0; 
this.ajaxGet();
}
}
});

Vue.component('aj-accordion-menu', {
template : '<ul class="leftSidebar" @click="onClk($event);"><slot></slot></ul>',
methods : {
onClk : function (e) {
this.children = this.$el.children;
this.highlightSubItem(e);
var _btn = e.target;
if (_btn.tagName == 'H3' && _btn.parentNode.tagName == 'LI') {
_btn = _btn.parentNode;
for (var btn, i = 0, j = this.children.length; i < j; i++) {
btn = this.children[i];
var ul = btn.querySelector('ul');
if (btn == _btn) {
if (btn.className.indexOf('pressed') != -1) {
btn.classList.remove('pressed'); 
if (ul)
ul.style.height = '0px';
} else {
if (ul)
ul.style.height = ul.scrollHeight + 'px';
btn.classList.add('pressed');
}
} else {
btn.classList.remove('pressed');
if (ul)
ul.style.height = '0px';
}
}
} else {
return;
}
},
highlightSubItem : function (e) {
var li, el = e.target;
if (el.tagName == 'A' && el.getAttribute('target')) {
li = el.parentNode;
li.parentNode.$('li', function(_el) {
if (_el == li)
_el.classList.add('selected');
else
_el.classList.remove('selected');
});
}
}
}
});
Vue.component('aj-expander', {
data : function () {
return {
expended : false
}
},
props : {
openHeight : { 
type : Number,
default : 200
},
closeHeight : {	
type : Number,
default : 50
}
},
template : 
'<div class="contentPanel" :style="\'height:\' + (expended ? openHeight : closeHeight) + \'px;\'">\
<div :class="expended ? \'closeBtn\' : \'openBtn\'" @click="expended = !expended;"></div>\
<slot></slot>\
</div>'
});

document.addEventListener("DOMContentLoaded", function() {
document.body.appendChild(document.createElement('div')).className = 'alertHolder';
aj.alert = new Vue({
el : '.alertHolder',
data : {
showText : '', 
afterClose : null,	
showOk : false,
showYes : false,
showNo : false
},
template : 
'<div class="modal hide" @click="close($event);">\
<div>\
{{showText}}\
<div>\
<button v-show="showOk" @click="onBtnClk($event)" class="ok">确定</button>\
<button v-show="showYes" @click="onBtnClk($event)" class="yes">是</button>\
<button v-show="showNo" @click="onBtnClk($event)" class="no">否</button>\
</div>\
</div>\
</div>',
methods : {
show : function(text, cfg) {
this.showText = text;
this.$el.classList.remove('hide');
aj.apply(this, cfg);
return this;
},
close : function(e) {
var div = e.target; 
if (div && div.className.indexOf('modal') != -1) {
this.$el.classList.add('hide');
this.afterClose && this.afterClose(div, this);
}
},
onBtnClk : function(e) {
var el = e.target;
switch(el.className) {
case 'ok':
this.onOkClk && this.onOkClk(e, this);
break;
case 'no':
this.onNoClk && this.onNoClk(e, this);
break;
case 'yes':
this.onYesClk && this.onYesClk(e, this);
break;
}
},
}
});
aj.showOk = function(text, callback) {
var alertObj = aj.alert.show(text, {
showYes : false,
showNo :false,
showOk :true,
onOkClk : function(e) { 
alertObj.$el.classList.add('hide');
callback && callback();
}
});
}
aj.showConfirm = function(text, callback) {
var alertObj = aj.alert.show(text, {
showYes : true,
showNo :true,
showOk :false,
onYesClk : function(e) {
alertObj.$el.classList.add('hide');
callback && callback();
},
onNoClk : function(e) { 
alertObj.$el.classList.add('hide');
}
});
}
document.body.appendChild(document.createElement('div')).className = 'msgHolder';
aj.msg = new Vue({
el : '.msgHolder',
data : {
showText : '' 
},
template : '<div class="topMsg">{{showText}}</div>',
methods : {
show : function(text, cfg) {
this.showText = text;
var el = this.$el;
setTimeout(function() {
el.classList.remove('fadeOut');
el.classList.add('fadeIn');
}, 0);
setTimeout(function() { 
el.classList.remove('fadeIn');
el.classList.add('fadeOut');
cfg && cfg.afterClose && cfg.afterClose(div, this);
}, cfg && cfg.showTime || 3000);
}
}
});
});
Vue.component('aj-layer', {
template : '<div class="modal hide" @click="close($event);"><div><slot></slot></div></div>',
methods : {
show : function(cfg) {
this.$el.classList.remove('hide');
if(cfg && cfg.afterClose)
this.afterClose = cfg.afterClose;
},
close : function(e) {
aj.alert.$options.methods.close.apply(this, arguments);
}
}
});

Vue.component('aj-page-fullscreen-loading-indicator', {
template : '<div class="aj-fullscreen-loading"></div>',
beforeCreate : function () {
document.onreadystatechange = function () {
if(document.readyState === "complete") {
aj(".aj-fullscreen-loading").classList.add('fadeOut');
}
}
}
});
Vue.component('aj-page-captcha', {
props : {
imgSrc : {
type: String, 
required: true,
},
fieldName : {	
type: String,
required: false,
default : 'captcha'
}
},
template : 
'<table><tr>\
<td><input type="text" :name="fieldName" placeholder="输入右侧验证码" data-regexp="integer" required /></td>\
<td style="vertical-align: top;">\
<img :src="imgSrc" @click="onClk($event);" title="点击刷新图片" />\
</td>\
</tr></table>',
methods : {
onClk : function(e) {
var img = e.target;
img.src = img.src.replace(/\?\d+$/, '') + '?' + new Date().valueOf();
}
}
});
Vue.component('aj-page-share', {
template : 
'<div class="aj-page-share">\
分享到 &nbsp;&nbsp;\
<a title="转发至QQ空间" :href="\'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=\' + url" target="_blank">\
<img src="http://static.youku.com/v1.0.0691/v/img/ico_Qzone.gif" /></a>\
<a title="转发至新浪微博" :href="\'http://v.t.sina.com.cn/share/share.php?appkey=2684493555&Uid=&source=&sourceUrl=&url=\' + url + \'&title=\' + title" target="_blank">\
<img src="http://static.youku.com/v1.0.0691/v/img/ico_sina.gif" /></a>\
<a title="分享到腾讯朋友" :href="\'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?to=pengyou&url=\' + url + \'&title=\' + title" target="_blank">\
<img src="http://static.youku.com/v1.0.0691/v/img/ico_pengyou.png" /></a>\
<a title="推荐到豆瓣" :href="\'http://www.douban.com/recommend/?url=\' + url + \'&title=\' + title" target="_blank">\
<img src="http://static.youku.com/v1.0.0691/v/img/ico_dou_16x16.png" /></a>\
</div>',
computed : {
title : function() {
return encodeURIComponent(document.title);
},
url : function() {
return document.location.href;
}
}
});
Vue.component('aj-adjust-font-size', {
props : {
articleTarget : { 
type: String,
required: false,
default : 'article p'
}
},
template : 
'<div class="aj-adjust-font-size" @click="onClk($event);">\
<span>字体大小</span>\
<ul>\
<li><label><input type="radio" name="fontSize" /> 小</label></li>\
<li><label><input type="radio" name="fontSize" /> 中</label></li>\
<li><label><input type="radio" name="fontSize" /> 大</label></li>\
</ul>\
</div>',
methods : {
onClk : function(e) {
var el = e.target, target = el.innerHTML;
if(el.tagName != 'LABEL')
el = el.up('label');
if(el.innerHTML.indexOf('大') != -1) {
this.setFontSize('12pt');
}else if(el.innerHTML.indexOf('中') != -1) {
this.setFontSize('10.5pt');
}else if(el.innerHTML.indexOf('小') != -1) {
this.setFontSize('9pt');
}
},
setFontSize : function (fontSize) {
aj(this.$props.articleTarget, function(p){
p.style.fontSize = fontSize;
});
}
}
});
Vue.component('aj-misc-function', {
props : {
articleTarget : { 
type: String,
required: false,
default : 'article'
}
},
template : 
'<div class="aj-misc-function">\
<a href="javascript:printContent();"><span style="font-size:1rem;">&#12958;</span>打 印</a>\
<a href="javascript:sendMail_onClick();"><span style="font-size:1rem;">&#9993;</span>发送邮件</a>\
<a href="javascript:;"><span style="font-size:1.2rem;">★ </span>收 藏</a>\
</div>',
methods : {
printContent : function () {
var printHTML = "<html><head><title></title><style>body{padding:2%};</style></head><body>";
printHTML += aj('article').innerHTML;
printHTML += "</body></html>";
var oldstr = document.body.innerHTML;
document.body.innerHTML = printHTML;
window.print();
document.body.innerHTML = oldstr;
},
sendMail : function () {
location.href = 'mailto:xxx@tagzine.com?subject= '
+ document.title
+ '&body=\u6211\u5411\u4F60\u63A8\u8350\u8FD9\u6761\u6587\u7AE0\uFF0C\u5E0C\u671B\u4F60\u559C\u6B22\uFF01\u6587\u7AE0\u6807\u9898\uFF1A'
+ document.title
+ '\u3002\u8BF7\u70B9\u51FB\u67E5\u770B\uFF1A ' + location.href;
}
}
});
Vue.component('aj-article-body', {
props : {
title : { 
type: String,
required: true
},
createDate : String,
content : { 
type: String,
required: true
},
neighbor : { 
type: Object,
default : function(){
return {};
},
required: false
}
},
template : 
'<div class="aj-article-body">\
<article>\
<h3>{{title}}</h3>\
<h4>{{createDate}}</h4>\
{{content}}\
<slot></slot>\
</article>\
<div>\
<a :href="neighbor.pervInfo.url" v-if="neighbor.pervInfo">上则记录：{{neighbor.pervInfo.name}}</a>\
<a :href="neighbor.nextInfo.url" v-if="neighbor.nextInfo">下则记录：{{neighbor.nextInfo.name}}</a>\
</div>\
<aj-misc-function></aj-misc-function><aj-adjust-font-size></aj-adjust-font-size><aj-page-share></aj-page-share>\
</div>'
});
Vue.component('aj-baidu-search', {
props : ['siteDomainName'],
template : 
'<div class="aj-baidu-search"><form method="GET" action="http://www.baidu.com/baidu" onsubmit="//return g(this);">\
<input type="text" name="word" placeholder="请输入搜索之关键字" />\
<input name="tn" value="bds" type="hidden" />\
<input name="cl" value="3" type="hidden" />\
<input name="ct" value="2097152" type="hidden" />\
<input name="si" :value="getSiteDomainName" type="hidden" />\
<div class="searchBtn" onclick="this.parentNode.submit();"></div>\
</form></div>',
computed : {
getSiteDomainName : function() {
return this.$props.siteDomainName || location.host || document.domain;
}
}
});
Vue.component('aj-chinese-switch', {
props : {
jsurl : { 
type: String,
required: true
}
},
template : 
'<span>\
<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>\
/<a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>\
</span>',
created: function() {
document.body.appendChild(document.createElement('script')).src = this.$props.jsurl;
}
});

;(function() {
ajaxjs.tree = function() {
this.initData();
};
ajaxjs.tree.prototype = {
initData : function() {
this.stack = [];
this.tree = {};
},
makeTree : function (jsonArray) {
if(ajaxjs.ua.isWebkit) {
for (var i = 0; i < jsonArray.length; i++) {
jsonArray[i].oldIndex = i;
}
}
jsonArray.sort(sortByPid);
for (var i = 0, j = jsonArray.length; i < j; i++) {
var n = jsonArray[i];
var parentNode = findParent(this.tree, n.pid);
if (parentNode == null) { 
this.tree[n.id] = { 
name : n.name,
pid : n.pid
};	
} else { 
var obj = {};
obj[n.id] = {
name : n.name,
pid : n.pid
};
if (!parentNode.children)
parentNode.children = [];
parentNode.children.push(obj);
}
}
},
output : function (map, cb) {
this.stack.push(map);
for (var i in map) {
map[i].level = this.stack.length;
cb(map[i], i);
var c = map[i].children;
if (c) {
for (var q = 0, p = c.length; q < p; q++) 
this.output(c[q], cb);
}
}
this.stack.pop();
}
}
var findParent = function (map, id) {
for ( var i in map) {
if (i == id)
return map[i];
var c = map[i].children;
if (c) {
for (var q = 0, p = c.length; q < p; q++) {
var result = arguments.callee(c[q], id);
if (result != null)
return result;
}
}
}
return null;
}
var sortByPid = ajaxjs.ua.isWebkit ? function (a, b) {
return [a.a, a.b] > [b.a, b.b] ? 1:-1;
} : function (a, b) {
return a.pid > b.pid;
}
ajaxjs.tree.selectUI = function() {
ajaxjs.tree.call(this);
this.renderer = function (json, select, selectedId, cfg) {
this.makeTree(json);
if(cfg) {
if(cfg.makeAllOption) {
var option = document.createElement('option');
option.value = option.innerHTML = "全部分类";
select.appendChild(option);
}
}
var temp = document.createDocumentFragment();
this.output(this.tree, function(node, nodeId) {
var option = document.createElement('option'); 
option.value = nodeId;
if(selectedId && selectedId == nodeId) { 
option.selected = true;
}
option.dataset['pid'] = node.pid;
option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
temp.appendChild(option);
});
select.appendChild(temp);
}
}
ajaxjs.tree.selectUI.prototype = ajaxjs.tree.prototype;
})();
Vue.component('aj-tree-catelog-select', {
props : {
catelogId : { 
type: Number,
required: true
},
selectedCatelogId : {	
type: Number,
required: false
},
fieldName : { 
type: String,
required: false
},
isAutoJump : Boolean 
},
template : '<select :name="fieldName" @change="onSelected($event);" class="aj-tree-catelog-select ajaxjs-select" style="width: 200px;"></select>',
mounted : function() {
aj.xhr.get(this.ajResources.ctx + "/admin/catelog/getListAndSubByParentId", this.load.bind(this), {parentId : this.catelogId});
},
methods : {
load : function(json) {
var catalogArr = json.result;
var selectUI = new ajaxjs.tree.selectUI();
var select = aj('select');
selectUI.renderer(catalogArr, select, this.selectedCatelogId, {makeAllOption : false});
},
onSelected : function(e) {
if(this.isAutoJump) {
var el = e.target, catalogId = el.selectedOptions[0].value;
location.assign('?catalogId=' + catalogId);
} else {
this.BUS.$emit('aj-tree-catelog-select-change', e, this);
}
}
}
});

Vue.component('ajaxjs-file-upload', {
data : function() {
return {
isFileSize : false,	
isExtName : false,	
errMsg : null,	
newlyId : null	
};
},
props : {
fieldName : {
type: String,
required: true
},
filedId : {
type: Number,
required: false
},
},
template : 
'<div class="ajaxjs-img-upload-perview">\
<div class="pseudoFilePicker">\
<input type="hidden" :name="fieldName" :value="newlyId || filedId" />\
<label for="input_file_molding"><div><div>+</div>点击选择图片</div></label>\
</div>\
<div v-if="!isFileSize || !isExtName">{{errMsg}}</div>\
<div v-if="isFileSize && isExtName">\
<button onclick="aj(\'form[target=upframe]\').submit();return false;">上传</button>\
</div>\
</div>'
});
Vue.component('ajaxjs-img-upload-perview', {
data : function() {
return {
imgBase64Str : null, 
isFileSize : false,	
isExtName : false,	
isImgSize : false, 
isFileTypeCheck : false, 
errMsg : null,	
imgNewlyId : null	
};
},
props : {
imgPlace : String, 
imgName : {
type: String,
required: false 
},
imgId : {
type: Number,
required: false
},
limit : {
type : Object,
required : false,
default : function(){
return { 
maxSize : 600,
fileExt: /png|gif|jpg|jpeg/i,
maxWidth: 1200,
maxHeight:1680
};
}
},
labelId : {
type : String,
required : false,
default : 'input_file_molding'
}
},
template : 
'<div class="ajaxjs-img-upload-perview">\
<div>\
<img class="upload_img_perview" :src="(isFileSize && isExtName && isImgSize && isFileTypeCheck && imgBase64Str) ? imgBase64Str : imgPlace" />\
<input v-if="imgName" type="hidden" :name="imgName" :value="imgNewlyId || imgId" />\
</div>\
<div class="pseudoFilePicker">\
<label :for="labelId"><div><div>+</div>点击选择图片</div></label>\
</div>\
<div v-if="isShowErrMessage()">{{errMsg}}</div>\
<div v-if="isFileSize && isExtName && isImgSize && isFileTypeCheck && imgBase64Str">\
<button @click.prevent="doUpload($event);" style="padding: .4em 1.3em; width: 80px;">上传</button>\
</div>\
</div>',
created : function(){
this.BUS.$on('upload-file-selected', this.onUploadInputChange);
},
methods : (function () {
var imgHeader = {
"jpeg" : "/9j/4",
"gif" : "R0lGOD",
"png" : "iVBORw"
};
return {
onUploadInputChange : function(e) {
var fileInput = e.target;
for(var i = 0, j = fileInput.files.length; i < j; i++) {
var reader = new FileReader(), fileObj = fileInput.files[i];
reader.onload = this.afterLoad.delegate(null, fileObj, this);
reader.readAsDataURL(fileObj);
}
},
afterLoad : function (e, fileObj, self) {
var imgBase64Str = e.target.result;
var isOk = self.checkFile(fileObj, imgBase64Str, self);
self.imgBase64Str = imgBase64Str;
},
isShowErrMessage : function() {
return !this.isFileSize || !this.isExtName || !this.isImgSize || !this.isFileTypeCheck;
},
checkFile : function (fileObj, imgBase64Str, cfg) {
var defaultLimit = cfg.limit;
if(fileObj.size > defaultLimit.maxSize * 1024) { 
cfg.isFileSize = false;
cfg.errMsg = "要上传的文件容量过大，请压缩到 " + defaultLimit.maxSize + "kb 以下";
return;
} else {
cfg.isFileSize = true;
}
var ext = fileObj.name.split('.').pop();
if (!defaultLimit.fileExt.test(ext)) {
cfg.isExtName = false;
cfg.errMsg = '根据文件后缀名判断，此文件不是图片'; 
return;
} else {
cfg.isExtName = true;
}
var imgEl = new Image();
imgEl.onload = function() {
if (imgEl.width > cfg.maxWidth || imgEl.height > cfg.maxHeight) {
cfg.isImgSize = false;
cfg.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
} else {
cfg.isImgSize = true;
}
}
imgEl.src = imgBase64Str;
cfg.isFileTypeCheck = false;
for ( var i in imgHeader) {
if (~imgBase64Str.indexOf(imgHeader[i])){
cfg.isFileTypeCheck = true;
return;
}
}
cfg.errMsg = "亲，改了扩展名我还能认得你不是图片哦";
},
doUpload : function(e) {
var form = this.$parent.$refs.uploadIframe && this.$parent.$refs.uploadIframe.$el;
if(!form) {
form = this.$parent.$el.$('form');
}
form && form.submit();
e.preventDefault();
return false;
}
};
})()
});
Vue.component('ajaxjs-fileupload-iframe', {
data : function() {
return {
radomId : Math.round(Math.random() * 1000),
uploadOk_callback: function(){}
};
},
props : {
uploadUrl : {
type: String,
required: true
},
labelId : {
type : String,
required : false,
default : 'input_file_molding'
}
},
template : 
'<form :action="uploadUrl" method="POST" enctype="multipart/form-data" :target="\'upframe_\' + radomId">\
<input name="fileInput" :id="labelId" type="file" multiple="multiple" class="hide" @change="fireUploadFileSelected($event)" />\
<iframe :name="\'upframe_\' + radomId" class="hide" @load="iframe_callback($event);"></iframe>\
</form>',
methods : {
iframe_callback: function (e) { 
var json = e.target.contentDocument.body.innerText;
if(json[0] == '{') {
json = JSON.parse(json);
if(json.isOk) {
aj.msg.show('上传成功！');
if(this.uploadOk_callback && typeof this.uploadOk_callback == 'function') {
var imgUrl = json.imgUrl;
this.uploadOk_callback(imgUrl, json);
}
}
}
},
fireUploadFileSelected : function(e) {
var p = this.$parent;
while(p && p.$refs && !p.$refs.uploadControl) {
p = p.$parent;
}
p.$refs.uploadControl.onUploadInputChange(e);
}
}
});
Vue.component('aj-popup-upload', {
data : function() {
return {
text : {}
};
},
props : {
uploadUrl : {
type: String,
required: true
},
imgName : {
type: String,
required: false
},
imgId : {
type: Number,
required: false
},
imgPlace : String 
},
template : 
'<aj-layer>\
<h3>图片上传</h3>\
<ajaxjs-img-upload-perview style="width:420px;" :img-name="imgName" :img-id="imgId" :img-place="imgPlace" label-id="foo1" ref="uploadControl"></ajaxjs-img-upload-perview>\
<ajaxjs-fileupload-iframe :upload-url="uploadUrl" label-id="foo1" ref="uploadIframe"></ajaxjs-fileupload-iframe>\
<div>上传限制：{{text.maxSize}}kb 或以下，分辨率：{{text.maxHeight}}x{{text.maxWidth}}</div>\
</aj-layer>',
mounted: function() {
this.text = this.$refs.uploadControl.$options.props.limit.default();
},
methods : {
show : function(callback) {
if(callback)
this.$refs.uploadIframe.uploadOk_callback = callback;
this.$children[0].show();
}
}
});

Vue.component('ajaxjs-admin-header', {
props : {
isCreate : Boolean,	
uiName : String,	
infoId : {	
type: Number,
required: false
}
},
template : 
'<header class="ajaxjs-admin-header">\
<div>\
<slot name="btns"></slot>\
<a href="#" target="_blank"><img width="12" src="data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==" /> 新窗口打开</a>\
</div>\
<fieldset>\
<legend><slot name="title">{{isCreate ? "新建":"编辑"}}{{uiName}} ：<span v-if="infoId">No.{{infoId}}</span></slot></legend>\
</fieldset>\
</header>'
})
Vue.component('ajaxjs-admin-info-btns', {
props : {
isCreate : Boolean 
},
template : 
'<div class="ajaxjs-admin-info-btns">\
<button><img :src="ajResources.commonAsset + \'/icon/save.gif\'" /> {{isCreate ? "新建":"编辑"}}</button>\
<button onclick="this.up(\'form\').reset();return false;">复 位</button>\
<button v-if="!isCreate" v-on:click.prevent="del()">\
<img :src="ajResources.commonAsset + \'/icon/delete.gif\'" /> 删 除\
</button><slot></slot>\
</div>',
methods : {
del : function () {
if (confirm('确定删除？'))
ajaxjs.xhr.dele('.', function(json) {
if (json && json.isOk) {
alert(json.msg);
location.assign('../list/');
}
});
}
}
});
Vue.component('aj-admin-filter-panel', {
props : {
label : {
type : String,
required : false
},
catelogId :{	
type: Number,
required: true
},
selectedCatelogId :{ 
type: Number,
required: false
}
},
template: 
'<div class="aj-admin-filter-panel">\
<form action="?" method="GET">\
<input type="hidden" name="searchField" value="content" />\
<input type="text" name="searchValue" placeholder="请输入正文之关键字" style="float: inherit;" class="ajaxjs-inputField" />\
<button style="margin-top: 0;" class="ajaxjs-btn">搜索</button>\
</form>\
{{label||\'分类\'}}：<aj-tree-catelog-select :is-auto-jump="true" :catelog-id="catelogId" :selected-catelog-id="selectedCatelogId"></aj-tree-catelog-select>\
</div>'
});
aj.admin = {
del : function(id, title) {
if (confirm('请确定删除记录：\n' + title + ' ？')) {
ajaxjs.xhr.dele('../' + id + '/', function(json) {
if (json.isOk) {
alert('删除成功！');
location.reload();
}
});
}
},
setStatus : function(id, status) {
ajaxjs.xhr.post('../setStatus/' + id + '/', function(json) {
if (json.isOk) {
}
}, {
status : status
});
}
};
