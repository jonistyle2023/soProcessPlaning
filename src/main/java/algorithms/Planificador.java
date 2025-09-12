package algorithms;

import model.Proceso;
import java.util.List;

public interface Planificador {
    List<Proceso> ejecutar(List<Proceso> procesos);
}
