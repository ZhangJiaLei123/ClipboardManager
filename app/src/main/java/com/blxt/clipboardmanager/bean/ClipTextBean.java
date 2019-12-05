package com.blxt.clipboardmanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ClipTextBean {

    @Id(autoincrement = true)
    Long id;
    @NotNull
    String idS;
    @NotNull
    String content;
    @NotNull
    String time;

    public ClipTextBean(String content){
        this.content = content;
        this.idS = md5Decode32(content);
        Calendar calendar = Calendar.getInstance();
        time = calendar.getTimeInMillis() + "";
    }

    public ClipTextBean(String idS, String content){
        this.idS = idS;
        this.content = content;
    }

    @Generated(hash = 972622379)
    public ClipTextBean(Long id, @NotNull String idS, @NotNull String content,
            @NotNull String time) {
        this.id = id;
        this.idS = idS;
        this.content = content;
        this.time = time;
    }

    @Generated(hash = 33591409)
    public ClipTextBean() {
    }

    public boolean isEmpty(){
        if(content == null){
            return true;
        }
        if(content.isEmpty()){
            return true;
        }
        if(content.length() == 0){
            return true;
        }
        return false;
    }

    /**
     * 32位MD5加密
     * @param content -- 待加密内容
     * @return
     */
    public String md5Decode32(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdS() {
        return this.idS;
    }

    public void setIdS(String idS) {
        this.idS = idS;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
