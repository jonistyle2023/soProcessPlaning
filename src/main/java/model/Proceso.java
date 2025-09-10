package main.java.model;

public class Proceso {
    private String id;
    private int tiempoEjecucion;
    private int tiempoLlegada;
    private int tiempoComienzo;
    private int tiempoFin;
    private int tiempoRetorno;
    private int tiempoEspera;
    private int tiempoEjecucionOriginal;

    public Proceso(String id, int tiempoEjecución, int tiempoLlegada) {
        this.id = id;
        this.tiempoEjecucion = tiempoEjecución;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoEjecucionOriginal = tiempoEjecución;
    }

    // Getters y Setters


    public String getId() {
        return id;
    }

    public int getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public int getTiempoComienzo() {
        return tiempoComienzo;
    }

    public int getTiempoFin() {
        return tiempoFin;
    }

    public int getTiempoRetorno() {
        return tiempoRetorno;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public int getTiempoEjecucionOriginal() {
        return tiempoEjecucionOriginal;
    }

    public void setTiempoComienzo(int tiempoComienzo) {
        this.tiempoComienzo = tiempoComienzo;
    }

    public void setTiempoFin(int tiempoFin) {
        this.tiempoFin = tiempoFin;
    }

    public void setTiempoRetorno(int tiempoRetorno) {
        this.tiempoRetorno = tiempoRetorno;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public void setTiempoEjecucionOriginal(int tiempoEjecucionOriginal) {
        this.tiempoEjecucionOriginal = tiempoEjecucionOriginal;
    }

    public void setTiempoEjecucion(int tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    @Override
    public String toString() {
        return String.format("Proceso %s: Llegada=%d, Ejecución=%d, Inicio=%d, Fin=%d, Retorno=%d, Espera=%d",
                id, tiempoLlegada, tiempoEjecucion, tiempoComienzo,
                tiempoFin, tiempoRetorno, tiempoEspera);
    }
}
