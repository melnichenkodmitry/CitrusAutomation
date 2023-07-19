package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * Ответ getAllIds
 */

@Setter //установка значения свойств
@Getter //установка значения свойств
@Accessors(fluent = true) //создание методов доступа (геттеров и сеттеров)
@JsonInclude(JsonInclude.Include.NON_NULL) //игнорирование свойств объекта равных null
public class GetAllIdsRes {
    @JsonProperty
    private List<Integer> ids;
}
