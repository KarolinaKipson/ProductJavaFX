package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sample.model.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public static final String GET_ALL_PRODUCTS = "http://localhost:8080/api/product";

    @FXML
    public TableView<Product> tableView;
    @FXML
    public TableColumn<Product, Long> productId;
    @FXML
    public TableColumn<Product, String> productName;
    @FXML
    public TableColumn<Product, String> productManufacturer;
    @FXML
    public TableColumn<Product, Double> productPrice;
    @FXML
    public TableColumn<Product, Integer> productItems;
    @FXML
    public TableColumn productEdit;
    @FXML
    public Button btnDelete;
    @FXML
    public Button btnAdd;
    @FXML
    public Button btnEdit;
    @FXML
    public TextField inputName;
    @FXML
    public TextField inputManufacturer;
    @FXML
    public Spinner<Double> spinnerPrice;
    @FXML
    public Spinner<Integer> spinnerItems;

    private void getAllData() throws RestClientException {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.ALL);
        converter.setSupportedMediaTypes(mediaTypes);
        messageConverters.add(converter);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);

        String resourceUrl = GET_ALL_PRODUCTS;
        ResponseEntity<Product[]> response = restTemplate.getForEntity(resourceUrl, Product[].class);
        System.out.println("Status code: " + response.getStatusCode());
        Product[] products = response.getBody();

        List<Product> productList = Arrays.asList(products);
        ObservableList<Product> observableList = FXCollections.observableArrayList(productList);

        tableView.getItems().setAll(observableList);

        btnDelete.setOnAction(e -> {
            Product selectedItem = tableView.getSelectionModel().getSelectedItem();
            tableView.getItems().remove(selectedItem);
            restTemplate.delete(resourceUrl + "/"+ selectedItem.getProductId());
        });

        btnAdd.setOnAction(e->{

        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.setEditable(true);
        productId.setCellValueFactory(new PropertyValueFactory<Product, Long>("productId"));
        productName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productManufacturer.setCellValueFactory(new PropertyValueFactory<Product, String>("manufacturer"));
        productPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        productItems.setCellValueFactory(new PropertyValueFactory<Product, Integer>("unitInStock"));
        getAllData();

    }


}
