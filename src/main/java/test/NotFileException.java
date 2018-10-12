package test;
/**
 * 自定义的异常类
 * @author HJK
 *
 */
public class NotFileException extends Exception {
	
	public NotFileException() {
		super();
	}

	public NotFileException(String message) {
		super(message);
	}
}
