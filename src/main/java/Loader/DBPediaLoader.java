package Loader;

import BaseInfra.Attribute;
import BaseInfra.DataVertex;
import BaseInfra.Edge;
import BaseInfra.Pattern;
import Util.Config;
import org.apache.jena.rdf.model.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class DBPediaLoader extends GraphLoader {

    //region --[Methods: Private]---------------------------------------

    /**
     * @param patterns List of Keys
     * @param typesPath Path to the DBPedia type file
     * @param dataPaths Path to the DBPedia graph file
     */
    public DBPediaLoader(List<Pattern> patterns, List<String> typesPath, List<String> dataPaths)
    {
        super(patterns);

        for (String typePath:typesPath)
            loadNodeMap(typePath);

        for (String dataPath:dataPaths)
            loadDataGraph(dataPath);
    }

    //endregion

    //region --[Methods: Private]---------------------------------------

    /**
     * Load file in the format of (subject, predicate, object)
     * This will load the type file and create a DataVertex for each different subject with type of object
     * @param nodeTypesPath Path to the Type file
     */
    private void loadNodeMap(String nodeTypesPath) {

        if (nodeTypesPath == null || nodeTypesPath.length() == 0) {
            System.out.println("No Input Node Types File Path!");
            return;
        }
        try
        {
            Model model = ModelFactory.createDefaultModel();
            System.out.println("Loading Node Types: " + nodeTypesPath);


            Path input= Paths.get(nodeTypesPath);
            model.read(input.toUri().toString());

            StmtIterator typeTriples = model.listStatements();

            while (typeTriples.hasNext()) {
                Statement stmt = typeTriples.nextStatement();

                String nodeURI = stmt.getSubject().getURI().toLowerCase();
                if (nodeURI.length() > 28) {
                    nodeURI = nodeURI.substring(28);
                }
                String nodeType = stmt.getObject().asResource().getLocalName().toLowerCase();

                // ignore the node if the type is not in the validTypes and
                // optimizedLoadingBasedOnTGFD is true
                if(Config.optimizedLoadingBasedOnKeys && !validTypes.contains(nodeType))
                    continue;
                //int nodeId = subject.hashCode();
                DataVertex v= (DataVertex) graph.getNode(nodeURI);

                if (v==null) {
                    v=new DataVertex(nodeURI,nodeType);
                    graph.addVertex(v);
                }
                else {
                    v.addType(nodeType);
                }
            }
            System.out.println("Done. Number of Types: " + graph.getSize());
            model.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method will load DBPedia graph file
     * @param dataGraphFilePath Path to the graph file
     */
    private void loadDataGraph(String dataGraphFilePath) {

        if (dataGraphFilePath == null || dataGraphFilePath.length() == 0) {
            System.out.println("No Input Graph Data File Path!");
            return;
        }
        System.out.println("Loading DBPedia Graph: "+dataGraphFilePath);
        int numberOfObjectsNotFound=0,numberOfSubjectsNotFound=0;

        try
        {
            Model model = ModelFactory.createDefaultModel();
            Path input= Paths.get(dataGraphFilePath);
            model.read(input.toUri().toString());

            StmtIterator dataTriples = model.listStatements();

            while (dataTriples.hasNext()) {

                Statement stmt = dataTriples.nextStatement();
                String subjectNodeURI = stmt.getSubject().getURI().toLowerCase();
                if (subjectNodeURI.length() > 28) {
                    subjectNodeURI = subjectNodeURI.substring(28);
                }

                String predicate = stmt.getPredicate().getLocalName().toLowerCase();
                RDFNode object = stmt.getObject();
                String objectNodeURI;

                if (object.isLiteral()) {
                    objectNodeURI = object.asLiteral().getString().toLowerCase();
                } else {
                    objectNodeURI = object.toString().substring(object.toString().lastIndexOf("/")+1).toLowerCase();
                }

                DataVertex subjVertex= (DataVertex) graph.getNode(subjectNodeURI);

                if (subjVertex==null) {

                    //System.out.println("Subject node not found: " + subjectNodeURI);
                    numberOfSubjectsNotFound++;
                    continue;
                }

                if (!object.isLiteral()) {
                    DataVertex objVertex= (DataVertex) graph.getNode(objectNodeURI);
                    if(objVertex==null)
                    {
                        //System.out.println("Object node not found: " + subjectNodeURI + "  ->  " + predicate + "  ->  " + objectNodeURI);
                        numberOfObjectsNotFound++;
                        continue;
                    }
                    else if (subjectNodeURI.equals(objectNodeURI)) {
                        //System.out.println("Loop found: " + subjectNodeURI + " -> " + objectNodeURI);
                        continue;
                    }
                    graph.addEdge(subjVertex, objVertex, new Edge(predicate));
                }
                else
                {
                    if(!Config.optimizedLoadingBasedOnKeys || !Config.optimizedLoadingBasedOnAttributesInKeys || (validAttributes.contains(predicate)))
                    {
                        subjVertex.addAttribute(new Attribute(predicate,objectNodeURI));
                    }
                }
            }
            System.out.println("Subjects and Objects not found: " + numberOfSubjectsNotFound + " ** " + numberOfObjectsNotFound);
            System.out.println("Done. Nodes: " + graph.getGraph().vertexSet().size() + ",  Edges: " +graph.getGraph().edgeSet().size());
            //System.out.println("Number of subjects not found: " + numberOfSubjectsNotFound);
            //System.out.println("Number of loops found: " + numberOfLoops);

            model.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //endregion

}
