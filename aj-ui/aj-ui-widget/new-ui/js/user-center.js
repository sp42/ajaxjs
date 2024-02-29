Vue.component('user-center-main', {
    template: html`<div class="user-center-main">
        <h3 class="aj-center-title">概览</h3>
	<h4>欢迎</h4>
	<hr class="aj-hr" />
	<div style="padding:0 3% 5% 3%;">
		<div style="float:right;"><a href="/profile/">修改个人资料</a>

		 | <a href="admin/">进入后台</a> 

		<br />
		上次登录  查看<a href="/user/account/log-history/">登录历史</a></div>
		您好，{{$parent.username}}，很高兴为您服务：）
	
	</div>
	
	<h4>我的订单</h4>
	<hr class="aj-hr" />
	<div class="box" style="margin: 0 auto;width: 500px;">
		<img src="https://ajaxjs.nos-eastchina1.126.net/icons/empty-cart.png" width="160" style="vertical-align: middle;" /> 还没消费过，马上<a href="{{ctx}}/shop/goods/">去看看？</a>~
	</div>
  </div>`,
});

Vue.component('user-center-order', {
    template: html`<div class="user-center-order">
        <h3 class="aj-center-title">订单</h3>
       
    </div>`,
});

Vue.component('user-center-cart', {
    template: html`<div class="user-center-cart">
        <h3 class="aj-center-title">购物车</h3>
    </div>`,
});

Vue.component('user-center-profile', {
    template: html`<div class="user-center-profile">
        <h3 class="aj-center-title">个人信息</h3>
        <h4>个人头像</h4>
        <hr class="aj-hr" />
        <h4>个人信息</h4>
        <hr class="aj-hr" />
        <h4>收货地址</h4>
        <hr class="aj-hr" />
    </div>`,
});

Vue.component('user-center-account', {
    template: html`<div class="user-center-account"><h3 class="aj-center-title">帐号管理</h3>
    <ul class="safe">
        <li>	
            <a href="javascript:openPopupTpl('modiflyPhone');">修改登录名</a>
            <div :class="$parent.username ? 'ok' : 'fail' ">登录名</div>
            <div>登录名即用户名</div>
        </li>
        <li>	
            <a href="###" @click="showSetPhone = true">设置手机</a>
            <div :class="$parent.USER.phone ? 'ok' : 'fail'">绑定手机</div>
            <div>{{$parent.USER.phone || '未绑定手机'}}</div>
        </li>
        <li>	
            <a href="###" @click="showSetEmail = true">设置邮箱</a>
            <div :class="$parent.USER.email ? 'ok' : 'fail' ">绑定邮箱</div>
            <div>{{$parent.USER.email || '未绑定邮箱'}}</div>
        </li>
        <li>	
            <a href="###" @click="showChangePsw = true">修改密码</a>
            <div class="ok">设置密码</div><div>已设置</div>
        </li>
        <li class="log-history">	
            <a href="log-history/">查看登录历史</a>
            <div style="padding-left:3%;">最近登录</div>
            <div>
               
            </div>
        </li>
        <li>	
            <a href="oauth/">管理绑定</a>
            <div style="padding-left:3%;">第三方登录</div><div>通过微博、微信、QQ等第三方登录的绑定</div>
        </li>
        <li>	
            <div style="padding-left:3%;">用户组</div><div></div>
        </li>
        <li>	
            <a href="###" @click="isShowDelAccount = true">账号注销</a>
            <div style="padding-left:3%;">删除帐号</div>
            
            <div>删除该帐号以及所有该帐号关联的信息</div>
        </li> 
    </ul>
    <aj-layer v-if="showSetPhone">
        <div style="width:800px; height:400px;text-align: center;">
            <h1>设置手机</h1>
            <aj-process-line />
            <form class="aj-form" style="width:300px; margin: 0 auto;">
                <div>
                    <input type="text" placeholder="请输入手机号码" size="30" />
                </div>
                <div>
                    <input type="text" placeholder="请输入验证码" size="10" /> <button>发送验证码</button>
                </div>
                <div>
                    <button>保存手机</button> &nbsp;&nbsp;&nbsp;<a href="###" @click="showSetPhone=false">取消</a>
                </div>
            </form>
        </div>
    </aj-layer>
    <aj-layer v-if="showSetEmail">
        <div style="width:800px; height:400px;text-align: center;">
            <h1>设置邮箱</h1>
            <aj-process-line />
            <form class="aj-form" style="width:300px; margin: 0 auto;">
                <div>
                    <input type="text" placeholder="请输入邮箱" size="30" />
                </div>
                <div>
                    <input type="text" placeholder="请输入邮箱验证码" size="10" /> <button>发送验证码</button>
                </div>
                <div>
                    <button>保存邮箱</button> &nbsp;&nbsp;&nbsp;<a href="###" @click="showSetEmail=false">取消</a>
                </div>
            </form>
        </div>
    </aj-layer>
    <aj-layer v-if="showChangePsw">
        <div style="width:800px; height:400px;text-align: center;">
            <h1>修改密码</h1>
            <aj-process-line />
            <form class="aj-form" style="width:300px; margin: 0 auto;">
                <div>
                    <input type="password" placeholder="请输入原密码" size="30" />
                </div>
                <div>
                    <input type="password" placeholder="请输入新密码" size="30" />
                </div>
                <div>
                    <input type="password" placeholder="请重复输入新密码" size="30" /> 
                </div>
                <div>
                    <button>修改密码</button> &nbsp;&nbsp;&nbsp;<a href="###" @click="showChangePsw=false">取消</a>
                </div>
            </form>
        </div>
    </aj-layer>
    <aj-confirm v-show="isShowDelAccount" state="isShowDelAccount" :confirm-handler="delAccount" message="确定删除帐号吗？" />
  </div>`,

    data() {
        return {
            userMail: null,
            userPhone: null,
            isEmailVerified: null,
            isShowDelAccount: false,
            showSetPhone: false,
            showSetEmail: false,
            showChangePsw: false,
        };
    },
    mounted() {
    },
    methods: {
        delAccount() {
            alert(9)
        }
    }
});

