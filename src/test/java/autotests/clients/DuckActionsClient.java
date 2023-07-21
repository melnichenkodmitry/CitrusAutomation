package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    @Description("FlyEndpoint")
    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id));
    }

    @Description("PropertiesEndpoint")
    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    @Description("QuackEndpoint")
    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    @Description("SwimEndpoint")
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id));
    }

    @Description("ValidateStringResponse")
    public void validateStringResponse(TestCaseRunner runner, String response) {
        runner.$(http().client(duckService)
                .receive() //получение ответа
                .response(HttpStatus.OK) //проверка статуса ответа
                .message()
                .type(MessageType.JSON) //проверка заголовка ответа
                .body(response)); //проверка тела ответа
    }

    @Description("ValidateJSONResponse")
    public void validateJSONResponse(TestCaseRunner runner, String expectedPayload) {
        runner.$(http().client(duckService)
                .receive() //получение ответа
                .response(HttpStatus.OK) //проверка статуса ответа
                .message()
                .type(MessageType.JSON) //проверка заголовка ответа
                .body(new ClassPathResource(expectedPayload))); //проверка тела ответа
    }

    @Description("ValidatePayloadResponse")
    public void validatePayloadResponse(TestCaseRunner runner, Object expectedPayload) {
        runner.$(http().client(duckService)
                .receive() //получение ответа
                .response(HttpStatus.OK) //проверка статуса ответа
                .message()
                .type(MessageType.JSON) //проверка заголовка ответа
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))); //проверка тела ответа
    }
}
