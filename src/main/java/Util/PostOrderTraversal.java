package Util;

import BaseInfra.Edge;
import BaseInfra.Pattern;
import BaseInfra.Vertex;

import java.util.ArrayList;

public class PostOrderTraversal {

    private Pattern pattern;
    private ArrayList<Vertex> vertices = new ArrayList<>();

    public PostOrderTraversal(Pattern pattern)
    {
        this.pattern = pattern;
    }

    public void travers(Vertex root)
    {
        vertices.add(root);
        for (Edge e:pattern.getPattern().outgoingEdgesOf(root)) {
            //This is the nodex that are connected to the input root
            Vertex target = e.getTarget();
            // You need to recursively call
            // Also, make sure not to call a node more than once
        }
    }

    public ArrayList<Vertex> getSortedVertices() {
        return vertices;
    }
}
