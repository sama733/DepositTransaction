package ir.dotin.school.core.server;

import java.math.BigDecimal;

public class Deposit {


    private String customerName;
    private String customerId;
    private BigDecimal initialBalance;
    private BigDecimal upperBound;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public  void doDepoit(BigDecimal amount) {
        BigDecimal previousBalance = getInitialBalance();
        Thread.yield();
        setInitialBalance(amount.add(previousBalance));
    }

    public  void doWithdrawl(BigDecimal amount) {
        setInitialBalance(getInitialBalance().subtract(amount));
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "customerName='" + customerName + '\'' +
                ", customerId='" + customerId + '\'' +
                ", initialBalance=" + initialBalance +
                ", upperBound=" + upperBound +
                '}';
    }

}


