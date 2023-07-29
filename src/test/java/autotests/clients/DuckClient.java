package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.validation.json.JsonPathMessageValidationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.qameta.allure.Description;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckClient extends BaseTest {

    /**
     * Методы create
     */

    @Step("Создание уточки через строки")
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

    @Step("Создание уточки через JSON")
    @Description("CreateEndpointJSON")
    public void duckCreate(TestCaseRunner runner, String payload) {
        sendPostJsonRequest(runner, duckService, "/api/duck/create", payload);
    }

    @Step("Создание уточки через Payload")
    @Description("CreateEndpointPayload")
    public void duckCreate(TestCaseRunner runner, Object payload) {
        sendPostPayloadRequest(runner, duckService, "/api/duck/create", payload);
    }

    /**
     * Методы delete
     */

    @Step("Удаление уточки в конце теста")
    @Description("DeleteFinallyEndpoint")
    public void duckDeleteFinally(TestCaseRunner runner, String id) {
        duckDeleteFinally(runner, duckService, "/api/duck/delete", "id", id);
    }

    @Step("Удаление уточки")
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

    @Step("Получение всех идентификаторов созданных уточек")
    @Description("GetAllIdsEndpoint")
    public void duckGetAllIds(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/getAllIds"));
    }

    /**
     * Метод update
     */

    @Step("Обновление уточки")
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

    @Step("Обновление уточки")
    @Description("UpdateEndpoint")
    public void duckUpdateWithoutSound(TestCaseRunner runner, String color, String height, String id, String material, String wingsState) {
        runner.$(http().client(duckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", height)
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("wingsState", wingsState));
    }

    /**
     * Методы валидации
     */

    @Step("Валидация ответа через строку")
    @Description("ValidateStringResponse")
    public void validateStringResponse(TestCaseRunner runner, HttpStatus status, String response) {
        validateStringResponse(runner, duckService, status, response);
    }

    @Step("Валидация ответа через строку и извлечение идентификатора")
    @Description("ValidateStringResponseAndExtractId")
    public void validateStringResponseAndExtractId(TestCaseRunner runner, String response) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.id", "id"))
                .body(response));
    }

    @Step("Валидация ответа через JSON")
    @Description("ValidateJsonResponse")
    public void validateJsonResponse(TestCaseRunner runner, HttpStatus status, String expectedPayload) {
        validateJsonResponse(runner, duckService, status, expectedPayload);
    }

    @Step("Валидация ответа через Payload")
    @Description("ValidatePayloadResponse")
    public void validatePayloadResponse(TestCaseRunner runner, HttpStatus status, Object expectedPayload) {
        validatePayloadResponse(runner, duckService, status, expectedPayload);
    }

    @Step("Валидация ответа через Payload и извлечение идентификатора")
    @Description("ValidatePayloadResponseAndExtractId")
    public void validatePayloadResponseAndExtractId(TestCaseRunner runner, Object expectedPayload) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.id", "id"))
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    @Step("Валидация ответа через JsonPath")
    @Description("ValidateJsonPathResponse")
    public void validateJsonPathResponse(TestCaseRunner runner, HttpStatus status, JsonPathMessageValidationContext.Builder body) {
        validateJsonPathResponse(runner, duckService, status, body);
    }

    /**
     * Методы извлечения данных из ответа
     */

    @Step("Извлечение идентификатора")
    @Description("ExtractId")
    public void extractId(TestCaseRunner runner) {
        extractId(runner, duckService, HttpStatus.OK);
    }

    @Step("Извлечение всех полей уточки")
    @Description("ExtractDuck")
    public void extractDuck(TestCaseRunner runner) {
        extractDuck(runner, duckService, HttpStatus.OK);
    }

    /**
     * Методы манипуляций с БД
     */

    @Step("Создание уточки в БД")
    @Description("duckCreateInDb")
    public void duckCreateInDb(TestCaseRunner runner, String id, String color, Double height, String material, String sound, String wingsState) {
        databaseUpdate(runner, db, "INSERT INTO DUCK VALUES (" + id +  ", '" + color + "', " + height + ", '" + material + "', '" + sound + "', '" + wingsState + "')");
    }

    @Step("Удаление уточки в БД в конце теста")
    @Description("duckDeleteFromDbFinally")
    public void duckDeleteFromDbFinally(TestCaseRunner runner, String id) {
        databaseUpdateFinally(runner, db, "DELETE FROM DUCK WHERE ID = " + id);
    }

    @Step("Удаление уточки в БД")
    @Description("duckDeleteFromDb")
    public void duckDeleteFromDb(TestCaseRunner runner, String id) {
        databaseUpdate(runner, db, "DELETE FROM DUCK WHERE ID = " + id);
    }

    @Step("Валидация уточки в БД")
    @Description("ValidateDuckInDb")
    public void validateDuckInDb(TestCaseRunner runner, String id, String color, String height, String material, String sound, String wingsState) {
        validateDuckInDb(runner, db, id, color, height, material, sound, wingsState);
    }

    @Step("Очистка БД")
    @Description("DbCleanup")
    public void dbCleanup(TestCaseRunner runner) {
        databaseUpdate(runner, db, "DELETE FROM DUCK");
    }
}
