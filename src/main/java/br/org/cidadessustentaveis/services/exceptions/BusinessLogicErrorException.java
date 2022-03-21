package br.org.cidadessustentaveis.services.exceptions;

public class BusinessLogicErrorException extends RuntimeException {

	private static final long serialVersionUID = -2505013071470822451L;
	
	public BusinessLogicErrorException(String msg) {
		super(msg);
	}
	
	public BusinessLogicErrorException(String msg, Throwable cause) {
		super(msg, cause);
	}


}
