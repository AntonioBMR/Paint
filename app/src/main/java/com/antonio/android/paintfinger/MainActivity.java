package com.antonio.android.paintfinger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
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

    private Pintor vistaDibujo;
    private ImageButton actual,colorBtn, grosorBtn, borrarBtn, newBtn, guardarBtn,colorSeleccionado;
    private float grosorBajo, grosorMedio, grosorAlto;
/**********************************************************/
/***********************ONCREATE***************************/
/**********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
        vistaDibujo = (Pintor)findViewById(R.id.drawing);
        //PALETA DE FORMAS
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.estilosPincel);
        //selectores
        actual = (ImageButton)paintLayout.getChildAt(0);
        actual.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        //TAMAÑO DEL PINCEL
        grosorBajo = getResources().getInteger(R.integer.small_size);
        grosorMedio = getResources().getInteger(R.integer.medium_size);
        grosorAlto = getResources().getInteger(R.integer.large_size);
        //BOTON GROSOR
        grosorBtn = (ImageButton)findViewById(R.id.draw_btn);
        grosorBtn.setBackgroundResource(R.drawable.brush);
        grosorBtn.setOnClickListener(this);
        vistaDibujo.setBrushSize(grosorMedio);
        //PALETA DE COLORES
        colorBtn = (ImageButton)findViewById(R.id.color_btn);
        colorBtn.setBackgroundResource(R.drawable.paleta);
        colorBtn.setOnClickListener(this);
        //BUTON BORRADO
        borrarBtn = (ImageButton)findViewById(R.id.erase_btn);
        borrarBtn.setBackgroundResource(R.drawable.eraser);
        borrarBtn.setOnClickListener(this);
        //BOTON NUEVO
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setBackgroundResource(R.drawable.new_pic);
        newBtn.setOnClickListener(this);
        // BUTON GUARDAR
        guardarBtn = (ImageButton)findViewById(R.id.save_btn);
        guardarBtn.setBackgroundResource(R.drawable.save);
        guardarBtn.setOnClickListener(this);
        //button vista color seleccionado
        colorSeleccionado=(ImageButton)findViewById(R.id.colorSeleccionado);
        int color=vistaDibujo.getColor();
        colorSeleccionado.setBackgroundColor(color);

    }

/**********************************************************/
/************************ONCLICK***************************/
/**********************************************************/
    @Override
    public void onClick(View view){

        if(view.getId()==R.id.draw_btn){
            borrarBtn.setBackgroundResource(R.drawable.eraser);
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle(R.string.grosorP);
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    vistaDibujo.setErase(false);
                    vistaDibujo.setBrushSize(grosorBajo);
                    vistaDibujo.setLastBrushSize(grosorBajo);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    vistaDibujo.setErase(false);
                    vistaDibujo.setBrushSize(grosorMedio);
                    vistaDibujo.setLastBrushSize(grosorMedio);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    vistaDibujo.setErase(false);
                    vistaDibujo.setBrushSize(grosorAlto);
                    vistaDibujo.setLastBrushSize(grosorAlto);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            borrarBtn.setBackgroundResource(R.drawable.eraser1);
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle(R.string.grosorG);
            brushDialog.setContentView(R.layout.brush_chooser);
            //Tamaños
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    vistaDibujo.setErase(true);
                    vistaDibujo.setBrushSize(grosorBajo);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    vistaDibujo.setErase(true);
                    vistaDibujo.setBrushSize(grosorMedio);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    vistaDibujo.setErase(true);
                    vistaDibujo.setBrushSize(grosorAlto);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }

        else if(view.getId()==R.id.color_btn){
            borrarBtn.setBackgroundResource(R.drawable.eraser);
            vistaDibujo.setErase(false);
            new ColorPickerDialog(this,this,vistaDibujo.getColor()).show();
            int color=vistaDibujo.getColor();
            colorSeleccionado.setBackgroundColor(color);

        }
        else if(view.getId()==R.id.new_btn){
            borrarBtn.setBackgroundResource(R.drawable.eraser);
            vistaDibujo.setErase(false);
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setMessage(R.string.des_new);
            newDialog.setPositiveButton(R.string.Si, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    vistaDibujo.startNew();
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
            borrarBtn.setBackgroundResource(R.drawable.eraser);
            vistaDibujo.setErase(false);
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            saveDialog.setView(input);
            saveDialog.setMessage(R.string.guardarO);
            saveDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //GUARDADO
                    String m_Text = input.getText().toString();
                    String nombre;
                    if(m_Text.isEmpty()){
                        nombre= new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());

                    }else{
                        nombre=m_Text+""+new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
                    }
                    System.out.println(" nombre "+nombre);
                    vistaDibujo.setDrawingCacheEnabled(true);
                    //attempt to save
                    Bitmap mapaDeBits= vistaDibujo.getBitM();
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

                    vistaDibujo.destroyDrawingCache();
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
        vistaDibujo.setTipoPincel(3);
        selector(v);
    }
    public void libre(View v){
        vistaDibujo.setTipoPincel(1);
        selector(v);
    }
    public void recta(View v){
        vistaDibujo.setTipoPincel(2);
        selector(v);
    }
    public void circulo(View v){
        vistaDibujo.setTipoPincel(4);
        selector(v);
    }
    public void circuloL(View v){
        vistaDibujo.setTipoPincel(8);
        selector(v);
    }
    public void rectanguloL(View v){
        vistaDibujo.setTipoPincel(7);
        selector(v);
    }
    public void poliLinea(View v){
        vistaDibujo.setTipoPincel(6);
        selector(v);
    }
    //ColorEnUso
    public void paintClicked(View view){
        vistaDibujo.setErase(false);
        ImageButton imgView = (ImageButton)view;
        String color = view.getTag().toString();
        vistaDibujo.setColor(color);
        colorSeleccionado.setBackgroundColor(vistaDibujo.getColor());
    }

/**********************************************************/
/*************Sobrescritua colorChanged********************/
/**********************************************************/
    @Override
    public void colorChanged(int color) {
        vistaDibujo.setColor(color);
        colorSeleccionado.setBackgroundColor(vistaDibujo.getColor());
    }
/**********************************************************/
/***************BOTON FORMA EN USO*************************/
/**********************************************************/
    public void selector(View v){
        borrarBtn.setBackgroundResource(R.drawable.eraser);
        vistaDibujo.setErase(false);
        if(v!=actual){
            ImageButton imgView = (ImageButton)v;
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            actual.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            actual=(ImageButton)v;
        }
    }
}