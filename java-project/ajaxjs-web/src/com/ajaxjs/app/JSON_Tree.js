/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// import package java.* / com/* by default
//importPackage(Packages.ajaxjs);// Import our all packages.Beware of names conficts!

/**
 * JSON_Tree 一种直观的树结构
 */
;(function(){
	/**
	 * 
	 * @param {Array} data
	 * @returns
	 */
	JSON_Tree = function(data) {
		// 类型判断
		if (!data) throw 'JSONTree::未输入 JSON 数据！';
		if (!data instanceof Array) throw 'JSONTree::输入的 JSON，要求 Array 类型， 不能 对象';
		
		this.data = data;
	};

	JSON_Tree.prototype = {
			
		/**
		 * 查找所有节点
		 * @param {Function} handler 处理器函数。如果 handler 返回 false 则终止查找，返回 void。
		 */
		search : function(handler) {
			array_Iterator(this.data, handler);
		},
		
	    /**
	     * 把所有节点 push 到一个队列之中，相当于扁平化。
	     * @return {Array<Node>} 查找到的节点数组；如果没有找到，则返回空数组。
	     */
		toList : function() {
			var list = [];

			this.search(function(e, arr, i, j) {
				list.push(e);
			});
			
			return list;
		},
		
	    /**
	     * 获取第 N 级的数据，从 0 开始。
	     * @param {Number} level 第 N 级
	     * @return {Array<Node>} 查找到的节点数组；如果没有找到，则返回空数组。
	     */
		levelAt : function(level) {
			var result = [];
			
			this.search(function(e, arr, i, j) {
				j == level && result.push(e);
			});

			return result;
		},
		
		/**
		 * 根据 path 查找某个节点。
		 * @param {String} path 节点路径，以 / 分隔。
		 * @returns {Node}
		 */
		getNodeByFullPath : function(path) {
			this.buildPath();
			
			var node = null;
			this.search(function(_node) {
				if (_node.fullPath == path) {
					node = _node;
					return false;
				}
			});

			return node;
		},
		
		// 最外层不是 array 的数据而应该是 object, 以方便检索
		builtData : null,
		
		/**
		 * 让节点包含 所有 父级 节点 的信息
		 * 除了构建路径 path 外，还会把子元素附加到父元素身上，以为 hash 查找之用
		 * @returns {}
		 */
		buildPath : function() {
			if(this.builtData) { // 如果已经 build 过无须重复 build
				return this.builtData;
			}else{
				this.builtData = {}; 
				
				array_Iterator(this.data, function(e, arr, i, j) {
					if(!this.fullPath)this.fullPath = '';	// 执行此步的时候为根目录
					
					if(this.fullPath) e.fullPath = this.fullPath + '/' + e.id;// 包含父路径的全称路径
					else e.fullPath = e.id;
					
					e.sj = j; 								// 缩进
					
					this[e.id] = e;							// 当前结构是，直接附加在对象身上。考虑分配一个专用的 key，或者 搜索 children？
				}, this.builtData);
				
				return this.builtData;
			}
		},

		/**
		 * 找到父节点，也找到自己。常用于“页面锚点”。
		 * @param {String} path 节点路径，以 / 分隔。
		 * @returns {Array<Node>} 
		 */
		getAllParentsByFullPath : function(path) {
			var paths = args2list(path);
			if (paths == null) return null;
	        
			var result = [], i = 0, withPathData = this.buildPath();
//			println('::' + withPathData.length);
	
	        (function(key){
	            var _shift = this[key];
	            if(!_shift) return;
	            
	            result.push(_shift);
	            arguments.callee.call(_shift, paths[++i]); // 相当于 i++;var nextKey = path[i];arguments.callee.call(_shift, nextKey);
	        }).call(withPathData, paths[i]);
	        
	        return result;
		},
		
		/**
		 *  返回一个数组，该数组第一个元素是传入的某个节点，其余的则是传入节点的所有子节点。
		 *  @param {Object} node
		 *  @return {Array<Object>} 
		 */
		getAllChildrenIdByNode : function (node) {
			var arr = [];

			;(function(node) {
				this.push(node);
				if (node.children && node.children.length) {
					for (var i = 0, j = node.children.length; i < j; i++) {
						arguments.callee.call(this, node.children[i]);
					}
				}
			}).call(arr, node);
			
			return arr;
		},
		
		/**
		 * 查找各个节点。这是简单的版本，没有 search 其他臃肿的逻辑
		 * 
		 * @param {Function}
		 *            handler 处理器函数，该函数有 node 的参数。如果返回 false 则终止查找，返回该节点。
		 * @return {Object} 查找到的节点；如果没有找到，则返回 null。
		 */
		fastSearch : function(handler) {
			var _node = null;

			var data = { children : this.data }; // 要求为 Object，不能为 Array
			;(function(node) {
				if (handler(node) == false) return node;
				
				if (node.children && node.children.length) {
					for (var i = 0, j = node.children.length; i < j; i++) {
						arguments.callee(node.children[i]);
					}
				}
			})(data);

			return _node;
		},

		/**
		 *  根据 uid/id 查找节点。
		 *  id 可能会重复，所以推荐在 uuid 下才可用这个方法
		 *  @param {String/Number} id 
		 *  @return {Object} 查找到的节点；如果没有找到，则返回 null
		 */
		fastSearchById : function(id) {
			var _node = null;
			
			this.fastSearch(function(node) {
				if ((node.uid && node.uid == id) || (node.id && node.id == id)) {
					_node = node;
					return false;
				}
			});

			return _node;
		}
	};
	
	// 返回数组。无论输入是 string, arguments,还是 array 本身（js 支持可变参数）
	function args2list(args) {
		var arr = null;
		if(arguments.length > 1)     // 可变参数的条件
			arr = Array.prototype.slice.call(arguments, 0); // 把 aguments 变为真实的  array
        else if(typeof args == 'string') {  // 送入字符串的条件
        	arr = args.split('/');
//        	arr.shift();
        }else if(args.pop) {                 // 送入数组的条件 do nothing...
        	arr = args;
        }
		
		return arr;
	}
	
	var childrenToken = 'children'; // 子元素所在数组的 key
	var undefinedStr = 'undefined';

	/**
	 * 最重要的函数遍历，查找 iterator。如果 fn 返回 false 则中止遍历
	 * @param {Array} arr
	 * @param {Object} scope 充分利用 JS 独有的 this 对象指针以确定函数作用域，收窄对象范围。
	 * @param {Number} i
	 * @param {Number} j A ghost value, 穿梭于
	 */
	function array_Iterator(arr, fn, scope, i, j) {
	    if(typeof scope == undefinedStr)scope = this;
	    if(typeof i == undefinedStr)i = 0;
	    if(typeof j == undefinedStr)j = 0;

	    var element = arr[i], 
	    /*	该函数为递归函数，以上参数随递归变化而不同，而下面此部分的参数相对“静态”，故透过 arg.callee 获取引用 */
	    	selfFn = arguments.callee;

	    if(typeof element == undefinedStr )return;

	    // do something...
	    if(fn && fn.call(scope, element, arr, i, j) == false) return;
	    // do something-End
	    
	    var children = element[childrenToken];
	    if (children && children.length) {
	        // --> y
	    	array_Iterator(children, fn, element/*已经变了这个Scope*/, 0, j + 1);// var x = j + 1; // 每一次循环 children 都会对j产生变化
	    }
	    
	    // --> x
	    array_Iterator(arr, fn, scope, ++i, j /* Do not give a "j" here, cause it's needed to have j reset!*/);    
	}
	
})();

