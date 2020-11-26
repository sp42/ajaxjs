<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="/ajaxjs" prefix="c" %>
<%@attribute name="type" required="true" type="String" description="标签类型"%>

<c:if test="${type == 'profile'}">	
	<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>
	<style>
		.aj-china-area span{
			clear: left;
		    display: block;
		    float: left;
		    width: 60px;
		}	
	</style>
	<form class="aj-form fixed-width" action="${ctx}/user/profile/" method="put">
		<input type="hidden" name="id" value="${info.id}" />
		<dl>
			<label>
				<dt>姓名</dt>
				<dd>
					<input type="text" name="username" value="${info.username}" placeholder="请输入您的真实姓名" />
				</dd>
			</label>
		</dl>
		<dl>
			<dt>性别</dt>
			<dd>
				<label><input type="radio" name="sex" value="1" ${info.sex == 1 ? 'checked' : '' }> 男</label> 
				<label><input type="radio" name="sex" value="2" ${info.sex == 2 ? 'checked' : '' }> 女</label> 
				<label><input type="radio" name="sex" value="0" ${info.sex == 0 ? 'checked' : '' }> 未知</label>
			</dd>
		</dl>
		<dl>
			<dt>出生日期</dt>
			<dd class="calendar">
				<aj-form-calendar-input field-name="birthday" field-value="${info.birthday}" :position-fixed="true"></aj-form-calendar-input>
			</dd>
		</dl>
		<dl>
			<dt>居住地</dt>
			<dd class="area">
				<aj-china-area province-code="${info.locationProvince}" city-code="${info.locationCity}" district-code="${info.locationDistrict}">
				</aj-china-area>
				<input type="text" name="address" placeholder="填写地址，不用再填省市区" pattern="^[\u4e00-\u9fa5\w]{4,15}$" value="${info.address}" size="32" />
			</dd>
		</dl>
		<dl>
			<label>
				<dt>工作职务</dt>
				<dd>
					<input type="text" name="jobTitle" value="${info.jobTitle}" placeholder="工作职务" />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>身份证号码</dt>
				<dd>
					<input type="text" name="idCardNo" value="${info.idCardNo}" placeholder="中国大陆身份证号码" pattern="^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$" />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>个人简介</dt>
				<dd>
					<textarea rows="6" cols="35" name="content" placeholder="200字符内" class="aj-input">${info.content}</textarea>
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd>
				<button>保存</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>
			</dd>
		</dl>
	</form>
	<script>
		new Vue({
			el : '.calendar'
		});
		new Vue({
			el : '.area'
		});
 
		// AJAX 表单提交
		ajaxjs.xhr.form('.aj-form');
	</script>
</c:if>

<c:if test="${type == 'avatar'}">
	<script src="${ajaxjs_ui_output}/lib/exif.min.js"></script>
	
	<form method="put" action="${ctx}/user/profile/" class="avatar" style="width:600px;margin:0 auto;text-align: center;">
		<input type="hidden" name="id" value="${userId}" />
		
		<aj-xhr-upload action="${ctx}/user/profile/avatar/${userUid}/?id=${userId}" :is-img-upload="true" 
			hidden-field="avatar" 
			hidden-field-value="${userAvatar}" 
			img-place="${empty userAvatar ? commonAsset.concat('/images/imgBg.png') : userAvatar}">
		</aj-xhr-upload>
		
		<p class="aj-note">
			先选择图片然后点击上传，然后请记得按下保存方能生效。<br />
			支持jpg、png、jpeg、bmp，图片大小5M以内 </p>
			
		<br />
		<br />
		<br />
		<div class="aj-btnsHolder">
			<button>保存</button> <button onclick="history.back();" class="aj-btn">返回</button>
		</div>
	</form>
	<script>
		new Vue({
			el : 'form.avatar'
		});
		
		aj.xhr.form('form.avatar', json => {
			aj.xhr.get('${ctx}/user/profile/avatar/updateAvatar/', json=>{
				aj.msg.show("修改头像成功");
				aj('.avatar img').src = "${aj_allConfig.uploadFile.imgPerfix}" + aj("input[name=avatar]").value;
			}, {
				avatar: aj("input[name=avatar]").value
			});
		});
	</script>
</c:if>

<script src="${ajaxjs_ui_output}/lib/md5.min.js"></script>
