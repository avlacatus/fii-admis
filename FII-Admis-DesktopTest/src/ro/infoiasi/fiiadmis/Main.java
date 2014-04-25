package ro.infoiasi.fiiadmis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<CandidateBean> list = readCandidates("test.input");
		System.out.println(list);
	}
	
	public static List<CandidateBean> readCandidates(String inputFile) {
		List<CandidateBean> output = new ArrayList<CandidateBean>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(inputFile));
			String line;
			while ((line = br.readLine()) != null) {
				CandidateBean bean = parseCandidateFromLine(line);
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
	private static CandidateBean parseCandidateFromLine(String line) {
		CandidateBean output = new CandidateBean();
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
