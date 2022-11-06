<template>
  <span>
    <label><input type="checkbox" name="read" v-model="allowRead" @change="crudClick" /> 访问/查询 </label> 
    <label><input type="checkbox" name="create" v-model="allowCreate" @change="crudClick" /> 新增 </label> 
    <label><input type="checkbox" name="update" v-model="allowUpdate" @change="crudClick" /> 修改 </label> 
    <label><input type="checkbox" name="delete" v-model="allowDelete" @change="crudClick" /> 删除 </label>
  </span>
</template>

<script lang="ts">
export default {
  props: {
    resId: Number, // 资源权限值
    setRightValue: Number, // 操作权限值，
  },
  data() {
    return {
      rightValue: this.setRightValue,
      allowRead: (this.setRightValue & 1) === 1,
      allowCreate: (this.setRightValue & 2) === 2,
      allowUpdate: (this.setRightValue & 4) === 4,
      allowDelete: (this.setRightValue & 8) === 8,
    };
  },
  mounted() {
    /* 可以通过 props 单向绑定 resRightValue，但每个组件要设置一样属性。这里避免多处重复设置属性  */
    // this.$watch('$parent.$parent.resRightValue', v => this.enabled = this.check(v, this.resId));
  },
  methods: {
    /**
     * 检查是否有权限
     *
     * @return {Boolean} true =  有权限，反之无
     */
    check(num: number, pos: number): boolean {
      console.log(num);
      num = num >>> pos;

      return (num & 1) === 1;
    },
    toggleRight(val: boolean, right: number): void {
      if (val === false && (this.rightValue & right) === right)
        // 有权限
        this.rightValue -= right;

      if (val === true && (this.rightValue & right) !== right)
        this.rightValue += right;
    },
    userEnableClick(e) {
      // 用户点击事件，不是来自数据的变化，修改立刻被保存到服务端
      let isEnable = e.target.checked;
      //   let  userGroupId = ASSIGN_RIGHT.userGroupId; // 全局变量

      //   if (userGroupId && this.resId) {
      // aj.xhr.post(
      //   "../user_group/updateResourceRightValue",
      //   (j) => aj.msg.show(j.msg),
      //   {
      //     userGroupId: userGroupId,
      //     isEnable: isEnable,
      //     resId: this.resId,
      //   }
      // );
      //   }
    },
    crudClick() {},
  },
  watch: {
    allowRead(val) {
      this.toggleRight(val, 1);
    },
    allowCreate(val) {
      this.toggleRight(val, 2);
    },
    allowUpdate(val) {
      this.toggleRight(val, 4);
    },
    allowDelete(val) {
      this.toggleRight(val, 8);
    },
  },
};
</script>