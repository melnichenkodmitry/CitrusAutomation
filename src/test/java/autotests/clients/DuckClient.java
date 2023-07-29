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
     * ������ create
     */

    @Step("�������� ������ ����� ������")
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

    @Step("�������� ������ ����� JSON")
    @Description("CreateEndpointJSON")
    public void duckCreate(TestCaseRunner runner, String payload) {
        sendPostJsonRequest(runner, duckService, "/api/duck/create", payload);
    }

    @Step("�������� ������ ����� Payload")
    @Description("CreateEndpointPayload")
    public void duckCreate(TestCaseRunner runner, Object payload) {
        sendPostPayloadRequest(runner, duckService, "/api/duck/create", payload);
    }

    /**
     * ������ delete
     */

    @Step("�������� ������ � ����� �����")
    @Description("DeleteFinallyEndpoint")
    public void duckDeleteFinally(TestCaseRunner runner, String id) {
        duckDeleteFinally(runner, duckService, "/api/duck/delete", "id", id);
    }

    @Step("�������� ������")
    @Description("DeleteEndpoint")
    public void duckDelete(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }

    /**
     * ����� get
     */

    @Step("��������� ���� ��������������� ��������� ������")
    @Description("GetAllIdsEndpoint")
    public void duckGetAllIds(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/getAllIds"));
    }

    /**
     * ����� update
     */

    @Step("���������� ������")
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

    @Step("���������� ������")
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
     * ������ ���������
     */

    @Step("��������� ������ ����� ������")
    @Description("ValidateStringResponse")
    public void validateStringResponse(TestCaseRunner runner, HttpStatus status, String response) {
        validateStringResponse(runner, duckService, status, response);
    }

    @Step("��������� ������ ����� ������ � ���������� ��������������")
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

    @Step("��������� ������ ����� JSON")
    @Description("ValidateJsonResponse")
    public void validateJsonResponse(TestCaseRunner runner, HttpStatus status, String expectedPayload) {
        validateJsonResponse(runner, duckService, status, expectedPayload);
    }

    @Step("��������� ������ ����� Payload")
    @Description("ValidatePayloadResponse")
    public void validatePayloadResponse(TestCaseRunner runner, HttpStatus status, Object expectedPayload) {
        validatePayloadResponse(runner, duckService, status, expectedPayload);
    }

    @Step("��������� ������ ����� Payload � ���������� ��������������")
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

    @Step("��������� ������ ����� JsonPath")
    @Description("ValidateJsonPathResponse")
    public void validateJsonPathResponse(TestCaseRunner runner, HttpStatus status, JsonPathMessageValidationContext.Builder body) {
        validateJsonPathResponse(runner, duckService, status, body);
    }

    /**
     * ������ ���������� ������ �� ������
     */

    @Step("���������� ��������������")
    @Description("ExtractId")
    public void extractId(TestCaseRunner runner) {
        extractId(runner, duckService, HttpStatus.OK);
    }

    @Step("���������� ���� ����� ������")
    @Description("ExtractDuck")
    public void extractDuck(TestCaseRunner runner) {
        extractDuck(runner, duckService, HttpStatus.OK);
    }

    /**
     * ������ ����������� � ��
     */

    @Step("�������� ������ � ��")
    @Description("duckCreateInDb")
    public void duckCreateInDb(TestCaseRunner runner, String id, String color, Double height, String material, String sound, String wingsState) {
        databaseUpdate(runner, db, "INSERT INTO DUCK VALUES (" + id +  ", '" + color + "', " + height + ", '" + material + "', '" + sound + "', '" + wingsState + "')");
    }

    @Step("�������� ������ � �� � ����� �����")
    @Description("duckDeleteFromDbFinally")
    public void duckDeleteFromDbFinally(TestCaseRunner runner, String id) {
        databaseUpdateFinally(runner, db, "DELETE FROM DUCK WHERE ID = " + id);
    }

    @Step("�������� ������ � ��")
    @Description("duckDeleteFromDb")
    public void duckDeleteFromDb(TestCaseRunner runner, String id) {
        databaseUpdate(runner, db, "DELETE FROM DUCK WHERE ID = " + id);
    }

    @Step("��������� ������ � ��")
    @Description("ValidateDuckInDb")
    public void validateDuckInDb(TestCaseRunner runner, String id, String color, String height, String material, String sound, String wingsState) {
        validateDuckInDb(runner, db, id, color, height, material, sound, wingsState);
    }

    @Step("������� ��")
    @Description("DbCleanup")
    public void dbCleanup(TestCaseRunner runner) {
        databaseUpdate(runner, db, "DELETE FROM DUCK");
    }
}
