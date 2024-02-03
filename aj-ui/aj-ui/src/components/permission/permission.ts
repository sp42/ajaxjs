import Role from './role';
import PermissionMgr from './permission-list.vue';
import { xhr_get } from '../../util/xhr';

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
                permissionList: [
                    // {
                    //     id: 1,
                    //     name: '不限租户访问'
                    // },
                    // {
                    //     id: 2,
                    //     name: '不限租户访问'
                    // },
                    // {
                    //     id: 3,
                    //     name: '不限租户访问'
                    // },
                    // {
                    //     name: '不限租户访问'
                    // },
                    // {
                    //     name: '不限租户访问'
                    // },
                    // {
                    //     name: '不限租户访问'
                    // },
                    // {
                    //     name: '不限租户访问'
                    // },
                ]
            }
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

        },
        clearPermission(): void {
            this.permission.permissionList = [];
        },
        savePermission(): void {
            this.permission.permissionList = [];
        },
        showPermissionMgr(isPermissionMgrPickup: boolean): void {
            this.isShowPermissionMgr = true;
            this.isPermissionMgrPickup = isPermissionMgrPickup;
        },
        pickupPermission(data: any): void {
            debugger
        }
    },
    watch: {
        currentRole(currentRole): void {
            if (currentRole && currentRole.id) {
                xhr_get(`${this.permissionApi}/permission_list_by_role/${currentRole.id}?allow=1`, (j: RepsonseResult) => {
                    if (j.status) {
                        debugger
                    }
                });
            }
        }
    }
}