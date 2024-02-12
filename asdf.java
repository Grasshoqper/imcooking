package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends TimedRobot {
  //Motors:
  CANSparkMax driveLeftA = new CANSparkMax(4, MotorType.kBrushed);
  CANSparkMax driveLeftB = new CANSparkMax(5, MotorType.kBrushed);
  CANSparkMax driveRightA = new CANSparkMax(9, MotorType.kBrushed);
  CANSparkMax driveRightB = new CANSparkMax(10, MotorType.kBrushed);
  CANSparkMax intakePivot = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax intake = new CANSparkMax(7, MotorType.kBrushless);
  
  XboxController driveController = new XboxController(0);

  //Variable Thingys:

  //Drive
  double turn;
  double forward;

  //Intake
  double in;
  double out; 

  //Intake Pivot
  double intakePivotSpeed;



  @Override
  public void robotInit() {
  //Directionals
  driveLeftA.setInverted(true);
  driveLeftB.setInverted(true);
  driveRightA.setInverted(false);
  driveRightB.setInverted(false);


  //Setting Motors Off
  driveLeftA.set(0);
  driveLeftB.set(0);
  driveRightA.set(0);
  driveRightB.set(0);
  intakePivot.set(0);
  intake.set(0);
  }
  @Override
  public void robotPeriodic() {
  //Drive:
  double forward = -driveController.getLeftY();
  double turn = -driveController.getRightX();

  double driveLeftPower = forward - turn;
  double driveRightPower = forward + turn;

  driveLeftA.set(driveLeftPower);
  driveLeftB.set(driveLeftPower);
  driveRightA.set(driveRightPower);
  driveRightB.set(driveRightPower);
  
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    //Intake:
    double in = driveController.getLeftTriggerAxis();
    double out = driveController.getRightTriggerAxis();
    
    double intakePower = in - out;
  
    intake.set(intakePower);

    //Intake Pivot
    double intakePivotSpeed = 0.6;

    boolean intakePivotInOn = driveController.getAButtonPressed();
    boolean intakePivotInOff = driveController.getAButtonReleased();

    if (intakePivotInOn) //Pulling intake into the robot
    {
      intakePivot.set(intakePivotSpeed);
    } 
    else if (intakePivotInOff) //Putting intake out of the robot
    {
      intakePivot.set(0);
    }

    if (intakePivotInOn && intakePivotInOff) // If your trying to turn pivot both ways it shuts motor off
    {
      intakePivot.set(0);
    }


  

  }

  @Override
  public void disabledInit() {
  //Turn off all motors when disabled
  driveLeftA.set(0);
  driveLeftB.set(0);
  driveRightA.set(0);
  driveRightB.set(0);
  intakePivot.set(0);
  intake.set(0);

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
