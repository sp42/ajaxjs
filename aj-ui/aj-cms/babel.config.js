module.exports = {
    presets: [
        ['@vue/app', {
            'targets': {
                'browsers': ['ie > 10', 'Chrome > 46']
            },
            'useBuiltIns': 'entry',
            'loose': true,
            'modules': 'auto'
        }]
    ]
    // ],
    // 'plugins': ['@babel/plugin-syntax-dynamic-import',
    //             ['@babel/plugin-transform-runtime', { corejs: 2 }]
    // ]
}
