package com.olshkol.rentcompany.dto;

import com.olshkol.rentcompany.documents.Car;
import com.olshkol.rentcompany.documents.RentRecord;

import java.io.Serializable;
import java.util.List;


public class RemovedCarDataDTO implements Serializable {
    private Car car;
    private List<RentRecord> removedRecords;

    public RemovedCarDataDTO() {
    }

    public RemovedCarDataDTO(Car car, List<RentRecord> removedRecords) {
        this.car = car;
        this.removedRecords = removedRecords;
    }

    public Car getCar() {
        return car;
    }

    public List<RentRecord> getRemovedRecords() {
        return removedRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemovedCarDataDTO)) return false;

        RemovedCarDataDTO that = (RemovedCarDataDTO) o;

        if (car != null ? !car.equals(that.car) : that.car != null) return false;
        return removedRecords != null ? removedRecords.equals(that.removedRecords) : that.removedRecords == null;
    }

    @Override
    public int hashCode() {
        int result = car != null ? car.hashCode() : 0;
        result = 31 * result + (removedRecords != null ? removedRecords.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RemovedCarData{" +
                "car=" + car +
                ", removedRecords=" + removedRecords +
                '}';
    }
}
