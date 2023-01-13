package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

public class Config {

    public static ArrayList<String> typesPaths = new ArrayList<>();
    public static ArrayList<String> dataPaths = new ArrayList<>();

    public static String patternPath = "";
    public static String language="N-Triples";
    public static dataset datasetName;
    public static boolean optimizedLoadingBasedOnKeys=false;
    public static boolean optimizedLoadingBasedOnAttributesInKeys = false;
    public static boolean saveDuplicates=false;
    public static boolean debug =false;

    public static String path = "";

    public static void parse(String input) throws FileNotFoundException {
        if(input.equals("--help")) {
            System.out.println(" " +
                    "Expected arguments to parse: " +
                    "-p <path to the patternFile> // in case of Amazon S3, it should be in the form of bucket_name/key " +
                    "[-t <typeFile>]" +
                    "[-d <dataFile>] " +
                    "-optgraphload <true-false> // load parts of data file that are needed based on the Keys" +
                    "-debug <true-false> // print details of matching " +
                    "-language <language name> // Names like \"N-Triples\", \"TURTLE\", \"RDF/XML\"" +
                    "-dataset <dataset name> // Options: imdb (default), dbpedia, synthetic, pdd");
        } else
            parseInputParams(input);
    }

    private static void parseInputParams(String pathToConfigFile) {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(pathToConfigFile));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] conf = line.toLowerCase().split(" ");
                if (conf.length != 2)
                    continue;
                if (conf[0].equals("-optgraphload")) {
                    optimizedLoadingBasedOnKeys = Boolean.parseBoolean(conf[1]);
                } else if (conf[0].equals("-debug")) {
                    debug = Boolean.parseBoolean(conf[1]);
                }
                else if(conf[0].equals("-saveduplicates")) {
                    saveDuplicates=Boolean.parseBoolean(conf[1]);
                }
                else if(conf[0].equals("-language")) {
                    language=conf[1];
                }else if(conf[0].equals("-dataset")) {
                    try {
                        datasetName = dataset.valueOf(conf[1]);
                    }
                    catch (IllegalArgumentException exception)
                    {
                        System.out.println("Error in dataset name: " + exception.getMessage());
                        System.out.println("Dataset name is being set to default name: \"IMDB\"");
                        datasetName= dataset.imdb;
                    }
                }else if (conf[0].startsWith("-t")) {
                    typesPaths.add(conf[1]);
                } else if (conf[0].startsWith("-d")) {
                    dataPaths.add(conf[1]);
                }  else if (conf[0].startsWith("-p")) {
                    patternPath = conf[1];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public enum dataset
    {
        pdd, dbpedia, imdb, synthetic
    }

}
