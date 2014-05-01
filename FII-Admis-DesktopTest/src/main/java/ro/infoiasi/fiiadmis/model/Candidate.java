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
		if (id != other.id)
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
