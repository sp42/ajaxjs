<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="c" 		uri="/ajaxjs" %>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/public/UI"%>
<%@taglib prefix="adminUI"  tagdir="/WEB-INF/tags/public/admin"%>
<adminUI:edit type="main">
	<div class="row">
				<div style="">
					<label>
						<div class="label">名 称：</div>
						<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" value="${info.name}" type="text" class="my-inputField" />
					</label>
					<label>
						分类：
							<select name="catalog">
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
						<textarea rows="15" cols="20" class="my-inputField" name="intro">${info.intro}</textarea>
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
			
	
			
			<div class="row">
				<div>
					<label>
						<div class="label">封面图：</div>
						<img src="https://gw.alicdn.com/tps/i1/TB1Cz4oJpXXXXXKXVXXXXXXXXXX_!!0-item_pic.jpg_150x150.jpg" />
					</label>
					
				 
					<div class="atttendmentPic">
						已上传列表：
						<ul>
				
							 
							<li>
								<div class="upload-btns">
									<input id="input_file_molding" class="btn_file_molding" size="6" type="file" /> 
									<label for="input_file_molding"> 
										<div>
											<div>+</div>
											点击选择图片
										</div>
									</label>
								</div>
							</li>
						</ul>
						
						<div style="font-size:.9rem;clear:left;">
							<a href="">设为封面图</a>
							<a href="">删除</a>
							<a href="">全部删除</a>
							<a href="">上传图片</a>
							
						</div>
					</div>
					
					<div class="upload"></div>
				</div>
			</div>
</adminUI:edit>