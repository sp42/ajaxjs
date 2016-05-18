$$.JSON = function(){} 
$$.JSON.prototype = {
	 toObject: function() {
		return eval("(" + arguments[0] + ')'); 
	}
	,toJSON: function(object) {
	    var type = typeof object;
	    if (type == 'object') {
	        if (Array == object.constructor)
	            type = 'array';
	        else if (object.getFullYear)
	            type = 'date';
	        else if (RegExp == object.constructor)
	            type = 'regexp';
	        else
	            type = 'object';
	           
	    }
	
	    switch (type) {
	        case 'undefined':
	        case 'unknown':
	        case 'function':
	        case 'regexp':
	            return;
	        break;
	        case 'boolean':
	        case 'number':
	            return object.toString();
	        break;
	        case 'date':
	        	 // @todo: user format()
	        	return object.getUTCFullYear() + 
	    			'-' + 
	    			this.makeTwoDigits(object.getUTCMonth() + 1) +
		            '-' +
		            this.makeTwoDigits(object.getUTCDate()) +
		            'T' +
		            this.makeTwoDigits(object.getUTCHours()) +
		            ':' +
		            this.makeTwoDigits(object.getUTCMinutes()) +
		            ':' +
		            this.makeTwoDigits(object.getUTCSeconds()) +
		            'Z';
	        break;
//                case 'number':
//                    return isFinite(object) ? object.toString() : 'null';
//                    break;
	        case 'string':
	            return '"' + object.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g,
			       function() {
			           var a = arguments[0];
			           return (a == '\n') ? '\\n' :
			                       (a == '\r') ? '\\r' :
			                       (a == '\t') ? '\\t' : ""
			       }) + '"';
	            break;
	        case 'object':
	            if (object === null) return 'null';
	            var results = [];
	            for (var property in object) {
	                var value = arguments.callee(object[property]);
	                if (value !== undefined)
	                    results.push(arguments.callee(property) + ':' + value);
	            }
	            return '{' + results.join(',') + '}';
	        break;
	        case 'array':
	            var results = [];
	            for (var i = 0; i < object.length; i++) {
	                var value = arguments.callee(object[i]);
	                if (value !== undefined) results.push(value);
	            }
	            return '[' + results.join(',') + ']';
	    }
	}
    ,makeTwoDigits: function (n){
        // Format integers to have at least two digits.
        return n < 10 ? '0' + n : n;
    }
};