package com.ajaxjs.payment.alipay;


import com.ajaxjs.payment.alipay.model.AlipayConfig;
import com.ajaxjs.payment.alipay.model.GroupStringPair;
import com.ajaxjs.payment.alipay.model.StringPair;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

public abstract class AbstractAlipay {
    private static final LogHelper LOGGER = LogHelper.getLog(AbstractAlipay.class);

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    protected AlipayConfig config;

    private PrivateKey myPrivateKey;

    private PublicKey alipayPublicKey;

    protected boolean preferRSA;

    protected AbstractAlipay(AlipayConfig config) {
        this.config = config;
        myPrivateKey = initMyPrivateKey(config.getMyPrivateKey());
        alipayPublicKey = initAlipayPublicKey(config.getAlipayPublicKey());
        preferRSA = (myPrivateKey != null && alipayPublicKey != null);
    }

    private PublicKey initAlipayPublicKey(String key) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = DatatypeConverter.parseBase64Binary(key);

            return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    private PrivateKey initMyPrivateKey(String key) {
        try {
            byte[] keyBytes = DatatypeConverter.parseBase64Binary(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    protected String signMD5(final List<StringPair> p) {
        String param = join(p, false, false);
        String sign = md5Sign(param + config.getMd5key());
        LOGGER.info("Signing {}", param);

        return sign;
    }

    protected String signRSA(final List<StringPair> p) {
        String param = join(p, false, false);

        return rsaSign(param);
    }

    /**
     * Mobile SDK join the fields with quote, which is not documented at all.
     *
     * @param p
     * @return
     */
    protected String signRSAWithQuote(final List<StringPair> p) {
        String param = join(p, false, true);

        return rsaSign(param);
    }

    protected boolean verifyRSA(String sign, List<StringPair> p) {
        String param = join(p, false, false);
        LOGGER.info("verifyRSA sing={}", sign);
        LOGGER.info("verifyRSA content={}" + param);

        return rsaVerify(param, sign);
    }

    protected boolean verifyMD5(String sign, List<StringPair> p) {
        return sign.equals(signMD5(p));
    }

    protected String join(final List<StringPair> p, boolean encode, boolean quote) {
        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < p.size(); ++i) {
            if (i != 0)
                buff.append("&");

            buff.append(p.get(i).getFirst()).append("=");
            if (quote)
                buff.append("\"");

            if (encode) {
                try {
                    buff.append(URLEncoder.encode(p.get(i).getSecond(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else
                buff.append(p.get(i).getSecond());

            if (quote)
                buff.append("\"");
        }
        return buff.toString();
    }

    // MD5 digest
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

//    private static String hex(byte[] input) {
//        StringBuilder buf = new StringBuilder();
//
//        for (byte b : input) {
//            buf.append(DIGITS[(b >> 4) & 0x0f]);
//            buf.append(DIGITS[b & 0x0f]);
//        }
//
//        return buf.toString();
//    }

    private static String md5Sign(String input) {
        return DigestUtils.md5DigestAsHex(input.getBytes(CHARSET));
    }

    private String rsaSign(String content) {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(myPrivateKey);
            signature.update(content.getBytes(CHARSET));
            byte[] signed = signature.sign();

            return DatatypeConverter.printBase64Binary(signed);
        } catch (Exception e) {
            LOGGER.warning(e);
        }

        return null;
    }

    protected boolean rsaVerify(String content, String sign) {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(alipayPublicKey);
            signature.update(content.getBytes(CHARSET));

            return signature.verify(DatatypeConverter.parseBase64Binary(sign));
        } catch (Exception e) {
            LOGGER.warning(e);
        }

        return false;
    }

    protected String decrypt(String content) {
        LOGGER.info("decrypt content={}", content);

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, myPrivateKey);

            InputStream ins = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(content));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            // TODO change this value depends on length of key
            byte[] buf = new byte[256];
            int bufl;

            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;

                if (buf.length == bufl)
                    block = buf;
                else {
                    block = new byte[bufl];

                    for (int i = 0; i < bufl; i++)
                        block[i] = buf[i];
                }

                writer.write(cipher.doFinal(block));
            }

            return new String(writer.toByteArray(), CHARSET);
        } catch (Exception e) {
            LOGGER.warning(e);
        }

        return null;
    }

    public GroupStringPair parseQueryString(final String queryString) {
        return GroupStringPair.parseQueryString(queryString, "utf-8");
    }

    public GroupStringPair parsePostBody(final InputStream postBody) {
        return GroupStringPair.parsePostBody(postBody, "utf-8", "utf-8");
    }
}
