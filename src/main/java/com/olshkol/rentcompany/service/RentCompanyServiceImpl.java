package com.olshkol.rentcompany.service;

import com.olshkol.rentcompany.documents.*;
import com.olshkol.rentcompany.dto.*;
import com.olshkol.rentcompany.repository.CarRepository;
import com.olshkol.rentcompany.repository.DriverRepository;
import com.olshkol.rentcompany.repository.ModelRepository;
import com.olshkol.rentcompany.repository.RentRecordRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static com.olshkol.rentcompany.dto.CarsReturnCode.*;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RentCompanyServiceImpl extends AbstractRentCompanyService {
    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final ModelRepository modelRepository;
    private final RentRecordRepository rentRecordRepository;

    private final ModelMapper mapper;

    @Transactional
    @Override
    public CarsReturnCode addModel(ModelDTO model) {
        if (modelRepository.existsById(model.getModelName()))
            return MODEL_EXISTS;
        modelRepository.save(mapper.map(model, Model.class));
        return OK;
    }

    @Override
    public ModelDTO getModel(String modelName) {
        return modelRepository
                .findById(modelName)
                .map(model -> mapper.map(model, ModelDTO.class))
                .orElse(null);
    }

    @Transactional
    @Override
    public CarsReturnCode addDriver(DriverDTO driver) {
        if (driverRepository.existsById(driver.getLicenseId()))
            return DRIVER_EXISTS;
        driverRepository.save(mapper.map(driver, Driver.class));
        return OK;
    }

    @Override
    public DriverDTO getDriver(long licenseId) {
        return driverRepository
                .findById(licenseId)
                .map(driver -> mapper.map(driver, DriverDTO.class))
                .orElse(null);
    }

    @Transactional
    @Override
    public CarsReturnCode addCar(CarDTO car) {
        if (carRepository.existsById(car.getRegNumber()))
            return CAR_EXISTS;

        String modelName = car.getModel().getModelName();
        Optional<Model> model = modelRepository.findById(modelName);
        if (model.isEmpty())
            return NO_MODEL;

        carRepository.save(mapper.map(car, Car.class));

        model.get().getCarsId().add(car.getRegNumber());
        modelRepository.save(model.get());
        return OK;
    }

    @Override
    public CarDTO getCar(String regNumber) {
        return carRepository
                .findById(regNumber)
                .map(car -> mapper.map(car, CarDTO.class))
                .orElse(null);
    }

    @Override
    public List<DriverDTO> getDriversByCar(String regNumber) {
        Optional<Car> carFromDB = carRepository.findById(regNumber);
        List<DriverDTO> driversByCar = new ArrayList<>();
        carFromDB.ifPresent(car -> rentRecordRepository.findAllById(car.getRentRecordIds())
                .iterator()
                .forEachRemaining(rentRecord -> driversByCar.add(mapper.map(rentRecord.getDriver(), DriverDTO.class))));
        return driversByCar;
    }

    @Override
    public List<CarDTO> getCarsByDriver(long licenseId) {
        Optional<Driver> driverFromDB = driverRepository.findById(licenseId);
        List<CarDTO> carsByDriver = new ArrayList<>();
        driverFromDB.ifPresent(driver -> rentRecordRepository.findAllById(driver.getRentRecordIds())
                .iterator()
                .forEachRemaining(rentRecord -> carsByDriver.add(mapper.map(rentRecord.getCar(),CarDTO.class))));
        return carsByDriver;
    }

    @Override
    public List<CarDTO> getCarsByModel(String modelName) {
        Optional<Model> modelFromDB = modelRepository.findById(modelName);
        List<CarDTO> carsByModel = new ArrayList<>();
        modelFromDB.ifPresent(model -> carRepository.findAllById(model.getCarsId())
                .iterator()
                .forEachRemaining(car -> carsByModel.add(mapper.map(car, CarDTO.class))));
        return carsByModel;
    }

    @Override
    public Set<ModelDTO> getModelsByDriver(long licenseId) {
        Optional<Driver> driverFromDB = driverRepository.findById(licenseId);
        Set<ModelDTO> modelsByDriver = new HashSet<>();
        driverFromDB.ifPresent(driver -> rentRecordRepository.findAllById(driver.getRentRecordIds())
                .iterator()
                .forEachRemaining(rentRecord ->
                        modelsByDriver.add(mapper.map(rentRecord.getCar().getModel(), ModelDTO.class))));
        return modelsByDriver;
    }

    @Transactional
    @Override
    public CarsReturnCode rentCar(String regNumber, long licenseId, LocalDate rentDate, int rentDays) {
        Optional<Driver> driverFromDB = driverRepository.findById(licenseId);
        if (driverFromDB.isEmpty())
            return NO_DRIVER;
        Driver rentDriver = driverFromDB.get();

        Optional<Car> carById = carRepository.findById(regNumber);
        if (carById.isEmpty())
            return NO_CAR;
        Car rentCar = carById.get();
        if (rentCar.isInUse())
            return CAR_IN_USE;
        if (rentCar.isRemoved())
            return CAR_REMOVED;

        rentCar.setInUse(true);
        RentRecord rentRecord = rentRecordRepository.save(new RentRecord(rentCar, rentDriver, rentDate, rentDays));
        rentCar.getRentRecordIds().add(rentRecord.getContractNum());
        carRepository.save(rentCar);

        rentDriver.getRentRecordIds().add(rentRecord.getContractNum());
        driverRepository.save(rentDriver);

        return OK;
    }

    @Override
    public List<RentRecordDTO> getRentRecordsAtDates(LocalDate from, LocalDate to) {
        return rentRecordRepository.findAllByRentDateBetween(from, to)
                .stream()
                .map(rentRecord -> mapper.map(rentRecord, RentRecordDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDTO> getAllFreeCars() {
        return carRepository.findAll().stream()
                .filter(car -> !car.isInUse())
                .map(car -> mapper.map(car, CarDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RentRecord returnCar(String regNumber, long licenseId, LocalDate returnDate, int damages, int tankPercent) {
        Optional<Car> carFromDB = carRepository.findById(regNumber);
        Optional<Driver> driverFromDB = driverRepository.findById(licenseId);
        if (carFromDB.isEmpty() || driverFromDB.isEmpty())
            return null;

        Car car = carFromDB.get();
        int size = car.getRentRecordIds().size();
        ObjectId lastRentRecordId = car.getRentRecordIds().get(size - 1);
        Optional<RentRecord> rentRecordFromDB = rentRecordRepository.findById(lastRentRecordId);
        if (rentRecordFromDB.isEmpty())
            return null;

        RentRecord rentRecord = rentRecordFromDB.get();
//        rentRecord.setDamages(damages);
//        setCarDamages(damages, car);
//
//        rentRecord.setTankPercent(tankPercent);
//        rentRecord.setReturnDate(returnDate);
//        setProfit(rentRecord, car);
//
//        car.setInUse(false);

        return rentRecord;
    }

//    private void setProfit(RentRecord rentRecord, Car car) {
//        Model model = models.get();
//        double cost = (model.getGasTank() * (rentRecord.getTankPercent() / 100.)) * gasPrice;
//
//        int realDays = Period.between(rentRecord.getRentDate(), rentRecord.getReturnDate()).getDays();
//        cost += model.getPriceDay() * rentRecord.getRentDays();
//
//        int diffDays = realDays - rentRecord.getRentDays();
//        if (diffDays > 0) {
//            cost += diffDays * ((finePercent / 100.) * model.getPriceDay() + model.getPriceDay());
//        }
//        rentRecord.setCost(cost);
//        rentRecord.setRentDays(realDays);
//    }
//
//    private void setCarDamages(int damages, Car car) {
//        if (damages >= 80)
//            car.setState(State.BAD);
//        else if (damages <= 40)
//            car.setState(State.EXCELLENT);
//        else car.setState(State.GOOD);
//    }
}
