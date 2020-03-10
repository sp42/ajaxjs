package com.ajaxjs.net.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.ajaxjs.util.Encode;

/**
 * 参考用
 * 
 * @author https://blog.csdn.net/axman/article/details/1487853
 *
 */
public class SendEmail {
	private Socket sc; // 一个发送会话的SOCKET连结
	private int PORT = 25; // SMTP端口
	private BufferedReader in; // SOCKET的输入流,用于接收命令响应
	private PrintWriter out; // SOCKET的输出流,用于发送命令
	private String smtpServer; // SMTP主机
	private boolean htmlStyle = false; // 是否用HTML格式发送
	private boolean authentication = false; // 服务器是否要求认证
	private String authorName = "guest"; // 用于认证的默认用户名
	private String authorPasswd = "guest"; // 用于认证的默认口令
	private String[] toArr; // 同时发送的目标地址数组
	private String[] ccArr; // 同时抄送的目标地址数组
	private String[] bccArr; // 同时暗送的目标地址数组
	private String from; // 发信人的地址
	private String charset = "gb2312"; // 默认的字符编码
	private int priority = 3; // 优先级

	// 以下对上面的属性提供存取方法
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public void setHtmlStyle(boolean htmlStyle) {
		this.htmlStyle = htmlStyle;
	}

