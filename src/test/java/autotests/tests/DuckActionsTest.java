package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
import autotests.payloads.Sound;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Flaky;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.Random;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;


@Epic("�������� ������")
public class DuckActionsTest extends DuckActionsClient {

    /**
     * ����� fly
     */

    @Step("�������� ������")
    @Description("wingsState = ACTIVE")
    @Flaky
    @Test(description = "�������� ������ �1")
    @CitrusTest
    public void successFly1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "yellow", 0.01, "rubber", "quack", "ACTIVE");
        duckFly(runner, "${id}");
        validateJsonPathResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "I'm flying"));
    }

    @Step("�������� ������")
    @Description("wingsState = FIXED")
    @Flaky
    @Test(description = "�������� ������ �2")
    @CitrusTest
    public void successFly2(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "yellow", 0.01, "rubber", "quack", "FIXED");
        duckFly(runner, "${id}");
        validatePayloadResponse(runner, HttpStatus.OK, new Message().message("I can't fly"));
    }

    /**
     * ����� properties
     */

    @Step("��������� ���� ����� ������")
    @Description("wingsState = ACTIVE")
    @Flaky
    @Test(description = "��������� ����� ������ �1")
    @CitrusTest
    public void successProperties1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "yellow", 0.01, "rubber", "quack", "ACTIVE");
        duckProperties(runner, "${id}");
        validateStringResponse(runner, HttpStatus.OK, "{\n" +
                "  \"color\": \"yellow\",\n" +
                "  \"height\": 0.01,\n" +
                "  \"material\": \"rubber\",\n" +
                "  \"sound\": \"quack\",\n" +
                "  \"wingsState\": \"ACTIVE\"\n" +
                "}");
    }

    @Step("��������� ���� ����� ������")
    @Description("wingsState = FIXED")
    @Flaky
    @Test(description = "��������� ����� ������ �2")
    @CitrusTest
    public void successProperties2(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "yellow", 0.01, "rubber", "quack", "FIXED");
        duckProperties(runner, "${id}");
        validateJsonResponse(runner, HttpStatus.OK, "duckClient/createEndpoint3.json");
    }

    @Step("��������� ���� ����� ������")
    @Description("wingsState = UNDEFINED")
    @Flaky
    @Test(description = "��������� ����� ������ �3")
    @CitrusTest
    public void successProperties3(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.01).material("rubber").sound("quack").wingsState("UNDEFINED");
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", duck.color(), duck.height(), duck.material(), duck.sound(), duck.wingsState());
        duckProperties(runner, "${id}");
        validatePayloadResponse(runner, HttpStatus.OK, duck);
    }

    @Step("��������� ���� ����� ������")
    @Description("material != rubber")
    @Flaky
    @Test(description = "��������� ����� ������ �4")
    @CitrusTest
    public void successProperties4(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.01).material("metal").sound("quack").wingsState("ACTIVE");
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", duck.color(), duck.height(), duck.material(), duck.sound(), duck.wingsState());
        duckProperties(runner, "${id}");
        validatePayloadResponse(runner, HttpStatus.OK, duck);
    }

    /**
     * ����� quack
     */

    @Step("��������� ������")
    @Description("���������� �������� = 0, ���������� ������ = 2")
    @Test(description = "��������� ������ �1")
    @CitrusTest
    public void successQuack1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "red", 10.0, "rubber", "quack", "UNDEFINED");
        duckQuack(runner, "${id}", "0", "2");
        validateStringResponse(runner, HttpStatus.OK, "{\n" +
                "  \"sound\": \"\",\n" +
                "}");
    }

    @Step("��������� ������")
    @Description("���������� �������� = 3, ���������� ������ = 2")
    @Flaky
    @Test(description = "��������� ������ �2")
    @CitrusTest
    public void successQuack2(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "red", 10.0, "plastic", "quack", "ACTIVE");
        duckQuack(runner, "${id}", "3", "2");
        validatePayloadResponse(runner, HttpStatus.OK, new Sound().sound("quack-quack, quack-quack, quack-quack"));
    }

    @Step("��������� ������")
    @Description("���������� �������� = 2, ���������� ������ = 3")
    @Flaky
    @Test(description = "��������� ������ �3")
    @CitrusTest
    public void successQuack3(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "red", 10.0, "plastic", "quack", "UNDEFINED");
        duckQuack(runner, "${id}", "2", "3");
        validatePayloadResponse(runner, HttpStatus.OK, new Sound().sound("quack-quack-quack, quack-quack-quack"));
    }

    @Step("��������� ������")
    @Description("���������� �������� = 3, ���������� ������ = 3")
    @Flaky
    @Test(description = "��������� ������ �4")
    @CitrusTest
    public void successQuack4(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "red", 10.0, "plastic", "quack", "ACTIVE");
        duckQuack(runner, "${id}", "3", "3");
        validatePayloadResponse(runner, HttpStatus.OK, new Sound().sound("quack-quack-quack, quack-quack-quack, quack-quack-quack"));
    }

    @Step("��������� ������")
    @Description("���������� �������� = 3, ���������� ������ = 0")
    @Test(description = "��������� ������ �5")
    @CitrusTest
    public void successQuack5(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "red", 10.0, "plastic", "quack", "ACTIVE");
        duckQuack(runner, "${id}", "3", "0");
        validatePayloadResponse(runner, HttpStatus.OK, new Sound().sound(""));
    }

    /**
     * ����� swim
     */

    @Step("��������� ������")
    @Description("���������� ���� � ��������� ����������")
    @Flaky
    @Test(description = "��������� ������ �1")
    @CitrusTest
    public void successSwim1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", new Random().nextInt(Integer.MAX_VALUE) + 1);
        duckDeleteFromDbFinally(runner, "${id}");
        duckDeleteFromDb(runner, "${id}");
        duckCreateInDb(runner, "${id}", "green", 15.0, "rubber", "gagaga", "FIXED");
        duckSwim(runner, "${id}");
        validatePayloadResponse(runner, HttpStatus.OK, new Message().message("I�m swimming"));
    }
}
