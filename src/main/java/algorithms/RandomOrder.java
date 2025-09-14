package algorithms;

import model.Proceso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class RandomOrder implements Planificador {

    @Override
    public List<Proceso> ejecutar(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = new ArrayList<>();
        for (Proceso p : procesosOriginales) {
            procesos.add(new Proceso(p.getNombre(), p.getTiempoEjecucion(), p.getTiempoLlegada()));
        }

        Collections.shuffle(procesos);

        int tiempo = 0;
        for (Proceso p : procesos) {
            tiempo = Math.max(tiempo, p.getTiempoLlegada());
            p.addTiempoComienzo(tiempo); // Registrar comienzo
            p.setTiempoComienzo(tiempo); // Para compatibilidad
            tiempo += p.getTiempoEjecucion();
            p.addTiempoFin(tiempo); // Registrar fin
            p.setTiempoFin(tiempo); // Para compatibilidad
            p.setTiempoRetorno(p.getTiempoFin() - p.getTiempoLlegada());
            p.setTiempoEspera(p.getTiempoRetorno() - p.getTiempoEjecucionOriginal());
        }
        return procesos;
    }
}
