package com.openelements.dbch;

public class Application {

    public static void main(String[] args) {
        Chain chain = new Chain();
        chain.addBlock("data1");
        chain.addBlock("data2");
        chain.addBlock("data3");
        System.out.println(chain);
        chain.verify();
    }
}
