/**

 * 表單控件
 */

// 日期选择器
Vue.component('aj-form-calendar', {
	data() {
		var date = new Date;
		return {
			date  : date,
			year  : date.getFullYear(),
			month : date.getMonth() + 1,
			day   : 1
		};
	},
	props: {
		showTime: false
	},
	beforeCreate() {	
		aj.getTemplate('form', 'aj-form-calendar', this);
	},
	mounted () {
		 this.$options.watch.date.call(this);
	},
	watch : {
		date (n) {
			this.year  = this.date.getFullYear();
			this.month = this.date.getMonth() + 1;
			this.render();
		}
	},
	methods : {
		// 画日历
		render() {
			var arr = this.getDateArr();// 用来保存日期列表
			var frag = document.createDocumentFragment();// 插入日期

			while (arr.length) {
				var row = document.createElement("tr"); // 每个星期插入一个 tr

				for (var i = 1; i <= 7; i++) { // 每个星期有7天
					var cell = document.createElement("td");
					if (arr.length) {
						var d = arr.shift();
						
						if (d) {
							cell.innerHTML = d;
							var text = this.year + '-' + this.month + '-' + d;
							cell.className = 'day day_' + text;
							cell.title = text;
							
							var on = new Date(this.year, this.month - 1, d);
							// 判断是否今日
							if (this.isSameDay(on, this.date)) {
								cell.classList.add('onToday');
								this.onToday && this.onToday(cell);
							}					
// 判断是否选择日期
// this.selectDay && this.onSelectDay && this.isSameDay(on, this.selectDay) &&
// this.onSelectDay(cell);
						}
					}
					row.appendChild(cell);
				}
				
				frag.appendChild(row);
			}

// 先清空内容再插入(ie的table不能用innerHTML)
// while (el.hasChildNodes())
// el.removeChild(el.firstChild);

			var tbody = this.$el.$("table tbody");
			tbody.innerHTML = '';
			tbody.appendChild(frag);
		},
		
		// 获取指定的日期
		getDate(dateType, month) {
			var nowYear = this.date.getFullYear(), nowMonth = this.date.getMonth() + 1; // 当前日期
			
			switch (dateType) {
				case 'preMonth':// 上一月
					this.date = new Date(nowYear, nowMonth - 2, 1);
					break;
				case 'nextMonth':// 下一月
					this.date = new Date(nowYear, nowMonth, 1);
					break;
				case 'setMonth':// 指定月份
					this.date = new Date(nowYear, month - 1, 1);
					break;
				case 'preYear':// 上一年
					this.date = new Date(nowYear - 1, nowMonth - 1, 1);
					break;
				case 'nextYear':// 下一年
					this.date = new Date(nowYear + 1, nowMonth - 1, 1);
					break;
			}
		},
		setMonth($event) {
			this.getDate('setMonth', Number($event.target.selectedOptions[0].value));
		},
		// 获取空白的非上月天数 + 当月天数
		getDateArr() {
			var arr = [];
			// 用 当月第一天 在一周中的日期值 作为 当月 离 第一天的天数
			for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
				arr.push(0);

			// 用 当月最后一天 在一个月中的 日期值 作为 当月的天数
			for (var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
				arr.push(i);

			return arr;
		},
		// 获取日期
		pickDay($event) {
			var el = $event.target, date = el.title;
			this.$emit('pick-date', date);
			return date;
		},
		pickupTime($event) {
			var time = this.$el.$('.hour').selectedOptions[0].value + ':' + this.$el.$('.minute').selectedOptions[0].value;
			this.$emit('pick-time', time);
		},

		/**
		 * 判断两个日期是否同一日
		 */
		isSameDay(d1, d2) {
			return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
		}
	}
});

// 帶有 input 輸入框的
Vue.component('aj-form-calendar-input', {
	data(){
		return {
			date : this.fieldValue, 
			time : ''
		};
	},
	props: {
		fieldName: { // 表单 name，字段名
			type: String, required: true
		},
		fieldValue: {
			type: String,// 表单值，可选的
			required: false, default : ''
		},
		dateOnly: { // 是否只处理日期，不考虑时间
			type: Boolean,// 表单值，可选的
			required: false, default: true
		},
		showTime: false,
		positionFixed: Boolean, // 是否采用固定定位
		placeholder: {// 提示文字
			type: String, default: '请输入日期'
		}
	},
	beforeCreate() {	
		aj.getTemplate('form', 'aj-form-calendar-input', this);
	},
	mounted() {
		if(this.positionFixed) 
			this.$el.$('.aj-form-calendar').classList.add('positionFixed');
	    // 2012-07-08
	    // firefox中解析 new Date('2012/12-23') 不兼容，提示invalid date 无效的日期
		// Chrome下可以直接将其格式化成日期格式，且时分秒默认为零
		// var arr = date.split('-'), now = new Date(arr[0], arr[1] - 1, arr[2],
		// " ", "", " ");
		if(this.fieldValue) {
			var arr = this.fieldValue.split(' ')[0], arr = arr.split('-'), date = new Date(arr[0], arr[1] - 1, arr[2], " ", "", " ");
			this.$refs.calendar.date = date;
		}
	},
	methods: {
		recTimeEvent(time) {
			this.time = time;
		},
		recEvent(date) {
			this.date = date.trim();
		},
		onMouseOver($event) {
			if(this.positionFixed) {
				var el = $event.currentTarget;
				var b = el.getBoundingClientRect();
				var c = this.$el.$('.aj-form-calendar');
					c.style.top  = (b.top + el.clientHeight - 0) + 'px';
					c.style.left = ((b.left - 0) + 0) +  'px';
			}
		}
	}
});

Vue.component('aj-form-between-date', {
	beforeCreate() {	
		aj.getTemplate('form', 'aj-form-between-date', this);
	},
	props: {
		isAjax:{ // 是否 AJAX 模式
			type: Boolean, default: true
		}
	},
	methods: {
		valid(e) {
			var start = this.$el.$('input[name=startDate]').value, end = this.$el.$('input[name=endDate]').value;
			
			if(!start||!end) {
				aj.showOk("输入数据不能为空");					
				e.preventDefault();
				return;
			}
			
			if(new Date(start) > new Date(end)) {
				aj.showOk("起始日期不能晚于结束日期");					
				e.preventDefault();
				return;
			}
			
			if(this.isAjax) {
				e.preventDefault();
				var grid = this.$parent.$parent;
				aj.apply(grid.$refs.pager.baseParam, {
					startDate: start, endDate: end
				});
				grid.reload();
			}
		}
	}
});

// HTML 在綫編輯器
// 注意：必须提供一个 <slot> 包含有 <textarea class="hide"
// name="content">${info.content}</textarea>
Vue.component('aj-form-html-editor', {
	props: {
		fieldName: { // 表单 name，字段名
			type: String, required: true
		},
		content: { // 内容
			type: String, required: false
		},
		basePath: { // iframe 的 <base href="${param.basePath}/" />路徑
			type: String, required: false, default: ''
		},
		uploadImageActionUrl: String
	},
	beforeCreate() {
/*		var xhr = new XMLHttpRequest();
		xhr.open("GET", this.ajResources.commonAsset + '/resources/htmleditor-tag.htm', false);// 同步方式请求
		xhr.send(null);
		this.$options.template = xhr.responseText;*/
		
		aj.getTemplate('form', 'aj-form-html-editor', this)
	},
	mounted() {
		var el = this.$el;
		this.iframeEl 	  = el.$('iframe');
		this.sourceEditor = el.$('textarea');
		this.iframeWin 	  = this.iframeEl.contentWindow;
		this.mode         = 'iframe'; // 当前可视化编辑 iframe|textarea
		
		this.toolbarEl = el.$('.toolbar');
		
		// 这个方法只能写在 onload 事件里面， 不写 onload 里还不执行
		this.iframeWin.onload = function() {
			this.iframeDoc 	= this.iframeWin.document;
			this.iframeDoc.designMode = 'on';
			this.iframeBody = this.iframeDoc.body;
			// 有内容
			this.sourceEditor.value && this.setValue(this.sourceEditor.value);
		    // 直接剪切板粘贴上传图片
			this.iframeDoc.addEventListener('paste', this.onImagePaste);
		}.bind(this);
	},
	methods: {
		/*
		 * 富文本编辑器中粘贴图片时，chrome可以得到e.clipBoardData.items并从中获取二进制数据，以便ajax上传到后台，
		 * 实现粘贴图片的功能。firefox中items为undefined，可选的方案：1将base64原样上传到后台进行文件存储替换，2将内容清空，待粘贴完毕后取图片src，再恢复现场
		 * https://stackoverflow.com/questions/2176861/javascript-get-clipboard-data-on-paste-event-cross-browser
		 */
		onImagePaste(event) {
			var items = event.clipboardData && event.clipboardData.items;
			var file = null; // file就是剪切板中的图片文件
			if (items && items.length) {
				// 检索剪切板items
				for (var i = 0; i < items.length; i++) {
					if (items[i].type.indexOf('image') !== -1) {
						if(window.isCreate) { // 有图片
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
				
				aj.img.changeBlobImageQuality(file, newBlob => {					       
					Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
						action: this.uploadImageActionUrl,
						progress: 0,
						uploadOk_callback(j) {
							self.format("insertImage", self.ajResources.imgPerfix + j.imgUrl);
						},
						$blob: newBlob,
						$fileName: 'foo.jpg'
					});
				});
			}  
		},
		onToolBarClk(e) {
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
					App.$refs.uploadLayer.show(json => {
						if(json && json.isOk)
							this.format("insertImage", json.fullUrl);
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
		
		format(type, para) {
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
		
		insertEl(html) {// 重複？
			this.iframeDoc.body.innerHTML = html;
		},
		
		saveRemoteImage2Local() {
			var str = [], remotePicArr = [], arr = this.iframeDoc.querySelectorAll('img');

			for(var i = 0, j = arr.length; i <j; i++) {
				var imgEl = arr[i], url = imgEl.getAttribute('src');
				
				if(/^http/.test(url)) {
					str.push(url);
					remotePicArr.push(imgEl);
				}
			}
			
			if(str.length)
				aj.xhr.post('../downAllPics/', json => {
					var _arr = json.result.pics;
					for(var i = 0, j = _arr.length; i <j; i++) 
						remotePicArr[i].src = "images/" + _arr[i];
					
					aj.alert.show('所有图片下载完成。');
				}, {pics: str.join('|')});
			else
				aj.alert.show('未发现有远程图片');
		},
		
		// 設置 HTML
		setValue(v) {
			setTimeout(() => {
				this.iframeWin.document.body.innerHTML = v;
// self.iframeBody.innerHTML = v;
			}, 500);
		},
		
		// 獲取 HTML
		getValue(cfg) {
			var result = this.iframeBody.innerHTML;
			
			if(cfg && cfg.cleanWord)
				result = this.cleanPaste(result);
			
			if(cfg && cfg.encode)
				result = encodeURIComponent(result);
			
			return result;
		},
		
		// MSWordHtmlCleaners.js https://gist.github.com/ronanguilloux/2915995
		cleanPaste(html) {
		    // Remove additional MS Word content
		    html = html.replace(/<(\/)*(\\?xml:|meta|link|span|font|del|ins|st1:|[ovwxp]:)((.|\s)*?)>/gi, ''); // Unwanted tags
		    html = html.replace(/(class|style|type|start)=("(.*?)"|(\w*))/gi, ''); // Unwanted sttributes
		    html = html.replace(/<style(.*?)style>/gi, '');   // Style tags
		    html = html.replace(/<script(.*?)script>/gi, ''); // Script tags
		    html = html.replace(/<!--(.*?)-->/gi, '');        // HTML comments
		    
		    return html;
		},
		
		// 切換 HTML 編輯 or 可視化編輯
		setMode() {
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
		
		// 使图片灰色
		grayImg(isGray) {
			this.toolbarEl.$('span', function(item) {
				if(item.className.indexOf('switchMode') != -1) {
					item.style.color = isGray ? 'red' : '';
				} else {
					item.style.filter = isGray ? 'grayscale(100%)' : '';
				}
			});
		},
		
		onFontfamilyChoserClk(e) {
			var el = e.target; 
			
			this.format('fontname', el.innerHTML);
			// 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：
			var menuPanel = el.parentNode;
			menuPanel.style.display = 'none';

			setTimeout(() => {
				menuPanel.style.display = '';
			}, 300);
		},
		
		onFontsizeChoserClk(e) {	
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
			// 定义变量
			var cl = ['00', '33', '66', '99', 'CC', 'FF'], a, b, c, d, e, f, i, j, k, T;
			// 创建head
			var h = '<div class="colorhead"><span class="colortitle">颜色选择</span></div>\
						<div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>';
			
			// 创建body  [6 x 6的色盘]
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
		// 清理冗余 HTML
		cleanHTML(){
		    this.iframeBody.innerHTML = HtmlSanitizer.SanitizeHtml(this.iframeBody.innerHTML);
		}
	}
});

// https://github.com/jitbit/HtmlSanitizer/blob/master/HtmlSanitizer.js
var HtmlSanitizer = new (function () {
	var tagWhitelist_ = {
		'A': true, 'ABBR': true, 'B': true, 'BLOCKQUOTE': true, 'BODY': true, 'BR': true, 'CENTER': true, 'CODE': true, 'DIV': true, 'EM': true, 'FONT': true,
		'H1': true, 'H2': true, 'H3': true, 'H4': true, 'H5': true, 'H6': true, 'HR': true, 'I': true, 'IMG': true, 'LABEL': true, 'LI': true, 'OL': true, 'P': true, 'PRE': true,
		'SMALL': true, 'SOURCE': true, 'SPAN': true, 'STRONG': true, 'TABLE': true, 'TBODY': true, 'TR': true, 'TD': true, 'TH': true, 'THEAD': true, 'UL': true, 'U': true, 'VIDEO': true
	};

	var contentTagWhiteList_ = { 'FORM': true }; //tags that will be converted to DIVs
	var attributeWhitelist_ = { 'align': true, 'color': true, 'controls': true, 'height': true, 'href': true, 'src': true, 'style': false, 'target': true, 'title': true, 'type': true, 'width': true };
	var cssWhitelist_ = { 'color': true, 'background-color': true, 'font-size': true, 'text-align': true, 'text-decoration': true, 'font-weight': true };
	var schemaWhiteList_ = [ 'http:', 'https:', 'data:', 'm-files:', 'file:', 'ftp:' ]; //which "protocols" are allowed in "href", "src" etc
	var uriAttributes_ = { 'href': true, 'action': true };

	this.SanitizeHtml = function(input) {
		input = input.trim();
		if (input == "") return ""; //to save performance and not create iframe

		//firefox "bogus node" workaround
		if (input == "<br>") return "";

		var iframe = document.createElement('iframe');
		if (iframe['sandbox'] === undefined) {
			alert('Your browser does not support sandboxed iframes. Please upgrade to a modern browser.');
			return '';
		}
		iframe['sandbox'] = 'allow-same-origin';
		iframe.style.display = 'none';
		document.body.appendChild(iframe); // necessary so the iframe contains a document
		var iframedoc = iframe.contentDocument || iframe.contentWindow.document;
		if (iframedoc.body == null) iframedoc.write("<body></body>"); // null in IE
		iframedoc.body.innerHTML = input;

		function makeSanitizedCopy(node) {
			if (node.nodeType == Node.TEXT_NODE) {
				var newNode = node.cloneNode(true);
			} else if (node.nodeType == Node.ELEMENT_NODE && (tagWhitelist_[node.tagName] || contentTagWhiteList_[node.tagName])) {

				//remove useless empty spans (lots of those when pasting from MS Outlook)
				if ((node.tagName == "SPAN" || node.tagName == "B" || node.tagName == "I" || node.tagName == "U")
					&& node.innerHTML.trim() == "") {
					return document.createDocumentFragment();
				}

				if (contentTagWhiteList_[node.tagName])
					newNode = iframedoc.createElement('DIV'); //convert to DIV
				else
					newNode = iframedoc.createElement(node.tagName);

				for (var i = 0; i < node.attributes.length; i++) {
					var attr = node.attributes[i];
					if (attributeWhitelist_[attr.name]) {
						if (attr.name == "style") {
							for (s = 0; s < node.style.length; s++) {
								var styleName = node.style[s];
								if (cssWhitelist_[styleName])
									newNode.style.setProperty(styleName, node.style.getPropertyValue(styleName));
							}
						}
						else {
							if (uriAttributes_[attr.name]) { //if this is a "uri" attribute, that can have "javascript:" or something
								if (attr.value.indexOf(":") > -1 && !startsWithAny(attr.value, schemaWhiteList_))
									continue;
							}
							newNode.setAttribute(attr.name, attr.value);
						}
					}
				}
				for (i = 0; i < node.childNodes.length; i++) {
					var subCopy = makeSanitizedCopy(node.childNodes[i]);
					newNode.appendChild(subCopy, false);
				}
			} else {
				newNode = document.createDocumentFragment();
			}
			return newNode;
		};

		var resultElement = makeSanitizedCopy(iframedoc.body);
		document.body.removeChild(iframe);
		return resultElement.innerHTML
			.replace(/<br[^>]*>(\S)/g, "<br>\n$1")
			.replace(/div><div/g, "div>\n<div"); //replace is just for cleaner code
	}

	function startsWithAny(str, substrings) {
		for (var i = 0; i < substrings.length; i++) {
			if (str.indexOf(substrings[i]) == 0) {
				return true;
			}
		}
		return false;
	}

	this.AllowedTags = tagWhitelist_;
	this.AllowedAttributes = attributeWhitelist_;
	this.AllowedCssStyles = cssWhitelist_;
	this.AllowedSchemas = schemaWhiteList_;
});

/**
 * 表单验证器
 * 
 * https://www.w3cplus.com/css/form-validation-part-2-the-constraint-validation-api-javascript.html
 * https://github.com/cferdinandi/validate/blob/master/src/js/validate.js
 */
;(function(){
	
	// 验证字段
	function hasError (field) {
		return
		if(!field || !field.getAttribute){
			console.log(field)
		};
		
		if (field.getAttribute('form') != null || field.disabled || field.type === 'file' || field.type === 'reset' || field.type === 'submit' || field.type === 'button') 
			return;
		
		// 获取 validity
		var validity = field.validity;
		if(!validity)return 'No validity';
		
		// 如果通过验证,就返回 undefined
		if (validity.valid) return;
		
		// 如果是必填字段但是字段为空时
		if (validity.valueMissing) return '该项是必填项';
		
		// 如果类型不正确
		if (validity.typeMismatch) {
			// Email
			if (field.type === 'email') return '请输入有效的邮件地址';
			
			// URL
			if (field.type === 'url') return '请输入一个有效的网址';
			
			return '请输入正确的类型';
		}
		
		// 如果输入的字符数太短
		if (validity.tooShort) return 'Please lengthen this text to ' + field.getAttribute('minLength') + ' characters or more. You are currently using ' + field.value.length + ' characters.';
		
		// 如果输入的字符数太长
		if (validity.tooLong) return 'Please shorten this text to no more than ' + field.getAttribute('maxLength') + ' characters. You are currently using ' + field.value.length + ' characters.';
		
		// 如果数字输入类型输入的值不是数字
		if (validity.badInput) return 'Please enter a number.';
		
		// 如果数字值与步进间隔不匹配
		if (validity.stepMismatch) return 'Please select a valid value.';
		
		// 如果数字字段的值大于max的值
		if (validity.rangeOverflow) return 'Please select a value that is no more than ' + field.getAttribute('max') + '.';
		
		// 如果数字字段的值小于min的值
		if (validity.rangeUnderflow) return 'Please select a value that is no less than ' + field.getAttribute('min') + '.';
		
		// 如果模式不匹配
		if (validity.patternMismatch) {
			// 如果包含模式信息，返回自定义错误
			if (field.hasAttribute('title')) return field.getAttribute('title');
			
			// 否则, 返回一般错误
			return '格式要求不正确';
		}
		
		// 如果是其他的错误, 返回一个通用的 catchall 错误
		return 'The value you entered for this field is invalid.';
	}
	
	function showError(field, error, isNewLine) {
		return
	    // 将错误类添加到字段
	    field.classList.add('error');

	    // 获取字段id 或者 name
	    var id = field.id || field.name;
	    if (!id) return;

	    // 检查错误消息字段是否已经存在 如果没有, 就创建一个
	    var message = field.form.querySelector('.error-message#error-for-' + id );
	    
	    if (!message) {
	        message = document.createElement('div');
	        message.className = 'error-message';
	        message.id = 'error-for-' + id;
	        field.parentNode.insertBefore( message, field.nextSibling );
	    }

	    // 添加ARIA role 到字段
	    field.setAttribute('aria-describedby', 'error-for-' + id);

	    // 更新错误信息
	    message.innerHTML = error;

	    // 显示错误信息
	    if(!isNewLine)
	    	message.style.display = 'inline-block';
	    message.style.visibility = 'visible';
	};
	
	// 移除所有的错误信息
	function removeError(field) {
		return
	    // 删除字段的错误类
	    field.classList.remove('error');

	    // 移除字段的 ARIA role
	    field.removeAttribute('aria-describedby');

	    // 获取字段的 id 或者 name
	    var id = field.id || field.name;
	    if (!id) return;

	    // 检查DOM中是否有错误消息
	    var message = field.form.querySelector('.error-message#error-for-' + id + '');
	    if (!message) return;

	    // 如果是这样的话, 就隐藏它
	    message.innerHTML = '';
	    message.style.display = 'none';
	    message.style.visibility = 'hidden';
	}
	
	aj.formValidator = function(el) {
		var isMsgNewLine = el.dataset.msgNewline ==="true";
		el.setAttribute('novalidate', true);
		
		// 监听所有的失去焦点的事件
		document.addEventListener('blur', event => {

			var error = hasError(event.target);

		    // 如果有错误,就把它显示出来
		    if (error) {
		        showError(event.target, error, isMsgNewLine);
		        return;
		    }
		    
		    // 否则, 移除所有存在的错误信息
		    removeError(event.target);
		}, true);
	}
	
	aj.formValidator.hasError = hasError;
	
	aj.formValidator.onSubmit = function(form) {
		// 获取所有的表单元素
	    var fields = form.elements;

	    // 验证每一个字段
	    // 将具有错误的第一个字段存储到变量中以便稍后我们将其默认获得焦点
	    var error, hasErrors;
	    for (var i = 0; i < fields.length; i++) {
	        error = hasError(fields[i]);
	        
	        if (error) {
	            showError(fields[i], error);
	            if (!hasErrors) 
	                hasErrors = fields[i];
	        }
	    }
	    
	    // 如果有错误,停止提交表单并使出现错误的第一个元素获得焦点
	    if (hasErrors) {
	        hasErrors.focus();
	        return false;
	    }
	    
	    return true;
	}
})();

if(!aj.form)
	aj.form = {};
	
// dep
aj.form.betweenDate = function(el) {	
	new Vue({
		el : el,
		methods: {
			valid(e) {
				var start = this.$el.$('input[name=startDate]').value, end = this.$el.$('input[name=endDate]').value;
				
				if(!start||!end) {
					aj.showOk("输入数据不能为空");					
					e.preventDefault();
				}
				
				if(new Date(start) > new Date(end)) {
					aj.showOk("起始日期不能晚于结束日期");					
					e.preventDefault();
				}
			}
		}
	});
}

/**
 * 上传组件
 */
Vue.component('aj-xhr-upload', {
	data () {
		return {
			isFileSize : false,			// 文件大小检查
			isExtName : false,			// 文件扩展名检查
			isImgSize : false, 			// 图片分辨率大小检查
			errMsg : null,				// 错误信息
			newlyId : null,				// 成功上传之后的文件 id
			radomId : Math.round(Math.random() * 1000),		// 不重复的 id
			uplodedFileUrl: null,
			uploadOk_callback: json => {// 回调函数
				this.uplodedFileUrl = json.imgUrl;
				
				if(this.hiddenField)
					this.$el.$('input[name=' + this.hiddenField + ']').value = json.imgUrl;
				
				ajaxjs.xhr.defaultCallBack(json);
			},				
	    	imgBase64Str : null,			// 图片的 base64 形式，用于预览
	    	progress : 0,
	    	fileName: ''
		};
	},
	props : {
		action: {			// 上传路径
			type: String,  required: true
		}, 		
		fieldName: String, 	// input name 字段名
	    limitSize: Number,
	    hiddenField: {		// 上传后的文件名保存在这个隐藏域之中
	    	type: String, default: null
	    },
	    hiddenFieldValue: String,
	    limitFileType: String,
	    accpectFileType: String,// 可以上传类型
	    isImgUpload : Boolean, 	// 是否图片上传
	    imgPlace : String,		// 图片占位符，用户没有选定图片时候使用的图片
	    imgMaxWidth : {type : Number, default : 1920},
	    imgMaxHeight: {type : Number, default : 1680},
	    buttonBottom: Boolean  // 上传按钮是否位于下方
	},
	beforeCreate() {	
		aj.getTemplate('form', 'aj-xhr-upload', this);
	},
	methods: {
		getFileName() {
			var v = this.$el.$('input[type=file]').value;
			var arr = v.split('\\');
			this.fileName = arr.pop().trim();
		},
		onUploadInputChange($event) {
			var fileInput = $event.target;
			var ext = fileInput.value.split('.').pop(); // 扩展名
			if(!fileInput.files || !fileInput.files[0]) return;
			
			this.$fileObj = fileInput.files[0]; // 保留引用
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
			
			this.getFileName();
		},
		readBase64(file) {
			var reader = new FileReader(), self = this;
			
			reader.onload = function(e) {
				var imgBase64Str = e.target.result;
				self.imgBase64Str = imgBase64Str;
				
				if(self.isImgUpload) {
					var imgEl = new Image();
					imgEl.onload = function() {
						if(file.size > 300 * 1024)  // 大于 300k 才压缩
							self.compressImg(imgEl);
						
						if (imgEl.width > self.maxWidth || imgEl.height > self.maxHeight) {
							self.isImgSize = false;
							self.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
						} else 
							self.isImgSize = true;
					}
					
					imgEl.src = imgBase64Str;
					// 文件头判别，看看是否为图片
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
		
		doUpload() {
			var fd = new FormData();
			
			if(this.$blob)
				fd.append("file", this.$blob, this.$fileName);
			else 
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
		
		// 图片压缩
		compressImg(imgObj) {
			var self = this;
			var maxWidth = 1000, maxHeight = 1500;
			var fitSizeObj = this.fitSize(imgObj.width, imgObj.height, maxWidth, maxHeight);
			var targetWidth = fitSizeObj.targetWidth, targetHeight = fitSizeObj.targetHeight;
			
			var orient = this.getPhotoOrientation(imgObj);// 获取照片的拍摄方向
			
			if (orient == 6) {
				targetWidth = fitSizeObj.targetHeight;
				targetHeight = fitSizeObj.targetWidth;
			}
			
			var comp = new Image();
			comp.onload = function() {
				var canvas = document.createElement('canvas');
				canvas.width = targetWidth;
				canvas.height = targetHeight;
				canvas.getContext('2d').drawImage(this, 0, 0, targetWidth, targetHeight); // 图片压缩
				
				// canvas转为blob并上传
				canvas.toBlob(function(blob) {
					self.$blob = blob;
				}, self.$fileType || 'image/jpeg');
			}
			
			comp.src = this.rotate(imgObj, orient);
		},
		fitSize(originWidth, originHeight, maxWidth, maxHeight) {
		    // 目标尺寸
		    var targetWidth = originWidth, targetHeight = originHeight;
		    
		    // 图片尺寸超过400x400的限制
		    if (originWidth > maxWidth || originHeight > maxHeight) {
		        if (originWidth / originHeight > maxWidth / maxHeight) {
		            // 更宽，按照宽度限定尺寸
		            targetWidth = maxWidth;
		            targetHeight = Math.round(maxWidth * (originHeight / originWidth));
		        } else {
		            targetHeight = maxHeight;
		            targetWidth = Math.round(maxHeight * (originWidth / originHeight));
		        }
		    }
		    
		    return {targetWidth : targetWidth, targetHeight : targetHeight};
		},
		getPhotoOrientation(img) {
		    var orient;
		    EXIF.getData(img, function () {
		        orient = EXIF.getTag(this, 'Orientation');
		    });
		    
		    return orient;
		},
		rotate(img, orient) {
			var width = img.width, height = img.height, 
			canvas = document.createElement('canvas'), ctx = canvas.getContext("2d");
			
			// set proper canvas dimensions before transform & export
			if ([ 5, 6, 7, 8 ].indexOf(orient) > -1) {
				canvas.width = height;
				canvas.height = width;
			} else {
				canvas.width = width;
				canvas.height = height;
			}
			
			// transform context before drawing image
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

// 相册列表
Vue.component('attachment-picture-list', {
	props: {
		picCtx: String,
		uploadUrl: String,
		blankBg: String,
		delImgUrl: String,
		loadListUrl: String
	},
	data() {
		return {
			pics: []
		};
	},
	beforeCreate() {	
		aj.getTemplate('form', 'attachment-picture-list', this);
	},
	mounted() {
		this.loadAttachmentPictures();
		this.$refs.attachmentPictureUpload.uploadOk_callback = this.loadAttachmentPictures;
	},
	methods: {
		loadAttachmentPictures() {
			aj.xhr.get(this.loadListUrl, j => this.pics = j.result);
		},
		delPic(picId) {
			aj.showConfirm("确定删除相册图片？", () => {
				aj.xhr.dele(this.delImgUrl + picId + "/", j => {
					if(j.isOk)
						this.loadAttachmentPictures();
				});
			}); 
		}
	}
});

/**
* 将上传控件嵌入到一个浮出层中
*/
Vue.component('aj-popup-upload', {
	data() {
		return {
			text : {}
		};
	},
	props: {
		uploadUrl: {// 上传接口地址
			type: String, required: true
		},
		imgName: { // 貌似没用
			type: String, required: false
		},
		imgId: {// 貌似没用
	      type: Number, required: false
	    },
		imgPlace: String // 图片占位符，用户没有选定图片时候使用的图片
	},
	beforeCreate() {	
		aj.getTemplate('form', 'aj-popup-upload', this);
	},
	mounted() {
		var obj = this.$refs.uploadControl;
		this.text = {maxSize: obj.limitSize || 600, maxHeight: obj.imgMaxHeight, maxWidth: obj.imgMaxWidth};
	},
	methods: {
		/**
		 * 显示上传控件
		 * 
		 * @param {Function} callback 上传成功之后的回调函数
		 */
		show(callback) {
			if(callback)
				this.$refs.uploadControl.uploadOk_callback = callback;
			
			this.$children[0].show();
		}
	}
});

//全国省市区 写死属性
Vue.component('aj-china-area', {
	beforeCreate() {	
		aj.getTemplate('form', 'aj-china-area', this);
	},
    props: {
        provinceCode: String,
        cityCode: String,
        districtCode: String
    },
   data() {
		if(!China_AREA)
			throw '中国行政区域数据 脚本没导入';
	   
        return {
        	province: this.provinceCode || '',
        	city: this.cityCode || '',
        	district: this.districtCode || '',
            addressData: China_AREA
        };
    },
    watch:{ // 令下一级修改
        province(val, oldval) {
//            if(val !== oldval) 
//                this.city = '';
            
        },
//        city(val, oldval) {
//            if(val !== oldval)
//                this.district = '';
//        }
    },
   
    computed: {
        citys() {
            if(!this.province)
                return;

            return this.addressData[this.province];
        },
        districts() {
            if(!this.city)
                return;

            return this.addressData[this.city];
        }
    }
});

//polyfill JavaScript-Canvas-to-Blob 解决了 HTMLCanvasElement.toBlob 的兼容性
//https://github.com/blueimp/JavaScript-Canvas-to-Blob
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