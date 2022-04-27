<template>
  <div>{{n}}</div>
</template>

<script lang="ts">
/* 后端数据 */
var data = {
  "10;20;30": {
    price: 5,
    count: 1,
  },
  "10;20;31": {
    price: 10,
    count: 2,
  },
  "11;20;30": {
    price: 5,
    count: 1,
  },
  "10;21;31": {
    price: 10,
    count: 2,
  },
  "10;21;32": {
    price: 10,
    count: 9,
  },
};

var myData = {},
  // 可选项 key 值
  keys = [
    [10, 11, 12],
    [20, 21],
    [30, 31, 32],
  ];

// 获取 key 的库存量
function getNum(key: string): number {
  let result: number = 0,
    n = [];

  if (typeof myData[key] != "undefined")   // 检查是否已计算过
    return myData[key];

  let items: string[] = key.split(";");

  if (items.length === keys.length) // 已选择数据是最小路径，直接从已端数据获取
    return data[key] ? data[key].count : 0;

  //拼接子串
  for (let i = 0; i < keys.length; i++) {
    let j;

    for (j = 0; j < keys[i].length && items.length > 0; j++) {
        // @ts-ignore
      if (keys[i][j] == items[0]) 
        break;
    }

    if (j < keys[i].length && items.length > 0) 
        n.push(items.shift());  // 找到该项，跳过
    else {
      for (let m = 0; m < keys[i].length; m++) // 分解求值
        result += getNum(n.concat(keys[i][m], items).join(";"));

      break;
    }
  }

  // 缓存
  myData[key] = result;

  return result;
}

// document.write(getNum("10") + ";"); //输出14
// document.write(getNum("11") + ";"); //输出1
// document.write(getNum("10;21") + ";"); //输出11
// document.write(getNum("21;31") + ";"); //输出2​
export default {
  data() {
    return {
      n: getNum("10"),
    };
  },
};
</script>