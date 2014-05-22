package ro.infoiasi.fiiadmis.service.rest;

import org.apache.log4j.Logger;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import ro.infoiasi.fiiadmis.service.rest.dao.DaoHolder;
import ro.infoiasi.fiiadmis.service.rest.resources.AdmissionResultPerCandidateResource;
import ro.infoiasi.fiiadmis.service.rest.resources.AdmissionResultsResource;
import ro.infoiasi.fiiadmis.service.rest.resources.CandidateResource;
import ro.infoiasi.fiiadmis.service.rest.resources.CandidatesResource;

import com.google.common.base.Preconditions;

public class MainApplication extends Application {

    private static final Logger LOG = Logger.getLogger(MainApplication.class);

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        LOG.debug("Routing each call to a new respective instance of resource");

        initializeDb();

        Router router = new Router(getContext());

        attach(router, "/candidates", CandidatesResource.class);
        attach(router, "/candidates/{candidate_id}", CandidateResource.class);

        attach(router, "/admission_results", AdmissionResultsResource.class);
        attach(router, "/admission_results/{candidate_id}", AdmissionResultPerCandidateResource.class);

        return router;
    }

    private void initializeDb() {
        DaoHolder.initializeDb();
    }

    private static void attach(Router router, String uriPath, Class<? extends ServerResource> resource) {
        Preconditions.checkNotNull(router);
        Preconditions.checkNotNull(uriPath);
        Preconditions.checkNotNull(resource);
        LOG.debug("Attaching " + uriPath + " to resource " + resource.getSimpleName());
        router.attach(uriPath, resource);
    }
}
