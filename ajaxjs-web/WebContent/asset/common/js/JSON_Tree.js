// JSON_Tree 一种直观的树结构
;(function(){
	JSON_Tree = function(data){
		this.data = data;
	};

	JSON_Tree.prototype = {
		search : function(handler){
			dataTypeDectect(this.data);
			array_Iterator(this.data, handler);
		},
		
		fastSearch : function(handler){
			var _node = null;
			
			var data = {children : this.data}; // 要求为 Object，不能为 Array
			;(function(node){
				if(handler(node) == false){
					return node;
				}
				if(node.children && node.children.length){
					for(var i = 0, j = node.children.length; i < j; i++){
						arguments.callee(node.children[i]);
					}
				}
			})(data);
			
			return _node;
		},
		// id 可能会重复，要在 uuid 下才可用这个方法
		fastSearchById : function(id){
			var _node = null;
			this.fastSearch(function(node){
				if((node.id && node.id == id) || (node.uid && node.uid == id)){
					_node = node;
					return false;
				}
			});

			return _node;
		},
		
		//  查找某个节点，并遍历全部的 子 节点，返回所有 id（包含当前节点）
		getAllChildrenIdByNode : function (node){
			var arr = [];
			
			(function(node){
				this.push(node);
				if(node.children && node.children.length){
					for(var i = 0, j = node.children.length; i < j; i++){
						arguments.callee.call(this, node.children[i]);
					}
				}
			}).call(arr, node);
			
			return arr;
		},
		
		toList : function(){
			var list = [];
			
			dataTypeDectect(this.data);
			array_Iterator(this.data, function(e, arr, i, j){
				list.push(e);
	        });
			
			return list;
		},
		
	    /**
	     * 获取第 N 级的数据，从 0 开始。
	     * @return {Object}
	     */
		levelAt : function(level){
			var result = [];
			
			dataTypeDectect(this.data);
			array_Iterator(this.data, function(e, arr, i, j){
				j == level && result.push(e);
	        });
			
			return result;
		},
		
		//最外层不是 array 的数据而是 object, 以方便检索
		builtData: null,
		
		/**
		 * 让节点包含 所有 父级 节点 的信息
		 * 除了构建路径 path 外，还会把子元素附加到父元素身上，以为 hash 查找之用
		 * @returns {}
		 */
		buildPath : function(){
			if(this.builtData){ // 如果已经 build 过无须重复 build
				return this.builtData;
			}else{
				var buildedPath = {}; 
				
				dataTypeDectect(this.data);
				array_Iterator(this.data, function(e, arr, i, j){
					if(!this.fullPath)this.fullPath = '';	// 执行此步的时候为根目录
					
					if(this.fullPath)
						e.fullPath = this.fullPath + '/' + e.id;// 包含父路径的全称路径
					else
						e.fullPath = e.id;
					
					e.sj = j; 								// 缩进
					
					this[e.id] = e;							// 当前结构是，直接附加在对象身上。考虑分配一个专用的 key，或者 搜索 children？
				}, buildedPath);
				
				this.builtData = buildedPath;
				return buildedPath;
			}
		},
		
		getNodeByFullPath : function(path){
			this.buildPath();
			var node = null;
			array_Iterator(this.data, function(_node){
				if(_node.fullPath == path){
					node = _node;
					return false;
				}
			});
			
			return node;
		},
		
		/**
		 * 找到父节点，也找到自己
		 * @param path
		 * @returns {Array} 
		 */
		getAllParentsByFullPath : function(path){
			var paths = args2list(path);
			if (paths == null)return null;
	        
			var result = [], i = 0, withPathData = this.buildPath();
//			println('df56546:s::' + withPathData.length);
	        /**
	         * @param {String} key
	         */
	        (function(key){
	            var _shift = this[key];
 
	            if(!_shift)return;
	            
	            result.push(_shift);
	            // 相当于 i++;var nextKey = path[i];arguments.callee.call(_shift, nextKey);
	            arguments.callee.call(_shift, paths[++i]);
	        }).call(withPathData, paths[i]);
	        
	        return result;
		}
		
	};
	
	// 返回数组。无论输入是 string, arguments,还是 array 本身（js 支持可变参数）
	function args2list(args){
		var arr = null;
		if(arguments.length > 1)     // 可变参数的条件
			arr = Array.prototype.slice.call(arguments, 0); // 把 aguments 变为真实的  array
        else if(typeof args == 'string'){  // 送入字符串的条件
        	arr = args.split('/');
//        	arr.shift();
        }else if(args.pop){                 // 送入数组的条件 do nothing...
        	arr = args;
        }
		
		return arr;
	}
	
	var childrenToken = 'children'; // 子元素所在数组的 key
	var undefinedStr = 'undefined';

	/**
	 * 最重要的函数
	 * 遍历，查找
	 * iterator
	 * 如果 fn 返回 false 则中止遍历
	 * @param {Array} arr
	 * @param {Object} scope 充分利用 JS 独有的 this 对象指针以确定函数作用域，收窄对象范围。
	 * @param {Number} i
	 * @param {Number} j A ghost value, 穿梭于
	 */
	function array_Iterator(arr, fn, scope, i, j){
	    if(typeof scope == undefinedStr)scope = this;
	    if(typeof i == undefinedStr)i = 0;
	    if(typeof j == undefinedStr)j = 0;

	    var element = arr[i], 
	    /*	该函数为递归函数，以上参数随递归变化而不同，而下面此部分的参数相对“静态”，故透过 arg.callee 获取引用 */
	    	selfFn = arguments.callee;

	    if(typeof element == undefinedStr)return;

	    // do something...
	    if(fn && fn.call(scope, element, arr, i, j) == false)return;
	    // do something-End
	    
	    var children = element[childrenToken];
	    if (children && children.length){
	        // -->y
	    	array_Iterator(children, fn, element/*已经变了这个Scope*/, 0, j + 1);// var x = j + 1; // 每一次循环 children 都会对j产生变化
	    }
	    
	    // -->x
	    array_Iterator(arr, fn, scope, ++i, j /* Do not give a "j" here, cause it's needed to have j reset!*/);    
	}
	
	function dataTypeDectect(data){
		if(!data)throw '未输入 JSON 数据！';
		if(!data instanceof Array) throw '输入 JSON，要求 Array， 不能 对象';
	}
})();

