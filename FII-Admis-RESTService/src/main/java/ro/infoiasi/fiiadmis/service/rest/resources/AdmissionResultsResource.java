package ro.infoiasi.fiiadmis.service.rest.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.engine.header.HeaderUtils;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.util.Series;

import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.AdmissionResultFilters;
import ro.infoiasi.fiiadmis.service.rest.dao.DaoHolder;

public class AdmissionResultsResource extends AbstractResource {

    private static final Logger LOG = Logger.getLogger(AdmissionResultsResource.class);

    @Get
    public JsonRepresentation getAdmissionResults() {
        LOG.debug("Retrieving the admission results from the DAO.");

        List<AdmissionResult> admissionResults = null;
        try {
            admissionResults = DaoHolder.getAdmissionResultsDao().getItems(AdmissionResultFilters.all());
        } catch (IOException e) {
            handleInternalServerError(e);
        }

        // Check if the admission results are available.
        if (!isAvailable(admissionResults)) {
            // Check the requester's identity (student or admin).
            if (isAdmin()) {
                // If admin, the admission results will be generated.
                try {
                    LOG.info("Start computing the admission results");
                    admissionResults = DaoHolder.startComputingAdmissionResults()
                            .getItems(AdmissionResultFilters.all());
                } catch (IOException e) {
                    handleInternalServerError(e);
                }
            } else {
                // If not admin, forbidden.
                LOG.debug("RESPONSE: " + Status.CLIENT_ERROR_FORBIDDEN);
                setStatus(Status.CLIENT_ERROR_FORBIDDEN);
                return null;
            }

        }

        // Create the response in json format.
        JsonRepresentation response = null;
        try {
            response = createJsonFrom("admissionResults", admissionResults);
            logResponse(response);
        } catch (JSONException e) {
            handleInternalServerError(e);
        } catch (IOException e) {
            handleInternalServerError(e);
        }

        return response;
    }

    private boolean isAdmin() {
        LOG.debug("Checking for the requester's identity");

        Series<Parameter> headers = (Series<Parameter>) getRequest().getAttributes().get("org.restlet.http.headers");
        Set<String> headerNames = headers.getNames();

        for (String h : headerNames) {
            LOG.debug("Header: " + h);
        }

        if (!headerNames.contains("Pragma") && !headerNames.contains("pragma")) {
            LOG.debug("No header Pragma found");
            return false;
        }

        LOG.debug("Header Pragma found");

        String values = headers.getValues("Pragma", "\n", true);
        if (values == null) {
            values = headers.getValues("pragma", "\n", true);
        }

        for (String headerValue : values.split("\n")) {
            if (headerValue.equals("admin")) {
                LOG.debug("Value ***** found.");
                return true;
            }
        }

        LOG.debug("No correct value found in the header values");

        return false;
    }

    private boolean isAvailable(List<AdmissionResult> admissionResults) {
        if (admissionResults == null) {
            return false;
        }
        if (admissionResults.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    protected Logger getLOG() {
        return LOG;
    }

}
