package tech.ghp.iot.domotica.command;

import java.io.Closeable;
import java.io.IOException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.system.SystemInfo;

import osgi.enroute.scheduler.api.Scheduler;

@Component
public class DomoticaCommand {
	
	private Scheduler scheduler;
	private Closeable schedule;
	
	@Activate
	void activate() throws Exception{
			System.out.println("Hello! ");
			System.out.println("I am a Raspberry Pi " + SystemInfo.getBoardType().name());
			System.out.println("Serial " + SystemInfo.getSerial());
			
			try {
				GpioController gpioController = GpioFactory.getInstance();
			
					
				while (!gpioController.getProvisionedPins().isEmpty()) 
					gpioController.unprovisionPin(gpioController.getProvisionedPins().iterator().next());

				GpioPinDigitalOutput out = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "LED1", PinState.LOW);
				
				schedule = scheduler.schedule(() -> {
					boolean high = out.getState().isHigh();
					out.setState(!high);
				}, 1000);
				
			} catch (Exception e) {e.printStackTrace();}
			
	}

	@Deactivate
		void deactivate() throws IOException{	
			schedule.close();	
			System.out.println("Goodbye!");
			
	}
	
	@Reference
	void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

}
