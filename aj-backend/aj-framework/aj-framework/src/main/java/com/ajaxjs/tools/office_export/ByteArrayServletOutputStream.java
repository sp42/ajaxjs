package com.ajaxjs.tools.office_export;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 自定义响应对象的输出流
 *
 * @author sp42 frank@ajaxjs.com
 */
public class ByteArrayServletOutputStream extends ServletOutputStream {
    /**
     * 创建一个 ByteArrayServletOutputStream 对象
     */
    public ByteArrayServletOutputStream() {
    }

    /**
     * 创建一个 ByteArrayServletOutputStream 对象
     *
     * @param out 输出流
     */
    public ByteArrayServletOutputStream(ByteArrayOutputStream out) {
        this.out = out;
    }

    /**
     * 输出流
     */
    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Override
    public void write(byte[] data, int offset, int length) {
        out.write(data, offset, length);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    public void writeTo(OutputStream _out) {
        try {
            out.writeTo(_out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream getOut() {
        return out;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public String toString() {
        return out.toString();
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }
}

