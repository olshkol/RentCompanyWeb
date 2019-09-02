//package com.olshkol.rentcompany.service;
//
//import com.olshkol.rentcompany.dto.CarsReturnCode;
//import com.olshkol.rentcompany.dto.RemovedCarData;
//import com.olshkol.rentcompany.entities.RentRecord;
//import com.olshkol.rentcompany.entities.*;
//
//import java.time.LocalDate;
//import java.time.Period;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentNavigableMap;
//import java.util.concurrent.ConcurrentSkipListMap;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import static com.olshkol.rentcompany.Config.*;
//import static com.olshkol.rentcompany.dto.CarsReturnCode.*;
//
//public class RentCompanyOld extends AbstractRentCompany{
//    private static final long serialVersionUID = 6980361909005366533L;
//
//    private ConcurrentHashMap<String, Car> cars = new ConcurrentHashMap<>(); // key - carNumber, value - car
//    private ConcurrentHashMap<Long, Driver> drivers = new ConcurrentHashMap<>(); // key - licenseId
//    private ConcurrentHashMap<String, Model> models = new ConcurrentHashMap<>(); // key - modelName
//
//    private ConcurrentHashMap<String, List<Car>> modelCars = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<String, List<RentRecord>> carRecords = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Long, List<RentRecord>> driverRecords = new ConcurrentHashMap<>();
//    private ConcurrentNavigableMap<LocalDate, List<RentRecord>> records = new ConcurrentSkipListMap<>();
//
//
//    @Override
//    public CarsReturnCode addCar(Car car) {
//        if (cars.containsKey(car.getRegNumber())) {
//            if (!cars.get(car.getRegNumber()).isRemoved())
//                return CAR_EXISTS;
//            //TODO if car was removed, but adding new car with same RegNumber
//            cars.get(car.getRegNumber()).setRemoved(false);
//            return OK;
//
//        }
//        if (!models.containsKey(car.getModel()))
//            return NO_MODEL;
//
//        cars.put(car.getRegNumber(), car);
//        modelCars.get(car.getModel()).add(car);
//        return OK;
//    }
//

//
//    @Override
//    public RemovedCarData removeCar(String regNumber) {
//        if (!cars.containsKey(regNumber))
//            return null;
//        Car car = cars.get(regNumber);
//        if (car.isInUse() || car.isRemoved())
//            return null;
//        if (!car.isRemoved())
//            car.setRemoved(true);
//        List<RentRecord> removedRecords = carRecords.getOrDefault(regNumber, new ArrayList<>());
//        return new RemovedCarData(car, removedRecords);
//    }
//
//    @Override
//    public List<RemovedCarData> removeModel(String modelName) {
//        List<Car> carsByModel = modelCars.getOrDefault(modelName, new ArrayList<>());
//        if (carsByModel.isEmpty()) {
//            models.remove(modelName);
//            modelCars.remove(modelName);
//            return new ArrayList<>();
//        }
//        List<RemovedCarData> removedCarData = new ArrayList<>();
//        for (Car car : carsByModel) {
//            if (!car.isInUse()) {
//                car.setRemoved(true);
//                removedCarData.add(new RemovedCarData(car, carRecords.get(car.getRegNumber())));
//            }
//        }
//        return removedCarData;
//    }
//
//    @Override
//    public RentRecord returnCar(String regNumber,
//                                long licenseId,
//                                LocalDate returnDate,
//                                int damages,
//                                int tankPercent) {
//        List<RentRecord> rentRecords = carRecords.get(regNumber);
//        RentRecord rentRecord = rentRecords.get(rentRecords.size() - 1);
//
//        Car car = cars.get(regNumber);
//        rentRecord.setDamages(damages);
//        setCarDamages(damages, car);
//
//        rentRecord.setTankPercent(tankPercent);
//        rentRecord.setReturnDate(returnDate);
//        setProfit(rentRecord, car);
//
//        car.setInUse(false);
//
//        return rentRecord;
//    }
//
//    private void setProfit(RentRecord rentRecord, Car car) {
//        Model model = models.get(car.getModel());
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
//
//    private boolean isProperAge(RentRecord r, int fromAge, int toAge) {
//        LocalDate rentDate = r.getRentDate();
//        Driver driver = getDriver(r.getDriver());
//        int driverAge = rentDate.getYear() - driver.getBirthYear();
//        return driverAge >= fromAge && driverAge < toAge;
//    }
//
//    @Override
//    public List<String> getMostPopularCarModels(LocalDate dateFrom,
//                                                LocalDate dateTo,
//                                                int ageFrom,
//                                                int ageTo) {
//        return records.subMap(dateFrom, dateTo.plusDays(1)).values().stream()
//                .flatMap(List::stream)
//                .filter(rentRecord -> isProperAge(rentRecord, ageFrom, ageTo))
//                .collect(Collectors.groupingBy(
//                        rentRecord -> cars.get(rentRecord.getCar()).getModel(),
//                        Collectors.summingInt(RentRecord::getRentDays)))
//                .entrySet().stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//                .limit(COUNT_MOST_POPULAR_MODELS)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<String> getMostProfitableCarModels(LocalDate dateFrom,
//                                                   LocalDate dateTo) {
//        return records.subMap(dateFrom, dateTo.plusDays(1)).values().stream()
//                .flatMap(List::stream)
//                .collect(Collectors.groupingBy(
//                        rentRecord -> cars.get(rentRecord.getCar()).getModel(),
//                        Collectors.summingDouble(RentRecord::getCost)))
//                .entrySet().stream()
//                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
//                .limit(COUNT_MOST_PROFITABLE_CAR_MODELS)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Driver> getMostActiveDrivers() {
//        return drivers.values().stream().collect(Collectors.toMap(
//                driver -> driver,
//                driver -> driverRecords.getOrDefault(driver.getLicenseId(), new ArrayList<>()).size()
//        ))
//                .entrySet().stream()
//                .sorted(Map.Entry.<Driver, Integer>comparingByValue().reversed())
//                .limit(COUNT_MOST_ACTIVE_DRIVES)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
//    }
//
//    public long getCountCars() {
//        return cars.values().stream().filter(car -> !car.isRemoved()).count();
//    }
//}