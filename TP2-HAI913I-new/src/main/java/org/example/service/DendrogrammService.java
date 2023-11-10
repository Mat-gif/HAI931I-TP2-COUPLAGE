package org.example.service;

import lombok.NoArgsConstructor;
import org.example.model.Coupling;
import org.example.model.Pair;
import org.example.model.PairCluster;
import smile.data.type.ObjectType;


import java.util.*;

@NoArgsConstructor
public class DendrogrammService {

    public TreeMap<Float, Set<String>> initCoupleValue(ArrayList<Coupling> couplings) {
        TreeMap<Float, Set<String>> res = new TreeMap<>();
        for (Coupling coupling : couplings)
        {
            res.put(coupling.getValue(),coupling.getClasses());
        }
        return  res;
    }

    public HashMap<Set<String>, List<Set<String>>> initCoupleClasses(ArrayList<Coupling> couplings) {
        HashMap<Set<String>, List<Set<String>>> res = new HashMap<>();


        for (Coupling coupling : couplings)
        {
            ArrayList<Set<String>> list =  new ArrayList<>();
            HashSet<String> key1 = new HashSet<>();
            HashSet<String> key2 = new HashSet<>();
            String classe1 = (String) coupling.getClasses().toArray()[0];
            String classe2 = (String) coupling.getClasses().toArray()[1];
            key1.add(classe1);
            key2.add(classe2);
            list.add(key1);
            list.add(key2);
            res.put(coupling.getClasses(),list);
        }
        return  res;

    }




    public Pair modifValues(LinkedHashMap<Set<String>,Float> values, Set<String> classeAB, float valueAB, HashMap<Set<String>, List<Set<String>>> keys) {


        LinkedHashMap<Set<String>,Float> valuesCopy = new LinkedHashMap<>(values);
        Set<String> classeABCopy = new HashSet<>(classeAB);

        // key = 0.3
        for(Set<String> key : valuesCopy.keySet() )
        {
            float val = valuesCopy.get(key); // AC
            for (String classe : classeABCopy)
            {
                if (key.contains(classe)) // true car AC contient A et A appart AB
                {
                    Set<String> newKey = new HashSet<>();
                    List<Set<String>> newList = new ArrayList<>();

                    newKey.addAll(classeAB);
                    newKey.addAll(key);

                    Set<String> newKey2 = new HashSet<>(newKey);

                    if(values.get(newKey) == null)
                    {
                        values.put(newKey,val);
                    }else {
                        float newVal = val+values.get(newKey);
                        values.put(newKey,newVal);
                    }
                    newList.add(classeAB);
                    newKey2.removeAll(classeAB);
                    newList.add(newKey2);
                    keys.put(newKey,newList);
                    values.remove(key);
                    break;
                }
            }

        }

        return Pair.builder().values(values).keys(keys).build();
    }




    public LinkedHashMap<Set<String>, Float> initTest(ArrayList<Coupling> couplings) {

        Map<Set<String>, Float> myMap = new HashMap<>();
        for (Coupling coupling : couplings)
        {
            myMap.put(coupling.getClasses(),coupling.getValue());
        }

        // Convert the map entries to a list
        List<Map.Entry<Set<String>, Float>> entryList = new ArrayList<>(myMap.entrySet());

        // Sort the list based on the values
        entryList.sort(Comparator.comparing(Map.Entry::getValue));

        // Create a new LinkedHashMap to preserve the order of insertion
        LinkedHashMap<Set<String>, Float> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Set<String>, Float> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;


    }



    // Méthode pour récupérer la dernière entrée d'une LinkedHashMap
    public  Map.Entry<Set<String>, Float>  getLastEntry(LinkedHashMap<Set<String>, Float> linkedHashMap) {
        if (linkedHashMap.isEmpty()) {
            return null; // Retourne null si la structure est vide
        }

        // Créer une liste à partir de l'ensemble d'entrées de la LinkedHashMap
        List<Map.Entry<Set<String>, Float>> entryList = new ArrayList<>(linkedHashMap.entrySet());

        // Trier la liste en fonction des valeurs (Float)
        entryList.sort(Map.Entry.comparingByValue());

        // Récupérer la dernière entrée de la liste triée
        Map.Entry<Set<String>, Float> lastEntry = entryList.get(entryList.size() - 1);

        return lastEntry;
    }




}
