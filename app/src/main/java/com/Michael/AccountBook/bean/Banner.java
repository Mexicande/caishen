package com.Michael.AccountBook.bean;

import java.io.Serializable;

/**
 * Created by apple on 2018/5/18.
 */

public class Banner implements Serializable{

    /**
     * advername : 块钱
     * pictrue : http://or2eh71ll.bkt.clouddn.com/149760896587611.jpg?e=1497612565&token=Npg7Sanmf4z8uv3mvwwffjOvoCMYN8Ezm4T8pDrC:JtsY_HbNvx0i61EQBskQOZRAhsU=
     * app : https://www.99bill.com/seashell/html/activity/161125_kyhcach_mm/default.html?datasrc=link106
     */

    private String advername;
    private String pictrue;
    private String app;

    public String getAdvername() {
        return advername;
    }

    public void setAdvername(String advername) {
        this.advername = advername;
    }

    public String getPictrue() {
        return pictrue;
    }

    public void setPictrue(String pictrue) {
        this.pictrue = pictrue;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
