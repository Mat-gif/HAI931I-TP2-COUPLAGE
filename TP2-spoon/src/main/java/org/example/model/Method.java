package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@Getter
@Builder
public class Method {
    private String name;
    private ArrayList<Inv> invocations;

    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", invocations=" + invocations +
                '}';
    }
}
