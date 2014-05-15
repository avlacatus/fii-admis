package ro.infoiasi.fiiadmis.db.parser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import ro.infoiasi.fiiadmis.model.Candidate;

public class DefaultCandidateFormatter implements EntityFormatter<Candidate> {
    @Override
    public String getFieldSeparator() {
        return ":";
    }

    @Override
    public Candidate read(String record) {

        String[] fields = record.split(getFieldSeparator());
        Preconditions.checkArgument(fields.length == 6, "The candidate line must have 6 fields");
        Candidate candidate = new Candidate();
        candidate.setId(fields[0]);
        candidate.setFirstName(fields[1]);
        candidate.setLastName(fields[2]);
        candidate.setSocialId(fields[3]);
        candidate.setGpaGrade(Double.parseDouble(fields[4]));
        candidate.setATestGrade(Double.parseDouble(fields[5]));

        return candidate;
    }

    @Override
    public String write(Candidate entity) {
        return Joiner.on(getFieldSeparator())
                     .join(
                            entity.getId(),
                            entity.getFirstName(),
                            entity.getLastName(),
                            entity.getSocialId(),
                            entity.getGpaGrade(),
                            entity.getATestGrade()
                    );
    }
}
