package ro.infoiasi.fiiadmis.service.rest.resources;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.model.CandidateFilters;
import ro.infoiasi.fiiadmis.service.rest.dao.DaoHolder;

public class CandidatesResource extends AbstractResource {

    private static final Logger LOG = Logger.getLogger(CandidatesResource.class);

    @Get
    public JsonRepresentation getCandidates() {
        LOG.debug("Retrieving the candidates from the DAO.");

        List<Candidate> candidates = null;
        try {
            candidates = DaoHolder.getCandidateDao().getItems(CandidateFilters.all());
        } catch (IOException e) {
            handleInternalServerError(e);
        }

        // Create the resonse in json format.
        JsonRepresentation response = null;
        try {
            response = createJsonFrom("candidates", candidates);
            logResponse(response);
        } catch (JSONException e) {
            handleInternalServerError(e);
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
