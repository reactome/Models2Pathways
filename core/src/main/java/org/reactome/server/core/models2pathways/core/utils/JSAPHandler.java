package org.reactome.server.core.models2pathways.core.utils;


import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import org.reactome.server.core.models2pathways.core.entrypoint.Models2Pathways;

/**
 * Created by Maximilian Koch (mkoch@ebi.ac.uk).
 */
public class JSAPHandler {
    public static JSAPResult ArgumentHandler(String[] args) {
        JSAP jsap = new JSAP();
        FlaggedOption opt1 = new FlaggedOption("significantFDR")
                .setStringParser(JSAP.DOUBLE_PARSER)
                .setRequired(false)
                .setShortFlag('s')
                .setDefault(String.valueOf(0.005))
                .setLongFlag(String.valueOf("significantFDR"));
        opt1.setHelp("Value of the FDR for significant results");

        FlaggedOption opt2 = new FlaggedOption("extendedFDR")
                .setStringParser(JSAP.DOUBLE_PARSER)
                .setRequired(false)
                .setShortFlag('e')
                .setLongFlag("extendedFDR");
        opt2.setHelp("Value of the FDR for possible results");

        FlaggedOption opt3 = new FlaggedOption("output")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag('o')
                .setLongFlag("output");
        opt3.setHelp("Path to output tsv");

        FlaggedOption opt4 = new FlaggedOption("coverage")
                .setStringParser(JSAP.DOUBLE_PARSER)
                .setRequired(false)
                .setShortFlag('c')
                .setDefault(String.valueOf(0.5))
                .setLongFlag("coverage");
        opt4.setHelp("minimum pathway reaction coverage");

        FlaggedOption opt5 = new FlaggedOption("header")
                .setStringParser(JSAP.BOOLEAN_PARSER)
                .setRequired(false)
                .setShortFlag('h')
                .setDefault(String.valueOf(true))
                .setLongFlag("header");
        opt5.setHelp("boolean for witting a header into the tsv-file");

        try {
            jsap.registerParameter(opt1);
            jsap.registerParameter(opt2);
            jsap.registerParameter(opt3);
            jsap.registerParameter(opt4);
            jsap.registerParameter(opt5);
        } catch (JSAPException e) {
            e.printStackTrace();
        }

        JSAPResult jsapResult = jsap.parse(args);

        if (!jsapResult.success()) {
            System.err.println();
            System.err.println("Usage: java "
                    + Models2Pathways.class.getName());
            System.err.println("                "
                    + jsap.getUsage());
            System.err.println();
            System.err.println(jsap.getHelp());
            System.exit(1);
        }
        return jsapResult;
    }
}