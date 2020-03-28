package com.ajaxjs.config;

/**
 * 配置 JSON 的说明文件。
 * 开始的时候写成普通 json 文件通过文件读取，但跨多个项目的时候不能同步，要不断复制比较麻烦。
 * 于是干脆写成 java 统一分发。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface ConfigScheme {
	public final static String JSON = 
"{\r\n" + 
"	\"site\": {\r\n" + 
"		\"titlePrefix\": {\r\n" + 
"			\"name\": \"网站标题前缀\",\r\n" + 
"			\"tip\": \"放在 head title 标签中\",\r\n" + 
"			\"type\": \"string\",\r\n" + 
"			\"ui\": \"input_text\"\r\n" + 
"		},\r\n" + 
"		\"keywords\": {\r\n" + 
"			\"name\": \"搜索关键字\",\r\n" + 
"			\"tip\": \"用于 SEO 关键字优化，以便于搜索引擎机器人查找、分类。多个关键字之间用英文 , 逗号分开\",\r\n" + 
"			\"type\": \"string\",\r\n" + 
"			\"ui\": \"input_text\"\r\n" + 
"		},\r\n" + 
"		\"description\": {\r\n" + 
"			\"name\": \"网站描述\",\r\n" + 
"			\"tip\": \"网站的说明文本。通常是一段话介绍网站即可。\",\r\n" + 
"			\"type\": \"string\",\r\n" + 
"			\"ui\": \"textarea\"\r\n" + 
"		},\r\n" + 
"		\"footCopyright\": {\r\n" + 
"			\"name\": \"网站标题前缀\",\r\n" + 
"			\"tip\": \"放在 head title 标签中\",\r\n" + 
"			\"type\": \"string\",\r\n" + 
"			\"ui\": \"htmlEditor\"\r\n" + 
"		}\r\n" + 
"	},\r\n" + 
"	\"clientFullName\": {\r\n" + 
"		\"name\": \"网站标题前缀\",\r\n" + 
"		\"tip\": \"放在 head title 标签中\"\r\n" + 
"	},\r\n" + 
"	\"clientShortName\": {\r\n" + 
"		\"name\": \"网站标题前缀\",\r\n" + 
"		\"tip\": \"放在 head title 标签中\"\r\n" + 
"	},\r\n" + 
"	\"isDebug\": {\r\n" + 
"		\"name\": \"调试模式\",\r\n" + 
"		\"tip\": \"是否打开调试模式 true=调试中，false=生产环境，null=自动识别\",\r\n" + 
"		\"type\": \"boolean\",\r\n" + 
"		\"ui\": \"radio\",\r\n" + 
"		\"option\": [\r\n" + 
"			\"true=是\",\r\n" + 
"			\"false=否\",\r\n" + 
"			\"null=自动\"\r\n" + 
"		]\r\n" + 
"	},\r\n" + 
"	\"data\": {\r\n" + 
"		\"articleCatalog_Id\": {\r\n" + 
"			\"name\": \"新闻分类父 id\",\r\n" + 
"			\"type\": \"string\"\r\n" + 
"		},\r\n" + 
"		\"jobCatalog_Id\": {\r\n" + 
"			\"name\": \"招聘分类父 id\",\r\n" + 
"			\"type\": \"number\"\r\n" + 
"		},\r\n" + 
"		\"database_node\": {\r\n" + 
"			\"name\": \"调试模式\",\r\n" + 
"			\"tip\": \"是否打开调试模式 true=调试中，false=生产环境，null=自动识别\",\r\n" + 
"			\"type\": \"string\",\r\n" + 
"			\"ui\": \"radio\",\r\n" + 
"			\"option\": [\r\n" + 
"				\"true=是\",\r\n" + 
"				\"false=否\",\r\n" + 
"				\"null=自动\"\r\n" + 
"			]\r\n" + 
"		}\r\n" + 
"	},\r\n" + 
"	\"domain\": {\r\n" + 
"		\"article\": {\r\n" + 
"			\"share\": {\r\n" + 
"				\"name\": \"显示分享组件\",\r\n" + 
"				\"tip\": \"在文章详情页面中显示分享组件\",\r\n" + 
"				\"ui\": \"radio\",\r\n" + 
"				\"type\": \"boolean\"\r\n" + 
"			},\r\n" + 
"			\"neighborRecord\": {\r\n" + 
"				\"name\": \"显示相邻记录\",\r\n" + 
"				\"tip\": \"在文章详情页面中显示相邻记录\",\r\n" + 
"				\"ui\": \"radio\",\r\n" + 
"				\"type\": \"boolean\"\r\n" + 
"			},\r\n" + 
"			\"comment\": {\r\n" + 
"				\"name\": \"显示评论组件\",\r\n" + 
"				\"tip\": \"在文章详情页面中显示评论组件\",\r\n" + 
"				\"ui\": \"radio\",\r\n" + 
"				\"type\": \"boolean\"\r\n" + 
"			},\r\n" + 
"			\"pageHelper\": {\r\n" + 
"				\"name\": \"显示页面工具\",\r\n" + 
"				\"tip\": \"在文章详情页面中显示页面工具组件\",\r\n" + 
"				\"ui\": \"radio\",\r\n" + 
"				\"type\": \"boolean\"\r\n" + 
"			},\r\n" + 
"			\"attachmentDownload\": {" + 
"				\"name\": \"附件下载\"," + 
"				\"tip\": \"是否开放图文的附件下载在详情页\"," + 
"				\"ui\": \"radio\"," + 
"				\"type\": \"boolean\"" + 
"			}" + 
"		},\r\n" + 
"		\"shop\": {\r\n" + 
"		\r\n" + 
"		}\r\n" + 
"	},\r\n" + 
"	\"entity\": {\r\n" + 
"		\"physicalDeletion\" : {\r\n" + 
"			\"name\": \"是否物理删除实体\",\r\n" + 
"			\"tip\": \"是=执行数据库 DELETE 删除命令；否=标记字段为已删除\",\r\n" + 
"			\"ui\": \"radio\",\r\n" + 
"			\"type\": \"boolean\"\r\n" + 
"		},\r\n" + 
"		\"exportXslPage\": {\r\n" + 
"			\"name\": \"导出 Excel 时\",\r\n" + 
"			\"tip\": \"当导出列表数据为 xsl 时，选择哪种读取方式\",\r\n" + 
"			\"ui\": \"select\",\r\n" + 
"			\"option\": [\r\n" + 
"				\"1=导出当前页的数据\",\r\n" + 
"				\"2=导出所有页的数据\"\r\n" + 
"			]\r\n" + 
"		}\r\n" + 
"	},\r\n" + 
"	\"uploadFile\": {\r\n" + 
"		\"MaxTotalFileSize\": {\r\n" + 
"			\"name\": \"最大文件\",\r\n" + 
"			\"type\": \"number\"\r\n" + 
"		},\r\n" + 
"		\"MaxSingleFileSize\": {\r\n" + 
"			\"name\": \"多个文件总容量限制\",\r\n" + 
"			\"type\": \"number\"\r\n" + 
"		},\r\n" + 
"		\"isFileOverwrite\": {\r\n" + 
"			\"name\": \"是否覆盖文件\",\r\n" + 
"			\"tip\": \"遇到同名是否覆盖\",\r\n" + 
"			\"ui\": \"radio\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"option\": [\r\n" + 
"				\"true=是\",\r\n" + 
"				\"false=否\"\r\n" + 
"			]\r\n" + 
"		},\r\n" + 
"		\"isAutoNewFileName\": {\r\n" + 
"			\"name\": \"是否重名\",\r\n" + 
"			\"tip\": \"上传文件后是否重新自动命名。如果 false 可能遇到重名\",\r\n" + 
"			\"ui\": \"radio\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"option\": [\r\n" + 
"				\"true=是\",\r\n" + 
"				\"false=否\"\r\n" + 
"			]\r\n" + 
"		},\r\n" + 
"		\"saveFolder\": {\r\n" + 
"			\"isUsingRelativePath\": {\r\n" + 
"				\"name\": \"是否使用相对路径\",\r\n" + 
"				\"tip\": \"如果是则 SaveFolder 项无效，使用 relativePath 得到的绝对路径来保存\",\r\n" + 
"				\"ui\": \"radio\",\r\n" + 
"				\"type\": \"boolean\",\r\n" + 
"				\"option\": [\r\n" + 
"					\"true=是\",\r\n" + 
"					\"false=否\"\r\n" + 
"				]\r\n" + 
"			},\r\n" + 
"			\"absolutePath\": {\r\n" + 
"				\"name\": \"绝对目录\",\r\n" + 
"				\"tip\": \"上传图片的保存目录，磁盘路径\",\r\n" + 
"				\"ui\": \"select\",\r\n" + 
"				\"option\": [\r\n" + 
"					\"//temp//=本地测试 for Mac：//temp//\",\r\n" + 
"					\"C://temp//=本地测试 for Win：C://temp//\",\r\n" + 
"					\"//dczx//=正式服务器 //sds//\"\r\n" + 
"				]\r\n" + 
"			},\r\n" + 
"			\"relativePath\": {\r\n" + 
"				\"name\": \"相对目录\",\r\n" + 
"				\"tip\": \"外界通过 Web 可以访问的路径\"\r\n" + 
"			}\r\n" + 
"		}\r\n" + 
"	},\r\n" + 
"	\"user\": {\r\n" + 
"		\"login\": {\r\n" + 
"			\"loginType\": {\r\n" + 
"				\"name\": \"开放登录类型\",\r\n" + 
"				\"tip\": \"开放登录类型\",\r\n" + 
"				\"ui\": \"checkbox\",\r\n" + 
"				\"option\": [\r\n" + 
"					\"1=口令\",\r\n" + 
"					\"2=三方\",\r\n" + 
"					\"4=Token\"\r\n" + 
"				]\r\n" + 
"			},\r\n" + 
"			\"passWordLoginType\": {\r\n" + 
"				\"name\": \"用户id类型\",\r\n" + 
"				\"tip\": \"口令登录下，支持何种用户id？\",\r\n" + 
"				\"ui\": \"checkbox\",\r\n" + 
"				\"option\": [\r\n" + 
"					\"1=用户名\",\r\n" + 
"					\"2=邮件\",\r\n" + 
"					\"4=手机\"\r\n" + 
"				]\r\n" + 
"			}\r\n" + 
"		},\r\n" + 
"		\"avatar\": {\r\n" + 
"			\"maxWidth\": {\r\n" + 
"				\"name\": \"头像最大宽度\"\r\n" + 
"			},\r\n" + 
"			\"maxHeight\": {\r\n" + 
"				\"name\": \"头像最大高度\"\r\n" + 
"			},\r\n" + 
"			\"maxSize\": {\r\n" + 
"				\"name\": \"头像文件最大大小\",\r\n" + 
"				\"tip\": \"单位：字节\"\r\n" + 
"			}\r\n" + 
"		},\r\n" + 
"		\"resetPassword\": {\r\n" + 
"			\"mailBody\": {\r\n" + 
"				\"name\": \"邮件正文模版\",\r\n" + 
"				\"tip\": \"找回邮件的邮件\",\r\n" + 
"				\"ui\": \"htmlEditor\"\r\n" + 
"			}\r\n" + 
"		}\r\n" + 
"	},\r\n" + 
"	\"mailServer\": {\r\n" + 
"		\"server\": {\r\n" + 
"			\"name\": \"SMTP 服务器地址\",\r\n" + 
"			\"tip\": \"发送邮件的 SMTP 服务器\"\r\n" + 
"		},\r\n" + 
"		\"user\": {\r\n" + 
"			\"name\": \"用户名\",\r\n" + 
"			\"tip\": \"发送邮件的用户名\"\r\n" + 
"		},\r\n" + 
"		\"password\": {\r\n" + 
"			\"name\": \"密码\",\r\n" + 
"			\"tip\": \"发送邮件的密码\"\r\n" + 
"		}\r\n" + 
"	},\r\n" + 
"	\"Symmetric\": {\r\n" + 
"		\"AES_Key\": {\r\n" + 
"			\"name\": \"AES 密钥\",\r\n" + 
"			\"tip\": \"请注意保管，不能外泄\"\r\n" + 
"		},\r\n" + 
"		\"apiTimeout\": {\r\n" + 
"			\"name\": \"API 请求超时\",\r\n" + 
"			\"type\": \"number\",\r\n" + 
"			\"tip\": \"超时视为非法请求，单位毫秒\"\r\n" + 
"		}\r\n" + 
"	},\r\n" + 
"	\"security\": {\r\n" + 
"		\"isEnableSecurityIO\": {\r\n" + 
"			\"name\": \"是否使用安全请求响应\",\r\n" + 
"			\"tip\": \"true 表示为使用 SecurityRequest、SecurityResponse\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"ui\": \"radio\"\r\n" + 
"		},\r\n" + 
"		\"isXXS_Filter\": {\r\n" + 
"			\"name\": \"是否 XXS 过滤\",\r\n" + 
"			\"tip\": \"true 表示为跨站脚本攻击检测\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"ui\": \"radio\"\r\n" + 
"		},\r\n" + 
"		\"isCRLF_Filter\": {\r\n" + 
"			\"name\": \"是否 CRLF 过滤\",\r\n" + 
"			\"tip\": \"true 表示为使用响应头拆分过滤\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"ui\": \"radio\"\r\n" + 
"		},\r\n" + 
"		\"isRefererCheck\": {\r\n" + 
"			\"name\": \"是否来路检测\",\r\n" + 
"			\"tip\": \"true 表示为使用来路检测（Referer 字段）\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"ui\": \"radio\"\r\n" + 
"		},\r\n" + 
"		\"isWhiteList\": {\r\n" + 
"			\"name\": \"是否启用白名单\",\r\n" + 
"			\"tip\": \"true 表示为启用白名单\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"ui\": \"radio\"\r\n" + 
"		},\r\n" + 
"		\"isBlackList\": {\r\n" + 
"			\"name\": \"是否启用黑名单\",\r\n" + 
"			\"tip\": \"true 表示为启用黑名单\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"ui\": \"radio\"\r\n" + 
"		},\r\n" + 
"		\"isCookiesSizeCheck\": {\r\n" + 
"			\"name\": \"是否 Cookies 大小检查\",\r\n" + 
"			\"tip\": \"超出 Cookie 允许容量，默认 4KB\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"ui\": \"radio\"\r\n" + 
"		}\r\n" + 
"	},\r\n" + 
"	\"forDelevelopers\": {\r\n" + 
"		\"logAsFile\": {\r\n" + 
"			\"name\": \"是否保存 log 到文件\",\r\n" + 
"			\"tip\": \"通过 FileHandler 将 warning 级别以上的日志保存到磁盘文件\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"ui\": \"radio\"\r\n" + 
"		},\r\n" + 
"		\"logAsFileFolder\": {\r\n" + 
"			\"name\": \"log 保存目录\",\r\n" + 
"			\"tip\": \"可输入 win/linux 绝对目录，须注意斜杠转义\",\r\n" + 
"			\"placeholder\": \"若不设置则为默认值（classpath/LogHelper）\",\r\n" + 
"			\"size\": 50\r\n" + 
"		},\r\n" + 
"		\"enableWebSocketLogOutput\": {\r\n" + 
"			\"name\": \"实时浏览日志\",\r\n" + 
"			\"tip\": \"通过浏览器 WebSocket 技术查看服务端 Tomcat 输出日志\",\r\n" + 
"			\"type\": \"boolean\",\r\n" + 
"			\"ui\": \"radio\"\r\n" + 
"		}\r\n" + 
"	}\r\n" + 
"}";
}