JSON_Tree.util = {
	// 扁平化 json
	flat : function(json) {
		var map = {};

		(function(obj, key) {
			for ( var i in obj) {
				var value = obj[i];
				var _key = key + (key == '' ? i : '_' + i);
				if (typeof value == 'string' || typeof value == 'boolean' || typeof value == 'number') {
					map[_key] = value;
					// println(_key + ':' + map[_key])
				} else { // it's object
					arguments.callee(value, _key);
				}
			}
		})(json, '');

		return map;
	},
	makeSiteMap : function(data, appID) {
		// var div = '<div class="indentBlock indentBlock_{1}"><span class="dot">·</span>{0}</div>\n',
		var sitemap = '', z = 0, isSetted = false;

		// var jsonTree = data instanceof JSON_Tree ? data : new JSON_Tree(data);
		data = JSON.stringify(data);// 会污染 对象，所以 new 一份
		data = JSON.parse(data);

		var jsonTree = new JSON_Tree(data);
		jsonTree.buildPath();
		jsonTree.search(function(e, arr, i, j) {
//			if (e.isHidden_fromFrontEnd) return false; // 忽略后台节点

			var diver = e.isHashUrl ? '#' : '/'; // 包含父路径的全称路径

			if (!this.fullPath)
				this.fullPath = ''; // 执行此步的时候为根目录

			// 数据库栏目 ，sectionId 不要影响后裔节点
			if (e.uid && !e.children) {
				e.fullPath = this.fullPath;
			} else {
				e.fullPath = this.fullPath + diver + e.id;
			}

			// 缩进
			var text = '<a href="{0}/" class="indentBlock_{2}"><span class="dot">·</span>{1}</a>\n ';
			if (e.uid) {
				text = text.format(appID + e.fullPath + '?sectionId=' + e.uid, e.name, j);
			} else {
				text = text.format(appID + e.fullPath, e.name, j);
			}

			// 划分为两栏
			z++;
			// if((z > small && z < big) && !isSetted && j == 0){

			if (isSetted && j == 0) {
				text = '\n\t<\/td>\n\t<td>\n\t\t' + text;
			}
			if (!isSetted)
				isSetted = true;
			sitemap += text;
		});

		return '<table class="siteMap"><tr><td>\n\t' + sitemap + '\n\t<\/td><\/tr><\/table>';
	},
	
	//  查找某个节点，并遍历全部的 子 节点，返回所有 id（包含当前节点）
	getAllChildrenIdByNode : function(data, id) {
		var tree = new JSON_Tree(data);
		var node = tree.fastSearchById(id); // 先定位该节点，然后返回其子节点
		return tree.getAllChildrenIdByNode(node);
	},
	

	// 同上，但返回 id[]
	getAllChildrenIdByNode_as_idArr : function() {
		var arr = this.getAllChildrenIdByNode.apply(this, arguments);
		var _arr = [];
		for (var i = 0, j = arr.length; i < j; i++) {
			_arr[i] = arr[i].id || arr[i].uid;
		}

		return _arr;
	}
};

