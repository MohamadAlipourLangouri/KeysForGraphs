import BaseInfra.Pattern;
import BaseInfra.Vertex;
import Loader.KeyGenerator;
import Util.PostOrderTraversal;

import java.util.ArrayList;
import java.util.List;

public class textKeyGenerator {

    public static void main(String []args)
    {
        KeyGenerator generator = new KeyGenerator("/Users/mortezaalipour/IdeaProjects/KeysForGraphs/sampleKeys/key1.txt");
        List<Pattern> keys = generator.getKeys();
        System.out.println("");

        for (Pattern key:keys) {
            System.out.println(key);
            PostOrderTraversal potr = new PostOrderTraversal(key);
            potr.travers(key.getFocusVertex());
            potr.getSortedVertices().forEach(vertex ->System.out.println(vertex));

            System.out.println("================================");
        }

    }

}
