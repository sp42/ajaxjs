module.exports={
    /** 本地开发环境端口 */
    "port": 8080,
  /** 依赖模块，如: ["@bingo/core"]，如果为空或空数组，自动从package.json分析 */
  "moduleDependencies": [],
  /** 排除依赖，如: ["@bingo/core"] */
  "moduleExcludes": [],
  /** npm run pack 是否生成dll.js */
  "dll": false,
   /** 指定npm组件打入模块dll，注意其它模块不能再打入，如: ["vue", "ivew"] */
  "npmPacks": [],
  /** 编译输出的dist目录 */
  "distPath": "../dist",
  /** 编译 module 源目录 */
  "modulePath": "../module",
  /** 编译 module 入口文件 */
  "moduleEntry": "../module/index.js",
  /** 编译应用的源文件 */
  "srcPath": "../src",
  /** 编译应用的入口文件 */
  "srcEntry": "../src/main.js",
  /** 开发时反向代理配置 */
  "proxy": "./proxy.conf",
  /** 是否启用css sourceMap */
  "cssSourceMap":true,
  /** webpack参数 */
  "webpack": "./webpack.conf",  
  "chainWebpack": "./chain.webpack",
  "iviewLoaderOptions": {
    "prefix": false
  },
  /** iview admin mock配置数据 */
  "mock": [
    "../src/mock"
  ],
  /** iview admin 是否启用国际化 */
  "i18n": true,
  "markdown": true,
  "eslint": false,
};
