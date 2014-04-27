package ro.infoiasi.fiiadmis.service.rest.resources;

import java.util.List;

import org.apache.log4j.Logger;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import ro.infoiasi.fiiadmis.db.dao.CandidatesDaoImpl;
import ro.infoiasi.fiiadmis.model.Candidate;

public class CandidatesResource extends ServerResource {

    private static final Logger LOG = Logger.getLogger(CandidatesResource.class);

    @Get
    public JsonRepresentation getCandidates() {
        LOG.debug("Retrieving the candidates from the DAO.");

        // Retrieving data from the DAO.
        List<Candidate> candidates = new CandidatesDaoImpl("db").getAllCandidates(null);

        // TODO mirelap translate List<Candidate> to json
        String responseText = "{\"candidates\": {}}";

        // Response.
        return new JsonRepresentation(responseText);
    }
}
