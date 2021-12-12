package database;

import constParams.Const;

import java.sql.*;

public class TablesDatabase {
     private final Statement statement;
     private final Connection connection;

    public TablesDatabase(Statement statement, Connection connection) {
        this.statement = statement;
        this.connection = connection;

        createTableWorker();
        createTotalDeposit();
        createTableHistoryContribution();
        addTableUserInDateBase();
        addTableContribution();
    }

    public void createTableWorker(){

        if(tableExists(Const.WORKER_TABLE)) {
            try {
                String SQL = "CREATE TABLE "+Const.WORKER_TABLE +
                        "( " +
                        " id  SERIAL PRIMARY KEY," +
                        " idUserWorker INTEGER, " +
                        " position VARCHAR (50) " +
                        ")";

                statement.executeUpdate(SQL);
                System.out.println("Таблица  была создана ! " +Const.WORKER_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTotalDeposit(){
        if(tableExists(Const.TOTAL_DEPOSIT)) {
            try {
                String SQL = "CREATE TABLE "+Const.TOTAL_DEPOSIT +
                        "( " +
                        " id  SERIAL PRIMARY KEY," +
                        " idDeposit INTEGER, " +
                        " amountDepositTotal INTEGER " +
                        ")";

                statement.executeUpdate(SQL);
                System.out.println("Таблица  была создана ! " +Const.TOTAL_DEPOSIT);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTableHistoryContribution(){

        if(tableExists(Const.HISTORY_CONTRIBUTION)) {
            try {
                String SQL = "CREATE TABLE "+Const.HISTORY_CONTRIBUTION +
                        "( " +
                        " id  SERIAL PRIMARY KEY," +
                        " idDeposit INTEGER, " +
                        " idUserHistory INTEGER " +

                        ")";

                statement.executeUpdate(SQL);
                System.out.println("Таблица  была создана ! " +Const.HISTORY_CONTRIBUTION);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTableUserInDateBase(){
        if(tableExists(Const.USERS_TABLE)) {
            try {
                String SQL = "CREATE TABLE "+Const.USERS_TABLE +
                        "( " +
                        " id  SERIAL PRIMARY KEY," +
                        " email VARCHAR (50), " +
                        " name VARCHAR (50), " +
                        " lastName VARCHAR (50), " +
                        " password VARCHAR (50), " +
                        " roll VARCHAR (50)"+
                        ")";

                statement.executeUpdate(SQL);
                System.out.println("Таблица с users была создана ! "+Const.USERS_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTableContribution(){
        if(tableExists(Const.CONTRIBUTION_TABLE)) {
            try {
                String SQL = "CREATE TABLE "+Const.CONTRIBUTION_TABLE +
                        "( " +
                        " id  SERIAL PRIMARY KEY," +
                        " userId INTEGER ,"+
                        " depositAmount INTEGER , " +
                        " percent VARCHAR (50) , " +
                        " countYEAR INTEGER,"+
                        " currency VARCHAR (50), "+
                        " collectMoney VARCHAR (50), "+
                        " typeDeposit VARCHAR (50) "+

                        ")";

                statement.executeUpdate(SQL);
                System.out.println("Таблица с users была создана ! "+Const.CONTRIBUTION_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tableExists(String nameTable){

        try{
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, nameTable, null);
            rs.last();
            return rs.getRow() <= 0;
        }catch(SQLException ignored){

        }
        return true;
    }
}
