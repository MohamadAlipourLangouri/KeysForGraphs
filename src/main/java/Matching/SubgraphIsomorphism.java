package Matching;

import BaseInfra.*;
import Loader.GraphLoader;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SubgraphIsomorphism {

    private GraphLoader loader;
    private Pattern key;
    private HashSet<List<DataVertex>> duplicateNodes;

    public SubgraphIsomorphism(GraphLoader loader, Pattern key)
    {
        this.loader = loader;
        this.key = key;
        duplicateNodes = new HashSet<>();
    }

    public void findMatches(PatternVertex vertex)
    {
        HashMap<Edge, List<String>> recursiveVertices = new HashMap<>();
        for (Edge outGoingEdge : key.getPattern().outgoingEdgesOf(vertex)) {
            recursiveVertices.put(outGoingEdge, new ArrayList<>());
        }
        String type = vertex.getTypes().iterator().next();
        HashMap<String, HashSet<DataVertex>> allSignatures = new HashMap<>();
        for (Vertex v:loader.getGraph().getGraph().vertexSet()) {
            DataVertex vD = (DataVertex)v;
            String signature = "";
            if(vD.isMapped(vertex))
            {
                signature += vD.getAttributeValues(vertex.getAllAttributesList());
                if(!recursiveVertices.isEmpty())
                {
                    recursiveVertices.keySet().forEach(e -> recursiveVertices.get(e).clear());
                    for (Edge e: recursiveVertices.keySet()) {
                        PatternVertex ptnV = (PatternVertex) e.getTarget();
                        for (Edge eOut:loader.getGraph().getGraph().outgoingEdgesOf(v)) {
                            if(eOut.getTarget().isMapped(ptnV))
                            {
                                String id = ((DataVertex)eOut.getTarget()).getUniqueID();
                                if(id!=null)
                                    recursiveVertices.get(e).add(id);
                            }
                        }
                    }
                    if(!checkIfMatches(recursiveVertices))
                        continue;

                    List<List<String>> allLists = new ArrayList<>();
                    for (Edge e: recursiveVertices.keySet()) {
                        allLists.add(recursiveVertices.get(e));
                    }
                    List<String> result = new ArrayList<>();
                    generatePermutations(allLists, result, 0, "");
                    for (String rec:result) {
                        String appendedSignature = signature + rec;
                        if(!allSignatures.containsKey(appendedSignature))
                            allSignatures.put(appendedSignature, new HashSet<>());
                        allSignatures.get(appendedSignature).add(vD);
                    }
                }
                else
                {
                    if(!allSignatures.containsKey(signature))
                        allSignatures.put(signature, new HashSet<>());
                    allSignatures.get(signature).add(vD);
                }

            }
            for (String key:allSignatures.keySet()) {
                String id = RandomStringUtils.randomAlphabetic(20);
                for (DataVertex dv:allSignatures.get(key)) {
                    dv.setUniqueID(id);
                }
                if(allSignatures.get(key).size()>1)
                {
                    List<DataVertex> temp = new ArrayList<>();
                    for (DataVertex t:allSignatures.get(key)) {
                        temp.add(t);
                    }
                    duplicateNodes.add(temp);
                }
            }
        }
    }

    public HashSet<List<DataVertex>> getDuplicateNodes() {
        return duplicateNodes;
    }

    private boolean checkIfMatches(HashMap<Edge, List<String>> recursiveVertices)
    {
        for (Edge e: recursiveVertices.keySet()) {
            if(recursiveVertices.get(e).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void generatePermutations(List<List<String>> lists, List<String> result, int depth, String current) {
        if (depth == lists.size()) {
            result.add(current);
            return;
        }

        for (int i = 0; i < lists.get(depth).size(); i++) {
            generatePermutations(lists, result, depth + 1, current + lists.get(depth).get(i));
        }
    }

}
