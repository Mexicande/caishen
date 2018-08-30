package com.example.expense.DoubleFlower.bean;

import java.io.Serializable;

/**
 *
 * @author apple
 * @date 2018/5/18
 *
 */

public class Product implements Serializable {

    /**
     * id : 105
     * sort : 54
     * name : 金兔宝
     * link : https://jtb.jintubao0574.com/jtb/regist/17af1be4e2dc452ab6a98fbb6043e2c1
     * product_logo : http://or2eh71ll.bkt.clouddn.com/153352880950250.png?e=1533532409&token=Npg7Sanmf4z8uv3mvwwffjOvoCMYN8Ezm4T8pDrC:OV0rUsb5kyLlzXPzncQBD9pMgEk=
     * product_introduction : 一分钟认证，十分钟审核，半小时放款
     * interest_algorithm : 日利率
     * min_algorithm : 0.060%
     * max_algorithm : 0.060%
     * maximum_amount : 200000
     * minimum_amount : 500
     */

    private String id;
    private int sort;
    private String name;
    private String link;
    private String product_logo;
    private String product_introduction;
    private String interest_algorithm;
    private String min_algorithm;
    private String max_algorithm;
    private String maximum_amount;
    private String minimum_amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(String product_logo) {
        this.product_logo = product_logo;
    }

    public String getProduct_introduction() {
        return product_introduction;
    }

    public void setProduct_introduction(String product_introduction) {
        this.product_introduction = product_introduction;
    }

    public String getInterest_algorithm() {
        return interest_algorithm;
    }

    public void setInterest_algorithm(String interest_algorithm) {
        this.interest_algorithm = interest_algorithm;
    }

    public String getMin_algorithm() {
        return min_algorithm;
    }

    public void setMin_algorithm(String min_algorithm) {
        this.min_algorithm = min_algorithm;
    }

    public String getMax_algorithm() {
        return max_algorithm;
    }

    public void setMax_algorithm(String max_algorithm) {
        this.max_algorithm = max_algorithm;
    }

    public String getMaximum_amount() {
        return maximum_amount;
    }

    public void setMaximum_amount(String maximum_amount) {
        this.maximum_amount = maximum_amount;
    }

    public String getMinimum_amount() {
        return minimum_amount;
    }

    public void setMinimum_amount(String minimum_amount) {
        this.minimum_amount = minimum_amount;
    }
}
