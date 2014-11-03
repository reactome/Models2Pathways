package uk.ac.ebi.biomodels.tools.reactome;

import java.net.URI;

/**
 * @author Maximilian Koch <mkoch@ebi.ac.uk>
 */
public class URLBuilder {

    public static URI getIdentifiersURL() {
        return URI.create("http://reactomedev.oicr.on.ca/AnalysisService/identifiers/" +
                "?pageSize=20&page=1");
    }

    public static URI getTokenURL(String token, String resource) {
        return URI.create("http://reactomedev.oicr.on.ca/AnalysisService/token/"
                + token + "?pageSize=20&page=1&sortBy=ENTITIES_PVALUE&order=ASC&resource=" + resource);
    }
}
