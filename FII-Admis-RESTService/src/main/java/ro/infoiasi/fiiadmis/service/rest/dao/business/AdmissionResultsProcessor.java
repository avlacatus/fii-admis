package ro.infoiasi.fiiadmis.service.rest.dao.business;

import java.util.List;

import org.apache.log4j.Logger;

import ro.infoiasi.fiiadmis.db.dao.EntityDAO;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.AdmissionResult.Status;
import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.model.CandidateFilters;

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

	public void processResults(EntityDAO<Candidate> candidatesDAO, EntityDAO<AdmissionResult> resultsDAO) {
		try {
			List<Candidate> candidates = candidatesDAO.getItems(CandidateFilters.all());

			for (Candidate c : candidates) {
				AdmissionResult result = new AdmissionResult();
				result.setCandidateId(c.getId());
				result.setFinalGrade(processFinalGrade(c.getGpaGrade(), c.getATestGrade()));
				result.setAdmissionStatus(getAdmissionStatus(c.getGpaGrade(), c.getATestGrade(), result.getFinalGrade()));
				String resultId = resultsDAO.addItem(result);
				LOG.debug("Added new admission result: " + resultId);
			}
		} catch (Exception e) {
			throw new IllegalStateException("Can not process admission results. Can not access candidates information",
					e);
		}

	}

	private double processFinalGrade(double gpaGrade, double aTestGrade) {
		return (gpaGrade + aTestGrade) / 2;
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