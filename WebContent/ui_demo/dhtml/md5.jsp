<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "其他");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>MD5</h4>
	<hr class="ajaxjs-hr" />
	<p>
		字符串的“abc”的 MD5 结果应该是 <span class="result1">900150983cd24fb0d6963f7d28e17f72</span>
		<button  class="ajaxjs-btn" onclick="test();">Test</button>
	</p>
	<div class="center result2"></div>
	<div class="center result"></div>

	<script src="${pageContext.request.contextPath}/asset/common/js/libs/md5.min.js"></script>
	<script>
		String.prototype.format = function() {
			var str = this;
			if (typeof (arguments[0]) == 'string'
					|| typeof (arguments[0]) == 'number') {
				for (var i = 0, j = arguments.length; i < j; i++) {
					str = str.replace(new RegExp('\\{' + i + '\\}', 'g'),
							arguments[i]);
				}
			} else {
				for ( var i in arguments[0]) {
					str = str.replace(new RegExp('\\{' + i + '\\}', 'g'),
							arguments[0][i]); // 大小写敏感  
				}
			}

			return str;
		}
		function test() {
			document.querySelector('.result2').innerHTML = 'md5("abc")结果：<span class="md5Result">{0}</span>'
					.format(md5('abc'));
			if (document.querySelector('.result1').innerHTML == document
					.querySelector('.md5Result').innerHTML) {
				document.querySelector('.result').innerHTML = '<span style="color:green;">测试通过<span>';
			} else
				document.querySelector('.result').innerHTML = '<span style="color:red;">测试不通过<span>';
		}
	</script>


	<h4>Base64</h4>
	<hr class="ajaxjs-hr" />
	<p>
		字符串的“abc”的 base64 编码应该是 <span class="result1_1">YWJj</span> (UTF-8)
		<button class="ajaxjs-btn" onclick="test2();">Test</button>
	</p>
	<div class="center result2_1"></div>
	<div class="center result_1"></div>

	<script src="${pageContext.request.contextPath}/asset/common/js/libs/base64.js"></script>
	<script>
		function test2() {
			document.querySelector('.result2_1').innerHTML = 'base("abc")结果：<span class="base64Result">{0}</span>'
					.format(base64.encode('abc'));
			if (document.querySelector('.result1_1').innerHTML == document
					.querySelector('.base64Result').innerHTML) {
				document.querySelector('.result_1').innerHTML = '<span style="color:green;">测试通过<span>';
			} else
				document.querySelector('.result_1').innerHTML = '<span style="color:red;">测试不通过<span>';
		}
	</script>



	<h4>一键繁体</h4>
	<hr class="ajaxjs-hr" />
	<script>
		/*
		 * --------------------------------------------------------
		 * 函数委托 参见 http://blog.csdn.net/zhangxin09/article/details/8508128
		 * @return {Function}
		 * --------------------------------------------------------
		 */
		Function.prototype.delegate = function() {
			var self = this, scope = this.scope, args = arguments, aLength = arguments.length, fnToken = 'function';

			return function() {
				var bLength = arguments.length, Length = (aLength > bLength) ? aLength
						: bLength;

				// mission one:
				for (var i = 0; i < Length; i++)
					if (arguments[i])
						args[i] = arguments[i]; // 拷贝参数

				args.length = Length; // 在 MS jscript下面，arguments作为数字来使用还是有问题，就是length不能自动更新。修正如左:

				// mission two:
				for (var i = 0, j = args.length; i < j; i++) {
					var _arg = args[i];
					if (_arg && typeof _arg == fnToken && _arg.late == true)
						args[i] = _arg.apply(scope || this, args);
				}

				return self.apply(scope || this, args);
			};
		};
	</script>
	<script src="${pageContext.request.contextPath}/asset/common/js/libs/chinese.js"></script>
	<div class="center">
		<button class="ajaxjs-btn"
			onclick="translate(document.body, traditionalized);">切换到正体中文</button>
		<button class="ajaxjs-btn"
			onclick="translate(document.body, simplized);">切换到简体</button>
	</div>



	<%@include file="../public/footer.jsp"%>
</body>
</html>