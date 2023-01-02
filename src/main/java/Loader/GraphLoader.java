package Loader;

import BaseInfra.Graph;
import BaseInfra.Pattern;
import BaseInfra.PatternVertex;
import BaseInfra.Vertex;
import Util.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base class for graph loaders
 * DBPedia and IMDB loaders extend this class
 */

public class GraphLoader {

    //region --[Fields: Protected]---------------------------------------

    protected Graph graph;
    protected Set <String> validTypes;
    protected Set<String> validAttributes;

    //endregion

    //region --[Constructors]--------------------------------------------

    public GraphLoader(List<Pattern> patterns)
    {
        graph=new Graph();
        validTypes=new HashSet <>();
        validAttributes=new HashSet<>();

        if(Config.optimizedLoadingBasedOnKeys)
            for (Pattern pattern:patterns) {
                extractValidTypesFromKeys(pattern);
                extractValidAttributesFromKeys(pattern);
            }
    }

    public GraphLoader()
    {
        graph=new Graph();
        validTypes=new HashSet <>();
        validAttributes=new HashSet<>();
    }


    //endregion

    //region --[Properties: Public]--------------------------------------

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    //endregion

    //region --[Private Methods]-----------------------------------------

    //endregion

    //region --[Private Methods]-----------------------------------------

    /**
     * Extracts all the types being used in a TGFD from from X->Y dependency and the graph pattern
     * @param pattern input Key
     */
    private void extractValidTypesFromKeys(Pattern pattern)
    {
        for (Vertex v:pattern.getPattern().vertexSet()) {
            if(v instanceof PatternVertex)
                validTypes.addAll(v.getTypes());
            System.out.println("Hello world");
        }
    }

    /**
     * Extracts all the attributes names being used in a TGFD from from X->Y dependency and the graph pattern
     * @param pattern input Key
     */
    private void extractValidAttributesFromKeys(Pattern pattern)
    {
        for (Vertex v:pattern.getPattern().vertexSet()) {
            if(v instanceof PatternVertex)
                validTypes.addAll(v.getAllAttributesNames());
        }
    }

    //endregion

}
