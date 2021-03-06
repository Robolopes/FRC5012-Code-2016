package com.gryffingear.y2016.systems;

import com.gryffingear.y2016.config.Constants;
import com.gryffingear.y2016.utilities.Debouncer;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake {

	private CANTalon intakeMotor = null;
	private Solenoid intakeSolenoid = null;
	private AnalogInput outerSensor = null;
	private AnalogInput innerSensor = null;

	public Intake(int im, int is, int oss, int iss) {

		intakeMotor = configureTalon(new CANTalon(im));
		intakeSolenoid = new Solenoid(is);
		outerSensor = new AnalogInput(oss);
		innerSensor = new AnalogInput(iss);
	}

	private CANTalon configureTalon(CANTalon in) {

		in.clearStickyFaults();
		in.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		in.setVoltageRampRate(Constants.Intake.RAMP_RATE);
		in.enableControl();
		System.out.println("[CANTalon]" + in.getDescription() + " Initialized at device ID: " + in.getDeviceID());
		return in;
	}

	public void setIntake(boolean state) {

		intakeSolenoid.set(state);
	}

	public void runIntake(double intakev) {

		intakeMotor.set(intakev);
	}

	private double getOuter() {
		return outerSensor.getVoltage();
	}

	private double getInner() {
		return innerSensor.getVoltage();
	}

	private Debouncer enteredFilter = new Debouncer(0.075);
	private Debouncer stagedFilter = new Debouncer(0.075);
	
	private boolean m_ballEntered = false;
	private boolean m_ballStaged = false;
	
	public boolean getBallEntered() {
		return m_ballEntered;
	}

	public boolean getBallStaged() {
		return m_ballStaged;
	}
	
	public void update() {
		m_ballEntered = enteredFilter.update(getOuter() < Constants.Intake.BALL_SENSOR_THRESHOLD);
		m_ballStaged = stagedFilter.update(getInner() < Constants.Intake.BALL_SENSOR_THRESHOLD);
	}
}
