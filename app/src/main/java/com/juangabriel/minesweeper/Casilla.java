package com.juangabriel.minesweeper;

/**
 * Created by JuanGabriel on 29/9/16.
 */

public class Casilla {

    public int contenido;
    public boolean destapada;


    private int firstX, firstY, lastX, lastY;

    public void fijarCoordenadasXY(int initX, int fila, int anchoCasilla){
        firstX = initX;
        firstY = fila;
        lastX = firstX+anchoCasilla;
        lastY = firstY+anchoCasilla;
    }

    public boolean dentroDelPixel(int x, int y){
        if (firstX<=x&& x<lastX &&
                firstY<=y && y < lastY){
            return true;
        } else {
            return false;
        }
    }



}
