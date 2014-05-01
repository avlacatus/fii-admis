package ro.infoiasi.fiiadmis.service.rest.resources;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

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
            candidates = DaoHolder.getCandidateDao().getItems(CandidateFilters.byId(candidateId), null);
        } catch (IOException e) {
            handleInternalServerError(e);
        }

        if (candidates.size() != 1) {
            handleClientError(Status.CLIENT_ERROR_NOT_FOUND);
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

    @Put
    public void updateCandidate(JsonRepresentation jsonCandidate) {
        String candidateId = (String) getRequestAttributes().get("candidate_id");
        LOG.debug("Updating candidate " + candidateId + " from the DAO.");

        try {
            updateCandidate(candidateId, jsonCandidate.getJsonObject());

            setStatus(Status.SUCCESS_NO_CONTENT);

            LOG.debug("RESPONSE: " + Status.SUCCESS_NO_CONTENT);
        } catch (JSONException e) {
            handleClientError(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        } catch (IOException e) {
            handleInternalServerError(e);
            return;
        }
    }

    @Delete
    public void deleteCandidate() {
        String candidateId = (String) getRequestAttributes().get("candidate_id");
        LOG.debug("Deleting candidate " + candidateId + " from the DAO.");

        try {
            DaoHolder.getCandidateDao().deleteItem(candidateId);
            setStatus(Status.SUCCESS_NO_CONTENT);
        } catch (IOException e) {
            handleInternalServerError(e);
        }
    }

    private void updateCandidate(String candidateId, JSONObject obj) throws IOException, JSONException {
        LOG.debug("Get information from the json and updating the candidate.");

        Candidate candidate = new Candidate();
        candidate.setId(candidateId);
        candidate.setFirstName(obj.getString("firstName"));
        candidate.setLastName(obj.getString("lastName"));
        candidate.setSocialId(obj.getString("socialId"));
        candidate.setGpaGrade(obj.getDouble("gpaGrade"));
        candidate.setATestGrade(obj.getDouble("ATestGrade"));

        LOG.debug("Update the candidate in the DAO.");
        DaoHolder.getCandidateDao().updateItem(candidate);
    }

    @Override
    protected Logger getLOG() {
        return LOG;
    }

}
