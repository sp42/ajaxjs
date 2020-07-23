<template id="aj-avatar">
	<a :href="avatar" target="_blank">
		<img :src="avatar" style="max-width:50px;max-height:60px;vertical-align: middle;" 
	 		@mouseenter="mouseEnter" 
	 		onmouseleave="aj.imageEnlarger.singleInstance.imgUrl = null;" />
	</a>
</template>

<!-- 
	=========LIST========= 
-->

<template id="aj-pager">
	<footer class="aj-pager">
		<a v-if="pageStart > 0" href="#" @click="previousPage">上一页</a>
		<a v-if="(pageStart > 0 ) && (pageStart + pageSize < total)" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>
		<a v-if="pageStart + pageSize < total" href="#" @click="nextPage">下一页</a>
		<div class="info">
			<input type="hidden" name="start" :value="pageStart" />
			页数：{{currentPage}}/{{totalPage}} 记录数：{{pageStart}}/{{total}}
			每页记录数： <input size="2" title="输入一个数字确定每页记录数" type="text" :value="pageSize" @change="onPageSizeChange" />
			跳转： <select @change="jumpPageBySelect;">
				<option :value="n" v-for="n in totalPage">{{n}}</option>
			</select>
		</div>
	</footer>
</template>

<!-- 
	=========表单 FORM========= 
-->

<template id="aj-popup-upload">
	<aj-layer>
		<h3>图片上传</h3>
		<aj-xhr-upload ref="uploadControl" :action="uploadUrl" :is-img-upload="true" :hidden-field="imgName"
			:img-place="ajResources.commonAsset + '/images/imgBg.png'">\
		</aj-xhr-upload>
		<div>上传限制：{{text.maxSize}}kb 或以下，分辨率：{{text.maxHeight}}x{{text.maxWidth}}</div>
	</aj-layer>
</template>

<template id="attachment-picture-list">
	<table><tr><td>
		<div class="label">相册图：</div>
		<ul>
			<li v-for="pic in pics" style="float:left;margin-right:1%;text-align:center;">
				<a :href="picCtx + pic.path" target="_blank"><img :src="picCtx + pic.path" style="max-width: 100px;max-height: 100px;" /></a><br />
				<a href="###" @click="delPic(pic.id);">删 除</a>
			</li>
		</ul>
	</td><td>
	<aj-xhr-upload ref="attachmentPictureUpload" :action="uploadUrl" :is-img-upload="true" :img-place="blankBg"></aj-xhr-upload>
	</td></tr></table>
</template>

<template id="aj-xhr-upload">
	<div class="aj-xhr-upload" :style="{display: buttonBottom ? 'inherit': 'flex'}">
		<input v-if="hiddenField" type="hidden" :name="hiddenField" :value="hiddenFieldValue" />
		<div v-if="isImgUpload">
			<a :href="imgPlace" target="_blank">
				<img class="upload_img_perview" :src="(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace" />
			</a>
		</div>
		<div class="pseudoFilePicker">
			<label :for="'uploadInput_' + radomId"><div><div>+</div>点击选择{{isImgUpload ? '图片': '文件'}}</div></label>
		</div>
		<input type="file" :name="fieldName" class="hide" :id="'uploadInput_' + radomId" 
			@change="onUploadInputChange" :accept="isImgUpload ? 'image/*' : accpectFileType" />
		<div v-if="!isFileSize || !isExtName">{{errMsg}}</div>
		<div v-if="isFileSize && isExtName">
			{{fileName}}<br />
			<button @click.prevent="doUpload();" style="min-width:110px;">{{progress && progress !== 100 ? '上传中 ' + progress + '%': '上传'}}</button>
		</div>
	</div>
</template>

<template id="aj-form-calendar-input">
	<div class="aj-form-calendar-input" @mouseover="onMouseOver">
		<div class="icon fa fa-calendar"></div>
		<input :placeholder="placeholder" size="12" :name="fieldName" 
			:value="date + (dateOnly ? '' : ' ' + time)" type="text" autocomplete="off" class="aj-input" />
		<aj-form-calendar ref="calendar" :show-time="showTime" @pick-date="recEvent" @pick-time="recTimeEvent">
		</aj-form-calendar>
	</div>
</template>

