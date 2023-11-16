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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static javax.management.remote.JMXConnectorFactory.connect;

/**
 *
 * @author Wahyu Isnan
 */
public class adminDashboardController implements Initializable {

    @FXML
    private Button addProducts_clearBtn;

    @FXML
    private TableColumn<productData, String> addProducts_col_harga;

    @FXML
    private TableColumn<productData, String> addProducts_col_namaBrand;

    @FXML
    private TableColumn<productData, String> addProducts_col_namaProduk;

    @FXML
    private TableColumn<productData, String> addProducts_col_produkID;

    @FXML
    private TableColumn<productData, String> addProducts_col_status;

    @FXML
    private AnchorPane addProducts_form;

    @FXML
    private Button addProducts_hapusBtn;

    @FXML
    private TextField addProducts_harga;

    @FXML
    private TextField addProducts_namaBrand;

    @FXML
    private TextField addProducts_namaProduk;

    @FXML
    private Button addProducts_perbaruiBtn;

    @FXML
    private TextField addProducts_produkID;

    @FXML
    private TextField addProducts_search;

    @FXML
    private ComboBox<?> addProducts_status;

    @FXML
    private TableView<productData> addProducts_tableView;

    @FXML
    private Button addProducts_tambahBtn;

    @FXML
    private Button close;

    @FXML
    private Label dashboard_activeEmployee;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AreaChart<?, ?> dashboard_chart;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_incomeToday;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private Button employees_btn;

    @FXML
    private Button employees_clearBtn;

    @FXML
    private TableColumn<employeeData, String> employees_col_date;

    @FXML
    private TableColumn<employeeData, String> employees_col_gender;

    @FXML
    private TableColumn<employeeData, String> employees_col_karyawanID;

    @FXML
    private TableColumn<employeeData, String> employees_col_namaBelakang;

    @FXML
    private TableColumn<employeeData, String> employees_col_namaDepan;

    @FXML
    private TableColumn<employeeData, String> employees_col_password;

    @FXML
    private AnchorPane employees_form;

    @FXML
    private ComboBox<?> employees_gender;

    @FXML
    private Button employees_hapusBtn;

    @FXML
    private TextField employees_karyawanID;

    @FXML
    private TextField employees_namaBelakang;

    @FXML
    private TextField employees_namaDepan;

    @FXML
    private TextField employees_password;

    @FXML
    private Button employees_perbaruiBtn;

    @FXML
    private Button employees_simpanBtn;

    @FXML
    private TableView<employeeData> employees_tableView;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Button tmbhproduk_btn;

    @FXML
    private Label username;

    private double x = 0;
    private double y = 0;

