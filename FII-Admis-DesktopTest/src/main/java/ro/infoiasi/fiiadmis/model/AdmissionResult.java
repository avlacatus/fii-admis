package ro.infoiasi.fiiadmis.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cosmin on 26/04/14.
 */
public class AdmissionResult implements Entity {


    public static enum Status {
        TAX_FREE(0, "tax_free"),
        TAX(1, "tax"),
        REJECTED(2, "rejected");

        private final int statusInt;
        private final String statusString;

        private static final Map<Integer, Status> fStatuses;

        static {
            fStatuses = new HashMap<>();
            for (Status s : Status.values()) {
                fStatuses.put(Integer.valueOf(s.getStatusInt()), s);
            }
        }

        Status(int statusInt, String statusString) {
            this.statusInt = statusInt;
            this.statusString = statusString;
        }

        public static Status getStatus(int intValue) {
            return fStatuses.get(Integer.valueOf(intValue));
        }

        public int getStatusInt() {
            return statusInt;
        }

        public String getStatusString() {
            return statusString;
        }
    }
    private String resultId;
    private String candidateId;
    private double finalGrade;
    private Status admissionStatus;

    public AdmissionResult() {
    }

    public AdmissionResult(String resultId, String candidateId, double finalGrade, Status admissionStatus) {
        this.resultId = resultId;
        this.candidateId = candidateId;
        this.finalGrade = finalGrade;
        this.admissionStatus = admissionStatus;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public Status getAdmissionStatus() {
        return admissionStatus;
    }

    public void setAdmissionStatus(Status admissionStatus) {
        this.admissionStatus = admissionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdmissionResult that = (AdmissionResult) o;

        if (Double.compare(that.finalGrade, finalGrade) != 0) return false;
        if (admissionStatus != that.admissionStatus) return false;
        if (candidateId != null ? !candidateId.equals(that.candidateId) : that.candidateId != null) return false;
        if (resultId != null ? !resultId.equals(that.resultId) : that.resultId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = resultId != null ? resultId.hashCode() : 0;
        result = 31 * result + (candidateId != null ? candidateId.hashCode() : 0);
        temp = Double.doubleToLongBits(finalGrade);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (admissionStatus != null ? admissionStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdmissionResult{" +
                "resultId='" + resultId + '\'' +
                ", candidateId='" + candidateId + '\'' +
                ", finalGrade=" + finalGrade +
                ", admissionStatus=" + admissionStatus +
                '}';
    }
}
