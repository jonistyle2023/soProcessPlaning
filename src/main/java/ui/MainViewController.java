package ui;

// Importaciones locales
import algorithms.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Proceso;
import model.TramoEjecucion;

// Importaciones de JavaFX
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.*;

// Importaciones para exportar a Excel
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

// Importaciones para exportar a PDF
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;

// Importaciones de Java
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class MainViewController {

    // Componentes FXML
    @FXML private ComboBox<String> algoritmoComboBox;
    @FXML private TableView<Proceso> tablaProcesos;
    @FXML private TableColumn<Proceso, String> colNombre;
    @FXML private TableColumn<Proceso, Integer> colLlegada;
    @FXML private TableColumn<Proceso, Integer> colEjecucion;
    @FXML private TableColumn<Proceso, String> colComienzo;
    @FXML private TableColumn<Proceso, String> colFin;
    @FXML private TableColumn<Proceso, Integer> colRetorno;
    @FXML private TableColumn<Proceso, Integer> colEspera;
    @FXML private HBox ganttBox;

    // Variables
    private final ObservableList<Proceso> listaProcesos = FXCollections.observableArrayList();
    private final Map<String, Planificador> algoritmos = new LinkedHashMap<>();
    private boolean ignorarCambioAlgoritmo = false;

    @FXML
    public void initialize() {
        algoritmos.put ("Aleatorio", new RandomOrder());
        algoritmos.put("Round Robin (Cíclico)", new RoundRobin());
        algoritmos.put("FCFS", new FCFS());
        algoritmos.put("SJF", new SJF());
        algoritmos.put("SRTF", new SRTF());

        algoritmoComboBox.getItems().addAll(algoritmos.keySet());
        algoritmoComboBox.getSelectionModel().selectFirst();
        algoritmoComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (ignorarCambioAlgoritmo) {
                ignorarCambioAlgoritmo = false;
                return;
            }
            if (!listaProcesos.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Esta acción borrará los datos de la tabla. Se recomienda guardar los resultados antes de cambiar de algoritmo.\n¿Desea continuar?\n",
                        ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Advertencia");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        listaProcesos.clear();
                        ganttBox.getChildren().clear();
                    } else {
                        ignorarCambioAlgoritmo = true;
                        algoritmoComboBox.getSelectionModel().select(oldVal);
                    }
                });
            } else {
                listaProcesos.clear();
                ganttBox.getChildren().clear();
            }
        });
        configurarTabla();
    }

    // Configuración de la tabla de procesos
    private void configurarTabla() {
        tablaProcesos.setItems(listaProcesos);
        tablaProcesos.setEditable(true);

        colNombre.setCellValueFactory(data -> data.getValue().nombreProperty());
        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombre.setOnEditCommit(event -> event.getRowValue().setNombre(event.getNewValue()));

        colLlegada.setCellValueFactory(data -> data.getValue().tiempoLlegadaProperty().asObject());
        colLlegada.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colLlegada.setOnEditCommit(event -> event.getRowValue().setTiempoLlegada(event.getNewValue()));

        colEjecucion.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTiempoEjecucionOriginal()).asObject());
        colEjecucion.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colEjecucion.setOnEditCommit(event -> {
            Proceso p = event.getRowValue();
            p.setTiempoEjecucion(event.getNewValue());
            p.setTiempoEjecucionOriginal(event.getNewValue());
        });

        colComienzo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTiemposComienzoStr()));
        colFin.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTiemposFinStr()));
        colRetorno.setCellValueFactory(data -> data.getValue().tiempoRetornoProperty().asObject());
        colEspera.setCellValueFactory(data -> data.getValue().tiempoEsperaProperty().asObject());
    }

    @FXML
    private void onAgregarProceso() {
        int nuevoId = listaProcesos.size() + 1;
        listaProcesos.add(new Proceso("P" + nuevoId, 1, 0));
    }

    @FXML
    private void onLimpiarProcesos() {
        listaProcesos.clear();
        ganttBox.getChildren().clear();
    }

    @FXML
    private void onGenerarAleatorio() {
        Random random = new Random();
        listaProcesos.clear();
        int cantidad = 5 + random.nextInt(4);
        for (int i = 1; i <= cantidad; i++) {
            String nombre = "P" + i;
            int llegada = random.nextInt(10);
            int ejecucion = random.nextInt(10) + 1;
            listaProcesos.add(new Proceso(nombre, ejecucion, llegada));
        }
    }

    // Ejecución del algoritmo seleccionado y actualización de la tabla y diagrama de Gantt
    @FXML
    private void onEjecutar() {
        if (listaProcesos.isEmpty()) {
            mostrarAlerta("No hay procesos para planificar.", Alert.AlertType.WARNING);
            return;
        }
        String algoSeleccionado = algoritmoComboBox.getSelectionModel().getSelectedItem();
        if (algoSeleccionado == null) {
            mostrarAlerta("Por favor, seleccione un algoritmo.", Alert.AlertType.WARNING);
            return;
        }
        List<Proceso> procesosParaEjecutar = listaProcesos.stream()
                .map(p -> new Proceso(p.getNombre(), p.getTiempoEjecucion(), p.getTiempoLlegada()))
                .collect(Collectors.toList());

        Planificador planificador = algoritmos.get(algoSeleccionado);
        List<Proceso> resultado = planificador.ejecutar(procesosParaEjecutar);

        listaProcesos.setAll(resultado);

        if (planificador instanceof SRTF) {
            dibujarGanttSRTF(((SRTF) planificador).getTramos());
        } else {
            dibujarGantt(resultado);
        }
    }

    // Dibujar el diagrama de Gantt
    private void dibujarGantt(List<Proceso> resultado) {
        ganttBox.getChildren().clear();
        if (resultado == null || resultado.isEmpty()) return;

        resultado.sort(Comparator.comparingInt(Proceso::getTiempoComienzo));

        Map<String, String> colores = generarMapaColores(resultado);
        List<StackPane> bloquesGantt = new ArrayList<>();

        int tiempoAnterior = 0;
        for (Proceso p : resultado) {
            // Añadir bloque de ocio si existe
            if (p.getTiempoComienzo() > tiempoAnterior) {
                bloquesGantt.add(crearBloqueGantt("Ocio", tiempoAnterior, p.getTiempoComienzo(), "#E0E0E0"));
            }
            bloquesGantt.add(crearBloqueGantt(p.getNombre(), p.getTiempoComienzo(), p.getTiempoFin(), colores.get(p.getNombre())));
            tiempoAnterior = p.getTiempoFin();
        }
        // Iniciar la animación secuencial
        animarBloquesGantt(bloquesGantt, 0);
    }

    private void dibujarGanttSRTF(List<TramoEjecucion> tramos) {
        ganttBox.getChildren().clear();
        if (tramos == null || tramos.isEmpty()) return;

        List<Proceso> procesosUnicos = listaProcesos.stream().distinct().collect(Collectors.toList());
        Map<String, String> colores = generarMapaColores(procesosUnicos);
        List<StackPane> bloquesGantt = new ArrayList<>();

        int tiempoAnterior = 0;
        for (TramoEjecucion tramo : tramos) {
            if (tramo.getInicio() > tiempoAnterior) {
                bloquesGantt.add(crearBloqueGantt("Ocio", tiempoAnterior, tramo.getInicio(), "#E0E0E0"));
            }
            bloquesGantt.add(crearBloqueGantt(tramo.getId(), tramo.getInicio(), tramo.getFin(), colores.get(tramo.getId())));
            tiempoAnterior = tramo.getFin();
        }
        // Iniciar la animación secuencial
        animarBloquesGantt(bloquesGantt, 0);
    }

    // Animación del dibujo de Gantt
    private StackPane crearBloqueGantt(String nombre, int inicio, int fin, String color) {
        double anchoReal = (fin - inicio) * 25.0; // Ancho final deseado

        Rectangle barra = new Rectangle(0, 40); // Inicia con ancho 0
        barra.setFill(Color.web(color));
        barra.setStroke(Color.BLACK);
        barra.setArcWidth(5); // Esquinas redondeadas
        barra.setArcHeight(5);

        Text textoNombre = new Text(nombre);
        textoNombre.setStyle("-fx-font-weight: bold; -fx-fill: black;");
        Text textoTiempos = new Text(inicio + " - " + fin);
        textoTiempos.setStyle("-fx-fill: black;");

        VBox textBox = new VBox(textoNombre, textoTiempos);
        textBox.setAlignment(Pos.CENTER);
        textBox.setMouseTransparent(true); // Para que los eventos de ratón lleguen a la barra

        StackPane stack = new StackPane(barra, textBox);
        stack.setAlignment(Pos.CENTER);
        stack.setPrefWidth(anchoReal); // Establecer el ancho preferido para el layout
        stack.setMaxWidth(anchoReal); // Y el máximo para que no se estire

        // Guardar el ancho real en una propiedad del StackPane para recuperarlo en la animación
        stack.setUserData(anchoReal);

        // Añadir el tiempo de fin debajo de cada barra
        Text finText = new Text(String.valueOf(fin));
        finText.setStyle("-fx-font-size: 10px; -fx-fill: gray;"); // Estilo para el tiempo de fin
        finText.setTranslateY(25); // Posicionar debajo de la barra
        stack.getChildren().add(finText);
        StackPane.setAlignment(finText, Pos.BOTTOM_CENTER);


        return stack;
    }

    // Animación secuencial de los bloques de Gantt
    private void animarBloquesGantt(List<StackPane> bloques, int index) {
        if (index < bloques.size()) {
            StackPane currentBlock = bloques.get(index);
            Rectangle barra = (Rectangle) currentBlock.getChildren().get(0); // La primera child es la barra
            double targetWidth = (double) currentBlock.getUserData(); // Recuperar el ancho objetivo

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new javafx.animation.KeyValue(barra.widthProperty(), 0)),
                    new KeyFrame(Duration.millis(300), new javafx.animation.KeyValue(barra.widthProperty(), targetWidth))
            );

            timeline.setOnFinished(event -> {
                ganttBox.getChildren().add(currentBlock);
                animarBloquesGantt(bloques, index + 1); // Animar el siguiente bloque
            });
            timeline.play();
        }
    }


    private Map<String, String> generarMapaColores(List<Proceso> procesos) {
        Map<String, String> mapa = new HashMap<>();
        String[] colores = {"#FF6B6B", "#4ECDC4", "#45B7D1", "#FED766", "#9CFFD9", "#B57EDC", "#F6BD60", "#F28482"};
        int colorIndex = 0;
        for (Proceso p : procesos) {
            if (!mapa.containsKey(p.getNombre())) {
                mapa.put(p.getNombre(), colores[colorIndex % colores.length]);
                colorIndex++;
            }
        }
        return mapa;
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo, mensaje, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    // Exportar a Excel
    @FXML
    private void onExportarExcel() {
        if (listaProcesos.isEmpty()) {
            mostrarAlerta("No hay datos en la tabla para exportar.", Alert.AlertType.INFORMATION);
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(tablaProcesos.getScene().getWindow());
        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Procesos");
                Row headerRow = sheet.createRow(0);
                String[] headers = {"Proceso", "Llegada", "Ejecución", "Comienzo", "Fin", "Retorno", "Espera"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }
                for (int i = 0; i < listaProcesos.size(); i++) {
                    Proceso p = listaProcesos.get(i);
                    Row row = sheet.createRow(i + 1);
                    row.createCell(0).setCellValue(p.getNombre());
                    row.createCell(1).setCellValue(p.getTiempoLlegada());
                    row.createCell(2).setCellValue(p.getTiempoEjecucionOriginal());
                    row.createCell(3).setCellValue(p.getTiemposComienzoStr());
                    row.createCell(4).setCellValue(p.getTiemposFinStr());
                    row.createCell(5).setCellValue(p.getTiempoRetorno());
                    row.createCell(6).setCellValue(p.getTiempoEspera());
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al exportar a Excel: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    // Exportar a PDF
    @FXML
    private void onExportarPDF() {
        if (listaProcesos.isEmpty()) {
            mostrarAlerta("No hay datos en la tabla para exportar.", Alert.AlertType.INFORMATION);
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(tablaProcesos.getScene().getWindow());
        if (file != null) {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                document.add(new Paragraph("Tabla de Resultados de Planificación"));
                document.add(new Paragraph(" "));
                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);
                String[] headers = {"Proceso", "Llegada", "Ejecución", "Comienzo", "Fin", "Retorno", "Espera"};
                for (String header : headers) {
                    table.addCell(header);
                }
                for (Proceso p : listaProcesos) {
                    table.addCell(p.getNombre());
                    table.addCell(String.valueOf(p.getTiempoLlegada()));
                    table.addCell(String.valueOf(p.getTiempoEjecucionOriginal()));
                    table.addCell(p.getTiemposComienzoStr());
                    table.addCell(p.getTiemposFinStr());
                    table.addCell(String.valueOf(p.getTiempoRetorno()));
                    table.addCell(String.valueOf(p.getTiempoEspera()));
                }
                document.add((Element) table);
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al exportar a PDF: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}