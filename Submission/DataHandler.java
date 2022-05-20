package jsp_azure_test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DataHandler {

    private Connection conn;

    // Azure SQL connection credentials
    private String server = "john0387-sql-server.database.windows.net";
    private String database = "cs-dsa-4513-sql-db";
    private String username = "john0387";
    private String password = "hootnanny111.OU";

    // Resulting connection string
    final private String url =
            String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
                    server, database, username, password);

    // Initialize and save the database connection
    private void getDBConnection() throws SQLException {
        if (conn != null) {
            return;
        }

        this.conn = DriverManager.getConnection(url);
    }

    // Perform Query 13
    public ResultSet queryThirteen(int lowerBound, int upperBound) throws SQLException {
        getDBConnection();
        
        final String sqlQuery = 
        		"SELECT * " +
    			"FROM Customer " +
    			"WHERE category BETWEEN ? AND ?;";
        final PreparedStatement stmt = conn.prepareStatement(sqlQuery);
        stmt.setInt(1, lowerBound);
        stmt.setInt(2, upperBound);
        
        return stmt.executeQuery();
    }

    // Perform Query 1
    public boolean queryOne(String customerName, String customerAddress, int customerCategory) throws SQLException {
        getDBConnection();

        final String sqlQuery =
        		"INSERT INTO Customer " + 
        		"VALUES (?, ?, ?);";
        final PreparedStatement stmt = conn.prepareStatement(sqlQuery);       
        stmt.setString(1, customerName);
        stmt.setString(2, customerAddress);
        stmt.setInt(3, customerCategory);
        
        return stmt.executeUpdate() == 1;
    }
}
