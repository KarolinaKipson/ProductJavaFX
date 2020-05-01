package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sample.dialog.SuccessDialog;
import sample.model.Product;

import java.net.URL;
import java.util.*;

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
    public Button btnClear;
    @FXML
    public TextField inputName;
    @FXML
    public TextField inputManufacturer;
    @FXML
    public Spinner<Double> spinnerPrice;
    @FXML
    public Spinner<Integer> spinnerItems;


    private void getAllData() throws RestClientException {
        String resourceUrl = GET_ALL_PRODUCTS;
        SpinnerValueFactory<Integer> valueFactoryInt =   new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        spinnerItems.setValueFactory(valueFactoryInt);

        SpinnerValueFactory<Double> valueFactoryDouble =   new SpinnerValueFactory.DoubleSpinnerValueFactory(1.00, 100000.00, 1.01);
        spinnerPrice.setValueFactory(valueFactoryDouble);

        spinnerPrice.setEditable(true);
        spinnerItems.setEditable(true);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.ALL);
        converter.setSupportedMediaTypes(mediaTypes);
        messageConverters.add(converter);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);


        tableView.getItems().setAll(showData(restTemplate));

        btnDelete.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("You are about to delete a product!");
            alert.setContentText("Are you sure you want to delete this product?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Product selectedItem = tableView.getSelectionModel().getSelectedItem();
                tableView.getItems().remove(selectedItem);
                restTemplate.delete(resourceUrl + "/"+ selectedItem.getProductId());

                SuccessDialog successDialog = new SuccessDialog();
                successDialog.show("You have successfully deleted product.");
            } else {
              clearData();
            }

        });

        btnAdd.setOnAction(e->{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            RestTemplate restTemplateNew = new RestTemplate();

             Product newProduct = new Product();
             newProduct.setName(inputName.getText());
             newProduct.setManufacturer(inputManufacturer.getText());
             double price = spinnerPrice.getValueFactory().getValue();
             int items = spinnerItems.getValueFactory().getValue();

             newProduct.setPrice(price);
             newProduct.setUnitInStock(items);
             HttpEntity<?> request = new HttpEntity<>(newProduct, headers);

             restTemplateNew.postForEntity(GET_ALL_PRODUCTS,request, Product.class);
             clearData();
             tableView.getItems().setAll(showData(restTemplateNew));

             SuccessDialog successDialog = new SuccessDialog();
             successDialog.show("You have successfully added product.");

        });

        btnClear.setOnAction(actionEvent -> {
           clearData();
        });

        btnEdit.setOnAction(actionEvent -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            RestTemplate restTemplateEdit = new RestTemplate();

            Product product = tableView.getSelectionModel().getSelectedItem();
            product.setName(inputName.getText());
            product.setManufacturer(inputManufacturer.getText());
            product.setPrice(spinnerPrice.getValueFactory().getValue());
            product.setUnitInStock(spinnerItems.getValueFactory().getValue());

            HttpEntity<?> request = new HttpEntity<>(product, headers);
            restTemplateEdit.put(GET_ALL_PRODUCTS + "/" + product.getProductId(), request, Product.class);

            SuccessDialog successDialog = new SuccessDialog();
            successDialog.show("You have successfully edited product.");

            tableView.getItems().setAll(showData(restTemplateEdit));
        });

    }

    public void clearData(){
        tableView.getSelectionModel().clearSelection();
        inputName.clear();
        inputManufacturer.clear();
        spinnerPrice.getValueFactory().setValue(1.00);
        spinnerItems.getValueFactory().setValue(1);
    }

    public ObservableList<Product> showData(RestTemplate restTemplate){

        String resourceUrl = GET_ALL_PRODUCTS;
        ResponseEntity<Product[]> response = restTemplate.getForEntity(resourceUrl, Product[].class);
        System.out.println("Status code: " + response.getStatusCode());
        Product[] products = response.getBody();

        List<Product> productList = Arrays.asList(products);
        ObservableList<Product> observableList = FXCollections.observableArrayList(productList);
        return observableList;
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

        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                if (tableView.getSelectionModel().getSelectedItem() != null) {
                    Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
                    inputName.setText(selectedProduct.getName());
                    inputManufacturer.setText(selectedProduct.getManufacturer());
                    spinnerPrice.getValueFactory().setValue(selectedProduct.getPrice());
                    spinnerItems.getValueFactory().setValue(selectedProduct.getUnitInStock());
                }} });

    }


}
