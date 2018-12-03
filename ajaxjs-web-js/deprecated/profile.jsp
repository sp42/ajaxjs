<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/bigfoot/asset/less/user.less" title="个人信息" />
<body>
	<commonTag:adminUI type="header" pageTitle="个人信息" />
	<style>
		body {
			background-color: #f4f4f4;
		}
		
		fieldset {
			background-color: white;
		}
	</style>
	<div class="center">
		<fieldset>
			<legend>基本资料</legend>
			<form class="aj">
				<label> NickName： <input type="text" name="nickname" />
				</label> <label> RealName： <input type="text" name="realname" />
				</label> <label> Sex：
					<div class="right">
						<label> <input type="radio" name="sex" value="male" /> 男
						</label> <label> <input type="radio" name="sex" value="female" />
							女
						</label> <label> <input type="radio" name="sex" value="nosex" />
							保密
						</label>
					</div>
				</label> <label> Location： <input type="text" name="password" />
				</label>
			</form>
		</fieldset>
		<fieldset>
			<legend>详细资料</legend>
			<p class="text_1">欢迎您输入详细的资料，以便我们提供更好的服务。我们承若不泄露你的资料予以第三方。</p>
			<form>
				<table class="form">
					<tbody>

						<tr>
							<td>
								<div>真实姓名</div>
							</td>
							<td><input name="in_fax" value="" type="text" placeholder="请输入您的真实姓名" /></td>
							<td>
								<div>出生日期</div>
							</td>
							<td><input name="in_alias" value="" type="text" placeholder="格式 年-月-日" /></td>
						</tr>
						<tr>
							<td>
								<div>固定电话</div>
							</td>
							<td><input name="in_mobile" value="" type="text" placeholder="区号+电话" /></td>
							<td>
								<div>传真号码</div>
							</td>
							<td><input name="in_phone" value="" class="readonly" type="text" placeholder="区号+传真" /></td>
						</tr>

						<tr>
							<td>
								<div>婚姻状况</div>
							</td>
							<td title="日期格式：YYYY-MM-DD"><select
								name="in_marital_status">
									<option value="" class="noSelect">-- 请选择姻状况  --</option>
									<option value="未婚">未婚</option>
									<option value="已婚">已婚</option>
							</select></td>
							<td>
								<div>教育水平</div>
							</td>
							<td><select name="in_education_level">
									<option value="" class="noSelect">-- 请选择教育水平 --</option>
									<option value="博士">博士</option>
									<option value="硕士">硕士</option>
									<option value="大学本科">大学本科</option>
									<option value="大学专科">大学专科</option>
									<option value="高中">高中</option>
									<option value="中专">中专</option>
									<option value="初中">初中</option>
									<option value="小学">小学</option>

							</select></td>
						</tr>

						<tr>
							<td>
								<div>有效证件类型</div>
							</td>
							<td><select name="in_certificate_type">
									<option value="" class="noSelect">-- 请选择 --</option>
									<option value="身份证">身份证</option>
									<option value="户口簿">户口簿</option>
									<option value="护照">护照</option>
									<option value="回乡证">回乡证</option>
									<option value="警官证">警官证</option>
									<option value="军官证">军官证</option>
									<option value="临时身份证">临时身份证</option>
									<option value="其它">其它</option>
									<option value="士兵证">士兵证</option>
							</select></td>
							<td>
								<div>证件号码</div>
							</td>
							<td><input name="in_certificate_no" value="" placeholder="请输入有效证件号码" type="text" /></td>
						</tr>
						<tr>
							<td>
								<div>省份/自治区</div>
							</td>
							<td><select
								name="in_province">
									<option value="" class="noSelect">-- 请选择省份 --</option>
									<option value="安徽">安徽</option>
									<option value="澳门">澳门</option>
									<option value="北京">北京</option>
									<option value="福建">福建</option>
									<option value="甘肃">甘肃</option>
									<option value="广东">广东</option>
									<option value="广西">广西</option>
									<option value="贵州">贵州</option>
									<option value="海南">海南</option>
									<option value="海外">海外</option>
									<option value="河北">河北</option>
									<option value="河南">河南</option>
									<option value="黑龙江">黑龙江</option>
									<option value="湖北">湖北</option>
									<option value="湖南">湖南</option>
									<option value="吉林">吉林</option>
									<option value="江苏">江苏</option>
									<option value="江西">江西</option>
									<option value="辽宁">辽宁</option>
									<option value="内蒙古">内蒙古</option>
									<option value="宁夏">宁夏</option>
									<option value="青海">青海</option>
									<option value="山东">山东</option>
									<option value="山西">山西</option>
									<option value="陕西">陕西</option>
									<option value="上海">上海</option>
									<option value="四川">四川</option>
									<option value="台湾">台湾</option>
									<option value="天津">天津</option>
									<option value="西藏">西藏</option>
									<option value="香港">香港</option>
									<option value="新疆">新疆</option>
									<option value="云南">云南</option>
									<option value="浙江">浙江</option>
									<option value="重庆">重庆</option>
							</select></td>
							<td>
								<div>城市</div>
							</td>
							<td><input
								name="in_city" value="" type="text" placeholder="请输入您所在的城市" /></td>
						</tr>
						<tr>
							<td>
								<div>县区</div>
							</td>
							<td><input name="in_city" value="" type="text" placeholder="请输入您所在的县区" /></td>
							<td>
								<div>邮政编码</div>
							</td>
							<td><input name="in_postcode" value="" type="text" placeholder="邮政编码可忽略" /></td>
						</tr>
						<tr>
							<td>
								<div>详细地址</div>
							</td>
							<td colspan="3">
								<input style="margin-left:2%;" name="in_address" value="" type="text" placeholder="请输入您的详细地址" />
							</td>
						</tr>
						<tr>
							<td>
								<div>您的职业</div>
							</td>
							<td><select name="in_occupation">
									<option value="" class="noSelect">-- 请选择职业 --</option>
									<option value="待业中">待业中</option>
									<option value="服务行业">服务行业</option>
									<option value="工程师">工程师</option>
									<option value="计算机业">计算机业</option>
									<option value="兼职">兼职</option>
									<option value="教育业">教育业</option>
									<option value="金融业">金融业</option>
									<option value="雇主">雇主</option>
									<option value="其他">其他</option>
									<option value="全职">全职</option>
									<option value="商业">商业</option>
									<option value="退休">退休</option>
									<option value="销售/广告/市场">销售/广告/市场</option>
									<option value="学生">学生</option>
									<option value="政府部门">政府部门</option>
									<option value="制造业">制造业</option>
							</select></td>
							<td>
								<div>月收入</div>
							</td>
							<td><select name="in_earning">
									<option value="" class="noSelect">-- 请选择月收入 --</option>
									<option value="10000元以上">10000元以上</option>
									<option value="1001-2000元">1001-2000元</option>
									<option value="2001-3000元">2001-3000元</option>
									<option value="3001-4000元">3001-4000元</option>
									<option value="4001-5000元">4001-5000元</option>
									<option value="5001-6000元">5001-6000元</option>
									<option value="500元以下">500元以下</option>
									<option value="501-1000元">501-1000元</option>
									<option value="6001-7000元">6001-7000元</option>
									<option value="7001-8000元">7001-8000元</option>
									<option value="8001-9000元">8001-9000元</option>
									<option value="9001-10000元">9001-10000元</option>
							</select></td>
						</tr>
						<tr>
							<td><div>企业类型</div></td>
							<td><select name="in_corporation_type">
									<option value="" class="noSelect">-- 请选择企业类型 --</option>
									<option value="党群机关">党群机关</option>
									<option value="国有企业">国有企业</option>
									<option value="合资企业">合资企业</option>
									<option value="集体企业">集体企业</option>
									<option value="其他">其他</option>
									<option value="人民团体">人民团体</option>
									<option value="事业单位">事业单位</option>
									<option value="私营企业">私营企业</option>
									<option value="外资企业">外资企业</option>
									<option value="行政机关">行政机关</option>
									<option value="学校">学校</option>
							</select></td>
							<td><div>所属行业</div></td>
							<td><select name="in_vocation">
									<option value="" class="noSelect">-- 请选择所属行业 --</option>
									<option value="餐饮业">餐饮业</option>
									<option value="法律、财务、咨询">法律、财务、咨询</option>
									<option value="房地产、建筑、装潢">房地产、建筑、装潢</option>
									<option value="服务业">服务业</option>
									<option value="工业自动化">工业自动化</option>
									<option value="广播、电影、报刊">广播、电影、报刊</option>
									<option value="广告、公关">广告、公关</option>
									<option value="航空、航天业">航空、航天业</option>
									<option value="计算机、信息技术">计算机、信息技术</option>
									<option value="教育业">教育业</option>
									<option value="进出口业">进出口业</option>
									<option value="科研、电子、摄影器材">科研、电子、摄影器材</option>
									<option value="旅游业">旅游业</option>
									<option value="农业、林业、渔业">农业、林业、渔业</option>
									<option value="其他行业">其他行业</option>
									<option value="通讯业">通讯业</option>
									<option value="银行、保险、金融服务">银行、保险、金融服务</option>
									<option value="娱乐、运动、休闲">娱乐、运动、休闲</option>
									<option value="运输、物流、快递">运输、物流、快递</option>
									<option value="政府部门">政府部门</option>
									<option value="制药业、保健、生物工程">制药业、保健、生物工程</option>
									<option value="制造业">制造业</option>
							</select></td>
						</tr>
					</tbody>
				</table>
					<div> 
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="保存" /> &nbsp;&nbsp;&nbsp;<a href="javascript:;" onclick="this.up('form').reset();" style="font-size:.9rem;color:gray;">复位</a>
			</div>
			</form>
		</fieldset>
		<fieldset>
			<legend>修改头像</legend>
			<input type="file" value="上传图片" /> 仅支持 jpg、gif、png 图片文件上传，且文件小于 5 兆。
			<input type="submit" value="上传图片" />
		</fieldset>
	</div>
</body>
</html>