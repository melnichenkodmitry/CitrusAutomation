package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Ответ create, property и запрос create
 */

@Setter //установка значения свойств
@Getter //установка значения свойств
@Accessors(fluent = true) //создание методов доступа (геттеров и сеттеров)
@JsonInclude(JsonInclude.Include.NON_NULL) //игнорирование свойств объекта равных null
public class Duck {
    @JsonProperty
    private Integer id;
    @JsonProperty
    private String color;
    @JsonProperty
    private Double height;
    @JsonProperty
    private String material;
    @JsonProperty
    private String sound;
    @JsonProperty
    private String wingsState;

}
