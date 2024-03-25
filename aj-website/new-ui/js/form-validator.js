// 判断浏览器是否支持 HTML5 表单验证
if (!('validity' in document.createElement('input')))
    window.alert("不支持 HTML5 表单验证");

function ErrorElement(el, msg) {
    this.el = el;
    this.msg = msg;
}

// 表单验证器
function Validator(el) {
    // 禁止浏览器原生的错误提示
    el.setAttribute('novalidate', 'true');

    this.el = el;
    this.errorElements = [];

    // 对每一个表单元素监听事件，一失去焦点就触发表单验证
    this.checkEveryField();
}

// 表单元素
Validator.prototype.el = null;

// 错误信息元素列表
Validator.prototype.errorElements = null;

// 对每一个表单元素监听事件，一失去焦点就触发表单验证
Validator.prototype.checkEveryField = function () {
    var arr = this.el.querySelectorAll('input');
    var that = this;

    for (var i = 0, len = arr.length; i < len; i++) {
        arr[i].addEventListener('blur', function (ev) {
            var el = ev.target;

            // 忽略部分元素
            if (el.tagName == "A" || Validator.isIgnoreEl(el))
                return;

            var result = Validator.check(el);

            if (result) { // 如果有错误,就把它显示出来
                that.errorElements.push(result);
                that.showError(result);
            } else
                that.removeError(el); // 否则, 移除所有存在的错误信息
        }, true);
    }
};

Validator.prototype.check = function () {
    var arr = this.el.querySelectorAll('input');
    var that = this;
    that.errorElements = [];

    for (var i = 0, len = arr.length; i < len; i++) {
        var el = arr[i];

        // 忽略部分元素
        if (el.tagName == "A" || Validator.isIgnoreEl(el))
            continue;

        var result = Validator.check(el);

        if (result) { // 如果有错误,就把它显示出来
            that.errorElements.push(result);
            that.showError(result);
        } else
            that.removeError(el); // 否则, 移除所有存在的错误信息

    }

    return that.errorElements;
};

// 显示错误信息
Validator.prototype.showError = function (err) {
    var el = err.el,
        id = el.id || el.name; // 获取字段 id 或者 name

    if (!id)
        return;

    err.el.classList.add('error'); // 将错误类添加到字段

    // 检查错误消息字段是否已经存在 如果没有, 就创建一个
    var message = err.el.form.querySelector(`.error-message#error-for-${id}`);

    if (!message) {
        message = document.createElement('div');
        message.className = 'error-message';
        message.id = 'error-for-' + id;
        el.parentNode?.insertBefore(message, el.nextSibling);
    }

    el.setAttribute('aria-describedby', 'error-for-' + id);// 添加 ARIA role 到字段
    message.innerHTML = err.msg;// 更新错误信息

    message.classList.remove('hide');
};

// 移除所有的错误信息
Validator.prototype.removeError = function (el) {
    var id = el.id || el.name;// 获取字段的 id 或者 name

    if (!id)
        return;

    el.classList.remove('error'); // 删除字段的错误类
    el.removeAttribute('aria-describedby'); // 移除字段的 ARIA role

    var message = el.form.querySelector(`.error-message#error-for-${id}`); // 检查 DOM 中是否有错误消息

    if (message) {
        message.innerHTML = ''; // 如果有错误消息就隐藏它
        message.classList.add('hide');
    }
};

// 是否忽略的表单元素
Validator.isIgnoreEl = function (el) {
    return el.disabled || el.type === 'file' || el.type === 'reset' || el.type === 'submit' || el.type === 'button';
};


// 验证字段
Validator.check = function (field) {
    var validity = field.validity; // 获取 validity

    if (!validity)
        throw '浏览器不支持 HTML5 表单验证';

    if (validity.valid) // 通过验证
        return null;
    else {
        var result = new ErrorElement(field, "无效输入"); // 通用错误讯息 The value you entered for this field is invalid.

        if (validity.valueMissing)      // 如果是必填字段但是字段为空时
            result.msg = '该项是必填项';

        if (validity.typeMismatch) {    // 如果类型不正确
            if (field.type === 'email')
                result.msg = '请输入有效的邮件地址';
            else if (field.type === 'url')
                result.msg = '请输入一个有效的网址';
            else
                result.msg = '请输入正确的类型';
        }

        if (validity.tooShort)
            result.msg = `请输入至少${field.getAttribute('minLength')}个字符。当前你输入了${field.value.length}个字符。`;
        // 'Please lengthen this text to ' + field.getAttribute('minLength') + ' characters or more. You are currently using ' + field.value.length + ' characters.'

        if (validity.tooLong)
            result.msg = `你只能输入最多${field.getAttribute('maxLength')}个字符。当前你输入了${field.value.length}个字符。`;
        // 'Please shorten this text to no more than ' + field.getAttribute('maxLength') + ' characters. You are currently using ' + field.value.length + ' characters.';

        if (validity.badInput)
            result.msg = '请输入数字';

        if (validity.stepMismatch)  // 如果数字值与步进间隔不匹配
            result.msg = '请选择一个有效值';

        if (validity.rangeOverflow)     // 如果数字字段的值大于 max 的值
            result.msg = `请选择小于 ${field.getAttribute('max')} 的值`;
        // return 'Please select a value that is no more than ' + field.getAttribute('max') + '.';

        if (validity.rangeUnderflow)
            result.msg = `请选择大于 ${field.getAttribute('min')} 的值`;
        // return 'Please select a value that is no less than ' + field.getAttribute('min') + '.';

        if (validity.patternMismatch)
            result.msg = field.getAttribute('title') || '格式要求不正确';

        return result;
    }
};


aj.formInit = function (el, beforeSubmit) {
    const formEL = document.querySelector(el);
    const validator = new Validator(formEL);

    formEL.addEventListener('submit', (event) => {
        event.preventDefault();
        validator.check();

        const errors = validator.check();

        if (errors.length > 0) {
            // 显示错误信息
            for (const error of errors) {
                console.log(error);
            }

            return;
        }

        // 表单验证通过，提交表单
        let data = aj.xhr.formData(formEL);

        if (beforeSubmit && !beforeSubmit(formEL, data)) {
            return;
        }

        aj.xhr.postForm(formEL.getAttribute('action'), data, (j) => {
            console.log(j)
        });
    });
}