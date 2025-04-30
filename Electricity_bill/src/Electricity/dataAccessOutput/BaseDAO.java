package Electricity.dataAccessOutput;

import Electricity.util.Conn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Base Data Access Object class providing common database operations.
 * All specific DAOs inherit from this class.
 */
public abstract class BaseDAO {

    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    protected Connection getConnection() throws SQLException {
        Conn conn = new Conn();
        return conn.connection;
    }

    /**
     * Close a result set
     * @param rs ResultSet to close
     */
    protected void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println("Error closing ResultSet: " + e.getMessage());
            }
        }
    }

    /**
     * Close a statement
     * @param stmt Statement to close
     */
    protected void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Error closing Statement: " + e.getMessage());
            }
        }
    }

    /**
     * Close a prepared statement
     * @param pstmt PreparedStatement to close
     */
    protected void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }

    /**
     * Close a connection
     * @param conn Connection to close
     */
    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing Connection: " + e.getMessage());
            }
        }
    }

    /**
     * Close all database resources
     * @param conn Connection to close
     * @param stmt Statement to close
     * @param rs ResultSet to close
     */
    protected void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(stmt);
        closeConnection(conn);
    }

    /**
     * Close database resources (PreparedStatement version)
     * @param conn Connection to close
     * @param pstmt PreparedStatement to close
     * @param rs ResultSet to close
     */
    protected void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        closeResultSet(rs);
        closePreparedStatement(pstmt);
        closeConnection(conn);
    }
}