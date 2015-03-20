package org.reactome.server.core.models2pathways.biomodels.model;

import org.reactome.server.core.models2pathways.core.model.Specie;

import java.util.Set;

/**
 * @author Maximilian Koch <mkoch@ebi.ac.uk>
 */
public class SBMLModel {
    private String name;
    private Long bioMdId;
    private Specie specie;
    private Set<Annotation> annotations;

    /**
     * SBMLModel represents given information from the given
     * SBMLModel/XML and the BioModel-Webservice
     *
     * @param name
     * @param bioMdId
     * @param specie
     * @param annotations
     */
    public SBMLModel(String name, Long bioMdId, Specie specie, Set<Annotation> annotations) {
        this.name = name;
        this.bioMdId = bioMdId;
        this.specie = specie;
        this.annotations = annotations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBioMdId() {
        return bioMdId;
    }

    public void setBioMdId(Long bioMdId) {
        this.bioMdId = bioMdId;
    }

    public Specie getSpecie() {
        return specie;
    }

    public void setSpecie(Specie specie) {
        this.specie = specie;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }

    @Override
    public String toString() {
        return "SBMLModel{" +
                "name='" + name + '\'' +
                ", bioMdId='" + bioMdId + '\'' +
                ", bioModelsTaxonomyId='" + specie + '\'' +
                ", annotations=" + annotations +
                '}';
    }
}
