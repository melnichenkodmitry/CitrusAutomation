package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.validation.json.JsonPathMessageValidationContext;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckActionsClient extends BaseTest {

    /**
     * �������� ������
     */

    @Step("������ �����")
    @Description("FlyEndpoint")
    public void duckFly(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/fly", "id", id);
    }

    @Step("�������� ������")
    @Description("PropertiesEndpoint")
    public void duckProperties(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/properties", "id", id);
    }

    @Step("������ �������")
    @Description("QuackEndpoint")
    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    @Step("������ ������")
    @Description("SwimEndpoint")
    public void duckSwim(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/swim", "id", id);
    }

    /**
     * ������ ���������
     */

    @Step("��������� ������ ����� ������")
    @Description("ValidateStringResponse")
    public void validateStringResponse(TestCaseRunner runner, HttpStatus status, String response) {
        validateStringResponse(runner, duckService, status, response);
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

    @Step("��������� ������ ����� JsonPath")
    @Description("ValidateJsonPathResponse")
    public void validateJsonPathResponse(TestCaseRunner runner, HttpStatus status, JsonPathMessageValidationContext.Builder body) {
        validateJsonPathResponse(runner, duckService, status, body);
    }

    /**
     * ������ create ��� �������� ������ � ��
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
     * ����� delete
     */

    @Step("�������� ������ � ����� �����")
    @Description("DeleteFinallyEndpoint")
    public void duckDeleteFinally(TestCaseRunner runner, String id) {
        duckDeleteFinally(runner, duckService, "/api/duck/delete", "id", id);
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
}
