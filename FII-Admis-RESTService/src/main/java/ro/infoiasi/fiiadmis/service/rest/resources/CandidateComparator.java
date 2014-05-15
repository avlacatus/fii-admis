package ro.infoiasi.fiiadmis.service.rest.resources;

import java.util.Comparator;

import ro.infoiasi.fiiadmis.model.Candidate;

public class CandidateComparator {

    private static Comparator<Candidate> BY_LAST_NAME =
            new Comparator<Candidate>() {
                @Override
                public int compare(Candidate o1, Candidate o2) {
                    if (o1 != null && o2 != null) {
                        if (o1.getLastName() != null) {
                            return o1.getLastName().compareTo(o2.getLastName());
                        } else
                            return 1;

                    }
                    return 0;
                }
            };

    public static Comparator<Candidate> byLastName() {
        return BY_LAST_NAME;
    }
}
