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
        duckCreate(runner, "DuckClient/createEndpoint1.json");
        validateStringResponseAndExtractId(runner, "{\n" +
                "    \"id\": \"@ignore@\",\n" +
                "    \"color\": \"\",\n" +
                "    \"height\": 0.0,\n" +
                "    \"material\": \"\",\n" +
                "    \"sound\": \"\",\n" +
                "    \"wingsState\": \"UNDEFINED\"\n" +
                "}");
    }

    /**
     * Тесты update
     */

    @Test(description = "Дымовой тест обновления уточки")
    @CitrusTest
    public void successUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, "red", "15.0", "rubber", "crya-crya", "ACTIVE");
        extractId(runner);
        duckUpdate(runner, "green", "11.0", "${id}", "plastic", "quack", "FIXED");
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = ${id} is updated"));
    }

    /**
     * Тесты get
     */

    @Test(description = "Дымовой тест получения идентификаторов №1")
    @CitrusTest
    public void successGetAllIds1(@Optional @CitrusResource TestCaseRunner runner) {
        duckGetAllIds(runner);
        validateJsonResponse(runner, HttpStatus.OK, "DuckClient/getAllIdsEndpoint.json");
    }

    @Test(description = "Дымовой тест получения идентификаторов №2")
    @CitrusTest
    public void successGetAllIds2(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("green").height(10.0).material("wood").sound("muamua").wingsState("ACTIVE"));
        extractId(runner);
        duckGetAllIds(runner);
        validatePayloadResponse(runner, HttpStatus.OK, "[${id}]");
    }

    /**
     * Тесты delete
     */

    @Test(description = "Дымовой тест удаления уточки №1")
    @CitrusTest
    public void successDelete1(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("green").height(10.0).material("wood").sound("muamua").wingsState("ACTIVE"));
        extractId(runner);
        duckDelete(runner, "${id}");
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck is deleted"));
    }

    @Test(description = "Дымовой тест удаления уточки №1")
    @CitrusTest
    public void successDelete2(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, new Duck().color("green").height(10.0).material("wood").sound("muamua").wingsState("ACTIVE"));
        extractId(runner);
        duckDelete(runner, "1000");
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = 1000 is deleted"));
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
                {null, new Duck().color("red").height(10.0).material("wood").sound("quack").wingsState("ACTIVE"), "ParamTests/redWoodDuck.json"},
                {null, new Duck().color("green").height(15.0).material("plastic").sound("crya").wingsState("FIXED"), "ParamTests/greenPlasticDuck.json"},
                {null, new Duck().color("blue").height(20.0).material("rubber").sound("gav").wingsState("UNDEFINED"), "ParamTests/blueRubberDuck.json"},
                {null, new Duck().color("yellow").height(25.0).material("metal").sound("meow").wingsState("ACTIVE"), "ParamTests/yellowMetalDuck.json"},
                {null, new Duck().color("violet").height(30.0).material("glass").sound("fuf").wingsState("FIXED"), "ParamTests/violetGlassDuck.json"},
        };
    }
}
