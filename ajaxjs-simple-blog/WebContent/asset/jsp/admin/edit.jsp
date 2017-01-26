<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="c" 		uri="/ajaxjs" %>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="commonTag"  tagdir="/WEB-INF/tags/common/"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/admin.less" title="${isCreate ? '新建' : '编辑'}${uiName}" />
<body>
<commonTag:adminUI type="editor">
	<div class="row">
				<div style="">
					<label>
						<div class="label">名 称：</div>
						<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" value="${info.name}" type="text" class="my-inputField" />
					</label>
					<label>
						<commonTag:page type="catalog_dropdownlist" isNotJump="true" />
					</label>
				</div>
			</div>
			

			
			<div class="row">
				<div>
					<label>
						<div class="label" style="vertical-align: top;">正 文：</div>
						<commonUI:htmlEditor name="content" basePath="../images">${info.content}</commonUI:htmlEditor>
					</label>
				</div>
			</div>
			
	<div class="row brief">
				<div>
					<label>
						<div class="label">摘 要：</div>
						<textarea rows="15" cols="20" class="my-inputField" name="brief">${info.brief}</textarea>
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
</commonTag:adminUI>
</body>
</html>