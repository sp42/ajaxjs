function onSelect() {
    let old = document.querySelector('.selected');
    old.classList.remove('selected');

    this.classList.add('selected');
}

document.querySelectorAll('menu li').forEach(item => {
    item.onclick = onSelect;
});

const loginUrl = 'http://127.0.0.1:8302/user/login?web_url=http://192.168.1.107:8080/dev-plat/';
const token = getQueryParam("token");
let accessToken = localStorage.getItem("accessToken");

if (!accessToken && !token) {
    alert('你未登录！');
    // location.assign(loginUrl);
}

if (token) {
    accessToken = decodeURIComponent(token);
    localStorage.setItem("accessToken", accessToken);
}

window.JWT_TOKEN = JSON.parse(accessToken);

// 将JWT Token拆分为三个部分
const tokenParts = window.JWT_TOKEN.id_token.split('.')
const payload = JSON.parse(atob(tokenParts[1])); // 解析载荷

new Vue({
    el: '#user-bar',
    data: {
        jwtToken: payload,
        loginUrl: loginUrl
    },
    methods: {
        logout() {
            localStorage.removeItem("accessToken");
            location.assign(loginUrl);
        }
    }
});