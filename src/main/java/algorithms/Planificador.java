package main.java.algorithms;

import main.java.model.Proceso;
import java.util.List;

public interface Planificador {
    List<Proceso> ejecutar(List<Proceso> procesos);
}
