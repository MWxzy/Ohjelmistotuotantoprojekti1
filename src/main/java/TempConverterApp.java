import database.DatabaseManager;
import database.TempRecordDAO;
import database.TemperatureUnitDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TempConverterApp extends Application {

    private final TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();
    private final TempRecordDAO recordDAO = new TempRecordDAO();

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.initializeDatabase();

        Label titleLabel = new Label("Temperature Converter");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField valueField = new TextField();
        valueField.setPromptText("Enter value");

        ComboBox<String> fromUnit = new ComboBox<>();
        fromUnit.getItems().addAll(unitDAO.getAllUnitNames());
        fromUnit.setValue("Celsius");

        ComboBox<String> toUnit = new ComboBox<>();
        toUnit.getItems().addAll(unitDAO.getAllUnitNames());
        toUnit.setValue("Fahrenheit");

        Label resultLabel = new Label("Result: ");
        resultLabel.setStyle("-fx-font-size: 14px;");

        Button convertButton = new Button("Convert");

        convertButton.setOnAction(e -> {
            try {
                double value = Double.parseDouble(valueField.getText());
                int fromId = fromUnit.getSelectionModel().getSelectedIndex() + 1;
                int toId = toUnit.getSelectionModel().getSelectedIndex() + 1;
                double result = convert(value, fromId, toId);
                resultLabel.setText(String.format("Result: %.2f %s", result, toUnit.getValue()));
                recordDAO.saveRecord(value, fromId, toId, result);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid input!");
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Value:"), 0, 0);
        grid.add(valueField, 1, 0);
        grid.add(new Label("From:"), 0, 1);
        grid.add(fromUnit, 1, 1);
        grid.add(new Label("To:"), 0, 2);
        grid.add(toUnit, 1, 2);
        grid.add(convertButton, 1, 3);
        grid.add(resultLabel, 0, 4, 2, 1);

        VBox root = new VBox(15, titleLabel, grid);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 350, 300);
        primaryStage.setTitle("Temperature Converter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private double convert(double value, int fromUnitId, int toUnitId) {
        double celsius = switch (fromUnitId) {
            case 1 -> value;
            case 2 -> (value - 32) * 5 / 9;
            case 3 -> value - 273.15;
            default -> value;
        };
        return switch (toUnitId) {
            case 1 -> celsius;
            case 2 -> (celsius * 9 / 5) + 32;
            case 3 -> celsius + 273.15;
            default -> celsius;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}