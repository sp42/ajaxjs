<%@tag pageEncoding="UTF-8" %>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@attribute name="list" required="true" type="java.util.List" description="标签类型"%>

<div class="achor" style="text-align: right;margin:2% 5%;"> 
	<a href="..">
		<img src="${commonAssetIcon}/add.png" style="width:16px;vertical-align: sub;" /> 添加新地址
	</a>	
</div>

<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>

<!-- 列表渲染，采用传统后端 MVC 渲染 -->
<div class="list-table">
	<table align="center">
		<colgroup>
			<col />
			<col />
			<col />
			<col />
			<col />
			<col />
			<col style="text-align: center;" align="center" />
		</colgroup>
		<thead>
			<tr>
				<th>收货人</th>
				<th>手机/固话</th>
				<th>省市区</th>
				<th>收货地址</th>
				<th>是否默认</th>
				<th>控制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="6"></td>
			</tr>
		</tfoot>
		<tbody>
			<c:foreach items="${list}">
				<tr>
					<td>${item.name}</td>
					<td>${item.phone}</td>
					<td>
						<script>
							document.write(China_AREA[86][${item.locationProvince}]);
							document.write(China_AREA[${item.locationProvince}][${item.locationCity}]);
							document.write(China_AREA[${item.locationCity}][${item.locationDistrict}])
						</script>		
					</td>
					<td>${item.address}</td>
					<td>${item.isDefault ? '是' : '否'}</td>
					<td>
						<a href="../${item.id}/">
							<img src="${commonAssetIcon}/user.png" style="width:16px;vertical-align: sub;" />修改
						</a> 
						<a href="javascript:aj.admin.del('${item.id}', '${item.name}');">
							<img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" />删 除
						</a>
					</td>
				</tr>
			</c:foreach>
		</tbody>
	</table>
</div>