package com.example.apple.easyspend.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author apple
 * @date 2018/5/18
 *
 */

public class Product implements Serializable {

    /**
     * id : 1
     * p_name : 米米米
     * url : https://www.baidu.com
     * p_logo : http://or2eh71ll.bkt.clouddn.com/150131715167829.png?e=1501320751&token=Npg7Sanmf4z8uv3mvwwffjOvoCMYN8Ezm4T8pDrC:DcwqOuHKq8MTwaxqV_TEmb1jbTg=
     * p_desc : 快速审批，当天到账。
     * min_algorithm : 0.900
     * fastest_time : 2小时
     * minimum_amount : 500
     * maximum_amount : 5000
     * labels : []
     */

    private String id;
    private String p_name;
    private String url;
    private String p_logo;
    private String p_desc;
    private String min_algorithm;
    private String fastest_time;
    private String minimum_amount;
    private String maximum_amount;
    private int interest_algorithm;
    private String apply;

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public int getInterest_algorithm() {
        return interest_algorithm;
    }

    public void setInterest_algorithm(int interest_algorithm) {
        this.interest_algorithm = interest_algorithm;
    }

    private List<?> labels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getP_logo() {
        return p_logo;
    }

    public void setP_logo(String p_logo) {
        this.p_logo = p_logo;
    }

    public String getP_desc() {
        return p_desc;
    }

    public void setP_desc(String p_desc) {
        this.p_desc = p_desc;
    }

    public String getMin_algorithm() {
        return min_algorithm;
    }

    public void setMin_algorithm(String min_algorithm) {
        this.min_algorithm = min_algorithm;
    }

    public String getFastest_time() {
        return fastest_time;
    }

    public void setFastest_time(String fastest_time) {
        this.fastest_time = fastest_time;
    }

    public String getMinimum_amount() {
        return minimum_amount;
    }

    public void setMinimum_amount(String minimum_amount) {
        this.minimum_amount = minimum_amount;
    }

    public String getMaximum_amount() {
        return maximum_amount;
    }

    public void setMaximum_amount(String maximum_amount) {
        this.maximum_amount = maximum_amount;
    }

    public List<?> getLabels() {
        return labels;
    }

    public void setLabels(List<?> labels) {
        this.labels = labels;
    }
}
