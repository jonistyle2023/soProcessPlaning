package algorithms;

import model.Proceso;
import java.util.*;

/**
 * Implementación del algoritmo de planificación Shortest Job First (SJF).
 * Es un algoritmo no apropiativo donde se selecciona el proceso con el tiempo de ejecución más corto entre los que han llegado.
 */

public class SJF implements Planificador {

    @Override
    public List<Proceso> ejecutar(List<Proceso> procesos) {
        // Ordenar por tiempo de llegada primero
        procesos.sort(Comparator.comparingInt(Proceso::getTiempoLlegada));

        List<Proceso> resultado = new ArrayList<>();
        int tiempoActual = 0;
        List<Proceso> colaListos = new ArrayList<>();
        for (Proceso p : procesos) {
            colaListos.add(new Proceso(p.getId(), p.getTiempoEjecucion(), p.getTiempoLlegada()));
        }

        while (!colaListos.isEmpty()) {
            // Tomar Procesos que ya le llegaron
            List<Proceso> disponibles = new ArrayList<>();
            for (Proceso p : colaListos) {
                if (p.getTiempoLlegada() <= tiempoActual) {
                    disponibles.add(p);
                }
            }

            if (disponibles.isEmpty()) {
                tiempoActual++;
                continue;
            }

            // Seleccionar el más corto (tiempo de ejecución menor)
            Proceso elegido = disponibles.stream()
                    .min(Comparator.comparingInt(Proceso::getTiempoEjecucion))
                    .get();

            colaListos.remove(elegido);

            // Calcular tiempos
            elegido.setTiempoComienzo(tiempoActual);
            elegido.setTiempoFin(tiempoActual + elegido.getTiempoEjecucion());
            elegido.setTiempoRetorno(elegido.getTiempoFin() - elegido.getTiempoLlegada());
            elegido.setTiempoEspera(elegido.getTiempoRetorno() - elegido.getTiempoEjecucion());

            // Avanzar reloj
            tiempoActual = elegido.getTiempoFin();

            resultado.add(elegido);

        }
        // Ordenamos el resultado por ID para verlo bonito
        resultado.sort(Comparator.comparing(Proceso::getId));
        return resultado;
    }

}
