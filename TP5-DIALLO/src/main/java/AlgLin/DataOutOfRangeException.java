package AlgLin;

public class DataOutOfRangeException extends Exception {
	private String message;

	public DataOutOfRangeException(String message) {
		super(message);
		this.message = message;
	}

	public String toString() {
		if (message == null || message.isEmpty())
			return "La valeur n'est pas comprise dans l'intervalle les abscisses des points de support !";
		return this.message;
	}
}
