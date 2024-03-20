package com.juangabriel.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private Tablero fondo;
    int x, y;
    private Casilla[][] casillas;
    private boolean activo = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        LinearLayout layout = (LinearLayout) findViewById(R.id.tablero);
        fondo = new Tablero(this);
        fondo.setOnTouchListener(this);
        layout.addView(fondo);

        reiniciarJuego();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int posX = (int) event.getX();
        int posY = (int) event.getY();

        if (activo){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j<8; j++){

                   if (casillas[i][j].dentroDelPixel(posX,posY)){
                       casillas[i][j].destapada = true;

                       if (casillas[i][j].contenido == 100){
                           Toast.makeText(this, "Booooooooom! En toda tu cara!!!",
                                   Toast.LENGTH_LONG).show();
                           destaparBombas();
                           activo = false;
                       } else  if (casillas[i][j].contenido == 0){
                           recorrer(i, j);
                       }

                       fondo.invalidate();
                   }
                }
            }

            if (activo && heGanado()){
                Toast.makeText(this, "Enhorabuena!!! Tenemos un ganador!!",
                        Toast.LENGTH_SHORT).show();
                activo = false;
            }

        }

        return false;
    }


    public void destaparBombas(){
        for (int i = 0; i<8; i++){
            for (int j = 0; j < 8; j++){
                if (casillas[i][j].contenido==100){
                    casillas[i][j].destapada = true;
                }
            }
        }
        fondo.invalidate();
    }



    public void reiniciarJuego(){
        casillas = new Casilla[8][8];
        for (int i = 0; i<8; i++){
            for (int j = 0; j<8; j++){
                casillas[i][j] = new Casilla();
            }
        }
        colocarMinas();
        contarBombasDelPerimetro();
        activo = true;

        fondo.invalidate();

    }


    private void colocarMinas(){
        int cantidadDeMinasPorColocar = 8;
        while (cantidadDeMinasPorColocar>0){
            int fila = (int) (Math.random()*8);
            int columna = (int) (Math.random()*8);
            if (casillas[fila][columna].contenido == 0){
                casillas[fila][columna].contenido = 100;
                cantidadDeMinasPorColocar --;

            }
        }
    }

    private boolean heGanado(){
        int cantidad = 0;
        for (int i = 0; i< 8; i++){
            for (int j = 0; j<8; j++){
                if (casillas[i][j].destapada){
                    cantidad++;
                }
            }
        }

        if (cantidad == 56){
            return true;
        } else {
            return false;
        }
    }

    private void contarBombasDelPerimetro() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (casillas[i][j].contenido == 0) {
                    int cantidadDeBombasVecinas = contarCoordenada(i, j);
                    casillas[i][j].contenido = cantidadDeBombasVecinas;
                }
            }
        }
    }

    private int contarCoordenada(int fila, int columna){
        int cantidadDeBombas = 0;

        if (fila - 1 >= 0 && columna - 1 >=0){
            if (casillas[fila-1][columna-1].contenido==100){
                cantidadDeBombas++;
            }
        }

        if (fila - 1 >= 0){
            if (casillas[fila-1][columna].contenido==100){
                cantidadDeBombas++;
            }
        }

        if (fila - 1 >= 0 && columna + 1 <8){
            if (casillas[fila-1][columna+1].contenido==100){
                cantidadDeBombas++;
            }
        }

        if ( columna + 1 <8){
            if (casillas[fila][columna+1].contenido==100){
                cantidadDeBombas++;
            }
        }

        if (fila + 1 <8 && columna + 1 <8){
            if (casillas[fila+1][columna+1].contenido==100){
                cantidadDeBombas++;
            }
        }

        if (fila + 1 <8 ){
            if (casillas[fila+1][columna].contenido==100){
                cantidadDeBombas++;
            }
        }

        if (fila + 1 <8 && columna - 1 >=0){
            if (casillas[fila+1][columna-1].contenido==100){
                cantidadDeBombas++;
            }
        }

        if ( columna - 1 >=0){
            if (casillas[fila][columna-1].contenido==100){
                cantidadDeBombas++;
            }
        }


        return cantidadDeBombas;
    }

    private void recorrer(int fila, int columna){

        if (fila>=0 && fila <8 && columna>=0 && columna<8){
            if (casillas[fila][columna].contenido == 0){

                casillas[fila][columna].destapada = true;
                casillas[fila][columna].contenido = 50;

                recorrer(fila-1, columna-1);
                recorrer(fila-1,columna);
                recorrer(fila-1,columna+1);
                recorrer(fila,columna+1);
                recorrer(fila+1, columna+1);
                recorrer(fila+1, columna);
                recorrer(fila+1, columna-1);
                recorrer(fila, columna-1);

            } else if (casillas[fila][columna].contenido<=8){
                casillas[fila][columna].destapada = true;
            }
        }


    }






    public class Tablero extends View {


        public Tablero(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas){

            canvas.drawRGB(0,0,0);

            int ancho = 0;
            if (canvas.getWidth()<canvas.getHeight()){
                ancho = fondo.getWidth();
            } else {
                ancho = fondo.getHeight();
            }

            int anchoCasilla = ancho/8;


            Paint paint = new Paint();
            paint.setTextSize(40);

            Paint paint2 = new Paint();
            paint2.setTextSize(40);
            paint2.setTypeface(Typeface.DEFAULT_BOLD);
            paint2.setARGB(255,0,0,255);

            Paint paintlinea1 = new Paint();
            paintlinea1.setARGB(255,255,255,255);

            int filaActual = 0;

            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8 ; j++){
                    casillas[i][j].fijarCoordenadasXY(j * anchoCasilla, filaActual, anchoCasilla);

                    if (casillas[i][j].destapada){
                        paint.setARGB(255,153,153,153);
                    } else {
                        paint.setARGB(153, 204,204,204);
                    }

                    canvas.drawRect(j*anchoCasilla, filaActual, j*anchoCasilla+anchoCasilla-1,
                            filaActual+anchoCasilla-2, paint);
                    canvas.drawLine(j*anchoCasilla, filaActual, j*anchoCasilla+anchoCasilla,
                            filaActual, paintlinea1);
                    canvas.drawLine(j*anchoCasilla + anchoCasilla-1, filaActual,
                            j*anchoCasilla+anchoCasilla-1, filaActual+anchoCasilla, paintlinea1);

                    //En este caso, la casilla no tiene mina, pero alguno de sus vecinos si.
                    if (casillas[i][j].contenido>=1 &&
                            casillas[i][j].contenido<8 &&
                            casillas[i][j].destapada){
                        canvas.drawText(
                                String.valueOf(casillas[i][j].contenido),
                                j*anchoCasilla + (anchoCasilla/2)-8,
                                filaActual + anchoCasilla/2,
                                paint2);
                    }

                    //En este caso, la casilla tiene una mina
                    if (casillas[i][j].contenido == 100 &&
                            casillas[i][j].destapada){
                        Paint bomba = new Paint();
                        bomba.setARGB(255,255,0,0);
                        canvas.drawCircle(
                                j*anchoCasilla+anchoCasilla/2,
                                filaActual+anchoCasilla/2,
                                20,
                                bomba);
                    }
                }
                filaActual += anchoCasilla;
            }
        }
    }



}
