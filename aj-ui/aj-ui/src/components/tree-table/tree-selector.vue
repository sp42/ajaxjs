<template>

  <Select v-model="selectId">
    <Option :value="item.id" v-for="(item, index) in data" :key="index" size="small">{{item.indent}}{{item.name}}</Option>
  </Select>

</template>

<script lang="ts">
import { TreeMap } from "./index";

export default {
  props: {
    treeJson: { type: Array, required: true },
  },
  data() {
    return {
      data: [],
      selectId: 14,
    };
  },
  watch: {
    treeJson(v): void {
      let json = JSON.parse(JSON.stringify(this.treeJson));
      prepare(<TreeMapLevel[]>json);
      this.data = [];

      plat(this.data, json);
    },
  },
};

/**
 * 处理过的 TreeMap，带有 level 和 indent
 */
type TreeMapLevel = TreeMap & {
  level: number;
  indent: string;
};

const stack: any[] = [];

/**
 * 加入 level
 */
function prepare(arr: TreeMapLevel[]): void {
  if (!arr || !arr.length) return;

  stack.push(arr);

  for (let i: number = 0, j: number = arr.length; i < j; i++) {
    let treeMap: TreeMapLevel = arr[i];
    let level: number = stack.length;

    treeMap.level = level; // 层数，也表示缩进多少个字符

    if (level == 1) treeMap.indent = "";
    else {
      console.log(Math.pow(level - 1, 2));
      let a = new Array(Math.pow(level + 1, 2));
      let b = new Array(Math.pow(level - 2, 2));
      treeMap.indent = a.join("\u00a0") + "└─" + b.join("─");
    }

    prepare(<TreeMapLevel[]>treeMap.children);
  }

  stack.pop();
}

function plat(platArr: TreeMapLevel[], treeMap: TreeMapLevel[]): void {
  for (let i: number = 0, j: number = treeMap.length; i < j; i++) {
    let _treeMap: TreeMapLevel = treeMap[i];
    platArr.push(_treeMap);

    if (_treeMap.children && _treeMap.children.length)
      plat(platArr, <TreeMapLevel[]>_treeMap.children);
  }
}
</script>