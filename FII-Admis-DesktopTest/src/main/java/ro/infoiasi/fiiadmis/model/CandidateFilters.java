package ro.infoiasi.fiiadmis.model;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.math.DoubleMath;

public class CandidateFilters {

    private CandidateFilters() {}

    public static Predicate<Candidate> all() {
        return Predicates.alwaysTrue();
    }

    public static Predicate<Candidate> none() {
        return Predicates.alwaysFalse();
    }

    public static Predicate<Candidate> byId(final String id) {
        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return id.equals(candidate.getId());
            }
        };
    }
    public static Predicate<Candidate> bySocialId(final String socialId) {
        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return socialId.equals(candidate.getSocialId());
            }
        };
    }
    public static Predicate<Candidate> byFirstName(final String name) {
        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return candidate.getFirstName().contains(name);
            }
        };
    }


    public static Predicate<Candidate> byLastName(final String name) {
        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return candidate.getLastName().contains(name);
            }
        };
    }

    public static Predicate<Candidate> byGpaGrade(final double grade, final double approximation) {
        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return DoubleMath.fuzzyEquals(candidate.getGpaGrade(), grade, approximation);
            }
        };
    }

    public static Predicate<Candidate> byATestGrade(final double grade, final double approximation) {
        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return DoubleMath.fuzzyEquals(candidate.getATestGrade(), grade, approximation);
            }
        };
    }




}
