package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
import autotests.payloads.Sound;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;


public class DuckActionsTest extends DuckActionsClient {

    /**
     * Тесты fly
     */

    @Test(description = "Летающая уточка №1, проверка wingsState = ACTIVE")
    @CitrusTest
    public void successFly(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("yellow").height(0.01).material("rubber").sound("quack").wingsState("ACTIVE"));
        extractId(runner); //ID из ответа
        duckFly(runner, "${id}"); //Запрос
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "I'm flying"));
    }

    @Test(description = "Летающая уточка №2, проверка wingsState = FIXED")
    @CitrusTest
    public void unsuccessFly(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("yellow").height(0.01).material("rubber").sound("quack").wingsState("FIXED"));
        extractId(runner);
        duckFly(runner, "${id}");
        validatePayloadResponse(runner, HttpStatus.OK, new Message().message("I can't fly"));
    }

    /**
     * Тесты properties
     */

    @Test(description = "Получение полей уточки №1")
    @CitrusTest
    public void successProperties1(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, "yellow", "0.01", "rubber", "quack", "ACTIVE");
        extractDuck(runner);
        duckProperties(runner, "${id}");
        validateStringResponse(runner, HttpStatus.OK, "{\n" +
                "  \"color\": \"${color}\",\n" +
                "  \"height\": ${height},\n" +
                "  \"material\": \"${material}\",\n" +
                "  \"sound\": \"${sound}\",\n" +
                "  \"wingsState\": \"${wingsState}\"\n" +
                "}");
    }

    @Test(description = "Получение полей уточки №2")
    @CitrusTest
    public void successProperties2(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, "DuckClient/createEndpoint3.json");
        extractId(runner);
        duckProperties(runner, "${id}");
        validateJsonResponse(runner, HttpStatus.OK, "DuckClient/createEndpoint4.json");
    }

    @Test(description = "Получение полей уточки №3")
    @CitrusTest
    public void successProperties3(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.01).material("rubber").sound("quack").wingsState("UNDEFINED");
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, duck);
        extractId(runner);
        duckProperties(runner, "${id}");
        validatePayloadResponse(runner, HttpStatus.OK, duck);
    }

    @Test(description = "Получение полей уточки №4")
    @CitrusTest
    public void successProperties4(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.01).material("metal").sound("quack").wingsState("ACTIVE");
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, duck);
        extractDuck(runner);
        duckProperties(runner, "${id}");
        validatePayloadResponse(runner, HttpStatus.OK, duck);
    }

    /**
     * Тесты quack
     */

    @Test(description = "Крякающая уточка №1")
    @CitrusTest
    public void successQuack1(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("red").height(10.0).material("rubber").sound("quack").wingsState("UNDEFINED"));
        extractId(runner);
        duckQuack(runner, "${id}", "0", "2");
        validateStringResponse(runner, HttpStatus.OK, "{\n" +
                "  \"sound\": \"\",\n" +
                "}");
    }

    @Test(description = "Крякающая уточка №2")
    @CitrusTest
    public void successQuack2(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("red").height(10.0).material("plastic").sound("quack").wingsState("ACTIVE"));
        extractId(runner);
        duckQuack(runner, "${id}", "3", "2");
        validatePayloadResponse(runner, HttpStatus.OK, new Sound().sound("quack-quack, quack-quack, quack-quack"));
    }

    @Test(description = "Крякающая уточка №3")
    @CitrusTest
    public void successQuack3(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("red").height(10.0).material("plastic").sound("quack").wingsState("ACTIVE"));
        extractId(runner);
        duckQuack(runner, "${id}", "2", "3");
        validatePayloadResponse(runner, HttpStatus.OK, new Sound().sound("quack-quack-quack, quack-quack-quack"));
    }

    @Test(description = "Крякающая уточка №4")
    @CitrusTest
    public void successQuack4(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("red").height(10.0).material("plastic").sound("quack").wingsState("ACTIVE"));
        extractId(runner);
        duckQuack(runner, "${id}", "3", "3");
        validatePayloadResponse(runner, HttpStatus.OK, new Sound().sound("quack-quack-quack, quack-quack-quack, quack-quack-quack"));
    }

    @Test(description = "Крякающая уточка №5")
    @CitrusTest
    public void successQuack5(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("red").height(10.0).material("plastic").sound("quack").wingsState("ACTIVE"));
        extractId(runner);
        duckQuack(runner, "${id}", "3", "0");
        validatePayloadResponse(runner, HttpStatus.OK, new Sound().sound(""));
    }

    /**
     * Тесты swim
     */

    @Test(description = "Плавающая уточка 1")
    @CitrusTest(name = "Плавающая уточка 1")
    @Description("Плавающая уточка 1")
    public void successSwim1(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("green").height(15.0).material("rubber").sound("gagaga").wingsState("FIXED")); //Создание уточки
        extractId(runner); //ID из ответа
        duckSwim(runner, "${id}"); //Запрос
        validatePayloadResponse(runner, HttpStatus.OK, new Message().message("I’m swimming")); //Валидация
    }
}
