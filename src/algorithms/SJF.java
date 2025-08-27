package algorithms;

import model.Proceso;
import java.util.*;

public class SJF implements Planificador {

    @Override
    public List<Proceso> ejecutar(List<Proceso> procesos) {
        // Ordenar por tiempo de llegada primero
        procesos.sort(Comparator.comparingInt(Proceso::getTiempoLlegada));

        List<Proceso> resultado = new ArrayList<>();
        int tiempoActual = 0;
        List<Proceso> colaListos = new ArrayList<>(procesos);

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
                    .min(Comparator.comparingInt(Proceso::getTiempoEjecución))
                    .get();

            colaListos.remove(elegido);

            // Calcular tiempos
            elegido.setTiempoComienzo(tiempoActual);
            elegido.setTiempoFin(tiempoActual + elegido.getTiempoEjecución());
            elegido.setTiempoRetorno(elegido.getTiempoFin() - elegido.getTiempoLlegada());
            elegido.setTiempoEspera(elegido.getTiempoRetorno() - elegido.getTiempoEjecución());

            // Avanzar reloj
            tiempoActual = elegido.getTiempoFin();

            resultado.add(elegido);

        }
        return resultado;
    }

}
