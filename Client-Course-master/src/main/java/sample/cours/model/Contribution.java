package sample.cours.model;



public class Contribution {


    private int id;
    private int depositAmount;
    private double percent;
    private int countYear;
    private String currency;
    private String collectMoney;
    private String typeDeposit;

    public Contribution(int id,int depositAmount, double percent, int countYear, String currency, String collectMoney, String typeDeposit) {
        this.id = id;
        this.depositAmount = depositAmount;
        this.percent = percent;
        this.countYear = countYear;
        this.currency = currency;
        this.collectMoney = collectMoney;
        this.typeDeposit = typeDeposit;
    }

    public Contribution(int id, int depositAmount, double percent,String typeDeposit) {
        this.id = id;
        this.depositAmount = depositAmount;
        this.percent = percent;
        this.typeDeposit = typeDeposit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollectMoney() {
        return collectMoney;
    }

    public void setCollectMoney(String collectMoney) {
        this.collectMoney = collectMoney;
    }

    public String getTypeDeposit() {
        return typeDeposit;
    }

    public void setTypeDeposit(String typeDeposit) {
        this.typeDeposit = typeDeposit;
    }

    public int getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(int depositAmount) {
        this.depositAmount = depositAmount;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getCountYear() {
        return countYear;
    }

    public void setCountYear(int countYear) {
        this.countYear = countYear;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Contribution : " +
                "depositAmount = " + depositAmount +
                ", percent = " + percent +
                ", countYear = " + countYear +
                ", currency = '" + currency + '\''+"\n";
    }
}
