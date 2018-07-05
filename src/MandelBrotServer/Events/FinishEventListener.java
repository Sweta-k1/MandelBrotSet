/*
 *  EventListener implementation with a child class
*/
package MandelBrotServer.Events;

/**
 *
 * @author Sweta
 */

    import java.util.EventListener;



public interface FinishEventListener extends EventListener {

	public void finish(FinishEvent dce);

}

