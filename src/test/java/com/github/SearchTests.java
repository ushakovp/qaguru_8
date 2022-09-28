package com.github;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.github.Lang.*;

public class SearchTests {


    private static void githubSearch(String testData) {
        $("[data-test-selector=nav-search-input]").click();
        $("[data-test-selector=nav-search-input]").setValue(testData);
        $("[data-test-selector=nav-search-input]").submit();
    }

    @ValueSource(strings = {"selenide", "selenoid", "playwright"})
    @ParameterizedTest(name = "Есть репозитарий {0}")
    void searchRepositoryTest(String testData) {
        open("https://github.com/");
        githubSearch(testData);
        $(byText(testData)).should(exist);
    }

    @CsvSource(value = {
            "selenide, Concise UI Tests with Java!",
            "selenoid, Selenium Hub successor running browsers within containers.",
            "playwright, Playwright is a framework for Web Testing and Automation."
    })
    @ParameterizedTest(name = "На странице поиска, для  \"{0}\" есть описание -  \"{0}\"")
    void shouldHaveDescriptionOnSearchPageTest(String testData, String expectedResult) {
        open("https://github.com/");
        githubSearch(testData);
        $(byText(testData)).should(exist);
        $$("li.repo-list-item").first().shouldHave(text(expectedResult));
    }

    static Stream<Arguments> dataProviderForSelenideSiteMenuTest() {
        return Stream.of(
                Arguments.of(ru, List.of("LIVEJOURNAL", "ГЛАВНАЯ", "ТОП", "420 ИДЕЙ", "ВОЙТИ", "СОЗДАТЬ АККАУНТ", "РУССКИЙ (RU)")),
                Arguments.of(fr, List.of("LIVEJOURNAL", "MAIN", "TOP", "375 IDEAS", "CONNEXION", "REJOINDRE", "FRANÇAIS (FR)")),
                Arguments.of(de, List.of("LIVEJOURNAL", "MAIN", "TOP", "375 IDEAS", "ANMELDEN", "JOIN FREE", "DEUTSCH (DE)"))
        );
    }

    @MethodSource("dataProviderForSelenideSiteMenuTest")
    @ParameterizedTest(name = "Для локали {0} отображаются кнопки {1}")
    void ljMainMenuLocalizationTest(Lang lang, List<String> expectedButtons) {
        open("https://www.livejournal.com/");
        $(".s-header-item__link--lang").click();
        $("[data-lang=" + lang + "]").click();
        $$(".s-header a")
                .filter(visible)
                .shouldHave(texts(expectedButtons));
    }
}
