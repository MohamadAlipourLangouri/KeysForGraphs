import BaseInfra.Pattern;
import Loader.KeyGenerator;

import java.util.ArrayList;
import java.util.List;

public class textKeyGenerator {

    public static void main(String []args)
    {
        KeyGenerator generator = new KeyGenerator("/Users/mortezaalipour/IdeaProjects/KeysForGraphs/sampleKeys/key1.txt");
        List<Pattern> keys = generator.getKeys();
        System.out.println("");

    }

}
