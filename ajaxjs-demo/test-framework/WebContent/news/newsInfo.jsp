<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs_config" prefix="config"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:common type="content" banner="../../images/news.jpg" showPageHelper="true">
	<jsp:attribute name="left">
		<div> </div>
	</jsp:attribute>
	<jsp:attribute name="body">
		<style>
		
			h2{
			
				text-align:center;
				padding:5%;
			}
			h4{
				padding: 10px;
				text-align:right;
				color: gray;
			}
			pre{
				border:1px solid lightgray;
				border-left-width:5px;
				color:gray;
				padding:2%;
			}
		</style>
		<h2>${info.name}</h2>
		<h4 class="createDate"> 创建日期：<c:dateFormatter value="${info.createDate}" format="yyyy-MM-dd" /></h4> 
		<article>${info.content}</article>
		<script>
			// 分类
			new Vue({
				el : '.body .left div',
				data : {
					result : []
				},
				template : '<ul><li><a href="#" @click="fireEvent(0)">全部新闻</a></li>\
						<li v-for="(item, index) in result"><a href="#" @click="fireEvent(item.id)">{{item.name}}</a></li></ul>',
				mounted : function() {
					ajaxjs.xhr.get('${ctx}/news/catelogList', function(json) {
						this.result = json.result;
					}.bind(this));
				}, 
				methods : {
					fireEvent:function(catelogId){
						// 0=全部分类
						this.BUS.$emit('base-param-change', {catelogId:catelogId});
					}
				}
			});
			
		</script>
		
		<script>
			// 修正图片地址 patch
			var arr = document.querySelectorAll('article img');
			for (var i = 0; i < arr.length; i++) {
				var img = arr[i], src = img.getAttribute('src')
				if (!/^\.\./.test(src) && !/^\//.test(src) && !/^http/.test(src)) {
					img.src = '../../' + src;
				}
			}
		</script>
		
	</jsp:attribute>
</tags:common>
