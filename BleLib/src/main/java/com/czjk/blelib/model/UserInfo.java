package com.czjk.blelib.model;

/**
 * Created by czjk on 2017/2/28.
 */

public class UserInfo {
    private int age;//年龄
    private int height;  //身高
    private int weight;  //体重
    private int sex; //性别 0 女  1 男
    private int stride;

    public UserInfo(int age,int height, int weight, int sex,int stride) {
        this.age=age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.stride=stride;
    }

    public UserInfo() {
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStride() {
        return stride;
    }

    public void setStride(int stride) {
        this.stride = stride;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", sex=" + sex +
                ", stride=" + stride +
                '}';
    }
}
