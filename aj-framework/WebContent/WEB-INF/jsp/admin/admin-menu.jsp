<%@page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RoleService"%>
<li>
	<h3><i class="fa fa-line-chart"></i> 产品管理</h3>
	<ul>
	</ul>
</li>
<li>
	<h3><i class="fa fa-cart-arrow-down" aria-hidden="true"></i> 销售管理</h3>
	<ul>
	</ul>
</li>
<li>
	<h3><i class="fa fa-sellsy"></i> 采购管理</h3>
	<ul>
	</ul>
</li>
<li>
	<h3><i class="fa fa-building-o"></i> 仓库管理</h3>
	<ul>
		<li><a target="iframepage" href="${ctx}/warehouse/">仓库管理</a></li>
 
	</ul>
</li>
<li>
	<h3><i class="fa fa-cny"></i> 财务管理</h3>
	<ul>
	</ul>
</li>
<li>
	<h3 class="config"><i></i> 基础数据</h3>
	<ul>
		<li><a target="iframepage" href="${ctx}/base/supplier/">供应商管理</a></li>
		<li><a target="iframepage" href="${ctx}/base/tax_rate/">税率管理</a></li>
		<li><a target="iframepage" href="${ctx}/base/bank/">银行管理</a></li>
	</ul>
</li>

<li>
	<h3 class="content"><i></i> 流程管理</h3>
	<ul>
		<li><a target="iframepage" href="${ctx}/admin/workflow/process/">流程定义</a></li> 
		<li><a target="iframepage" href="${ctx}/admin/workflow/process/active/">流程实例</a></li> 
		<li><a target="iframepage" href="${ctx}/admin/workflow/process/history">历史流程 </a></li> 
		<li><a target="iframepage" href="${ctx}/admin/workflow/surrogate/">委托授权</a></li> 
		<li><a target="iframepage" href="${ctx}/admin/workflow/task/">待办任务</a></li> 
		<li><a target="iframepage" href="${ctx}/admin/workflow/task/help/">协办任务</a></li> 
		<li><a target="iframepage" href="${ctx}/admin/workflow/task/ccorder/">抄送任务</a></li> 
		<li><a target="iframepage" href="${ctx}/admin/workflow/task/history">历史任务 </a></li> 
	</ul>
</li>