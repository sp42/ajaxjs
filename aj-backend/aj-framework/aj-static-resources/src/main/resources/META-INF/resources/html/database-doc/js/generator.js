function beanGen(tableInfo) {
    let fields = [], getter = [];

    tableInfo.columns.forEach(col => {
        let type = sqlType2javaType(col.type);
        let fieldName = toHump(col.name);

        let tpl =
            `   /**
    * ${col.comment}
    */
    ${MAIN.codeGenerator.data.isGetter ? 'private' : 'public'} ${type} ${fieldName};`;
        fields.push(tpl);

        let getterTpl = `    public void set${firstLetterUpper(fieldName)}(${type} ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }

    public ${type} get${firstLetterUpper(fieldName)}() {
        return ${fieldName};
    }`;
        getter.push(getterTpl);
    });

    let tpl =
        `package ${MAIN.codeGenerator.data.packageName};

/**
* ${tableInfo.comment}
* 
* ${MAIN.codeGenerator.data.author ? '@author' : ''} ${MAIN.codeGenerator.data.author}
*/
@Data
public class ${firstLetterUpper(toHump(tableInfo.name))} implements IBaseModel {
${fields.join('\n\n')}
${MAIN.codeGenerator.data.isGetter ? getter.join('\n'): ''}
}`;

    return tpl;
}

let _long = ["int(11)", "bigint"];
let _int = ["int", "smallint", "mediumint", "integer", "tinyint"];
let _boolean = ["tinyint(1)"];
let _float = ["float"];
let _double = ["double", "decimal"];
let text = ["char", "varchar", "tinytext", "text", "mediumtext", "longtext"];
let date = ["date", "datetime", "timestamp"];

function has(arr, str) {
    for (var i = 0, j = arr.length; i < j; i++) {
        if (str.indexOf(arr[i]) != -1)
            return true; // found
    }

    return false; // not found
}

function sqlType2javaType(type) {
    if (has(text, type))
        return 'String';

    if (has(_long, type))
        return 'Long';

    if (has(_boolean, type))
        return 'Boolean';

    if (has(_int, type))
        return 'Integer';

    if (has(_float, type))
        return 'Float';

    if (has(_double, type))
        return 'Double';

    if (has(date, type))
        return 'Date';

    console.log(type);
    return 'Object';
}

function firstLetterUpper(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

// 下划线转换驼峰
function toHump(name) {
    return name.replace(/\_(\w)/g, function (all, letter) {
        return letter.toUpperCase();
    });
}

// 驼峰转换下划线
function toLine(name) {
    return name.replace(/([A-Z])/g, "_$1").toLowerCase();
}