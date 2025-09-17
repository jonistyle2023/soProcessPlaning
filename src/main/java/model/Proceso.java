package model;

import javafx.beans.property.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Proceso {
    // Campos de la clase
    private final StringProperty nombre;
    private final IntegerProperty tiempoLlegada;
    private final IntegerProperty tiempoEjecucion; //
    private int tiempoEjecucionOriginal;
    private int tiempoLlegadaOriginal;

    // Campos de los resultados
    private final IntegerProperty tiempoComienzo = new SimpleIntegerProperty(0);
    private final IntegerProperty tiempoFin = new SimpleIntegerProperty(0);
    private final IntegerProperty tiempoRetorno = new SimpleIntegerProperty(0);
    private final IntegerProperty tiempoEspera = new SimpleIntegerProperty(0);

    private final List<Integer> tiemposComienzo = new ArrayList<>();
    private final List<Integer> tiemposFin = new ArrayList<>();

    // Constructor
    public Proceso(String nombre, int tiempoEjecucion, int tiempoLlegada) {
        this.nombre = new SimpleStringProperty(nombre);
        this.tiempoLlegada = new SimpleIntegerProperty(tiempoLlegada);
        this.tiempoEjecucion = new SimpleIntegerProperty(tiempoEjecucion);
        this.tiempoEjecucionOriginal = tiempoEjecucion;
        this.tiempoLlegadaOriginal = tiempoLlegada;
    }

    public int getTiempoLlegadaOriginal() { return tiempoLlegadaOriginal; }
    public void setTiempoLlegadaOriginal(int t) { this.tiempoLlegadaOriginal = t; }

    // --- Getters y Setters para Propiedades JavaFX ---
    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public StringProperty nombreProperty() { return nombre; }

    public int getTiempoLlegada() { return tiempoLlegada.get(); }
    public void setTiempoLlegada(int tiempoLlegada) { this.tiempoLlegada.set(tiempoLlegada); }
    public IntegerProperty tiempoLlegadaProperty() { return tiempoLlegada; }

    public int getTiempoEjecucion() { return tiempoEjecucion.get(); }
    public void setTiempoEjecucion(int tiempoEjecucion) { this.tiempoEjecucion.set(tiempoEjecucion); }
    public IntegerProperty tiempoEjecucionProperty() { return tiempoEjecucion; }

    public int getTiempoEjecucionOriginal() { return tiempoEjecucionOriginal; }
    public void setTiempoEjecucionOriginal(int t) { this.tiempoEjecucionOriginal = t; }

    public int getTiempoComienzo() { return tiempoComienzo.get(); }
    public void setTiempoComienzo(int t) { this.tiempoComienzo.set(t); }
    public IntegerProperty tiempoComienzoProperty() { return tiempoComienzo; }

    public int getTiempoFin() { return tiempoFin.get(); }
    public void setTiempoFin(int t) { this.tiempoFin.set(t); }
    public IntegerProperty tiempoFinProperty() { return tiempoFin; }

    public int getTiempoRetorno() { return tiempoRetorno.get(); }
    public void setTiempoRetorno(int t) { this.tiempoRetorno.set(t); }
    public IntegerProperty tiempoRetornoProperty() { return tiempoRetorno; }

    public int getTiempoEspera() { return tiempoEspera.get(); }
    public void setTiempoEspera(int t) { this.tiempoEspera.set(t); }
    public IntegerProperty tiempoEsperaProperty() { return tiempoEspera; }

    // El ID es el nombre para simplificar
    public String getId() { return getNombre(); }

    public void addTiempoComienzo(int t) { tiemposComienzo.add(t); }
    public void addTiempoFin(int t) { tiemposFin.add(t); }
    public List<Integer> getTiemposComienzo() { return tiemposComienzo; }
    public List<Integer> getTiemposFin() { return tiemposFin; }

    public String getTiemposComienzoStr() {
        return tiemposComienzo.isEmpty() ? String.valueOf(getTiempoComienzo()) :
                tiemposComienzo.stream().map(String::valueOf).collect(Collectors.joining("-"));
    }
    public String getTiemposFinStr() {
        return tiemposFin.isEmpty() ? String.valueOf(getTiempoFin()) :
                tiemposFin.stream().map(String::valueOf).collect(Collectors.joining("-"));
    }
}
