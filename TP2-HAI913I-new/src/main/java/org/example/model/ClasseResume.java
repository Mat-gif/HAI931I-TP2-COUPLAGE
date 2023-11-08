package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;

@Data
@Getter
@Builder
@ToString
public class ClasseResume {
    private String name;
    private int tot;
    private HashMap<String, Integer> countByClass;
}
