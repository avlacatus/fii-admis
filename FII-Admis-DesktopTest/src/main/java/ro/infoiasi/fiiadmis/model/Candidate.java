package ro.infoiasi.fiiadmis.model;

public class Candidate implements Entity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5208664767813416795L;
	private String id;
	private String firstName;
	private String lastName;
	private String socialId;
	private double gpaGrade;
	private double aTestGrade;

	public Candidate() {
	}

	public Candidate(String candidateId, String firstName, String lastName, String socialId, double gpaGrade,
                     double aTestGrade) {
		super();
		this.id = candidateId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.socialId = socialId;
		this.gpaGrade = gpaGrade;
		this.aTestGrade = aTestGrade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return "Candidate [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", socialId=" + socialId + ", gpaGrade=" + gpaGrade + ", aTestGrade=" + aTestGrade + "]";
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Candidate candidate = (Candidate) o;

        if (Double.compare(candidate.aTestGrade, aTestGrade) != 0) return false;
        if (Double.compare(candidate.gpaGrade, gpaGrade) != 0) return false;
        if (firstName != null ? !firstName.equals(candidate.firstName) : candidate.firstName != null) return false;
        if (id != null ? !id.equals(candidate.id) : candidate.id != null) return false;
        if (lastName != null ? !lastName.equals(candidate.lastName) : candidate.lastName != null) return false;
        if (socialId != null ? !socialId.equals(candidate.socialId) : candidate.socialId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (socialId != null ? socialId.hashCode() : 0);
        temp = Double.doubleToLongBits(gpaGrade);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(aTestGrade);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