bf.AppStru = (function() {
	function setNode(node) {
		node = String(node);
		node = node.replace('#', '/');// #--> /
		node = (function(node) {
			var arr = node.split('/'), last = arr.pop();
			
			if (last.indexOf('index.jsp') != -1) {// index.jsp 为本级节点所属，其他的为下一级
			} else {
				node = arr.join('/');
				// e.g product.jsp --> product
				node += '/';
				node += last.split('.').shift();
			}

			node = node.replace(/^\/|\/$/g, ''); // 一头一尾的 / 都去掉
			return node;
		})(node);

		return node;
	}

	function makeAnchor(arr, root) {
		var markup = [], tpl = '<a href="{0}/{fullPath}/">{name}</a> '.format(root);

		for (var i = 0, j = arr.length; i < j; i++) {
			markup.push(tpl.format(arr[i]));
		}

		return markup.join(' » ');
	}

	function apply(obj, config) {
		for ( var i in config) obj[i] = config[i];
	}
	
	return {
		init : function() {
			this.tree = new JSON_Tree(this.data);
		},
		getNav : function() {
			var arr = this.tree.levelAt(0);
			println(arr);
			println(JSON.stringify(arr));
			return arr;
		},

		/**
		 * 读取某个节点
		 * 
		 * @param {String} path 节点路径，以 / 分隔。
		 * @returns
		 */
		getNode : function(path) {
			path = setNode(path);
			if (path == '/') return {}; // 首页
			return this.tree.getNodeByFullPath(path);
		},

		getPageNode : function(path, root) {
			var node = setNode(path);
			if (node == '/') return {}; // 首页
			var arr = this.tree.getAllParentsByFullPath(node);

			if (arr == null || !arr.length) return null;
			var section = arr[0];// 获取二级栏目的
			
			return {
				sectionName : section.name,
				sectionNameLink : section.name.link(root + '/' + section.fullPath),// 获取节点的 A 连接：<a href="xxx">xxx</a>
				anchorLink : makeAnchor(arr, root),
				menu : arr[0].children,
				levels : section.sj
			};
		},
		getRouteMap : function() {
			this.routeMap = new JSON_Tree(bf.AdminStru);
			this.routeMap.buildPath();
			var arr = this.routeMap.getAllChildrenIdByNode({
				id : "",
				name : "根目录",
				children : this.routeMap.data
			});

			return arr;
		}
	}; 
})();