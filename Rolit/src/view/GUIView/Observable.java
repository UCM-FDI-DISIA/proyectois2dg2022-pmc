package view.GUIView;

/**
 * This interface declares the addObserver method to implement
 * the Observer design pattern
 * @author PMC
 */
public interface Observable<T> {
	void addObserver(T o);
}
