/**
 * 错误的元素
 */
interface ErrorElement {
    /**
     * DOM 元素
     */
    el: HTMLFormControl;

    /**
     * 错误的讯息
     */
    msg: string;
}

/**
 * 通常是表单里面的 input 元素，但是 ts 没有对应的类型
 */
export interface HTMLFormControl extends HTMLElement {
    type: string;
    disabled: boolean;
    validity: ValidityState;
    name: string;
    value: string;
    form: HTMLFormElement;
}

if (!('validity' in document.createElement('input')))
    window.alert("不支持 HTML5 表单验证");

/**
 * 表单验证器
 */
export class Validator {
    constructor(el: HTMLFormElement) {
        // let isMsgNewLine: boolean = el.dataset.msgNewline === "true";
        el.setAttribute('novalidate', 'true');// 禁止浏览器原生的错误提示
        this.el = el;
        this.checkEveryField();
    }

    /**
     * 表单元素
     */
    private el: HTMLFormElement;

    private errorElements: ErrorElement[] = [];

    /**
     * 对每一个表单元素监听事件，一失去焦点就触发表单验证
     */
    private checkEveryField(): void {
        let arr: NodeListOf<HTMLInputElement> = this.el.querySelectorAll('input');
        arr.forEach((input: HTMLInputElement) => {
            input.addEventListener('blur', (ev: Event) => {
                let el: HTMLFormControl = <HTMLFormControl>ev.target;

                if (el.tagName == "A" || Validator.isIgnoreEl(el))// 忽略部分元素；a 元素也有 blur 事件，忽略之
                    return;

                let result: ErrorElement | null = Validator.check(el);

                if (result) { // 如果有错误,就把它显示出来
                    this.errorElements.push(result);
                    this.showError(result);
                } else
                    this.removeError(el); // 否则, 移除所有存在的错误信息
            }, true);
        });
    }

    /**
     * 
     * @param err 
     */
    private showError(err: ErrorElement): void {
        let el = err.el,
            id: string = el.id || el.name; // 获取字段 id 或者 name

        if (!id)
            return;

        err.el.classList.add('error'); // 将错误类添加到字段

        // 检查错误消息字段是否已经存在 如果没有, 就创建一个
        let message = err.el.form.querySelector(`.error-message#error-for-${id}`);
        if (!message) {
            message = document.createElement('div');
            message.className = 'error-message';
            message.id = 'error-for-' + id;
            el.parentNode?.insertBefore(message, el.nextSibling);
        }

        el.setAttribute('aria-describedby', 'error-for-' + id);// 添加 ARIA role 到字段
        (<HTMLElement>message).innerHTML = err.msg;// 更新错误信息

        // if (!isNewLine)// 显示错误信息
        //     message.style.display = 'inline-block';

        (<HTMLElement>message).classList.remove('hide');
    }

    /**
     * 移除所有的错误信息
     * 
     * @param el 
     */
    private removeError(el: HTMLFormControl): void {
        let id: string = el.id || el.name;// 获取字段的 id 或者 name
        if (!id)
            return;

        el.classList.remove('error');            // 删除字段的错误类
        el.removeAttribute('aria-describedby');  // 移除字段的 ARIA role

        let message: HTMLElement | null = <HTMLElement | null>el.form.querySelector(`.error-message#error-for-${id}`); // 检查 DOM 中是否有错误消息
        if (message) {
            message.innerHTML = ''; // 如果有错误消息就隐藏它
            message.classList.add('hide');
        }
    }

    /**
     * 是否忽略的表单元素
     * 
     * @param el 
     */
    private static isIgnoreEl(el: HTMLFormControl): boolean {
        return el.disabled || el.type === 'file' || el.type === 'reset' || el.type === 'submit' || el.type === 'button';
    }

