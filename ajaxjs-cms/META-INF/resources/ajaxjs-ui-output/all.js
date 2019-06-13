// build date:Wed Jun 12 08:45:22 GMT+08:00 2019

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
return a;
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
}
Function.prototype.after = function(composeFn, isForceCall, scope) {
var self = this;
return function() {
var result = self.apply(scope || this, arguments);
if (isForceCall) {
return composeFn.call(this, result);
}
return result && (typeof result.pop != 'undefined')&& (typeof result.pop != 'unknown') ? composeFn.apply(this, result) : composeFn.call(this, result);
};
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
try{
data = JSON.parse(responseText);	
} catch(e) {
try{
data = eval("TEMP_VAR = " + responseText); 
} catch(e) {
throw e;
}
}
}
} catch (e) {
alert('XHR 错误:\n' + e + '\nUrl is:' + cb.url); 
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
var method;
if(form.getAttribute('method'))
method = form.getAttribute('method').toLowerCase();
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
if (formElement.name === '' || formElement.disabled || (cfg && cfg.ignoreField == name))
continue;
switch (formElement.nodeName.toLowerCase()) {
case 'input':
switch (formElement.type) {
case 'number':
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
tppl = function(tpl, data){
var fn = function(d) {
var i, k = [], v = [];
for (i in d) {
k.push(i);
v.push(d[i]);
};
return (new Function(k, fn.$)).apply(d, v);
};
if(!fn.$){
var tpls = tpl.split('[:');
fn.$ = "var $=''";
for(var t = 0;t < tpls.length;t++){
var p = tpls[t].split(':]');
if(t!=0){
fn.$ += '='==p[0].charAt(0)
? "+("+p[0].substr(1)+")"
: ";"+p[0].replace(/\r\n/g, '')+"$=$"
}
fn.$ += "+'"+p[p.length-1].replace(/\'/g,"\\'").replace(/\r\n/g, '\\n').replace(/\n/g, '\\n').replace(/\r/g, '\\n')+"'";
}
fn.$ += ";return $;";
}
return data ? fn(data) : fn;
}
ajaxjs.tab = function(cfg) {
ajaxjs.apply(this, cfg);
var mover = this.el.querySelector(this.moverTagName || 'div');
var children = mover.children, len = children.length;
var stepWidth = this.stepWidth || mover.parentNode.clientWidth || window.innerWidth; 
if(this.isMagic) 
mover.style.width = this.isUsePx ? (stepWidth * 2) +'px' : '200%';
else
mover.style.width = this.isUsePx ? (stepWidth * len) +'px' : len + '00%';
var tabWidth = this.isUsePx ? stepWidth + 'px' : (1 / len * 100).toFixed(5) + '%';
for(var i = 0; i < len; i++) 
children[i].style.width = this.isMagic ? '50%' : tabWidth;
this.isUsePx && ajaxjs.throttle.init(onResize.bind(this));
this.tabHeader = this.el.$('header');
if (this.tabHeader && !this.disableTabHeaderJump)
this.tabHeader.onclick = onTabHeaderPress.bind(this);
var isWebkit = navigator.userAgent.toLowerCase().indexOf('webkit') != -1;
this.go = function(i) {
if(this.isGetCurrentHeight) {
for(var p = 0; p < len; p++) {
if(i == p) {
this.children[p].style.height = 'initial';	
}else{
this.children[p].style.height = '1px';	
}
}
}
if(this.isMagic) {
for(var p = 0; p < len; p++) {
if(this.currentIndex == p) {
continue;
}else if(i == p) {
children[p].classList.remove('hide');
}else {	
children[p].classList.add('hide');
}
}
var cssText = i > this.currentIndex
? 'translate3d({0}, 0px, 0px)'.replace('{0}', '-50%')
: 'translate3d({0}, 0px, 0px)'.replace('{0}', '0%');
mover.style.webkitTransition = '-webkit-transform 400ms linear';
mover.style.webkitTransform = cssText;
}else{
var leftValue = this.isUsePx ? ('-' + (i * stepWidth) + 'px') : ('-' + (1 / len * 100 * i).toFixed(2) + '%');
mover.style[isWebkit ? 'webkitTransform' : 'transform'] = 'translate3d({0}, 0px, 0px)'.replace('{0}', leftValue);
}
this.currentIndex = i;
this.onItemSwitch && this.onItemSwitch.call(this, i, children[i]);
}
this.goPrevious = function() {
this.currentIndex--;
if (this.currentIndex < 0)this.currentIndex = len - 1;
this.go(this.currentIndex); 
}
this.goNext = function() {
this.currentIndex++;
if (this.currentIndex == len)this.currentIndex = 0; 
this.go(this.currentIndex);
}
this.loop = function() {
if(this.isEnableLoop)
this.loopTimer = window.setInterval(this.goNext.bind(this), this.autoLoop);
}
this.goTab = function(index) {
onTabHeaderPress.call(this, {
target : this.el.querySelectorAll('header ul li')[index]
});
}
function onResize() {
var stepWidth = mover.parentNode.clientWidth; 
mover.style.width = this.isUsePx ? (stepWidth * len) +'px' : len + '00%';
for(var i = 0; i < len; i++) 
children[i].style.width = stepWidth + 'px';
}
function onTabHeaderPress(e) {
var target = e.target,
li = target.tagName == 'LI' ? target : target.up('li');
if(!li)return;
var arr = this.tabHeader.$('ul').children, index;
for(var i = 0, j = arr.length; i < j; i++) {
if(li == arr[i]) {
arr[i].classList.add('active');
index = i;
} else
arr[i].classList.remove('active');
}
this.go(index);
this.currentIndex = index;
var nextItem = children[index]; 
this.onTabHeaderSwitch && this.onTabHeaderSwitch.call(this, li, nextItem, target, index);
autoHeight.call(this, nextItem);
}
function autoHeight(nextItem) {
if(this.autoHeight) {
var tabHeaderHeight = 0;
if(this.tabHeader) 
tabHeaderHeight = this.tabHeader.scrollHeight;
this.el.style.height = (nextItem.scrollHeight + tabHeaderHeight + 50) + 'px'; 
}
}
}
ajaxjs.tab.prototype = {
isMagic : false,
isUsePx : false,
autoHeight : false,
disableTabHeaderJump : false, 
isGetCurrentHeigh : true,
currentIndex : 0,
autoLoop : 4000,
initFirstTab : function() {
this.goTab(0);
}
};
ajaxjs.banner = function(cfg) {
cfg.isUsePx = true; 
cfg.isEnableLoop = true;
cfg.isGetCurrentHeight = false;
ajaxjs.tab.call(this, cfg);
this.initIndicator = function() {
var ol = this.el.$('ol');
this.onItemSwitch = function(index) {
if (ol) ol.$('li', function(li, i) {
if(index == i) 
li.classList.add('active');
else
li.classList.remove('active');
});
}
var self = this;
ol.onclick = function(e) {
var el = e.target;
if(el.tagName != 'LI')return;
if (ol) ol.$('li', function(li, i) {
if(el == li) {
self.go(i);
return;
}
});
}
}
this.loop();
this.initIndicator();
}
ajaxjs.banner.prototype = ajaxjs.tab.prototype;
ajaxjs.list = function(cfg) {
ajaxjs.apply(this, cfg);
var args = {};
args.start = 0;
args.limit = 10;
this.baseParam && ajaxjs.apply(args, this.baseParam);
if (!this.jsonInit)
this.jsonInit = function(json) { 
if (json) {
if (!json.isOk) 
ajaxjs.alert(json.msg || '执行失败！原因未知！');
} else {
ajaxjs.alert('ServerSide Error!');
}
return {
data : json.result,
total : json.total
};
}
this.load = function() {
ajaxjs.xhr.get(this.url, function(json) {
var result = this.jsonInit(json);
var data = result.data;
renderer.call(this, data);
args.start += args.limit;
this.afterDataLoad && typeof this.afterDataLoad == 'function' && this.afterDataLoad.call(this, result);
}.bind(this), args);
}
function renderer(data) {
if (!data || data.length <= 0)
return; 
var lis = [];
for (var i = 0; i < data.length; i++) {
var _data = this.renderer ? this.renderer(data[i]) : data[i]; 
if (_data === false)
continue; 
var li = tppl(this.tpl, _data);
if (this.isAppend) { 
var loadingIndicator = this.el.$('.loadingIndicator');
if (loadingIndicator) 
this.el.removeChild(loadingIndicator);
var temp = document.createElement('div');
temp.innerHTML = li;
this.el.appendChild(temp.$('li'));
temp = null;
} else {
lis.push(li);
}
}
if (!this.isAppend)
this.el.innerHTML = lis.join('');
}
function getCellRequestWidth() {
window.devicePixelRatio = window.devicePixelRatio || 1;
var screenWidth = window.innerWidth; 
var columns = 3; 
var cellWidth = screenWidth * ( 1 / columns );
var cellHight = cellWidth / (4 / 3); 
var reqeustWidth = cellWidth * window.devicePixelRatio;
reqeustWidth = Math.floor(reqeustWidth);
var MaxWidth = 500;
return reqeustWidth;
}
function autoHeight() {
var firstHeight = arguments.callee.firstHeight;
for(var i = 0 , j = imgs.length; i < j; i++) {
var imgObj = imgs[i];
console.log(imgObj.el.complete);
if(i == 0 && !firstHeight)
firstHeight = arguments.callee.firstHeight = imgObj.el.height;
else
imgObj.el.height = firstHeight;
}
}
function fixImgHeigtBy() {
window.innerWidth * 0.3 / 1.333
}
}
ajaxjs.list.thumbFadeIn = function() {
var imgs = [];
this.el.$('img[data-src^="https://"]', function(img, index) {
img.onload = function(){ this.classList.add('tran') };
imgs.push({
index : index, 
el : img,	
src : img.getAttribute('data-src') 
});
});
Step(function() {
for(var i = 0 , j = imgs.length; i < j; i++) {
if(imgs[i].src) {
var img = new Image();
img.onload = this.parallel();	
img.src = imgs[i].src;
}
}
}, function() {
var i = 0;
var nextStep = this;
var id = setInterval(function() {
var imgObj = imgs[i++];
imgObj.el.src = imgObj.src;
if(i == imgs.length) {
clearInterval(id);
nextStep();
}
}, 300);
}, function() {
});
}


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
<button><img :src="ajResources.commonAsset + \'/icon/save.gif\'" /> {{isCreate ? "新建":"保存"}}</button>\
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
required: false
},
selectedCatelogId :{ 
type: Number,
required: false
},
noCatelog : {
type : Boolean, 
default : false
},
searchFieldValue : { 
required: false,
default : 'name'
}
},
template: 
'<div class="aj-admin-filter-panel">\
<form action="?" method="GET">\
<input type="hidden" name="searchField" :value="searchFieldValue" />\
<input type="text" name="searchValue" placeholder="请输入搜索之关键字" style="float: inherit;" class="ajaxjs-inputField" />\
<button style="margin-top: 0;" class="ajaxjs-btn">搜索</button>\
</form><slot></slot>\
<span v-if="!noCatelog">{{label||\'分类\'}}：\
<aj-tree-catelog-select :is-auto-jump="true" :catelog-id="catelogId" :selected-catelog-id="selectedCatelogId"></aj-tree-catelog-select></span>\
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
aj._carousel = {
props : {
isMagic : {	
type : Boolean,
default : false 
},
isUsePx : {
type : Boolean,
default : false 
},
autoHeight : {
type : Boolean,
default : false 
},
disableTabHeaderJump : {
type : Boolean,
default : false 
}, 
isGetCurrentHeight : {
type : Boolean,
default : true 
},
initItems : Array 
},
data : function() {
return {
selected : 0
};
},
mounted: function() {
this.mover = this.$el.$('div.content'); 
var mover = this.mover, children = mover.children, len = children.length;
setTimeout(function() {
var stepWidth = this.setItemWidth();
if(this.isMagic) 
mover.style.width = this.isUsePx ? (stepWidth * 2) +'px' : '200%';
else
mover.style.width = this.isUsePx ? (stepWidth * len) +'px' : len + '00%';
var tabWidth = this.isUsePx ? stepWidth + 'px' : (1 / len * 100).toFixed(5) + '%';
this.tabWidth = tabWidth;
for(var i = 0; i < len; i++) 
children[i].style.width = this.isMagic ? '50%' : tabWidth;
var headerUl = this.$el.$('header ul');
if(headerUl)
for(var i = 0; i < len; i++) 
headerUl.children[i].style.width = tabWidth;
this.doHeight(this.selected);
}.bind(this), 400);
},
watch:{
'selected' : function(index, oldIndex) {
if(this.$isStop) 
return;
var children = this.$el.$('header ul').children;
var contentChild = this.$el.$('.content').children;
if(children && contentChild && children[oldIndex] && contentChild[oldIndex]) {
children[oldIndex].classList.remove('active');
contentChild[oldIndex].classList.remove('active');
children[index].classList.add('active');
contentChild[index].classList.add('active');
this.go(index);
}
}
},
methods : {
setItemWidth : function() {
this.stepWidth = this.stepWidth || this.mover.parentNode.clientWidth || window.innerWidth; 
return this.stepWidth;
},
changeTab : function(index) {
this.selected = index;
this.go(index);
},
go : function(i) {
this.$emit('before-carousel-item-switch', this, i);
if(this.$isStop) 
return;
var mover = this.mover, children = mover.children, len = children.length;
this.doHeight(i);
if(this.isMagic) {
for(var p = 0; p < len; p++) {
if(this.selected == p) {
continue;
}else if(i == p) {
children[p].classList.remove('hide');
}else {	
children[p].classList.add('hide');
}
}
var cssText = i > this.selected
? 'translate3d({0}, 0px, 0px)'.replace('{0}', '-50%')
: 'translate3d({0}, 0px, 0px)'.replace('{0}', '0%');
mover.style.webkitTransition = '-webkit-transform 400ms linear';
mover.style.webkitTransform = cssText;
}else{
var isWebkit = navigator.userAgent.toLowerCase().indexOf('webkit') != -1;
if(!this.stepWidth)
this.setItemWidth();
var leftValue = this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-' + (1 / len * 100 * i).toFixed(2) + '%');
mover.style[this.isWebkit ? 'webkitTransform' : 'transform'] = 'translate3d({0}, 0px, 0px)'.replace('{0}', leftValue);
}
this.$emit('carousel-item-switch', this, i, children[i]);
},
goPrevious : function() {
if(this.$isStop) 
return;
var len = this.mover.children.length;
this.selected--;
if (this.selected < 0)
this.selected = len - 1;
this.go(this.selected); 
},
goNext : function() {
if(this.$isStop) 
return;
var len = this.mover.children.length;
this.selected++;
if (this.selected == len)
this.selected = 0; 
this.go(this.selected);
},
onResize : function () {
var stepWidth = this.mover.parentNode.clientWidth; 
this.mover.style.width = this.isUsePx ? (stepWidth * this.len) +'px' : this.len + '00%';
for(var i = 0; i < this.len; i++) 
this.children[i].style.width = stepWidth + 'px';
},
doHeight: function(i) {
if(this.isGetCurrentHeight) {
var mover = this.mover, children = mover.children, len = children.length;
for(var p = 0; p < len; p++) {
if(i == p) {
children[p].style.height = 'initial';	
}else{
children[p].style.height = '1px';	
}
}
}
},
doAutoHeight : function (nextItem) {
if(this.autoHeight) {
var tabHeaderHeight = 0;
if(this.tabHeader) 
tabHeaderHeight = this.tabHeader.scrollHeight;
this.el.style.height = (nextItem.scrollHeight + tabHeaderHeight + 50) + 'px'; 
}
},
autoChangeTab: function(e) {
if(this.$isStop) 
return;
var el = e.currentTarget;
var children = el.parentNode.children;
for(var i = 0, j = children.length; i < j; i++) {
if(el == children[i]) {
break;
}
}
this.selected = i;
}
}
};
Vue.component('aj-carousel', {
mixins: [aj._carousel],
template : 
'<div class="aj-carousel aj-carousel-tab">\
<header><ul>\
<li v-for="(item, index) in items" :class="{\'active\': index === selected}" @click="changeTab(index);">{{item.name}}</li>\
</ul></header>\
<div>\
<div v-for="(item, index) in items" :class="{\'active\': index === selected}" v-html="item.content"></div>\
</div>\
</div>',
data : function() {
return {
items : this.initItems || [
{name : '杜甫：望岳', content : '岱宗夫如何，齊魯青未了。<br>\
造化鐘神秀，陰陽割昏曉。<br>\
蕩胸生層云，決眥入歸鳥，<br>\
會當凌絕頂，一覽眾山小。'
},
{name : '资质证照', content : '之所以如此，端在于中国传统中存在着发达的契约。予谓不信，可看看早年由福建师范大学内部印行的两册本《明清福建经济契约文书选集》，中国社会科学出版社出版的《自贡盐业契约档案选集》，花山文艺出版社出版的共二十册的《徽州千年契约文书》;再看看近些年由安徽师范大学出版社出版的十卷本的《千年徽州契约文书集萃》，广西师范大学出版社出版的四辑共四十册《徽州文书》、三辑共三十册的《清水江文书》，民族出版社出版的多卷本《贵州清水江流域明清契约文书》，凤凰出版社出版的《敦煌契约文书辑校》，天津古籍出版社出版的《清代宁波契约文书辑校》，浙江大学出版社出版的《清代浙东契约文书辑选》……不难发现，中国传统契约文书的整理出版，完全可称为如雨后春笋般在迅速成长!'},
{name : '资质证照', content : '笔者出于个人兴趣，在关注这些已经整理出版的、卷帙浩繁的契约文献的同时，也游走各地，或亲自搜集各类契约文书，或到一些地方档案馆查看其业已编辑成册、内部印行的传统契约文书，如在台湾宜兰、高雄，山东青岛、威海，贵州锦屏、从江等地档案机构或民间都见到过相关契约文献。记忆尤深的一次，是我和几位学生共游山东浮来山。在一处值班室里，居然发现有人以清代契约文本粘糊墙壁!足见只要我们稍加留意，在这个文明发展历经数千年的国度，不时可以发现一些令人称心的古代契约文献。'}
]
};
}
});
Vue.component('aj-banner', {
mixins: [aj._carousel],
props : {
isUsePx : {
default: true
},
isGetCurrentHeight : {
default: false
},
autoLoop : { 
type : Number,
default : 4000 
},
showTitle : { 
type: Boolean,
default : false
},
showDot : {	
type : Boolean,
default : true
}
},
template : 
'<div class="aj-carousel aj-banner">\
<header><ul v-show="showTitle">\
<li v-for="(item, index) in items" :class="{\'hide\': index !== selected}">{{item.name}}</li>\
</ul><ol v-show="showDot">\
<li v-for="n in items.length" :class="{\'active\': (n - 1) === selected}" @click="changeTab(n - 1);"></li></ol></header>\
<div class="content">\
<div v-for="(item, index) in items" :class="{\'active\': index === selected}" v-html="getContent(item.content, item.href)"></div>\
</div>\
</div>',
data : function() {
return {
items : this.initItems || [
{name : '杜甫：望岳', content : '<img src="../images/20150826162352938.jpg" />', href : 'http://qq.com'},
{name : '资质证照', content : '<img src="../images/20150906125934797.jpg" />', href : 'javascript:alert(9);'},
{name : '资质证照', content : '<img src="../images/20150906113629683.jpg" />', href: '#'}
]
}; 
},
mounted: function() {
this.loop();
},
methods:{
loop : function() {
if(this.autoLoop)
this.loopTimer = window.setInterval(this.goNext.bind(this), this.autoLoop);
},
getContent : function(content, href) {
if(!href)
return content;
else
return '<a href="' + href + '">' + content + '</a>';
}
}
});

Vue.component('aj-page-captcha', {
props : {
imgSrc : {
type: String, 
required: false,
},
fieldName : {	
type: String,
required: false,
default : 'captcha'
}
},
template : 
'<table class="aj-page-captcha"><tr>\
<td><input type="text" :name="fieldName" placeholder="输入右侧验证码" data-regexp="integer" required /></td>\
<td style="vertical-align: top;">\
<img :src="imgSrc || ajResources.ctx + \'/Captcha\'" @click="onClk($event);" title="点击刷新图片" />\
</td>\
</tr></table>',
methods : {
onClk : function(e) {
var img = e.target;
img.src = img.src.replace(/\?\d+$/, '') + '?' + new Date().valueOf();
},
refreshCode: function(){
var img = this.$el.querySelector('img');
img.src = img.src.replace(/\?\d+$/, '') + '?' + new Date().valueOf();
}
}
});
Vue.component('aj-form-calendar', {
data() {
var date = new Date;
return {
date : date,
year : date.getFullYear(),
month : date.getMonth() + 1,
day : 1
};
},
props : {
showTime: false
},
template : 
'<div class="aj-form-calendar">\
<div class="selectYearMonth">\
<a href="#" @click="getDate(\'preYear\')" class="preYear" title="上一年">&lt;</a> \
<select @change="setMonth($event)" v-model="month">\
<option value="1">一月</option><option value="2">二月</option><option value="3">三月</option><option value="4">四月</option>\
<option value="5">五月</option><option value="6">六月</option><option value="7">七月</option><option value="8">八月</option>\
<option value="9">九月</option><option value="10">十月</option><option value="11">十一月</option><option value="12">十二月</option>\
</select>\
<a href="#" @click="getDate(\'nextYear\')" class="nextYear" title="下一年">&gt;</a>\
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
<div v-if="showTime">\
时 <select class="hour ajaxjs-select"><option v-for="n in 24">{{n}}</option></select> \
分 <select class="minute ajaxjs-select"><option v-for="n in 61">{{n - 1}}</option></select>\
<a href="#" @click="pickupTime($event)">选择时间</a>\
</div>\
</div>',
mounted () {
this.$options.watch.date.call(this);
},
watch : {
date (n) {
this.year = this.date.getFullYear();
this.month = this.date.getMonth() + 1;
this.render();
}
},
methods : {
render () {
var arr = this.getDateArr();
var frag = document.createDocumentFragment();
while (arr.length) {
var row = document.createElement("tr"); 
for (var i = 1; i <= 7; i++) { 
var cell = document.createElement("td");
if (arr.length) {
var d = arr.shift();
if (d) {
cell.innerHTML = d;
var text = this.year + '-' + this.month + '-' + d;
cell.className = 'day day_' + text;
cell.title = text;
var on = new Date(this.year, this.month - 1, d);
if (this.isSameDay(on, this.date)) {
cell.classList.add('onToday');
this.onToday && this.onToday(cell);
}
}
}
row.appendChild(cell);
}
frag.appendChild(row);
}
var tbody = this.$el.$("table tbody");
tbody.innerHTML = '';
tbody.appendChild(frag);
},
getDate (dateType, month) {
var nowYear = this.date.getFullYear(), nowMonth = this.date.getMonth() + 1; 
switch (dateType) {
case 'preMonth':
this.date = new Date(nowYear, nowMonth - 2, 1);
break;
case 'nextMonth':
this.date = new Date(nowYear, nowMonth, 1);
break;
case 'setMonth':
this.date = new Date(nowYear, month - 1, 1);
break;
case 'preYear':
this.date = new Date(nowYear - 1, nowMonth - 1, 1);
break;
case 'nextYear':
this.date = new Date(nowYear + 1, nowMonth - 1, 1);
break;
}
},
setMonth(e){
this.getDate('setMonth', Number(e.target.selectedOptions[0].value));
},
getDateArr () {
var arr = [];
for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
arr.push(0);
for (var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
arr.push(i);
return arr;
},
pickDay (e) {
var el = e.target, date = el.title;
this.$emit('pick-date', date);
return date;
},
pickupTime(e) {
var time = this.$el.$('.hour').selectedOptions[0].value + ':' + this.$el.$('.minute').selectedOptions[0].value;
this.$emit('pick-time', time);
},
isSameDay (d1, d2) {
return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
}
}
});
Vue.component('aj-form-calendar-input', {
data : function(){
return {
date : this.fieldValue, 
time : ''
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
},
showTime: false,
positionFixed : Boolean 
},
template : 
'<div class="aj-form-calendar-input" @mouseover="onMouseOver($event)">\
<div class="icon" ><div class="menu"></div></div>\
<input placeholder="请输入日期" :name="fieldName" :value="date +\' \' + time" type="text" />\
<aj-form-calendar ref="calendar" :show-time="showTime" @pick-date="recEvent" @pick-time="recTimeEvent"></aj-form-calendar>\
</div>',
mounted : function() {
if(this.positionFixed) {
this.$el.$('.aj-form-calendar').classList.add('positionFixed');
}
if(this.fieldValue) {
var arr = this.fieldValue.split(' ')[0], arr = arr.split('-'), date = new Date(arr[0], arr[1] - 1, arr[2], " ", "", " ");
this.$refs.calendar.date = date;
}
},
methods : {
recTimeEvent: function(time) {
this.time = time;
},
recEvent: function(date) {
this.date = date;
},
onMouseOver : function(e) {
if(this.positionFixed) {
var el = e.currentTarget;
var b = el.getBoundingClientRect();
var c = this.$el.$('.aj-form-calendar');
c.style.top = (b.top + el.clientHeight - 0) + 'px';
c.style.left = ((b.left - 0) + 0) + 'px';
}
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
},
uploadImageActionUrl: String
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
this.iframeDoc.addEventListener('paste', this.onImagePaste);
}.bind(this);
},
methods: {
onImagePaste(event) {
var items = event.clipboardData && event.clipboardData.items;
var file = null; 
if (items && items.length) {
for (var i = 0; i < items.length; i++) {
if (items[i].type.indexOf('image') !== -1) {
if(window.isCreate) { 
aj.alert.show('请保存记录后再上传图片。');
return;
}
file = items[i].getAsFile();
break;
}
}
}
if(!this.uploadImageActionUrl) {
alert('未提供图片上传地址');
return;
}
if(file) {
var imgInsert = event.target, self = this;
event.preventDefault();
aj.img.changeBlobImageQuality(file, (newBlob)=> {	
Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
action: this.uploadImageActionUrl,
progress: 0,
uploadOk_callback(j) {
self.format("insertImage", j.imgUrl);
},
$blob: newBlob,
$fileName: 'foo.jpg'
});
});
} 
},
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
case 'cleanHTML':
this.cleanHTML();
break;
case 'saveRemoteImage2Local':
this.saveRemoteImage2Local();
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
saveRemoteImage2Local(){
var str = [], arr = this.iframeDoc.querySelectorAll('img');
for(var i = 0, j = arr.length; i <j; i++) {
var imgEl = arr[i], url = imgEl.getAttribute('src');
if(/^http/.test(url)) {
str.push(url);
}
}
aj.xhr.post('', (json)=>{
console.log(json);
}, {urls: str.join('|')});
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
onFontColorPicker(e) {
var color = e.target.title;
this.format('foreColor', color);
},
onFontBgColorPicker(e) {
var color = e.target.title;
this.format('backColor', color);
},
createColorPickerHTML() {
var cl = ['00', '33', '66', '99', 'CC', 'FF'], a, b, c, d, e, f, i, j, k, T;
var h = '<div class="colorhead"><span class="colortitle">颜色选择</span></div>\
<div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>';// 创建body
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
},
cleanHTML(){
var tagsAllowed = "|h1|h2|h3|p|div|a|b|strong|br|ol|ul|li|pre|img|br|hr|font|";
var attributesAllowed = {};
attributesAllowed["div"] = "|id|class|";
attributesAllowed["a"] = "|id|class|href|name|";
attributesAllowed["img"] = "|src|";
this.everyNode(this.iframeBody, node => {
var isDelete = false;
if (node.nodeType === 1) {
var tag = node.tagName.toLowerCase();
if (tagsAllowed.indexOf("|" + tag + "|") === -1)
isDelete = true;
if (!isDelete) { 
var attrs = node.attributes;
for(var i = attrs.length - 1; i >= 0; i--) {
var name = attrs[i].name;
if (attributesAllowed[tag] == null || attributesAllowed[tag].indexOf("|" + name.toLowerCase() + "|") == -1){
node.removeAttribute(name);
}
}
}
} else if (node.nodeType === 8) {
isDelete = true;
}
return isDelete;
});
},
everyNode (el, fn) {
var objChildNode = el.firstChild;
while (objChildNode) {
if (fn(objChildNode)) { 
var next = objChildNode.nextSibling;
el.removeChild(objChildNode);
objChildNode = next;
} else {
if (objChildNode.nodeType === 1) 
this.everyNode(objChildNode, fn);
objChildNode = objChildNode.nextSibling;
}
}
}
}
});

aj.img = (function() {
function dataURLtoBlob(dataurl) {
var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), len = bstr.length, u8arr = new Uint8Array(len);
while (len--)
u8arr[len] = bstr.charCodeAt(len);
return new Blob([ u8arr ], {
type : mime
});
}
return {	
imageToCanvas(imgUrl, cb, isCovernt2DataUrl) {
var img = new Image();
img.onload = () => {
var canvas = document.createElement('canvas');
canvas.width = img.width;
canvas.height = img.height;
canvas.getContext('2d').drawImage(img, 0, 0);
if(isCovernt2DataUrl) {
cb(canvas.toDataURL(isCovernt2DataUrl.format || 'image/jpeg', isCovernt2DataUrl.quality || .9));
} else {
cb(canvas);
}
}
img.src = imgUrl;
},
imageElToBlob(imgUrl, cb) {
this.imageToCanvas(imgUrl, (canvas) => {
cb(dataURLtoBlob(canvas));
}, true)
},
changeBlobImageQuality (blob, callback, format, quality) {
format = format || 'image/jpeg';
quality = quality || 0.9; 
var fr = new FileReader();
fr.onload = function(e) {
var dataURL = e.target.result;
var img = new Image();
img.onload = function() {
var canvas = document.createElement('canvas');
var ctx = canvas.getContext('2d');
canvas.width = img.width;
canvas.height = img.height;
ctx.drawImage(img, 0, 0);
var newDataURL = canvas.toDataURL(format, quality);
callback && callback(dataURLtoBlob(newDataURL));
canvas = null;
};
img.src = dataURL;
};
fr.readAsDataURL(blob); 
}
};
})();
aj._list = {
props : {
apiUrl : {	
type : String,
required : true
},
hrefStr : {
type : String,
required : false
}
},
data : function() {
return {
result : [],	
baseParam: {},	
realApiUrl: this.apiUrl
};
}
};
Vue.component('aj-simple-list', {
mixins: [aj._list],
template : '<ul class="aj-simple-list"><li v-for="(item, index) in result">\
<slot v-bind="item">\
<a :href="(hrefStr || \'\').replace(\'{id}\', item.id)" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>\
</slot>\
</li></ul>',
mounted : function() {
ajaxjs.xhr.get(this.realApiUrl, function(json) {
aj.apply(this, json);
}.bind(this), this.baseParam);
}
});
Vue.component('aj-page-list', {
mixins: [aj._list],
data : function() {
return {
pageSize : this.initPageSize,
total : 0,
totalPage :0,
pageStart: 0,
currentPage : 0
};
},
props : {
initPageSize : {
type : Number,
required : false,
default : 5
},
isShowFooter : {
type : Boolean,
default : true
},
autoLoadWhenReachedBottom : {	
type : String,
default : ''
},
isDataAppend : {
type : Boolean, 
default : false
}
},
template : 
'<div class="aj-page-list">\
<ul><li v-for="(item, index) in result">\
<slot v-bind="item">\
<a href="#" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>\
</slot>\
</li></ul>\
<footer v-show="isShowFooter">\
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
</footer><div v-show="!!autoLoadWhenReachedBottom" class="buttom"></div>\
</div>',
mounted : function() {
ajaxjs.xhr.get(this.realApiUrl, this.doAjaxGet, {
limit : this.pageSize
});
if(!!this.autoLoadWhenReachedBottom) {
var scrollSpy = new aj.scrollSpy({
scrollInElement : aj(this.autoLoadWhenReachedBottom),
spyOn : thish.$el.$('.buttom')
});
scrollSpy.onScrollSpyBackInSight = function (e) {
this.nextPage();
}.bind(this);
}
},
created : function() {
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
doAjaxGet : function(json) {
this.total = json.total;
this.result = this.isDataAppend ? this.result.concat(json.result) : json.result;
this.count();
}, 
ajaxGet : function () {
var params = {};
aj.apply(params, { start : this.pageStart, limit : this.pageSize });
this.baseParam && aj.apply(params, this.baseParam);
ajaxjs.xhr.get(this.realApiUrl, this.doAjaxGet, params);
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
},
watch: {
'baseParam' : function(index, oldIndex) {
this.ajaxGet();
}
}
});
aj.scrollSpy = function(cfg) {
var isScrollInElement = !!(cfg && cfg.scrollInElement);
var handleScroll = function () {
var currentViewPosition;
if(isScrollInElement) {
currentViewPosition = cfg.scrollInElement.scrollTop + window.innerHeight;
}else {
currentViewPosition = document.documentElement.scrollTop ? document.documentElement.scrollTop: document.body.scrollTop;
}
for (var i in elements) {
var element = elements[i], 
el = element.domElement,
elementPosition = getPositionOfElement(el);
var usableViewPosition = currentViewPosition;
if (element.isInViewPort == false) 
usableViewPosition -= el.clientHeight;
if (usableViewPosition < elementPosition) {
this.onScrollSpyOutOfSight && this.onScrollSpyOutOfSight(el);
element.isInViewPort = false;
} else if (element.isInViewPort == false) {
this.onScrollSpyBackInSight && this.onScrollSpyBackInSight(el);
element.isInViewPort = true;
}
}
}.bind(this);
if (document.addEventListener) {
(cfg && cfg.scrollInElement || document).addEventListener("touchmove", handleScroll, false);
(cfg && cfg.scrollInElement || document).addEventListener("scroll", handleScroll, false);
} else if (window.attachEvent) {
window.attachEvent("onscroll", handleScroll);
}
var elements = {};
this.spyOn = function(domElement) {
var element = {};
element['domElement'] = domElement;
element['isInViewPort'] = true;
elements[domElement.id] = element;
}
if(cfg && cfg.spyOn) 
this.spyOn(cfg.spyOn);
function getPositionOfElement(domElement) {
var pos = 0;
while (domElement != null) {
pos += domElement.offsetTop;
domElement = domElement.offsetParent;
}
return pos;
}
}
aj._simple_marquee_text = {
props : {
interval : {
type : Number, 
default : 500
},
canstop :{
type : Boolean, 
default : true
}
},
mounted : function() {
if (this.canstop) {
this.$el.onmouseover = this.clearTimer.bind(this);
this.$el.onmouseout = this.start.bind(this);
}
},
methods : {
start : function() {
this.$timerId = window.setInterval(this.scroll, this.interval);
},
clearTimer : function() {
this.$timerId && window.clearInterval(this.$timerId);
}
}
};
Vue.component('aj-super-simple-marquee-text', {
mixins : [aj._simple_marquee_text],
template : '<div><slot>这是一段滚动的文字；这是一段滚动的文字；这是一段滚动的文字</slot></div>',
mounted : function() {
this.$arr = this.$el.innerHTML.split("");
this.start();
},
methods : {
scroll : function () {
this.$arr.push(this.$arr.shift());
this.$el.innerHTML = this.$arr.join("");
}
}
});
Vue.component('aj-simple-marquee-text', {
mixins : [aj._simple_marquee_text],
template : '<ol><li>11111111111</li><li>22222222222</li><li>33333333333</li><li>44444444444</li><li>55555555555</li></ol>',
mounted : function() {
this.start();
},
methods : {
scroll : function () {
var lastEl = this.$el.firstChild;
while (lastEl.nodeType != 1)
lastEl = lastEl.nextSibling;
this.$el.appendChild(this.$el.removeChild(lastEl)); 
}
}
});
Vue.component('aj-simple-marquee', {
props : {
interval : {
default : 20
},
pauseInterval : { 
type : Number,
default : 2000
},
itemHeight : { 
type : Number,
required : 20,
},
},
template : 
'<div class="aj-simple-marquee" style="width: 100%; overflow: hidden;">\
<div class="items"><slot></slot>\
</div>\
<div class="clone"></div>\
</div>',
mixins : [aj._simple_marquee_text],
mounted : function() {
var el = this.$el, children = el.$('.items').children, itemHeight = this.itemHeight;
el.style.height = itemHeight + "px";
var allHeight = 0;
for(var i = 0, j = children.length; i < j; i++) { 
var item = children[i];
item.style.display = 'block';
item.style.height = itemHeight + "px";
allHeight += itemHeight;
}
el.$('.clone').style.height = allHeight + 'px';
var clone = children[0].cloneNode(true);
el.$('.clone').appendChild(clone);
setTimeout(this.start.bind(this), 2000);
},
methods : {
scroll :function () {
var el = this.$el, top = el.scrollTop, height = el.$('.items').clientHeight;
if (top <= height) {
el.scrollTop ++;
if(top != 0 && (top % this.itemHeight) === 0) {
this.clearTimer();
setTimeout(this.start.bind(this), this.pauseInterval);
}
} else {
el.scrollTop -= height; 
}
}
}
});

Vue.component('aj-accordion-menu', {
template : '<ul class="aj-accordion-menu" @click="onClk($event);"><slot></slot></ul>',
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
'<div class="aj-expander" :style="\'height:\' + (expended ? openHeight : closeHeight) + \'px;\'">\
<div :class="expended ? \'closeBtn\' : \'openBtn\'" @click="expended = !expended;"></div>\
<slot></slot>\
</div>'
});
Vue.component('aj-menu-moblie-scroll', {
props : {
initItems : {
type : Array,
default : function() {
return [{name : 'foo'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}, {name : 'bar'}];
}
}
},
data : function() {
return {
selected : 0,
items : this.initItems
}
},
template : 
'<div class="aj-hoz-scroll"><div><ul>\
<li @click="fireEvent($event, index);" v-for="item, index in items" :class="{\'selected\': index === selected}">{{item.name}}</li>\
</ul><div class="indicator"></div></div></div>',
mounted : function() {
var self = this;
setTimeout(function() {
self.$el.$('.indicator').style.width = self.$el.$('li').clientWidth + 'px';
}, 500);
},
methods: {
fireEvent : function(e, index) {
var el = e.target;
this.$el.$('.indicator').style.marginLeft = el.offsetLeft + 'px';
this.$emit('on-aj-menu-moblie-scroll-click', e, index, this.selected);
this.selected = index;
}
}
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
'<div class="aj-modal hide" @click="close($event);">\
<div><div v-html="showText"></div>\
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
template : '<div class="aj-topMsg" v-html="showText"></div>',
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
template : '<div class="aj-modal hide" @click="close($event);"><div><slot></slot></div></div>',
props :{
notCloseWhenTap: Boolean
},
methods : {
show : function(cfg) {
this.$el.classList.remove('hide');
if(cfg && cfg.afterClose)
this.afterClose = cfg.afterClose;
},
close : function(e) { 
if(e.isForceClose || !this.notCloseWhenTap)
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
initCreateDate : String,
initContent : { 
type: String,
required: false
},
isShowTools : { 
type: Boolean,
required: false
},
neighbor : { 
type: Object,
default : function(){
return {};
},
required: false
}
},
data: function(){
return {
content : this.initContent,
createDate : this.initCreateDate
};
},
template : 
'<div class="aj-article-body">\
<article>\
<h3>{{title}}</h3>\
<h4>{{createDate}}</h4>\
<section v-html="content"></section>\
<slot></slot>\
</article>\
<div v-if="isShowTools">\
<a :href="neighbor.pervInfo.url" v-if="neighbor.pervInfo">上则记录：{{neighbor.pervInfo.name}}</a>\
<a :href="neighbor.nextInfo.url" v-if="neighbor.nextInfo">下则记录：{{neighbor.nextInfo.name}}</a>\
</div>\
<aj-misc-function v-if="isShowTools"></aj-misc-function><aj-adjust-font-size v-if="isShowTools"></aj-adjust-font-size><aj-page-share v-if="isShowTools"></aj-page-share>\
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
Vue.component('aj-simple-tab', {
template : 
'<div :class="isVertical ? \'aj-simple-tab-vertical\' : \'aj-simple-tab-horizontal\' ">\
<ul>\
<li v-for="(item, index) in items" :class="{\'selected\': index === selected}" @click="changeTab(index);">{{item.name}}</li>\
</ul>\
<div class="content">\
<div v-for="(item, index) in items" :class="{\'selected\': index === selected}" v-html="item.content"></div>\
</div>\
</div>',
props: {
isVertical : Boolean, 
initItems : Array
},
data : function() {
return {
selected : 0,
items : this.initItems || [
{name : '杜甫：望岳', content : '岱宗夫如何，齊魯青未了。<br>\
造化鐘神秀，陰陽割昏曉。<br>\
蕩胸生層云，決眥入歸鳥，<br>\
會當凌絕頂，一覽眾山小。'
},
{name : '资质证照', content : '之所以如此，端在于中国传统中存在着发达的契约。予谓不信，可看看早年由福建师范大学内部印行的两册本《明清福建经济契约文书选集》，中国社会科学出版社出版的《自贡盐业契约档案选集》，花山文艺出版社出版的共二十册的《徽州千年契约文书》;再看看近些年由安徽师范大学出版社出版的十卷本的《千年徽州契约文书集萃》，广西师范大学出版社出版的四辑共四十册《徽州文书》、三辑共三十册的《清水江文书》，民族出版社出版的多卷本《贵州清水江流域明清契约文书》，凤凰出版社出版的《敦煌契约文书辑校》，天津古籍出版社出版的《清代宁波契约文书辑校》，浙江大学出版社出版的《清代浙东契约文书辑选》……不难发现，中国传统契约文书的整理出版，完全可称为如雨后春笋般在迅速成长!'},
{name : '资质证照', content : '笔者出于个人兴趣，在关注这些已经整理出版的、卷帙浩繁的契约文献的同时，也游走各地，或亲自搜集各类契约文书，或到一些地方档案馆查看其业已编辑成册、内部印行的传统契约文书，如在台湾宜兰、高雄，山东青岛、威海，贵州锦屏、从江等地档案机构或民间都见到过相关契约文献。记忆尤深的一次，是我和几位学生共游山东浮来山。在一处值班室里，居然发现有人以清代契约文本粘糊墙壁!足见只要我们稍加留意，在这个文明发展历经数千年的国度，不时可以发现一些令人称心的古代契约文献。'}
]
};
},
methods : {
changeTab : function(index) {
this.selected = index;
}
}
});
Vue.component('aj-back-top', {
template : 
'<a href="###" @click="go">回到顶部</a>',
methods : {
go : function() {
this.$timerId && window.clearInterval(this.$timerId);
var top = speed = 0;
this.$timerId = window.setInterval(function() {
top = document.documentElement.scrollTop || document.body.scrollTop;
speed = Math.floor((0 - top) / 8);
if (top === 0)
clearInterval(this.$timerId);
else
document.documentElement.scrollTop = document.body.scrollTop = top + speed;
}.bind(this), 30);
}
}
});
Vue.component('aj-process-line', {
template :
'<div class="aj-process-line">\
<div class="process-line">\
<div v-for="(item, index) in items" :class="{current : index == current, done : index < current}">\
<span>{{index + 1}}</span><p>{{item}}</p>\
</div>\
</div>\
</div>',
props : {
items : {
type: Array,
default : function() { 
return ['Step 1', 'Step 2', 'Step 3']; 
}
}
},
data : function() {
return {
current : 0
}
},
methods: {
go : function(i) {
this.current = i;
},
perv: function() {
var perv = this.current - 1;
if (perv < 0)
perv = this.items.length - 1;
this.go(perv); 
},
next: function() {
var next = this.current + 1;
if (this.items.length == next)
next = 0; 
this.go(next);
}
}
});
function throttleV2(fn, delay, mustRunDelay) {
var timer = null;
var t_start;
return function() {
var context = this, args = arguments, t_curr = +new Date();
clearTimeout(timer);
if(!t_start) 
t_start = t_curr;
if(t_curr - t_start >= mustRunDelay) {
fn.apply(context, args);
t_start = t_curr;
} else {
timer = setTimeout(function() {
fn.apply(context, args);
}, delay);
}
};
};
aj.imageEnlarger = function() {
var vue = new Vue({
el : document.body.appendChild(document.createElement('div')),
template: 
'<div class="aj-image-large-view">\
<div style="position: fixed;max-width:400px;transition: top ease-in 200ms, left ease-in 200ms;">\
<img :src="imgUrl" style="width: 100%;" />\
</div></div>',
data : {
imgUrl: null
},
mounted: function(){
document.addEventListener('mousemove', throttleV2(this.move.bind(this), 50, 5000), false);
},
methods: {
move: function(e) {
if(this.imgUrl) {
var el = this.$el.$('div');
var w = 0, imgWidth = this.$el.$('img').clientWidth;
if(imgWidth > e.pageX) {
w = imgWidth;
}
el.style.top = (e.pageY + 20)+ 'px';
el.style.left = (e.pageX - el.clientWidth + w) + 'px';
}
}
}
});
aj.imageEnlarger.singleInstance = vue; 
return vue;
}

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
Vue.component('aj-select', {
props : {
json : {
type: Object,
required: true
},
defaultSelected : {	
type: String,
required: false
},
fieldName : { 
type: String,
required: true
}
},
data : function() {
return {
selected : ""
};
},
template : '<select :name="fieldName" @change="onSelected($event);" class="ajaxjs-select" style="min-width: 200px;" v-model="selected">\
<option v-for="(key, v) in json" :value="v">{{key}}</option>\
</select>',
created:function(){
if(this.defaultSelected) {
this.selected = this.defaultSelected;
} else {
for(var i in this.json) {
this.selected = i;
break;
}
}
},
methods : {
onSelected : function(e) {
},
getSelected: function(){
console.log(this.selected)
}
}
});
Vue.component('aj-select-arr', {
props : {
url : '',
fieldName : { 
type: String,
required: true
},
firstOption:{
type:String,
default :'---请选择---'
},
defaultSelected : {	
type: String,
default: "0"
}
},
data : function() {
return {
json : {}
};
},
template:'<aj-select :json="json" :field-name="fieldName" :default-selected="defaultSelected"></aj-select>',
mounted : function(){
var self = this; 
aj.xhr.get(this.ajResources.ctx + this.url, function(arr) {
var json = {0: self.firstOption};
for (var i = 0, j = arr.length; i < j; i++) { 
json[arr[i].id] = arr[i].name;
}
self.json = json;
});
}
});
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
required: false,
default:'catelogId'
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
selectUI.renderer(catalogArr, this.$el, this.selectedCatelogId, {makeAllOption : false});
},
onSelected : function(e) {
if(this.isAutoJump) {
var el = e.target, catalogId = el.selectedOptions[0].value;
location.assign('?' + this.fieldName + '=' + catalogId);
} else {
this.BUS.$emit('aj-tree-catelog-select-change', e, this);
}
}
}
});
Vue.component('aj-china-area', {
template : '<div class="aj-china-area"><select v-model="province" class="ajaxjs-select" name="locationProvince">\
<option value="">请选择</option>\
<option v-for="(v, k) in addressData[86]" :value="k">{{v}}</option>\
</select>\
<select v-model="city" class="ajaxjs-select" name="locationCity">\
<option value="">请选择</option>\
<option v-for="(v, k) in citys" :value="k">{{v}}</option>\
</select>\
<select v-model="district" class="ajaxjs-select" name="locationDistrict">\
<option value="">请选择</option>\
<option v-for="(v, k) in districts" :value="k">{{v}}</option>\
</select>\
</div>',
props:{
provinceCode:String,
cityCode: String,
districtCode: String
},
data:function(){
if(!China_AREA)throw '中国行政区域数据 脚本没导入';
return {
province: this.provinceCode || '',
city: this.cityCode || '',
district: this.districtCode || '',
addressData: China_AREA
}
},
watch:{ 
province: function(val, oldval) {
},
},
computed: {
citys:function() {
if(!this.province)
return;
return this.addressData[this.province];
},
districts :function(){
if(!this.city)
return;
return this.addressData[this.city];
}
}
});
Vue.component('aj-tree-item', {
template: 
'<li>\
<div :class="{bold: isFolder, node: true}" @click="toggle">\
<span>········</span>{{ model.name }}\
<span v-if="isFolder">[{{ open ? \'-\' : \'+\' }}]</span>\
</div>\
<ul v-show="open" v-if="isFolder" :class="{show: open}">\
<aj-tree-item :event-bus="eventBus" class="item" v-for="(model, index) in model.children" :key="index" :model="model"></aj-tree-item>\
<li v-if="allowAddNode" class="add" @click="addChild">+</li>\
</ul>\
</li>',
props: {
model: Object,
allowAddNode: { 
type: Boolean,
default:false
},
eventBus: Object
},
data: function () {
return {
open: false
};
},
computed: {
isFolder: function () {
return this.model.children && this.model.children.length;
}
},
methods: {
toggle: function () {
if (this.isFolder)
this.open = !this.open;
if(this.eventBus) {
this.eventBus.$emit('treenodeclick', this.model);
}
},
fireEvent(){
},
changeType: function () {
if (!this.isFolder) {
Vue.set(this.model, 'children', []);
this.addChild();
this.open = true;
}
},
addChild: function () {
this.model.children.push({
name: 'new stuff'
});
}
}
});
Vue.component('aj-tree', {
template : '<ul class="aj-tree"><aj-tree-item :model="treeData" :event-bus="this.eventBus"></aj-tree-item></ul>',
props: {
url: String, 
topNodeName : String 
},
data: function() {
return {
treeData: { name : this.topNodeName || 'TOP', children : null },
eventBus: new Vue()
};
},
mounted : function() {
aj.xhr.get(this.ajResources.ctx + this.url, 
json => 
this.treeData.children = this.makeTree(json.result)
);
this.eventBus.$on('treenodeclick', data => { 
this.$emit('treenodeclick', data);
});
},
methods: {
makeTree (jsonArray) {
var arr = [];
for (var i = 0, j = jsonArray.length; i < j; i++) {
var n = jsonArray[i];
if(n.pid === -1) {
arr.push(n);
} else {
var parentNode = this.findParent(arr, n.pid);
if (parentNode) {
if (!parentNode.children)
parentNode.children = [];
parentNode.children.push(n);
} else{
console.log('parent not found!');
}
}
}
return arr;
},
findParent (jsonArray, id) {
for (var i = 0, j = jsonArray.length; i < j; i++) {
var map = jsonArray[i];
if (map.id == id)
return map;
if (map.children) {
var result = arguments.callee(map.children, id);
if (result != null)
return result;
}
}
return null;
},
captureBubble(data) {
this.$emit('treenodeclick', data);
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
required: false
},
filedId : {
type: Number,
required: false
},
limitSize : Number,
limitFileType: String
},
template : 
'<div class="ajaxjs-file-upload">\
<div class="pseudoFilePicker">\
<input type="hidden" v-if="fieldName" :name="fieldName" :value="newlyId || filedId" />\
<label for="input_file_molding"><div><div>+</div>点击选择文件</div></label>\
</div>\
<div v-if="!isFileSize || !isExtName">{{errMsg}}</div>\
<div v-if="isFileSize && isExtName">\
<button @click.prevent="doUpload($event);">上传</button>\
</div>\
</div>',
methods : {
onUploadInputChange : function(e) {
var fileInput = e.target;
var ext = fileInput.value.split('.').pop(); 
var size = fileInput.files[0].size;
if(this.limitSize)
this.isFileSize = size < this.limitSize;
else
this.isFileSize = true;
if(this.limitFileType)
this.isExtName = new RegExp(this.limitFileType, 'i').test(ext);
else
this.isExtName = true;
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
}
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
},
accpectFileType : { 
type : String
}
},
template : 
'<form :action="uploadUrl" method="POST" enctype="multipart/form-data" :target="\'upframe_\' + radomId">\
<input name="fileInput" :id="labelId" type="file" multiple="multiple" class="hide" @change="fireUploadFileSelected($event)" :accept="accpectFileType" />\
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
var imgUrl = json.imgUrl || json.visitPath;
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
if (!HTMLCanvasElement.prototype.toBlob) {
Object.defineProperty(HTMLCanvasElement.prototype, 'toBlob', {
value : function(callback, type, quality) {
var binStr = atob(this.toDataURL(type, quality).split(',')[1]), len = binStr.length, arr = new Uint8Array(len);
for (var i = 0; i < len; i++) {
arr[i] = binStr.charCodeAt(i);
}
callback(new Blob([ arr ], {
type : type || 'image/png'
}));
}
});
}
Vue.component('aj-xhr-upload', {
data : function() {
return {
isFileSize : false,	
isExtName : false,	
isImgSize : false, 
errMsg : null,	
newlyId : null,	
radomId : Math.round(Math.random() * 1000),	
uploadOk_callback: ajaxjs.xhr.defaultCallBack,	
imgBase64Str : null,	
progress : 0
};
},
props : {
action : {
type : String, 
required: true
}, 
fieldName : String, 
limitSize : Number,
limitFileType: String,
accpectFileType: String,
isImgUpload : Boolean, 
imgPlace : String,	
imgMaxWidth : {type : Number, default : 1920},
imgMaxHeight: {type : Number, default : 1680}
},
template : 
'<div class="aj-xhr-upload">\
<div v-if="isImgUpload">\
<a :href="imgPlace" target="_blank">\
<img class="upload_img_perview" :src="(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace" />\
</a>\
</div>\
<div class="pseudoFilePicker">\
<label :for="\'uploadInput_\' + radomId"><div><div>+</div>点击选择{{isImgUpload ? \'图片\': \'文件\'}}</div></label>\
</div>\
<input type="file" :name="fieldName" class="hide" :id="\'uploadInput_\' + radomId" @change="onUploadInputChange($event)" :accept="isImgUpload ? \'image/*\' : accpectFileType" />\
<div v-if="!isFileSize || !isExtName">{{errMsg}}</div>\
<div v-if="isFileSize && isExtName">\
<button @click.prevent="doUpload();" style="min-width:110px;">{{progress && progress !== 100 ? \'上传中 \' + progress + \'%\': \'上传\'}}</button>\
</div>\
</div>',
methods : {
onUploadInputChange : function(e) {
var fileInput = e.target;
var ext = fileInput.value.split('.').pop(); 
if(!fileInput.files || !fileInput.files[0]) return;
this.$fileObj = fileInput.files[0]; 
this.$fileName = this.$fileObj.name;
this.$fileType = this.$fileObj.type;
var size = this.$fileObj.size;
if(this.limitSize) {
this.isFileSize = size < this.limitSize;
this.errMsg = "要上传的文件容量过大，请压缩到 " + this.limitSize + "kb 以下";
} else
this.isFileSize = true;
if(this.limitFileType) {
this.isExtName = new RegExp(this.limitFileType, 'i').test(ext);
this.errMsg = '根据文件后缀名判断，此文件不能上传';	
} else
this.isExtName = true;
this.readBase64(fileInput.files[0]);
if(self.isImgUpload) {
var imgEl = new Image();
imgEl.onload = function() {
if (imgEl.width > self.imgMaxWidth || imgEl.height > self.imgMaxHeight) {
cfg.isImgSize = false;
self.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
} else {
cfg.isImgSize = true;
}
}
}
},
readBase64 : function(file) {
var reader = new FileReader(), self = this;
reader.onload = function(e) {
var imgBase64Str = e.target.result;
self.imgBase64Str = imgBase64Str;
if(self.isImgUpload) {
var imgEl = new Image();
imgEl.onload = function() {
if(file.size > 300 * 1024) { 
self.compressImg(imgEl);
}
if (imgEl.width > self.maxWidth || imgEl.height > self.maxHeight) {
self.isImgSize = false;
self.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
} else {
self.isImgSize = true;
}
}
imgEl.src = imgBase64Str;
var imgHeader = {
"jpeg" : "/9j/4",
"gif" : "R0lGOD",
"png" : "iVBORw"
};
for ( var i in imgHeader) {
if (~imgBase64Str.indexOf(imgHeader[i])) {
self.isExtName = true;
return;
}
}
self.errMsg = "亲，改了扩展名我还能认得你不是图片哦";
}
}
reader.readAsDataURL(file);
},
doUpload : function() {
var fd = new FormData();
if(this.$blob){
fd.append("file", this.$blob, this.$fileName);
} else 
fd.append("file", this.$fileObj);
var xhr = new XMLHttpRequest(), self = this;
xhr.onreadystatechange = ajaxjs.xhr.callback.delegate(null, this.uploadOk_callback, 'json');
xhr.open("POST", this.action, true);
xhr.onprogress = function(evt) {
var progress = 0;
var p = ~~(evt.loaded * 1000 / evt.total);
p = p / 10;
if(progress !== p) {
progress = p;
console.log('progress: ', p);
}
self.progress = progress;
};
xhr.send(fd);
},
compressImg: function(imgObj) {
var self = this;
var maxWidth = 1000, maxHeight = 1500;
var fitSizeObj = this.fitSize(imgObj.width, imgObj.height, maxWidth, maxHeight);
var targetWidth = fitSizeObj.targetWidth, targetHeight = fitSizeObj.targetHeight;
var orient = this.getPhotoOrientation(imgObj);
if (orient == 6) {
targetWidth = fitSizeObj.targetHeight;
targetHeight = fitSizeObj.targetWidth;
}
var comp = new Image();
comp.onload = function() {
var canvas = document.createElement('canvas');
canvas.width = targetWidth;
canvas.height = targetHeight;
canvas.getContext('2d').drawImage(this, 0, 0, targetWidth, targetHeight); 
canvas.toBlob(function(blob) {
self.$blob = blob;
}, self.$fileType || 'image/jpeg');
}
comp.src = this.rotate(imgObj, orient);
},
fitSize: function (originWidth, originHeight, maxWidth, maxHeight) {
var targetWidth = originWidth, targetHeight = originHeight;
if (originWidth > maxWidth || originHeight > maxHeight) {
if (originWidth / originHeight > maxWidth / maxHeight) {
targetWidth = maxWidth;
targetHeight = Math.round(maxWidth * (originHeight / originWidth));
} else {
targetHeight = maxHeight;
targetWidth = Math.round(maxHeight * (originWidth / originHeight));
}
}
return {
targetWidth : targetWidth, targetHeight : targetHeight
};
},
getPhotoOrientation : function (img) {
var orient;
EXIF.getData(img, function () {
orient = EXIF.getTag(this, 'Orientation');
});
return orient;
},
rotate : function (img, orient) {
var width = img.width, height = img.height, 
canvas = document.createElement('canvas'), ctx = canvas.getContext("2d");
if ([ 5, 6, 7, 8 ].indexOf(orient) > -1) {
canvas.width = height;
canvas.height = width;
} else {
canvas.width = width;
canvas.height = height;
}
switch (orient) {
case 2:
ctx.transform(-1, 0, 0, 1, width, 0);
break;
case 3:
ctx.transform(-1, 0, 0, -1, width, height);
break;
case 4:
ctx.transform(1, 0, 0, -1, 0, height);
break;
case 5:
ctx.transform(0, 1, 1, 0, 0, 0);
break;
case 6:
ctx.transform(0, 1, -1, 0, height, 0);
break;
case 7:
ctx.transform(0, -1, -1, 0, height, width);
break;
case 8:
ctx.transform(0, -1, 1, 0, 0, width);
break;
default:
ctx.transform(1, 0, 0, 1, 0, 0);
}
ctx.drawImage(img, 0, 0);
return canvas.toDataURL('image/jpeg');
}
}
});
Vue.component('attachment-picture-list', {
props : {
picCtx : String,
uploadUrl : String,
blankBg : String,
delImgUrl : String,
loadListUrl: String
},
data : function() {
return {
pics: []
};
},
template : '<table width="100%"><tr><td>\
<div class="label">相册图：</div>\
<ul>\
<li v-for="pic in pics" style="float:left;margin-right:1%;text-align:center;">\
<a href="picCtx + pic.path" target="_blank"><img :src="picCtx + pic.path" style="max-width: 180px;max-height: 160px;" /></a><br />\
<a href="#" @click="delPic(pic.id);">删 除</a>\
</li>\
</ul>\
</td><td>\
<aj-xhr-upload ref="attachmentPictureUpload" :action="uploadUrl" :is-img-upload="true" :img-place="blankBg"></aj-xhr-upload>\
</td></tr></table>',
mounted: function() {
this.loadAttachmentPictures();
this.$refs.attachmentPictureUpload.uploadOk_callback = this.loadAttachmentPictures;
},
methods : {
loadAttachmentPictures : function() {
var self = this;
aj.xhr.get(this.loadListUrl, function(json) {
self.pics = json.result;
});
},
delPic: function(picId) {
var self = this;
aj.xhr.dele(this.delImgUrl + picId, function(json) {
if(json.isOk) {
self.loadAttachmentPictures();
} else {
}
});
}
}
});
