import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryExample {
	
	private static final String URL = "jdbc:mysql://localhost:3306/librarydatabase";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // JDBC sürücüsünü yükle
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Veritabanına bağlan
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL sorgusu
            String sql = "SELECT * FROM tablo_adi";
            preparedStatement = connection.prepareStatement(sql);

            // Sorguyu çalıştır ve sonucu al
            resultSet = preparedStatement.executeQuery();

            // Sonuçları işle
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                // Sonuçları kullan
                System.out.println("ID: " + id + ", Name: " + name);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC sürücüsü bulunamadı.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Veritabanı işlemi sırasında hata oluştu.");
            e.printStackTrace();
        } finally {
            // Kaynakları serbest bırak
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
