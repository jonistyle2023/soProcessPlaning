package algorithms;

import model.Proceso;
import model.TramoEjecucion;
import java.util.*;

/**
 * Implementación del algoritmo de planificación Shortest Remaining Time First (SRTF).
 * Es un algoritmo apropiativo donde se selecciona el proceso con el tiempo de ejecución restante más corto entre los que han llegado.
 */

public class SRTF implements Planificador {

    private final List<TramoEjecucion> tramos = new ArrayList<>();

    public List<TramoEjecucion> getTramos() {
        return tramos;
    }

    @Override
    public List<Proceso> ejecutar(List<Proceso> procesos) {
        tramos.clear();
        // Copiamos para no alterar lista original
        List<Proceso> lista = new ArrayList<>();
        for (Proceso p : procesos) {
            lista.add(new Proceso(p.getId(), p.getTiempoEjecucion(), p.getTiempoLlegada()));
        }

        int tiempo = 0;
        int completados = 0;
        Proceso actual = null;
        int inicioRafaga = -1;

        while (completados < lista.size()) {
            // Filtrar procesos que llegaron
            List<Proceso> disponibles = new ArrayList<>();
            for (Proceso p : lista) {
                if (p.getTiempoLlegada() <= tiempo && p.getTiempoEjecucion() > 0) {
                    disponibles.add(p);
                }
            }
            // Escoger el de menor tiempo restante
            disponibles.sort(Comparator.comparingInt(Proceso::getTiempoEjecucion));
            Proceso siguiente = disponibles.isEmpty() ? null : disponibles.getFirst();

            if (siguiente != actual) {
                // Hubo cambio de contexto
                if (actual != null && inicioRafaga >= 0) {
                    tramos.add(new TramoEjecucion(actual.getId(), inicioRafaga, tiempo));
                }
                actual = siguiente;
                inicioRafaga = tiempo;
            }

            // Avanzar el tiempo
            if (siguiente != null) {
                siguiente.setTiempoEjecucion(siguiente.getTiempoEjecucion() - 1);
                if (siguiente.getTiempoEjecucion() == 0) {
                    siguiente.setTiempoFin(tiempo + 1);
                    completados++;
                }
            }
            tiempo++;
        }
        // Cerrar último tramo
        if (actual != null && inicioRafaga >= 0) {
            tramos.add(new TramoEjecucion(actual.getId(), inicioRafaga, tiempo));
        }

        // Calcular tiempos derivados
        for (Proceso p : lista) {
            p.setTiempoRetorno(p.getTiempoFin() - p.getTiempoLlegada());
            p.setTiempoEspera(p.getTiempoRetorno() - p.getTiempoEjecucionOriginal());
            p.setTiempoComienzo(p.getTiempoFin() - p.getTiempoEjecucionOriginal() - p.getTiempoEspera());
        }

        // Ordenamos el resultado por ID para verlo bonito
        lista.sort(Comparator.comparing(Proceso::getId));
        return lista;
    }
}