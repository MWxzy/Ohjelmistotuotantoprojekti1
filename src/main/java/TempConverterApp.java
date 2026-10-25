import database.DatabaseManager;
import database.TempRecordDAO;
import database.TemperatureUnitDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Locale;

public class TempConverterApp extends Application {

    private static final String LANGUAGE_PROMPT_TITLE =
            "Select Language / Valitse kieli / Välj språk / 言語の選択";
    private static final String LANGUAGE_PROMPT_HEADER =
            "Select your preferred language\n"
                    + "Valitse haluamasi kieli\n"
                    + "Välj önskat språk\n"
                    + "希望する言語を選択してください";
    private static final String LANGUAGE_PROMPT_CONTENT =
            "Language / Kieli / Språk / 言語:";

    private final TemperatureConverter converter = new TemperatureConverter();
    private final TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();
    private final TempRecordDAO recordDAO = new TempRecordDAO();

    @Override
    public void start(Stage primaryStage) {
        I18n.setLocale(resolveLocale());

        DatabaseManager.initializeDatabase();

        Label titleLabel = new Label(I18n.get("app.title"));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField valueField = new TextField();
        valueField.setPromptText(I18n.get("prompt.enterValue"));

        List<String> unitNames = loadUnitNames();

        ComboBox<String> fromUnit = new ComboBox<>();
        fromUnit.getItems().addAll(unitNames);

        ComboBox<String> toUnit = new ComboBox<>();
        toUnit.getItems().addAll(unitNames);

        if (!unitNames.isEmpty()) {
            fromUnit.setValue(unitNames.get(0));
            toUnit.setValue(unitNames.size() > 1 ? unitNames.get(1) : unitNames.get(0));
        }

        Label resultLabel = new Label(I18n.get("label.result"));
        resultLabel.setStyle("-fx-font-size: 14px;");

        Button convertButton = new Button(I18n.get("button.convert"));

        convertButton.setOnAction(e -> {
            try {
                if (fromUnit.getValue() == null || toUnit.getValue() == null) {
                    resultLabel.setText(I18n.get("error.invalidInput"));
                    return;
                }

                double value = Double.parseDouble(valueField.getText().trim());
                int fromId = fromUnit.getSelectionModel().getSelectedIndex() + 1;
                int toId = toUnit.getSelectionModel().getSelectedIndex() + 1;
                double result = convert(value, fromId, toId);

                resultLabel.setText(I18n.format("result.format", result, toUnit.getValue()));
                recordDAO.saveRecord(value, fromId, toId, result);
            } catch (NumberFormatException ex) {
                resultLabel.setText(I18n.get("error.invalidInput"));
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label(I18n.get("label.value")), 0, 0);
        grid.add(valueField, 1, 0);
        grid.add(new Label(I18n.get("label.from")), 0, 1);
        grid.add(fromUnit, 1, 1);
        grid.add(new Label(I18n.get("label.to")), 0, 2);
        grid.add(toUnit, 1, 2);
        grid.add(convertButton, 1, 3);
        grid.add(resultLabel, 0, 4, 2, 1);

        VBox root = new VBox(15, titleLabel, grid);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 350, 300);
        primaryStage.setTitle(I18n.get("app.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Locale resolveLocale() {
        String requested = System.getProperty("app.locale");
        if (requested == null || requested.isBlank()) {
            requested = System.getenv("APP_LOCALE");
        }
        if (requested == null || requested.isBlank()) {
            for (String argument : getParameters().getRaw()) {
                if (argument.startsWith("--lang=")) {
                    requested = argument.substring("--lang=".length());
                    break;
                }
            }
        }

        if (requested != null && !requested.isBlank()) {
            Language language = Language.fromCode(requested);
            return language != null ? language.getLocale() : Locale.US;
        }

        return promptForLanguage().getLocale();
    }

    private Language promptForLanguage() {
        ChoiceDialog<Language> dialog =
                new ChoiceDialog<>(Language.ENGLISH, Language.values());
        dialog.setTitle(LANGUAGE_PROMPT_TITLE);
        dialog.setHeaderText(LANGUAGE_PROMPT_HEADER);
        dialog.setContentText(LANGUAGE_PROMPT_CONTENT);
        return dialog.showAndWait().orElse(Language.ENGLISH);
    }

    private List<String> loadUnitNames() {
        List<String> names = unitDAO.getAllUnitNames();
        if (names.size() < 3) {
            names = List.of("Celsius", "Fahrenheit", "Kelvin");
        }
        return names.stream().map(this::localizeUnitName).toList();
    }

    private String localizeUnitName(String name) {
        String key = switch (name.toLowerCase(Locale.ROOT)) {
            case "celsius" -> "unit.celsius";
            case "fahrenheit" -> "unit.fahrenheit";
            case "kelvin" -> "unit.kelvin";
            default -> null;
        };
        return key != null && I18n.contains(key) ? I18n.get(key) : name;
    }

    private double convert(double value, int fromUnitId, int toUnitId) {
        double celsius = switch (fromUnitId) {
            case 1 -> value;
            case 2 -> converter.fahrenheitToCelsius(value);
            case 3 -> converter.kelvinToCelsius(value);
            default -> value;
        };
        return switch (toUnitId) {
            case 1 -> celsius;
            case 2 -> converter.celsiusToFahrenheit(celsius);
            case 3 -> celsius + 273.15;
            default -> celsius;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