JSON_Tree.util = {
	// 扁平化 json
	flat : function(json){
		var map = {};
		
		(function(obj, key){
			for(var i in obj){
				var value = obj[i];
				var _key = key + (key == '' ? i : '_' + i);
				if(typeof value == 'string'){
					map[_key] = value;
					// println(_key + ':' + map[_key])
				}else{ // it's object
					arguments.callee(value, _key);
				}
			}
		})(json, '');
		
		return map;
	},
	makeSiteMap : function(data, appID){
//		var div	= '<div class="indentBlock indentBlock_{1}"><span class="dot">·</span>{0}</div>\n',
		var sitemap = '', z = 0, isSetted = false;
	    
	    var jsonTree = new JSON_Tree(data);
	    jsonTree.buildPath();
	    jsonTree.search(function (e, arr, i, j){ 
	    	if(e.isHidden_fromFrontEnd)return false; // 忽略后台节点
			
			var diver = e.isHashUrl ? '#' : '/';  // 包含父路径的全称路径
			
			// 数据库栏目 ，sectionId 不要影响后裔节点
			if(e.uid && !e.children){
				e.fullPath = this.fullPath;
			}else{
				e.fullPath = this.fullPath + diver + e.id; 
			}
			
			// 缩进
			var text = '<a href="{0}" class="indentBlock_{2}"><span class="dot">·</span>{1}</a>\n ';
			if(e.uid){
				text = text.format(appID + e.fullPath + '?sectionId=' + e.uid , e.name, j);
			}else{
				text = text.format(appID + e.fullPath, e.name, j);
			}
			
			// 划分为两栏
			z++;
//			if((z > small && z < big) && !isSetted && j == 0){
			
			if(isSetted && j == 0){
				text = '\n\t<\/td>\n\t<td>\n\t\t' + text;
			}	
			if(!isSetted)isSetted = true;
	        sitemap += text;
	    });
	    
		return '<table class="siteMap"><tr><td>\n\t' + sitemap + '\n\t<\/td><\/tr><\/table>';
	},
	
	//  查找某个节点，并遍历全部的 子 节点，返回所有 id（包含当前节点）
	getAllChildrenIdByNode : function(data, id){
		var tree = new JSON_Tree(data);
		var node = tree.fastSearchById(id);
		return tree.getAllChildrenIdByNode(node);
	},
	
	// 同上，但返回 id[]
	getAllChildrenIdByNode_as_idArr : function(){
		var arr = this.getAllChildrenIdByNode.apply(this, arguments);
		var _arr = [];
		for(var i = 0, j = arr.length; i < j ; i ++){
			_arr[i] = arr[i].id || arr[i].uid;
		}
		
		return _arr;
	}
};