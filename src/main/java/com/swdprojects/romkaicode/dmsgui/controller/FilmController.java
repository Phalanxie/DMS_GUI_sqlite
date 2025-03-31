package com.swdprojects.romkaicode.dmsgui.controller;
import com.swdprojects.romkaicode.dmsgui.model.DatabaseConnection;
import com.swdprojects.romkaicode.dmsgui.model.Film;
import com.swdprojects.romkaicode.dmsgui.model.FilmModel;
import com.swdprojects.romkaicode.dmsgui.view.AddFilmView;
import com.swdprojects.romkaicode.dmsgui.view.DeleteFilmView;
import com.swdprojects.romkaicode.dmsgui.view.UpdateFilmView;

import java.io.*;
import java.sql.*;
import java.util.*;

/*
    Roberto Ruiz,CEN3024C, 3/30/2025
    Software Development I
    FilmController: this class controls the main logic and validation of the films handled through the entire application
* */
public class FilmController
{
    private final FilmModel filmModel;

    private final AddFilmView addView;
    private final UpdateFilmView updateView;
    private final DeleteFilmView deleteView;


    public FilmController(FilmModel filmModel)
    {
        this.filmModel = filmModel;
        this.deleteView = new DeleteFilmView();
        this.addView = new AddFilmView();
        this.updateView = new UpdateFilmView();
    }

    /*
     * validateID: checks if the ID format is valid
     * args: ID input
     * return: boolean value
     * */
    private boolean validateFilmId(int id) {
        if (id <= 0) return false;
        if (id < 1000 || id > 9999) return false; // If you need 4-digit IDs
        return true;
    }

    /*
     * validateID: checks if the string input format is valid
     * args: string input
     * return: boolean value
     * */
    private boolean validateString(String input)
    {
        return input != null && !input.trim().isEmpty();
    }

    /*
     * validateID: checks if the float input format is valid
     * args: float
     * return: boolean value
     * */
    private boolean validatePrice(float price)
    {
        return price > 0;
    }

    /*
     * validateID: checks if the int input format is valid and a positive number
     * args: float
     * return: boolean value
     * */
    private boolean validatePositiveInt(int value)
    {
        return value > 0;
    }


    /*
     * filmExists: checks if the film inputted is valid in the sqlite database
     * args: int filmID, Connection conn
     * return: boolean value
     * */
    private boolean filmExists(int filmID, Connection conn) throws SQLException
    {
        String checkSql = "SELECT COUNT(*) FROM films WHERE id = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql))
        {
            checkStmt.setInt(1, filmID);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean validateFilm(Film film)
    {
        return film != null
                && validateFilmId(film.getID())
                && validateString(film.getName())
                && validatePrice(film.getPrice())
                && validatePositiveInt(film.getReleaseDate())
                && validatePositiveInt(film.getRuntime());
    }

    /*
     *  skipLines: workaround for blank lines for invalid film data, skips to next X amount of lines
     * args: Bufferreader, number of lines to skip
     * return: none
     * */
    private void skipLines(BufferedReader reader, int numLines) throws IOException
    {
        for (int i = 0; i < numLines; i++)
        {
            //if line is empty, move onto next line for input
            if (reader.readLine() == null) break; // Stop if EOF
        }
    }

    /*
     *  addFilmManually/addFilmToDatabase: adds film object from manual data entry to the List
     * args: ID,  Name, desciptions ,Price, ReleaseDate, Runtime
     * return: boolean value (validation)
     * */
    public boolean addFilm(Film film)
    {
        // Validate film object first
        if (!validateFilm(film))
        {
            return false;
        }

        String sql = "INSERT INTO films (id, name, description, price, releaseYear, runtime) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            // Check for duplicate first
            if (filmExists(film.getID(), conn))
            {
                return false;
            }

            // Set parameters
            stmt.setInt(1, film.getID());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setFloat(4, film.getPrice());
            stmt.setInt(5, film.getReleaseDate());
            stmt.setInt(6, film.getRuntime());

            return stmt.executeUpdate() > 0; // Returns true if inserted

        } catch (SQLException e)
        {
            // Log properly in production
            System.err.println("Error adding film: " + e.getMessage());
            return false;
        }
    }
    /*
     *  removeFilm: removes film via ID from the List of films
     * args: ID
     * return: boolean value  (validation)
     * */
    public boolean removeFilm(int ID)
    {
        String sql = "DELETE FROM films WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, ID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Film deleted successfully.");
                return true;
            }
            System.out.println("No film found with ID: " + ID);
            return false;

        }
        catch (SQLException e)
        {
            System.err.println("Error deleting film: " + e.getMessage());
            return false;
        }
    }

    /*
     *  updateFilm: changes existing film object attributes
     * args: ID, new Name, new Description, new Price,  new ReleaseDate,  new Runtime
     * return: (commented out)boolean value, now void
     * */
    public boolean updateFilm(int ID, String newName, String newDescription, Float newPrice, Integer newReleaseDate, Integer newRuntime)
    {
        /*for (Film film : films)
        {
            if (film.getID() == ID) {
                if (newName != null && !newName.isEmpty()) film.setName(newName);
                if (newDescription != null && !newDescription.isEmpty()) film.setDescription(newDescription);
                if (newPrice != null && newPrice >= 0) film.setPrice(newPrice);
                if (newReleaseDate != null && newReleaseDate >= 0) film.setReleaseDate(newReleaseDate);
                if (newRuntime != null && newRuntime > 0) film.setRuntime(newRuntime);
                return true;
            }
        }
        //film not found, exit
        return false;*/

        String sql = "UPDATE films SET name = ?, description = ?, price = ?, releaseYear = ?, runtime = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Check if film exists first
            if (!filmExists(ID, conn)) {
                System.out.println("No film found with ID: " + ID);
                return false;
            }

            pstmt.setString(1, newName);
            pstmt.setString(2, newDescription);
            pstmt.setFloat(3, newPrice);
            pstmt.setInt(4, newReleaseDate);
            pstmt.setInt(5, newRuntime);
            pstmt.setInt(6, ID);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Return true if at least one row was updated

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    /*
     *  getTotalRuntime: collects the total of all films' runtime
     * args: none
     * return: int
     * */
    public int getTotalRuntime()
    {
        String sql = "SELECT SUM(runtime) AS total_runtime FROM films";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next())
            {
                return rs.getInt("total_runtime");
            }
            return 0; // Return 0 if no films exist

        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1; // Return -1 to indicate error
        }
    }

    /*
     *  getAllFilms: returns all film object in a List
     * args: none
     * return: List of film objects List<Film>
     * */
    public List<Film> getAllFilms()
    {

        List<Film> filmList = new ArrayList<>();
        String sql = "SELECT * FROM films";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Film film = new Film(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getFloat("price"),
                        rs.getInt("releaseYear"),
                        rs.getInt("runtime")
                );
                filmList.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filmList;

    }

}