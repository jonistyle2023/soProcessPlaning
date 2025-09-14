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
        procesos.sort(Comparator.comparingInt(Proceso::getTiempoLlegada));

        int tiempoActual = 0;
        for (Proceso p : procesos) {
            if (tiempoActual < p.getTiempoLlegada()) {
                tiempoActual = p.getTiempoLlegada();
            }

            // Registrar el tramo de ejecución
            p.addTiempoComienzo(tiempoActual);
            p.setTiempoComienzo(tiempoActual);

            tiempoActual += p.getTiempoEjecucionOriginal();

            p.addTiempoFin(tiempoActual);
            p.setTiempoFin(tiempoActual);

            p.setTiempoRetorno(p.getTiempoFin() - p.getTiempoLlegada());
            p.setTiempoEspera(p.getTiempoRetorno() - p.getTiempoEjecucionOriginal());
        }
        return procesos;
    }
}
