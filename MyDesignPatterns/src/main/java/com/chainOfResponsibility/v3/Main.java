package com.chainOfResponsibility.v2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Msg m = new Msg();
        m.setStr("大家好，我是xxx，这次疫情对全球的人类造成了不可估量的上海，<img>都是那一个吃蝙蝠的傻x惹的祸啊！</img>");
        System.out.println("处理前："+m.toString());

//        List<Filter> filters = new ArrayList<>();
//        filters.add(new SpecialFilter());
//        filters.add(new SensitiveFilter());
//        filters.add(new ImgFilter());
//
//        for(Filter f:filters){
//            f.doFilter(m);
//        }

        FilterChain filterChain = new FilterChain();
        filterChain.addFilter(new SpecialFilter()).addFilter(new SensitiveFilter()).addFilter(new ImgFilter());
        filterChain.doFilter(m);

//        new SpecialFilter().doFilter(m);
//        new SensitiveFilter().doFilter(m);
//        new ImgFilter().doFilter(m);

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

/**
 * 特殊字符
 */
class SpecialFilter implements Filter{

    @Override
    public boolean doFilter(Msg m) {
        String str = m.getStr().replaceAll("x","*");
        m.setStr(str);
        return true;
    }
}

/**
 * 敏感字符
 */
class SensitiveFilter implements Filter{

    @Override
    public boolean doFilter(Msg m) {
        String str = m.getStr().replaceAll("傻","*");
        m.setStr(str);
        return false;
    }
}

class ImgFilter implements Filter {

    @Override
    public boolean doFilter(Msg m) {
        String str = m.getStr().replaceAll("<img>","");
        str = str.replaceAll("</img>","");
        m.setStr(str);
        return true;
    }
}

class FilterChain implements Filter{

    List<Filter> filters = new ArrayList<>();

    public FilterChain addFilter(Filter f){
        filters.add(f);
        return this;
    }

    @Override
    public boolean doFilter(Msg m){
        for(Filter f:filters) {
            if(!f.doFilter(m)){
                return false;
            }
        }
        return true;
    }
}


interface Filter {

    boolean doFilter(Msg m);
}