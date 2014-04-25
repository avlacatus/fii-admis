package ro.infoiasi.fiiadmis.model;

public class Candidate {

	private int candidateId;
	private String firstName;
	private String lastName;
	private String socialId;
	private double gpaGrade;
	private double aTestGrade;

	public Candidate() {
	}

	public Candidate(int candidateId, String firstName, String lastName, String socialId, double gpaGrade,
                     double aTestGrade) {
		super();
		this.candidateId = candidateId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.socialId = socialId;
		this.gpaGrade = gpaGrade;
		this.aTestGrade = aTestGrade;
	}

	public int getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(int candidateId) {
		this.candidateId = candidateId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public double getGpaGrade() {
		return gpaGrade;
	}

	public void setGpaGrade(double gpaGrade) {
		this.gpaGrade = gpaGrade;
	}

	public double getATestGrade() {
		return aTestGrade;
	}

	public void setATestGrade(double aTestGrade) {
		this.aTestGrade = aTestGrade;
	}
	
	@Override
	public String toString() {
		return "CandidateBean [candidateId=" + candidateId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", socialId=" + socialId + ", gpaGrade=" + gpaGrade + ", aTestGrade=" + aTestGrade + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(aTestGrade);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + candidateId;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		temp = Double.doubleToLongBits(gpaGrade);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((socialId == null) ? 0 : socialId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Candidate other = (Candidate) obj;
		if (Double.doubleToLongBits(aTestGrade) != Double.doubleToLongBits(other.aTestGrade))
			return false;
		if (candidateId != other.candidateId)
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (Double.doubleToLongBits(gpaGrade) != Double.doubleToLongBits(other.gpaGrade))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (socialId == null) {
			if (other.socialId != null)
				return false;
		} else if (!socialId.equals(other.socialId))
			return false;
		return true;
	}

}
