<%@page pageEncoding="UTF-8" import="com.ajaxjs.cms.user.role.RightConstant"%>
<%
	long num = 0;
	num = RightConstant.set(num, 1, true);
/*
	num = RightConstant.set(num, 2, true);
	num = RightConstant.set(num, 8, true);
*/
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
</jsp:include>
<style type="text/css">
	th {
		padding: 0 !important;
		font-size:.9rem;
	}
	
	caption {
		font-size: 1.2rem;
		padding:2%;
		font-weight: bold;
	}
	
	/*模拟对角线*/
	.out {
		border-top: 40px #D6D3D6 solid; /*上边框宽度等于表格第一行行高*/
		width: 0px; /*让容器宽度为0*/
		height: 0px; /*让容器高度为0*/
		border-left: 180px #BDBABD solid; /*左边框宽度等于表格第一行第一格宽度*/
		position: relative; /*让里面的两个子容器绝对定位*/
	}
	
	b {
		font-style: normal;
		display: block;
		position: absolute;
		top: -40px;
		left: -100px;
		width: 85px;
	}
	
	em {
		font-style: normal;
		display: block;
		position: absolute;
		top: -25px;
		left: -170px;
		width: 55x;
	}
	
	.t1{
		text-align:center;
	}
	.crud label{
		cursor: pointer;
	}
	.crud.disabled label {
		color:gray;
		cursor: not-allowed;
	}
