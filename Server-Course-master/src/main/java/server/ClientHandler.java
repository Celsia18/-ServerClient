package server;

import database.DataBaseHandler;
import repository.ServiceDepositApp;
import service.ServiceDepositAppImplementation;


import java.io.*;
import java.net.Socket;


public class ClientHandler extends Thread  {
    private final Socket socket;
    private Boolean checkAuth =false;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final ServiceDepositApp serviceDepositAppImplementation;


    public ClientHandler(Socket socket,DataBaseHandler dataBaseHandler) throws IOException {

        this.socket = socket;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        int currentCountClient = ServerConnect.countClient++;
        serviceDepositAppImplementation = new ServiceDepositAppImplementation(dataBaseHandler,in, currentCountClient);
        start();

    }

    @Override
    public void run() {
        try {

            workWithDeposit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void workWithDeposit() throws IOException{

            while (true) {
                switch (in.readLine()) {

                    case "auth":
                        serviceDepositAppImplementation.findUserInDataBase();
                        break;

                    case "registration":
                        serviceDepositAppImplementation.registrationUser();
                        break;

                    case "signOut":
                        checkAuth = false;
                        break;

                    case "addContribution":
                        serviceDepositAppImplementation.addInDatabaseContribution();
                        break;

                    case "showContributionById":
                        serviceDepositAppImplementation.showContributionById();
                        break;

                    case "searchContributionByDepositAverage":
                        serviceDepositAppImplementation.searchContribution();
                        break;

                    case "deleteContributionByID":
                        serviceDepositAppImplementation.deleteContributionById();
                        break;
/*

                    case "showArchive":
                        serviceDepositAppImplementation.showArchive();
                        break;
*/


                    case "currentUserData":
                        serviceDepositAppImplementation.getFullParamsUser();
                        break;

                    case "updateUserData":
                        serviceDepositAppImplementation.updateUser();
                        break;

                    case "showWorker":

                        serviceDepositAppImplementation.showAllWorkers();
                        break;
                    case "statusUser":

                        serviceDepositAppImplementation.statusUser();
                        break;
                    case "showAllContribution":
                        serviceDepositAppImplementation.showAllContribution();
                        break;

                    case "showAllUser":
                        serviceDepositAppImplementation.showAllUsers();
                        break;

                    case "redactionUser":
                        serviceDepositAppImplementation.redactionUser();
                        break;

                    case "searchTotal":
                        serviceDepositAppImplementation.searchTotal();
                        break;

                    case "addInHistoryTable":
                        serviceDepositAppImplementation.addContributionInHistoryDataBase();
                        break;

                    case "showAllContributionInHistory":
                        serviceDepositAppImplementation.showAllContributionInHistory();
                        break;

            }
        }
    }


    public void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }
}
