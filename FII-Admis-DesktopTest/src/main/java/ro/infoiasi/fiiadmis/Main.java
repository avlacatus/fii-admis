package ro.infoiasi.fiiadmis;

import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<Candidate> list = readCandidates("test.input");
		System.out.println(list);
	}
	
	public static List<Candidate> readCandidates(String inputFile) {
		List<Candidate> output = new ArrayList<Candidate>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(inputFile));
			String line;
			while ((line = br.readLine()) != null) {
				Candidate bean = parseCandidateFromLine(line);
				if (bean != null) {
					output.add(bean);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * TODO has no validations
	 * @param line
	 * @return
	 */
	private static Candidate parseCandidateFromLine(String line) {
		Candidate output = new Candidate();
		String[] tokens = line.split(":");
		if (tokens.length == 6) {
			output.setCandidateId(Integer.parseInt(tokens[0]));
			output.setFirstName(tokens[1]);
			output.setLastName(tokens[2]);
			output.setSocialId(tokens[3]);
			output.setGpaGrade(Double.parseDouble(tokens[4]));
			output.setATestGrade(Double.parseDouble(tokens[5]));
		}
		return output;
	}

}
