/**
 * Java 类型转换为 js 类型
 * @param v
 * @returns
 */
function java_value2js_value(v) {
	if (v == null)
		return null;

	if (v.getClass) {
		// println( v.getClass() == java.lang.String);
		switch (v.getClass()) {
		case java.util.Date:
			return new Date(v);
			break;
		case java.lang.String:
			return String(v);
			break;
		default:
			return String(v);
		}
	} else if (Number(v) == v) {
		return Number(v);
	} else if (Boolean(v) == v) {
		return Boolean(v);
	} else {
		return null;
	}
}

function pojo2json(pojo) {
	var obj = {};
	for ( var k in pojo) {
		var value, hasGet = k.indexOf('get') != -1, hasIs = k.indexOf('is') != -1;

		if ((hasGet || hasIs) && k != 'getClass') {
			value = pojo[k]();

			// println(k + ':' + value);
			var fieldName;

			if (hasGet) {
				fieldName = k.replace('get', '');
			} else {
				fieldName = k.replace('is', '');
			}
			// 第一个字母转为小写
			fieldName = fieldName.charAt(0).toLowerCase() + fieldName.slice(1);
			obj[fieldName] = java_value2js_value(value);
		}
	}
	return obj;
}
print('hihi')
function test(list) {
	var j = list.size(), str = new Array(j);

	for (var i = 0; i < j; i++) {
		var pojo = list.get(i);
		println(pojo)
		str[i] = pojo2json(pojo);

	}
	return JSON.stringify(str);
}