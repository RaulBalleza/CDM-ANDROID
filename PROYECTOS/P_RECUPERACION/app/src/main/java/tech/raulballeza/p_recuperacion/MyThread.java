package tech.raulballeza.p_recuperacion;

import android.graphics.Canvas;

public class MyThread extends Thread {
    static final long FPS = 60;
    private MySurfaceView myView;
    private boolean running = false;

    public MyThread(MySurfaceView view) {
        myView = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = myView.getHolder().lockCanvas();
                synchronized (myView.getHolder()) {
                    //myView.onDraw(c);
                    myView.drawSomething(c);
                }
            } finally {
                if (c != null) {
                    myView.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {
            }
        }
    }
}
