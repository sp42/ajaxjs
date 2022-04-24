module.exports = {
    publicPath: './', // vue_cli 打包后的项目，默认使用的是绝对路径。改为相对路径
    runtimeCompiler: true,
    pluginOptions: {
        'style-resources-loader': {
            preProcessor: 'less',
            patterns: ['C:\\code\\aj-ui\\src\\style\\common-functions.less']
        }
    },
    lintOnSave: true,
    devServer: {
        overlay: {
            warnings: true,
            error: true
        }
    }
};
