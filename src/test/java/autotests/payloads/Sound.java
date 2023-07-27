package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter //установка значения свойств
@Getter //установка значения свойств
@Accessors(fluent = true) //создание методов доступа (геттеров и сеттеров)
//@JsonInclude(JsonInclude.Include.NON_NULL) //игнорирование свойств объекта равных null
public class Sound {
    @JsonProperty
    private String sound;
}
