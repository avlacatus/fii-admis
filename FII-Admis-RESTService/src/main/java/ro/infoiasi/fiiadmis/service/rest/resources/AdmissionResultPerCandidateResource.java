package ro.infoiasi.fiiadmis.service.rest.resources;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.AdmissionResultFilters;
import ro.infoiasi.fiiadmis.service.rest.dao.DaoHolder;

public class AdmissionResultPerCandidateResource extends AbstractResource {

    private static final Logger LOG = Logger.getLogger(AdmissionResultPerCandidateResource.class);

    @Get
    public JsonRepresentation getCandidate() {
        String candidateId = (String) getRequestAttributes().get("candidate_id");
        LOG.debug("Retrieving admission result for candidate " + candidateId + " from the DAO.");

        List<AdmissionResult> admissionResultPerCandidate = null;
        try {
            admissionResultPerCandidate = DaoHolder.getAdmissionResultsDao().getItems(
                    AdmissionResultFilters.byCandidateId(candidateId));
        } catch (IOException e) {
            handleInternalServerError(e);
        }

        if (admissionResultPerCandidate.size() != 1) {
            return handleClientError(null, Status.CLIENT_ERROR_NOT_FOUND);
        }

        // Create the response in json format.
        JsonRepresentation response = null;
        try {
            response = createJsonFrom(admissionResultPerCandidate.get(0));
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
