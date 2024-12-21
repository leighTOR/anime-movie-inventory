import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    private TableView<AnimePurchase> tableView;
    private TextField titleField;
    private TextField quantityField;
    private TextField priceField;
    private Label messageLabel;

    @Override
    public void start(Stage primaryStage) {
        // Ensure DB table exists
        createTableIfNotExists();

        // Set up the TableView
        tableView = new TableView<>();
        tableView.setPrefHeight(200);

        TableColumn<AnimePurchase, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getId()).asObject()
        );

        TableColumn<AnimePurchase, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getTitle())
        );

        TableColumn<AnimePurchase, Integer> colQty = new TableColumn<>("Quantity");
        colQty.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject()
        );

        TableColumn<AnimePurchase, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject()
        );

        tableView.getColumns().addAll(colId, colTitle, colQty, colPrice);

        // When a row is selected, populate the text fields
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                titleField.setText(newVal.getTitle());
                quantityField.setText(String.valueOf(newVal.getQuantity()));
                priceField.setText(String.valueOf(newVal.getPrice()));
            }
        });

        // Create text fields
        titleField = new TextField();
        titleField.setPromptText("Anime Title");

        quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        priceField = new TextField();
        priceField.setPromptText("Price");

        // Create buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> createPurchase());

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updatePurchase());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deletePurchase());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> loadDataFromDatabase());

        // A label to show status/error messages
        messageLabel = new Label();
        messageLabel.setId("messageLabel");

        // Layout using a GridPane for the form
        GridPane formPane = new GridPane();
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.add(new Label("Title:"), 0, 0);
        formPane.add(titleField, 1, 0);
        formPane.add(new Label("Quantity:"), 0, 1);
        formPane.add(quantityField, 1, 1);
        formPane.add(new Label("Price:"), 0, 2);
        formPane.add(priceField, 1, 2);

        // Put buttons in a vertical box
        VBox buttonBox = new VBox(10, addButton, updateButton, deleteButton, refreshButton);
        formPane.add(buttonBox, 2, 0, 1, 3);

        // Root layout: table at top, form in middle, message label at bottom
        VBox root = new VBox(10, tableView, formPane, messageLabel);
        root.setPadding(new Insets(10));

        // Scene + Styles
        Scene scene = new Scene(root, 650, 450);
        // Load our custom CSS (assuming style.css is in the same folder as MainApp.java)
        scene.getStylesheets().add(
            getClass().getResource("style.css").toExternalForm()
        );

        primaryStage.setTitle("Anime Movie Inventory System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initial data load
        loadDataFromDatabase();
    }

    // -------------------------------------------------------------------------
    //  CRUD Methods
    // -------------------------------------------------------------------------

    private void createPurchase() {
        String title = titleField.getText().trim();
        String qtyStr = quantityField.getText().trim();
        String priceStr = priceField.getText().trim();

        if (title.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) {
            messageLabel.setText("Please fill in all fields before adding.");
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyStr);
            double price = Double.parseDouble(priceStr);

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO anime_purchases (title, quantity, price) VALUES (?, ?, ?)"
                 )) {
                pstmt.setString(1, title);
                pstmt.setInt(2, quantity);
                pstmt.setDouble(3, price);
                pstmt.executeUpdate();

                messageLabel.setText("Anime purchase added successfully!");
                clearFields();
                loadDataFromDatabase();
            }
        } catch (NumberFormatException ex) {
            messageLabel.setText("Quantity and Price must be numbers.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("DB Error: " + ex.getMessage());
        }
    }

    private void updatePurchase() {
        AnimePurchase selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Select a row to update.");
            return;
        }

        String title = titleField.getText().trim();
        String qtyStr = quantityField.getText().trim();
        String priceStr = priceField.getText().trim();

        if (title.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) {
            messageLabel.setText("All fields required to update.");
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyStr);
            double price = Double.parseDouble(priceStr);

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE anime_purchases SET title=?, quantity=?, price=? WHERE id=?"
                 )) {
                pstmt.setString(1, title);
                pstmt.setInt(2, quantity);
                pstmt.setDouble(3, price);
                pstmt.setInt(4, selected.getId());
                pstmt.executeUpdate();

                messageLabel.setText("Anime purchase updated successfully!");
                clearFields();
                loadDataFromDatabase();
            }
        } catch (NumberFormatException ex) {
            messageLabel.setText("Quantity and Price must be numbers.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("DB Error: " + ex.getMessage());
        }
    }

    private void deletePurchase() {
        AnimePurchase selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Select a row to delete.");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "DELETE FROM anime_purchases WHERE id=?"
             )) {
            pstmt.setInt(1, selected.getId());
            pstmt.executeUpdate();

            messageLabel.setText("Anime purchase deleted.");
            clearFields();
            loadDataFromDatabase();
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("DB Error: " + ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    //  Helpers
    // -------------------------------------------------------------------------

    private void loadDataFromDatabase() {
        tableView.getItems().clear();
        List<AnimePurchase> dataList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM anime_purchases")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int qty = rs.getInt("quantity");
                double price = rs.getDouble("price");

                AnimePurchase ap = new AnimePurchase(id, title, qty, price);
                dataList.add(ap);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("DB Error (Load): " + ex.getMessage());
        }

        tableView.getItems().addAll(dataList);
    }

    private void createTableIfNotExists() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String createSQL = "CREATE TABLE IF NOT EXISTS anime_purchases ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "title TEXT NOT NULL, "
                    + "quantity INTEGER, "
                    + "price REAL"
                    + ")";
            stmt.execute(createSQL);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        titleField.clear();
        quantityField.clear();
        priceField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
