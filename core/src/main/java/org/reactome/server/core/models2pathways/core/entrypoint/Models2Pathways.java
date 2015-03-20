package org.reactome.server.core.models2pathways.core.entrypoint;

import com.martiansoftware.jsap.JSAPResult;
import org.reactome.server.core.models2pathways.biomodels.model.SBMLModel;
import org.reactome.server.core.models2pathways.core.Consumer;
import org.reactome.server.core.models2pathways.core.Producer;
import org.reactome.server.core.models2pathways.core.helper.NameSpaceHelper;
import org.reactome.server.core.models2pathways.core.helper.SpeciesHelper;
import org.reactome.server.core.models2pathways.core.helper.TrivialChemicalHelper;
import org.reactome.server.core.models2pathways.core.utils.FileExporter;
import org.reactome.server.core.models2pathways.core.utils.JSAPHandler;
import org.reactome.server.core.models2pathways.core.utils.ProptertiesLoader;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;


/**
 * @author Maximilian Koch <mkoch@ebi.ac.uk>
 */
public class Models2Pathways {
    final static Logger logger = Logger.getLogger(Models2Pathways.class.getName());

    final static int BLOCKING_QUEUE_SIZE = 3;

    private static Thread PRODUCER;

    public static void main(String[] args) {
        logger.info("Process has been started");
        //Set up all given arguments.
        JSAPResult jsapResult = JSAPHandler.ArgumentHandler(args);
        FileExporter.setLocationPath(jsapResult.getString("output"));

        try {
            SpeciesHelper.getInstance().setSpecies(ProptertiesLoader.readSpecies(""));
            NameSpaceHelper.getInstance().setNamespaces(ProptertiesLoader.readNamespaces(""));
            TrivialChemicalHelper.getInstance().setTrivialChemicals(ProptertiesLoader.readTrivialChemicals(""));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Setting up output file
        FileExporter.createFile();

        //Shared blockingqueue for producert consumer.
        BlockingQueue<SBMLModel> sbmlModelBlockingQueue = new LinkedBlockingDeque<>(BLOCKING_QUEUE_SIZE);

        //Let's go... starting threads
        Producer producer = new Producer(sbmlModelBlockingQueue);

        Models2Pathways.PRODUCER = new Thread(producer);
        Models2Pathways.PRODUCER.start();
        logger.info("Started producer process");

        Consumer consumer = null;
        if (jsapResult.getString("extendedFDR") == null) {
            consumer = new Consumer(sbmlModelBlockingQueue, jsapResult.getDouble("significantFDR"), jsapResult.getDouble("coverage"));

        } else {
            if (jsapResult.getDouble("significantFDR") < jsapResult.getDouble("extendedFDR")) {
                consumer = new Consumer(sbmlModelBlockingQueue, jsapResult.getDouble("significantFDR"), jsapResult.getDouble("extendedFDR"), jsapResult.getString("coverage"));
            } else {
                logger.info("significantFDR is bigger than extendedFDR");
                System.exit(1);
            }
        }
        try {
            new Thread(consumer).start();
        } catch (NullPointerException e) {
            logger.info("NullPointerException on starting consumer thread");
            e.printStackTrace();
            System.exit(1);
        }
        logger.info("Started consumer process");
    }

    public static boolean isProducerAlive() {
        return Models2Pathways.PRODUCER.isAlive();
    }
}