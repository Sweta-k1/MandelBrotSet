/*
 *Implementation of EventObject with a new child class.
 */
package MandelBrotServer.Events;

import java.util.EventObject;

/**
 *
 * @author Sweta
 */

    public class FinishEvent extends EventObject {

	private static final long serialVersionUID = 1L;



	public FinishEvent(Object source){

		super(source);

	}
    
}
