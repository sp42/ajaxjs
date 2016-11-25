<%@tag pageEncoding="UTF-8" description="走马灯"%>
	<input id="startBtn" type="button" value="开始滚动" />
	<input id="stopBtn" type="button" value="停止滚动">
	<div id="content">这是一段滚动的文字</div>
	<script type="text/javascript">
		var Util = {
			$ : function(e) {
				return typeof e == "string" ? document.getElementById(e) : e;
			}
		};
		function ScrollText(s, fn, t) {
			this.s = s.split("");
			this.fn = fn;
			this.t = t || 300;
		}
		ScrollText.prototype = {
			start : function() {
				var fn = this.fn;
				var s = this.s;
				clearInterval(this.interval);
				this.interval = setInterval(function() {
					s.push(s.shift());
					fn(s.join(""));
				}, this.t);
			},
			stop : function() {
				clearInterval(this.interval);
			}
		}
		var scroll = new ScrollText("这是一段滚动的文字", function(s) {
			Util.$("content").innerHTML = s;
		})
		var startBtn = Util.$("startBtn"), stopBtn = Util.$("stopBtn");
		startBtn.onclick = function() {
			scroll.start();
		}
		stopBtn.onclick = function() {
			scroll.stop();
		}
	</script>
