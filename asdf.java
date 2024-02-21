package frc.robot;

import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.TimedRobot;

import java.util.concurrent.CancellationException;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Robot extends TimedRobot {
  RelativeEncoder intakePivotEncoder;

  //Motors:
  CANSparkMax intake = new CANSparkMax(1, MotorType.kBrushless);
  CANSparkMax intakePivot = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax B = new CANSparkMax(7, MotorType.kBrushless);
 

  XboxController driveController = new XboxController(0);

  @Override
  public void robotInit() {
  
  intakePivot.restoreFactoryDefaults();
  intakePivotEncoder = intakePivot.getEncoder();
  //Directionals
  
  

  //Setting Motors Off
 
  
  }
  @Override
  public void robotPeriodic() {
  //Drive:
 

  
  
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    
  }

  final double kP = 0.05;
  double setpoint = 0;

  @Override
  public void teleopPeriodic() {
    
    double intakeSpeed = driveController.getRightTriggerAxis() - driveController.getLeftTriggerAxis();

    intake.set(intakeSpeed);
    
    double sensorPosition = intakePivotEncoder.getPosition();
    boolean intakePivotIn = driveController.getAButton();
    boolean intakePivotOut = driveController.getXButton();
    boolean intakePivotAmp = driveController.getBButton();
    

    if (intakePivotIn) 
    {
      setpoint = 0;
    }
    else if (intakePivotOut)
    {
      setpoint = 100;
    }
    else if (intakePivotAmp) 
    {
      setpoint = 50;
    }

    double error = setpoint - sensorPosition;
    
    double intakePivotSpeed = kP * error;
    
    intakePivot.set(intakePivotSpeed);


  

  }

  @Override
  public void disabledInit() {
  //Turn off all motors when disabled
 
  
  }
  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
