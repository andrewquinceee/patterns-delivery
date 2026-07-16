package ru.netology.patterns;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Selectors.byTagName;

public class DeliveryTest {

    @BeforeEach
    void setup() {
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.timeout = 20000; // Увеличиваем таймаут
    }

    @Test
    void shouldPlanDeliveryWithSameUserButDifferentDate() {
        open("/");

        DeliveryInfo user = DataGenerator.generateDeliveryInfo(3);

        // ПЕРВЫЙ ЗАКАЗ
        fillForm(user);
        $(".button").click();
        
        // Ждем появления ЛЮБОГО элемента с текстом "Успешно" на странице
        $(byTagName("body"))
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно"));

        // Закрываем уведомление
        $(byTagName("body")).sendKeys(Keys.ESCAPE);
        
        // Ждем пока уведомление исчезнет
        $(".notification").shouldBe(hidden, Duration.ofSeconds(5));

        // МЕНЯЕМ ДАТУ
        String newDate = DataGenerator.generateDate(5);
        
        SelenideElement dateField = $("[data-test-id='date'] input");
        dateField.doubleClick();
        dateField.sendKeys(Keys.BACK_SPACE);
        dateField.setValue(newDate);
        dateField.sendKeys(Keys.TAB);

        // ОТПРАВЛЯЕМ ВТОРОЙ РАЗ
        $(".button").click();

        // Ждем появления текста "подтверждение" на странице
        $(byTagName("body"))
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("подтверждение"));
    }

    private void fillForm(DeliveryInfo info) {
        $("[data-test-id='city'] input").setValue(info.getCity());
        $("[data-test-id='date'] input").setValue(info.getDate());
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[data-test-id='agreement']").click();
    }
}
