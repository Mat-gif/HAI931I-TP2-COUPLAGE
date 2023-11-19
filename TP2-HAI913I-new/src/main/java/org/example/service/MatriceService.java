package org.example.service;

import lombok.NoArgsConstructor;
import org.example.model.Coupling;

import java.util.ArrayList;
import java.util.HashSet;


@NoArgsConstructor
public class MatriceService {







    private  double[][] initMatrix(int max)
    {
        double[][] matrix = new double[max][max];

        // Remplir la matrice avec des zéros
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                matrix[i][j] = 0.0000000000;
            }
        }
        return matrix;
    }

    private  void afficherM(double[][] matrix,int max)
    {
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                System.out.print(matrix[i][j] + " ---------");
            }
            System.out.println();
        }
    }


    public  double[][] uptdateMatrix( int max, ArrayList<Coupling> couplings, HashSet<String> names )
    {

        double[][] matrix = initMatrix(max);
        ArrayList<String> arrayList = new ArrayList<>(names);

        System.out.println(arrayList);

        for (Coupling coupling : couplings)
        {
            String classe1 = (String) coupling.getClasses().toArray()[0];
            String classe2 = (String) coupling.getClasses().toArray()[1];
            int index1 = arrayList.indexOf(classe1);
            int index2 = arrayList.indexOf(classe2);
            matrix[index1][index2] = coupling.getValue();
            matrix[index2][index1] = coupling.getValue();

        }


        afficherM(matrix,max);

        return matrix;
    }

}
