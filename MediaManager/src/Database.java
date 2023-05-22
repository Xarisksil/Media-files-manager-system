import java.sql.*;


public class Database {

	protected static Connection conn = null;
	protected static Statement stmt = null;
   
	public static void connect() {
		
		String connectionUrl = "jdbc:mariadb://localhost:3306/mediamanager";
		String userDB = "root";
		String passDB = "";
		
        try {
        	 Class.forName("org.mariadb.jdbc.Driver");
            
             System.out.println("Connecting to a selected database...");
             conn = DriverManager.getConnection(
            		 connectionUrl,userDB,passDB);
             System.out.println("Connected database successfully...");
             stmt = conn.createStatement();
         } catch (SQLException se) {
             se.printStackTrace();
         } catch (Exception e) {
             e.printStackTrace();
         }
 	} 

           
	public static void closeConnection() {
		if (conn!=null) {
			try {
				conn.close();
			} catch (Exception ex) {
				System.err.println("ERROR: " + ex.getMessage());
			}
		}
	}
	
	
    //stmt.executeUpdate(sql);
    //stmt.executeQuery
	public static Integer insertRecord(String name, String type) {
		String sql = "INSERT INTO files (name,type) VALUES (?,?)";
		Integer results = 0;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, type);
			results = pstmt.executeUpdate();
			System.out.println("Success with results: " + results);
		} catch (Exception e) {
	        System.err.println("SQL exception: " + e.getMessage()); 
	    }
		
		return results;
	
	}
	
	public static ResultSet getAllRecords() {
		
		String sql = "SELECT * FROM files ORDER BY name ASC";
		ResultSet results = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			results = pstmt.executeQuery();
			System.out.println("Success with results: " + results);
		} catch (Exception e) {
	        System.err.println("SQL exception: " + e.getMessage()); 
	    }
		
		return results;
	
	}
	
	
	public static ResultSet getRecordsBySearch(String searchText) {
		
		String sql = "SELECT * FROM files WHERE name LIKE ? ORDER BY name ASC";
		ResultSet results = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  "%" + searchText + "%");
			results = pstmt.executeQuery();
			System.out.println("Success with results: " + results);
		} catch (Exception e) {
	        System.err.println("SQL exception: " + e.getMessage()); 
	    }
		
		return results;
	
	}
	

	
	 public static boolean deleteRecord(int id) {
	        String sql = "DELETE FROM files WHERE id = ?";

	        try {
	            PreparedStatement pstmt = conn.prepareStatement(sql);

	            // set the corresponding param
	            pstmt.setInt(1, id);
	            if (pstmt.executeUpdate() > 0) {
	            	return true;
	            } else {
	            	return false;
	            }

	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        return false;
	    }

}