	public void setAuthentication(boolean authentication) {
		this.authentication = authentication;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public void setAuthorPasswd(String authorPasswd) {
		this.authorPasswd = authorPasswd;
	}

	public void setToArr(String[] toArr) {
		this.toArr = toArr;
	}

	public void setCcArr(String[] ccArr) {
		this.ccArr = ccArr;
	}

	public void setBccArr(String[] bccArr) {
		this.bccArr = bccArr;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * 开始建立SOCKET,同时初始化输入输出,如果是应用程序本方法的功能应用在构造方法中完成
	 * 
	 * @return 是否成功
	 */
	public boolean createConnect() {
		try {
			sc = new Socket(smtpServer, PORT);
			in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			out = new PrintWriter(sc.getOutputStream());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public void close() {
		try {
			if (this.in != null)
				in.close();
		} catch (Exception e) {
		}
		try {
			if (this.out != null)
				out.close();
		} catch (Exception e) {
		}
		try {
			if (this.sc != null)
				sc.close();
		} catch (Exception e) {
		}
		// 分别放在try/c块中防止in/out的close()抛出异常而不能执行到sc.close();
	}

	/**
	 * 为了方便调试,在一次会话中一个命令发送应该有一个响应,所以把一个命令发送和响应过程封装到一个方法中
	 * 
	 * @param s 命令
	 * @return 响应内容
	 * @throws IOException
	 */
	public String doCommand(String s) throws IOException {
		if (s != null) {
			out.print(s);
			out.flush();
		}

		String line;

		if ((line = in.readLine()) != null) {
			return line;
		} else {
			return "";
		}
	}

	// 在发送MAIL实体前,认证和非认证的服务器发送命令不同,所以把发送实体前的会话封装到本方法中
	// 注意本方法返回boolean类型是调试成功后封装的,为了在send方法中调用方便,但在具体调试时,本方法
	// 应用返回String类型,也就是每次把do_command("AUTH LOGIN/r/n").indexOf("334")赋给line并把line
	// 返回出来以便能在错误时知道返回的错误码
	public boolean sendHeader() {
		try {
			@SuppressWarnings("unused")
			String line;
			doCommand(null);
			if (authentication) {
				// 如果是服务器要求认证,可能是有两种编码/加密方法,一是MD5,一是BASE64,目前很少用MD5认证的,所以本方法
				// 中用BASE64对明码用户名和口令编码, Encode.base64Encode是MailEncode的静态方法,在以下的介绍
				// 中会提供相应的编码和加密方法源程序

				authorName = Encode.base64Encode(authorName);
				authorPasswd = Encode.base64Encode(authorPasswd);
				if (-1 == doCommand("EHLO " + smtpServer + "/r/n").indexOf("250"))
					return false;
				while (true) {
					if (-1 != in.readLine().indexOf("250 "))
						break;
				}
				if (-1 == doCommand("AUTH LOGIN/r/n").indexOf("334"))
					return false;
				if (-1 == doCommand(authorName + "/r/n").indexOf("334"))
					return false;
				if (-1 == doCommand(authorPasswd + "/r/n").indexOf("235"))
					return false;
			} else {
				if (-1 == doCommand("HELO " + smtpServer + "/r/n").indexOf("250"))
					return false;
			}

			if (-1 == (line = doCommand("MAIL FROM: " + from + "/r/n")).indexOf("250"))
				return false;

			// 对于目标地址,发送,抄送和暗送,在发送过程中没有任何区别.区别只是在MAIL实体中它们的位置而在
			// SMTP会话中它们只以相同的RCPT TO命令发送,注意,有些服务器不允许一次连结发送给太多的地址.那么
			// 你应该限制toArr,ccArr,bccArr三个数组的总长度不超它们设定的最大值.当然如果你只有一个发送地址
			// 你就不必要在FOR回圈中处理,但本方法为了兼容同时发送给多人(而不是写在抄送中),用FOR回圈中来处理
			// 假你是一个目标地址,你应该生成一个元素的数组String[] toArr = {"aaa@aaa.com"};或者你可以重载本
			// 方法让to只是一个字符串

			if (toArr != null) {
				for (int i = 0; i < toArr.length; i++) {
					if (-1 == (line = doCommand("RCPT TO: " + toArr[i] + "/r/n")).indexOf("250"))
						return false;
				}
			} else
				return false;
			// 其实,从程序本身来说如果没有toArr只要有ccArr或bccArr还是可以发送的,但这样的信件没有目标地址却有抄送(暗送
			// 看不到)不合逻辑,在MAIL协议中一个重要原则是宽进严出,也就是我们接收别人的信格式可以放宽,他们发给我的只要符合
			// 协议我就应该接收和解析,而我发送出去的一定要非常严格地遵循标准,所以本处如果没有写发送就直接返回
			if (ccArr != null) {
				for (int i = 0; i < ccArr.length; i++) {
					if (-1 == (line = doCommand("RCPT TO: " + ccArr[i] + "/r/n")).indexOf("250"))
						return false;
				}
			}

			if (bccArr != null) {
				for (int i = 0; i < bccArr.length; i++) {
					if (-1 == (line = doCommand("RCPT TO: " + bccArr[i] + "/r/n")).indexOf("250"))
						return false;
				}
			}

			if (-1 == (line = doCommand("DATA/r/n")).indexOf("354"))
				return false;
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * 在发送MAIL实体时,为了处理方便和性能的原因,我把有附件和没有附件的方法分开来.BASE64是目前任何MUA都能处理的编码,本着宽进严出的原则我们严格使用BASE64编码
	 * 
	 * @param subject 主题
	 * @param message 邮件正文
	 * @return 是否发送成功
	 */
	public boolean send(String subject, String message) {
		subject = Encode.base64Encode(subject);
		subject = "=?GB2312?B?" + subject + "?=";
		// subject = HtmlUtil.replaceAll(subject, "/r/n", "?=/r/n/t=?GB2312?B?");
		// 按RFC标准进行折行自理,主题的换行必须以"?="结束,并以"空格=?字符集?数据编码"开始
		message = Encode.base64Encode(message);

		@SuppressWarnings("unused")
		String line;
		if (!sendHeader())
			return false;

		message = "MIME-Version: 1.0/r/n/r/n" + message;
		message = "Content-Transfer-Encoding: base64/r/n" + message;
		if (htmlStyle)
			message = "Content-Type: text/html;charset=\"" + charset + "\"/r/n" + message;
		else
			message = "Content-Type: text/plain;charset=\"" + charset + "\"/r/n" + message;

		message = "Subject: " + subject + "/r/n" + message;

		// 这儿是发送和抄送的列表,它只是在信体中的标记不同,暗送不必写,在和SMTP会话中直接RCPT过去
		String target = "";
		String ctarget = "";
		for (int i = 0; i < toArr.length; i++) {
			target += toArr[i];
			if (i < toArr.length - 1)
				target += ";";
		}

		if (ccArr != null) {
			for (int i = 0; i < ccArr.length; i++) {
				ctarget += ccArr[i];
				if (i < ccArr.length - 1)
					ctarget += ";";
			}
		}
		// 不能把bccArr加入
		message = "To: " + target + "/r/n" + message;
		if (ctarget.length() != 0)
			message = "Cc: " + ctarget + "/r/n" + message;
		message = "From: " + from + "/r/n" + message;
		out.print(message + "/r/n");

		try {
			if (-1 == (line = doCommand("/r/n./r/n")).indexOf("250"))
				return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// 下面是对有附件的发送,因为信体中的文本和附件要经过不同的处理,它们中间要加入各种分隔符和MIME类型,所以
	// 按顺序把每一行先写入临时文件中,最后一次取出来发送,其中把附件编码成字符串分行的方法会在以下介绍上给出
	public boolean send(String subject, String message, String[] att) {
		subject = Encode.base64Encode(subject);
		subject = "=?GB2312?B?" + subject + "?=";
		// subject = HtmlUtil.replaceAll(subject, "/r/n", "?=/r/n/t=?GB2312?B?");
		message = Encode.base64Encode(message);
		String target = "";
		String ctarget = "";
		for (int i = 0; i < toArr.length; i++) {
			target += toArr[i];
			if (i < toArr.length - 1)
				target += ";";
		}
		if (ccArr != null) {
			for (int i = 0; i < ccArr.length; i++) {
				ctarget += ccArr[i];
				if (i < ccArr.length - 1)
					ctarget += ";";
			}
		}
		try {
			File tmp = new File("tmp" + System.currentTimeMillis());
			PrintWriter outTmp = new PrintWriter(new FileWriter(tmp));
			outTmp.println("Message-Id: " + System.currentTimeMillis());
			outTmp.println("Date: " + new java.util.Date());
			outTmp.println("X-Priority: " + priority);
			outTmp.println("From: " + from);
			outTmp.println("To: " + target);
			if (ctarget.length() != 0)
				outTmp.println("Cc: " + ctarget);
			outTmp.println("Subject: " + subject);
			outTmp.println("MIME-Version: 1.0");
			String boundary = "------=_NextPart_" + System.currentTimeMillis();
			outTmp.println("Content-Type: multipart/mixed;boundary=\"" + boundary + "\"");
			outTmp.println("X-Mailer: Axman SendMail bate 1.0/r/n");
			outTmp.println("This is a MIME Encoded Message/r/n");

			outTmp.println("--" + boundary);
			if (htmlStyle)
				outTmp.println("Content-Type: text/html; charset=\"" + charset + "\"");
			else
				outTmp.println("Content-Type: text/plain; charset=\"" + charset + "\"");
			outTmp.println("Content-Transfer-Encoding: base64/r/n");
			outTmp.println(message);
			outTmp.println();
			if (att != null) {
				for (int i = 0; i < att.length; i++) {
					int kk = att[i].lastIndexOf("/");
					if (-1 == kk)
						kk = att[i].lastIndexOf("/");
					if (-1 == kk)
						kk = att[i].lastIndexOf("_");
					String name = att[i].substring(kk + 1);
					outTmp.println("--" + boundary);
					outTmp.println("Content-Type: application/octet-stream; name=\"" + name + "\"/r/n");
					outTmp.println("Content-Transfer-Encoding: base64/r/n");
					outTmp.println("Content-Disposition: attachment; filename=\"" + name + "\"/r/n");
					// Encode.base64EncodeFile(att[i], outTmp);
					outTmp.println();
				}
			}
			outTmp.println("--" + boundary + "--/r/n");
			outTmp.close();
			if (!sendHeader())
				return false;
			BufferedReader br = new BufferedReader(new FileReader(tmp));
			String line = null;

			while ((line = br.readLine()) != null)
				out.println(line);

			// 对于大的附件,一定要读一些输出一些,而不能象JavaMail那样build成一个part放在内存在.本处是先放统一编码
			// 到临时文件中然后从临时文件中读一行输出一行.所以仅仅是时间上开销而不会产生内存的大开销,相对于网络传
			// 输,本地文件读写可以忽略.
			br.close();
			if (-1 == doCommand("/r/n./r/n").indexOf("250"))
				return false;
			tmp.delete(); // 这一行的处理要周密考虑上面的return false前都应该先删除临时文件再返回,应该放在finally子句中
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		SendEmail sm = new SendEmail();
		sm.setSmtpServer("10.0.0.1");
		try {
			if (sm.createConnect()) {
				String[] to = { "axman@staff.coremsg.com" };
				String[] cc = { "stone@staff.coremsg.com" };
				String[] bcc = { "axman@staff.coremsg.com" };
				sm.setToArr(to);
				sm.setCcArr(cc);
				sm.setBccArr(bcc);
				sm.setFrom("axman@staff.coremsg.com");
				// sm.setAuthentication(true);
				// sm.setAuthorName("axman");
				// sm.setAuthorPasswd("11111");
				sm.setHtmlStyle(true);
				String subject = "中文测试！";
				String message = "大家好啊！";
				// String[] att = {"a.zip","b.zip"};
				System.out.print(sm.send(subject, message, null));
			} else {
				System.err.println("怎么连不上SMTP服务器啊？/r/n");

			}
		} finally {
			sm.close();
		}
	}
}