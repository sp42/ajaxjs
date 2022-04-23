Function.prototype.delegate = function(_args) {
	var self = this, scope = this.scope, args = arguments, aLength = arguments.length, fnToken = 'function';

	return function() {
		var bLength = arguments.length, Length = (aLength > bLength) ? aLength : bLength;

		// mission one:
		for (var i = 0; i < Length; i++)
			if (arguments[i])
				args[i] = arguments[i]; // 拷贝参数

		args.length = Length; // 在 MS JScript 下面，arguments 作为数字来使用还是有问题，就是 length 不能自动更新。修正如左:

		// mission two:
		for (var i = 0, j = args.length; i < j; i++) {
			var _arg = args[i];
			if (_arg && typeof _arg == fnToken && _arg.late == true)
				args[i] = _arg.apply(scope || this, args);
		}

		return self.apply(scope || this, args);
	};
}