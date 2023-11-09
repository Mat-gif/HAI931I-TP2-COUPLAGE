package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;

@Data
@Getter
@Builder
@ToString
public class Coupling {
    private HashSet<String> classes;
    private float value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coupling coupling)) return false;
        return Float.compare(getValue(), coupling.getValue()) == 0 && Objects.equals(getClasses(), coupling.getClasses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClasses(), getValue());
    }
}
