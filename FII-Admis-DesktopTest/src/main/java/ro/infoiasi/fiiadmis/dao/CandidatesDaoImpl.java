package ro.infoiasi.fiiadmis.dao;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import ro.infoiasi.fiiadmis.dao.parser.DefaultCandidateIO;
import ro.infoiasi.fiiadmis.dao.parser.EntityIO;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class CandidatesDaoImpl extends AbstractEntityDAO<Candidate> {

    private static final Logger LOG = Logger.getLogger(CandidatesDaoImpl.class);

    private final Path dbFilePath;
    private final DefaultCandidateIO candidateIO;

    public CandidatesDaoImpl(String dbFilename, DefaultCandidateIO candidateIO) {
        this.dbFilePath = Paths.get(dbFilename);
        this.candidateIO = candidateIO;
    }

    @Override
    public Candidate getItemById(String entityId) throws IOException {
        Preconditions.checkArgument(entityId != null, "Candidate id must not be null");
        return getSingleItem(Filters.byId(entityId));
    }

    @Override
    public List<Candidate> getItems(Predicate<Candidate> filter) throws IOException {
        Preconditions.checkArgument(filter != null, "Filter must not be null");
        return getMultipleItems(filter);
    }

    @Override
    public String addItem(Candidate item) throws IOException {
        String newId = RandomStringUtils.randomAlphanumeric(4);
        item.setCandidateId(newId);
        try (BufferedWriter writer = Files.newBufferedWriter(dbFilePath, Charset.defaultCharset(), StandardOpenOption.APPEND)) {
            writer.write(candidateIO.write(item));
            writer.write(System.lineSeparator());
        }
        return newId;
    }

    @Override
    public void updateItem(Candidate item) throws IOException {
        Preconditions.checkArgument(item != null, "The input candidate must not be null");
        Path tmpPath = Paths.get(dbFilePath.toString() + ".tmp");
        Scanner s = new Scanner(dbFilePath);
        try (BufferedWriter writer = Files.newBufferedWriter(tmpPath, Charset.defaultCharset())) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.isEmpty())
                    continue; // skipping empty lines
                Candidate currentCandidate = candidateIO.read(line);

                if (item.getCandidateId().equals(currentCandidate.getCandidateId())) {
                    writer.write(candidateIO.write(item));
                    writer.write(System.lineSeparator());
                } else {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
            }
            s.close();
        }
        Files.move(tmpPath, dbFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void deleteItem(String entityId) throws IOException {
        Preconditions.checkArgument(entityId != null, "Candidate id must be null");
        Path tmpPath = Paths.get(dbFilePath.toString() + ".tmp");
        Scanner s = new Scanner(dbFilePath);

        try (BufferedWriter writer = Files.newBufferedWriter(tmpPath, Charset.defaultCharset())) {

            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.isEmpty())
                    continue; // skipping empty lines
                Candidate currentCandidate = candidateIO.read(line);

                if (!entityId.equals(currentCandidate.getCandidateId())) {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
            }
            s.close();
        }
        Files.move(tmpPath, dbFilePath, StandardCopyOption.REPLACE_EXISTING);

    }


    @Override
    protected Path getDBFilePath() {
        return dbFilePath;
    }

    @Override
    protected EntityIO<Candidate> getEntityIO() {
        return candidateIO;
    }
}
