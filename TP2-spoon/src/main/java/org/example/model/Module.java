package org.example.model;

import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Data
@Getter
public class Module {
    private Set<String>classes = new HashSet<>();
    private float moyenne ;
}
