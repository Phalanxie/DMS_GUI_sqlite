package com.swdprojects.romkaicode.dmsgui.controller;

import com.swdprojects.romkaicode.dmsgui.model.DatabaseConnection;
import com.swdprojects.romkaicode.dmsgui.model.Film;
import com.swdprojects.romkaicode.dmsgui.model.FilmModel;
import com.swdprojects.romkaicode.dmsgui.view.AddFilmView;
import com.swdprojects.romkaicode.dmsgui.view.DeleteFilmView;
import com.swdprojects.romkaicode.dmsgui.view.MainView;
import com.swdprojects.romkaicode.dmsgui.view.UpdateFilmView;
import javafx.stage.Window;

import java.util.List;

/*
    Roberto Ruiz,CEN3024C, 3/30/2025
    Software Development I
    maincontroller.java: this class handles the logic for the main control between the film model and the main view window
* */
public class MainController
{
    private final FilmModel filmModel;
    private final MainView mainView;
    private FilmController filmController;
    private final AddFilmView addFilmView;
    private final DeleteFilmView deleteFilmView;
    private final UpdateFilmView updateFilmView;

    public MainController(FilmModel filmModel, MainView mainView)
    {
        this.filmModel = filmModel;
        this.mainView = mainView;
        this.filmController = new FilmController(filmModel);
        this.addFilmView = new AddFilmView();
        this.deleteFilmView = new DeleteFilmView();
        this.updateFilmView = new UpdateFilmView();
        initialize();
    }

    /*
     * initialize: Sets up event handlers and loads initial data
     * args: none
     * return: none
     */
    private void initialize()
    {
        setupEventHandlers();
        loadInitialData();
    }


    /*
     * setupEventHandlers: Configures button event handlers for the main view.
     * args: none
     * return: none
     */
    private void setupEventHandlers()
    {
        mainView.getAddButton().setOnAction(e -> handleAddFilm());
        mainView.getUpdateButton().setOnAction(e -> handleUpdateFilm());
        mainView.getDeleteButton().setOnAction(e -> handleDeleteFilm());
        mainView.getLoadDBButton().setOnAction(e -> handleLoadDB());
        mainView.getShowFilmsButton().setOnAction(event -> handleShowFilms());
        mainView.getRuntimeButton().setOnAction(e -> updateRuntimeDisplay());

    }

    /*
     * loadInitialData: Loads and displays initial film data in the table
     * args: none
     * return: none
     */
    private void loadInitialData()
    {
        mainView.refreshFilmTable(filmController.getAllFilms());
    }


    /*
     * handleAddFilm: Opens the add film view and handles adding a new film
     * args: none
     * return: none
     */
    private void handleAddFilm()
    {
        addFilmView.clearFields();
        addFilmView.SubmitHandler(() -> {
            try {
                Film film = addFilmView.FilmInputCollector();
                if (filmController.addFilm(film))
                {
                    addFilmView.close();
                    mainView.showAlert("Success", "Film added successfully");
                } else {
                    mainView.showAlert("Error", "Film could not be added (possibly duplicate ID)");
                }
            } catch (NumberFormatException e)
            {
                mainView.showAlert("Input Error", "Please enter valid numbers for ID, Price, Year and Runtime");
            }
        });
        addFilmView.showAndWait();
    }

    /*
     * handleDeleteFilm: Opens the delete film view and handles film deletion
     * args: none
     * return: none
     */
    private void handleDeleteFilm()
    {
        deleteFilmView.clearFields();
        deleteFilmView.SubmitHandler(() -> {
            try
            {
                int id = deleteFilmView.idCollector();
                if (filmController.removeFilm(id))
                {
                    deleteFilmView.close();
                    mainView.showAlert("Success", "Film Deleted successfully");
                }
                else {mainView.showAlert("Error", "Film not deleted (not found or invalid ID)");}
            }
            catch (NumberFormatException e)
            {
                mainView.showAlert("Error","Please enter a valid ID");
            }
            }
        );
        deleteFilmView.showAndWait();

    }

    /*
     * handleUpdateFilm: Opens the update film view and handles film updates.
     * args: none
     * return: none
     */
    private void handleUpdateFilm()
    {
        /*steps:
         * - clear textfields
         * - lambda for try-catch of updating a film*/

        updateFilmView.clearFields();
        updateFilmView.SubmitHandler(() -> {
            try
            {
                Film film = updateFilmView.FilmInputCollector();
                if (filmController.updateFilm(film.getID(), film.getName(), film.getDescription(),
                        film.getPrice(), film.getReleaseDate(), film.getRuntime()))
                {
//                    mainView.refreshFilmTable(filmController.getAllFilms());
                    updateFilmView.close();
                    mainView.showAlert("Success", "Film updated successfully");
                }
                else
                {
                    mainView.showAlert("Error", "Film could not be updated");
                }
            }
            catch (NumberFormatException e)
            {
                mainView.showAlert("Input Error", "Please enter valid numbers for ID, Price, Year and Runtime");
            }
        }
        );
        updateFilmView.showAndWait();
    }

    /*
     * updateRuntimeDisplay: Updates the runtime label in the main view.
     * args: none
     * return: none
     */
    private void updateRuntimeDisplay()
    {
        int runtime = filmController.getTotalRuntime();
        mainView.updateRuntimeLabel();
    }

    /*
     * handleLoadDB: Handles selecting and loading a database file.
     * args: none
     * return: none
     */
    private void handleLoadDB()
    {
        //Get the current window for the file dialog
        Window ownerWindow = mainView.getScene().getWindow();

        if (DatabaseConnection.setDatabasePath(ownerWindow)) {
            try {
                //Initialize the database and creates tables if they don't exist
                DatabaseConnection.initializeDatabase();
                List<Film> loadedFilms = filmController.getAllFilms();

                mainView.showAlert("Success",
                        "Database loaded successfully from:\n" +
                                DatabaseConnection.getCurrentDbPath() +
                                "\nLoaded " + loadedFilms.size() + " films");
            } catch (Exception e) {
                mainView.showAlert("Error", "Failed to load database: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            mainView.showAlert("Information", "No database selected or connection failed");
        }
    }

    /*
     * handleShowFilms: Retrieves and updates the film list in the main view
     * args: none
     * return: none
     */
    private void handleShowFilms()
    {
        try
        {
            filmController.getAllFilms();
            mainView.refreshFilmTable(filmController.getAllFilms());
            mainView.showAlert("success", "film list updated successfully!");
        }
        catch (Exception e)
        {
            mainView.showAlert("error","failed to update list");
        }
    }
}
