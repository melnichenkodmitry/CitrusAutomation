package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.validation.json.JsonPathMessageValidationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource db;

//    @Step("Отправка GET запроса")
    protected void sendGetRequest(TestCaseRunner runner, HttpClient URL, String path, String nameQuery, String valueQuery) {
        runner.$(http().client(URL)
                .send()
                .get(path)
                .queryParam(nameQuery, valueQuery));
    }

//    @Step("Отправка POST запроса через строку")
    protected void sendPostStringRequest(TestCaseRunner runner, HttpClient URL, String path, String body) {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body));
    }

//    @Step("Отправка POST запроса через payload")
    protected void sendPostPayloadRequest(TestCaseRunner runner, HttpClient URL, String path, Object payload) {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(payload, new ObjectMapper())));
    }

//    @Step("Отправка POST запроса через JSON")
    protected void sendPostJsonRequest(TestCaseRunner runner, HttpClient URL, String path, String payload) {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(payload)));
    }

    protected void validateStringResponse(TestCaseRunner runner, HttpClient URL, HttpStatus status, String response) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .body(response));
    }

    protected void validateJsonResponse(TestCaseRunner runner, HttpClient URL, HttpStatus status, String expectedPayload) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource(expectedPayload)));
    }

    protected void validatePayloadResponse(TestCaseRunner runner, HttpClient URL, HttpStatus status, Object expectedPayload) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    protected void validateJsonPathResponse(TestCaseRunner runner, HttpClient URL, HttpStatus status, JsonPathMessageValidationContext.Builder body) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .validate(body));
    }

    protected void duckDeleteFinally(TestCaseRunner runner, HttpClient URL, String path, String nameQuery, String valueQuery) {
        runner.$(doFinally().actions(http().client(URL)
                .send()
                .delete(path)
                .queryParam(nameQuery, valueQuery)));
    }

    protected void extractId(TestCaseRunner runner, HttpClient URL, HttpStatus status) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.id", "id")));
    }

    protected void extractDuck(TestCaseRunner runner, HttpClient URL, HttpStatus status) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.id", "id"))
                .extract(fromBody().expression("$.color", "color"))
                .extract(fromBody().expression("$.height", "height"))
                .extract(fromBody().expression("$.material", "material"))
                .extract(fromBody().expression("$.sound", "sound"))
                .extract(fromBody().expression("$.wingsState", "wingsState")));
    }

    protected void databaseUpdate(TestCaseRunner runner, SingleConnectionDataSource dataSource, String sql) {
        runner.$(sql(dataSource)
                .statement(sql));
    }

    protected void databaseUpdateFinally(TestCaseRunner runner, SingleConnectionDataSource dataSource, String sql) {
        runner.$(doFinally().actions(sql(dataSource)
                .statement(sql)));
    }

    protected void validateDuckInDb(TestCaseRunner runner, SingleConnectionDataSource dataSource, String id, String color, String height, String material, String sound, String wingsState) {
        runner.$(query(dataSource)
                .statement("SELECT * FROM DUCK WHERE ID = " + id)
                .validate("COLOR", color)
                .validate("HEIGHT", height)
                .validate("MATERIAL", material)
                .validate("SOUND", sound)
                .validate("WINGS_STATE", wingsState));
    }
}
