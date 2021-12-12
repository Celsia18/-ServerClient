package database;

import constParams.Const;


import java.sql.*;
import java.util.LinkedList;

public class DataBaseHandler   {
    private  Connection connection;
    private  Statement statement;


    public DataBaseHandler() {
        connectionToDb();
        createTable(connection,statement);
    }



    public void connectionToDb(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(Const.HOST_DATABASE+Const.NAME_DATABASE,
                    Const.USER_DATABASE,
                    Const.PASSWORD_DATABASE);
            statement= connection.createStatement();

            System.out.println("Database connection is done");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createTable(Connection connection, Statement statement){
        new TablesDatabase(statement,connection);
    }



    public boolean addUserInDb(String emailDb, String passwordDb, String rollDb,String name,String lastName){
          if(!checkUserIndDb(emailDb)) {
              return false;
          }

            try {
                String query = " insert into users_bank (email, password,roll,name,lastName )"
                        + " values ( ?, ?,?,?,?)";
                
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                preparedStmt.setString (1, emailDb);
                preparedStmt.setString (2, passwordDb);
                preparedStmt.setString (3, rollDb);
                preparedStmt.setString (4, name);
                preparedStmt.setString (5, lastName);


                preparedStmt.executeUpdate();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        return true;
    }
  
    private boolean checkUserIndDb(String emailUserDb){
        String query = "SELECT " + Const.ID  + " FROM " + Const.USERS_TABLE +
                " WHERE " + Const.EMAIL + " = " + "'" + emailUserDb + "'";
        ResultSet rs = null;
        int idInDb=0;
        try {
            rs = statement.executeQuery(query);
            while (rs.next()) {
               idInDb =  rs.getInt(Const.ID);

            }
            if(idInDb==0){
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public String authUser(String email,String password) {
        String currentUser="";
        try {
            String query = "SELECT " + Const.EMAIL + "," + Const.PASSWORD+","+Const.ID+","+Const.ROLL + " FROM " + Const.USERS_TABLE +
                    " WHERE " + Const.EMAIL + " = " + "'" + email + "'" + " AND " + Const.PASSWORD + " = " + "'" + password + "'";

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (!rs.getString(Const.EMAIL).equals("") &&
                        !rs.getString(Const.PASSWORD).equals("")) {
                    currentUser+=rs.getString(Const.ID)+" ";
                    currentUser+=rs.getString(Const.ROLL);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(currentUser.equals("")) {
            return "false";
        }
        else {
            return currentUser;
        }
    }

    public boolean addContributionInDb(int userIdDb, int depositDb , String percent, int countYearDb ,String currencyDb
    ,String collectMoneyDb,String typeDepositDb){

        try {
            String query = " insert into " +Const.CONTRIBUTION_TABLE + " (userId, depositAmount,percent ,countYEAR,currency,collectMoney,typeDeposit )"
                    + " values ( ?, ?,?,?,?,?,?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setInt (1, userIdDb);
            preparedStmt.setInt (2, depositDb);
            preparedStmt.setString (3, percent);
            preparedStmt.setInt (4, countYearDb);
            preparedStmt.setString (5, currencyDb);
            preparedStmt.setString (6, collectMoneyDb);
            preparedStmt.setString (7, typeDepositDb);


            preparedStmt.executeUpdate();




            System.out.println("Данные в "+Const.CONTRIBUTION_TABLE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        double sumInOneYears =((Double.parseDouble(percent) * depositDb) /100)*countYearDb;

        addInTotal(countModel(),sumInOneYears);


        return true;
    }

    private void addInTotal(int id,double amountDepositTotal){
        try {
            String query = " insert into " +Const.TOTAL_DEPOSIT + " (idDeposit, amountDepositTotal)"
                    + " values ( ?, ?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setInt (1, id);
            preparedStmt.setDouble (2, amountDepositTotal);


            preparedStmt.executeUpdate();




            System.out.println("Данные в "+Const.TOTAL_DEPOSIT);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }



    public LinkedList<String> showContributionById(int idUser){

        LinkedList<String> list = new LinkedList<>();
        String query = "SELECT "+Const.ID_CONTRIBUTION+" , " + Const.DEPOSIT_AMOUNT+" , "+Const.PERCENT+" , "
                +Const.COUNT_YEAR+" ," +Const.CURRENCY+" , " +Const.COLLECT_MONEY+" , " +Const.TYPE_DEPOSIT+ " FROM " + Const.CONTRIBUTION_TABLE +
                " WHERE " + Const.USER_ID + " = " + "'" + idUser + "'" + " AND " + Const.ID_CONTRIBUTION + " NOT IN  (select " + Const.ID_CONTRIBUTION_TOTAL + " from " + Const.HISTORY_CONTRIBUTION + ")";
        ResultSet rs = null;
        String contribution="";
        try {
            rs = statement.executeQuery(query);

            while (rs.next()) {

                contribution+=rs.getString(Const.ID_CONTRIBUTION)+" ";
                contribution+=rs.getString(Const.DEPOSIT_AMOUNT)+" ";
                contribution+=rs.getString(Const.PERCENT)+" ";
                contribution+=rs.getString(Const.COUNT_YEAR)+" ";
                contribution+=rs.getString(Const.CURRENCY)+" ";
                contribution+=rs.getString(Const.COLLECT_MONEY)+" ";
                contribution+=rs.getString(Const.TYPE_DEPOSIT)+" ";
                list.add(contribution);

                contribution="";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public LinkedList<String> showContributionByDeposit(int depositDb,int idUser){
        LinkedList<String> list = new LinkedList<>();
        String query = "SELECT "+Const.ID_CONTRIBUTION+" , " + Const.DEPOSIT_AMOUNT+" , "+Const.PERCENT+" , "
                +Const.COUNT_YEAR+" ," +Const.CURRENCY+" , " +Const.COLLECT_MONEY+" , " +Const.TYPE_DEPOSIT+ " FROM " + Const.CONTRIBUTION_TABLE +
                " WHERE " + Const.DEPOSIT_AMOUNT + " = " + "'" + depositDb + "'" +" AND "+ Const.USER_ID + " = " + "'" + idUser + "'" + " AND " + Const.ID_CONTRIBUTION + " NOT IN  (select " + Const.ID_CONTRIBUTION_TOTAL + " from " + Const.HISTORY_CONTRIBUTION + ")";
        ResultSet rs = null;
        String contribution="";
        try {
            rs = statement.executeQuery(query);

            while (rs.next()) {

                contribution+=rs.getString(Const.ID_CONTRIBUTION)+" ";
                contribution+=rs.getString(Const.DEPOSIT_AMOUNT)+" ";
                contribution+=rs.getString(Const.PERCENT)+" ";
                contribution+=rs.getString(Const.COUNT_YEAR)+" ";
                contribution+=rs.getString(Const.CURRENCY)+" ";
                contribution+=rs.getString(Const.COLLECT_MONEY)+" ";
                contribution+=rs.getString(Const.TYPE_DEPOSIT)+" ";
                list.add(contribution);
                contribution="";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public int countModel()  {
     LinkedList<String> listContribution =showAllContribution();
        String[] subStr;
        subStr = listContribution.get(listContribution.size()-1).split(" ");


     return Integer.parseInt(subStr[0]);
    }

    public void deleteContributionByDeposit(int id)  {

        String selectSQL = "DELETE FROM "+Const.CONTRIBUTION_TABLE +  " WHERE id = ?";
        try {
            connection.prepareStatement(selectSQL);
            PreparedStatement preparedStmt = connection.prepareStatement(selectSQL);
            preparedStmt.setInt(1, id);
            preparedStmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public String totalDeposit(int id){
        String selectSQL = "SELECT "+Const.AMOUNT_TOTAL_DEPOSIT +" FROM "+Const.TOTAL_DEPOSIT  +  " WHERE idDeposit = "+ "'" + id + "'" ;
        ResultSet rs = null;
        String contribution="";
        try {
            rs = statement.executeQuery(selectSQL);

            while (rs.next()) {
                contribution+=rs.getString(Const.AMOUNT_TOTAL_DEPOSIT);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contribution;

    }

    public String currentUserData(int id){
          Connection connection = null;
          Statement statement = null;

        try {
            connection = this.connection;
            statement= connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        String currentUser="";
        try {
            String query = "SELECT " + Const.NAME_USER + " , " + Const.LAST_NAME_USER+" , "+Const.PASSWORD +" , "+Const.EMAIL+" , "+Const.ROLL+ " FROM " + Const.USERS_TABLE +
                    " WHERE " + Const.ID + " = " + "'" + id + "'";

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {

                    currentUser+=rs.getString(Const.NAME_USER)+" ";
                    currentUser+=rs.getString(Const.LAST_NAME_USER)+" ";
                    currentUser+=rs.getString(Const.PASSWORD)+" ";
                    currentUser+=rs.getString(Const.EMAIL)+" ";
                    currentUser+=rs.getString(Const.ROLL)+" ";
                    currentUser+=id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return currentUser;

    }

    public  void updateUser(int id,String name ,String lastName,String email ,String password)  {

        String query = "UPDATE users_bank SET name  = ?, lastName = ? ,email = ?,password = ? WHERE id = ?";
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString   (1, name);
            preparedStmt.setString(2, lastName);
            preparedStmt.setString(3, email);
            preparedStmt.setString(4, password);
            preparedStmt.setInt(5, id);
            preparedStmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void statusUser(int id ,String position){
        try {
            String query = " insert into " +Const.WORKER_TABLE + " (idUserWorker,position )"
                    + " values ( ?, ?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setInt (1, id);
            preparedStmt.setString (2, position);


            preparedStmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public LinkedList<String>  showAllWorkers(){
        LinkedList<String> list = new LinkedList<>();
        String query = "SELECT " + Const.ID_USER_WORKER+" , "+Const.POSITION+ " FROM " + Const.WORKER_TABLE ;

        
        String contribution="";
        try {
            ResultSet rs  = statement.executeQuery(query);

            while (rs.next()) {
                
                contribution+=currentUserData(Integer.parseInt(rs.getString(Const.ID_USER_WORKER)))+" ";
                contribution+=rs.getString(Const.POSITION);
                System.out.println(contribution);
                list.add(contribution);

                contribution="";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;

    }

    public LinkedList<String> showAllContribution() {
        LinkedList<String> list = new LinkedList<>();
  /*      String query = "SELECT "+Const.ID_CONTRIBUTION+" , " + Const.DEPOSIT_AMOUNT+" , "+Const.PERCENT+" , "
                 +Const.TYPE_DEPOSIT+ " FROM " + Const.CONTRIBUTION_TABLE ;*/
        String query = "SELECT "+Const.ID_CONTRIBUTION+" , " + Const.DEPOSIT_AMOUNT+" , "+Const.PERCENT+" , "
                +Const.TYPE_DEPOSIT+ " FROM " + Const.CONTRIBUTION_TABLE + " WHERE " + Const.ID_CONTRIBUTION +
                " NOT IN  (select " + Const.ID_CONTRIBUTION_TOTAL + " from " + Const.HISTORY_CONTRIBUTION + ")";


        ResultSet rs = null;
        String contribution="";
        try {
            rs = statement.executeQuery(query);

            while (rs.next()) {

                contribution+=rs.getString(Const.ID_CONTRIBUTION)+" ";
                contribution+=rs.getString(Const.DEPOSIT_AMOUNT)+" ";
                contribution+=rs.getString(Const.PERCENT)+" ";
                contribution+=rs.getString(Const.TYPE_DEPOSIT)+" ";
                list.add(contribution);
                contribution="";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;


    }

    public LinkedList<String> showAllContributionInHistory() {
        LinkedList<String> list = new LinkedList<>();

        String query = "SELECT "+Const.ID_CONTRIBUTION+" , " + Const.DEPOSIT_AMOUNT+" , "+Const.PERCENT+" , "
                +Const.TYPE_DEPOSIT+ " FROM " + Const.CONTRIBUTION_TABLE + " WHERE " + Const.ID_CONTRIBUTION +
                " IN  (select " + Const.ID_CONTRIBUTION_TOTAL + " from " + Const.HISTORY_CONTRIBUTION + ")";


        ResultSet rs = null;
        String contribution="";
        try {
            rs = statement.executeQuery(query);

            while (rs.next()) {

                contribution+=rs.getString(Const.ID_CONTRIBUTION)+" ";
                contribution+=rs.getString(Const.DEPOSIT_AMOUNT)+" ";
                contribution+=rs.getString(Const.PERCENT)+" ";
                contribution+=rs.getString(Const.TYPE_DEPOSIT)+" ";
                list.add(contribution);
                contribution="";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;


    }


    public LinkedList<String> showAllUsers() {

        LinkedList<String> list = new LinkedList<>();
        String query = "SELECT "+Const.ID+" , " + Const.EMAIL+" , "+Const.PASSWORD+" , "
                +Const.ROLL+ " FROM " + Const.USERS_TABLE ;
        ResultSet rs = null;
        String contribution="";
        try {
            rs = statement.executeQuery(query);

            while (rs.next()) {

                contribution+=rs.getString(Const.ID)+" ";
                contribution+=rs.getString(Const.EMAIL)+" ";
                contribution+=rs.getString(Const.PASSWORD)+" ";
                contribution+=rs.getString(Const.ROLL)+" ";
                list.add(contribution);
                contribution="";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;

    }

   /* public void statusUser(int id, String status){
        String query = "insert into " + Const.WORKER_TABLE + " ( idUserWorker )" + " (position )";
        try {
            preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, email);
            preparedStmt.setString(2, password);
            preparedStmt.setInt   (3, roll);
            // execute the java preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }*/

    public void redactionUser(int idUser, String email,String password,int roll) {
        String query = "update "+Const.USERS_TABLE+" SET   email =?, password =?, roll=? WHERE " + Const.ID + " = " + "'" + idUser + "'";
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, email);
            preparedStmt.setString(2, password);
            preparedStmt.setInt   (3, roll);
            // execute the java preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void addHistoryDeposit(int id){
        try{
        String query = " insert into " +Const.HISTORY_CONTRIBUTION + " (idDeposit )"
                + " values (?)" /* + "delete from " + Const.CONTRIBUTION_TABLE
                    + " WHERE id IN (select idDeposit from " + Const.HISTORY_CONTRIBUTION + ")"*/;
        PreparedStatement preparedStmt = connection.prepareStatement(query);

        preparedStmt.setInt (1, id);

        preparedStmt.executeUpdate();

    } catch (SQLException throwables) {
        throwables.printStackTrace();
     }
    }

    public LinkedList<String> showHistory(){
        LinkedList<String> list = new LinkedList<>();
        String query = "SELECT "+Const.ID_CONTRIBUTION_HISTORY+ " FROM " + Const.HISTORY_CONTRIBUTION ;
        ResultSet rs = null;
        String contribution="";
        try {
            rs = statement.executeQuery(query);

            while (rs.next()) {

                contribution+=rs.getString(Const.ID_CONTRIBUTION_HISTORY);
                list.add(contribution);
                contribution="";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

   /* public LinkedList<String> showAllContributionInHistory() {
        LinkedList<String> list = new LinkedList<>();
  *//*      String query = "SELECT "+Const.ID_CONTRIBUTION+" , " + Const.DEPOSIT_AMOUNT+" , "+Const.PERCENT+" , "
                 +Const.TYPE_DEPOSIT+ " FROM " + Const.CONTRIBUTION_TABLE ;*//*
        String query = "SELECT "+Const.ID_CONTRIBUTION+" , " + Const.DEPOSIT_AMOUNT+" , "+Const.PERCENT+" , "
                +Const.TYPE_DEPOSIT+ " FROM " + Const.CONTRIBUTION_TABLE + " WHERE " + Const.ID_CONTRIBUTION*//* +
                " NOT IN  (select " + Const.ID_CONTRIBUTION_TOTAL + " from " + Const.HISTORY_CONTRIBUTION + ")"*//*;


        ResultSet rs = null;
        String contribution="";
        try {
            rs = statement.executeQuery(query);

            while (rs.next()) {

                contribution+=rs.getString(Const.ID_CONTRIBUTION)+" ";
                contribution+=rs.getString(Const.DEPOSIT_AMOUNT)+" ";
                contribution+=rs.getString(Const.PERCENT)+" ";
                contribution+=rs.getString(Const.TYPE_DEPOSIT)+" ";
                list.add(contribution);
                contribution="";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;


    }
*/

   /* public void showContributionByIdHistory(LinkedList<String> list ,int id){

        String query = "SELECT "+Const.ID_CONTRIBUTION+" , " + Const.DEPOSIT_AMOUNT+" , "+Const.PERCENT+" , "
                +Const.COUNT_YEAR+" ," +Const.CURRENCY+" , " +Const.COLLECT_MONEY+" , " +Const.TYPE_DEPOSIT+ " FROM " + Const.CONTRIBUTION_TABLE +
                " WHERE " + Const.ID_CONTRIBUTION + " = " + "'" + id + "'";
        ResultSet rs = null;
        String contribution="";
        try {
            rs = statement.executeQuery(query);

            while (rs.next()) {

                contribution+=rs.getString(Const.ID_CONTRIBUTION)+" ";
                contribution+=rs.getString(Const.DEPOSIT_AMOUNT)+" ";
                contribution+=rs.getString(Const.PERCENT)+" ";
                contribution+=rs.getString(Const.COUNT_YEAR)+" ";
                contribution+=rs.getString(Const.CURRENCY)+" ";
                contribution+=rs.getString(Const.COLLECT_MONEY)+" ";
                contribution+=rs.getString(Const.TYPE_DEPOSIT)+" ";
                list.add(contribution);

                contribution="";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }*/
}
