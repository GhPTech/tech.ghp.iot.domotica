package tech.ghp.iot.domotica.provider;

import org.osgi.service.component.annotations.Component;

import tech.ghp.iot.domotica.api.Domotica;

/**
 * 
 */
@Component(name = "tech.ghp.iot.domotica")
public class DomoticaImpl implements Domotica{
	
	public String upper(String input){
		return input.toUpperCase();
	}
	
}
