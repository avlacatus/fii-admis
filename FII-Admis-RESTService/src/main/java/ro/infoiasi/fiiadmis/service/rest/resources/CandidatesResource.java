package ro.infoiasi.fiiadmis.service.rest.resources;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

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

    @Post
    public void postCandidate(JsonRepresentation jsonCandidate) {
        LOG.debug("Posting the candidate to the DAO.");

        JSONObject obj;
        try {
            obj = jsonCandidate.getJsonObject();
            String candidateId = addCandidateFrom(obj);

            setStatus(Status.SUCCESS_CREATED);
            setLocationRef("api/candidates/" + candidateId);

            LOG.debug("RESPONSE: " + Status.SUCCESS_CREATED + " and location header set to " + getLocationRef());
        } catch (JSONException e) {
            handleClientError(e, Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        } catch (IOException e) {
            handleInternalServerError(e);
            return;
        }
    }

    private String addCandidateFrom(JSONObject obj) throws JSONException, IOException {
        LOG.debug("Get information from the json and creating the candidate.");

        Candidate candidate = new Candidate();
        candidate.setFirstName(obj.getString("first_name"));
        candidate.setLastName(obj.getString("last_name"));
        candidate.setSocialId(obj.getString("social_id"));
        candidate.setGpaGrade(obj.getDouble("gpa_grade"));
        candidate.setATestGrade(obj.getDouble("a_test_grade"));

        LOG.debug("Adding the candidate in the CandidateDAO.");
        return DaoHolder.getCandidateDao().addItem(candidate);
    }

    @Override
    protected Logger getLOG() {
        return LOG;
    }
}
