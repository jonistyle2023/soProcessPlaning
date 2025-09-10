package main.java.model;

public class TramoEjecucion {
    private final String id;
    private final int inicio;
    private final int fin;

    public TramoEjecucion(String id, int inicio, int fin) {
        this.id = id;
        this.inicio = inicio;
        this.fin = fin;
    }

    public String getId() { return id; }
    public int getInicio() { return inicio; }
    public int getFin() { return fin; }
}