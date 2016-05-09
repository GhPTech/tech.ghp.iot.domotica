package tech.ghp.iot.domotica.provider;

//import java.io.Closeable;
//import java.io.IOException;

//import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Deactivate;
//import org.osgi.service.component.annotations.Reference;

//import com.pi4j.io.gpio.GpioController;
//import com.pi4j.io.gpio.GpioFactory;
//import com.pi4j.io.gpio.GpioPinDigitalOutput;
//import com.pi4j.io.gpio.PinState;
//import com.pi4j.io.gpio.RaspiPin;
//import com.pi4j.system.SystemInfo;

//import osgi.enroute.scheduler.api.Scheduler;

import tech.ghp.iot.domotica.api.Domotica;

/**
 * 
 */
@Component(name = "tech.ghp.iot.domotica")
public class DomoticaImpl implements Domotica{
	
	public String upper(String input){
		return input.toUpperCase();
	}
	/*
	private Scheduler scheduler;
	private Closeable schedule;
	
	@Activate
	void activate() throws Exception {
		System.out.println("Hello Raspberry Pi!");
		System.out.println(SystemInfo.getBoardType().name() + " " + SystemInfo.getSerial());
		try {
			GpioController gpioController = GpioFactory.getInstance();
		
				
			while (!gpioController.getProvisionedPins().isEmpty()) 
			gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

			GpioPinDigitalOutput out = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "LED1",PinState.LOW);

			schedule = scheduler.schedule(() -> {
				boolean high = out.getState().isHigh();
				out.setState(!high);
			}, 500);
			} catch (Exception e) {e.printStackTrace();}
	}

	@Deactivate
	void deactivate() throws IOException{
			schedule.close();
			System.out.println("Goodbye Raspberry Pi");
	}
		
	@Reference
	void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	*/
}
