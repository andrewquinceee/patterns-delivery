package ru.netology.patterns;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
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
        dateField.clear();               // Надежная очистка поля
        dateField.setValue(newDate);     // Ввод новой даты
        dateField.sendKeys(Keys.TAB);    // Снимаем фокус для триггера валидации

        // 4. ОТПРАВЛЯЕМ ВТОРОЙ РАЗ
        $(".button").click();

        // 5. Находим ИМЕННО ТО уведомление, которое содержит нужный текст (игнорируя старое скрытое)
        // И проверяем его видимость, как просил проверяющий
        SelenideElement confirmationMessage = $$(".notification")
                .findBy(text("Необходимо подтверждение"))
                .shouldBe(visible, Duration.ofSeconds(15));

        // 6. Нажимаем кнопку ПЕРЕЗАПЛАНИРОВАТЬ внутри этого конкретного сообщения
        confirmationMessage.$(".button").click();
    }

    private void fillForm(DeliveryInfo info) {
        $("[data-test-id='city'] input").setValue(info.getCity());
        $("[data-test-id='date'] input").setValue(info.getDate());
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[data-test-id='agreement']").click();
    }
}
