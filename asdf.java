package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends TimedRobot {
  // motors
  CANSparkMax intake = new CANSparkMax(1, MotorType.kBrushless);
  CANSparkMax flywheelLeft = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax flap = new CANSparkMax(3, MotorType.kBrushless);
  CANSparkMax climbLeft = new CANSparkMax(4, MotorType.kBrushless);
  CANSparkMax driveLeftA = new CANSparkMax(5, MotorType.kBrushed);
  CANSparkMax driveLeftB = new CANSparkMax(6, MotorType.kBrushed);
  CANSparkMax flywheelRight = new CANSparkMax(7, MotorType.kBrushless);
  CANSparkMax intakePivot = new CANSparkMax(8, MotorType.kBrushless);
  CANSparkMax climbRight = new CANSparkMax(9, MotorType.kBrushless);
  CANSparkMax driveRightA = new CANSparkMax(10, MotorType.kBrushed);
  CANSparkMax driveRightB = new CANSparkMax(11, MotorType.kBrushed);

  // controllers
  XboxController driveControllerA = new XboxController(0);
  XboxController driveControllerB = new XboxController(1);

  // encoders
  private RelativeEncoder intakePivotEncoder;

  @Override 
  public void robotInit() {
  // directionals
  intake.setInverted(false);
  flywheelLeft.setInverted(true);
  flap.setInverted(false);
  climbLeft.setInverted(true);
  driveLeftA.setInverted(true);
  driveLeftB.setInverted(true);
  flywheelRight.setInverted(false);
  intakePivot.setInverted(false);
  climbRight.setInverted(false);
  driveRightA.setInverted(false);
  driveRightB.setInverted(false);

  // set all speeds 0
  intake.set(0);
  flywheelLeft.set(0);
  flap.set(0);
  climbLeft.set(0);
  driveLeftA.set(0);
  driveLeftB.set(0);
  flywheelRight.set(0);
  intakePivot.set(0);
  climbRight.set(0);
  driveRightA.set(0);
  driveRightB.set(0);
  
  // encoders
  intakePivotEncoder = intakePivot.getEncoder();

  }
  @Override
  public void robotPeriodic() {}

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
    // drive
    double forward = -driveControllerA.getLeftY();
    double turn = -driveControllerA.getRightX();

    double driveLeftPower = forward - turn;
    double driveRightPower = forward + turn;

    driveLeftA.set(driveLeftPower);
    driveLeftB.set(driveLeftPower);
    driveRightA.set(driveRightPower);
    driveRightB.set(driveRightPower);

    while (forward < 0) // flip turn while reversing
    {
      turn = -turn;
    }
  
    // intake pivot
    
    // get sensor position
    double sensorPosition = intakePivotEncoder.getPosition();

    // calculations
    double error = setpoint - sensorPosition;

    double outputSpeed = kP * error;

    if (driveControllerB.getAButton()) // intake down/out
    {
      setpoint = 100;
    }
    else if (driveControllerB.getXButton()) // intake up/in
    {
      setpoint = 0;
    }
    else if (driveControllerB.getYButton()) // amp 
    {
      setpoint = 50;
    }

    intakePivot.set(outputSpeed); // setting pivot motor to the pid calculations
  
  // flywheels/speaker
    
    // speaker

    double speakerSpeed = 100;

    if (driveControllerB.getBButtonPressed()) // set flywheels to speaker speed
    {
      flywheelLeft.set(speakerSpeed);
      flywheelRight.set(speakerSpeed - 5);
    }
    else if (driveControllerB.getBButtonReleased()) // set flywheels to 0 on release
    {
      flywheelLeft.set(0);
      flywheelRight.set(0);
    }

    

    // intake & outake

    double intakeSpeed = 1;
    double outakeSpeed = - 0.5;

    double rightTriggerAxis = driveControllerA.getRightTriggerAxis();
    double leftTriggerAxis = driveControllerA.getLeftTriggerAxis();

    while (rightTriggerAxis > 0.25) // intake
    {
      intake.set(intakeSpeed);
    }
    
    while (leftTriggerAxis > 0.25) // outake
    {
      intake.set(outakeSpeed);
    }

    // climb

    double climbRightSpeed = driveControllerB.getRightY();
    double climbLeftSpeed = driveControllerB.getLeftY();

    climbRight.set(climbRightSpeed);
    climbLeft.set(climbLeftSpeed);

  }

  @Override
  public void disabledInit() {
  // set all speeds 0
  intake.set(0);
  flywheelLeft.set(0);
  flap.set(0);
  climbLeft.set(0);
  driveLeftA.set(0);
  driveLeftB.set(0);
  flywheelRight.set(0);
  intakePivot.set(0);
  climbRight.set(0);
  driveRightA.set(0);
  driveRightB.set(0);
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
