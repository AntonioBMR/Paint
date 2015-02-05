package com.antonio.android.paintfinger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Antonio on 22/01/2015.
 */
public class Pintor extends View {
    //forma por defecto
    private int tipoPincel=1;
    //ruta
    private Path drawPath;
    //drawing and canvas paint
    private Paint pincel, canvasPaint;
    //inicial color
    private int paintColor = 0xFF000000;
    //canvas
    private Canvas lienzoFondo;
    //ultimo valor polilinea
    private float polX=-1;
    private float polY=-1;
    //canvas bitmap
    private Bitmap canvasBitmap;
    //radio circulos
    private double radio=0;
    //tamaños
    private float brushSize, lastBrushSize;
    //bandera uso borrar
    private boolean erase=false;
    //coordenadas canvas fuera de cotas del lienzo
    private float x0 = -1, y0 = -1, xi = -1, yi = -1;

    public Pintor(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    //Datos a cargar
    private void setupDrawing(){
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
        drawPath = new Path();
        pincel = new Paint();
        pincel.setColor(paintColor);
        pincel.setAntiAlias(true);
        pincel.setStrokeWidth(brushSize);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeJoin(Paint.Join.ROUND);
        pincel.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }
    //tamaño lienzo
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(canvasBitmap);
    }
    public void setTipoPincel(int tp) {
        this.tipoPincel = tp;
    }
    //draw the view - will be called after touch event
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, pincel);
        switch (tipoPincel) {
            case 1://A mano alzada
                pincel.setStyle(Paint.Style.STROKE);
                canvas.drawPath(drawPath, pincel);
                polX=-1;
                polY=-1;
                break;
            case 2://Linea recta
                canvas.drawLine(x0, y0, xi, yi, pincel);
                polX=-1;
                polY=-1;
                break;
            case 3://Rectangulo
                pincel.setStyle(Paint.Style.STROKE);
                float x = Math.min(x0, xi);
                float x1 = Math.max(x0,xi);
                float y = Math.min(y0, yi);
                float y1 = Math.max(y0, yi);
                canvas.drawRect(x, y, x1, y1, pincel);
                polX=-1;
                polY=-1;
                break;
            case 4://Circulo
                pincel.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(x0, y0,(float)radio, pincel);
                polX=-1;
                polY=-1;
                break;
            case 6://polilinea
                canvas.drawLine(x0, y0, xi, yi, pincel);
                break;
            case 7://rect lleno
                pincel.setStyle(Paint.Style.FILL);
                float xl = Math.min(x0, xi);
                float xl1 = Math.max(x0,xi);
                float yl = Math.min(y0, yi);
                float yl1 = Math.max(y0, yi);
                canvas.drawRect(xl, yl, xl1, yl1, pincel);
                polX=-1;
                polY=-1;
                break;
            case 8://Circulo lleno
                canvas.drawCircle(x0, y0,(float)radio, pincel);
                polX=-1;
                polY=-1;
                break;
        }

    }

    //registro de toques con el dedo sobre lienzo
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(tipoPincel==1){//libre
                    pincel.setStyle(Paint.Style.STROKE);
                    x0 = xi = event.getX();
                    y0 = yi = event.getY();
                    drawPath.reset();
                    drawPath.moveTo(x0, y0);
                }
                if(tipoPincel==2){//rectas
                    x0 =touchX;
                    y0 =touchY;
                }
                if(tipoPincel==3||tipoPincel==7){//rectangulos
                    x0=xi=touchX;
                    y0=yi=touchY;
                    if(tipoPincel==3){
                        pincel.setStyle(Paint.Style.STROKE);
                    }else{
                        pincel.setStyle(Paint.Style.FILL);
                    }
                }
                if(tipoPincel==4||tipoPincel==8){//circulos
                    x0 =xi=touchX;
                    y0 =yi=touchY;
                    if(tipoPincel==4){
                        pincel.setStyle(Paint.Style.STROKE);
                    }else{
                        pincel.setStyle(Paint.Style.FILL);
                    }
                 }
                if(tipoPincel==6){//polilinea
                    if(polX==-1&&polY==-1){
                        x0 =touchX;
                        y0 =touchY;
                    }else{
                        y0=polY;
                        x0=polX;
                        xi=touchX;
                        yi=touchY;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                //xi = touchX;
               // yi = touchY;
                if(tipoPincel==1){
                    drawPath.lineTo(touchX, touchY);
                    drawPath.quadTo(xi, yi, (touchX + xi) / 2, (touchY + yi) / 2);
                    xi = touchX;
                    yi = touchY;
                    x0 = xi;
                    y0 = yi;
                    lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                    invalidate();
                }
                if(tipoPincel==3||tipoPincel==7){//rectangulos
                    xi = touchX;
                    yi = touchY;
                    if(tipoPincel==3){
                        pincel.setStyle(Paint.Style.STROKE);
                    }else{
                        pincel.setStyle(Paint.Style.FILL);
                    }
                    invalidate();
                } if(tipoPincel==2) {//recta
                    xi=touchX;
                    yi=touchY;
                    //invalidate();
                } if(tipoPincel==6) {//polilinea
                    xi=touchX;
                    yi=touchY;
                }
                if(tipoPincel==4||tipoPincel==8) {//circulos
                    if(tipoPincel==4){
                        pincel.setStyle(Paint.Style.STROKE);
                    }else{
                        pincel.setStyle(Paint.Style.FILL);
                    }
                    xi=touchX;
                    yi=touchY;
                    radio = Math.sqrt(((xi - x0) * (xi - x0)) + ((yi - y0) * (yi - y0)));
                    //invalidate();
                  }
                break;
            case MotionEvent.ACTION_UP:
                if(tipoPincel==1){//libre
                    pincel.setStyle(Paint.Style.STROKE);
                    drawPath.lineTo(touchX, touchY);
                    lienzoFondo.drawPath(drawPath, pincel);
                    drawPath.reset();
                }if(tipoPincel==3||tipoPincel==7){//rectangulo
                    if(tipoPincel==3) {
                        pincel.setStyle(Paint.Style.STROKE);
                        lienzoFondo.drawRect(x0, y0, xi, yi, pincel);
                    }else{
                        pincel.setStyle(Paint.Style.FILL);
                        lienzoFondo.drawRect(x0, y0, xi, yi, pincel);
                    }
                }if(tipoPincel==4||tipoPincel==8) {//circulo
                    xi = touchX;
                    yi = touchY;
                    if(tipoPincel==4){
                        pincel.setStyle(Paint.Style.STROKE);
                    }else{
                        pincel.setStyle(Paint.Style.FILL);
                       // invalidate();
                    }
                    radio = Math.sqrt(((xi-x0)*(xi-x0))+((yi-y0)*(yi-y0)));
                    lienzoFondo.drawCircle(x0, y0,(float)radio, pincel);
                    radio=0;
                }if(tipoPincel==2) {//recta
                    lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                    drawPath.reset();
                }
                if(tipoPincel==6) {//polillinea
                    xi=touchX;
                    yi=touchY;
                    lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                    x0=xi;
                    y0=yi;
                    polX=x0;
                    polY=y0;
                }
                break;
        }
        //redraw
        invalidate();
        return true;

    }
    //actualizar color
    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        pincel.setColor(paintColor);
    }
    public int getColor(){
        return pincel.getColor();
    }
    //devuelve bitmap
    public Bitmap getBitM(){
        return this.canvasBitmap;
    }
    //cambiar color
    public void setColor(int color){
        invalidate();

        pincel.setColor(color);
    }
    //cambiar tamaño brocha size
    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        pincel.setStrokeWidth(brushSize);
    }

    //ultimo /anterior tamaño brocha size
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    //estaborrando
    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) pincel.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else pincel.setXfermode(null);
        polX=-1;
        polY=-1;
    }

    //nuevo dibujo
    public void startNew(){
        lienzoFondo.drawColor(0, PorterDuff.Mode.CLEAR);
        polX=-1;
        polY=-1;
        invalidate();
    }

}
