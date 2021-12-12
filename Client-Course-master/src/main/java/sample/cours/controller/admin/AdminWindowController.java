package sample.cours.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.cours.alerts.AlertWindows;
import sample.cours.model.Contribution;
import sample.cours.model.User;
import sample.cours.windows.WindowsCreator;
import sample.cours.service.Client;

public class AdminWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button allWorkerButton;

    @FXML
    private Button statusButton;

    @FXML
    private TableView<Contribution> contributionTable;

    @FXML
    private TableColumn<Contribution, Integer> idContributionTab;

    @FXML
    private TableColumn<Contribution, Integer> amountDepositTab;

    @FXML
    private TableColumn<Contribution, Double> percentContributionTab;

    @FXML
    private TableColumn<Contribution, String> typeDepositTab;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> idUserTab;

    @FXML
    private TableColumn<User, String> emailTab;

    @FXML
    private TableColumn<User, String> passwordTab;

    @FXML
    private TableColumn<User, Integer> rollTab;



    @FXML
    private TextField idField;

    @FXML
    private Button redactionButton;

    @FXML
    private TextField emailField;

    @FXML
    private Button HistoryButton;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField statusField;

    @FXML
    private TextField rollField;

    @FXML
    private Button chooseButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button inHistoryButton;

    private final ObservableList<Contribution> listContribution = FXCollections.observableArrayList();
    private final ObservableList<User> listUsers = FXCollections.observableArrayList();


    @FXML
    void initialize() {

        HistoryButton.setOnAction(event -> {

            WindowsCreator.windowsCreator.addNewWindow("/fxml/archive.fxml",HistoryButton);

        });

        inHistoryButton.setOnAction(event -> {
            addInHistory();
            WindowsCreator.windowsCreator.addNewWindow("/fxml/adminWindow.fxml",inHistoryButton);

        });


        returnButton.setOnAction(event -> {
            WindowsCreator.windowsCreator.addNewWindow("/fxml/login.fxml",returnButton);
        });

        try {
            initContributionTable(showAllContributions());
            initUsers(showAllUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }

        allWorkerButton.setOnAction(event -> {
            WindowsCreator.windowsCreator.addNewWindow("/fxml/allWorkerTable.fxml", allWorkerButton);

        });


        chooseButton.setOnAction(event -> {
            initFieldToRedaction();
        });

        statusButton.setOnAction(event -> {
            Client.interactionsWithServer.sendMsg("statusUser");
            Client.interactionsWithServer.sendMsg(idField.getText()+" "+statusField.getText());
            try {
                initUsers(showAllUsers());
                idField.setText("");
                emailField.setText("");
                passwordField.setText("");
                rollField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        redactionButton.setOnAction(event -> {
            Client.interactionsWithServer.sendMsg("redactionUser");
            Client.interactionsWithServer.sendMsg(idField.getText()+" "+emailField.getText()+" "+passwordField.getText()+" "+rollField.getText());
            try {
                initUsers(showAllUsers());
                idField.setText("");
                emailField.setText("");
                passwordField.setText("");
                rollField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

  /*  private void initFieidToStatus(){
        LinkedList<User> listUsersDb = new LinkedList<>();
        listUsersDb.addAll(listUsers);
        if(usersTable.getSelectionModel().getSelectedItem() != null) {
            int count = usersTable.getSelectionModel().getSelectedCells().get(0).getRow();
            statusField.setText(listUsers.get(count).getPosition());

        }
        else{
            AlertWindows.alertWindows.alertWindowShowWarning("Вы не выбрали строку !");
        }
    }*/

    private void initFieldToRedaction(){
        LinkedList<User> listUsersDb = new LinkedList<>();
        listUsersDb.addAll(listUsers);
        if(usersTable.getSelectionModel().getSelectedItem() != null) {
            int count = usersTable.getSelectionModel().getSelectedCells().get(0).getRow();
            idField.setEditable(false);
            idField.setText(String.valueOf(listUsers.get(count).getIdUser()));
            emailField.setText(listUsers.get(count).getEmail());
            passwordField.setText(listUsers.get(count).getPassword());
            rollField.setText(String.valueOf(listUsers.get(count).getRoll()));
           statusField.setText(listUsers.get(count).getPosition());
        }
        else{
            AlertWindows.alertWindows.alertWindowShowWarning("Вы не выбрали строку !");
        }


        }



    private void initContributionTable(LinkedList<Contribution> listDb){
        listContribution.clear();
        listContribution.addAll(listDb);

        idContributionTab.setCellValueFactory(new PropertyValueFactory<Contribution, Integer>("id"));
        amountDepositTab.setCellValueFactory(new PropertyValueFactory<Contribution, Integer>("depositAmount"));
        percentContributionTab.setCellValueFactory(new PropertyValueFactory<Contribution, Double>("percent"));
        typeDepositTab.setCellValueFactory(new PropertyValueFactory<Contribution, String>("typeDeposit"));


        contributionTable.setItems(listContribution);
    }

    private void initUsers(LinkedList<User> listDb){
        listUsers.clear();
        listUsers.addAll(listDb);

        idUserTab.setCellValueFactory(new PropertyValueFactory<User, Integer>("idUser"));
        emailTab.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        passwordTab.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        rollTab.setCellValueFactory(new PropertyValueFactory<User, Integer>("roll"));


        usersTable.setItems(listUsers);
    }


    private LinkedList<Contribution> showAllContributions() throws IOException {
        Client.interactionsWithServer.sendMsg("showAllContribution");
        return Client.interactionsWithServer.showAllContribution();
    }

    private LinkedList<Contribution> showAllContributionsInHistory()  throws IOException {
        Client.interactionsWithServer.sendMsg("showAllContributionInHistory");
        return Client.interactionsWithServer.showAllContributionInHistory();
    }

    private LinkedList<User> showAllUsers() throws IOException {
        Client.interactionsWithServer.sendMsg("showAllUser");
        return Client.interactionsWithServer.showAllUsers();
    }

    private void addInHistory(){
        if(contributionTable.getSelectionModel().getSelectedItem() != null) {
            int count = contributionTable.getSelectionModel().getSelectedCells().get(0).getRow();
            Client.interactionsWithServer.sendMsg("addInHistoryTable");
            Client.interactionsWithServer.sendMsg(String.valueOf(listContribution.get(count).getId()));

          //  Client.interactionsWithServer.sendMsg("showAllContributionInHistory");

          /*  try {
                Client.interactionsWithServer.getHistory();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        else{
            AlertWindows.alertWindows.alertWindowShowWarning("Вы не выбрали строку !");
        }

    }
}



