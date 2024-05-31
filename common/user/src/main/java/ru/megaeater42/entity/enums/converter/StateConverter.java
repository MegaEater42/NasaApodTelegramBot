package ru.megaeater42.entity.enums.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.megaeater42.entity.enums.State;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class StateConverter implements AttributeConverter<State, String> {
    @Override
    public String convertToDatabaseColumn(State state) {
        if (state == null) {
            return null;
        }

        return state.toString();
    }

    @Override
    public State convertToEntityAttribute(String userStateString) {
        if (userStateString == null) {
            return null;
        }

        return Stream.of(State.values())
                .filter(userState -> userState.toString().equals(userStateString))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
