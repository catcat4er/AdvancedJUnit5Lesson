import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;

@Owner("catcat4er")
@DisplayName("Пошаримся по Икее")
public class TestWithVariableParameters {

    @BeforeEach
    void beforeEach() {
        Configuration.browserSize = "1920x1080";
        open("https://www.ikea.com/ru/ru/");
    }
    @AfterAll
    static void afterAll() {
        closeWebDriver();
    }


    //Обычная аннотация к тесту, без набора разных параметров. Отключена.
    @Test
    @DisplayName("Обычный тест")
    void searchTest () {
        $(".search-field__input").setValue("мебель для гостиной").pressEnter();
        $(".search-summary__content").shouldHave(Condition.text("Результаты поиска: мебель для гостиной"));
    }


    //Параметризованная аннотация с использованием Value
    @DisplayName("Тест с использованием Value")
    @ValueSource(strings = {"мебель для гостиной", "лампы настольные"})
    @ParameterizedTest(name = "Поищем {0}")
    void searchTestWithValueSource (String inputData){
        $(".search-field__input").setValue(inputData).pressEnter();
        $(".search-summary__content").shouldHave(text("Результаты поиска: " + inputData));
    }


    //Параметризованная аннотация с использованием CSV
    @DisplayName("Тест с использованием CSV")
    @CsvSource({
            "мебель для гостиной,Стеллажи и книжные шкафы,90383339",
            "лампы настольные,Детские товары,80325694"
    })
    @ParameterizedTest(name = "Поищем {0}")
    void searchTestWithCSV (String inputData, String elseInputData, String productNumber) {
        $(".search-field__input").setValue(inputData).pressEnter();
        $(".filter-button__container button[id='CATEGORIES']").click();
        $$(".category-filter__list span").findBy(text(elseInputData)).scrollTo().click();
        String prodNum = format("[data-product-number='%s']", productNumber);
        $(prodNum).shouldBe(visible);
    }


    //Параметризованная аннотация с использованием Stream
    static Stream<Arguments> streamData() {
        return Stream.of(
                Arguments.of("мебель для гостиной", "Стеллажи и книжные шкафы", "90383339"),
                Arguments.of("лампы настольные", "Детские товары", "80325694"));
    }

    @DisplayName("Тест с использованием Stream")
    @MethodSource("streamData")
    @ParameterizedTest(name = "Поищем {0}")
    void searchTestWithStream (String inputData, String elseInputData, String productNumber) {
        $(".search-field__input").setValue(inputData).pressEnter();
        $(".filter-button__container button[id='CATEGORIES']").click();
        $$(".category-filter__list span").findBy(text(elseInputData)).scrollTo().click();
        String prodNum = format("[data-product-number='%s']", productNumber);
        $(prodNum).shouldBe(visible);
    }

}
