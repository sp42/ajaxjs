<%@page pageEncoding="UTF-8"%>
	<h4>简单列表</h4>
	<p>属性说明：</p>
	<table class="ajaxjs-borderTable">
		<tr>
			<th>属性</th>
			<th>含义</th>
			<th>类型</th>
			<th>是否必填</th>
		</tr>
		<tr>
			<td>apiUrl</td>
			<td>JSON 接口地址</td>
			<td>String</td>
			<td>Y</td>
		</tr>
		<tr>
			<td>data</td>
			<td>数据对象，包含 result 结果数组和 baseParam 每次请求都附带的参数对象</td>
			<td>Boolean</td>
			<td>N</td>
		</tr>
	</table>
	
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-simple-list api-url="${ctx}/api/article/"></aj-simple-list></xmp></pre>
	
	<p>演示：</p>
	<div class="tab">
		<aj-simple-tab></aj-simple-tab>
	</div>
	
	
	<h4>分页列表</h4>
	<p>支持 start/limt 的分页</p>
	
		<p>属性说明：</p>
	<table class="ajaxjs-borderTable">
		<tr>
			<th>属性</th>
			<th>含义</th>
			<th>类型</th>
			<th>是否必填</th>
		</tr>
		<tr>
			<td>apiUrl</td>
			<td>JSON 接口地址</td>
			<td>String</td>
			<td>Y</td>
		</tr>
		<tr>
			<td>data</td>
			<td>数据对象，包含 result 结果数组和 baseParam 每次请求都附带的参数对象</td>
			<td>Boolean</td>
			<td>N</td>
		</tr>
		<tr>
			<td>initPageSize</td>
			<td>指定的分页大小</td>
			<td>Number</td>
			<td>N，默认5</td>
		</tr>
		<tr>
			<td>&lt;slot&gt;</td>
			<td>指定 item 显示的 markup</td>
			<td>String</td>
			<td>N，默认 a
			</td>
		</tr>
	</table>
	
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-page-list api-url="${ctx}/api/article/" :init-page-size="8">
	<div slot-scope="item" :onclick="'location.assign(\'' + item.id + '/\');'">
		<div class="imgHolder">
			<img src="https://static.rong360.com/pimg/f8/f80090d3b132aa00aa67f08d52cfdf6e3402ff5a.jpeg" />
		</div>
		<div class="info">
			<h3>{{item.name}}</h3>
			<section>
				<table>
					<tr>
						<td>无需抵押 <br /> 上班族 可申请<br /> 1天放款<br />
						</td>
						<td>无需抵押 <br /> 上班族 可申请<br /> 1天放款<br />
						</td>
						<td>无需抵押 <br /> 上班族 可申请<br /> 1天放款<br />
						</td>
						<td style="text-align:right;">
							<button>查看详情</button>
						</td>
					</tr>
				</table>
			</section>
		</div>
	</div>
</aj-page-list></xmp></pre>
	


	<script>
	//	new Vue({el:'.tab'});
	//	new Vue({el:'.tab_hoz'});
	</script>	