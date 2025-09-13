package algorithms;

import model.Proceso;
import java.util.*;

/**
 * Algoritmo Ciclico Round Robin, una forma de planificacion de procesos en sistemas operativos.
 * Cada proceso recibe una cantidad fija de tiempo (quantum) para ejecutarse en cada ciclo.
 * Si un proceso no termina en su quantum, se coloca al final de la cola para esperar su próximo turno.
 */

public class RoundRobin implements Planificador {

    /**
     * El quantum es el intervalo de tiempo que cada proceso recibe para ejecutarse.
     * - quantum pequeño: Refleja más cambios de conteto, mayor equidad, pero puede aumentar la sobrecarga del sistema.
     * - quantum grande: Menos cambios de contexto, los processo pueden terminar en un solo turno, pero se parece más a FCFS
     * y puede perjudicar la respuesta de procesos cortos.
     */
    private int quantum = 2; // Se puede ajustar el quantum según lo necesites

    public RoundRobin() {
    }

    public RoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public List<Proceso> ejecutar(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = new ArrayList<>();
        for (Proceso p : procesosOriginales) {
            procesos.add(new Proceso(p.getNombre(), p.getTiempoEjecucion(), p.getTiempoLlegada()));
        }

        Queue<Proceso> cola = new LinkedList<>();
        int tiempo = 0;
        List<Proceso> completados = new ArrayList<>();
        procesos.sort(Comparator.comparingInt(Proceso::getTiempoLlegada));
        int index = 0;

        while (!cola.isEmpty() || index < procesos.size()) {
            while (index < procesos.size() && procesos.get(index).getTiempoLlegada() <= tiempo) {
                cola.add(procesos.get(index));
                index++;
            }
            if (cola.isEmpty()) {
                tiempo++;
                continue;
            }
            Proceso actual = cola.poll();
            if (actual.getTiempoComienzo() == 0 && tiempo >= actual.getTiempoLlegada()) {
                actual.setTiempoComienzo(tiempo);
            }
            int ejecucion = Math.min(quantum, actual.getTiempoEjecucion());
            actual.setTiempoEjecucion(actual.getTiempoEjecucion() - ejecucion);
            tiempo += ejecucion;

            while (index < procesos.size() && procesos.get(index).getTiempoLlegada() <= tiempo) {
                cola.add(procesos.get(index));
                index++;
            }

            if (actual.getTiempoEjecucion() == 0) {
                actual.setTiempoFin(tiempo);
                actual.setTiempoRetorno(actual.getTiempoFin() - actual.getTiempoLlegada());
                actual.setTiempoEspera(actual.getTiempoRetorno() - actual.getTiempoEjecucionOriginal());
                completados.add(actual);
            } else {
                cola.add(actual);
            }
        }
        completados.sort(Comparator.comparingInt(Proceso::getTiempoLlegada));
        return completados;
    }
}