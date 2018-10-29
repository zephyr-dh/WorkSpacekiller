package io.oacy.education.asynchronization.volatiles;

public class VolatileTest {
    public static void main(String[] args) {

        DemoThread dt = new DemoThread();
        dt.start();

        while(true) {
            if (dt.isFlag()) {
                System.out.println("主线程终止");
                break;
            }
        }

    }
}

class DemoThread extends Thread {

    private volatile boolean flag;
//    private boolean flag;

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flag = true;
        System.out.println("子线程-flag:" + isFlag());
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
