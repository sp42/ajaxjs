<template>
  <div>
    <h1 class="page-title">权限管理</h1>

    <div class="main">
      <div class="left">
        <div>
          <span class="btns">
            <Button type="primary" icon="ios-add" @click="createTopRoleNode">创建顶级角色</Button>
            <Button type="info" icon="ios-refresh" @click="refreshRoleList">刷新</Button>
          </span>

        </div>
        <h2>角色管理</h2>

        <div class="tree">
          <Tree :data="roleTreeData" @on-select-change="onTreeNodeClk" @on-contextmenu="handleContextMenu">
            <template slot="contextMenu">
              <DropdownItem @click.native="editRole" style="color: cornflowerblue">▶ 编辑角色</DropdownItem>
              <DropdownItem @click.native="addSubNode" style="color: green">
                ➕ 添加子节点
              </DropdownItem>
              <DropdownItem @click.native="delRole" style="color: #ed4014">
                ✖ 删除角色
              </DropdownItem>
            </template>
          </Tree>
        </div>
      </div>

      <div class="right">
        <p style="text-indent:2em;">你可以维护角色的权限，可以给角色分配权限，也可以给角色分配子角色。一个角色对应多个权限；角色可以继承，拥有父级的所有权限。
        </p>
        <fieldset class="panel">
          <legend>继承的父级权限：</legend>
          <div class="inherited-permission">系统管理员-不限租户访问、系统管理员-不限租户访问、系统管理员-不限租户访问
            系统管理员-不限租户访问、系统管理员-不限租户访问、系统管理员-不限租户访问
            系统管理员-不限租户访问、系统管理员-不限租户访问、系统管理员-不限租户访问
            系统管理员-不限租户访问、系统管理员-不限租户访问、系统管理员-不限租户访问
          </div>
        </fieldset>
        <br />
        <br />
        <div>
          <h2>{{currentRole ? '角色 ' + currentRole.name + ' 的权限' : '请选择一个角色'}}</h2>

          <div class="permission-list">
            <select multiple>
              <option v-for="item in permission.permissionList" :key="item.name">{{item.name}}</option>
            </select>
            <div class="permission-bts">
              <Button :disabled="currentRole == null" type="primary" icon="ios-add" @click="addPermission">添加权限</Button>
              <Button :disabled="currentRole == null" type="warning" icon="ios-remove" @click="removePermission">移除权限</Button>
              <Button :disabled="currentRole == null" type="error" icon="ios-close" @click="clearPermission">清空权限</Button>
              <br />
              <Button :disabled="currentRole == null" type="success" icon="ios-add-circle-outline" @click="savePermission">&nbsp;&nbsp;&nbsp;保 存&nbsp;&nbsp;&nbsp;</Button>
            </div>
            <p>增加、删除权限请到<a @click="showPermissionMgr(false)">权限管理</a>。</p>
          </div>
        </div>
      </div>
    </div>

    <Modal v-model="isShisShowRoleEditForm" :title="'角色'+ (!roleForm.isCreate ? ' #' + currentRole.id : '' )" @on-ok="saveRole">
      <Form :model="currentRole" :label-width="100" style="margin-right: 10%;margin-left: 3%;">
        <FormItem label="角色名称">
          <Input v-model="currentRole.name" placeholder="Enter something..."></Input>
        </FormItem>
        <FormItem label="角色说明">
          <Input type="textarea" :rows="4" v-model="currentRole.content" placeholder="Enter something..."></Input>
        </FormItem>
        <FormItem v-if="!roleForm.isTop">
          <Checkbox v-model="currentRole.isInheritedParent">继承父级权限</Checkbox>
        </FormItem>
        <FormItem label="角色状态">
          <label><input type="radio" v-model="currentRole.stat" value="0" /> 启用</label> &nbsp;
          <label><input type="radio" v-model="currentRole.stat" value="2" /> 禁用</label>
        </FormItem>
        <FormItem v-if="!roleForm.isCreate" style="color:gray;">
          创建于 {{ currentRole.createDate | formatDate }} 修改于 {{ currentRole.updateDate | formatDate }}
        </FormItem>
      </Form>
    </Modal>

    <Modal v-model="isShowPermissionMgr" width="1000" title="权限管理列表">
      <PermissionMgr :is-pickup="isPermissionMgrPickup" :on-pickup="pickupPermission" />
    </Modal>
  </div>
</template>

<script lang="ts" src="./permission.ts"></script>

<style lang="less" scoped src="./permission.less"></style>