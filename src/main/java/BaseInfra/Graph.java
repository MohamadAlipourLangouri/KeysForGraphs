package BaseInfra;

import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.Serializable;
import java.util.*;

public class Graph implements Serializable {

    private org.jgrapht.Graph<Vertex, Edge> graph = new DefaultDirectedGraph<>(Edge.class);

    private HashMap<String, Vertex> nodeMap;

    public Graph()
    {
        nodeMap= new HashMap<>();
    }

    public Graph(org.jgrapht.Graph<Vertex, Edge> graph)
    {
        nodeMap= new HashMap<>();
        this.graph = graph;
        for (Vertex v:graph.vertexSet()) {
            DataVertex dataV=(DataVertex) v;
            if(!nodeMap.containsKey(dataV.getURI())) {
                nodeMap.put(dataV.getURI(), dataV);
            }
        }
    }

    public org.jgrapht.Graph<Vertex, Edge> getGraph() {
        return graph;
    }

    public void addVertex(DataVertex v)
    {
        if(!nodeMap.containsKey(v.getURI()))
        {
            graph.addVertex(v);
            nodeMap.put(v.getURI(),v);
        }
    }

    public Vertex getNode(String vertexURI)
    {
        return nodeMap.getOrDefault(vertexURI, null);
    }

    public boolean addEdge(DataVertex v1, DataVertex v2, Edge edge)
    {
        graph.addEdge(v1,v2,edge);
        return true;
    }

    public void removeEdge(DataVertex v1, DataVertex v2, Edge edge)
    {
        for (Edge e:graph.outgoingEdgesOf(v1)) {
            DataVertex target=(DataVertex) e.getTarget();
            if(target.getURI().equals(v2.getURI()) && edge.getLabel().equals(e.getLabel()))
            {
                this.graph.removeEdge(e);
                return;
            }
        }
    }

    public int getSize()
    {
        return nodeMap.size();
    }

    public HashMap<String, Vertex> getNodeMap() {
        return nodeMap;
    }

    public boolean deleteVertex(DataVertex vertex)
    {
        if(!nodeMap.containsKey(vertex.getURI()))
            return false;

        DataVertex v = (DataVertex) this.getNode(vertex.getURI());
        if (v == null)
            return false;
        List<Edge> edgesToDelete = new ArrayList<>(graph.edgesOf(v));
        for (Edge e : edgesToDelete) {
            graph.removeEdge(e);
        }
        boolean deleteVertex = graph.removeVertex(v);
        nodeMap.remove(v.getURI());

        return deleteVertex;
    }

    private Set <String> extractValidTypesFromPatterns(Pattern pattern)
    {
        Set <String> validTypes=new HashSet<>();
        for (Vertex v:pattern.getPattern().vertexSet()) {
            if(v instanceof PatternVertex)
                validTypes.addAll(v.getTypes());
        }
        return validTypes;
    }

    private boolean isValidType(Set<String> validTypes, Set<String> givenTypes)
    {
        return givenTypes.stream().anyMatch(validTypes::contains);
    }



}