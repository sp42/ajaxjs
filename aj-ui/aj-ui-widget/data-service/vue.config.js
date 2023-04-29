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
    },
    chainWebpack: config => {
        config.module.rule('images').set('parser', {
            dataUrlCondition: {
                maxSize: 15 * 1024 // 4KiB 内联文件的大小限制
            }
        });

        config.plugin('html').tap(args => {
            args[0].title = '你要的标题';

            return args;
        });
    }
};
