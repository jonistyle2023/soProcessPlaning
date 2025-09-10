package main.java.app;

import javafx.scene.layout.Pane;
import main.java.algorithms.Planificador;
import main.java.algorithms.SJF;
import main.java.algorithms.SRTF;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.java.model.Proceso;
import main.java.model.TramoEjecucion;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {

    @FXML
    private ComboBox<String> algoritmoComboBox;

    @FXML
    private TableView<Proceso> tablaResultados;

    @FXML
    private TableColumn<Proceso, String> colId;
    @FXML
    private TableColumn<Proceso, Number> colEjecucion;
    @FXML
    private TableColumn<Proceso, Number> colLlegada;
    @FXML
    private TableColumn<Proceso, Number> colComienzo;
    @FXML
    private TableColumn<Proceso, Number> colFinal;
    @FXML
    private TableColumn<Proceso, Number> colRetorno;
    @FXML
    private TableColumn<Proceso, Number> colEspera;

    @FXML
    public void initialize() {
        algoritmoComboBox.setItems(FXCollections.observableArrayList("SJF", "SRTF"));
        algoritmoComboBox.getSelectionModel().selectFirst();

        // Configurar columnas
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        colEjecucion.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTiempoEjecucion()));
        colLlegada.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTiempoLlegada()));
        colComienzo.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTiempoComienzo()));
        colFinal.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTiempoFin()));
        colRetorno.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTiempoRetorno()));
        colEspera.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTiempoEspera()));
    }

    @FXML
    public void onEjecutar() {
        String algoritmo = algoritmoComboBox.getSelectionModel().getSelectedItem();

        List<Proceso> procesos = new ArrayList<>();
        procesos.add(new Proceso("A", 8, 0));
        procesos.add(new Proceso("B", 4, 1));
        procesos.add(new Proceso("C", 9, 2));
        procesos.add(new Proceso("D", 5, 3));
        procesos.add(new Proceso("E", 2, 4));

        if ("SRTF".equals(algoritmo)) {
            SRTF planificador = new SRTF();
            List<Proceso> resultado = planificador.ejecutar(procesos);
            tablaResultados.setItems(FXCollections.observableArrayList(resultado));
            dibujarGanttSRTF(planificador.getTramos());
        } else {
            Planificador planificador = new SJF();
            List<Proceso> resultado = planificador.ejecutar(procesos);
            tablaResultados.setItems(FXCollections.observableArrayList(resultado));
            dibujarGantt(resultado);
        }
    }

    @FXML
    private Pane ganttPane;
    private void dibujarGantt(List<Proceso> procesos) {
        ganttPane.getChildren().clear();
        if (procesos.isEmpty()) return;

        // Escala: 20 px por unidad de tiempo (puedes ajustar)
        double escala = 20.0;
        double altoBarra = 30.0;
        double yBase = 40.0;

        // Colores base
        String[] colores = {"#ff6666", "#66ff66", "#6666ff", "#ffcc66", "#66ccff", "#cc66ff"};
        int colorIndex = 0;

        // Ordenar por tiempo de comienzo para pintar en orden
        procesos.sort(Comparator.comparingInt(Proceso::getTiempoComienzo));

        for (Proceso p : procesos) {
            double x = p.getTiempoComienzo() * escala;
            double ancho = (p.getTiempoFin() - p.getTiempoComienzo()) * escala;
            String color = colores[colorIndex % colores.length];
            colorIndex++;

            javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(x, yBase, ancho, altoBarra);
            rect.setFill(javafx.scene.paint.Paint.valueOf(color));
            rect.setStroke(javafx.scene.paint.Color.BLACK);

            javafx.scene.text.Text label = new javafx.scene.text.Text(x + ancho / 2 - 5, yBase + altoBarra / 2 + 5, p.getId());
            ganttPane.getChildren().addAll(rect, label);

            // Añadir marcas de tiempo (inicio y fin)
            javafx.scene.text.Text tInicio = new javafx.scene.text.Text(x, yBase + altoBarra + 20, String.valueOf(p.getTiempoComienzo()));
            javafx.scene.text.Text tFin = new javafx.scene.text.Text(x + ancho, yBase + altoBarra + 20, String.valueOf(p.getTiempoFin()));
            ganttPane.getChildren().addAll(tInicio, tFin);
        }
    }

    private void dibujarGanttSRTF(List<TramoEjecucion> tramos) {
        ganttPane.getChildren().clear();
        if (tramos.isEmpty()) return;

        double escala = 20.0;
        double altoBarra = 30.0;
        double yBase = 40.0;

        Map<String, String> colorMap = new HashMap<>();
        String[] colores = {"#ff6666","#66ff66","#6666ff","#ffcc66","#66ccff","#cc66ff"};
        AtomicInteger colorIndex = new AtomicInteger();

        for (TramoEjecucion t : tramos) {
            String color = colorMap.computeIfAbsent(t.getId(), k -> colores[colorIndex.getAndIncrement() % colores.length]);
            double x = t.getInicio() * escala;
            double ancho = (t.getFin() - t.getInicio()) * escala;

            javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(x, yBase, ancho, altoBarra);
            rect.setFill(javafx.scene.paint.Paint.valueOf(color));
            rect.setStroke(javafx.scene.paint.Color.BLACK);

            javafx.scene.text.Text label = new javafx.scene.text.Text(x + ancho / 2 - 5, yBase + altoBarra / 2 + 5, t.getId());
            javafx.scene.text.Text tiempoInicio = new javafx.scene.text.Text(x, yBase + altoBarra + 20, String.valueOf(t.getInicio()));
            javafx.scene.text.Text tiempoFin = new javafx.scene.text.Text(x + ancho, yBase + altoBarra + 20, String.valueOf(t.getFin()));

            ganttPane.getChildren().addAll(rect, label, tiempoInicio, tiempoFin);
        }
    }
}
