package com.github;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class SearchTests {

    private static final String REPOSITORY = "selenide";


    @ValueSource(strings = {"selenide", "selenoid", "playwright"})
    @ParameterizedTest(name = "Есть репозитарий {0}")
    void repositoryHasIssueListenerTest(String testData) {
        open("https://github.com/");
        $("[data-test-selector=nav-search-input]").click();
        $("[data-test-selector=nav-search-input]").setValue(testData);
        $("[data-test-selector=nav-search-input]").submit();
        $(byText(testData)).should(exist);
    }

    @CsvSource(value = {
            "selenide, Concise UI Tests with Java!",
            "selenoid, Selenium Hub successor running browsers within containers.",
            "playwright, Playwright is a framework for Web Testing and Automation."
    })
    @ParameterizedTest(name = "На странице поиска, для  \"{0}\" есть описание -  \"{0}\"")
    void tst(String testData, String expectedResult) {
        open("https://github.com/");
        $("[data-test-selector=nav-search-input]").click();
        $("[data-test-selector=nav-search-input]").setValue(testData);
        $("[data-test-selector=nav-search-input]").submit();
        $(byText(testData)).should(exist);
        $$("li.repo-list-item").first().shouldHave(text(expectedResult));
    }

}
