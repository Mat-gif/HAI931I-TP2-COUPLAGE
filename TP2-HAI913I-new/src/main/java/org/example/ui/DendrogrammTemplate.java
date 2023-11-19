package org.example.ui;


import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import lombok.NoArgsConstructor;
import org.example.model.Coupling;
import org.example.model.Pair;
import org.example.model.PairCluster;
import org.example.service.ClusterService;
import org.example.service.DendrogrammService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

@NoArgsConstructor
public class DendrogrammTemplate {

    private static final int WIDTH_FRAME = 1000;
    private static final int HEIGHT_FRAME = 1000;
    private static final int MARGIN = 100;
    private JFrame frame = new JFrame("");

    private static final float CP = 0.15F;
    private int nModule ;
    private Map<Set<String>,Object> sommets = new HashMap<>();
    private Object feuille;



    public void createGraph(ArrayList<Coupling> couplings, int m) {
        SwingUtilities.invokeLater(() -> {
            mxGraph graph = initGraph(frame);
            DendrogrammService service = new DendrogrammService();
            // Créer un bouton de dézoom




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

                    sommetA = graph.insertVertex(feuille, null, classeA, 20, 20, 80, 30);
                    sommets.put(classeA, sommetA);
                }

                if (sommetB == null) {
                    sommetB = graph.insertVertex(feuille, null, classeB, 20, 20, 80, 30);
                    sommets.put(classeB, sommetB);

                }

                // On cree le noued de couplage
                Object sommetAB = graph.insertVertex(feuille, null, valueAB, 20, 20, 80, 30);
                sommets.put(classeAB,sommetAB);

                mxRectangle dimensionsA = graph.getPreferredSizeForCell(sommetA);
                mxRectangle dimensionsB = graph.getPreferredSizeForCell(sommetB);
                mxRectangle dimensionsAB = graph.getPreferredSizeForCell(sommetAB);
                graph.resizeCell(sommetA, dimensionsA);
                graph.resizeCell(sommetB, dimensionsB);
                graph.resizeCell(sommetAB, dimensionsAB);

                mxCell cellA = (mxCell) sommetA;
                mxCell cellB = (mxCell) sommetB;

                Object arcA_AB = graph.insertEdge(feuille, null,null,sommetA,sommetAB);
                Object arcB_AB = graph.insertEdge(feuille, null,null,sommetB,sommetAB);
                String edgeStyle =
                        mxConstants.STYLE_ROUNDED + "=false;" +
                        mxConstants.STYLE_ELBOW + "=vertical";

                graph.getModel().setStyle(arcA_AB, edgeStyle);
                graph.getModel().setStyle(arcB_AB, edgeStyle);
                values.remove(classeAB);
                keys.remove(classeAB);

                Pair pair = service.modifValues(values,classeAB,valueAB,keys);

                values= pair.getValues();
                keys= pair.getKeys();


            }


            System.out.println("nModule: "+ nModule +" ->  nbClusters : "+ clusterService.getModules().size());
            clusterService.getModules().forEach(System.err::println);

            showGraph(graph, feuille);
        });
    }



    private void showGraph(mxGraph graph, Object feuille)
    {
//        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
//        layout.setIntraCellSpacing(100); // Espacement entre les nœuds dans la même couche
//        layout.setInterRankCellSpacing(150); // Espacement entre les couches
//        layout.execute(feuille);
        organizeCellsManually(graph, feuille);
        graph.getModel().endUpdate();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setPreferredSize(new Dimension(800, 800));frame.getContentPane().add(graphComponent);
        frame.getContentPane().add(graphComponent);
//        graphComponent.zoomTo(0.5,true);
        frame.pack();
        frame.setVisible(true);
    }



    private void organizeCellsManually(mxGraph graph, Object parent) {
        Object[] cells = graph.getChildVertices(parent);
        int j = 0;
        int x0 = 100;
        int y = 100;

        for (Object cell : cells) {
            mxCell cell2 = (mxCell) cell;
            if(! (cell2.getValue() instanceof Float)) {
                graph.getModel().setGeometry(cell, new mxGeometry(x0, 100, 150, 30));
                x0 += 200;

            }else{
                mxCell cell3 = (mxCell) cells[j-1];
                mxCell cell4 = (mxCell) cells[j-2];
                float test = (float) Math.abs((cell3.getGeometry().getX()-cell4.getGeometry().getX()));
                float min = (float) Math.min(cell3.getGeometry().getX(),cell4.getGeometry().getX());
                y += 100;
                graph.getModel().setGeometry(cell, new mxGeometry((double) min+(test /2), y, 5, 5));
            }
            j+=1;
        }
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
        feuille = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        return graph;
    }
}
