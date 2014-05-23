package ro.infoiasi.fiiadmis.service.rest.pdf;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.service.rest.dao.DaoHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ResultsPDFExport extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Preconditions.checkArgument(request != null, "Can't work on null request.");
        Preconditions.checkArgument(response != null, "Can't work on null response.");

        DaoHolder.initializeDb();
        List<AdmissionResult> admissionResults = DaoHolder.getAdmissionResultsDao().getItems(null, null);
        List<Candidate> candidates = DaoHolder.getCandidateDao().getItems(null, new Comparator<Candidate>() {
            @Override
            public int compare(Candidate o1, Candidate o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });

        OutputStream outputStream = response.getOutputStream();
        // Expire response (we don't want the image to be cached)
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Max-Age", 0);

        try {
            exportResults(outputStream, candidates, admissionResults);

            response.setContentType("application/pdf");
            outputStream.close();

        } catch (COSVisitorException e) {
            throw new IOException(e);
        }

    }

    protected void exportResults(OutputStream outputStream, List<Candidate> candidates, List<AdmissionResult> results)
            throws IOException, COSVisitorException {
        Preconditions.checkArgument(outputStream != null);
        Preconditions.checkNotNull(candidates != null);
        Preconditions.checkNotNull(results != null);

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA_BOLD;

        // Start a new content stream which will "hold" the to be created
        // content
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.moveTextPositionByAmount(50, 750);

        Map<String, AdmissionResult> resultsMap = Maps.newHashMap();
        for (AdmissionResult result : results) {
            resultsMap.put(result.getCandidateId(), result);
        }

        for (Candidate candidate : candidates) {
            AdmissionResult admissionResult = resultsMap.get(candidate.getId());
            contentStream.setFont(font, 10);
            contentStream.moveTextPositionByAmount(0, -10);
            contentStream.drawString(Joiner.on("\t\t").join(
                    Arrays.asList(candidate.getLastName(),
                            candidate.getFirstName(),
                            candidate.getGpaGrade(),
                            candidate.getATestGrade(),
                            admissionResult != null ? admissionResult.getFinalGrade() : "-",
                            admissionResult != null ? admissionResult.getAdmissionStatus().getStatusString() : "-")
                    ));

            contentStream.drawString("\n");

        }
        contentStream.endText();

        // Make sure that the content stream is closed:
        contentStream.close();

        // Save the results and ensure that the document is properly closed:
        document.save(outputStream);
        document.close();
    }

}
