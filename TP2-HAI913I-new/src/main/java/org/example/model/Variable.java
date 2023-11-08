package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@Builder
@ToString
public class Variable {
    private String name ;
    private String type ;
}