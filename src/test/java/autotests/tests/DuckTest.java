package autotests.tests;

import autotests.clients.DuckClient;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Flaky;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.Random;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

@Epic("������ CRUD ��� ������")
public class DuckTest extends DuckClient {

    /**
     * ����� create
     */

    @Step("�������� ������")
    @Description("���� � ��������� ����������")
    @Test(description = "�������� ������ �1")
    @CitrusTest
    public void successCreate1(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        Duck duck = new Duck().color("green").height(10.0).material("wood").sound("muamua").wingsState("ACTIVE");
        duckCreate(runner, duck);
        validatePayloadResponseAndExtractId(runner, duck.id());
    }

    @Step("�������� ������")
    @Description("�������� ������ ������")
    @Test(description = "�������� ������ �2")
    @CitrusTest
    public void successCreate2(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFinally(runner, "${id}");
        duckCreate(runner, "duckClient/createEndpoint1.json");
        validateStringResponseAndExtractId(runner, "{\n" +
                "    \"id\": \"@ignore@\",\n" +
                "    \"color\": \"\",\n" +
                "    \"height\": 0.0,\n" +
                "    \"material\": \"\",\n" +
                "    \"sound\": \"quack\",\n" +
                "    \"wingsState\": \"ACTIVE\"\n" +
                "}");
    }

    @Step("�������� ������")
    @Description("�������� ������ � ���������� � ��")
    @Test(description = "�������� ������ �3")
    @CitrusTest
    public void successCreate3(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFromDbFinally(runner, "${id}");
        Duck duck = new Duck().color("green").height(10.0).material("wood").wingsState("ACTIVE");
        duckCreate(runner, duck);
        extractId(runner);
        validateDuckInDb(runner, "${id}", duck.color(), String.valueOf(duck.height()), duck.material(), "quack", duck.wingsState());
    }

    @Step("�������� ������")
    @Description("�������� ������ ������ � ���������� � ��")
    @Test(description = "�������� ������ �4")
    @CitrusTest
    public void successCreate4(@Optional @CitrusResource TestCaseRunner runner) {
        duckDeleteFromDbFinally(runner, "${id}");
        duckCreate(runner, "duckClient/createEndpoint1.json");
        extractId(runner);
        validateDuckInDb(runner, "${id}", "", "0.0", "", "quack", "ACTIVE");
    }

    /**
     * ����� update
     */

    @Step("���������� ������")
    @Flaky
    @Description("���������� ������������ ������. ����� ���")
    @Test(description = "���������� ������")
    @CitrusTest
    public void successUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDb(runner, "${id}");
        duckDeleteFromDbFinally(runner, "${id}");
        Duck modDuck = new Duck().color("green").height(15.0).material("plastic").wingsState("FIXED");
        duckCreateInDb(runner, "${id}", "red", 20.0, "plastic", "quack", "ACTIVE");
        duckUpdateWithoutSound(runner, modDuck.color(), String.valueOf(modDuck.height()), "${id}", modDuck.material(), modDuck.wingsState());
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = ${id} is updated"));
        validateDuckInDb(runner, "${id}", modDuck.color(), String.valueOf(modDuck.height()), modDuck.material(), "quack", modDuck.wingsState());
    }

    /**
     * ����� get
     */

    @Step("��������� ���� ���������������")
    @Description("�� ������")
    @Test(description = "��������� ���� ��������������� �1")
    @CitrusTest
    public void successGetAllIds1(@Optional @CitrusResource TestCaseRunner runner) {
        dbCleanup(runner); //������� �� ����� ������
        duckGetAllIds(runner);
        validateJsonResponse(runner, HttpStatus.OK, "duckClient/getAllIdsEndpoint.json");
    }

    @Step("��������� ���� ���������������")
    @Description("� �� ���������� 1 ������")
    @Test(description = "��������� ���� ��������������� �2")
    @CitrusTest
    public void successGetAllIds2(@Optional @CitrusResource TestCaseRunner runner) {
        dbCleanup(runner); //������� �� ����� ������
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckCreateInDb(runner, "${id}", "green", 10.0, "wood", "muamua", "ACTIVE");
        duckGetAllIds(runner);
        validatePayloadResponse(runner, HttpStatus.OK, "[${id}]");
    }

    /**
     * ����� delete
     */

    @Step("�������� ������")
    @Description("�������� ������������ ������")
    @Test(description = "�������� ������ �1")
    @CitrusTest
    public void successDelete1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "green", 10.0, "wood", "muamua", "ACTIVE");
        duckDelete(runner, "${id}");
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = ${id} is deleted"));
    }

    @Step("�������� ������")
    @Description("�������� �������������� ������")
//    @Flaky
    @Test(description = "�������� ������ �2")
    @CitrusTest
    public void successDelete2(@Optional @CitrusResource TestCaseRunner runner) {
        dbCleanup(runner);
        duckDelete(runner, "1");
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = 1 is not found"));
    }

    /**
     * ������������������� ����
     */

    @Step("������������������� ����")
    @Description("������������������� ���� �� �������� 5 ������")
    @Test(dataProvider = "duckList", description = "������������������� ����")
    @CitrusTest
    @CitrusParameters({"runner", "payload", "response"})
    public void createDuckList(@Optional @CitrusResource TestCaseRunner runner, Object payload, String response) {
        duckCreate(runner, payload);
        validateJsonResponse(runner, HttpStatus.OK, response);
    }

    @DataProvider(name = "duckList")
    public Object[][] duckList() {
        return new Object[][]{
                {null, new Duck().color("red").height(10.0).material("wood").wingsState("ACTIVE"), "paramTests/redWoodDuck.json"},
                {null, new Duck().color("green").height(15.0).material("plastic").wingsState("FIXED"), "paramTests/greenPlasticDuck.json"},
                {null, new Duck().color("blue").height(20.0).material("rubber").wingsState("UNDEFINED"), "paramTests/blueRubberDuck.json"},
                {null, new Duck().color("yellow").height(25.0).material("metal").wingsState("ACTIVE"), "paramTests/yellowMetalDuck.json"},
                {null, new Duck().color("violet").height(30.0).material("glass").wingsState("FIXED"), "paramTests/violetGlassDuck.json"},
        };
    }
}
