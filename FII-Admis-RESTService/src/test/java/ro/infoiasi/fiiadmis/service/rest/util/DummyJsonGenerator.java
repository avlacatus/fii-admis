package ro.infoiasi.fiiadmis.service.rest.util;

import ro.infoiasi.fiiadmis.model.Candidate;

public class DummyJsonGenerator {

    public static String[] jsonPartsFromCandidate(Candidate candidate) {
        String id = "\"id\":" + "\"" + candidate.getId() + "\"";
        String lastName = "\"lastName\":" + "\"" + candidate.getLastName() +
                "\"";
        String firstName = "\"firstName\":" + "\"" + candidate.getFirstName()
                + "\"";
        String gpaGrade = "\"gpaGrade\":" + candidate.getGpaGrade();
        String aTestGrade = "\"ATestGrade\":" + candidate.getATestGrade();
        String socialId = "\"socialId\":" + "\"" + candidate.getSocialId() + "\"";

        return new String[] { id, lastName, firstName, gpaGrade, aTestGrade, socialId };
    }
}
