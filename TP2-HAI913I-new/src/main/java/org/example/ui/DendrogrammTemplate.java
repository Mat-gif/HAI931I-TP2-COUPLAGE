package org.example.ui;


import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import lombok.NoArgsConstructor;
import org.example.model.Coupling;
import org.example.model.Pair;
import org.example.model.PairCluster;
import org.example.service.ClusterService;
import org.example.service.DendrogrammService;

import javax.swing.*;
import java.util.*;

@NoArgsConstructor
public class DendrogrammTemplate {

    private static final int WIDTH_FRAME = 1000;
    private static final int HEIGHT_FRAME = 1000;
    private static final int MARGIN = 100;
    private JFrame frame = new JFrame("");

    private static final float CP = 0.16F;
    private int nModule ;
    private Map<Set<String>,Object> sommets = new HashMap<>();
    private Object parent;

    public void createGraph(ArrayList<Coupling> couplings, int m) {
        SwingUtilities.invokeLater(() -> {
            mxGraph graph = initGraph(frame);
            DendrogrammService service = new DendrogrammService();

            nModule = Math.abs(m/2);
//            System.err.println(nModule);

            ClusterService clusterService = new ClusterService();



            HashMap<Set<String>, List<Set<String>>> keys = service.initCoupleClasses(couplings);
            LinkedHashMap<Set<String>,Float> values = service.initTest(couplings);
//            PairCluster pairCluster = PairCluster.builder().classes(new LinkedList<LinkedList<Object>>()).values(new LinkedList<Float>()).build();

            while (!values.isEmpty())
            {


                Map.Entry<Set<String>, Float>  lastEntry = service.getLastEntry(values);
//                System.out.println("--------->" +lastEntry);
                float valueAB = lastEntry.getValue();
                Set<String> classeAB = lastEntry.getKey();

//                pairCluster= service.saveStructure(pairCluster, classeAB, valueAB );

                List<Set<String>> classesComposantes = keys.get(classeAB);

                // Sommet du couplage
                Set<String> classeA =  classesComposantes.get(0);
                Set<String> classeB =  classesComposantes.get(1);
                // get sommet si ils existent
                Object sommetA = sommets.get(classeA);
                Object sommetB = sommets.get(classeB);


                if (sommetA == null)
                {
                    if (sommetB == null)
                    {
                        clusterService.calcul(valueAB,classeAB,nModule,CP);
                    }
                    clusterService.calcul(valueAB,classeA,nModule,CP);
                }else if(sommetB == null )
                {
                    clusterService.calcul(valueAB,classeB,nModule,CP);
                }

                //Sinon on les crees
                if (sommetA == null) {

                    sommetA = graph.insertVertex(parent, null, classeA, 20, 20, 80, 30);
                    sommets.put(classeA, sommetA);
                }

                if (sommetB == null) {


                    sommetB = graph.insertVertex(parent, null, classeB, 20, 20, 80, 30);
                    sommets.put(classeB, sommetB);
                }

                // On cree le noued de couplage
                Object sommetAB = graph.insertVertex(parent, null, valueAB, 20, 20, 80, 30);
                sommets.put(classeAB,sommetAB);


                mxRectangle dimensionsA = graph.getPreferredSizeForCell(sommetA);
                mxRectangle dimensionsB = graph.getPreferredSizeForCell(sommetB);
                mxRectangle dimensionsAB = graph.getPreferredSizeForCell(sommetAB);
                graph.resizeCell(sommetA, dimensionsA);
                graph.resizeCell(sommetB, dimensionsB);
                graph.resizeCell(sommetAB, dimensionsAB);

                Object arcA_AB = graph.insertEdge(parent, null,null,sommetA,sommetAB);
                Object arcB_AB = graph.insertEdge(parent, null,null,sommetB,sommetAB);

                values.remove(classeAB);
                keys.remove(classeAB);

                Pair pair = service.modifValues(values,classeAB,valueAB,keys);

                values= pair.getValues();
                keys= pair.getKeys();



            }


            System.out.println(nModule +" -> "+ clusterService.getModules().size());
            clusterService.getModules().forEach(System.err::println);

            showGraph(graph);
        });
    }



    private void showGraph(mxGraph graph)
    {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setIntraCellSpacing(100); // Espacement entre les nœuds dans la même couche
        layout.setInterRankCellSpacing(150); // Espacement entre les couches
        layout.execute(parent);
        graph.getModel().endUpdate();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        frame.getContentPane().add(graphComponent);
        frame.pack();
        frame.setVisible(true);
    }





    private mxGraph  initGraph(JFrame frame)
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mxGraph graph = new mxGraph();
        graph.setCellsEditable(true); // Désactiver l'édition
        graph.setCellsMovable(true); // Désactiver le déplacement
        graph.setCellsResizable(true); // Désactiver le redimensionnement
        graph.setDropEnabled(true); // Désactiver le glisser-déposer
        graph.setSplitEnabled(true); // Désactiver la division
        parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        return graph;
    }
}
