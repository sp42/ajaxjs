export function formatSql(sql, options) {
    // 默认选项
    const defaultOptions = {
      indent: 4,
      quoteIdentifiers: true,
      quoteStrings: true,
    };
  
    // 合并选项
    options = Object.assign({}, defaultOptions, options);
  
    // 将 SQL 字符串转换为字符数组
    const sqlTokens = sql.split(" ");
  
    // 格式化 SQL 语句
    for (let i = 0; i < sqlTokens.length; i++) {
      // 标识符
      if (sqlTokens[i].match(/^[a-zA-Z][a-zA-Z0-9_]+$/)) {
        sqlTokens[i] = quoteIdentifier(sqlTokens[i], options);
      }
  
      // 字符串
      else if (sqlTokens[i].match(/^".+"$/)) {
        sqlTokens[i] = quoteString(sqlTokens[i], options);
      }
  
      // 标签
      else if (sqlTokens[i].match(/^<\w+.+?>/)) {
        // 获取标签名称
        const tag = sqlTokens[i].substr(1, sqlTokens[i].length - 2);
  
        // 处理标签
        switch (tag) {
          case "if":
            sqlTokens[i] = handleIfTag(sqlTokens, i, options);
            break;
          case "foreach":
            sqlTokens[i] = handleForeachTag(sqlTokens, i, options);
            break;
          default:
            // 不做任何操作
            break;
        }
      }
  
      // 其他
      else {
        // 不做任何操作
      }
    }
  
    // 将格式化后的 SQL 字符串拼接起来
    return sqlTokens.join(" ");
  }
  
  // 处理 if 标签
  function handleIfTag(sqlTokens, i, options) {
    // 获取 if 标签的条件
    const condition = sqlTokens[i + 1];
  
    // 判断条件是否为空
    if (condition === "") {
      return "";
    }
  
    // 获取 if 标签的主体
    const body = sqlTokens.slice(i + 2, sqlTokens.indexOf("</if>", i));
  
    // 格式化 if 标签的主体
    body = formatSql(body, options);
  
    // 将 if 标签拼接起来
    return `<if test="${condition}">${body}</if>`;
  }
  
  // 处理 foreach 标签
  function handleForeachTag(sqlTokens, i, options) {
    // 获取 foreach 标签的变量名称
    const varName = sqlTokens[i + 1];
  
    // 获取 foreach 标签的集合
    const collection = sqlTokens[i + 2];
  
    // 获取 foreach 标签的主体
    const body = sqlTokens.slice(i + 3, sqlTokens.indexOf("</foreach>", i));
  
    // 格式化 foreach 标签的主体
    body = formatSql(body, options);
  
    // 将 foreach 标签拼接起来
    return `<foreach collection="${collection}" item="${varName}" open="(" close=")" separator=",">${body}</foreach>`;
  }
  

  function quoteIdentifier(identifier, options) {
    // 默认选项
    const defaultOptions = {
      identifierQuote: "\"",
      escapeChar: "\\",
    };
  
    // 合并选项
    options = Object.assign({}, defaultOptions, options);
  
    // 判断标识符是否包含空格
    if (identifier.indexOf(" ") !== -1) {
      // 使用引号引用
      return options.identifierQuote + identifier + options.identifierQuote;
    }
  
    // 不包含空格，直接返回
    return identifier;
  }
  

  function quoteString(string, options) {
    // 默认选项
    const defaultOptions = {
      stringQuote: "\"",
      escapeChar: "\\",
    };
  
    // 合并选项
    options = Object.assign({}, defaultOptions, options);
  
    // 判断字符串是否包含引号或其他特殊字符
    if (string.indexOf(options.escapeChar) !== -1) {
      // 使用引号引用
      return options.stringQuote + string.replace(options.escapeChar, options.escapeChar + options.escapeChar) + options.stringQuote;
    }
  
    // 不包含引号或其他特殊字符，直接返回
    return string;
  }
  