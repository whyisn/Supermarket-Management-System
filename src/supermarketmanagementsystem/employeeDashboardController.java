/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermarketmanagementsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Wahyu Isnan
 */
public class employeeDashboardController implements Initializable {

    @FXML
    private Button beli_bayarBtn;

    @FXML
    private Button beli_cetakStrukBtn;

    @FXML
    private TableColumn<customerData, String> beli_col_brand;

    @FXML
    private TableColumn<customerData, String> beli_col_harga;

    @FXML
    private TableColumn<customerData, String> beli_col_kuantitas;

    @FXML
    private TableColumn<customerData, String> beli_col_produk;

    @FXML
    private Label beli_employeed;

    @FXML
    private Spinner<Integer> beli_kuantitas;

    @FXML
    private TextField beli_namaBrand;

    @FXML
    private ComboBox<?> beli_namaProduk;

    @FXML
    private TableView<customerData> beli_tableView;

    @FXML
    private Button beli_tmbhBtn;

    @FXML
    private Label beli_total;

    @FXML
    private Button close;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    // Buat tabel untuk customer
    public void purchaseAdd() {
        purchaseCustomerId();
        purchaseSpinnerValue();
        purchaseGetPrice();

        String insertProd = "INSERT INTO customer "
                + "(customer_id, brand, productName, quantity, price, date) "
                + "VALUES(?, ?, ?, ?, ?, ?)";

        connect = database.connectDb();

        try {

            Alert alert;

            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            if (beli_namaBrand.getText().isEmpty()
                    || beli_namaProduk.getSelectionModel().getSelectedItem() == null
                    || qty == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Silahkan pilih produk terlebih dahulu");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(insertProd);
                prepare.setString(1, String.valueOf(customerId));
                prepare.setString(2, beli_namaBrand.getText());
                prepare.setString(3, (String) beli_namaProduk.getSelectionModel().getSelectedItem());
                prepare.setString(4, String.valueOf(qty));

                totalPrice = (qty * price);

                prepare.setString(5, String.valueOf(totalPrice));
                prepare.setString(6, String.valueOf(sqlDate));

                prepare.executeUpdate();

                // Untuk update tableView
                purchaseShowListData();
                // Untuk menampilkan total harga
                purchaseDisplayTotal();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void purchaseReset() {
        purchaseCustomerId();
        String resetData = "DELETE * FROM customer WHERE customer_id = '" + customerId + "'";

        connect = database.connectDb();

        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Apa kamu yakin ingin mereset? List produk juga akan direset");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                statement = connect.createStatement();
                statement.executeUpdate(resetData);

                beli_namaBrand.setText("");
                beli_namaProduk.getSelectionModel().clearSelection();
                purchaseSpinner();
                beli_total.setText("$0.0");

                purchaseShowListData();

            } else {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void purchasePay() {
        purchaseCustomerId();
        purchaseDisplayTotal();
        String sql = "INSERT INTO customer_receipt (customer_id, total, date)"
                + "VALUES (?, ?, ?)";

        connect = database.connectDb();

        try {
            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            Alert alert;
            if (beli_tableView.getItems().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Silahkan pilih produk terlebih dahulu :<");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Apa kamu yakin ingin membayarnya?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, String.valueOf(customerId));
                    prepare.setString(2, String.valueOf(totalP));
                    prepare.setString(3, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Berhasil! :>");
                    alert.showAndWait();

                } else {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void purchaseReceipt() {
        HashMap hash = new HashMap();
        // Parameter Nama, id
        hash.put("supermarketP", customerId);

        try {
            Alert alert;
            if (totalP == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid <3");
                alert.showAndWait();
            } else {
                JasperDesign jDesign = JRXmlLoader.load("C:\\Users\\Wahyu Isnan\\Documents\\NetBeansProjects\\SupermarketManagementSystem\\src\\supermarketmanagementsystem\\report.jrxml");
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, hash, connect);

                JasperViewer.viewReport(jPrint, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double totalP = 0;

    public void purchaseDisplayTotal() {
        purchaseCustomerId();
        String sql = "SELECT SUM(price) FROM customer WHERE customer_id = '"
                + customerId + "'";

        connect = database.connectDb();

        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            if (result.next()) {
                totalP = result.getDouble("SUM(price)");
            }
            beli_total.setText("$" + String.valueOf(totalP));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private double price = 0;
    private double totalPrice = 0;

    public void purchaseGetPrice() {

        String gPrice = "SELECT price FROM product WHERE product_name = '"
                + beli_namaProduk.getSelectionModel().getSelectedItem() + "'";

        connect = database.connectDb();

        try {

            statement = connect.createStatement();
            result = statement.executeQuery(gPrice);

            if (result.next()) {
                price = result.getDouble("price");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void pirchaseSearchBrand() {

        String searchB = "SELECT * FROM product WHERE brand = '"
                + beli_namaBrand.getText() + "' and status = 'Available'";

        connect = database.connectDb();

        try {

            prepare = connect.prepareStatement(searchB);
            result = prepare.executeQuery();

            ObservableList listProduct = FXCollections.observableArrayList();

            while (result.next()) {
                listProduct.add(result.getString("product_name"));
            }

            beli_namaProduk.setItems(listProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private SpinnerValueFactory spinner;

    public void purchaseSpinner() { //Min, Max, display num
        spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0);

        beli_kuantitas.setValueFactory(spinner);
    }

    private int qty;

    public void purchaseSpinnerValue() {
        qty = beli_kuantitas.getValue();

//        System.out.println(qty);
    }

    public ObservableList<customerData> purchaseListData() {

        purchaseCustomerId();

        ObservableList<customerData> customorList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer WHERE customer_id = '" + customerId + "'";
        connect = database.connectDb();

        try {

            customerData custD;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                custD = new customerData(result.getInt("customer_id"),
                        result.getString("brand"),
                        result.getString("productName"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getDate("date"));

                customorList.add(custD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customorList;
    }

    private ObservableList<customerData> purchaseList;

    public void purchaseShowListData() {
        purchaseList = purchaseListData();

        beli_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        beli_col_produk.setCellValueFactory(new PropertyValueFactory<>("productName"));
        beli_col_kuantitas.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        beli_col_harga.setCellValueFactory(new PropertyValueFactory<>("price"));

        beli_tableView.setItems(purchaseList);

    }

    private int customerId;

    public void purchaseCustomerId() {

        String cID = "SELECT customer_id FROM customer";

        connect = database.connectDb();

        try {

            prepare = connect.prepareStatement(cID);
            result = prepare.executeQuery();

            while (result.next()) {
                // FINAL ROW OF CUSTOMER ID
                customerId = result.getInt("customer_id");
            }

            int checkNum = 0;

            String checkCustomerId = "SELECT customer_id FROM customer_receipt";

            statement = connect.createStatement();
            result = statement.executeQuery(checkCustomerId);

            while (result.next()) {
                checkNum = result.getInt("customer_id");
            }

            if (customerId == 0) {
                customerId += 1;
            } else if (checkNum == customerId) {
                customerId += 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayEmployeeId() {
        beli_employeed.setText(getData.employeeId);
    }

    private double x = 0;
    private double y = 0;

    public void logout() {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Apakah kamu yakin ingin keluar dari Aplikasi?");

            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {

                logout.getScene().getWindow().hide();

                // Kembali ke halaman login
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });
                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });
                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();

            } else {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayEmployeeId();
        purchaseShowListData();
        purchaseSpinner();
        purchaseDisplayTotal();
    }

}
