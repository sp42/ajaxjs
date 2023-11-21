// 使用样式，修改主题可以在 styles 目录下创建新的主题包并修改 iView 默认的 less 变量
// 参考 https://www.iviewui.com/docs/guide/theme

import webApplication from '@bingo/core';
import { ViewUI, iViewPro } from '@bingo/iview-pro';

import App from './App';
import mixinApp from '@/mixins/app';

webApplication
    .use(ViewUI, {
        i18n: (key, value) => webApplication.i18n.t(key, value)
    })
    .use(iViewPro)
    .start({
        mixins: [mixinApp],
        render: h => h(App)
});
