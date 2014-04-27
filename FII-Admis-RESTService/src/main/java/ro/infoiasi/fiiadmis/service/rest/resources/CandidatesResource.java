package ro.infoiasi.fiiadmis.service.rest.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import ro.infoiasi.fiiadmis.model.Candidate;

public class CandidatesResource extends AbstractResource {

    private static final Logger LOG = Logger.getLogger(CandidatesResource.class);

    @Get
    public JsonRepresentation getCandidates() {
        LOG.debug("Retrieving the candidates from the DAO.");

        // TODO mirelap use the DAO

        // Sample code.
        List<Candidate> candidates = new ArrayList<Candidate>();
        // Response.
        candidates.add(new Candidate(1, "Mirela", "Popoveniuc", "121", 12.3, 12.56));
        candidates.add(new Candidate(2, "Georgiana", "Popoveniuc", "121", 12.3, 12.56));

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
