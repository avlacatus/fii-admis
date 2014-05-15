package ro.infoiasi.fiiadmis.db.parser;

import org.junit.Before;
import org.junit.Test;
import ro.infoiasi.fiiadmis.model.AdmissionResult;

import static org.junit.Assert.assertEquals;

public class DefaultAdmissionResultsFormatterTest {

    private DefaultAdmissionResultsFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new DefaultAdmissionResultsFormatter();
    }

    @Test
    public void testGetFieldSeparator() {

        assertEquals(":", formatter.getFieldSeparator());
    }

    @Test
    public void testReadNormal() {
        AdmissionResult result = formatter.read("id:idcand:8.78:1");

        assertEquals("id", result.getId());
        assertEquals("idcand", result.getCandidateId());
        assertEquals(8.78, result.getFinalGrade(), 0.0);
        assertEquals(AdmissionResult.Status.TAX, result.getAdmissionStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWrongFinalGradeFormat() {
        formatter.read("id:idcand:notgood:1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWrongAdmissionStatusFormat() {
        formatter.read("id:idcand:8:status");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWrongAdmissionStatusNumber() {
        formatter.read("id:idcand:8:4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadInsufficientNumberOfFields() {
        formatter.read("id:idcand:9");
    }

    @Test
    public void testWriteRejected() {
        AdmissionResult result = new AdmissionResult();
        result.setId("id");
        result.setCandidateId("idcand");
        result.setFinalGrade(9.77);
        result.setAdmissionStatus(AdmissionResult.Status.REJECTED);

        String stringResult = formatter.write(result);

        assertEquals("id:idcand:9.77:2", stringResult);
    }

    @Test
    public void testWriteTaxFree() {
        AdmissionResult result = new AdmissionResult();
        result.setId("id");
        result.setCandidateId("idcand");
        result.setFinalGrade(9.77);
        result.setAdmissionStatus(AdmissionResult.Status.TAX_FREE);

        String stringResult = formatter.write(result);

        assertEquals("id:idcand:9.77:0", stringResult);
    }

    @Test
    public void testWriteTax() {
        AdmissionResult result = new AdmissionResult();
        result.setId("id");
        result.setCandidateId("idcand");
        result.setFinalGrade(9.77);
        result.setAdmissionStatus(AdmissionResult.Status.TAX);

        String stringResult = formatter.write(result);

        assertEquals("id:idcand:9.77:1", stringResult);
    }

}
