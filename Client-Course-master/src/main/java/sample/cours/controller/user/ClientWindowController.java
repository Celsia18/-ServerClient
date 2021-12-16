package sample.cours.controller.user;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.cours.alerts.AlertWindows;
import sample.cours.model.Contribution;
import sample.cours.model.User;
import sample.cours.windows.WindowsCreator;
import sample.cours.service.Client;

public class ClientWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addContributionButton;

    @FXML
    private Button signOutButton;

    @FXML
    private Button updateContributionButton;


    @FXML
    private TableView<Contribution> tableViewContribution;

    @FXML
    private TableColumn<Contribution, Integer> depositAmountTab;

    @FXML
    private TableColumn<Contribution, Double> percentTab;

    @FXML
    private TableColumn<Contribution, Integer> countYearTab;

    @FXML
    private TableColumn<Contribution, String> currencyTab;

    @FXML
    private TableColumn<Contribution, String> collectMoneyTab;

    @FXML
    private TableColumn<Contribution, String> typeDepositTab;


    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;


    @FXML
    private Button calculateButton;

    @FXML
    private TextField sumField;

    @FXML
    private Button createDocButton;

    @FXML
    private Button deleteContributionButton;

    @FXML
    private Button profileButton;


    private final ObservableList<Contribution> listContribution = FXCollections.observableArrayList();

    @FXML
    void initialize() {


        profileButton.setOnAction(event -> {

            WindowsCreator.windowsCreator.addNewWindow("/fxml/editAccount.fxml",profileButton);
        });

        searchButton.setOnAction(event -> {

            try {
                initTab(searchDataContribution());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        calculateButton.setOnAction(event -> {
            try {
                sumField.setText(calculate());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        try {
            initTab(findDataContribution());
            initField();
        } catch (IOException e) {
            e.printStackTrace();
        }


        createDocButton.setOnAction(event -> {
            try {
                createFileToClient();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        addContributionButton.setOnAction(event -> {
           WindowsCreator.windowsCreator.addNewWindow("/fxml/windowToAddContribution.fxml",addContributionButton);
        });

        signOutButton.setOnAction(event -> {
            signOut();
            WindowsCreator.windowsCreator.addNewWindow("/fxml/login.fxml",addContributionButton);
        });

        updateContributionButton.setOnAction(event -> {

            try {
                initTab(findDataContribution());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        deleteContributionButton.setOnAction(event -> {
            deleteContribution();
        });
    }

    private  void deleteContribution(){
        if(tableViewContribution.getSelectionModel().getSelectedItem() != null) {
            int  count = tableViewContribution.getSelectionModel().getSelectedCells().get(0).getRow();

            if (!listContribution.get(count).getCollectMoney().equals("no")) {
                Client.interactionsWithServer.sendMsg("deleteContributionByID");
                Client.interactionsWithServer.sendMsg(String.valueOf(listContribution.get(count).getId()));
                try {
                    initTab(findDataContribution());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AlertWindows.alertWindows.alertWindowShowWarning("Операция прошла успешно !");

            } else {

                AlertWindows.alertWindows.alertWindowShowWarning("Нельзя забрать этот вклад !");

            }
        }
        else {
            AlertWindows.alertWindows.alertWindowShowWarning("Выберите строку !");

        }
    }

    private void createFileToClient() throws IOException {


        if(listContribution.size()>0) {

            FileWriter csvWriter = new FileWriter(User.currentUser.getEmail() + ".doc");
            for (Contribution rowData : listContribution) {
                csvWriter.append(rowData.toString());
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
            AlertWindows.alertWindows.alertWindowShowWarning("Документ был создан!");

        }else{
            AlertWindows.alertWindows.alertWindowShowWarning("Упс! У вас нету вкладов!");

        }
    }

    private void initTab(LinkedList<Contribution> listDb) {
        listContribution.clear();
        listContribution.addAll(listDb);
        depositAmountTab.setCellValueFactory(new PropertyValueFactory<Contribution, Integer>("depositAmount"));
        percentTab.setCellValueFactory(new PropertyValueFactory<Contribution, Double>("percent"));
        countYearTab.setCellValueFactory(new PropertyValueFactory<Contribution, Integer>("countYear"));
        currencyTab.setCellValueFactory(new PropertyValueFactory<Contribution, String>("currency"));
        collectMoneyTab.setCellValueFactory(new PropertyValueFactory<Contribution, String>("collectMoney"));
        typeDepositTab.setCellValueFactory(new PropertyValueFactory<Contribution, String>("typeDeposit"));

        searchField.setText("");// Для поиска путого значения

        tableViewContribution.setItems(listContribution);
    }

    private LinkedList<Contribution> findDataContribution() throws IOException {
        Client.interactionsWithServer.sendMsg("showContributionById");
        Client.interactionsWithServer.sendMsg(String.valueOf(User.currentUser.getIdUser()));

         return Client.interactionsWithServer.showContribution();
    }
    private LinkedList<Contribution> searchDataContribution() throws IOException {

        if(!searchField.getText().equals("")&& searchField.getText().chars().allMatch(Character::isDigit)) {
            Client.interactionsWithServer.sendMsg("searchContributionByDepositAverage");
            Client.interactionsWithServer.sendMsg(searchField.getText()+" "+User.currentUser.getIdUser());

        return Client.interactionsWithServer.showContribution();
        }
        else{
            AlertWindows.alertWindows.alertWindowShowWarning("Ой-Ой. Технические неполадки!");

            return findDataContribution();
        }
    }
    private void signOut(){
        Client.interactionsWithServer.sendMsg("signOut");

    }

    private String calculate () throws IOException {
        int count=-1;

            if(tableViewContribution.getSelectionModel().getSelectedItem() != null) {
                LinkedList<Contribution> contributionInTable =getListInTableView();
                count = tableViewContribution.getSelectionModel().getSelectedCells().get(0).getRow();

              Client.interactionsWithServer.sendMsg("searchTotal");
              Client.interactionsWithServer.sendMsg(String.valueOf(contributionInTable.get(count).getId()));

            }
            else {
                AlertWindows.alertWindows.alertWindowShowWarning("Выберите строку !");

                return "";
            }

            return Client.interactionsWithServer.getTotalDeposit();
    }

    private LinkedList<Contribution> getListInTableView(){
        LinkedList<Contribution> list =  new LinkedList<>();
        list.addAll(listContribution);
        return list;

    }
    private void initField() throws IOException {

        Client.interactionsWithServer.sendMsg("currentUserData");
        Client.interactionsWithServer.sendMsg(String.valueOf(User.currentUser.getIdUser()));
        Client.interactionsWithServer.initUserFullParams();


    }
}
