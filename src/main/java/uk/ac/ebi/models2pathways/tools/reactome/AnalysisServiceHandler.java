package uk.ac.ebi.models2pathways.tools.reactome;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import uk.ac.ebi.models2pathways.resources.mapping.reactomeanalysisservice.AnalysisResult;
import uk.ac.ebi.models2pathways.resources.mapping.reactomeanalysisservice.PathwaySummary;
import uk.ac.ebi.models2pathways.resources.mapping.reactomeanalysisservice.ResourceSummary;
import uk.ac.ebi.models2pathways.tools.sbml.SBMLModel;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Maximilian Koch <mkoch@ebi.ac.uk>
 */
public class AnalysisServiceHandler {

    public static AnalysisResult getReactomeAnalysisResultBySBMLModel(SBMLModel sbmlModel) {
        HttpResponse httpResponse = AnalysisServiceRequest.requestByModel(sbmlModel);
        String jsonResult = null;
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            try {
                jsonResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            AnalysisResult analysisResult = AnalysisResultConverter.getAnalysisResultObject(jsonResult);
            if (analysisResult.getPathwaysFound() != 0) {
                if (tokenRequestRequired(analysisResult.getResourceSummary())) {
                    String token = analysisResult.getSummary().getToken();
                    String resourceSummary = getResource(analysisResult.getResourceSummary());
                    httpResponse = AnalysisServiceRequest.requestByToken(token, resourceSummary);
                    try {
                        HttpEntity entity = httpResponse.getEntity();
                        jsonResult = EntityUtils.toString(entity, "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    analysisResult = AnalysisResultConverter.getAnalysisResultObject(jsonResult);
                }
            }
            if (onlyNecessaryPathways(analysisResult.getPathways())) {
                return analysisResult;
            }
        }
        return null;
    }

    private static boolean onlyNecessaryPathways(List<PathwaySummary> pathways) {
        Set<PathwaySummary> unnecessaryPathways = new HashSet<PathwaySummary>();
        for (PathwaySummary pathway : pathways) {
            if (!pathway.isLlp()) {
                unnecessaryPathways.add(pathway);
            }
            if (pathway.getEntities().getFdr() >= 0.005) {
                unnecessaryPathways.add(pathway);
            }
        }
        pathways.removeAll(unnecessaryPathways);
        return true;
    }

    private static boolean tokenRequestRequired(List<ResourceSummary> resourceSummary) {
        return resourceSummary.size() < 3;
    }

    private static String getResource(List<ResourceSummary> resourceSummary) {
        return resourceSummary.get(1).getResource();
    }
}
