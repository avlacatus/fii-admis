package ro.infoiasi.fiiadmis.service.rest.resources;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ServerResource;

import com.google.common.base.Preconditions;

public abstract class AbstractResource extends ServerResource {

    protected abstract Logger getLOG();

    protected JsonRepresentation createJsonFrom(Object object) {
        Preconditions.checkNotNull(object);

        return new JsonRepresentation(object);
    }

    protected JsonRepresentation createJsonFrom(String name, Collection<?> collection) throws JSONException {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(collection);

        // Create the json array from the collection.
        JSONArray array = new JSONArray();
        for (Object obj : collection) {
            array.put(new JSONObject(obj));
        }

        // Create json object with the json array and the name.
        JSONObject obj = new JSONObject();
        obj.put(name, array);

        // Returning the json represenation;
        return new JsonRepresentation(obj);

    }

    protected void handleClientError(Status status) {
        Preconditions.checkNotNull(status);

        setStatus(status);
        getLOG().debug("RESPONSE - Client Error: " + status);
    }

    protected JsonRepresentation handleInternalServerError(Exception e) {
        Preconditions.checkNotNull(e);

        setStatus(Status.SERVER_ERROR_INTERNAL);
        JsonRepresentation internalServerErrorResponse = new JsonRepresentation(e);
        logInternalServerError(internalServerErrorResponse);
        return internalServerErrorResponse;
    }

    protected void logInternalServerError(JsonRepresentation internalServerErrorResponse) {
        Preconditions.checkNotNull(internalServerErrorResponse);

        getLOG().error("RESPONSE - Internal Server Error: " + internalServerErrorResponse.toString());
    }

    protected void logResponse(JsonRepresentation json) throws IOException {
        Preconditions.checkNotNull(json);

        getLOG().debug("RESPONSE: " + json.getText());
    }
}
