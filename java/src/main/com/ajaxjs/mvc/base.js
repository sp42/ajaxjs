/**
 * 本人不才，曾开发轻量级ORM框架LessSQL.Net，由于设计为SQL语句必须由对象模块实例映射生成，而关系模型数据集合无法自动填充任意的对象模型实体中，无法支撑复杂的查询语句，而缓存方面仅实现了SQL语句缓存性能优化有限，因此框架仅适用于小型工具软件。因为踩过这些坑，所以对ORM框架有一点浅薄的认识和看法
 * 返回行结果
 * 按照 JDBC 数据类型 到 JavaScript 类型的转换。
 * @private
 * @param {ResultSet}
 * @return {Array}
 */
bf.sql = {};
bf.sql.getValue = function (rsObj){
	var result = [];
	var ResultSetMetaData = rsObj.getMetaData();
	var Types = java.sql.Types;
	
	while(rsObj.next()){
		var row = {};
		for(var i = 0, j = ResultSetMetaData.getColumnCount(); i < j; i++) {
			var colIndex = i + 1; // 列数从 1 开始
			
			var columnName = ResultSetMetaData.getColumnLabel(colIndex); 
//			var columnName = ResultSetMetaData.getColumnName(colIndex); 
//					columnName = columnName.toLowerCase(),
			var value = null; // 奇怪，不能是  var value; 如果是 undefined，会不保存 key
			// http://www.cnblogs.com/myCodingSky/p/3637513.html
//			println('::::::::::'+columnName);
//			println( String(rsObj.getString(colIndex)));

			switch(ResultSetMetaData.getColumnType(colIndex)){
				case Types.BIT:
				case Types.SMALLINT:
					value = Boolean(rsObj.getBoolean(colIndex));
				break;
				case Types.LONGVARBINARY:// what the hell?
				case Types.CHAR:
				case Types.VARCHAR:
				case Types.LONGVARCHAR:
					value = String(rsObj.getString(colIndex));
				break;
				case Types.NUMERIC:
				case Types.DECIMAL:
				case Types.TINYINT:
				case Types.SMALLINT:
				case Types.INTEGER:
				case Types.BIGINT:
				case Types.REAL:
				case Types.FLOAT:
				case Types.DOUBLE:
					value= Number(rsObj.getDouble(colIndex));
				break;
				// DATE 	java.sql.Date
				// TIME 	java.sql.Time
				// TIMESTAMP 	java.sql.Timestamp			
				case Types.DATE:
				case Types.TIME:
				case Types.TIMESTAMP:
//						var date = rsObj.getDate(colIndex);
//						println(rsObj.getString(colIndex))
//						value = new Date(rsObj.getString(colIndex));
					value = rsObj.getString(colIndex);
				break;
			}
			
			if(value){
				if(typeof value == 'number'){
				}else{
					// string?
					value = unescape(value); // 转义
				}
			}
			row[columnName] = value;
			
			value = null; // 有时会重复 value的值不知何解
		}
		result.push(row);
	}
	
	return result;
};

////-------------------------------- writer--------------------------
//@dep
bf_service_create = function(tableName, pojo, requestMap){
	var fields = [], values = [];
	pojo = self[pojo];
	
	for(var field in pojo){
		var value = request.getParameter(field);
		if (canWrite(value)){
			fields.push('`' + field + '`');
			var fieldObj = pojo[field];
			value = getJSType(fieldObj, value);
			fieldObj.value = value; // 写入 java 字段对象
									// 这样获取 System.out.println("this.pojo.name.value:"+getPojo().name.value);
			value = json2sql(value);
			
//			println(field + '=======' + value);
//			if(value && typeof value == 'string')value =trim(value);
			values.push(value);
		}
	}
	
	var now = json2sql(new Date);

	fields.push('createDate', 'updateDate');
	values.push(now, now);
	
	return "INSERT INTO {tableName} ({columns}) VALUES ({values})".format({
		tableName : serviceInstance.getTableName(),
		columns : fields.join(', '),
		values : values.join(', ')
	});
};