</style>
</head>
<body>
	<table class="ajaxjs-borderTable" align="center" width="90%">
		<caption>权限管理系统 {{currentUserGroup}}</caption>
		<tr>
		  <th style="width: 220px;">用户组</th>
			<th style="width: 180px;">
				<div class="out">
					<b>操作权限</b> <em>功能模块</em>				</div>			</th>
			<th>子模块</th>
			<th>操作权限</th>
		</tr>
		<tr>
		  <td rowspan="26" valign="top">
		  	<!-- 树控件 -->
		  	<aj-tree url="/admin/user/user_group/list/?limit=99" @treenodeclick="captureBubble" top-node-name="栏目"></aj-tree>		  </td>
		  <td rowspan="3" class="t1">前/后台</td>
		  <td>允许浏览前台</td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.FRONTEND_ALLOW_ENTNER%>)" :res-id="<%=RightConstant.FRONTEND_ALLOW_ENTNER%>"></aj-admin-role-check-right>		  </td>
	  </tr>
		<tr>
		  <td>进入后台</td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.ADMIN_SYSTEM_ALLOW_ENTNER%>)" :res-id="<%=RightConstant.ADMIN_SYSTEM_ALLOW_ENTNER%>"></aj-admin-role-check-right>		  </td>
	  </tr>
		<tr>
		  <td><p>API 接口</p>	      </td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.API_ALLOW_ACCESS%>)" :res-id="<%=RightConstant.API_ALLOW_ACCESS%>"></aj-admin-role-check-right>		  </td>
	  </tr>
		<tr>
		  <td rowspan="2" class="t1">文章模块</td>
		  <td>上线文章</td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.ARTICLE_ONLINE%>)" :res-id="<%=RightConstant.ARTICLE_ONLINE%>" :set-right-value="5"></aj-admin-role-check-right>		  </td>
	  </tr>
		<tr>
		  <td>已下线文章</td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.ARTICLE_OFFLINE%>)" :res-id="<%=RightConstant.ARTICLE_OFFLINE%>" :set-right-value="5"></aj-admin-role-check-right>		  </td>
	  </tr>
		<tr>
		  <td rowspan="2" class="t1">招聘模块</td>
		  <td>上线招聘</td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.HR_ONLINE%>)" :res-id="<%=RightConstant.HR_ONLINE%>" :set-right-value="5"></aj-admin-role-check-right>		  </td>
	  </tr>
		<tr>
		  <td>下线招聘</td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.HR_OFFLINE%>)" :res-id="<%=RightConstant.HR_OFFLINE%>" :set-right-value="5"></aj-admin-role-check-right>		  </td>
	  </tr>
		<tr>
		  <td rowspan="2" class="t1">产品模块</td>
		  <td>上线产品</td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.PRODUCT_ONLINE%>)" :res-id="<%=RightConstant.PRODUCT_ONLINE%>" :set-right-value="5"></aj-admin-role-check-right>		  </td>
	  </tr>
		<tr>
		  <td>下线产品</td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.PRODUCT_OFFLINE%>)" :res-id="<%=RightConstant.PRODUCT_OFFLINE%>" :set-right-value="5"></aj-admin-role-check-right>		  </td>
	  </tr>
	    <tr>
	      <td class="t1">商城模块</td>
	      <td></td>
	      <td><aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.SHOP%>)" :res-id="<%=RightConstant.SHOP%>"></aj-admin-role-check-right></td>
      </tr>
      <tr>
		  <td class="t1">留言反馈模块</td>
		  <td></td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.FEEDBACK%>)" :res-id="<%=RightConstant.FEEDBACK%>"></aj-admin-role-check-right>		  </td>
	  </tr>
	  <tr>
		  <td class="t1">广告模块</td>
		  <td></td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.ADS%>)" :res-id="<%=RightConstant.ADS%>"></aj-admin-role-check-right>		  </td>
	  </tr>
	<tr>
		  <td class="t1">栏目馈模块</td>
		  <td></td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.SECTION%>)" :res-id="<%=RightConstant.SECTION%>"></aj-admin-role-check-right>		  </td>
	  </tr>
	  <tr>
		  <td class="t1">网站模块</td>
		  <td></td>
		  <td>
		  	<aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.WEBSITE%>)" :res-id="<%=RightConstant.WEBSITE%>"></aj-admin-role-check-right>		  </td>
	  </tr>
		<tr>
		  <td class="t1">全局参数</td>
		  <td>&nbsp;</td>
		  <td><aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.GLOBAL_SETTING%>)" :res-id="<%=RightConstant.GLOBAL_SETTING%>"></aj-admin-role-check-right>	</td>
	  </tr>
		<tr>
		  <td class="t1">用户管理</td>
		  <td>&nbsp;</td>
		  <td><aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.USER%>)" :res-id="<%=RightConstant.USER%>"></td>
	  </tr>
		<tr>
		  <td class="t1">用户分配权限</td>
		  <td>&nbsp;</td>
		  <td><aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.USER_PRIVILEGE%>)" :res-id="<%=RightConstant.USER_PRIVILEGE%>"></td>
	  </tr>
		<tr>
		  <td class="t1">开发者工具模块</td>
		  <td>&nbsp;</td>
		  <td><aj-admin-role-check-right :is-enable="resRight(<%=RightConstant.DEVELOPER_TOOL%>)" :res-id="<%=RightConstant.DEVELOPER_TOOL%>"></td>
	  </tr>
		<tr>
		  <td class="t1">&nbsp;</td>
		  <td>&nbsp;</td>
		  <td>&nbsp;</td>
	  </tr>
		<tr>
		  <td class="t1">&nbsp;</td>
		  <td>&nbsp;</td>
		  <td>&nbsp;</td>
	  </tr>
		<tr>
		  <td class="t1">&nbsp;</td>
		  <td>&nbsp;</td>
		  <td>&nbsp;</td>
	  </tr>
		<tr>
		  <td class="t1">&nbsp;</td>
		  <td>&nbsp;</td>
		  <td>&nbsp;</td>
	  </tr>
		<tr>
		  <td class="t1">&nbsp;</td>
		  <td>&nbsp;</td>
		  <td>&nbsp;</td>
	  </tr>
		<tr>
		  <td class="t1">&nbsp;</td>
		  <td>&nbsp;</td>
		  <td>&nbsp;</td>
	  </tr>
		<tr>
		  <td class="t1">&nbsp;</td>
		  <td>&nbsp;</td>
		  <td>&nbsp;</td>
	  </tr>
		<tr>
		  <td class="t1">&nbsp;</td>
		  <td>&nbsp;</td>
		  <td>&nbsp;</td>
	  </tr>
	</table>
	<script src="${ctx}/asset/admin/user-assign-right.js"></script>
	<script>
		VUE = new Vue({
			el : 'table', 
			data : {
				userGroupId: null,
				currentUserGroup: '',
				resRightValue : <%=num%>
			},
			methods : {
				resRight(v) {
					return check(this.resRightValue, v);
				},
				captureBubble(data) {
					if(data.id){
						this.userGroupId = data.id;
						
						//this.resRightValue = data.accessKey || 0;
						aj.xhr.get('../user_group/'+ data.id, json => {
							this.resRightValue = json.result.accessKey || 0;
						});
					}
					
					if(data.name)
						this.currentUserGroup = data.name;
				}
			}
		});
	</script>
</body>
</html>