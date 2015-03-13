package org.reactome.server.core.entrypoint;

import com.martiansoftware.jsap.JSAPResult;
import org.reactome.server.core.helper.Consumer;
import org.reactome.server.core.helper.Producer;
import org.reactome.server.core.model.sbml.SBMLModel;
import org.reactome.server.core.output.FileExporter;
import org.reactome.server.core.utils.JSAPHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;


/**
 * @author Maximilian Koch <mkoch@ebi.ac.uk>
 */
public class Models2Pathways {
    final static Logger logger = Logger.getLogger(Models2Pathways.class.getName());

    final static int BLOCKING_QUEUE_SIZE = 5;

    private static Thread PRODUCER;

    public static void main(String[] args) {
        logger.info("Process has been started");
        //Set up all given arguments.
        JSAPResult jsapResult = JSAPHandler.ArgumentHandler(args);
        FileExporter.setLocationPath(jsapResult.getString("output"));

        //Setting up output file
        if (FileExporter.createFile()) {
            FileExporter.addHeader();
        }

        //Shared blockingqueue for producert consumer.
        BlockingQueue<SBMLModel> sbmlModelBlockingQueue = new LinkedBlockingDeque<>(BLOCKING_QUEUE_SIZE);

        //Let's go... starting threads
        Producer producer = new Producer(sbmlModelBlockingQueue);

        Models2Pathways.PRODUCER = new Thread(producer);
        Models2Pathways.PRODUCER.start();
        logger.info("Started producer process");

        Consumer consumer = new Consumer(sbmlModelBlockingQueue, jsapResult.getString("significantFDR"), jsapResult.getString("extendedFDR"), jsapResult.getString("coverage"));
        new Thread(consumer).start();
        logger.info("Started consumer process");
    }

    public static boolean isProducerAlive() {
        return Models2Pathways.PRODUCER.isAlive();
    }
}