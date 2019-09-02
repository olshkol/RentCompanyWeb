package com.olshkol.rentcompany.service;

public abstract class AbstractRentCompanyService implements RentCompanyService {
    private static final long serialVersionUID = 6980361909005366533L;
    int finePercent; // percent of fine per delay day
    int gasPrice; // liter price of rent company

    public AbstractRentCompanyService() {
        finePercent = 15;
        gasPrice = 10;
    }

    public int getFinePercent() {
        return finePercent;
    }

    public void setFinePercent(int finePercent) {
        this.finePercent = finePercent;
    }

    public int getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(int gasPrice) {
        this.gasPrice = gasPrice;
    }

    @Override
    public String toString() {
        return "AbstractRentCompany{" +
                "finePercent=" + finePercent +
                ", gasPrice=" + gasPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractRentCompanyService)) return false;

        AbstractRentCompanyService that = (AbstractRentCompanyService) o;

        if (finePercent != that.finePercent) return false;
        return gasPrice == that.gasPrice;
    }

    @Override
    public int hashCode() {
        int result = finePercent;
        result = 31 * result + gasPrice;
        return result;
    }
}
