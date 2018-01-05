package cn.pingweb;

import lombok.ToString;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        B b = new B();
        A a = new A(b);
        b.setA(a);
        System.out.println(a);
        System.out.println(b);
    }
}

@ToString
class A {
    private B b;
    A (B b) {
       this.b = b;
    }
}

@ToString
class B {
    private A a;

    public void setA(A a) {
        this.a = a;
    }
}