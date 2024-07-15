import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcExample {
	 private static final String URL = "jdbc:mysql://localhost:3306/LibraryDatabase";
	    private static final String USER = "root";
	    private static final String PASSWORD = "123456";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		try {
            // JDBC sürücüsünü yükle
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Veritabanına bağlan
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            if (connection != null) {
                System.out.println("Veritabanına başarıyla bağlandı!");
                // Bağlantıyı kapat
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC sürücüsü bulunamadı.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanırken hata oluştu.");
            e.printStackTrace();
        }
	}

}
