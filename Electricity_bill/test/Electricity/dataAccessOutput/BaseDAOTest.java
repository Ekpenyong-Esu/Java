package Electricity.dataAccessOutput;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseDAOTest {

    // Concrete implementation of BaseDAO for testing
    private static class TestBaseDAO extends BaseDAO {
        // Expose protected methods for testing
        public Connection getConnectionForTest() throws SQLException {
            return super.getConnection();
        }
        
        public void closeResultSetForTest(ResultSet rs) {
            super.closeResultSet(rs);
        }
        
        public void closeStatementForTest(Statement stmt) {
            super.closeStatement(stmt);
        }
        
        public void closePreparedStatementForTest(PreparedStatement pstmt) {
            super.closePreparedStatement(pstmt);
        }
        
        public void closeConnectionForTest(Connection conn) {
            super.closeConnection(conn);
        }
        
        public void closeResourcesStmtForTest(Connection conn, Statement stmt, ResultSet rs) {
            super.closeResources(conn, stmt, rs);
        }
        
        public void closeResourcesPstmtForTest(Connection conn, PreparedStatement pstmt, ResultSet rs) {
            super.closeResources(conn, pstmt, rs);
        }
    }
    
    private TestBaseDAO baseDAO;
    
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        baseDAO = new TestBaseDAO();
    }

    @Test
    public void testCloseResultSet() throws SQLException {
        // Arrange
        
        // Act
        baseDAO.closeResultSetForTest(mockResultSet);
        
        // Assert
        verify(mockResultSet).close();
    }
    
    @Test
    public void testCloseResultSetHandlesNull() {
        // Act - This should not throw an exception
        baseDAO.closeResultSetForTest(null);
    }
    
    @Test
    public void testCloseResultSetHandlesException() throws SQLException {
        // Arrange
        doThrow(new SQLException("Test exception")).when(mockResultSet).close();
        
        // Act - This should not throw an exception outside the method
        baseDAO.closeResultSetForTest(mockResultSet);
        
        // Assert
        verify(mockResultSet).close();
    }
    
    @Test
    public void testCloseStatement() throws SQLException {
        // Act
        baseDAO.closeStatementForTest(mockStatement);
        
        // Assert
        verify(mockStatement).close();
    }
    
    @Test
    public void testCloseStatementHandlesNull() {
        // Act - This should not throw an exception
        baseDAO.closeStatementForTest(null);
    }
    
    @Test
    public void testClosePreparedStatement() throws SQLException {
        // Act
        baseDAO.closePreparedStatementForTest(mockPreparedStatement);
        
        // Assert
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void testClosePreparedStatementHandlesNull() {
        // Act - This should not throw an exception
        baseDAO.closePreparedStatementForTest(null);
    }
    
    @Test
    public void testCloseConnection() throws SQLException {
        // Act
        baseDAO.closeConnectionForTest(mockConnection);
        
        // Assert
        verify(mockConnection).close();
    }
    
    @Test
    public void testCloseConnectionHandlesNull() {
        // Act - This should not throw an exception
        baseDAO.closeConnectionForTest(null);
    }
    
    @Test
    public void testCloseResourcesStmt() throws SQLException {
        // Act
        baseDAO.closeResourcesStmtForTest(mockConnection, mockStatement, mockResultSet);
        
        // Assert - verify resources are closed in the correct order
        verify(mockResultSet).close();
        verify(mockStatement).close();
        verify(mockConnection).close();
    }
    
    @Test
    public void testCloseResourcesPstmt() throws SQLException {
        // Act
        baseDAO.closeResourcesPstmtForTest(mockConnection, mockPreparedStatement, mockResultSet);
        
        // Assert - verify resources are closed in the correct order
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
        verify(mockConnection).close();
    }
}