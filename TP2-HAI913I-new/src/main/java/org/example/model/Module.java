package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Getter
public class Module {
    private Set<String>classes = new HashSet<>();
    private float moyenne ;
}
