package com.swdprojects.romkaicode.dmsgui.view;

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
    DeleteFilmView.java: this class handles the UI logic for the "Delete Film" window for user functionality to delete
    films
* */
public class DeleteFilmView extends Stage
{
    TextField idField = new TextField();
    private Button submitButton = new Button("Submit");


    public DeleteFilmView()
    {
        //setup for window styling, positioning, and labels
        setTitle("Delete Film");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
                new Label("ID:"), idField,
                submitButton
        );
        setScene(new Scene(layout, 250, 150));
    }

    /*
     * idCollector: Retrieves the film ID entered by the user as an int
     * args: none
     * return: int of the entered film ID
     */
    public int idCollector() throws NumberFormatException
    {
        return Integer.parseInt(idField.getText());

    }

    /*
     * SubmitHandler: Sets an event handler for the submit button.
     * args: Runnable handler for the function to be executed when the submit button is clicked
     * return: none
     */
    public void SubmitHandler(Runnable handler)
    {
        submitButton.setOnAction(e -> handler.run());
    }

    /*
     * clearFields: Clears the input field for the film ID
     * args: none
     * return: none
     */
    public void clearFields()
    {
        idField.clear();
    }

}
