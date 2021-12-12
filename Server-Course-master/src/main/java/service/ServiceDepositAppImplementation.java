package service;

import database.DataBaseHandler;
import repository.ServiceDepositApp;
import server.ServerConnect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class ServiceDepositAppImplementation implements ServiceDepositApp {
    private final DataBaseHandler dbHandler;
    private final BufferedReader in;
    private final int currentCountClient;

    public ServiceDepositAppImplementation(DataBaseHandler dbHandler, BufferedReader in, int currentCountClient) {
        this.dbHandler = dbHandler;
        this.in = in;
        this.currentCountClient = currentCountClient;
    }

    @Override
    public void addContributionInHistoryDataBase(){
        try {
            dbHandler.addHistoryDeposit(Integer.parseInt(in.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   /* @Override
    public void showAllContributionInHistory(){
        LinkedList<String> listDb = new LinkedList<>();
        LinkedList<String> countDeposit = dbHandler.showHistory();
        int count;
        for(int i=0;i<countDeposit.size();i++){
            count = Integer.parseInt(countDeposit.get(i));
            dbHandler.showContributionByIdHistory(listDb,count);

        }
        ServerConnect.serverList.get(currentCountClient).send(String.valueOf(listDb.size()));
        for(int i=0;i<listDb.size();i++){
            System.out.println(listDb.get(i));
            ServerConnect.serverList.get(currentCountClient).send(listDb.get(i));
        }

    }*/
    @Override
    public void searchTotal(){
        try {

            ServerConnect.serverList.get(currentCountClient).send(  dbHandler.totalDeposit(Integer.parseInt(in.readLine())));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void statusUser(){
        String[] subStr;

        try {

            subStr = in.readLine().split(" ");
            dbHandler.statusUser(Integer.parseInt(subStr[0]),subStr[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void redactionUser() {
        String[] subStr;
        try {
            subStr = in.readLine().split(" ");
            dbHandler.redactionUser(Integer.parseInt(subStr[0]),subStr[1],subStr[2], Integer.parseInt(subStr[3]));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void showAllUsers(){
        LinkedList<String> list = dbHandler.showAllUsers();
        ServerConnect.serverList.get(currentCountClient).send(String.valueOf(list.size()));
        for(String s:list){

            ServerConnect.serverList.get(currentCountClient).send(s);

        }
    }
    @Override
    public void showAllContribution(){
        LinkedList<String> list = dbHandler.showAllContribution();
        ServerConnect.serverList.get(currentCountClient).send(String.valueOf(list.size()));
        for(String s:list){

            ServerConnect.serverList.get(currentCountClient).send(s);

        }
    }

    @Override
    public void showAllContributionInHistory(){
        LinkedList<String> list = dbHandler.showAllContributionInHistory();
        ServerConnect.serverList.get(currentCountClient).send(String.valueOf(list.size()));
        for(String s:list){

            ServerConnect.serverList.get(currentCountClient).send(s);

        }
    }

    @Override
    public void updateUser(){
        String[] str;

        try {
            str=in.readLine().split(" ");
            dbHandler.updateUser(Integer.parseInt(str[0]),str[1],str[2],str[3],str[4]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void getFullParamsUser(){
        try {

            ServerConnect.serverList.get(currentCountClient).send(dbHandler.currentUserData(Integer.parseInt(in.readLine())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteContributionById(){
        try {
            dbHandler.deleteContributionByDeposit(Integer.parseInt(in.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void searchContribution() throws IOException {

        String[] str;
        str=in.readLine().split(" ");


        LinkedList<String> list = dbHandler.showContributionByDeposit(Integer.parseInt(str[0]),Integer.parseInt(str[1]));
        ServerConnect.serverList.get(currentCountClient).send(String.valueOf(list.size()));
        for(String s:list){

            ServerConnect.serverList.get(currentCountClient).send(s);

        }
    }
    @Override
    public void showAllWorkers(){
        LinkedList<String> list = dbHandler.showAllWorkers();
        ServerConnect.serverList.get(currentCountClient).send(String.valueOf(list.size()));
        for(String s:list){

            ServerConnect.serverList.get(currentCountClient).send(s);

        }
    }
    @Override
    public void showContributionById() throws IOException {
        LinkedList<String> list = dbHandler.showContributionById(Integer.parseInt(in.readLine()));
        ServerConnect.serverList.get(currentCountClient).send(String.valueOf(list.size()));
        for(String s:list){

            ServerConnect.serverList.get(currentCountClient).send(s);

        }

    }
    @Override
    public void addInDatabaseContribution(){
        String[] subStr;
        try {
            subStr = in.readLine().split(" ");
            if(subStr.length>6 &&  validateInput(subStr[1],subStr[2],subStr[3],subStr[4],subStr[5],subStr[6])) {
                if (dbHandler.addContributionInDb(Integer.parseInt(subStr[0]), Integer.parseInt(subStr[1]), subStr[2], Integer.parseInt(subStr[3]), subStr[4], subStr[5], subStr[6])) {
                    ServerConnect.serverList.get(currentCountClient).send("1");
                } else {
                    ServerConnect.serverList.get(currentCountClient).send("0");
                }
            }
            else{
                ServerConnect.serverList.get(currentCountClient).send("0");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean validateInput(String deposit,String percent,String year,String currencyDb,String collectMoneyDb,String  typeDepositDb){

        if(!deposit.equals("") || !percent.equals("") || !year.equals("") || !currencyDb.equals("") ||
                !collectMoneyDb.equals("") || !typeDepositDb.equals("")) {

            try {
                Double.parseDouble(percent);
            } catch (NumberFormatException | NullPointerException nfe) {
                return false;
            }
            if (!deposit.chars().allMatch(Character::isDigit)) {
                return false;
            }
            if (!year.chars().allMatch(Character::isDigit)) {
                return false;
            }
        }else {
            return false;
        }


        return true;
    }
    @Override
    public void registrationUser(){
        String[] subStr;
        try {
            subStr = in.readLine().split(" ");
            if(dbHandler.addUserInDb(subStr[0], subStr[1],"0",subStr[2],subStr[3])){
                ServerConnect.serverList.get(currentCountClient).send("1");
            }
            else{
                ServerConnect.serverList.get(currentCountClient).send("0");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void findUserInDataBase(){
        try {
            String[] subStr = in.readLine().split(" ");
            String check= dbHandler.authUser(subStr[0], subStr[1]);
            if (!check.equals("false")) {
                ServerConnect.serverList.get(currentCountClient).send("1");
                ServerConnect.serverList.get(currentCountClient).send(check);
            } else {
                ServerConnect.serverList.get(currentCountClient).send("0");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
