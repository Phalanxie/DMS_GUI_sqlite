package com.swdprojects.romkaicode.dmsgui.model;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
    Roberto Ruiz,CEN3024C, 3/30/2025
    Software Development I
    DatabaseConnection: this class handles the connectivity of a loaded SQLite database
* */
public class DatabaseConnection
{

    private static String currentDBPath = "";

    /*
     * setDatabasePath: Opens a file chooser for the user to select an SQLite database file
     * args: Window owner
     * return: boolean
     */
    public static boolean setDatabasePath(Window owner)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select SQLite Database");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQLite Database", "*.db", "*.sqlite", "*.sqlite3"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(owner);

        if (selectedFile == null) {
            return false; // User cancelled
        }

        currentDBPath = selectedFile.getAbsolutePath();
        return testConnection();
    }


    /*
     * getConnection: Establishes and returns a connection to the SQLite database
     * args: none
     * return: Connection
     */
    public static Connection getConnection() throws SQLException
    {
        if(currentDBPath == null || currentDBPath.isEmpty())
        {
            throw new SQLException("No database path specified");
        }
        String URL = "jdbc:sqlite:"+currentDBPath;
        return DriverManager.getConnection(URL);
    }


    /*
     * testConnection: Checks if a connection to the database can be established
     * args: none
     * return: boolean
     */
    public static boolean testConnection()
    {
        try (Connection conn = getConnection())
        {
            return conn.isValid(2); // Test connection with 2 second timeout
        } catch (SQLException e)
        {
            return false;
        }
    }

    /*
     * getCurrentDbPath: Retrieves the current database file path.
     * args: none
     * return: String - the absolute path of the selected database file.
     */
    public static String getCurrentDbPath() {
        return currentDBPath;
    }

    /*
     * initializeDatabase: Creates the 'films' table if it does not exist in the database
     * args: none
     * return: none
     */
    public static void initializeDatabase()
    {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS films (" +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT, " +
                    "description TEXT, " +
                    "price REAL, " +
                    "releaseYear INTEGER, " +
                    "runtime INTEGER)";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


