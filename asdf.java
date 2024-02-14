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
  CANSparkMax intakePivot = new CANSparkMax(6, MotorType.kBrushless);
  CANSparkMax intake = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax flywheelA = new CANSparkMax(1, MotorType.kBrushless);
  CANSparkMax flywheelB = new CANSparkMax(7, MotorType.kBrushless);

  
  XboxController driveController = new XboxController(0);

  @Override
  public void robotInit() {
  //Directionals
  driveLeftA.setInverted(true);
  driveLeftB.setInverted(true);
  driveRightA.setInverted(false);
  driveRightB.setInverted(false);
  flywheelA.setInverted(false);
  flywheelB.setInverted(true);

  //Setting Motors Off
  driveLeftA.set(0);
  driveLeftB.set(0);
  driveRightA.set(0);
  driveRightB.set(0);
  intakePivot.set(0);
  intake.set(0);
  flywheelA.set(0);
  flywheelB.set(0);
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

    if (intakePivotIn && intakePivotInOff) // If your trying to turn pivot both ways it shuts motor off
    {
      intakePivot.set(0);
    }

    //Flywheels
    double speakerSpeed = 0.7;
    double ampSpeed = 0.4;

    //Amp
    boolean ampSequence = driveController.getYButtonPressed();
    boolean ampSequenceOff = driveController.getYButtonReleased();

    if (ampSequence) //Amp stuff
    {
      flywheelA.set(ampSpeed);
      flywheelB.set(ampSpeed - 0.05);
      intake.set(outakeSpeed + 0.2);
    }
    else if (ampSequenceOff) //Amp stuff over
    {
      flywheelA.set(0);
      flywheelB.set(0);
    }


    //Speaker
    boolean speakerSequence = driveController.getBButtonPressed();
    boolean speakerSequenceOff = driveController.getBButtonReleased();

    if (speakerSequence)
    {
      flywheelA.set(speakerSpeed);
      flywheelB.set(speakerSpeed - 0.05);
      intake.set(outakeSpeed);
    }
    else if (speakerSequenceOff)
    {
      flywheelA.set(0);
      flywheelB.set(0);
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
  flywheelA.set(0);
  flywheelB.set(0);

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
