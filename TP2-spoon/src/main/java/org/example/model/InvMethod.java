package org.example.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@Builder
@ToString
public class InvMethod implements Inv {
    private String method;
    private String obj;
    private String instance;
}
