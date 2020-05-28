package com.clj.demo.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {
    //生成秘钥对
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();
        String publicKeyStr = byte2Base64(publicKeyBytes);
        String privateKeyStr = byte2Base64(privateKeyBytes);
        try {
            PrintWriter pub=new PrintWriter("C:\\Users\\76477\\IdeaProjects\\TextDemo\\UserRSApubK.txt");
            PrintWriter pri=new PrintWriter("C:\\Users\\76477\\IdeaProjects\\TextDemo\\UserRSApriK.txt");
            pub.write(publicKeyStr);
            pri.write(privateKeyStr);
            pub.flush();  pri.flush();
            pub.close();  pri.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return keyPair;
    }

    //文件读取
    public static String readFile(File file) throws Exception {
        StringBuffer buffer = new StringBuffer();
        BufferedReader br= new BufferedReader(new FileReader(file));
        String s = null;

        while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            buffer.append(s.trim());
        }
        br.close();
        return buffer.toString();
    }

    //获取公钥(Base64编码)
    public static String getPublicKey(KeyPair keyPair){
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2Base64(bytes);
    }

    public static String readPublicKey() throws Exception {
        File file = new File("C:\\Users\\76477\\IdeaProjects\\TextDemo\\UserRSApubK.txt");
        return readFile(file);
    }


    //获取私钥(Base64编码)
    public static String getPrivateKey(KeyPair keyPair){
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return byte2Base64(bytes);
    }

    public static String readPrivateKey() throws Exception {
        File file = new File("C:\\Users\\76477\\IdeaProjects\\TextDemo\\UserRSApriK.txt");
        return readFile(file);
    }

    //将Base64编码后的公钥转换成PublicKey对象
    public static PublicKey string2PublicKey(String pubStr) throws Exception{
        byte[] keyBytes = base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    //将Base64编码后的私钥转换成PrivateKey对象
    public static PrivateKey string2PrivateKey(String priStr) throws Exception{
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    //公钥加密
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    //私钥解密
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    //字节数组转Base64编码
    public static String byte2Base64(byte[] bytes){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    //Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) throws IOException{
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }

    public static String encryKey(String str, String publicKeyStr) throws Exception {
        //将Base64编码后的公钥转换成PublicKey对象
        PublicKey publicKey = RSAUtils.string2PublicKey(publicKeyStr);
        //用公钥加密
        byte[] publicEncrypt = RSAUtils.publicEncrypt(str.getBytes(), publicKey);
        //return publicEncrypt.toString();
        //加密后的内容Base64编码
        return RSAUtils.byte2Base64(publicEncrypt);
    }

    public static String decryKey(String enStr, String privateKeyStr) throws Exception {
        //将Base64编码后的私钥转换成PrivateKey对象
        PrivateKey privateKey = RSAUtils.string2PrivateKey(privateKeyStr);
        //加密后的内容Base64解码
        byte[] base642Byte = RSAUtils.base642Byte(enStr);
        //用私钥解密
        byte[] privateDecrypt = RSAUtils.privateDecrypt(base642Byte, privateKey);
        //解密后的明文
        return new String(privateDecrypt);
    }

    public static void main(String[] args){
        try {
            //===============生成公钥和私钥，公钥传给客户端，私钥服务端保留==================
            //生成RSA公钥和私钥，并Base64编码
            RSAUtils.getKeyPair();
            String publicKeyStr = RSAUtils.readPublicKey();
            String privateKeyStr = RSAUtils.readPrivateKey();
            //System.out.println("RSA公钥Base64编码:" + publicKeyStr);
            //System.out.println("RSA私钥Base64编码:" + privateKeyStr);

            //=================客户端=================
            //加密
            String message = "你好啊，我是曹琳婧!";
            String result = RSAUtils.encryKey(message,publicKeyStr);
            System.out.println("公钥加密并Base64编码的结果：" + result);

            //##############	网络上传输的内容有Base64编码后的公钥 和 Base64编码后的公钥加密的内容     #################

            //===================服务端================
            //解密
            System.out.println("解密后的明文: " + RSAUtils.decryKey(result,privateKeyStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}