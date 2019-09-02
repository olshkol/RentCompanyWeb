package com.olshkol.rentcompany.service;

import com.olshkol.rentcompany.dto.*;
import com.olshkol.rentcompany.documents.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RentCompanyService {
    int getGasPrice(); //price for one liter of a company
    void setGasPrice(int price); //setting price of one liter
    int getFinePercent(); //fine of one delay day
    void setFinePercent(int finePercent); //setting fine
    CarsReturnCode addModel(ModelDTO model); //OK, MODEL_EXISTS
    CarsReturnCode addCar(CarDTO car); //OK, CAR_EXISTS, NO_MODEL
    CarsReturnCode addDriver(DriverDTO driver); //OK, DRIVER_EXISTS
    ModelDTO getModel(String modelName);
    CarDTO getCar(String regNumber);
    DriverDTO getDriver(long licenseId);

    CarsReturnCode rentCar(String regNumber, long licenseId, LocalDate rentDate, int rentDays); //OK, NO_CAR, NO_DRIVER, CAR_REMOVED, CAR_IN_USE
    List<CarDTO> getCarsByDriver(long licenseId);
    List<DriverDTO> getDriversByCar(String regNumber);
    List<CarDTO> getCarsByModel(String modelName);
    Set<ModelDTO> getModelsByDriver(long licenseId);
    List<RentRecordDTO> getRentRecordsAtDates(LocalDate from, LocalDate to);
    List<CarDTO> getAllFreeCars();
//
//    RemovedCarData removeCar(String regNumber);
//    List<RemovedCarData> removeModel(String modelName);
    RentRecord returnCar(String regNumber, long licenseId, LocalDate returnDate, int damages, int tankPercent);
//
//    List<String> getMostPopularCarModels(LocalDate dateFrom, LocalDate dateTo, int ageFrom, int ageTo);
//    List<String> getMostProfitableCarModels(LocalDate dateFrom, LocalDate dateTo);
//    List<Driver> getMostActiveDrivers();


}
