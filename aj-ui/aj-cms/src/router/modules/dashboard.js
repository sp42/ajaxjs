const meta = {
    auth: false
};

const pre = 'dashboard';
const basePath = '/dashboard'

export default [
    {
        path: `${basePath}/console`,
        name: `${pre}Console`,
        meta: {
            ...meta,
            title: '主控台',
            closable: false
        },
        component: () => import('@/pages/dashboard/console')
    }
];
