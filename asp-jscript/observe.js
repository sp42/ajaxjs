/**
* 观察者类，用于推送消息或者广播事件。addEventListener 方法为对象新增消息类型，fire 方法进行消息推送。
* 使用该类后，增加一个 msgs 属性，结构如下：
* msg = Object : [ 
* 	{
* 	   name : String,
*      bodies : Object [
* 			{
* 				scope	: Mixed,
* 				fn		: Function,
* 				args	: Mixed []
* 			}
* 	   ]
*  }
* ]
*/
define(function(require, exports, module) {
    var $$ = require('common/lang');
    module.exports = $$.event = $$.define(Object, function (){
    	var msg = {
            name : '',
    		bodies : [{
    		    scope : null,
    			fn : null,
    			agrs : []
    		}], /* ... */
    		isCustomDomEvent : false
        }
        function observe(){
        	// I'm Event
        	this.event = true;
        }
        this.init = observe;

        /**
        * @param {String}		msgName		消息名称
        * @param {Function}	    msgHandler	消息行为
        * @param {*}		    scope		函数作用域
        * @param {*|Array}	    args		消息行为的前期参数
        * @return {Boolean}
        */
        this.addEventListener = this.on = function (msgName, msgHandler, scope, args) {
            if (!this.msgs) {
                this.msgs = [];// 全部消息之容器
            }

            check_msgBody_Queen(this.msgs);
            var msgObj;

            var hasToken = false;
            for (var i = 0, j = this.msgs.length; i < j; i++) {
                msgObj = this.msgs[i];
                if (msgObj.name == msgName) {
                    hasToken = true;
                    break; // 找到已存在的消息队列
                }
            }
            if (hasToken == false) {
                // 消息结构体 @todo remove create()
                msgObj = Object.create(msg); // 新建消息对象
                msgObj.name = msgName;
                msgObj.bodies = [];         // you must new it!
                if (msgName)
                    msgObj.isCustomDomEvent = this.customDomEventNames
                                             ? (indexOf(msgName, this.customDomEventNames) != -1)
                                             : false;
            }

            check_msgBody_Queen(msgObj.bodies);

            if (!(typeof args instanceof Array)) { // 前期输入的参数
                if (args == undefined) {
                    args = [];
                } else {
                    args = [args];
                }
            }

            msgObj.bodies.push({        // 压入消息队列
                fn: msgHandler,
    			scope: scope || this,  // 保存函数作用域
    			args: args
            });

            if (hasToken == false) {
                this.msgs.push(msgObj);
            }

            return true;
        }

        /**
        * 检查消息体队列其类型
        * @param {Array} arr
        */
        function check_msgBody_Queen(arr) {
            if (!arr || !arr instanceof Array) {
                throw new TypeError();
            }
        }

        function indexOf(item, arr) {
            for (var i = 0, j = arr.length; i < j; i++) {
                if (item == arr[i]) {
                    return i;
                }
            }

            return -1;
        }

        /**
        * 根据 msgName，查找指定的消息并讲其分发。
        * @param {String} msgName
        * @param {Array/Mixed} args 可选
        * @param {Mixed} scope 可选
        */
        this.fire = function (msgName, args, scope) {
        	var foundMsgObj = false;
            for (var msgObj, i = 0, j = this.msgs.length; i < j; i++) {
                msgObj = this.msgs[i];
                if (msgObj.name == msgName) {
                	foundMsgObj = true;
                    break;
                }
            }

            if (foundMsgObj === false) {
            	debugger;
                throw '没有此消息类型' + msgName;
            }


            var _fn, _body, _args;
            for (var body, i = 0, j = msgObj.bodies.length; i < j; i++) {
                body = msgObj.bodies[i];

                _fn = body.fn;
                _scope = scope || body.scope;
                if(args == undefined){
                	_args = body.args;
                }else{
                	// args 应该在数组前面，否则数组顺序会颠倒
                	if (args instanceof Array){
                		_args = args.concat(body.args);
                	}else{
                		_args = [args].concat(body.args);
                	}
                }
                
                _fn.apply(_scope, _args);
            }
        }
        // event-->msg
        this.customDomEvent = function (el, eventType, msgName) {
            if (!this.customDomEventNames) {
                this.customDomEventNames = [];
            }
            this.customDomEventNames.push(msgName);
            el.addEventListener(eventType, (function (e) {
                e = e || window.event;
                this.instance.fire(this.msgName, e, this.instance);
            }).bind({
                instance: this,
                msgName: msgName
            }));
        }
    });

    /**
     * 占位用的空函数。
     * @return {[type]} [description]
     */
    module.exports.emptyHandler = function(){}
});