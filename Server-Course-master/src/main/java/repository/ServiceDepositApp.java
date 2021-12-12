package repository;

import java.io.IOException;

public interface ServiceDepositApp {
   void findUserInDataBase();
   void registrationUser();
   void addInDatabaseContribution();
   void showContributionById() throws IOException;
   void searchContribution() throws IOException;
   void deleteContributionById();
   void getFullParamsUser();
   void updateUser();
   void showAllWorkers();
   void showAllContribution();
   void showAllUsers();
   void redactionUser();
   void statusUser();
   void searchTotal();
   void addContributionInHistoryDataBase();
   void showAllContributionInHistory();

}
