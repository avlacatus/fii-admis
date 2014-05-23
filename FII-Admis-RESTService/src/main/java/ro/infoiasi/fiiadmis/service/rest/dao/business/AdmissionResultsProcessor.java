package ro.infoiasi.fiiadmis.service.rest.dao.business;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;

import ro.infoiasi.fiiadmis.db.dao.EntityDAO;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.AdmissionResult.Status;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class AdmissionResultsProcessor {

	private static final Logger LOG = Logger.getLogger(AdmissionResultsProcessor.class);

	private static AdmissionResultsProcessor sInstance;

	private AdmissionResultsProcessor() {

	}

	public static AdmissionResultsProcessor get() {
		if (sInstance == null) {
			sInstance = new AdmissionResultsProcessor();
		}
		return sInstance;
	}

	public void processResults(EntityDAO<Candidate> candidatesDAO, EntityDAO<AdmissionResult> resultsDAO,
                               Comparator<AdmissionResult> comparator) {
	        Preconditions.checkArgument(candidatesDAO != null, "Can't process null DAO.");
	        Preconditions.checkArgument(resultsDAO != null, "Can't process null DAO.");
	        Preconditions.checkArgument(comparator != null, "Can't use null comparator.");
		try {
			List<Candidate> candidates = candidatesDAO.getItems(null, null);
			List<AdmissionResult> results = new ArrayList<>();
			for (Candidate c : candidates) {
				AdmissionResult result = new AdmissionResult();
				result.setCandidateId(c.getId());
				result.setFinalGrade(processFinalGrade(c.getGpaGrade(), c.getATestGrade()));
				result.setAdmissionStatus(getAdmissionStatus(c.getGpaGrade(), c.getATestGrade(), result.getFinalGrade()));
				results.add(result);
			}

			Collections.sort(results, comparator);

			for (AdmissionResult result : results) {
				String resultId = resultsDAO.addItem(result);
				LOG.debug("Added new admission result: " + resultId + " " + result.getFinalGrade());
			}
		} catch (Exception e) {
			throw new IllegalStateException("Can not process admission results. Can not access candidates information",
					e);
		}

	}

	private double processFinalGrade(double gpaGrade, double aTestGrade) {
        double initial = (gpaGrade + aTestGrade) / 2;
        // display final grade with 3 decimals precision
        int temp = (int) (initial * 1000);

        return (double)temp / 1000;
	}

	private Status getAdmissionStatus(double gpaGrade, double aTestGrade, double finalGrade) {
		if (finalGrade >= 9.00) {
			return Status.TAX_FREE;
		} else if (finalGrade >= 7.00) {
			return Status.TAX;
		} else {
			return Status.REJECTED;
		}
	}

}
