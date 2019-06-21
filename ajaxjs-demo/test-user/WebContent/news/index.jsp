<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content banner="../images/news.jpg" bodyClass="">
	<jsp:attribute name="left">
		<div> </div>
	</jsp:attribute>
	<jsp:attribute name="body">
		<style>
			.newsList li {
			    padding: 25px 0 20px 0;
			    border-bottom: 1px dashed #e8e8e8;
			    overflow: hidden;
			}
			li h2{
				color:gray;
				font-size: 1.2rem;
			}
			li h3 {
			    font-size: .9rem;
			    color: #6f6f6f;
			    text-align: right;
			    letter-spacing: 0;
			    padding: 8px 0;
			}
			
			li .more { 
			    text-align: right;
			    font-size: .9rem;
			    text-indent: 0;
			}
		</style>
		<div class="newsList">
			<aj-page-list api-url="${ctx}/news/api" :init-page-size="5">
				<template slot-scope="item">
					<a :href="item.id + '/'">
						<h2>{{item.name}}</h2>
						<h3>日期：{{item.createDate}} 分类：{{item.catelogName}}</h3>
						<p>{{item.intro}}</p>
						<p class="more">查看更多...</p>
					</a>
				</template>
			</aj-page-list>
		</div>
		<script>
			new Vue({el : '.newsList'});
			
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
	</jsp:attribute>
</tags:content>
