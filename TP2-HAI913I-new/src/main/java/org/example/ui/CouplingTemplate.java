package org.example.ui;


import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import lombok.NoArgsConstructor;
import org.example.model.Coupling;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class CouplingTemplate {

    private static final int WIDTH_FRAME = 1000;
    private static final int HEIGHT_FRAME = 1000;
    private static final int MARGIN = 100;
    private JFrame frame = new JFrame("Graphe de Couplage");

    private Map<String,Object> sommets = new HashMap<>();
    private Object parent;

    public void createGraph(ArrayList<Coupling> couplings) {
        SwingUtilities.invokeLater(() -> {
            mxGraph graph = initGraph(frame);

            for (Coupling couple : couplings)
            {
//                if(couple.getClasses().size() <= 1) continue;

                String classe1 = (String) couple.getClasses().toArray()[0];
                System.out.println(classe1);
                String classe2 = (String) couple.getClasses().toArray()[1];
                System.out.println(classe2);

                Object sommet1 = sommets.get(classe1);
                Object sommet2 = sommets.get(classe2);

                // je trace mes deux sommets
                if (sommet1 == null)
                {
                    sommet1 = graph.insertVertex(parent, null, classe1, 20, 20, 80, 30);
                    sommets.put(classe1,sommet1);
                }
                if (sommet2 == null)
                {
                    sommet2 = graph.insertVertex(parent, null, classe2, 20, 20, 80, 30);
                    sommets.put(classe2,sommet2);
                }

                mxRectangle dimensions1 = graph.getPreferredSizeForCell(sommet1);
                mxRectangle dimensions2 = graph.getPreferredSizeForCell(sommet2);
                graph.resizeCell(sommet1, dimensions1);
                graph.resizeCell(sommet2, dimensions2);

                // je trace un arc entre mes deux sommets
                Object arc = graph.insertEdge(parent, null,couple.getValue(),sommet1,sommet2);

            }
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
        graph.setCellsEditable(false); // Désactiver l'édition
        graph.setCellsMovable(false); // Désactiver le déplacement
        graph.setCellsResizable(false); // Désactiver le redimensionnement
        graph.setDropEnabled(false); // Désactiver le glisser-déposer
        graph.setSplitEnabled(false); // Désactiver la division
        parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        return graph;
    }
}
