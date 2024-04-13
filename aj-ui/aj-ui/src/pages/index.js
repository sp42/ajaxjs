import DataSource from "../components/data-service/datasource/datasource.vue";
// import DataService from "../components/data-service/data-service.vue";
import DataService from "../components/data-service2/data-service.vue";
import DataServiceIndex from "../components/data-service/index/index.vue";
import FactoryList from "../components/factory-list/list.vue";
import FactoryListLoader from "../components/factory-list/list-loader.vue";
import ModelMgr from "../components/factory-form/list.vue";
import DeveloperTools from "../components/admin-page/developer-tools/index.vue";
import DataDict from "../components/admin-page/system/data-dict.vue";
import SysConfig from "../components/admin-page/system/config.vue";
import RBAC from "../components/admin-page/user/rbac/rbac.vue";
import UserListIndex from "../components/admin-page/user/user-list-index.vue";
import OrgIndex from "../components/admin-page/user/org/index.vue";
import WebsiteConfig from "../components/admin-page/website/config.vue";
import ApiHelper from "../components/api-helper/api-helper.vue";
import Permission from "../components/permission/permission-index.vue";
import { setBaseQueryString } from "../util/xhr";

function getQueryParam(variable, isParent) {
    var query = (isParent ? parent.location : window.location).search.substring(
        1
    );
    var vars = query.split("&");

    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");

        if (pair[0] == variable) return pair[1];
    }

    return false;
}

window.loginUrl = "http://127.0.0.1:8088/base/user/login?web_url=http://localhost:8081";
const token = getQueryParam("token");
let accessToken = localStorage.getItem("accessToken");

if (!accessToken && !token) {
    alert("你未登录！");
    location.assign(loginUrl);
}

if (token) {
    accessToken = decodeURIComponent(token);
    localStorage.setItem("accessToken", accessToken);

    // 只需要第一次的参数，之后不需要，现在清除       
    const url = new URL(location.href); // 创建一个包含查询参数的URL
    const params = new URLSearchParams(url.search);// 获取URL中的查询参数
    params.delete('token');// 删除名为'b'的参数
    url.search = params.toString();// 更新URL的查询参数

    location.assign(url.href);
}

// window.JWT_TOKEN = JSON.parse(accessToken);

// 将JWT Token拆分为三个部分
// const tokenParts = window.JWT_TOKEN.id_token.split(".");
const tokenParts = accessToken.split(".");
const payload = JSON.parse(atob(tokenParts[1])); // 解析载荷
console.log(payload.aud);

// 租户 id
const arr = payload.aud.match(/(?<=tenantId=)\d+/);

if (arr) {
    const tenantId = arr[0];
    setBaseQueryString({ tenantId: tenantId });
}


// 监听来自A域的网页发送的消息
window.addEventListener('message', function (event) {
    // if (event.origin === 'http://A域的网页地址') {
    console.log('收到来自A域的网页的消息：', event.data);

    if (event.data === 'doLogout') {
        localStorage.removeItem("accessToken");
    }
    // }
});

let SHOW_MOUDLE; // 显示的模块

function getQueryString() {
    var url = new URL(window.location.href);
    // 创建 URLSearchParams 对象
    var params = new URLSearchParams(url.search);

    switch (params.get("m")) {
        case "portal":
            SHOW_MOUDLE = {
                portal: true,
                title: "门户管理",
                expandMenu: ["portal-1", "portal-2"],
                load: "WebsiteConfig",
            };
            break;
        case "model":
            SHOW_MOUDLE = {
                model: true,
                title: "业务建模",
                expandMenu: ["model"],
                load: "DataService",
            };
            break;
        default:
        case "user":
            SHOW_MOUDLE = {
                user: true,
                title: "AJ-IAM 管理后台",
                expandMenu: ["user-1", "user-2"],
                load: "UserListIndex",
            };
            break;
    }
}

getQueryString();

export default {
    components: {
        DataServiceIndex,
        DataSource,
        DataService,
        FactoryList,
        FactoryListLoader,
        ModelMgr,
        WebsiteConfig,
        DeveloperTools,
        DataDict,
        SysConfig,
        RBAC,
        UserListIndex,
        OrgIndex,
        ApiHelper,
        Permission,
    },
    data() {
        return {
            load: SHOW_MOUDLE.load || "ModelMgr",
            apiRoot: window.config.dsApiRoot,
            listId: 0,
            listTitle: "",
            SHOW_MOUDLE: SHOW_MOUDLE,
        };
    },
    mounted() {
        let menu = document.querySelector(".ivu-menu.ivu-menu-vertical");

        if (menu) {
            let style = "";

            if (this.SHOW_MOUDLE.portal) style = "portal";
            else if (this.SHOW_MOUDLE.model) style = "model";
            else if (this.SHOW_MOUDLE.user) style = "user";

            menu.classList.add(style);
        }
    },
    methods: {
        routeTo(route) {
            location.hash = "#/" + route;
        },
        open(route) {
            window.open("#/" + route);
        },
        showList(id, title) {
            this.load = "showList";
            this.listId = id;
            this.listTitle = title;
        },
    },
};