
module.exports = function (config, libs) {
    // 开发环境
    if(config.get('mode')==='development'){
        //浏览器调试可看到原始代码： 原始源代码，编译普通速度
        config.set('devtool','eval-source-map');
    }

    // 重新设置 alias
    config.resolve.alias
        .set('@', libs.resolveWorkspace('./src'))
        .set('src', libs.resolveWorkspace('./src'))
        .set('@api', libs.resolveWorkspace('./src/api'))
        .set('libs', libs.resolveWorkspace('./src/libs'));

    // 不编译 iView Pro
    config.module
        .rule('js')
        .test(/\.jsx?$/)
        .exclude
        .add(libs.resolveWorkspace('./src/libs/iview-pro'))
        .end();

    // 排除 image
    const imagesRule = config.module.rule('images');
    imagesRule
        .test(/\.(png|jpe?g|gif|webp|svg)(\?.*)?$/)
        .exclude
        .add(libs.resolveWorkspace('./src/assets/svg'))
        .end();

};