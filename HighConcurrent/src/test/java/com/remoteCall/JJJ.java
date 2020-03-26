package com.remoteCall;

public class JJJ {


    public static void main(String[] args) {

        CCC c = new CCC(){

        };
        System.out.println(AAA.class.isInterface());

    }

    class AAA implements CCC{


    }


    interface  CCC{

    }
}