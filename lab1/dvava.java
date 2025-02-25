package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Класс, представляющий хранилище топлива
class FuelStorage {
    private int fuelAmount; // Количество топлива в хранилище

    public FuelStorage(int initialFuel) {
        this.fuelAmount = initialFuel; // Инициализируем хранилище заданным количеством топлива
    }

    // Метод для получения топлива с синхронизацией, чтобы избежать проблем многопоточности
    public synchronized int getFuel(int amount) {
        if (fuelAmount >= amount) { // Если есть достаточно топлива
            fuelAmount -= amount; // Уменьшаем количество топлива
            return amount; // Возвращаем запрошенное количество
        } else { // Если топлива меньше, чем запрашивается
            int availableFuel = fuelAmount; // Сохраняем оставшееся топливо
            fuelAmount = 0; // Обнуляем хранилище
            return availableFuel; // Возвращаем, сколько есть
        }
    }

    // Метод для получения текущего количества топлива
    public synchronized int getFuelAmount() {
        return fuelAmount;
    }
}

// Класс транспорта, который доставляет топливо на электростанции
class Transport implements Runnable {
    private final FuelStorage fuelStorage; // Ссылка на хранилище топлива
    private final PowerStation[] stations; // Массив электростанций, которым нужно топливо

    public Transport(FuelStorage fuelStorage, PowerStation[] stations) {
        this.fuelStorage = fuelStorage;
        this.stations = stations;
    }

    // Метод доставки топлива на станцию
    public synchronized void deliverFuel(PowerStation station, int amount) {
        int fuelDelivered = fuelStorage.getFuel(amount); // Запрашиваем топливо из хранилища
        station.receiveFuel(fuelDelivered); // Передаем топливо станции
        System.out.println("Транспорт доставил " + fuelDelivered + " единиц топлива на станцию.");
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) { // Цикл выполняется, пока поток не прерван
            for (PowerStation station : stations) {
                if (station.getFuelLevel() <= 10) { // Если на станции мало топлива
                    System.out.println("Запрос топлива на станции с уровнем " + station.getFuelLevel());
                    deliverFuel(station, 50); // Доставляем 50 единиц топлива
                }
            }

            try {
                Thread.sleep(5000); // Задержка между проверками (5 секунд)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Если поток прерывается, выходим из цикла
            }
        }
    }
}

// Класс электростанции
class PowerStation implements Runnable {
    private int fuelLevel; // Уровень топлива на станции
    private final int fuelConsumptionRate; // Скорость потребления топлива

    public PowerStation(int initialFuel, int consumptionRate) {
        this.fuelLevel = initialFuel; // Устанавливаем начальный уровень топлива
        this.fuelConsumptionRate = consumptionRate; // Устанавливаем скорость расхода
    }

    // Метод для получения топлива от транспорта
    public synchronized void receiveFuel(int amount) {
        fuelLevel += amount;
        System.out.println(Thread.currentThread().getName() + " получил " + amount + " топлива.");
    }

    // Метод для получения текущего уровня топлива
    public synchronized int getFuelLevel() {
        return fuelLevel;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) { // Работаем, пока поток не прерван
            synchronized (this) {
                if (fuelLevel > 0) { // Если есть топливо, продолжаем работу
                    fuelLevel -= fuelConsumptionRate; // Уменьшаем топливо
                    System.out.println(Thread.currentThread().getName() + " работает. Остаток топлива: " + fuelLevel);
                } else { // Если топлива нет, станция останавливается
                    System.out.println(Thread.currentThread().getName() + " остановлена: нет топлива.");
                }
            }

            try {
                Thread.sleep(1000); // Ждем 1 секунду перед следующим циклом
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Выходим из цикла при прерывании
            }
        }
    }
}

// Главный класс программы
public class Main {
    public static void main(String[] args) {
        FuelStorage storage = new FuelStorage(200); // Создаем хранилище с 200 единицами топлива

        // Создаем три электростанции с разными начальными уровнями топлива
        PowerStation station1 = new PowerStation(50, 5);
        PowerStation station2 = new PowerStation(80, 5);
        PowerStation station3 = new PowerStation(60, 5);

        PowerStation[] stations = {station1, station2, station3}; // Массив станций
        Transport transport = new Transport(storage, stations); // Создаем транспорт

        // Создаем пул потоков (4 потока: 3 станции и 1 транспорт)
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(station1); // Запускаем поток для первой станции
        executor.execute(station2); // Запускаем поток для второй станции
        executor.execute(station3); // Запускаем поток для третьей станции
        executor.execute(transport); // Запускаем поток для транспорта

        try {
            Thread.sleep(30000); // Работаем 30 секунд
        } catch (InterruptedException e) {
            e.printStackTrace(); // Выводим ошибку в случае прерывания
        }

        executor.shutdownNow(); // Завершаем все потоки после 30 секунд работы
        System.out.println("Остановка всех потоков.");
    }
}
