package model;

import javafx.beans.property.*;

public class Proceso {
    private final StringProperty nombre;
    private final IntegerProperty tiempoLlegada;
    private final IntegerProperty tiempoEjecucion; // Tiempo restante
    private int tiempoEjecucionOriginal; // Tiempo total, no cambia

    // Campos para resultados
    private final IntegerProperty tiempoComienzo = new SimpleIntegerProperty(0);
    private final IntegerProperty tiempoFin = new SimpleIntegerProperty(0);
    private final IntegerProperty tiempoRetorno = new SimpleIntegerProperty(0);
    private final IntegerProperty tiempoEspera = new SimpleIntegerProperty(0);

    public Proceso(String nombre, int tiempoEjecucion, int tiempoLlegada) {
        this.nombre = new SimpleStringProperty(nombre);
        this.tiempoLlegada = new SimpleIntegerProperty(tiempoLlegada);
        this.tiempoEjecucion = new SimpleIntegerProperty(tiempoEjecucion);
        this.tiempoEjecucionOriginal = tiempoEjecucion;
    }

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
}
