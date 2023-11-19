package org.example.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Builder
public class Pair {

    private  HashMap<Set<String>, List<Set<String>>> keys = new HashMap<>();
    private LinkedHashMap<Set<String>,Float> values = new LinkedHashMap<>();

}
