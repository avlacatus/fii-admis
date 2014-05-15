package ro.infoiasi.fiiadmis.db.parser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import ro.infoiasi.fiiadmis.model.AdmissionResult;

public class DefaultAdmissionResultsFormatter implements EntityFormatter<AdmissionResult> {
    @Override
    public String getFieldSeparator() {
        return ":";
    }

    @Override
    public AdmissionResult read(String record) {
        String[] fields = record.split(getFieldSeparator());
        Preconditions.checkArgument(fields.length == 4, "The admission result line must have 4 fields");
        AdmissionResult candidate = new AdmissionResult();
        candidate.setId(fields[0]);
        candidate.setCandidateId(fields[1]);
        candidate.setFinalGrade(Double.parseDouble(fields[2]));
        candidate.setAdmissionStatus(AdmissionResult.Status.getStatus(Integer.parseInt(fields[3])));
        return candidate;
    }

    @Override
    public String write(AdmissionResult entity) {
        return Joiner.on(getFieldSeparator())
                     .join(
                            entity.getId(),
                            entity.getCandidateId(),
                            entity.getFinalGrade(),
                            entity.getAdmissionStatus().getStatusInt()
                    );
    }
}
