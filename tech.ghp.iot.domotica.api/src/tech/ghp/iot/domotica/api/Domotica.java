package tech.ghp.iot.domotica.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A service that converts a string to upper case
 */
@ProviderType
public interface Domotica {
	
	/**
	 * Converts a string and return the upper case string
	 */
	 String upper(String input);

	
}
