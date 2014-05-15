package ro.infoiasi.fiiadmis.service.rest.resources;

import java.util.Comparator;

import ro.infoiasi.fiiadmis.model.AdmissionResult;

public class AdmissionResultComparator {

    private static final Comparator<AdmissionResult> BY_FINAL_GRADE = new Comparator<AdmissionResult>() {
        @Override
        public int compare(AdmissionResult o1, AdmissionResult o2) {
            return -Double.compare(o1.getFinalGrade(), o2.getFinalGrade());
        }
    };

    public static Comparator<AdmissionResult> byFinalGrade() {
        return BY_FINAL_GRADE;
    }

}
