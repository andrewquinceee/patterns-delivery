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

public class DeliveryTest {

    @BeforeEach
    void setup() {
        Configuration.baseUrl = "http://localhost:9999";
    }

    @Test
    void shouldPlanDeliveryWithSameUserButDifferentDate() {
        open("/");

        // 1. Генерируем пользователя ОДИН раз
        DeliveryInfo user = DataGenerator.generateDeliveryInfo(3);

        // 2. ПЕРВЫЙ ЗАКАЗ
        fillForm(user);
        $(".button").click();
        
        // Проверяем точный элемент уведомления, его видимость и полный текст с датой
        $(".notification")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно!"))
                .shouldHave(text(user.getDate()));

        // 3. МЕНЯЕМ ДАТУ для того же пользователя
        String newDate = DataGenerator.generateDate(5);
        
        SelenideElement dateField = $("[data-test-id='date'] input");
        dateField.doubleClick();
        dateField.sendKeys(Keys.BACK_SPACE);
        dateField.setValue(newDate);
        dateField.sendKeys(Keys.TAB);

        // 4. ОТПРАВЛЯЕМ ВТОРОЙ РАЗ
        $(".button").click();

        // 5. Проверяем сообщение о подтверждении (точный селектор, а не body)
        $(".notification")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Необходимо подтверждение"));

        // 6. Нажимаем кнопку ПЕРЕЗАПЛАНИРОВАТЬ внутри самого уведомления
        $(".notification .button").click();
    }

    private void fillForm(DeliveryInfo info) {
        $("[data-test-id='city'] input").setValue(info.getCity());
        $("[data-test-id='date'] input").setValue(info.getDate());
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[data-test-id='agreement']").click();
    }
}
