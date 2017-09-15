package com.example.xiaojun.gonganposji.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/9/14.
 */
@Entity
public class BaoCunBean {
    @Id
    @NotNull
    private Long id;
    private String cameraIP;
    private String zhuji;
    private String jiudianID;
    private String jiudianName;
    @Generated(hash = 101600409)
    public BaoCunBean(@NotNull Long id, String cameraIP, String zhuji,
            String jiudianID, String jiudianName) {
        this.id = id;
        this.cameraIP = cameraIP;
        this.zhuji = zhuji;
        this.jiudianID = jiudianID;
        this.jiudianName = jiudianName;
    }
    @Generated(hash = 1469853663)
    public BaoCunBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCameraIP() {
        return this.cameraIP;
    }
    public void setCameraIP(String cameraIP) {
        this.cameraIP = cameraIP;
    }
    public String getZhuji() {
        return this.zhuji;
    }
    public void setZhuji(String zhuji) {
        this.zhuji = zhuji;
    }
    public String getJiudianID() {
        return this.jiudianID;
    }
    public void setJiudianID(String jiudianID) {
        this.jiudianID = jiudianID;
    }
    public String getJiudianName() {
        return this.jiudianName;
    }
    public void setJiudianName(String jiudianName) {
        this.jiudianName = jiudianName;
    }

}
