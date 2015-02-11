package uk.ac.ebi.models2pathways.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Maximilian Koch <mkoch@ebi.ac.uk>
 */
public enum Species {
    HOMO_SAPIENS("9606", "48887"),
    ARABIDOPSIS_THALIANA("3702", "170905"),
    BOS_TAURUS("9913", "48898"),
    DICTYOSTELIUM_DISCOIDEUM("44689", "170941"),
    DROSOPHILA_MELANOGASTE("7227", "56210"),
    ESCHERICHIA_COLI("562", "159879"),
    MUS_MUSCULUS("10090", "48892"),
    MYCOBACTERIUM_TUBERCULOSIS("1773", "176806"),
    RATTUS_NORVEGICUS("10116", "48895"),
    SACCHAROMYCES_CEREVISIAE("4932", "68322"),
    SCHIZOSACCHAROMYCES_POMBE("4896", "68324");

    private final String bioModelsTaxonomyid;
    private final String reactomeTaxonomyId;

    private Species(String bioModelsTaxonomyid, String reactomeTaxonomyId) {
        this.bioModelsTaxonomyid = bioModelsTaxonomyid;
        this.reactomeTaxonomyId = reactomeTaxonomyId;
    }

    public static Set<String> getAllBioModelsTaxonomyIds() {
        Set<String> bioModelsTaxonomyIds = new HashSet<String>();
        for (Species species : Species.values()) {
            bioModelsTaxonomyIds.add(species.getBioModelsTaxonomyId());
        }
        return bioModelsTaxonomyIds;
    }

    public static Species getSpeciesByBioModelsTaxonomyid(String bioModelsTaxonomyid) {
        for (Species species : Species.values()) {
            if (species.bioModelsTaxonomyid.equals(bioModelsTaxonomyid)) {
                return species;
            }
        }
        return null;
    }

    public String getBioModelsTaxonomyId() {
        return bioModelsTaxonomyid;
    }

    @Override
    public String toString() {
        return "Species{" +
                "bioModelsTaxonomyid='" + bioModelsTaxonomyid + '\'' +
                ", reactomeTaxonomyId='" + reactomeTaxonomyId + '\'' +
                '}';
    }
}
