package it.polito.tdp.porto.model;

public class Co_autori {
	
	private Author A1;
	private Author A2;
	
	public Co_autori(Author a1, Author a2) {
		super();
		A1 = a1;
		A2 = a2;
	}

	public Author getA1() {
		return A1;
	}

	public void setA1(Author a1) {
		A1 = a1;
	}

	public Author getA2() {
		return A2;
	}

	public void setA2(Author a2) {
		A2 = a2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((A1 == null) ? 0 : A1.hashCode());
		result = prime * result + ((A2 == null) ? 0 : A2.hashCode());
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
		Co_autori other = (Co_autori) obj;
		if (A1 == null) {
			if (other.A1 != null)
				return false;
		} else if (!A1.equals(other.A1))
			return false;
		if (A2 == null) {
			if (other.A2 != null)
				return false;
		} else if (!A2.equals(other.A2))
			return false;
		return true;
	}
	
	

}
