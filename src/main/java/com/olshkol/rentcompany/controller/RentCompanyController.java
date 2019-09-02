package com.olshkol.rentcompany.controller;

import com.olshkol.rentcompany.dto.*;
import com.olshkol.rentcompany.service.RentCompanyService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/rentcompany")

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RentCompanyController {
    private static final Logger log = getLogger(RentCompanyController.class);

    private RentCompanyService rentCompanyService;

    @PostMapping("/models")
    public ResponseEntity<CarsReturnCode> addModel(@RequestBody ModelDTO model){
        CarsReturnCode body = rentCompanyService.addModel(model);
        log.info(String.format("add model=%s, CarsReturnCode=%s", model, body));
        return ResponseEntity.ok(body);
    }

    @GetMapping("/models")
    public ResponseEntity<ModelDTO> getModel(@RequestParam String modelName){
        ModelDTO model = rentCompanyService.getModel(modelName);
        log.info(String.format("get model by modelname=%s, model=%s", modelName, model));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/drivers")
    public ResponseEntity<CarsReturnCode> addDriver(@RequestBody DriverDTO driver){
        CarsReturnCode body = rentCompanyService.addDriver(driver);
        log.info(String.format("add driver=%s, CarsReturnCode=%s", driver, body));
        return ResponseEntity.ok(body);
    }

    @GetMapping("/drivers")
    public ResponseEntity<DriverDTO> getDriver(@RequestParam Long licenseId){
        DriverDTO driver = rentCompanyService.getDriver(licenseId);
        log.info(String.format("get driver by licenseId=%d, driver=%s", licenseId, driver));
        return ResponseEntity.ok(driver);
    }

    @PostMapping("/cars")
    public ResponseEntity<CarsReturnCode> addCar(@RequestBody CarDTO car){
        log.info(String.format("add car %s", car));
        return ResponseEntity.ok(rentCompanyService.addCar(car));
    }

    @GetMapping("/cars")
    public ResponseEntity<CarDTO> getCar(@RequestParam String regNumber){
        CarDTO car = rentCompanyService.getCar(regNumber);
        log.info(String.format("get car by regNumber=%s, car=%s", regNumber, car));
        return ResponseEntity.ok(car);
    }

    @GetMapping("/cars/models/{modelId}")
    public ResponseEntity<List<CarDTO>> getCarsByModel(@PathVariable("modelId") String modelName){
        List<CarDTO> carsModel = rentCompanyService.getCarsByModel(modelName);
        log.info(String.format("get cars by model=%s, cars=%s", modelName, carsModel));
        return ResponseEntity.ok(carsModel);
    }

    @GetMapping("cars/drivers/{driverId}")
    public ResponseEntity<List<CarDTO>> getCarsByDriver(@PathVariable("driverId") Long licenseId) {
        List<CarDTO> carsDriver = rentCompanyService.getCarsByDriver(licenseId);
        log.info(String.format("get cars by driver=%d, cars=%s", licenseId, carsDriver));
        return ResponseEntity.ok(carsDriver);
    }

    @GetMapping("drivers/cars/{carId}")
    public ResponseEntity<List<DriverDTO>> getDriversByCar(@PathVariable("carId") String regNumber){
        List<DriverDTO> driversCar = rentCompanyService.getDriversByCar(regNumber);
        log.info(String.format("get drivers by car=%s, drivers=%s", regNumber, driversCar));
        return ResponseEntity.ok(driversCar);
    }

    @PostMapping("/rentrecords")
    public ResponseEntity<CarsReturnCode> rentCar(@RequestParam String regNumber, @RequestParam Long licenseId,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate rentDate,
                                                  @RequestParam Integer rentDays){
        CarsReturnCode body = rentCompanyService.rentCar(regNumber, licenseId, rentDate, rentDays);
        log.info(String.format("rent car: regNumber=%s, licenseId=%d, rentDate=%s, rentDays=%d, CarsReturnCode=%s"
                , regNumber, licenseId, rentDate, rentDays, body));
        return ResponseEntity.ok(body);
    }

    @GetMapping("/rentrecords/between")
    public ResponseEntity<List<RentRecordDTO>> getRentRecordsAtDates(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to){
        List<RentRecordDTO> rentRecordsAtDates = rentCompanyService.getRentRecordsAtDates(from, to);
        log.info(String.format("get rentRecords between %s and %s, rentRecords=%s", from, to, rentRecordsAtDates));
        return ResponseEntity.ok(rentRecordsAtDates);
    }

    @GetMapping("/cars/filtered-free")
    public ResponseEntity<List<CarDTO>> getAllFreeCars(){
        log.info("get all free cars");
        return ResponseEntity.ok(rentCompanyService.getAllFreeCars());
    }

    @GetMapping("/models/drivers/{driverId}")
    public ResponseEntity<Set<ModelDTO>> getModelsByDriver(@PathVariable("driverId") Long licenseId){
        Set<ModelDTO> modelsByDriver = rentCompanyService.getModelsByDriver(licenseId);
        log.info(String.format("get models by driver=%s, models=%s", licenseId, modelsByDriver));
        return ResponseEntity.ok(modelsByDriver);
    }
}
