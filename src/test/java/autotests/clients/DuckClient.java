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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    @Description("CreateEndpoint")
    public void duckCreate(TestCaseRunner runner, String color, String height, String material, String sound, String wingsState) {
        runner.$(http().client(duckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "  \"color\": \"" + color + "\",\n" +
                        "  \"height\": " + height + ",\n" +
                        "  \"material\": \"" + material + "\",\n" +
                        "  \"sound\": \"" + sound + "\",\n" +
                        "  \"wingsState\": \"" + wingsState + "\"\n" +
                        "}"));
    }

    @Description("DeleteEndpoint")
    public void duckDelete(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }

    @Description("GetAllIdsEndpoint")
    public void duckGetAllIds(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/getAllIds"));
    }

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

    @Description("ValidateStringResponse")
    public void validateStringResponse(TestCaseRunner runner, String response) {
        runner.$(http().client(duckService)
                .receive() //получение ответа
                .response(HttpStatus.OK) //проверка статуса ответа
                .message()
                .type(MessageType.JSON) //проверка заголовка ответа
                .body(response));
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
