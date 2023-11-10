package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;


import java.util.*;

@Data
@Getter
@Builder
public class PairCluster {
private LinkedList<LinkedList<Object>> classes = new LinkedList<>();
private LinkedList<Float> values = new LinkedList<>();

}
