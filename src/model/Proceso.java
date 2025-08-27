package model;

public class Proceso {
    private String id;
    private int tiempoEjecución;
    private int tiempoLlegada;
    private int tiempoComienzo;
    private int tiempoFin;
    private int tiempoRetorno;
    private int tiempoEspera;

    public Proceso(String id, int tiempoEjecución, int tiempoLlegada) {
        this.id = id;
        this.tiempoEjecución = tiempoEjecución;
        this.tiempoLlegada = tiempoLlegada;
    }

    // Getters y Setters


    public String getId() {
        return id;
    }

    public int getTiempoEjecución() {
        return tiempoEjecución;
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

    @Override
    public String toString() {
        return String.format("Proceso %s: Llegada=%d, Ejecución=%d, Inicio=%d, Fin=%d, Retorno=%d, Espera=%d",
                id, tiempoLlegada, tiempoEjecución, tiempoComienzo,
                tiempoFin, tiempoRetorno, tiempoEspera);
    }
}
