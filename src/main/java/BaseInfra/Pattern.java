package BaseInfra;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.Serializable;
import java.util.*;

public class Pattern implements Serializable {

    private Graph<Vertex, Edge> pattern;

    private String focusVertexType ="";

    private Vertex focusVertex;

    private String name = "";



    private HashMap<Vertex, Integer> vertexToRadius = new HashMap<>();

    public Pattern()
    {
        pattern = new DefaultDirectedGraph<>(Edge.class);
    }

    public Graph<Vertex, Edge> getPattern() {
        return pattern;
    }

    public void addVertex(PatternVertex v)
    {
        pattern.addVertex(v);
    }

    public void addEdge(PatternVertex v1, PatternVertex v2, Edge edge)
    {
        pattern.addEdge(v1,v2,edge);
    }

    public String getFocusVertexType()
    {
        return focusVertexType;
    }

    public void setFocusVertex(Vertex focusVertex) {
        this.focusVertex = focusVertex;
    }

    public Vertex getFocusVertex() {
        return focusVertex;
    }

    public void setFocusVertexType(String focusVertexType) {
        this.focusVertexType = focusVertexType;
    }

    public int getSize()
    {
        return this.pattern.edgeSet().size();
    }

    @Override
    public String toString() {
        StringBuilder res= new StringBuilder("Pattern{");
        for (Edge edge: pattern.edgeSet()) {
            res.append(edge.toString());
        }
        res.append('}');
        return res.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
