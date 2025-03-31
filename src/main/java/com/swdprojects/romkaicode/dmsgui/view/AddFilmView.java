package com.swdprojects.romkaicode.dmsgui.view;
import com.swdprojects.romkaicode.dmsgui.model.Film;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
    Roberto Ruiz,CEN3024C, 3/30/2025
    Software Development I
    AddFilmView.java: this class handles the UI logic for the "Add Film" window for user functionality of adding films
* */
public class AddFilmView extends Stage
{
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField descField = new TextField();
    private final TextField priceField = new TextField();
    private final TextField releaseField = new TextField();
    private final TextField runtimeField = new TextField();
    private final Button submitButton = new Button("Submit");

    public AddFilmView()
    {
        //setup for window styling, positioning, and labels
        setTitle("Add Film");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
                new Label("ID:"), idField,
                new Label("Name:"), nameField,
                new Label("Description:"), descField,
                new Label("Price:"), priceField,
                new Label("Release Year:"), releaseField,
                new Label("Runtime:"), runtimeField,
                submitButton
        );
        setScene(new Scene(layout, 300, 450));
    }


    /*
     * FilmInputCollector: Collects user input and creates a Film object
     * args: none
     * return: Film object
     */
    public Film FilmInputCollector() throws NumberFormatException
    {
        return new Film(
                Integer.parseInt(idField.getText()),
                nameField.getText(),
                descField.getText(),
                Float.parseFloat(priceField.getText()),
                Integer.parseInt(releaseField.getText()),
                Integer.parseInt(runtimeField.getText())
        );

    }

    /*
     * SubmitHandler: Sets an event handler for the submit button of the window
     * args: Runnable handler for the function to be executed when the submit button is clicked
     * return: none
     */
    public void SubmitHandler(Runnable handler) {
        submitButton.setOnAction(e -> handler.run());
    }

    /*
     * clearFields: Clears all input fields in the form
     * args: none
     * return: none
     */
    public void clearFields() {
        idField.clear();
        nameField.clear();
        descField.clear();
        priceField.clear();
        releaseField.clear();
        runtimeField.clear();
    }
}
