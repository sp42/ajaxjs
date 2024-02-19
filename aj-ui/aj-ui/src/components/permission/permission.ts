import Role from './role';
import PermissionMgr from './permission-list.vue';
import { xhr_get, xhr_post, xhr_put } from '../../util/xhr';

export default {
    mixins: [Role],
    components: { PermissionMgr },
    data() {
        return {
            simpleApi: 'http://localhost:8888/iam/simple_api',
            permissionApi: 'http://localhost:8888/iam/permission',
            isShisShowRoleEditForm: false,
            isShowPermissionMgr: false,
            isPermissionMgrPickup: true,
            // 当前角色
            currentRole: {
                name: ''
            },
            permission: {
                inheritPermissionList: [],
                permissionList: []
            },
            selectedPermissions: []
        }
    },
    mounted() {
        this.refreshRoleList();
    },
    methods: {
        addPermission(): void {
            this.showPermissionMgr(true);
        },
        removePermission(): void {
            for (const id of this.selectedPermissions) {
                const index = this.permission.permissionList.findIndex(element => element.id === id);

                if (index !== -1)
                    this.permission.permissionList.splice(index, 1);
            }
        },
        clearPermission(): void {
            this.permission.permissionList = [];
        },

        savePermission(): void {
            let arr: string[] = [];
            this.permission.permissionList.forEach((item: any) => arr.push(item.id));

            let data: any = {
                roleId: this.currentRole.id,
                permissionIds: arr.join(',')
            };

            xhr_post(`${this.permissionApi}/add_permissions_to_role`, (j: RepsonseResult) => {
                if (j.status)
                    this.$Message.success('保存权限成功');
            }, data);
        },
        showPermissionMgr(isPermissionMgrPickup: boolean): void {
            this.isShowPermissionMgr = true;
            this.isPermissionMgrPickup = isPermissionMgrPickup;
        },
        pickupPermission(data: any): void {
            // TODO add multiple selection
            let arr: any[] = this.permission.permissionList;

            for (let i = 0; i < arr.length; i++) {
                if (arr[i].id == data.id) {
                    this.$Message.warning('已经添加了权限' + data.name);
                    return;
                }
            }

            this.permission.permissionList.push({
                id: data.id,
                name: data.name
            });

            this.$Message.success(`添加权限[${data.name}]成功`);
            // debugger
        },
        handlePermissionList(data: any): void {
            this.permission.inheritPermissionList = [];
            this.permission.permissionList = [];

            data.forEach((item: any) => {
                if (item.isInherit)
                    this.permission.inheritPermissionList.push(item);
                else
                    this.permission.permissionList.push({
                        id: item.id,
                        name: item.name
                    });

            });
        }
    },
    watch: {
        currentRole(currentRole): void {
            if (currentRole && currentRole.id) {
                xhr_get(`${this.permissionApi}/permission_list_by_role/${currentRole.id}`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.handlePermissionList(j.data);
                    } else
                        this.$Message.warning(j.message || '获取数据失败');
                });
            }
        }
    }
}