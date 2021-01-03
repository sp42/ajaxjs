"use strict";
/**
 * 表单验证器
 *
 * https://www.w3cplus.com/css/form-validation-part-2-the-constraint-validation-api-javascript.html
 * https://github.com/cferdinandi/validate/blob/master/src/js/validate.js
 */
var aj;
(function (aj) {
    var form;
    (function (form_1) {
        /**
         * 验证字段
         *
         * @param field
         */
        function hasError(field) {
            // if (!field || !field.getAttribute)
            //     console.log(field);
            if (field.disabled || field.type === 'file' || field.type === 'reset' || field.type === 'submit' || field.type === 'button')
                return ""; // 忽略部分元素
            var validity = field.validity; // 获取 validity
            if (!validity)
                return '浏览器不支持 HTML5 表单验证';
            if (validity.valid) // 如果通过验证,就返回 ""
                return "";
            if (validity.valueMissing) // 如果是必填字段但是字段为空时
                return '该项是必填项';
            if (validity.typeMismatch) { // 如果类型不正确
                if (field.type === 'email')
                    return '请输入有效的邮件地址';
                if (field.type === 'url')
                    return '请输入一个有效的网址';
                return '请输入正确的类型';
            }
            if (validity.tooShort) // 如果输入的字符数太短
                return 'Please lengthen this text to ' + field.getAttribute('minLength') + ' characters or more. You are currently using ' + field.value.length + ' characters.';
            if (validity.tooLong) // 如果输入的字符数太长
                return 'Please shorten this text to no more than ' + field.getAttribute('maxLength') + ' characters. You are currently using ' + field.value.length + ' characters.';
            if (validity.badInput) // 如果数字输入类型输入的值不是数字
                return 'Please enter a number.';
            if (validity.stepMismatch) // 如果数字值与步进间隔不匹配
                return 'Please select a valid value.';
            if (validity.rangeOverflow) // 如果数字字段的值大于max的值
                return 'Please select a value that is no more than ' + field.getAttribute('max') + '.';
            if (validity.rangeUnderflow) // 如果数字字段的值小于min的值
                return 'Please select a value that is no less than ' + field.getAttribute('min') + '.';
            if (validity.patternMismatch) { // 如果模式不匹配
                var title = field.getAttribute('title');
                if (title) // 如果包含模式信息，返回自定义错误
                    return title;
                return '格式要求不正确'; // 否则, 返回一般错误
            }
            return 'The value you entered for this field is invalid.'; // 如果是其他的错误, 返回一个通用的 catchall 错误
        }
        /**
         *
         * @param field
         * @param error
         * @param isNewLine
         */
        function showError(field, error, isNewLine) {
            var _a;
            var id = field.id || field.name; // 获取字段 id 或者 name
            if (!id)
                return;
            field.classList.add('error'); // 将错误类添加到字段
            // 检查错误消息字段是否已经存在 如果没有, 就创建一个
            var message = field.form.querySelector('.error-message#error-for-' + id);
            if (!message) {
                message = document.createElement('div');
                message.className = 'error-message';
                message.id = 'error-for-' + id;
                (_a = field.parentNode) === null || _a === void 0 ? void 0 : _a.insertBefore(message, field.nextSibling);
            }
            field.setAttribute('aria-describedby', 'error-for-' + id); // 添加ARIA role 到字段
            message.innerHTML = error; // 更新错误信息
            if (!isNewLine) // 显示错误信息
                message.style.display = 'inline-block';
            message.style.visibility = 'visible';
        }
        ;
        /**
         * 移除所有的错误信息
         *
         * @param field
         */
        function removeError(field) {
            var id = field.id || field.name; // 获取字段的 id 或者 name
            if (!id)
                return;
            field.classList.remove('error'); // 删除字段的错误类
            field.removeAttribute('aria-describedby'); // 移除字段的 ARIA role
            var message = field.form.querySelector('.error-message#error-for-' + id + ''); // 检查 DOM 中是否有错误消息
            if (message) {
                message.innerHTML = ''; // 如果有错误消息就隐藏它
                message.style.display = 'none';
                message.style.visibility = 'hidden';
            }
        }
        /**
         *
         * @param el
         */
        function Validator(el) {
            var isMsgNewLine = el.dataset.msgNewline === "true";
            el.setAttribute('novalidate', 'true');
            // 监听所有的失去焦点的事件
            document.addEventListener('blur', function (event) {
                var el = event.target, errMsg = hasError(el);
                if (errMsg) { // 如果有错误,就把它显示出来
                    showError(el, errMsg, isMsgNewLine);
                    return;
                }
                removeError(el); // 否则, 移除所有存在的错误信息
            }, true);
        }
        form_1.Validator = Validator;
        Validator.hasError = hasError;
        /**
         * 是否通过验证
         *
         * @param form
         */
        Validator.onSubmit = function (form) {
            var fields = form.elements; // 获取所有的表单元素
            // 验证每一个字段
            // 将具有错误的第一个字段存储到变量中以便稍后我们将其默认获得焦点
            var error, hasErrors = null;
            for (var i = 0, j = fields.length; i < j; i++) {
                var el = fields[i];
                error = hasError(el);
                if (error) {
                    showError(el, error);
                    if (!hasErrors) // 如果有错误,停止提交表单并使出现错误的第一个元素获得焦点
                        hasErrors = el;
                }
            }
            if (hasErrors) {
                hasErrors.focus();
                return false;
            }
            return true;
        };
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));
