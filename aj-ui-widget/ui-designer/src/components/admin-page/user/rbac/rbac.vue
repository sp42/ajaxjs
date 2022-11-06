<template>
  <Tabs value="name1" :animated="false" style="padding:1% 3%;">
    <TabPane label="角色管理" name="name1">
      <h2 style="margin-bottom:25px;">用户角色管理<span>角色管理是将所有权限进行分类，形成角色，什么样的角色该拥有什么样的权限，分配给相应的管理人员</span></h2>
      <TreeTable name="角色" :list="UserRole_List" :api="roleListApi" />
    </TabPane>

    <TabPane label="授权管理" name="name2">
      <h2>授权<span>不同类型的人员（角色）可以设置不同的权限，比如只能增加、不能修改和删除</span></h2>
      <div>
        <Button type="primary" style="float:right">保存</Button>
        <div class="select-role">
          选择角色
          <!--   <TreeSelector style="display:inline-block;width:300px;margin-right:20px;" />  -->
          <Button @click="roleInherit.isShow = true">角色继承</Button>
        </div>
      </div>
      <table class="aj-table">
        <!-- <caption>权限管理系统</caption> -->
        <tr>
          <th style="width: 180px;padding:0">
            <div class="out"><b>操作权限</b> <em>功能模块</em></div>
          </th>
          <th>子模块</th>
          <th style="width: 380px;">操作权限</th>
        </tr>

        <tr v-for="(item, index) in resources" :key="index">
          <td align="center" class="group">{{item.group}}</td>
          <td>#{{item.id}} {{item.name}}</td>
          <td align="center">
            <Operate :res-id="item.id" />
          </td>
        </tr>

      </table>

      <Modal v-model="roleInherit.isShow" title="角色继承（复制）" ok-text="继承">
        请为[{{roleInherit.sonRoleName}}]选择继承的父角色。
        <br />
        <div style="margin:25px 0">
          源角色：
          <!--  <TreeSelector style="display:inline-block;width:300px;" /> -->
        </div>
        继承后，将会复制所有属性到当前[{{roleInherit.sonRoleName}}]角色身上。

        注意：复制操作之后会<b>覆盖</b>当前配置，请谨慎[保存]。

      </Modal>
    </TabPane>
    <!--   <TabPane label="功能管理" name="name3">标签三的内容</TabPane>
        <TabPane label="操作管理" name="name3">标签三的内容</TabPane> -->
  </Tabs>
</template>

<script lang="ts" src="./rbac.ts"></script>

<style lang="less" scoped>
h2 span{
  font-size:14px;
  color:gray;
  padding-left: 20px;
}
.select-role{
  margin: 20px 0;
}

table {
  width: 100%;
  margin: 0 auto;
}

/*模拟对角线*/
.out {
  border-top: 40px #d6d3d6 solid; /*上边框宽度等于表格第一行行高*/
  width: 0px; /*让容器宽度为0*/
  height: 0px; /*让容器高度为0*/
  border-left: 180px #bdbabd solid; /*左边框宽度等于表格第一行第一格宽度*/
  position: relative; /*让里面的两个子容器绝对定位*/
    b {
    font-style: normal;
    display: block;
    position: absolute;
    top: -40px;
    left: -100px;
    width: 85px;
    }

    em {
    font-style: normal;
    display: block;
    position: absolute;
    top: -25px;
    left: -170px;
    width: 55x;
    }
}

/*//-------END ----------*/

// 快速制作1px 表格边框，为需要设置的 table 元素加上 border 的class即可
.aj-table {
  border: 1px lightgray solid;
  border-collapse: collapse;
  border-spacing: 0;

  th {
    background-color: #efefef;
    letter-spacing: 3px;
  }

  td,
  th {
    border: 1px lightgray solid;
    line-height: 160%;
    height: 120%;
    padding: 6px;
    
  }

  tr {
    // .transition (background-color 400ms ease-out);

    &:nth-child(odd) {
      background: #f5f5f5;
      box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8) inset;
    }

    &:hover {
      background-color: #fbf8e9;
    }
  }
}

/*//-------END ----------*/
</style>