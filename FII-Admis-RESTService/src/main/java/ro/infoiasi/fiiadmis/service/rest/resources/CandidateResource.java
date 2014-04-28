package ro.infoiasi.fiiadmis.service.rest.resources;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.model.CandidateFilters;
import ro.infoiasi.fiiadmis.service.rest.dao.DaoHolder;

public class CandidateResource extends AbstractResource {

    private static final Logger LOG = Logger.getLogger(CandidateResource.class);

    @Get
    public JsonRepresentation getCandidate() {
        String candidateId = (String) getRequestAttributes().get("candidate_id");
        LOG.debug("Retrieving candidate " + candidateId + " from the DAO.");

        List<Candidate> candidates = null;
        try {
            candidates = DaoHolder.getCandidateDao().getItems(CandidateFilters.byId(candidateId));
        } catch (IOException e) {
            handleInternalServerError(e);
        }

        if (candidates.size() != 1) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        }

        // Create the response in json format.
        JsonRepresentation response = null;
        try {
            response = createJsonFrom(candidates.get(0));
            logResponse(response);
        } catch (IOException e) {
            handleInternalServerError(e);
        }

        return response;
    }

    @Override
    protected Logger getLOG() {
        return LOG;
    }

}
