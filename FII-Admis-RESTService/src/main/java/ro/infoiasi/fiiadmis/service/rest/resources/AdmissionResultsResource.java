package ro.infoiasi.fiiadmis.service.rest.resources;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.util.Series;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.service.rest.dao.DaoHolder;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class AdmissionResultsResource extends AbstractResource {

	private static final Logger LOG = Logger.getLogger(AdmissionResultsResource.class);

	@Get
	public JsonRepresentation getAdmissionResults() {
		LOG.debug("Retrieving the admission results from the DAO.");

		List<AdmissionResult> admissionResults = null;
		try {
			admissionResults = getAdmissionResultsFromDao();
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
					admissionResults = DaoHolder.startComputingAdmissionResults().getItems(null, null);
				} catch (IOException e) {
					handleInternalServerError(e);
				}
			} else {
				// If not admin, forbidden.
				handleClientError(Status.CLIENT_ERROR_FORBIDDEN);
				return null;
			}

		}

		// Create the response in json format.
		JsonRepresentation response = null;
		try {
			response = createJsonFrom("admissionResults", admissionResults);
			logResponse(response);
		} catch (JSONException | IOException e) {
			handleInternalServerError(e);
		}

        return response;
	}

	private List<AdmissionResult> getAdmissionResultsFromDao() throws IOException {

        LOG.debug("Get all admission results.");
        return DaoHolder.getAdmissionResultsDao().getItems(null, new Comparator<AdmissionResult>() {
            @Override
            public int compare(AdmissionResult o1, AdmissionResult o2) {
                return - Double.compare(o1.getFinalGrade(), o2.getFinalGrade());
            }
        });
	}

	@Delete
	public void deleteAdmissionResults() {
		if (!isAdmin()) {
			handleClientError(Status.CLIENT_ERROR_FORBIDDEN);
			return;
		}

		try {
			List<AdmissionResult> items = DaoHolder.getAdmissionResultsDao().getItems(null, null);
			for (AdmissionResult item : items) {
				DaoHolder.getAdmissionResultsDao().deleteItem(item.getId());
			}
		} catch (IOException e) {
			handleInternalServerError(e);
			return;
		}

		setStatus(Status.SUCCESS_NO_CONTENT);
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
        return admissionResults != null && !admissionResults.isEmpty();
    }

	@Override
	protected Logger getLOG() {
		return LOG;
	}

}
