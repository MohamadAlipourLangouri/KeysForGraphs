package Loader;

import BaseInfra.Attribute;
import BaseInfra.Edge;
import BaseInfra.Pattern;
import BaseInfra.PatternVertex;
import Util.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class KeyGenerator {

     private List<Pattern> keys;

    public KeyGenerator(String path)
    {
        keys=new ArrayList<>();
        loadGraphPattern(path);
    }

    public List<Pattern> getKeys() {
        return keys;
    }

    private void loadGraphPattern(String path) {

        System.out.println("Loading Keys from the path: " + path);
        if(path.equals(""))
            return;
        HashMap<String, PatternVertex> allVertices=new HashMap<>();
        Pattern currentKey=null;
        BufferedReader br=null;

        String line="";
        try
        {
            br = new BufferedReader(new FileReader(path));
            while ((line=br.readLine())!=null) {
                line = line.toLowerCase();
                if(line.startsWith("key"))
                {
                    if(currentKey!=null)
                    {
                        keys.add(currentKey);
                    }
                    currentKey=new Pattern();
                    allVertices=new HashMap<>();
                    String []args = line.split("#");
                    if(args.length==2)
                        currentKey.setName(args[1]);
                }
                else if(line.startsWith("v"))
                {
                    String []args = line.split("#");
                    PatternVertex v=new PatternVertex(args[2]);
                    for (int i=3;i<args.length;i++)
                    {
                        if(args[i].contains("$"))
                        {
                            String []attr=args[i].split("\\$");
                            if(attr.length==2)
                                v.addAttribute(new Attribute(attr[0],attr[1]));
                        }
                        else
                        {
                            v.addAttribute(new Attribute(args[i]));
                        }
                    }
                    currentKey.addVertex(v);
                    allVertices.put(args[1],v);
                }
                else if(line.startsWith("e"))
                {
                    String []args = line.split("#");
                    currentKey.addEdge(allVertices.get(args[1]),allVertices.get(args[2]),new Edge(args[3]));
                }
                else if(line.startsWith("c"))
                {
                    String []args = line.split("#");
                    currentKey.setFocusVertex(allVertices.get(args[1]));
                    currentKey.setFocusVertexType((allVertices.get(args[1])).getTypes().iterator().next());
                }
            }
            if(currentKey!=null)
            {
                keys.add(currentKey);
            }
            br.close();
            System.out.println("Number of Keys loaded: " + keys.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
