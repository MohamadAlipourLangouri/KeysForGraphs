import BaseInfra.Pattern;
import BaseInfra.PatternVertex;
import BaseInfra.Vertex;
import Loader.DBPediaLoader;
import Loader.KeyGenerator;
import Matching.SubgraphIsomorphism;
import Util.Config;
import Util.PostOrderTraversal;

import java.io.FileNotFoundException;
import java.util.List;

public class textKeyGenerator {

    public static void main(String []args) throws FileNotFoundException {

        Config.parse("/Users/mortezaalipour/IdeaProjects/KeysForGraphs/config/config.txt");

        KeyGenerator generator = new KeyGenerator(Config.patternPath);
        List<Pattern> keys = generator.getKeys();
        DBPediaLoader dbpedia = new DBPediaLoader(keys, Config.typesPaths, Config.dataPaths);

        for (Pattern key:keys) {
            SubgraphIsomorphism matching = new SubgraphIsomorphism(dbpedia, key);

            System.out.println(key);
            PostOrderTraversal potr = new PostOrderTraversal(key);
            potr.travers(key.getFocusVertex());
            for (Vertex v: potr.getSortedVertices()) {
                matching.findMatches((PatternVertex) v);
            }
            matching.getDuplicateNodes().forEach(vertex ->System.out.println(vertex));

            System.out.println("================================");
        }

    }

}
