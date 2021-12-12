package sample.cours.controller.user;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.cours.alerts.AlertWindows;
import sample.cours.model.User;
import sample.cours.windows.WindowsCreator;
import sample.cours.service.Client;

public class AddNewContributionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField depositAmountField;

    @FXML
    private TextField percentField;

    @FXML
    private TextField countYearField;

    @FXML
    private TextField currencyField;

    @FXML
    private Button addNewContributionButton;

    @FXML
    private Button returnButton;

    @FXML
    private TextField returnContributionField;

    @FXML
    private TextField typeContributionField;



    @FXML
    void initialize() {
        initField();
    addNewContributionButton.setOnAction(event -> {
        addNewContributionInDb();
    });

    returnButton.setOnAction(event -> {
        WindowsCreator.windowsCreator.addNewWindow("/fxml/clientWindow.fxml",returnButton);
    });

    }
    private void addNewContributionInDb(){


        Client.interactionsWithServer.sendMsg("addContribution");
        Client.interactionsWithServer.sendMsg(User.currentUser.getIdUser()+" "+depositAmountField.getText()+" "+
                        percentField.getText()+" "+ countYearField.getText() +" "+currencyField.getText()+" "+
                returnContributionField.getText()+" "+typeContributionField.getText());
        if(Client.interactionsWithServer.checkAddContributionInDb()){

            AlertWindows.alertWindows.alertWindowShowWarning("Вклад был добавлен !");

        }
        else {
            AlertWindows.alertWindows.alertWindowShowWarning("Что то пошло не так !");

        }}



    private void initField(){
        depositAmountField.setText("");
        percentField.setText("");
        countYearField.setText("");
        currencyField.setText("");
        returnContributionField.setText("");
        typeContributionField.setText("");
    }

}