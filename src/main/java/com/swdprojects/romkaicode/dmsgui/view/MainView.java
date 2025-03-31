package com.swdprojects.romkaicode.dmsgui.view;

import com.swdprojects.romkaicode.dmsgui.controller.FilmController;
import com.swdprojects.romkaicode.dmsgui.model.Film;
import com.swdprojects.romkaicode.dmsgui.model.FilmModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/*
    Roberto Ruiz,CEN3024C, 3/30/2025
    Software Development I
    MainView: this class controls the GUI functionality of the main window.
* */
public class MainView extends BorderPane
{
    private TableView<Film> filmTable;
    private Button addButton, updateButton, deleteButton,
            showFilmsButton, runtimeButton, loadDBButton;
    private Label runtimeLabel;
    private FilmModel filmModel;
    private final FilmController filmController = new FilmController(filmModel);

    public TableView<Film> getFilmTable() { return filmTable; }
    public Button getAddButton() { return addButton; }
    public Button getDeleteButton() { return deleteButton; }
    public Button getUpdateButton() { return updateButton; }
    public Button getShowFilmsButton() {return showFilmsButton;}
    public Button getLoadDBButton() {return loadDBButton;}
    public Button getRuntimeButton() {return runtimeButton;}
    public FilmController getFilmController() {return filmController;}
    public Label getRuntimeLabel() {return runtimeLabel;}

    public MainView()
    {
        initComponents();
        setupButtonLayout();
    }

    /*
     * setupButtonLayout: Configures the layout for the buttons and adds them to the window
     * args: none
     * return: none
     */
    private void setupButtonLayout()
    {
        //horizontal button layout and positioning
        HBox buttonBar = new HBox(10,
                addButton, updateButton, deleteButton,
                showFilmsButton, loadDBButton, runtimeButton
        );
        buttonBar.setAlignment(Pos.CENTER);

        //lower right anchor for "Total Runtime" text label
        StackPane bottomRightPane = new StackPane(runtimeLabel);
        bottomRightPane.setPadding(new Insets(10));
        bottomRightPane.setAlignment(Pos.BOTTOM_RIGHT);

        this.setTop(buttonBar);
        this.setCenter(filmTable);
        this.setBottom(bottomRightPane);
        this.setPadding(new Insets(15));
        this.setStyle("-fx-background-color: #F4F4F4");


    }

    /*
     * initComponents: Initializes the UI components such as the table and buttons
     * args: none
     * return: none
     */
    private void initComponents()
    {
        filmTable = new TableView<>();

        addButton = createStyledButton("Add Film");
        updateButton = createStyledButton("Update Film");
        deleteButton =  createStyledButton("Delete Film");
        showFilmsButton = createStyledButton("Show Films");
        runtimeButton = createStyledButton("Total Runtime");
        loadDBButton = createStyledButton("Load Database");


        //setup columns for table in main window
        TableColumn<Film, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        TableColumn<Film, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Film, String> descColumn = new TableColumn<>("Description");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Film, Float> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Film, String> releaseColumn = new TableColumn<>("Release Date");
        releaseColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));

        TableColumn<Film, Integer> runtimeColumn = new TableColumn<>("Runtime");
        runtimeColumn.setCellValueFactory(new PropertyValueFactory<>("runtime"));

        filmTable.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn, releaseColumn, runtimeColumn);

        //button setup and styling for each, including padding, color, raduis
        deleteButton.setStyle("-fx-background-color: #FF4444; -fx-text-fill: white;" +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px;" +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        updateButton.setStyle("-fx-background-color: #F2C57C; -fx-text-fill: white;" +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px;" +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        runtimeButton.setStyle("-fx-background-color: #5E5E5E; -fx-text-fill: white;" +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px;" +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        showFilmsButton.setStyle("-fx-background-color: #7FB685; -fx-text-fill: white;" +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px;" +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        //styling for movie runtime label
        runtimeLabel = new Label("Total Runtime: 0 minutes");
        runtimeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        runtimeLabel.setTextFill(Color.DARKBLUE);

    }

    /*
     * showAlert: Displays an alert dialog with a given title and message
     * args: String title, String message
     * return: none
     */
    public void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*
     * createStyledButton: Creates a styled button with the given text
     * args: String text
     * return: Button
     */
    public Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        return button;
    }


    /*
     * refreshFilmTable: Updates the table with a new list of films
     * args: List<Film> films
     * return: none
     */
    public void refreshFilmTable(List<Film> films)
    {

        filmTable.getItems().setAll(films);
    }

    /*
     * updateRuntimeLabel: Updates the total runtime label with the calculated runtime
     * args: none
     * return: none
     */
    public void updateRuntimeLabel()
    {
        int totalRuntime = filmController.getTotalRuntime();

        if (totalRuntime == -1) {
            runtimeLabel.setText("Error calculating runtime");
            runtimeLabel.setTextFill(Color.RED);
        } else {
            runtimeLabel.setText("Total Runtime: " + totalRuntime + " minutes");
            runtimeLabel.setTextFill(Color.DARKBLUE);
        }
    }
}
