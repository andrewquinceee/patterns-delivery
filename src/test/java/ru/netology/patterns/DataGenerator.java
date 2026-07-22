package ru.netology.patterns;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));
    
    // Массив допустимых городов для генерации (чтобы приложение не выдало ошибку валидации)
    private static final String[] cities = {"Москва", "Санкт-Петербург", "Казань", "Новосибирск", "Екатеринбург"};

    public static String generateCity() {
        // Случайный выбор города из массива
        return cities[faker.number().numberBetween(0, cities.length)];
    }

    public static String generateName() {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generatePhone() {
        return "+79" + faker.number().digits(9);
    }

    public static String generateDate(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static DeliveryInfo generateDeliveryInfo(int daysToAdd) {
        return new DeliveryInfo(
                generateCity(),
                generateName(),
                generatePhone(),
                generateDate(daysToAdd)
        );
    }
}
