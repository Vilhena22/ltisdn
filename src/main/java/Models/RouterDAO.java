package Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RouterDAO {
private static RouterDAO instance;
    public RouterDAO() {
        try {
            createTable();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static RouterDAO getInstance() {
        if (instance == null) {
            instance = new RouterDAO();
        }
        return instance;
    }

    public void createTable() throws Exception {
        try (Connection conn = Database.connect()) {
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS routers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    host TEXT NOT NULL,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL,
                    last_used TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
            """);
        }
    }

    public void saveRouter(Router router) throws Exception {
        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("""
                INSERT INTO routers(host, username, password)
                VALUES( ?, ?, ?)
            """);

            stmt.setString(1, router.host);
            stmt.setString(2, router.username);
            stmt.setString(3, router.password);

            stmt.executeUpdate();
        }
    }

    public boolean checkRouter(Router router) throws Exception {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM routers WHERE host = ? AND username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, router.host);
            stmt.setString(2, router.username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Router> getAllRouters() throws Exception {
        List<Router> list = new ArrayList<>();

        try (Connection conn = Database.connect()) {
            ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT * FROM routers ORDER BY last_used DESC");

            while (rs.next()) {
                Router r = new Router();
                r.id = rs.getInt("id");
                r.host = rs.getString("host");
                r.username = rs.getString("username");
                r.password = rs.getString("password");

                list.add(r);
            }
        }
        return list;
    }
}
