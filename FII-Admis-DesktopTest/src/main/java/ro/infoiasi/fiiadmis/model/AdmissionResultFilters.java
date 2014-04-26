package ro.infoiasi.fiiadmis.model;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.math.DoubleMath;

public class AdmissionResultFilters {

    private AdmissionResultFilters() {}

    public static Predicate<AdmissionResult> all() {

        return Predicates.alwaysTrue();
    }

    public static Predicate<AdmissionResult> none() {

        return Predicates.alwaysFalse();
    }

    public static Predicate<AdmissionResult> byCandidateId(final String candidateId) {

        return new Predicate<AdmissionResult>() {
            @Override
            public boolean apply(AdmissionResult result) {
                return candidateId.equals(result.getCandidateId());
            }
        };
    }

    public static Predicate<AdmissionResult> byFinalGrade(final double grade, final double approximation) {

        return new Predicate<AdmissionResult>() {
            @Override
            public boolean apply(AdmissionResult result) {
                return DoubleMath.fuzzyEquals(result.getFinalGrade(), grade, approximation);
            }
        };
    }

    public static Predicate<AdmissionResult> byStatus(final AdmissionResult.Status status) {

        return new Predicate<AdmissionResult>() {
            @Override
            public boolean apply(AdmissionResult result) {
                return status == result.getAdmissionStatus();
            }
        };
    }




}
