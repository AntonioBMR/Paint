package com.antonio.android.paintfinger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity implements OnClickListener, ColorPickerDialog.OnColorChangedListener {

    private DrawingView drawView;
    private ImageButton currPaint,colorBtn, drawBtn, eraseBtn, newBtn, saveBtn;
    private float smallBrush, mediumBrush, largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

        //get drawing view
        drawView = (DrawingView)findViewById(R.id.drawing);

        //get the palette and first color button
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.estilosPincel);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //draw button
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setBackgroundResource(R.drawable.brush);
        drawBtn.setOnClickListener(this);

        //set initial size
        drawView.setBrushSize(mediumBrush);
               //boton color
        colorBtn = (ImageButton)findViewById(R.id.color_btn);
        colorBtn.setBackgroundResource(R.drawable.paleta);
        colorBtn.setOnClickListener(this);
        //erase button
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setBackgroundResource(R.drawable.eraser);
        eraseBtn.setOnClickListener(this);

        //new button
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setBackgroundResource(R.drawable.new_pic);
        newBtn.setOnClickListener(this);

        //save button
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setBackgroundResource(R.drawable.save);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //user clicked paint

    @Override
    public void onClick(View view){

        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            eraseBtn.setBackgroundResource(R.drawable.eraser);

            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle(R.string.grosorP);
            brushDialog.setContentView(R.layout.brush_chooser);
            //listen for clicks on size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            //show and wait for user interaction
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            eraseBtn.setBackgroundResource(R.drawable.eraser1);
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle(R.string.grosorG);
            brushDialog.setContentView(R.layout.brush_chooser);
            //size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }

        else if(view.getId()==R.id.color_btn){
            eraseBtn.setBackgroundResource(R.drawable.eraser);
            drawView.setErase(false);
            new ColorPickerDialog(this,this,drawView.getSolidColor()).show();

        }
        else if(view.getId()==R.id.new_btn){
            eraseBtn.setBackgroundResource(R.drawable.eraser);
            drawView.setErase(false);
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setMessage(R.string.des_new);
            newDialog.setPositiveButton(R.string.Si, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            eraseBtn.setBackgroundResource(R.drawable.eraser);
            drawView.setErase(false);
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            saveDialog.setView(input);
            saveDialog.setMessage(R.string.guardarO);
            saveDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    String m_Text = input.getText().toString();
                    String nombre;
                    if(m_Text.isEmpty()){
                        nombre= new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());

                    }else{
                        nombre=m_Text+""+new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
                    }
                    System.out.println(" nombre "+nombre);
                    drawView.setDrawingCacheEnabled(true);
                    //attempt to save
                    Bitmap mapaDeBits= drawView.getBitM();
                    File carpeta = new File(Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_PICTURES).getPath());
                    File archivo = new File(carpeta,nombre+".PNG");
                    try {
                        FileOutputStream fos = new FileOutputStream(archivo);
                        mapaDeBits.compress(
                                Bitmap.CompressFormat.PNG, 90, fos);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    };

                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
    }

    public void rectangulo(View v){
        drawView.setTipoPincel(3);
        selector(v);
    }
    public void libre(View v){
        drawView.setTipoPincel(1);
        selector(v);
    }
    public void recta(View v){
        drawView.setTipoPincel(2);
        selector(v);
    }
    public void circulo(View v){
        drawView.setTipoPincel(4);
        selector(v);
    }
    public void circuloL(View v){
        drawView.setTipoPincel(8);
        selector(v);
    }
    public void rectanguloL(View v){
        drawView.setTipoPincel(7);
        selector(v);
    }
    public void poliLinea(View v){
        drawView.setTipoPincel(6);
        selector(v);
    }
    @Override
    public void colorChanged(int color) {
        drawView.setColor(color);
    }
    public void selector(View v){
        eraseBtn.setBackgroundResource(R.drawable.eraser);
        drawView.setErase(false);
        if(v!=currPaint){
            ImageButton imgView = (ImageButton)v;
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)v;
        }
    }

}