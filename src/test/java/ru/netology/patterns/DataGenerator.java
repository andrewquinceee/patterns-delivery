package ru.netology.patterns;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));

    public static String generateCity() {
        return "Москва"; 
    }

    public static String generateName() {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generatePhone() {
        return "+7" + faker.number().digits(10);
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
