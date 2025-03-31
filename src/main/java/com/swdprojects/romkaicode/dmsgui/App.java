package com.swdprojects.romkaicode.dmsgui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.swdprojects.romkaicode.dmsgui.controller.*;
import com.swdprojects.romkaicode.dmsgui.model.*;
import com.swdprojects.romkaicode.dmsgui.view.*;

/*
    Roberto Ruiz,CEN3024C, 3/30/2025
    Software Development I
    App.java: this class serves as the launching point of the main application. The application will let the user
    add, delete, display, and update film title. It will also show the total runtime of all movies combined.
* */

public class App extends Application
{
    /*
     * start: JavaFX start function that is the main starting point of the application
     * args: Stage object
     * return: none
     * */
    @Override
    public void start(Stage primaryStage)
    {
        //Model classes variables setup
        DatabaseConnection dbService = new DatabaseConnection();
        DatabaseConnection.initializeDatabase();
        FilmModel filmModel = new FilmModel();

        //View classes variables setup
        MainView mainView = new MainView();
        AddFilmView addView = new AddFilmView();
        UpdateFilmView updateView = new UpdateFilmView();
        DeleteFilmView deleteView = new DeleteFilmView();

        //Controller classes variables setup
        FilmController filmController = new FilmController(filmModel);
        MainController mainController = new MainController(filmModel, mainView);

        //window styling and setting main window scene
        primaryStage.setTitle("Film Management System");
        Scene scene = new Scene(mainView,1000,600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}