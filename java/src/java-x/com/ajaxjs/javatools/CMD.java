package com.ajaxjs.javatools;

/*
* jShell.java
* class jShell is used for executing shell command
* USAGE:
* jShell obj=new jShell(shellCommand);
* obj.startErr();
* obj.startOut();
* obj.startIn();
* You can Interupt I/O thread when nessasary:
* obj.interruptErr();
* obj.interruptOut();
* obj.interruptIn();
*
* BY Ahui Wang Nankai U. 2007-05-12
*/

import java.io.*;

/**
 * JAVA 中执行Shell
 * 
 * @author http://hanqunfeng.iteye.com/blog/868186
 *
 */
public class CMD {
	Thread tIn; // handle input of child process
	Thread tOut;// handle output of child process
	Thread tErr;// handle error output of child process

	public static void main(String[] args) {
		CMD shell = new CMD("ls -l");
		shell.startErr();
		shell.startIn();
		shell.startOut();
	}

	public CMD(String shellCommand) {

		Process child = null; // child process
		try {
			child = Runtime.getRuntime().exec(shellCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (child == null) {
			return;
		}

		final InputStream inputStream = child.getInputStream();
		final BufferedReader brOut = new BufferedReader(new InputStreamReader(inputStream));

		tOut = new Thread() { // initialize thread tOut
			String line;
			int lineNumber = 0;

			public void run() {
				try {
					while ((line = brOut.readLine()) != null) {
						System.out.println(lineNumber + ". " + line);
						lineNumber++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		final InputStream errorStream = child.getErrorStream();
		final BufferedReader brErr = new BufferedReader(new InputStreamReader(errorStream));

		tErr = new Thread() { // initialize thread tErr
			String line;
			int lineNumber = 0;

			public void run() {
				try {
					while ((line = brErr.readLine()) != null) {
						System.out.println(lineNumber + ". " + line);
						lineNumber++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		// read buffer of parent process' input stream
		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		final OutputStream outputStream = child.getOutputStream();
		tIn = new Thread() {
			// String line;

			public void run() {
				try {
					while (true) {
						outputStream.write((reader.readLine() + "\n").getBytes());
						outputStream.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

	}

	public void startIn() { // start thread tIn
		if (tIn != null) {
			tIn.start();
		}
	}

	public void startErr() { // start thread tErr
		if (tErr != null) {
			tErr.start();
		}
	}

	public void startOut() { // start thread tOut
		if (tOut != null) {
			tOut.start();
		}
	}

	public void interruptIn() { // interrupt thread tIn
		if (tIn != null) {
			tIn.interrupt();
		}
	}

	public void interruptErr() { // interrupt thread tErr
		if (tErr != null) {
			tErr.interrupt();
		}
	}

	public void interruptOut() { // interrupt thread tOut
		if (tOut != null) {
			tOut.interrupt();
		}
	}

	// JAVA中执行bat
	public static void bat(String[] args) {
		try {
			Process process = Runtime.getRuntime().exec("test.bat");
			StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");

			// kick off stderr
			errorGobbler.start();

			StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
			// kick off stdout
			outGobbler.start();

			process.waitFor();
			System.out.println(process.exitValue());
		} catch (Exception e) {
		}
	}

	public static class StreamGobbler extends Thread {
		InputStream is;
		String type;
		OutputStream os;

		StreamGobbler(InputStream is, String type) {
			this(is, type, null);
		}

		StreamGobbler(InputStream is, String type, OutputStream redirect) {
			this.is = is;
			this.type = type;
			this.os = redirect;
		}

		public void run() {
			try {
				PrintWriter pw = null;
				if (os != null)
					pw = new PrintWriter(os);

				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					if (pw != null)
						pw.println(line);
					System.out.println(type + ">" + line);
				}
				if (pw != null)
					pw.flush();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}