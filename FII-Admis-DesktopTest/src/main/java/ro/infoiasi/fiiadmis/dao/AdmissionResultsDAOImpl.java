package ro.infoiasi.fiiadmis.dao;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import ro.infoiasi.fiiadmis.dao.parser.DefaultAdmissionResultsIO;
import ro.infoiasi.fiiadmis.dao.parser.EntityIO;
import ro.infoiasi.fiiadmis.model.AdmissionResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class AdmissionResultsDAOImpl extends AbstractEntityDAO<AdmissionResult> {

    private static final Logger LOG = Logger.getLogger(AdmissionResultsDAOImpl.class);

    private final Path dbFilePath;
    private final DefaultAdmissionResultsIO admissionResultsIO;

    public AdmissionResultsDAOImpl(String dbFilename, DefaultAdmissionResultsIO admissionResultsIO) {
        this.dbFilePath = Paths.get(dbFilename);
        this.admissionResultsIO = admissionResultsIO;
    }

    @Override
    public AdmissionResult getItemById(final String entityId) throws IOException {
        Preconditions.checkArgument(entityId != null, "Admission result id must not be null");
        return getSingleItem(new Predicate<AdmissionResult>() {
            @Override
            public boolean apply(AdmissionResult result) {
                return entityId.equals(result.getCandidateId());
            }
        });
    }

    @Override
    public List<AdmissionResult> getItems(Predicate<AdmissionResult> filter) throws IOException {
        Preconditions.checkArgument(filter != null, "Filter must not be null");
        return getMultipleItems(filter);
    }

    @Override
    public String addItem(AdmissionResult item) throws IOException {
        String newId = RandomStringUtils.randomAlphanumeric(4);
        item.setResultId(newId);
        try (BufferedWriter writer = Files.newBufferedWriter(dbFilePath, Charset.defaultCharset(), StandardOpenOption.APPEND)) {
            writer.write(admissionResultsIO.write(item));
            writer.flush();
        }
        return newId;
    }

    @Override
    public void updateItem(AdmissionResult item) {
        /**
         * TODO implement me
         */
    }

    @Override
    public void deleteItem(String entityId) {
        /**
         * TODO implement me
         */
    }

    @Override
    protected Path getDBFilePath() {
        return dbFilePath;
    }

    @Override
    protected EntityIO<AdmissionResult> getEntityIO() {
        return admissionResultsIO;
    }
}
