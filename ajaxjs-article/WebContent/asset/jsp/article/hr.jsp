<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
	<div class="row">
		<div>
			<label>
				<div class="label">名 称：</div>
				<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" value="${info.name}" type="text" class="my-inputField" />
			</label>
			<label>
			工作年限：
				<input placeholder="请填写${uiName}名称" size="10" required="required" name="expr" value="${info.expr}" type="text" class="my-inputField" />
			
			</label>
			<label>
				工作分类：
					<select name="jobType">
						<option value="全职" ${info.jobType == '全职' ? 'selected' : ''}>全职</option>
						<option value="兼职" ${info.jobType == '兼职' ? 'selected' : ''}>兼职</option>
					</select>
			</label>
		</div>
	</div>
	<div class="row">
		<div>
			<label>
				<div class="label" style="vertical-align: top;">正 文：</div>
				<commonUI:htmlEditor name="content" basePath="../">${info.content}</commonUI:htmlEditor>
			</label>
		</div>
	</div>
