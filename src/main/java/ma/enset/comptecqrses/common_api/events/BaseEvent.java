package ma.enset.comptecqrses.common_api.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public abstract class BaseEvent<T> {
    @Getter
    private T id;

}
