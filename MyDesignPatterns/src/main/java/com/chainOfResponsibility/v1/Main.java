package com.chainOfResponsibility.v1;

public class MyFilter {

    public static void main(String[] args) {
        Msg m = new Msg();
        m.setStr("大家好，我是xxx，这次疫情对全球造成了不可估量的损失，<img>都是那一个吃蝙蝠的傻x惹的祸啊！</img>");
        System.out.println("处理前："+m.toString());

        String str = m.getStr().replaceAll("x","*");
        str = str.replaceAll("傻","*");
        str = str.replaceAll("<img>","");
        str = str.replaceAll("</img>","");

        m.setStr(str);
        System.out.println("处理后："+m.toString());
    }

}

class Msg {

    String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "{" +
                "str='" + str + '\'' +
                '}';
    }
}