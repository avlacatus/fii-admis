package ro.infoiasi.fiiadmis.dao.parser;

import ro.infoiasi.fiiadmis.model.Candidate;

public interface CandidateIO {

    String getFieldSeparator();
    Candidate read(String textLine);
    String write(Candidate candidate);
}
