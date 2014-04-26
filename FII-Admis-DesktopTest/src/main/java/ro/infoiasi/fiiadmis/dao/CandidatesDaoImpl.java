package ro.infoiasi.fiiadmis.dao;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import ro.infoiasi.fiiadmis.dao.parser.CandidateIO;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CandidatesDaoImpl implements CandidatesDao {

    private static final Logger LOG = Logger.getLogger(CandidatesDaoImpl.class);

    private final Path dbFilePath;
    private final CandidateIO candidateIO;

    public CandidatesDaoImpl(String dbFilename, CandidateIO candidateIO) {

        this.dbFilePath = Paths.get(dbFilename);
        this.candidateIO = candidateIO;
    }

    @Override
    public Candidate getCandidateById(int id) throws IOException {

        Preconditions.checkArgument(id >= 0, "Candidate id must be positive");

        return singleCandidate(Filters.byId(id));

    }

    @Override
    public Candidate getCandidateBySocialNumber(String socialId) throws IOException {

        Preconditions.checkArgument(socialId != null, "Candidate social id must not be null");

        return singleCandidate(Filters.bySocialId(socialId));

    }

    @Override
    public List<Candidate> getAllCandidates(Predicate<Candidate> filter) throws IOException {
        Preconditions.checkArgument(filter != null, "Filter must not be null");

        return multipleCandidates(filter);
    }

    @Override
    public int addCandidate(Candidate c) {
        return 0;
    }

    @Override
    public void updateCandidate(Candidate c) {

    }

    @Override
    public void deleteCandidate(int candidateId) {

    }

    private Candidate singleCandidate(Predicate<Candidate> filter) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(dbFilePath, Charset.defaultCharset())){
            String line = null;
            while ((line = reader.readLine()) != null) {

                Candidate currentCandidate = candidateIO.read(line);
                if (filter.apply(currentCandidate)) {
                    return currentCandidate;
                }
            }

            return null; // no candidate with this id was found
        }
    }

    private List<Candidate> multipleCandidates(Predicate<Candidate> filter) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(dbFilePath, Charset.defaultCharset())){
            String line = null;
            final List<Candidate> results = Lists.newArrayList();
            while ((line = reader.readLine()) != null) {

                Candidate currentCandidate = candidateIO.read(line);
                if (filter.apply(currentCandidate)) {
                    results.add(currentCandidate);
                }
            }

            return results;
        }
    }
}
