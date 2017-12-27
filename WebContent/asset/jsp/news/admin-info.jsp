<%@page pageEncoding="UTF-8" import="com.ajaxjs.web.Constant"%>	
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="adminUI" tagdir="/WEB-INF/tags/common/admin"%>
<%@taglib prefix="c" uri="/ajaxjs" %>

<adminUI:form>
	<div class="row">
		<div>
			<label>
				<div class="label">名 称：</div>
				<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" value="${info.name}" type="text" class="ajaxjs-inputField" />
			</label>
			<label>
				分类：
					<select name="catalog" class="ajaxjs-select" style="width:20%;">
					<c:foreach items="${catalogs}" var="current">
						<c:choose>
							<c:when test="${info.catalog == current.id}">
								<option value="${current.id}" selected>${current.name}</option>
							</c:when>
							<c:otherwise>
								<option value="${current.id}">${current.name}</option>
							</c:otherwise>
						</c:choose>
					</c:foreach>
					</select>
			</label>
		</div>
	</div>
			
	<div class="row brief">
		<div>
			<label>
				<div class="label">摘 要：</div>
				<textarea rows="15" cols="20" class="ajaxjs-inputField" name="intro">${info.intro}</textarea>
			 </label>
		</div>
	</div>			
</adminUI:form>