    /**
     * 验证字段
     * 
     * @param field 表单字段元素
     * @returns 若验证通过返回 null，否则返回 ErrorElement
     */
    public static check(field: HTMLFormControl): ErrorElement | null {
        // if (!field || !field.getAttribute)
        //     console.log(field);

        let validity: ValidityState = field.validity;  // 获取 validity

        if (!validity)
            throw '浏览器不支持 HTML5 表单验证';

        if (validity.valid) // 通过验证
            return null;
        else {
            let result: ErrorElement = {
                el: field,
                msg: "无效输入" // 通用错误讯息 The value you entered for this field is invalid.
            };

            if (validity.valueMissing)      // 如果是必填字段但是字段为空时
                result.msg = '该项是必填项';

            if (validity.typeMismatch) {    // 如果类型不正确
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

            if (validity.stepMismatch)  // 如果数字值与步进间隔不匹配
                result.msg = '请选择一个有效值';

            if (validity.rangeOverflow)     // 如果数字字段的值大于 max 的值
                result.msg = `请选择小于 ${field.getAttribute('max')} 的值`;
            // return 'Please select a value that is no more than ' + field.getAttribute('max') + '.';

            if (validity.rangeUnderflow)
                result.msg = `请选择大于 ${field.getAttribute('min')} 的值`;
            // return 'Please select a value that is no less than ' + field.getAttribute('min') + '.';

            if (validity.patternMismatch)
                result.msg = field.getAttribute('title') || '格式要求不正确';

            return result;
        }
    }

    /**
     * 是否通过验证
     * 
     * @param form 
     */
    public static onSubmit(form: HTMLFormElement): boolean {
        let fields: HTMLFormControlsCollection = form.elements;// 获取所有的表单元素

        // 验证每一个字段
        // 将具有错误的第一个字段存储到变量中以便稍后我们将其默认获得焦点
        let error: ErrorElement | null, hasErrors: HTMLElement | null = null;

        for (let i = 0, j = fields.length; i < j; i++) {
            let el: HTMLFormControl = <HTMLFormControl>fields[i];
            error = this.check(el);

            if (error) {
                // showError(el);
                if (!hasErrors) // 如果有错误,停止提交表单并使出现错误的第一个元素获得焦点
                    hasErrors = <HTMLElement>el;
            }
        }

        if (hasErrors) {
            hasErrors.focus();
            return false;
        }

        return true;
    }
}


// ; (function (window, document, undefined) {  // 确保 ValidityState 全部被支持 (所有的功能)
//     var supported = function () {
//         var input = document.createElement('input');
//         return ('validity' in input && 'badInput' in input.validity && 'patternMismatch' in input.validity && 'rangeOverflow' in input.validity && 'rangeUnderflow' in input.validity && 'stepMismatch' in input.validity && 'tooLong' in input.validity && 'tooShort' in input.validity && 'typeMismatch' in input.validity && 'valid' in input.validity && 'valueMissing' in input.validity);
//     };

//     /**
//      *  Generate the field validity object
//      *  @param {Node]} field The field to validate
//      *  @return {Object} The validity object
//      **/
//     var getValidityState = function (field) {
//         // 变量
//         var type = field.getAttribute('type') || input.nodeName.toLowerCase();
//         var isNum = type === 'number' || type === 'range';
//         var length = field.value.length; var valid = true;
//         //检测支持性
//         var checkValidity = {
//             badInput: (isNum && length > 0 && !/[-+]?[0-9]/.test(field.value)),
//             // 数字字段的值不是数字
//             patternMismatch: (field.hasAttribute('pattern') && length > 0 && new RegExp(field.getAttribute('pattern')).test(field.value) === false),
//             // 输入的值不符合模式
//             rangeOverflow: (field.hasAttribute('max') && isNum && field.value > 1 && parseInt(field.value, 10) > parseInt(field.getAttribute('max'), 10)),
//             // 数字字段的值大于max属性值
//             rangeUnderflow: (field.hasAttribute('min') && isNum && field.value > 1 && parseInt(field.value, 10) < parseInt(field.getAttribute('min'), 10)),
//             // 数字字段的值小于min属性值
//             stepMismatch: (field.hasAttribute('step') && field.getAttribute('step') !== 'any' && isNum && Number(field.value) % parseFloat(field.getAttribute('step')) !== 0),
//             // 数字字段的值不符合 stepattribute
//             tooLong: (field.hasAttribute('maxLength') && field.getAttribute('maxLength') > 0 && length > parseInt(field.getAttribute('maxLength'), 10)),
//             // 用户在具有maxLength属性的字段中输入的值的长度大于属性值
//             tooShort: (field.hasAttribute('minLength') && field.getAttribute('minLength') > 0 && length > 0 && length < parseInt(field.getAttribute('minLength'), 10)),
//             // 用户在具有minLength属性的字段中输入的值的长度小于属性值
//             typeMismatch: (length > 0 && ((type === 'email' && !/^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test(field.value)) || (type === 'url' && !/^(?:(?:https?|HTTPS?|ftp|FTP):\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-zA-Z\u00a1-\uffff0-9]-*)*[a-zA-Z\u00a1-\uffff0-9]+)(?:\.(?:[a-zA-Z\u00a1-\uffff0-9]-*)*[a-zA-Z\u00a1-\uffff0-9]+)*)(?::\d{2,5})?(?:[\/?#]\S*)?$/.test(field.value)))),
//             // email 或者 URL 字段的值不是一个 email地址或者 URL
//             valueMissing: (field.hasAttribute('required') && (((type === 'checkbox' || type === 'radio') && !field.checked) || (type === 'select' && field.options[field.selectedIndex].value < 1) || (type !== 'checkbox' && type !== 'radio' && type !== 'select' && length < 1))) // 必填字段没有值
//         };

//         // 检查是否有错误
//         for (var key in checkValidity) {
//             if (checkValidity.hasOwnProperty(key)) {
//                 // If there's an error, change valid value
//                 if (checkValidity[key]) { valid = false; break; }
//             }
//         }
//         // 给 validity 对象添加 valid 属性
//         checkValidity.valid = valid;
//         // 返回对象
//         return checkValidity;
//     };

//     // 如果不支持完整的 ValidityState 功能，则可以使用polyfill
//     if (!supported()) {
//         Object.defineProperty(HTMLInputElement.prototype, 'validity', {
//             get: function ValidityState() {
//                 return getValidityState(this);
//             },
//             configurable: true,
//         });
//     }
// })(window, document);

