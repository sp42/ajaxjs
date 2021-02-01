"use strict";
function phoneNumber(p) {
    return /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/.test(p);
}
function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}
new Vue({
    el: '.aj-form',
    data: {
        checkUserIdMsg: '',
        checkUserPhoneMsg: '',
        checkUserEmailMsg: '',
        isAllowRegister: false // 是否可以注册
    },
    mounted: function () {
        var _this = this;
        aj.xhr.form(this.$el, aj.xhr.defaultCallBack_cb.delegate(null, null, function (j) { return setTimeout("location.assign('${ctx}/user/login/')", 2000); }), {
            beforeSubmit: function (f, json) {
                if (!_this.$el.$('.privacy').checked) {
                    aj.alert('请同意用户注册协议和隐私政策');
                    return false;
                }
                if (json.password != json.password2) {
                    aj.alert('两次密码输入不一致！');
                    return false;
                }
                json.password = md5(json.password);
                delete json.password2;
            },
            googleReCAPTCHA: '${aj_allConfig.security.disableCaptcha ? ', ' : aj_allConfig.security.GoogleReCAPTCHA.siteId}': 
        });
    },
    methods: {
        checkUserId: function (e) {
            var _this = this;
            var el = e.target, userId = el.value;
            if (aj.formValidator.hasError(el) === undefined)
                aj.xhr.get('${ctx}/user/register/checkIfRepeat/', function (j) {
                    _this.isAllowRegister = !j.result.isRepeat;
                    if (j.result.isRepeat)
                        _this.checkUserIdMsg = userId + "已经注册";
                    else
                        _this.checkUserIdMsg = userId + "可以注册";
                }, {
                    name: userId
                });
        },
        checkEmailValid: function (e) {
            var _this = this;
            var el = e.target, email = el.value;
            if (aj.formValidator.hasError(el) === undefined) {
                aj.xhr.get('${ctx}/user/register/checkIfRepeat/', function (json) {
                    _this.isAllowRegister = !json.result.isRepeat;
                    if (json.result.isRepeat)
                        _this.checkUserEmailMsg = email + "已经注册";
                    else
                        _this.checkUserEmailMsg = email + "可以注册";
                }, {
                    email: email
                });
            }
        },
        checkPhoneValid: function (e) {
            var _this = this;
            var phone = e.target.value;
            this.phoneNumberValid = phoneNumber(phone);
            if (this.phoneNumberValid) {
                aj.xhr.get('${ctx}/user/register/checkIfRepeat/', function (json) {
                    _this.isAllowRegister = !json.result.isRepeat;
                    if (json.result.isRepeat)
                        _this.phoneMsg = phone + "已经注册！";
                    else
                        _this.phoneMsg = phone + "可以注册！";
                }, {
                    phone: phone
                });
            }
        },
        sendSMScode: function (e) {
            e.preventDefault();
            if (this.phoneNumberValid && this.isAllowRegister) {
                var value = aj('form input[name=phone]').value;
                if (phoneNumber(value)) {
                    aj.xhr.post('${ctx}/user/register/sendSMScode', function (json) {
                        if (json && json.msg)
                            ajaxjs.alert.show(json.msg);
                    }, {
                        phoneNo: value
                    });
                }
                else {
                    ajaxjs.alert.show('手机格式不正确！');
                }
            }
        },
        onSubmit: function () {
            if (!this.$el.$('.privacy').checked) {
                ajaxjs.alert.show('请同意用户注册协议和隐私政策');
                return false;
            }
            var passowrd = this.$el.$('input[name=password]');
            if (passowrd.value != this.$el.$('input[name=password2]').value) {
                ajaxjs.alert.show('两次密码输入不一致！');
                return false;
            }
            passowrd.value = md5(passowrd.value);
        }
    }
});