<template id="aj-form-calendar">
	<div class="aj-form-calendar">
		<div class="selectYearMonth">
			<a href="###" @click="getDate('preYear')" class="preYear" title="上一年">&lt;</a> 
			<select @change="setMonth" v-model="month">
				<option value="1">一月</option><option value="2">二月</option><option value="3">三月</option><option value="4">四月</option>
				<option value="5">五月</option><option value="6">六月</option><option value="7">七月</option><option value="8">八月</option>
				<option value="9">九月</option><option value="10">十月</option><option value="11">十一月</option><option value="12">十二月</option>
			</select>
			<a href="###" @click="getDate('nextYear')" class="nextYear" title="下一年">&gt;</a>
		</div>
		<div class="showCurrentYearMonth">
			<span class="showYear">{{year}}</span>/<span class="showMonth">{{month}}</span>
		</div>
		<table>
			<thead>
				<tr><td>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr>
			</thead>
			<tbody @click="pickDay"></tbody>
		</table>
		<div v-if="showTime">
			时 <select class="hour aj-select"><option v-for="n in 24">{{n}}</option></select>
			分 <select class="minute aj-select"><option v-for="n in 61">{{n - 1}}</option></select>
			<a href="#" @click="pickupTime">选择时间</a>
		</div>
	</div>
</template>

<template id="aj-grid-inline-edit-row">
	<tr class="aj-grid-inline-edit-row" :class="{editing: isEditMode}">
		<td v-if="showCheckboxCol" class="selectCheckbox">
			<input type="checkbox" @change="selectCheckboxChange" :data-id="id" />
		</td>
		<td v-if="showIdCol">{{id}}</td>
		<td v-for="key in columns" :style="styleModifly" class="cell" @dblclick="dbEdit">
			<aj-cell-renderer v-if="isFixedField(key) || !isEditMode" :html="renderCell(data, key)"></aj-cell-renderer>
			<input type="text" v-if="canEdit(key)" size="0" v-model="data[key]" /> 
		</td>
		<td class="control">
			<span @click="onEditClk"><img :src="ajResources.commonAsset + '/icon/update.gif'" /> 
			{{!isEditMode ? "编辑" : "确定"}}</span>
			<span @click="dele(id)"><img :src="ajResources.commonAsset + '/icon/delete.gif'" /> 删除</span> 
		</td>
	</tr>
</template> 

<template id="aj-grid-inline-edit-row-create">
	<tr class="aj-grid-inline-edit-row isEditMode"> 
		<td><input type="checkbox" /></td>
		<td></td>
		<td v-for="key in columns" style="padding:0" class="cell" @dblclick="dbEdit">
			<input v-if="key != null" type="text" size="0" :name="key" /> 
		</td>
		<td class="control">
			<span @click="addNew"><img :src="ajResources.commonAsset + '/icon/update.gif'" />新增</span>
			<span @click="$parent.showAddNew = false"><img :src="ajResources.commonAsset + '/icon/delete.gif'" /> 撤销</span>
		</td>
	</tr>
</template>

<template id="aj-edit-form">
	<form class="aj-table-form" :action="getInfoApi + (isCreate ? '' : info.id + '/')" :method="isCreate ? 'POST' : 'PUT'">
		<h3>{{isCreate ? "新建" : "编辑" }}{{uiName}}</h3>
		<!-- 传送 id 参数 -->
		<input v-if="!isCreate" type="hidden" name="id" :value="info.id" />
		<slot v-bind:info="info"></slot>
		<div class="aj-btnsHolder">
			<button><img :src="ajResources.commonAsset + '/icon/save.gif'" /> {{isCreate ? "新建":"保存"}}</button>
			<button onclick="this.up(\'form\').reset();return false;">复 位</button>
			<button v-if="!isCreate" v-on:click.prevent="del()">
				<img :src="ajResources.commonAsset + '/icon/delete.gif'" /> 删 除
			</button>
			<button @click.prevent="close">关闭</button>
		</div>
	</form>
</template>

<template id="aj-entity-toolbar">
	<div class="toolbar">
		<form v-if="search" class="right">
			<input type="text" name="keyword" placeholder="请输入关键字" size="12" />
			<button @click="doSearch"><i class="fa fa-search" style="color:#417BB5;"></i>搜索</button>
		</form>
		
		<aj-form-between-date v-if="betweenDate" class="right"></aj-form-between-date>
		
		<ul>
			<li @click="$parent.onCreateClk"><i class="fa fa-plus" style="color:#0a90f0;"></i> 新建</li>
			<li v-if="save" @click="$parent.onDirtySaveClk"><i class="fa fa-floppy-o" style="color:rgb(205, 162, 4);"></i> 保存</li>
			<li><i class="fa fa-trash-o" style="color:red;"></i> 删除</li>
			<li v-if="excel"><i class="fa fa-file-excel-o" style="color:green;"></i> 导出</li>
		</ul>
	</div>
</template>

<template id="aj-form-between-date">
	<form action="." method="GET" class="dateRange" @submit="valid">
		  <aj-form-calendar-input :date-only="true" :position-fixed="true" placeholder="起始时间" field-name="startDate" ></aj-form-calendar-input>
		- <aj-form-calendar-input :date-only="true" :position-fixed="true" placeholder="截至时间" field-name="endDate"></aj-form-calendar-input>
		<button class="aj-btn">按时间筛选</button>
	</form>	
</template>