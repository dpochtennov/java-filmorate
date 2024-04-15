package ru.yandex.practicum.filmorate.util;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@NoArgsConstructor
public class IdGenerator {
    private long idCounter = 1L;

    public synchronized long generateId() {
        return idCounter++;
    }
}
