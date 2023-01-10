package Util;

import BaseInfra.Edge;
import BaseInfra.Pattern;
import BaseInfra.Vertex;

import java.util.ArrayList;
import java.util.HashSet;

public class PostOrderTraversal {

    private Pattern pattern;
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private HashSet<Vertex> seenVertices;

    public PostOrderTraversal(Pattern pattern)
    {
        this.pattern = pattern;
        seenVertices = new HashSet<>();
    }

    public void travers(Vertex root)
    {
        seenVertices.add(root);
        for (Edge e:pattern.getPattern().outgoingEdgesOf(root)) {
            Vertex target = e.getTarget();
            if(!seenVertices.contains(target))
            {
                travers(target);
            }
        }
        vertices.add(root);
    }

    public ArrayList<Vertex> getSortedVertices() {
        return vertices;
    }
}
