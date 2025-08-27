package app;

import algorithms.Planificador;
import algorithms.SJF;
import model.Proceso;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Proceso> procesos = new ArrayList<>();
        procesos.add(new Proceso("A", 8, 0));
        procesos.add(new Proceso("B", 4, 1));
        procesos.add(new Proceso("C", 9, 2));
        procesos.add(new Proceso("D", 5, 3));
        procesos.add(new Proceso("W", 2, 4));

        Planificador planificador = new SJF();
        List<Proceso> resultado = planificador.ejecutar(procesos);

        System.out.println("Tabla de Resultados (SJF):");
        for (Proceso p : resultado) {
            System.out.println(p);
        }

    }
}