bf_service_write = function(pojo, request_JavaMap){

	pojo = self[pojo];
	var valueObj = {};
	
	for(var fieldName in pojo){
		var resrveWords = fieldName == 'many2many';
		if(resrveWords){
			continue;
		}
		
		var fieldObject = pojo[fieldName];
		
		if(fieldObject.isNotNull === false && request_JavaMap.get(fieldName) == null){
													// 允许为空
		}else{
			continue;								// 不允许为空，抛出异常
		}
			
		var value = String(request_JavaMap.get(fieldName));
		if(value == 'null')value = null;
		
		if(fieldObject.isNotNull && typeof value == undefined)
			continue;								// 允许为空
		
		if(fieldObject.defaultValue && typeof value == undefined)
			value = fieldObject.defaultValue;		// 若未提交数据，设置默认值
		
		if(fieldObject.type && value != null)
			value = fieldObject.type(value);			// 强制转换类型
		
		if(fieldObject.onCreate){					// 业务逻辑
			value = fieldObject.onCreate.call(pojo, value, valueObj, request_JavaMap);
		}
		
		fieldObject.value	= value;				// 充血
		valueObj[fieldName] = value;				// 写入 sql pair
	}
	
	return valueObj;
};

orm = {};
orm.copyField = function (a, b){
	for(var i in b){
		a[i] = b[i];
	}
};
orm.field = function (cfg){
	/**
	 * 类型，为 JavaScript Function
	 */
	this.type = String;
	/**
	 * 是否允许为空
	 */
	this.isNotNull = false; 
	
	/**
	 * 默认值
	 */
	this.defaultValue = null;
	
	this.value = null;
	
	/**
	 *  用于表单验证的正则。设置一个 map，对应 js regexp 类型（不在 java 里写正则）
	 */
	this.regexpTest = null; 
	
	/**
	 * 用于 UI提示的文字。
	 */
	this.label = null;
	
	this.maxLength = null;
	
	/**
	 *  SQL Alias
	 */
	this.AS = null;
	
	/**
	 * 是否大体积的字段，如果是，则不出现在 列表查询中，否则数据量太大
	 */
	this.isBigContent = false;
	
	orm.copyField(this, cfg);
};

orm.base = {
	uid : new orm.field({
		isNotNull : true,
		label : 'ID'
	}),
	name : new orm.field({
		isNotNull : true,
		label : '名称'
	}),
	content : new orm.field({
		label : '内容',
		isBigContent : true
	}),
	createDate : new orm.field({
		type : Date,
		label : '创建日期'
	}),
};

var self = this;

/**
 * @cfg isLessBig 是否不出现 大体积 的字段
 * @param tableName
 * @param pojo
 * @param cfg
 */
bf.sql.getSelectFields = function(tableName, pojo, cfg){
	pojo = self[pojo];
	
	if(!pojo)return null;
	
	if(cfg && typeof cfg == 'string'){
		cfg = JSON.parse(cfg);
	}else if(!cfg){
		cfg = {};
	}
	
	var fields = [];
	
	for(var fieldName in pojo){
		var fieldObject = pojo[fieldName];
		
		var resrveWords = fieldName == 'many2many',
			isLessBig = cfg.isLessBig && fieldObject.isBigContent === true;
		if(resrveWords || isLessBig){
			continue;
		}
		
		var field = tableName + '.' + fieldName;
		if(fieldObject.AS) field += ' AS ' + fieldObject.AS;
		
		fields.push(field);
	}
	
	if(pojo.many2many){
		for(var i = 0, j = pojo.many2many.length; i < j; i++){
			var obj = pojo.many2many[i];
			if(obj.many2one){
				fields.push(obj.sql.format(tableName));
			}
		}
	}
	
	return fields.join(', ');
};

getJoin = function(tableName, pojo, cfg){
	pojo = self[pojo];
	cfg = cfg || {};
	
	var joins = [], found = false;
	if(pojo.many2many){
		for(var i = 0, j = pojo.many2many.length; i < j; i++){
			var obj = pojo.many2many[i];
			
			if(obj.hasMany){
				println(obj.sql.format(tableName));
				// 去掉字符串中的回车和换行符
				joins.push(obj.sql.format(tableName).replace(/Chr(13)/g, ' '));
				found = true;
			}
		}
	}
	return found ? joins.join(' ') : null;
};

// a example
news = Object.create(orm.base);
news.many2many = [
	{
		many2one: 'section', sql : "(SELECT group_concat(name || '_' || uid) FROM (tagDetail LEFT JOIN tag ON tag.tagId = tagDetail.uid) WHERE tag.entryId = {0}.uid) AS tags"
	}, {
		hasMany:'tag',
		sql : "(news LEFT JOIN section ON section.entryId = news.uid) 	\
				LEFT JOIN \
				sectionDetail ON (sectionDetail.uid = section.sectionId)"
	}
];

news.brief = new orm.field({
	label : '简介',
	onCreate : function(data){
		if(this.content.value)
			this.value = this.content.value.substr(0, 20);
	}
});