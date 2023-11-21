// 菜单，顶栏

export default [
    {
        path: '/',
        title: '首页',
        icon: 'md-home',
        hideSider: false, // 是否隐藏侧边栏
        name: 'home', // 用 name 区分哪些二级菜单显示
        auth: [] // 权限
    },
    {
        path: '/log',
        title: '日志',
        icon: 'md-locate',
        hideSider: true,
        name: 'system',
        auth: []
    }
];
