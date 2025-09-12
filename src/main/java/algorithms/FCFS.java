package algorithms;
import model.Proceso;
import java.util.Comparator;
import java.util.List;

/**
 * Implementación del algoritmo de planificación First-Come, First-Served (FCFS).
 * Es un algoritmo no apropiativo donde los procesos se ejecutan en el orden en que llegan.
 */

public class FCFS implements Planificador {
    @Override
    public List<Proceso> ejecutar(List<Proceso> procesos) {
        // Ordenar la lista de procesos según su tiempo de llegada.
        procesos.sort(Comparator.comparingInt(Proceso::getTiempoLlegada));

        int tiempoActual = 0;
        for (Proceso p : procesos) {
            // Si la CPU está ociosa, el tiempo avanza hasta que llega el siguiente proceso.
            if (tiempoActual < p.getTiempoLlegada()) {
                tiempoActual = p.getTiempoLlegada();
            }

            // Se calculan los tiempos del proceso actual.
            p.setTiempoComienzo(tiempoActual);
            p.setTiempoFin(tiempoActual + p.getTiempoEjecucionOriginal());
            p.setTiempoRetorno(p.getTiempoFin() - p.getTiempoLlegada());
            p.setTiempoEspera(p.getTiempoRetorno() - p.getTiempoEjecucionOriginal());

            // El reloj avanza hasta el momento en que este proceso termina.
            tiempoActual = p.getTiempoFin();
        }
        return procesos;
    }
}
