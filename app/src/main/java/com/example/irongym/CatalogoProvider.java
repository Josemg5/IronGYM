package com.example.irongym;

import java.util.ArrayList;
import java.util.List;

public class CatalogoProvider {

    public static List<Ejercicio> obtenerCatalogo() {
        List<Ejercicio> catalogo = new ArrayList<>();

        Ejercicio e1 = new Ejercicio();
        e1.nombre = "Press de Banca";
        catalogo.add(e1);

        Ejercicio e2 = new Ejercicio();
        e2.nombre = "Press Inclinado con Mancuernas";
        catalogo.add(e2);

        Ejercicio e3 = new Ejercicio();
        e3.nombre = "Aperturas con Mancuernas";
        catalogo.add(e3);

        Ejercicio e4 = new Ejercicio();
        e4.nombre = "Cruces en Polea";
        catalogo.add(e4);

        Ejercicio e5 = new Ejercicio();
        e5.nombre = "Flexiones de Pecho";
        catalogo.add(e5);

        Ejercicio e6 = new Ejercicio();
        e6.nombre = "Dominadas";
        catalogo.add(e6);

        Ejercicio e7 = new Ejercicio();
        e7.nombre = "Jalón al Pecho";
        catalogo.add(e7);

        Ejercicio e8 = new Ejercicio();
        e8.nombre = "Remo con Barra";
        catalogo.add(e8);

        Ejercicio e9 = new Ejercicio();
        e9.nombre = "Remo en Polea Baja";
        catalogo.add(e9);

        Ejercicio e10 = new Ejercicio();
        e10.nombre = "Pullover en Polea Alta";
        catalogo.add(e10);

        Ejercicio e11 = new Ejercicio();
        e11.nombre = "Curl de Bíceps con Barra";
        catalogo.add(e11);

        Ejercicio e12 = new Ejercicio();
        e12.nombre = "Curl Martillo";
        catalogo.add(e12);

        Ejercicio e13 = new Ejercicio();
        e13.nombre = "Extensión de Tríceps en Polea";
        catalogo.add(e13);

        Ejercicio e14 = new Ejercicio();
        e14.nombre = "Press Francés";
        catalogo.add(e14);

        Ejercicio e15 = new Ejercicio();
        e15.nombre = "Fondos de Tríceps";
        catalogo.add(e15);

        Ejercicio e16 = new Ejercicio();
        e16.nombre = "Sentadillas";
        catalogo.add(e16);

        Ejercicio e17 = new Ejercicio();
        e17.nombre = "Prensa de Piernas";
        catalogo.add(e17);

        Ejercicio e18 = new Ejercicio();
        e18.nombre = "Peso Muerto Rumano";
        catalogo.add(e18);

        Ejercicio e19 = new Ejercicio();
        e19.nombre = "Extensión de Cuádriceps";
        catalogo.add(e19);

        Ejercicio e20 = new Ejercicio();
        e20.nombre = "Curl Femoral";
        catalogo.add(e20);
        return catalogo;
    }
}
