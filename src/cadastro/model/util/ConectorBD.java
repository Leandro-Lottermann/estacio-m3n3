package cadastro.model.util;

import java.sql.*;

public class ConectorBD {
    private String connectionUrl =
            "jdbc:sqlserver://localhost:1433;"
                    + "database=Loja;"
                    + "user=Loja;"
                    + "password=Loja;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;"
                    + "loginTimeout=10;";
    private Connection con;
    private PreparedStatement prepared;
    private ResultSet result;

    public ConectorBD() {
        try {
            this.con = DriverManager.getConnection(connectionUrl);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return con;
    }

    public PreparedStatement getPrepared(String sqlQuery) {
        try {
            this.prepared = con.prepareStatement(sqlQuery);
            return prepared;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet  getSelect() {
        try {
            this.result = prepared.executeQuery();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void  execute() {
        try {
            prepared.execute();
            prepared.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeQuerys() {
        try {
            this.result.close();
            this.prepared.close();

        } catch (SQLException e ) {
            throw new RuntimeException(e);
        }

    }

    public void closeConn() {
        try {
            this.con.close();
        } catch (SQLException e ) {
            throw new RuntimeException(e);
        }

    }

}