Vue.component('user-center-login-log', {
    template: html`<div class="user-center-login-log">
        <h3 class="aj-center-title">登录历史记录</h3>
        <aj-list style="margin: 0 25px">
            <template v-slot:header>
                <th>日期</th><th>登录类型</th><th>登录 IP</th><th>登录地区</th>
            </template>
            <template v-slot:default="data">
                <td>{{data.data.createDate}}</td><td>{{loginType[data.data.loginType]}}</td><td>{{data.data.ip}}</td><td>{{data.data.ipLocation}}</td>
            </template>
        </aj-list>
</div>
    `,
    data() {
        return {
            loginType: {
                1: 'PC登录',
                2: '小程序登录'
            }
        };
    }
});

Vue.component('user-center', {
    template: html`<div class="user-center">
        <div class="left">
				<div class="avatar">
					<div class="imgHolder userAvatar">
						<img :src="USER.avatar" />
					</div>

					<h3 class="userName">{{username}}</h3>
				</div>
				<menu>
					<ul>
						<li :class="{actived: showing == 'main'}"><a href="###" @click="showing = 'main'">概 览</a></li>
						<li :class="{actived: showing == 'order'}"><a href="###" @click="showing = 'order'">订单</a></li>
						<li :class="{actived: showing == 'cart'}"><a href="###" @click="showing = 'cart'">购物车</a></li> 
						<li :class="{actived: showing == 'bookmark'}"><a href="###" @click="showing = 'bookmark'">收藏夹</a></li> 
						<li :class="{actived: showing == 'profile'}"><a href="###" @click="showing = 'profile'">个人信息</a></li>
						<li :class="{actived: showing == 'account'}"><a href="###" @click="showing = 'account'">帐号管理</a></li>
						<li><a href="###" @click="isShow = true">退出登录</a></li>
					</ul>
				</menu>
				<!-- 对话框 -->
				<span class="logout"><aj-confirm v-show="isShow" message="确定退出吗？"></aj-confirm></span>
			</div>
			<div class="right">
                <user-center-main v-if="showing =='main'" />
                <user-center-order v-if="showing =='order'" />
                <user-center-cart v-if="showing =='cart'" />
                <user-center-profile v-if="showing =='profile'" />
                <user-center-account v-if="showing =='account'" />
                <user-center-login-log v-if="showing =='loginLog'" />
			</div>
  </div>`,
    props: {
        username: {
            type: String,
            required: false
        },
        interval: {
            type: Number,
            default: 5000
        }
    },
    data() {
        return {
            showing: 'loginLog',
            isShow: false,
            USER: {},
        }
    },

    mounted() {
        new aj.aj_iam.Sdk().getUserInfo(user => {
            this.USER = user;
            this.username = user.loginId;
        });
    },
    methods: {
        confirm() {
            aj.xhr.get('logout');// 本地登出
            sdk.logout('/');
        }
    }
});