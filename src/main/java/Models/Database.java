package Models;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private static final String URL = "jdbc:sqlite:data/app.db";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }
}
