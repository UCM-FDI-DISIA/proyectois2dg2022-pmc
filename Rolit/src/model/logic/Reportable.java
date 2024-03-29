package model.logic;

import org.json.JSONObject;

/**
 * This interface is implemented by serializable Objects
 * @author PMC
 *
 */
public interface Reportable {
	/**
	 * This method serializes an object into a JSON
	 * @return Serialization of the object in JSONObject
	 */
	public JSONObject report();
}
