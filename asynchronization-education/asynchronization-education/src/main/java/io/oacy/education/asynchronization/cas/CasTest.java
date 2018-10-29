package io.oacy.education.asynchronization.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class CasTest {
    public static void main(String[] args) {

        DemoRunnable dr = new DemoRunnable();

        for (int i = 0; i < 10; i++) {
            new Thread(dr).start();
        }
    }
}

class DemoRunnable implements Runnable {

//    private volatile int count = 0;
    private volatile AtomicInteger count = new AtomicInteger();

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("子线程-count:" + (++count));
        System.out.println("子线程-count:" + (count.incrementAndGet()));
    }

}
