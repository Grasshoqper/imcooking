package frc.robot;

import edu.wpi.first.wpilibj.CAN;
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
  CANSparkMax intakePivot = new CANSparkMax(6, MotorType.kBrushless);
  CANSparkMax intake = new CANSparkMax(1, MotorType.kBrushless);
  CANSparkMax flywheelRight = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax flywheelLeft = new CANSparkMax(7, MotorType.kBrushless);
  CANSparkMax climbRight = new CANSparkMax(3, MotorType.kBrushless);
  CANSparkMax climbLeft = new CANSparkMax(8, MotorType.kBrushless);

  XboxController driveController = new XboxController(0);

  @Override
  public void robotInit() {
  //Directionals
  driveLeftA.setInverted(true);
  driveLeftB.setInverted(true);
  driveRightA.setInverted(false);
  driveRightB.setInverted(false);
  flywheelRight.setInverted(false);
  flywheelLeft.setInverted(true);

  //Setting Motors Off
  driveLeftA.set(0);
  driveLeftB.set(0);
  driveRightA.set(0);
  driveRightB.set(0);
  intakePivot.set(0);
  intake.set(0);
  flywheelRight.set(0);
  flywheelLeft.set(0);
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
  
  boolean flipTurn = driveController.getLeftStickButtonPressed();

  if (flipTurn) //Flip the turn when the left joystick pressed
  {
    turn *= -1;
  }

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
    
    double intakeSpeed = in - out;
    double outakeSpeed = -0.5;
  
    intake.set(intakeSpeed);


    //Intake Pivot
    double intakePivotSpeed = 0.6;
    double intakePivotOutSpeed = -0.6;

    boolean intakePivotIn = driveController.getAButtonPressed();
    boolean intakePivotInOff = driveController.getAButtonReleased();

    boolean intakePivotOut = driveController.getXButtonPressed();
    boolean intakePivotOutOff = driveController.getXButtonReleased();

    if (intakePivotIn) //Pulling intake into the robot
    {
      intakePivot.set(intakePivotSpeed);
    } 
    else if (intakePivotInOff) //Shutting motor off when realeasing button
    {
      intakePivot.set(0);
    }

    if (intakePivotOut) //Putting intake out
    {
      intakePivot.set(intakePivotOutSpeed);
    }
    else if (intakePivotOutOff) //Shutting motor off when releasing button
    {
      intakePivot.set(0);
    }


    //Flywheels
    double speakerSpeed = 1;
    double ampSpeed = 0.4;

    //Amp
    boolean ampSequence = driveController.getYButtonPressed();
    boolean ampSequenceOff = driveController.getYButtonReleased();

    if (ampSequence) //Amp stuff
    {
      flywheelRight.set(ampSpeed);
      flywheelLeft.set(ampSpeed - 0.05);
      intake.set(outakeSpeed + 0.2);
    }
    else if (ampSequenceOff) //Amp stuff over
    {
      flywheelRight.set(0);
      flywheelLeft.set(0);
    }


    //Speaker
    boolean speakerSequence = driveController.getBButtonPressed();
    boolean speakerSequenceOff = driveController.getBButtonReleased();

    if (speakerSequence)
    {
      flywheelRight.set(speakerSpeed);
      flywheelLeft.set(speakerSpeed - 0.05);
      intake.set(outakeSpeed);
    }
    else if (speakerSequenceOff)
    {
      flywheelRight.set(0);
      flywheelLeft.set(0);
      intake.set(0);
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
  flywheelRight.set(0);
  flywheelLeft.set(0);

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
