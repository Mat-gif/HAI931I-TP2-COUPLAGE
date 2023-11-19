package org.example.ui;

import lombok.Setter;
import org.example.model.Coupling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Setter
public class ClusteringDetermination {


    /*


    Classe de clustering non utilisée
    dans le cadre de ce TP (test non concluants)


     */

    private ArrayList<Coupling> couplings = null;

    public List<List<String>> findClusters(List<Object> dendrogram, int M, float CP) {
        List<List<String>> clusters = new ArrayList<>();
        int numClasses = countClasses(dendrogram);

        if (numClasses > M / 2 && calculateAverageCoupling(couplings, extractClassesFromBranch(dendrogram)) > CP) {
            clusters.add(extractClassesFromBranch(dendrogram));
        } else {
            for (Object item : dendrogram) {
                if (item instanceof String) {
                    List<String> cluster = new ArrayList<>();
                    cluster.add((String) item);
                    clusters.add(cluster);
                } else if (item instanceof List) {
                    ArrayList<Object> branch = (ArrayList<Object>) item;
                    List<List<String>> subClusters = findClusters(branch, M, CP);
                    clusters.addAll(subClusters);
                }
            }
        }

        return clusters;
    }

    public int countClasses(List<Object> dendrogram) {
        int count = 0;

        for (Object item : dendrogram) {
            if (item instanceof String) {
                count++;
            } else if (item instanceof List) {
                count += countClasses((List<Object>) item);
            }
        }

        return count;
    }

    public List<String> extractClassesFromBranch(Object branch) {
        List<String> classes = new ArrayList<>();

        if (branch instanceof String) {
            classes.add((String) branch);
        } else if (branch instanceof List) {
            for (Object item : (List<?>) branch) {
                classes.addAll(extractClassesFromBranch(item));
            }
        }

        return classes;
    }

    public float calculateAverageCoupling(ArrayList<Coupling> couplings, List<String> classes) {
        float totalCoupling = 0;
        int count = 0;

        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                float couplingValue = getCouplingValue(couplings, classes.get(i), classes.get(j));
                if (couplingValue >= 0) {
                    totalCoupling += couplingValue;
                    count++;
                }
            }
        }

        return count > 0 ? totalCoupling / count : 0;
    }

    private float getCouplingValue(ArrayList<Coupling> couplings, String classA, String classB) {
        for (Coupling coupling : couplings) {
            if (coupling.getClasses().contains(classA) && coupling.getClasses().contains(classB)) {
                return coupling.getValue();
            }
        }
        return -1; // A value less than 0 to indicate no coupling found
    }



}
