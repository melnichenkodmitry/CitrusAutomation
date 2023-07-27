package autotests.tests;

import autotests.clients.DuckClient;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.Random;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckTest extends DuckClient {

    /**
     * Тесты create
     */

    @Test(description = "Дымовой тест создания уточки №1")
    @CitrusTest
    public void successCreate1(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        Duck duck = new Duck().color("green").height(10.0).material("wood").sound("muamua").wingsState("ACTIVE");
        duckCreate(runner, duck);
        validatePayloadResponseAndExtractId(runner, duck.id());
    }

    @Test(description = "Дымовой тест создания уточки №2")
    @CitrusTest
    public void successCreate2(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, "duckClient/createEndpoint1.json");
        validateStringResponseAndExtractId(runner, "{\n" +
                "    \"id\": \"@ignore@\",\n" +
                "    \"color\": \"\",\n" +
                "    \"height\": 0.0,\n" +
                "    \"material\": \"\",\n" +
                "    \"sound\": \"\",\n" +
                "    \"wingsState\": \"UNDEFINED\"\n" +
                "}");
    }

    @Test(description = "Дымовой тест создания уточки №3 с валидацией в БД")
    @CitrusTest
    public void successCreate3(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFromDbFinally(runner, "${id}");
        Duck duck = new Duck().color("green").height(10.0).material("wood").sound("muamua").wingsState("ACTIVE");
        duckCreate(runner, duck);
        extractId(runner);
        validateDuckInDb(runner, "${id}", duck.color(), String.valueOf(duck.height()), duck.material(), duck.sound(), duck.wingsState());
    }

    @Test(description = "Дымовой тест создания уточки №4 с валидацией в БД")
    @CitrusTest
    public void successCreate4(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFromDbFinally(runner, "${id}");
        duckCreate(runner, "duckClient/createEndpoint1.json");
        extractId(runner);
        validateDuckInDb(runner, "${id}", "", "0.0", "", "", "UNDEFINED");
    }

    /**
     * Тесты update
     */

    @Test(description = "Дымовой тест обновления уточки")
    @CitrusTest
    public void successUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(1, Integer.MAX_VALUE));
        duckDeleteFromDb(runner, "${id}");
        duckDeleteFromDbFinally(runner, "${id}");
        Duck modDuck = new Duck().color("green").height(11.0).material("plastic").sound("quack").wingsState("FIXED");
        duckCreateInDb(runner, "${id}", "red", 10.0, "plastic", "quack", "ACTIVE");
        duckUpdate(runner, modDuck.color(), String.valueOf(modDuck.height()), "${id}", modDuck.material(), modDuck.sound(), modDuck.wingsState());
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = ${id} is updated"));
        validateDuckInDb(runner, "${id}", modDuck.color(), String.valueOf(modDuck.height()), modDuck.material(), modDuck.sound(), modDuck.wingsState());
    }

    /**
     * Тесты get
     */

    @Test(description = "Дымовой тест получения идентификаторов №1, БД пустая")
    @CitrusTest
    public void successGetAllIds1(@Optional @CitrusResource TestCaseRunner runner) {
        dbCleanup(runner); //очистка БД перед тестом
        duckGetAllIds(runner);
        validateJsonResponse(runner, HttpStatus.OK, "duckClient/getAllIdsEndpoint.json");
    }

    @Test(description = "Дымовой тест получения идентификаторов №2, в БД содержится 1 уточка")
    @CitrusTest
    public void successGetAllIds2(@Optional @CitrusResource TestCaseRunner runner) {
        dbCleanup(runner); //очистка БД перед тестом
        runner.variable("id", new Random().nextInt(1, Integer.MAX_VALUE));
        duckDeleteFromDbFinally(runner, "${id}");
        duckCreateInDb(runner, "${id}", "green", 10.0, "wood", "muamua", "ACTIVE");
        duckGetAllIds(runner);
        validatePayloadResponse(runner, HttpStatus.OK, "[${id}]");
    }

    /**
     * Тесты delete
     */

    @Test(description = "Дымовой тест удаления существующей уточки №1")
    @CitrusTest
    public void successDelete1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(1, Integer.MAX_VALUE));
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "green", 10.0, "wood", "muamua", "ACTIVE");
        duckDelete(runner, "${id}");
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck is deleted"));
    }

    @Test(description = "Дымовой тест удаления несуществующей уточки №2")
    @CitrusTest
    public void successDelete2(@Optional @CitrusResource TestCaseRunner runner) {
        dbCleanup(runner);
        duckDelete(runner, "1");
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = 1 is not found"));
    }

    /**
     * Параметризированный тест
     */

    @Test(dataProvider = "duckList")
    @CitrusTest
    @CitrusParameters({"runner", "payload", "response"})
    public void createDuckList(@Optional @CitrusResource TestCaseRunner runner, Object payload, String response) {
        duckCreate(runner, payload);
        validateJsonResponse(runner, HttpStatus.OK, response);
    }

    @DataProvider(name = "duckList")
    public Object[][] duckList() {
        return new Object[][]{
                {null, new Duck().color("red").height(10.0).material("wood").sound("quack").wingsState("ACTIVE"), "paramTests/redWoodDuck.json"},
                {null, new Duck().color("green").height(15.0).material("plastic").sound("crya").wingsState("FIXED"), "paramTests/greenPlasticDuck.json"},
                {null, new Duck().color("blue").height(20.0).material("rubber").sound("gav").wingsState("UNDEFINED"), "paramTests/blueRubberDuck.json"},
                {null, new Duck().color("yellow").height(25.0).material("metal").sound("meow").wingsState("ACTIVE"), "paramTests/yellowMetalDuck.json"},
                {null, new Duck().color("violet").height(30.0).material("glass").sound("fuf").wingsState("FIXED"), "paramTests/violetGlassDuck.json"},
        };
    }
}
