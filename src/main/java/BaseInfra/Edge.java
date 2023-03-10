package BaseInfra;

import org.jgrapht.graph.DefaultEdge;

public class Edge extends DefaultEdge {

    private String label;

    @Override
    public String toString() {
        return "(" + getSource() + " -> " + getTarget() + " : " + label + ")";
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Edge))
            return false;

        Edge edge = (Edge) obj;
        return label.equals(edge.label) && getSource().equals(edge.getSource()) && getTarget().equals(edge.getTarget());
    }

    public String getLabel() {
        return label;
    }

    public Edge(String label) {
        this.label = label.toLowerCase();
    }

    @Override
    public Vertex getTarget() {
        return (Vertex) super.getTarget();
    }

    @Override
    public Vertex getSource() {
        return (Vertex) super.getSource();
    }
}
