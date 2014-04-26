package ro.infoiasi.fiiadmis.service.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import ro.infoiasi.fiiadmis.service.rest.resources.CandidatesResource;

public class Main extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new respective
        // instance of resource.
        Router router = new Router(getContext());

        router.attach("/candidates", CandidatesResource.class);

        return router;
    }
}
