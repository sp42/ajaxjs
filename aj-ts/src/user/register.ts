
namespace aj.user.register {
    interface md5 {
        (str: string): string;
    }

    function phoneNumber(p: string): boolean {
        return /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/.test(p);
    }

    function validateEmail(email: string): boolean {
        let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }

    interface Register extends Vue {
        checkUserIdMsg: string;
        checkUserPhoneMsg: string;
        checkUserEmailMsg: string;
        isAllowRegister: boolean;
        phoneNumberValid: boolean;

        phoneMsg: string;
    }

    new Vue({
        el: '.aj-form',
        data: {
            checkUserIdMsg: '',
            checkUserPhoneMsg: '',
            checkUserEmailMsg: '',
            isAllowRegister: false // 是否可以注册
        },

        mounted() {
            xhr.form(this.$el,
                //@ts-ignore
                xhr.defaultCallBack.delegate(null, null, (j: RepsonseResult) => setTimeout("location.assign('${ctx}/user/login/')", 2000)),
                {
                    beforeSubmit: (form: HTMLFormElement, json: StringJsonParam) => {
                        if (!this.$el.$('.privacy').checked) {
                            aj.alert('请同意用户注册协议和隐私政策');
                            return false;
                        }

                        if (json.password != json.password2) {
                            aj.alert('两次密码输入不一致！');
                            return false;
                        }

                        //@ts-ignore
                        json.password = md5(json.password);
                        delete json.password2;
                    },
                    // googleReCAPTCHA: '${aj_allConfig.security.disableCaptcha ? '' : aj_allConfig.security.GoogleReCAPTCHA.siteId}'
                }
            );
        },

        methods: {
            checkUserId(this: Register, ev: Event): void {
                let el: form.HTMLFormControl = <form.HTMLFormControl>ev.target,
                    userId: string = el.value;

                if (!form.Validator.check(el))
                    xhr.get('${ctx}/user/register/checkIfRepeat/', j => {
                        this.isAllowRegister = !j.result.isRepeat;

                        if (j.result.isRepeat)
                            this.checkUserIdMsg = userId + "已经注册";
                        else
                            this.checkUserIdMsg = userId + "可以注册";
                    }, {
                        name: userId
                    });
            },
            checkEmailValid(this: Register, ev: Event): void {
                let el: form.HTMLFormControl = <form.HTMLFormControl>ev.target,
                    email: string = el.value;

                if (!form.Validator.check(el)) {
                    xhr.get('${ctx}/user/register/checkIfRepeat/', json => {
                        this.isAllowRegister = !json.result.isRepeat;

                        if (json.result.isRepeat)
                            this.checkUserEmailMsg = email + "已经注册";
                        else
                            this.checkUserEmailMsg = email + "可以注册";
                    }, {
                        email: email
                    });
                }
            },
            checkPhoneValid(this: Register, ev: Event): void {
                let el: HTMLInputElement = <HTMLInputElement>ev.target,
                    phone: string = el.value;

                this.phoneNumberValid = phoneNumber(phone);

                if (this.phoneNumberValid) {
                    xhr.get('${ctx}/user/register/checkIfRepeat/', json => {
                        this.isAllowRegister = !json.result.isRepeat;

                        if (json.result.isRepeat)
                            this.phoneMsg = phone + "已经注册！";
                        else
                            this.phoneMsg = phone + "可以注册！";
                    }, {
                        phone: phone
                    });
                }
            },

            sendSMScode(this: Register, ev: Event): void {
                let el: HTMLInputElement = <HTMLInputElement>ev.target,
                    phone: string = el.value;

                ev.preventDefault();
                if (this.phoneNumberValid && this.isAllowRegister) {
                    let value: string = (<HTMLInputElement>document.body.$('form input[name=phone]')).value;

                    if (phoneNumber(value)) {
                        xhr.post('${ctx}/user/register/sendSMScode', function (json) {
                            if (json && json.msg)
                                alert(json.msg);
                        }, {
                            phoneNo: value
                        });
                    } else
                        alert('手机格式不正确！');
                }
            },

            onSubmit(this: Register) {
                if (!(<HTMLInputElement>this.$el.$('.privacy')).checked) {
                    aj.alert('请同意用户注册协议和隐私政策');

                    return false;
                }

                let passowrd = <HTMLInputElement>this.$el.$('input[name=password]')
                if (passowrd.value != (<HTMLInputElement>this.$el.$('input[name=password2]')).value) {
                    aj.alert('两次密码输入不一致！');
                    return false;
                }

                //@ts-ignore
                passowrd.value = md5(passowrd.value);
            }
        }
    });
}