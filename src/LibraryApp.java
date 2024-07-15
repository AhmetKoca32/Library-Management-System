import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.*;

public class LibraryApp extends JFrame{
	
	// Veritabanı bağlantı bilgileri
	private static final String URL = "jdbc:mysql://localhost:3306/librarydatabase";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

	private JFrame frmLibraryManagementSystem;
	private JTextField titleField;
	private JTextField authorField;
	private JTextField idField;
	private JTextField searchField;

	
	DefaultListModel<String> bookListModel = new DefaultListModel<>();
	JList<String> bookList = new JList<>(bookListModel);
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LibraryApp window = new LibraryApp();
					window.frmLibraryManagementSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LibraryApp() {
		initialize();
	}
	
	
	// Kitap ekleme işlevi
    private void addBook() {
        String title = titleField.getText();
        String author = authorField.getText();

        // Veritabanına bağlan ve sorguyu çalıştır
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO books (title, author) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Book added successfully!");
                } else {
                    JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Failed to add book.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Error: " + ex.getMessage());
        }
    }
    
    // Kitap güncelleme işlevi
    private void updateBook(int bookId, String newTitle, String newAuthor) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "UPDATE books SET title = ?, author = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newTitle);
                preparedStatement.setString(2, newAuthor);
                preparedStatement.setInt(3, bookId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Book updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Failed to update book.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Error connecting to the database.");
        }
    }
    
    // Kitap silme işlevi
    private void deleteBook(int bookId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "DELETE FROM books WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, bookId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Book deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Failed to delete book.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Error connecting to the database.");
        }
    }

    // Kitap arama işlevi
    private void searchBook(String title) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM books WHERE title LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + title + "%");
                ResultSet resultSet = preparedStatement.executeQuery();

                bookListModel.clear(); // Önceki sonuçları temizleyin
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String bookTitle = resultSet.getString("title");
                    String author = resultSet.getString("author");

                    // JList'e ekleme
                    bookListModel.addElement("ID: " + id + ", Title: " + bookTitle + ", Author: " + author);
                    
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Error connecting to the database.");
        }
    }


    
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLibraryManagementSystem = new JFrame();
		frmLibraryManagementSystem.setTitle("Library management System");
		frmLibraryManagementSystem.setBounds(100, 100, 757, 466);
		frmLibraryManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLibraryManagementSystem.getContentPane().setLayout(null);
		
		JLabel titlelabel = new JLabel("Book Title :");
		titlelabel.setBounds(45, 58, 67, 13);
		frmLibraryManagementSystem.getContentPane().add(titlelabel);
		
		titleField = new JTextField();
		titleField.setBounds(145, 55, 96, 19);
		frmLibraryManagementSystem.getContentPane().add(titleField);
		titleField.setColumns(10);
		
		JLabel authorLabel = new JLabel("Author :");
		authorLabel.setBounds(45, 95, 67, 13);
		frmLibraryManagementSystem.getContentPane().add(authorLabel);
		
		authorField = new JTextField();
		authorField.setBounds(145, 92, 96, 19);
		frmLibraryManagementSystem.getContentPane().add(authorField);
		authorField.setColumns(10);
		
		JButton addButton = new JButton("Add Book");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBook();
			}
		});
		addButton.setBounds(302, 58, 105, 50);
		frmLibraryManagementSystem.getContentPane().add(addButton);
		
		JButton updateButton = new JButton("Update Book");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int bookId = Integer.parseInt(idField.getText());
		        String newTitle = titleField.getText();
		        String newAuthor = authorField.getText();
		        updateBook(bookId, newTitle, newAuthor);
				
			}
		});
		updateButton.setBounds(204, 195, 116, 50);
		frmLibraryManagementSystem.getContentPane().add(updateButton);
		
		JButton deleteButton = new JButton("Delete Book");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int bookId = Integer.parseInt(idField.getText());
		        deleteBook(bookId);
				
			}
		});
		deleteButton.setBounds(330, 195, 105, 50);
		frmLibraryManagementSystem.getContentPane().add(deleteButton);
		
		JLabel idText = new JLabel("ID : ");
		idText.setBounds(45, 214, 24, 13);
		frmLibraryManagementSystem.getContentPane().add(idText);
		
		idField = new JTextField();
		idField.setBounds(79, 211, 96, 19);
		frmLibraryManagementSystem.getContentPane().add(idField);
		idField.setColumns(10);
		
		searchField = new JTextField();
		searchField.setColumns(10);
		searchField.setBounds(79, 314, 96, 19);
		frmLibraryManagementSystem.getContentPane().add(searchField);
		
		JLabel lblArama = new JLabel("Arama");
		lblArama.setBounds(105, 290, 50, 13);
		frmLibraryManagementSystem.getContentPane().add(lblArama);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("");
		scrollPane.setBounds(350, 270, 250, 100);
		frmLibraryManagementSystem.getContentPane().add(scrollPane);
		
		JButton searchButton = new JButton("Search Book");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String searchTitle = searchField.getText();
		        searchBook(searchTitle);

		        // JList'e yeni veri ekledikten sonra JScrollPane'i güncelleyin
		        bookList.setModel(bookListModel); // JList'in modelini güncelleyin

		        // ScrollPane'i güncelleyin
		        scrollPane.setViewportView(bookList);

		        // JFrame'i yenileyin
		        frmLibraryManagementSystem.revalidate();
		        frmLibraryManagementSystem.repaint();
		        
			}
		});
		searchButton.setBounds(204, 301, 116, 50);
		frmLibraryManagementSystem.getContentPane().add(searchButton);
		
		
	}
}
