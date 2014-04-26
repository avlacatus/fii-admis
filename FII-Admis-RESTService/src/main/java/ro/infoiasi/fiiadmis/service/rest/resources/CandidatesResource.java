package ro.infoiasi.fiiadmis.service.rest.resources;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class CandidatesResource extends ServerResource {

    @Get
    public JsonRepresentation getCandidates() {
        return new JsonRepresentation("{\"candidates\": {}}");
    }
}
