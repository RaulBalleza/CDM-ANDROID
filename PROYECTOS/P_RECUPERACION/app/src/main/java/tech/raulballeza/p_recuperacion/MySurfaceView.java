package tech.raulballeza.p_recuperacion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MySurfaceView extends SurfaceView {
    private SurfaceHolder holder;
    private MyThread gameLoopThread;
    private int Estado = 0;
    private int contador = 0;
    private double contador3 = 0;
    private int contador2 = 0;
    private int barra_color = 0;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        gameLoopThread = new MyThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
                               @Override
                               public void surfaceCreated(SurfaceHolder surfaceHolder) {
                                   gameLoopThread.setRunning(true);
                                   gameLoopThread.start();
                               }

                               @Override
                               public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                               }

                               @Override
                               public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                                   boolean retry = true;
                                   gameLoopThread.setRunning(false);
                                   while (retry) {
                                       try {
                                           gameLoopThread.join();
                                           retry = false;
                                       } catch (InterruptedException e) {
                                       }
                                   }
                               }
                           }
        );
    }

    public void drawSomething(Canvas canvas) {
        contador++;
        System.out.println("contador = " + contador);
        int progress = MainActivity.seekBar.getProgress();
        contador2 = progress;
        contador3 = progress + Math.PI;
        System.out.println("progreso = " + progress);
        int x = getWidth();//OBTENER ANCHO
        int y = getHeight();//OBTENER ALTO
        int l = y / 3;
        Paint paint = new Paint();
        //paint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);



        switch (Estado) {
            case 0:
                paint.setColor(Color.WHITE);
                break;
            case 1:
                paint.setColor(Color.MAGENTA);
                break;
        }
        canvas.drawPaint(paint);
        // Use Color.parseColor to define HTML colors
        //paint.setColor(Color.parseColor("#FF0000"));
        paint.setColor(Color.BLACK);

        //Dibujar Texto
        /*paint.setTextSize(50);
        canvas.drawText("TEXTO", 50, 50, paint);*/

        //Dibujar una linea
        //canvas.drawLine(0, (y / 3f) * 2, x, (y / 3f) * 2, paint);

        /*Elementos estaticos*/
        //Radio del circulo
        float radius = x / 4f;
        //Dibujar circulo(cx, cy, radio, lienzo)
        canvas.drawCircle(x / 2f, y / 2f, radius, paint);
        Punto punto1 = new Punto(canvas, paint, (float) (radius * cos(180 * Math.PI / 180.0f) + x / 2f), (float) (radius * sin(180 * Math.PI / 180.0f) + y / 2f));
        Punto punto2 = new Punto(canvas, paint, (float) (radius * cos(300 * Math.PI / 180.0f) + x / 2f), (float) (radius * sin(300 * Math.PI / 180.0f) + y / 2f));
        Punto punto3 = new Punto(canvas, paint, (float) (radius * cos(45 * Math.PI / 180.0f) + x / 2f), (float) (radius * sin(45 * Math.PI / 180.0f) + y / 2f));
        Punto punto_medio = new Punto(canvas, paint, x / 2f, y / 3.1f);
        float z = radius + ((contador - 100) * 6);

        /*for (float i = 0; i <= 360; i += 360 / 7) {
            punto_medio.setX(punto_medio.getX()+contador);
            punto_medio.setY(punto_medio.getY()+contador);
        }*/
        for (int t = 0; t < 360; t += 1) {
            punto_medio.setX((float) (radius * cos(contador3 * Math.PI * Math.PI / 180.0f) + x / 2f));
            punto_medio.setY((float) (radius * sin(contador3 * Math.PI * Math.PI / 180.0f) + y / 2f));
        }
        //canvas.drawLine((float) (z + (Math.sin(Math.toRadians(i)) * (radius - 8))), (float) ((l * 2 - radius) + (Math.cos(Math.toRadians(i)) * (radius - 8))), z, (l * 2 - radius), paint);

        punto1.draw();
        punto2.draw();
        punto3.draw();

        System.out.println( punto2.toString());

        paint.setColor(Color.BLACK);
        paint.setPathEffect(new DashPathEffect(new float[]{10f, 20f}, 0f));

        canvas.drawLine(punto1.getX(), punto1.getY(), punto3.getX(), punto3.getY(), paint);
        canvas.drawLine(punto3.getX(), punto3.getY(), punto2.getX(), punto2.getY(), paint);
        canvas.drawLine(punto1.getX(), punto1.getY(), punto2.getX(), punto2.getY(), paint);

        /*Animacion alrededor del circulo*/
        punto_medio.draw();
        System.out.println(punto_medio.toString());
        paint.setPathEffect(null);
        paint.setColor(Color.BLUE);
        canvas.drawLine(punto1.getX(), punto1.getY(), punto_medio.getX(), punto_medio.getY(), paint);
        paint.setColor(Color.GREEN);
        canvas.drawLine(punto2.getX(), punto2.getY(), punto_medio.getX(), punto_medio.getY(), paint);
        paint.setColor(Color.RED);
        canvas.drawLine(punto3.getX(), punto3.getY(), punto_medio.getX(), punto_medio.getY(), paint);

        /*Animacion de las barras*/
        paint.setColor(Color.BLUE);
        Punto punto_barra1 = new Punto(canvas, paint, x / 4f, y / 4f * 3);
        Punto punto_barra1_2 = new Punto(canvas, paint, (x / 4f * 3) + contador2, punto_barra1.getY());

        Punto punto_barra2 = new Punto(canvas, paint, x / 4f, (y / 4f * 3) + 50);
        Punto punto_barra2_2 = new Punto(canvas, paint, ((x / 4f) * 2.7f) - contador2, punto_barra2.getY());
        Punto punto_barra2_3 = new Punto(canvas, paint, punto_barra1_2.getX(), punto_barra2.getY());
        ;


        //canvas.drawLine(0, (y / 3f) * 2, x, (y / 3f) * 2, paint);

        if (progress >= 11 && progress <=54) {
            barra_color = 0;
            contador2 = 0;
        } else if (progress >= 54 && progress <=92) {
            barra_color = 1;
            contador2 = 0;
        } else{
            barra_color = 2;
            contador2 = 0;
        }


        punto_barra1.draw();
        punto_barra2.draw();
        punto_barra1_2.draw();
        punto_barra2_2.draw();
        punto_barra2_3.draw();

        switch (barra_color) {
            case 0:
                paint.setColor(Color.GREEN);
                break;
            case 1:
                paint.setColor(Color.RED);
                break;
            case 2:
                paint.setColor(Color.BLUE);
                break;
        }
        canvas.drawLine(punto_barra1.getX(), punto_barra1.getY(), punto_barra1_2.getX(), punto_barra1_2.getY(), paint);
        switch (barra_color) {
            case 0:
                paint.setColor(Color.RED);
                break;
            case 1:
                paint.setColor(Color.BLUE);
                break;
            case 2:
                paint.setColor(Color.GREEN);
                break;
        }
        canvas.drawLine(punto_barra2.getX(), punto_barra2.getY(), punto_barra2_2.getX(), punto_barra2_2.getY(), paint);
        switch (barra_color) {
            case 0:
                paint.setColor(Color.BLUE);
                break;
            case 1:
                paint.setColor(Color.GREEN);
                break;
            case 2:
                paint.setColor(Color.RED);
                break;
        }
        canvas.drawLine(punto_barra2_2.getX(), punto_barra2_2.getY(), punto_barra2_3.getX(), punto_barra2_3.getY(), paint);

        contador2++;
    }

    private class Punto {
        private final Canvas canvas;
        private final Paint paint;
        private float x;
        private float y;

        public Punto(Canvas canvas, Paint paint, float x, float y) {
            this.canvas = canvas;
            this.paint = paint;
            this.x = x;
            this.y = y;
        }

        public void draw() {
            Path path;
            float px, py;

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            path = new Path();//ROMBO POSICION 0
            path.setFillType(Path.FillType.EVEN_ODD);
            px = x;
            py = y;
            path.moveTo(px, py);
            path.lineTo(px + 15, py + 15);
            path.lineTo(px, py + 30);
            path.lineTo(px - 15, py + 15);
            path.lineTo(px, py);
            path.close();
            canvas.drawPath(path, paint);
        }

        @Override
        public String toString() {
            return "Punto{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y + 15;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y) {
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Punto punto = (Punto) o;
            return Float.compare(punto.getX(), getX()) == 0 &&
                    Float.compare(punto.getY(), getY()) == 0 &&
                    Objects.equals(canvas, punto.canvas) &&
                    Objects.equals(paint, punto.paint);
        }

        @Override
        public int hashCode() {
            return Objects.hash(canvas, paint, getX(), getY());
        }
    }
}
