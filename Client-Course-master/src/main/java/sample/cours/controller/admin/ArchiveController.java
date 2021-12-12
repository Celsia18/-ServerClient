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
import javafx.scene.control.cell.PropertyValueFactory;
import sample.cours.model.Contribution;
import sample.cours.service.Client;
import sample.cours.windows.WindowsCreator;

public class ArchiveController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private Button returnButton;

    private final ObservableList<Contribution> listContribution = FXCollections.observableArrayList();


    @FXML
    void initialize() {

        returnButton.setOnAction(event -> {
            WindowsCreator.windowsCreator.addNewWindow("/fxml/adminWindow.fxml",returnButton);
        });

        try {
            initContributionTable(showAllContributions());

        } catch (IOException e) {
            e.printStackTrace();
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
    private LinkedList<Contribution> showAllContributions() throws IOException {
        Client.interactionsWithServer.sendMsg("showAllContributionInHistory");
        return Client.interactionsWithServer.showAllContributionInHistory();
    }
}
