package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.validation.json.JsonPathMessageValidationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckClient extends BaseTest {

    /**
     * Методы create
     */

    @Description("CreateEndpointString")
    public void duckCreate(TestCaseRunner runner, String color, String height, String material, String sound, String wingsState) {
        sendPostStringRequest(runner, duckService, "/api/duck/create", "{\n" +
                "  \"color\": \"" + color + "\",\n" +
                "  \"height\": " + height + ",\n" +
                "  \"material\": \"" + material + "\",\n" +
                "  \"sound\": \"" + sound + "\",\n" +
                "  \"wingsState\": \"" + wingsState + "\"\n" +
                "}");
    }

    @Description("CreateEndpointJSON")
    public void duckCreate(TestCaseRunner runner, String payload) {
        sendPostJsonRequest(runner, duckService, "/api/duck/create", payload);
    }

    @Description("CreateEndpointPayload")
    public void duckCreate(TestCaseRunner runner, Object payload) {
        sendPostPayloadRequest(runner, duckService, "/api/duck/create", payload);
    }

    /**
     * Методы delete
     */

    @Description("DeleteFinallyEndpoint")
    public void duckDeleteFinally(TestCaseRunner runner, String id) {
        duckDeleteFinally(runner, duckService, "/api/duck/delete", "id", id);
    }

    @Description("DeleteEndpoint")
    public void duckDelete(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }

    /**
     * Метод get
     */

    @Description("GetAllIdsEndpoint")
    public void duckGetAllIds(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/getAllIds"));
    }

    /**
     * Метод update
     */

    @Description("UpdateEndpoint")
    public void duckUpdate(TestCaseRunner runner, String color, String height, String id, String material, String sound, String wingsState) {
        runner.$(http().client(duckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", height)
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", wingsState));
    }

    /**
     * Методы валидации
     */

    @Description("ValidateStringResponse")
    public void validateStringResponse(TestCaseRunner runner, HttpStatus status, String response) {
        validateStringResponse(runner, duckService, status, response);
    }

    @Description("ValidateStringResponse")
    public void validateStringResponseAndExtractId(TestCaseRunner runner, String response) {
        runner.$(http().client(duckService)
                .receive() //получение ответа
                .response(HttpStatus.OK) //проверка статуса ответа
                .message()
                .type(MessageType.JSON) //проверка заголовка ответа
                .extract(fromBody().expression("$.id", "id"))
                .body(response));
    }

    @Description("ValidateJsonResponse")
    public void validateJsonResponse(TestCaseRunner runner, HttpStatus status, String expectedPayload) {
        validateJsonResponse(runner, duckService, status, expectedPayload);
    }

    @Description("ValidatePayloadResponse")
    public void validatePayloadResponse(TestCaseRunner runner, HttpStatus status, Object expectedPayload) {
        validatePayloadResponse(runner, duckService, status, expectedPayload);
    }

    @Description("ValidatePayloadResponse")
    public void validatePayloadResponseAndExtractId(TestCaseRunner runner, Object expectedPayload) {
        runner.$(http().client(duckService)
                .receive() //получение ответа
                .response(HttpStatus.OK) //проверка статуса ответа
                .message()
                .type(MessageType.JSON) //проверка заголовка ответа
                .extract(fromBody().expression("$.id", "id")) //извлечение идентификатора
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))); //проверка тела ответа
    }

    @Description("ValidateJsonPathResponse")
    public void validateJsonPathResponse(TestCaseRunner runner, HttpStatus status, JsonPathMessageValidationContext.Builder body) {
        validateJsonPathResponse(runner, duckService, status, body);
    }

    /**
     * Методы извлечения данных из ответа
     */

    @Description("ExtractId")
    public void extractId(TestCaseRunner runner) {
        extractId(runner, duckService, HttpStatus.OK);
    }

    @Description("ExtractDuck") //Метод не используется. Оставил, вдруг пригодится
    public void extractDuck(TestCaseRunner runner) {
        extractDuck(runner, duckService, HttpStatus.OK);
    }

    /**
     * Методы манипуляций с БД
     */

    @Description("duckCreateInDb")
    public void duckCreateInDb(TestCaseRunner runner, String id, String color, Double height, String material, String sound, String wingsState) {
        databaseUpdate(runner, testDB, "INSERT INTO DUCK VALUES (" + id +  ", '" + color + "', " + height + ", '" + material + "', '" + sound + "', '" + wingsState + "')");
    }

    @Description("duckDeleteFromDbFinally")
    public void duckDeleteFromDbFinally(TestCaseRunner runner, String id) {
        databaseUpdateFinally(runner, testDB, "DELETE FROM DUCK WHERE ID = " + id);
    }

    @Description("duckDeleteFromDbFinally")
    public void duckDeleteFromDb(TestCaseRunner runner, String id) {
        databaseUpdate(runner, testDB, "DELETE FROM DUCK WHERE ID = " + id);
    }

    @Description("ValidateDuckInDb")
    public void validateDuckInDb(TestCaseRunner runner, String id, String color, String height, String material, String sound, String wingsState) {
        validateDuckInDb(runner, testDB, id, color, height, material, sound, wingsState);
    }

    @Description("DbCleanup")
    public void dbCleanup(TestCaseRunner runner) {
        databaseUpdate(runner, testDB, "DELETE FROM DUCK");
    }
}
