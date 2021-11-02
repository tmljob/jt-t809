package io.tml.iov.common.exception;

public class BizProcessException extends RuntimeException {

	private static final long serialVersionUID = 8436493212844826112L;

	private final String msg;

	public BizProcessException(String msg) {
	    this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
