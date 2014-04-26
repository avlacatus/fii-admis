package ro.infoiasi.fiiadmis.dao.parser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import ro.infoiasi.fiiadmis.model.Candidate;

public class DefaultCandidateIO implements CandidateIO {
    @Override
    public String getFieldSeparator() {
        return ":";
    }

    @Override
    public Candidate read(String textLine) {

        String[] fields = textLine.split(getFieldSeparator());
        Preconditions.checkArgument(fields != null && fields.length == 6, "The candidate line must have 6 fields");
        Candidate candidate = new Candidate();
        candidate.setCandidateId(fields[0]);
        candidate.setFirstName(fields[1]);
        candidate.setLastName(fields[2]);
        candidate.setSocialId(fields[3]);
        candidate.setGpaGrade(Double.parseDouble(fields[4]));
        candidate.setATestGrade(Double.parseDouble(fields[5]));

        return candidate;
    }

    @Override
    public String write(Candidate candidate) {
        return "\n" + Joiner.on(getFieldSeparator())
                     .join(

                            candidate.getCandidateId(),
                            candidate.getFirstName(),
                            candidate.getLastName(),
                            candidate.getSocialId(),
                            candidate.getGpaGrade(),
                            candidate.getATestGrade()
                    );
    }
}
