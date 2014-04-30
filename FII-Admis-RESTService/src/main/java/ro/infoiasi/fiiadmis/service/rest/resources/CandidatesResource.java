package ro.infoiasi.fiiadmis.service.rest.resources;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
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

        // Get candidates from dao.
        List<Candidate> candidates = null;
        try {
            candidates = getCandidatesFromDao();
        } catch (IOException e) {
            handleInternalServerError(e);
        }

        // Create the response in json format.
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

    private List<Candidate> getCandidatesFromDao() throws IOException {
        Form queryParams = getRequest().getResourceRef().getQueryAsForm();
        if (queryParams == null) {
            LOG.debug("Get all candidates.");
            return DaoHolder.getCandidateDao().getItems(CandidateFilters.all());
        }

        String lastName = queryParams.getFirstValue("sort_by");
        if (lastName == null) {
            return DaoHolder.getCandidateDao().getItems(CandidateFilters.all());
        }

        if (lastName.equals("lastName")) {
            // TODO get sorted candidates from Dao
            LOG.debug("Get candidates sorted by lastname.");
            return DaoHolder.getCandidateDao().getItems(CandidateFilters.all());
        }

        LOG.debug("Get all candidates.");
        return DaoHolder.getCandidateDao().getItems(CandidateFilters.all());
    }

    @Post
    public void postCandidate(JsonRepresentation jsonCandidate) {
        LOG.debug("Posting the candidate to the DAO.");

        try {
            String candidateId = addCandidateFrom(jsonCandidate.getJsonObject());

            setStatus(Status.SUCCESS_CREATED);
            setLocationRef("candidates/" + candidateId);

            LOG.debug("RESPONSE: " + Status.SUCCESS_CREATED + " and location header set to " + getLocationRef());
        } catch (JSONException e) {
            handleClientError(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        } catch (IOException e) {
            handleInternalServerError(e);
            return;
        }
    }

    private String addCandidateFrom(JSONObject obj) throws JSONException, IOException {
        LOG.debug("Get information from the json and creating the candidate.");

        Candidate candidate = new Candidate();
        candidate.setFirstName(obj.getString("firstName"));
        candidate.setLastName(obj.getString("lastName"));
        candidate.setSocialId(obj.getString("socialId"));
        candidate.setGpaGrade(obj.getDouble("gpaGrade"));
        candidate.setATestGrade(obj.getDouble("ATestGrade"));

        LOG.debug("Adding the candidate in the CandidateDAO.");
        return DaoHolder.getCandidateDao().addItem(candidate);
    }

    @Override
    protected Logger getLOG() {
        return LOG;
    }
}