    // Database
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public void dashboardDisplayActiveEmployees() {

        String sql = "SELECT COUNT(id) FROM employee";

        connect = database.connectDb();

        int countE = 0;

        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            if (result.next()) {
                countE = result.getInt("COUNT(id)");
            }

            dashboard_activeEmployee.setText(String.valueOf(countE));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardDisplayIncomeToday() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String sql = "SELECT SUM(total) FROM customer_receipt WHERE date = '"
                + sqlDate + "'";

        double sumT = 0;

        connect = database.connectDb();

        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            if (result.next()) {
                sumT = result.getDouble("SUM(total)");
            }

            dashboard_incomeToday.setText("$" + String.valueOf(sumT));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardTotalIncome() {

        String sql = "SELECT SUM(total) FROM customer_receipt";

        double sumTI = 0;

        connect = database.connectDb();

        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            if (result.next()) {
                sumTI = result.getDouble("SUM(total)");
            }

            dashboard_totalIncome.setText("$" + String.valueOf(sumTI));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardChart() {
        dashboard_chart.getData().clear();

        String sql = "SELECT date, SUM(total) FROM customer_receipt GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 9";

        connect = database.connectDb();

        try {

            XYChart.Series chart = new XYChart.Series();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }

            dashboard_chart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addProductsAdd() {

        String insertProduct = "INSERT INTO product "
                + "(product_id, brand, product_name, status, price)"
                + "VALUES (?, ?, ?, ?, ?)";

        connect = database.connectDb();

        try {

            Alert alert;

            if (addProducts_produkID.getText().isEmpty()
                    || addProducts_namaBrand.getText().isEmpty()
                    || addProducts_namaProduk.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || addProducts_harga.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String Check = "SELECT product_id FROM product WHERE product_id = '"
                        + addProducts_produkID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(Check);

                // jika product_id berisi data yang sama, maka kita akan memblok data tersebut
                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("ID Produk: " + addProducts_produkID.getText() + " telah tersedia!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(insertProduct);
                    prepare.setString(1, addProducts_produkID.getText());
                    prepare.setString(2, addProducts_namaBrand.getText());
                    prepare.setString(3, addProducts_namaProduk.getText());
                    prepare.setString(4, (String) addProducts_status.getSelectionModel().getSelectedItem());
                    prepare.setString(5, addProducts_harga.getText());

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Berhasil ditambahkan! :>");
                    alert.showAndWait();

                    // Untuk memperbarui TableView ketika memasukkan data
                    addProductsShowData();
                    // Membersihkan fields ketika masukkan data berhasil
                    addProductsClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProductsUpdate() {
        String updateProduct = "UPDATE product SET brand = '"
                + addProducts_namaBrand.getText() + "', product_name = '"
                + addProducts_namaProduk.getText() + "', status = '"
                + addProducts_status.getSelectionModel().getSelectedItem() + "', price = '"
                + addProducts_harga.getText() + "' WHERE product_id = '"
                + addProducts_produkID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;

            if (addProducts_produkID.getText().isEmpty()
                    || addProducts_namaBrand.getText().isEmpty()
                    || addProducts_namaProduk.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || addProducts_harga.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Apa kamu yakin ingin memperbarui produk dengan Produk ID " + addProducts_produkID.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(updateProduct);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Data berhasil diperbarui!");
                    alert.showAndWait();

                    // Untuk memperbarui TableView ketika memasukkan data
                    addProductsShowData();
                    // Membersihkan fields ketika masukkan data berhasil
                    addProductsClear();

                } else {
                    return;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProductsDelete() {
        String deleteProduct = "DELETE FROM product WHERE product_id = '"
                + addProducts_produkID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;

            if (addProducts_produkID.getText().isEmpty()
                    || addProducts_namaBrand.getText().isEmpty()
                    || addProducts_namaProduk.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || addProducts_harga.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Apa kamu yakin ingin menghapus produk dengan Produk ID " + addProducts_produkID.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    prepare = connect.prepareStatement(deleteProduct);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Data berhasil dihapus!");
                    alert.showAndWait();

                    // Untuk memperbarui TableView ketika memasukkan data
                    addProductsShowData();
                    // Membersihkan fields ketika masukkan data berhasil
                    addProductsClear();

                } else {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProductsClear() {
        addProducts_produkID.setText("");
        addProducts_namaBrand.setText("");
        addProducts_namaProduk.setText("");
        addProducts_status.getSelectionModel().clearSelection();
        addProducts_harga.setText("");
    }

    private String[] statusList = {"Available", "Not Available"};

    public void addProductsStatusList() {
        List<String> listS = new ArrayList<>();

        for (String data : statusList) {
            listS.add(data);
        }

        ObservableList statusData = FXCollections.observableArrayList(listS);
        addProducts_status.setItems(statusData);

    }

    public void addProductsSearch() {

        FilteredList<productData> filter = new FilteredList<>(addProductsList, e -> true);

        addProducts_search.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateProductData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateProductData.getProductId().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductData.getBrand().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductData.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductData.getProductName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductData.getPrice().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<productData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(addProducts_tableView.comparatorProperty());
        addProducts_tableView.setItems(sortList);

    }

    public ObservableList<productData> addProductsListData() {
        ObservableList<productData> prodList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";

        connect = database.connectDb();

        try {
            productData prod;

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                prod = new productData(result.getString("product_id"),
                        result.getString("brand"),
                        result.getString("product_name"),
                        result.getString("status"),
                        result.getDouble("price"));

                prodList.add(prod);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prodList;
    }

    private ObservableList<productData> addProductsList;

    public void addProductsShowData() { // Untuk menampilkan data di TableView
        addProductsList = addProductsListData();

        addProducts_col_produkID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        addProducts_col_namaBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addProducts_col_namaProduk.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addProducts_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        addProducts_col_harga.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProducts_tableView.setItems(addProductsList);

    }

    public void addProductsSelect() {
        productData prod = addProducts_tableView.getSelectionModel().getSelectedItem();
        int num = addProducts_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < - 1) {
            return;
        }

        addProducts_produkID.setText(prod.getProductId());
        addProducts_namaBrand.setText(prod.getBrand());
        addProducts_namaProduk.setText(prod.getProductName());
        addProducts_harga.setText(String.valueOf(prod.getPrice()));

    }

    public void employeesSave() {

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String insertEmployee = "INSERT INTO employee "
                + "(employee_id, password, firstName, lastName, gander, date)"
                + "VALUES(?, ?, ?, ?, ?, ?)";

        connect = database.connectDb();

        try {

            Alert alert;

            if (employees_karyawanID.getText().isEmpty()
                    || employees_password.getText().isEmpty()
                    || employees_namaDepan.getText().isEmpty()
                    || employees_namaBelakang.getText().isEmpty()
                    || employees_gender.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                String checkExist = "SELECT employee_id FROM employee WHERE employee_id = '"
                        + employees_karyawanID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkExist);

                // jika employee telah tersedia
                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("ID Employee: " + employees_karyawanID.getText() + " telah tersedia!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(insertEmployee);
                    prepare.setString(1, employees_karyawanID.getText());
                    prepare.setString(2, employees_password.getText());
                    prepare.setString(3, employees_namaDepan.getText());
                    prepare.setString(4, employees_namaBelakang.getText());
                    prepare.setString(5, (String) employees_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(6, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Berhasil disimpan!");
                    alert.showAndWait();

                    // Untuk memperbarui TabelView
                    employeesShowListData();
                    // Untuk membersihkan fields
                    employeeReset();

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String[] genderList = {"Laki-Laki", "Perempuan"};

    public void employeesGender() {
        List<String> genderL = new ArrayList<>();

        for (String data : genderList) {
            genderL.add(data);
        }

        ObservableList listG = FXCollections.observableArrayList(genderL);
        employees_gender.setItems(listG);

    }

    public void employeesUpdate() {
        String updateEmployee = "UPDATE employee SET password = '"
                + employees_password.getText() + "', firstName = '"
                + employees_namaDepan.getText() + "', lastName = '"
                + employees_namaBelakang.getText() + "', gender = '"
                + employees_gender.getSelectionModel().getSelectedItem()
                + "' WHERE employee_id = '" + employees_karyawanID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;
            if (employees_karyawanID.getText().isEmpty()
                    || employees_password.getText().isEmpty()
                    || employees_namaDepan.getText().isEmpty()
                    || employees_namaBelakang.getText().isEmpty()
                    || employees_gender.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Apa kamu yakin ingin memperbarui employee dengan Employee ID " + employees_karyawanID.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(updateEmployee);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Data berhasil diperbarui!");
                    alert.showAndWait();

                    // Untuk memperbarui TableView ketika memasukkan data
                    employeesShowListData();
                    // Membersihkan fields ketika masukkan data berhasil
                    employeeReset();

                } else {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void employeesDelete() {

        String deleteEmployee = "DELETE FROM employee WHERE employee_id = '"
                + employees_karyawanID.getText() + "'";

        connect = database.connectDb();

        try {

            Alert alert;
            if (employees_karyawanID.getText().isEmpty()
                    || employees_password.getText().isEmpty()
                    || employees_namaDepan.getText().isEmpty()
                    || employees_namaBelakang.getText().isEmpty()
                    || employees_gender.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Apa kamu yakin ingin menghapus employee dengan Employee ID " + employees_karyawanID.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(deleteEmployee);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Data berhasil dihapus!");
                    alert.showAndWait();

                    // Untuk memperbarui TableView ketika memasukkan data
                    employeesShowListData();
                    // Membersihkan fields ketika masukkan data berhasil
                    employeeReset();

                } else {
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void employeeReset() {
        employees_karyawanID.setText("");
        employees_password.setText("");
        employees_namaDepan.setText("");
        employees_namaBelakang.setText("");
        employees_gender.getSelectionModel().clearSelection();
    }

    public ObservableList<employeeData> employeesListData() {

        ObservableList<employeeData> emData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM employee";

        connect = database.connectDb();

        try {

            employeeData employeeD;

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {

                employeeD = new employeeData(result.getString("employee_id"),
                        result.getString("password"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("gender"),
                        result.getDate("date"));

                emData.add(employeeD);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return emData;

    }

    private ObservableList<employeeData> employeesList;

    public void employeesShowListData() {
        employeesList = employeesListData();

        employees_col_karyawanID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employees_col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        employees_col_namaDepan.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        employees_col_namaBelakang.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        employees_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        employees_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        employees_tableView.setItems(employeesList);

    }

    public void employeesSelect() {
        employeeData employeeD = employees_tableView.getSelectionModel().getSelectedItem();
        int num = employees_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < - 1) {
            return;
        }

        employees_karyawanID.setText(employeeD.getEmployeeId());
        employees_password.setText(employeeD.getPassword());
        employees_namaDepan.setText(employeeD.getFirstName());
        employees_namaBelakang.setText(employeeD.getLastName());

    }

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

    public void displayUsername() {
        username.setText(getData.username);
    }

    public void defaultNavBtn() {
        dashboard_btn.setStyle("-fx-background-color: linear-gradient(to top right, #896b34, #b8a536)");
    }

    public void switchFrom(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            addProducts_form.setVisible(false);
            employees_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: linear-gradient(to top right, #896b34, #b8a536)");
            tmbhproduk_btn.setStyle("-fx-background-color: transparent");
            employees_btn.setStyle("-fx-background-color: transparent");

            dashboardDisplayActiveEmployees();
            dashboardDisplayIncomeToday();
            dashboardTotalIncome();
            dashboardChart();

        } else if (event.getSource() == tmbhproduk_btn) {
            dashboard_form.setVisible(false);
            addProducts_form.setVisible(true);
            employees_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            tmbhproduk_btn.setStyle("-fx-background-color: linear-gradient(to top right, #896b34, #b8a536)");
            employees_btn.setStyle("-fx-background-color: transparent");

            // Untuk menampilkan data ketika meengklik tombol navigasi
            addProductsShowData();
            addProductsStatusList();
            addProductsSearch();

        } else if (event.getSource() == employees_btn) {
            dashboard_form.setVisible(false);
            addProducts_form.setVisible(false);
            employees_form.setVisible(true);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            tmbhproduk_btn.setStyle("-fx-background-color: transparent");
            employees_btn.setStyle("-fx-background-color: linear-gradient(to top right, #896b34, #b8a536)");

            employeesShowListData();

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
        displayUsername();
        defaultNavBtn();

        dashboardDisplayActiveEmployees();
        dashboardDisplayIncomeToday();
        dashboardTotalIncome();
        dashboardChart();

        addProductsShowData();
        addProductsStatusList();

        employeesShowListData();
        employeesGender();

    }
}
