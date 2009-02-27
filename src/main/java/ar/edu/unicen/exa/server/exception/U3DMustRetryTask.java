/**
 * 
 */
package ar.edu.unicen.exa.server.exception;

import com.sun.sgs.app.ExceptionRetryStatus;

/**
 * Exception created for use in task that need Re-Schedule on failures.
 * 
 * @author Cabrera Emilio Facundo &lt;cabrerafacundo at gmail dot com&gt;
 * @encoding UTF-8
 */
public final class U3DMustRetryTask  
extends RuntimeException implements ExceptionRetryStatus {

	/** Default serialVersionUID. */
	private static final long serialVersionUID = 650164550976425140L;

	/**
	 * Indicate if a task need reschedule.
	 * 
	 * @see com.sun.sgs.app.ExceptionRetryStatus#shouldRetry()
	 * 
	 * @return true indicate that the task needs retry.
	 */
	@Override
	public boolean shouldRetry() {
		return true;
	}

}
