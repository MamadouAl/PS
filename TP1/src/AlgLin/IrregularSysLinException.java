package AlgLin;

public class IrregularSysLinException extends Exception {
	private String message;
	
	public IrregularSysLinException(String message) {
		this.message = message;
	}

	public String toString()
	{
		if( message == null || message.isEmpty() ) 
			return "Le système est irrégulier !";
		return this.message;
	}
}