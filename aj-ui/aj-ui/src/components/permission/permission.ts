import Role from './role';

export default {
    mixins: [Role],
    data() {
        return {
            isShisShowRoleEditForm: true,
            data1: [
                {
                    title: 'parent 1',
                    expand: true,
                    children: [
                        {
                            title: 'parent 1-1',
                            expand: true,
                            contextmenu: true,
                            children: [
                                {
                                    title: 'leaf 1-1-1'
                                },
                                {
                                    title: 'leaf 1-1-2'
                                }
                            ]
                        },
                        {
                            title: 'parent 1-2',
                            expand: true,
                            children: [
                                {
                                    title: 'leaf 1-2-1'
                                },
                                {
                                    title: 'leaf 1-2-1'
                                }
                            ]
                        }
                    ]
                }
            ],
            // 当前角色
            currentRole: {
                name: 'rrtre'
            },
            permission: {
                permissionList: [
                //     {
                //         id: 1,
                //         name: '不限租户访问'
                //     },
                //     {
                //         id: 2,
                //         name: '不限租户访问'
                //     },
                //     {
                //         id: 3,
                //         name: '不限租户访问'
                //     },
                //     {
                //         name: '不限租户访问'
                //     },
                //     {
                //         name: '不限租户访问'
                //     },
                //     {
                //         name: '不限租户访问'
                //     },
                //     {
                //         name: '不限租户访问'
                //     },
                // ]
            }
        }
    },
    methods: {
        addPermission(): void {

        },
        removePermission(): void {

        },
        clearPermission(): void {
            this.permission.permissionList = [];
        },
        savePermission(): void {
            this.permission.permissionList = [];
        }
    }
}