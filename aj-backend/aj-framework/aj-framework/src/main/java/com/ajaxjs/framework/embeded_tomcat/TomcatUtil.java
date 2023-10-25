package com.ajaxjs.framework.embeded_tomcat;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * 可以通过Socket关闭tomcat： telnet 127.0.0.1 8005，输入SHUTDOWN字符串
 */
public class TomcatUtil {
    public static void shutdown() {
        shutdown("localhost", 8005);
    }

    public static void shutdown(String serverHost, Integer serverPort) {
        System.out.println("localIP: " + Objects.requireNonNull(getSpecialHostAddress(serverHost)).getHostAddress());
        send("SHUTDOWN", serverHost, serverPort);
    }

    private static InetAddress getSpecialHostAddress(String hostName) {
        try {
            return InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static String send(String msg, String host, int port) {
        try (Socket socket = new Socket(host, port);
             BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
            out.write(msg.getBytes());
            out.flush();

            socket.shutdownOutput();
            String ackMsg = socketRead(socket);
            socket.shutdownInput();
            System.out.println("[" + System.currentTimeMillis() + "] Reply from server " + host + ":" + port + ": ");
            System.out.println("\t" + ackMsg);

            return ackMsg;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String socketRead(Socket socket) throws IOException {
        socket.setSoTimeout(5000);

        int byteCount = 0;
        char[] buffer = new char[4096];
        int bytesRead;

        try (InputStreamReader in = new InputStreamReader(socket.getInputStream()); StringWriter out = new StringWriter()) {
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
//            out.flush();
            return out.toString();
        }
    }

    public static File createTempDir(String folderName) {
        File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        tmpdir = new File(tmpdir, folderName);

        if (!tmpdir.exists())
            tmpdir.mkdir();

        return tmpdir;
    }

    public static File createTempDir(String prefix, int port) {
        File tempDir;

        try {
            tempDir = File.createTempFile(prefix + ".", "." + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tempDir.delete();
        tempDir.mkdir();
        tempDir.deleteOnExit();

        return tempDir;
    }

    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;

        try {
            if (path.isFile()) {
                java.nio.file.Files.delete(path.toPath());
                return;
            }

            File[] files = path.listFiles();
            assert files != null;

            for (File file : files) deleteAllFilesOfDir(file);
            java.nio.file.Files.delete(path.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
