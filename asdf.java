package frc.robot;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.TimedRobot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  // motors
  CANSparkMax intake = new CANSparkMax(4, MotorType.kBrushless);
  CANSparkMax flywheelLeft = new CANSparkMax(10, MotorType.kBrushless);
  CANSparkMax driveLeftA = new CANSparkMax(2, MotorType.kBrushed);
  CANSparkMax driveLeftB = new CANSparkMax(1, MotorType.kBrushed);
  CANSparkMax flywheelRight = new CANSparkMax(6, MotorType.kBrushless);
  //CANSparkMax intakePivot = new CANSparkMax(7, MotorType.kBrushless);
  CANSparkMax driveRightA = new CANSparkMax(9, MotorType.kBrushed);
  CANSparkMax driveRightB = new CANSparkMax(5, MotorType.kBrushed);
  
  // controllers
  XboxController driveControllerA = new XboxController(1);
  XboxController driveControllerB = new XboxController(0);

  // encoders
  //private RelativeEncoder intakePivotEncoder;
  private RelativeEncoder flywheelEncoder;

  // drive
  DifferentialDrive drive = new DifferentialDrive(driveLeftA, driveRightA);


  @Override 
  public void robotInit() {
  // directionals
  intake.setInverted(false);
  flywheelLeft.setInverted(true);
  driveLeftA.setInverted(true);
  flywheelRight.setInverted(false);
  //intakePivot.setInverted(false);
  driveRightA.setInverted(false);


  // set all speeds 0
  intake.set(0);
  flywheelLeft.set(0);

  driveLeftA.set(0);
  driveLeftB.set(0);
  flywheelRight.set(0);
  //intakePivot.set(0);

  driveRightA.set(0);
  driveRightB.set(0);
  
  // encoders
  //intakePivotEncoder = intakePivot.getEncoder();
  flywheelEncoder = flywheelLeft.getEncoder();

  flywheelEncoder.setPosition(0);
  //intakePivotEncoder.setPosition(0);
  

  // setting slave motors
  driveLeftB.follow(driveLeftA);
  driveRightB.follow(driveRightA);

  // drive
  
  

  

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


  private double revTime = 0.0;
  private double endShoot = 0.0;

  private boolean buttonPressed = false;

  private boolean intakeActive = false;
  private boolean outakeActive = false;

  SlewRateLimiter driveLimiter = new SlewRateLimiter(0.15);
  SlewRateLimiter turnLimiter = new SlewRateLimiter(0.25);
  
  @Override
  public void teleopPeriodic() {
    // encoders
    //SmartDashboard.putNumber("Encoder Position", intakePivotEncoder.getPosition());
    SmartDashboard.putNumber("Encoder Position", flywheelEncoder.getPosition());

    // drive
    drive.tankDrive(driveLimiter.calculate(driveControllerA.getLeftY()), turnLimiter.calculate(driveControllerA.getRightX()));

    // get sensor position
    //double sensorPosition = intakePivotEncoder.getPosition();

    // calculations
   /* double error = setpoint - sensorPosition;

    double outputSpeed = kP * error;

    // intake pivot
    if (driveControllerA.getRightBumper()) // intake down/out
    {
      setpoint = 100;
    }
    else if (driveControllerB.getRightBumper()) // intake up/in
    {
      setpoint = 0;
    }
    else if (driveControllerB.getLeftBumper()) // amp 
    {
      setpoint = 50;
    }

    intakePivot.set(outputSpeed); // setting pivot motor to the pid calculations
    */
  
  // flywheels/speaker
    
    // speaker

    double speakerSpeed = 100;

    /*if (driveControllerB.getBButton()) 
    {
      double flywheelPosition = 
    }*/

    // intake
    if (driveControllerA.getLeftTriggerAxis() > 0.25 && !intakeActive)
    {
      intake.set(0.75);
      intakeActive = true;
    }
    else if (driveControllerA.getLeftTriggerAxis() < 0.25 && intakeActive)
    {
      intake.set(0);
      intakeActive = false;
    }

    // outake
    if (driveControllerA.getRightTriggerAxis() > 0.25 && !outakeActive) 
    {
      intake.set(0.4);
      outakeActive = true;
    }
    else if (driveControllerA.getRightTriggerAxis() < 0.25 && outakeActive)
    {
      intake.set(0);
      outakeActive = false;
    }

    double flywheelPositionActive = flywheelEncoder.getPosition();

    if (driveControllerA.getRightBumper() && !buttonPressed) {
      double flywheelPosition = flywheelEncoder.getPosition();
      revTime = flywheelPosition + 140;
      endShoot = flywheelPosition + 200;

      buttonPressed = true;
    }
    
    if (flywheelPositionActive < endShoot) 
    {
      flywheelLeft.set(1);
      flywheelRight.set(0.95);
    }

    if (flywheelPositionActive < endShoot && flywheelPositionActive > revTime) 
    {
      intake.set(-0.25);
    }

    if (flywheelPositionActive > endShoot && buttonPressed) 
    {
      flywheelLeft.set(0);
      flywheelRight.set(0);
      intake.set(0);

      buttonPressed = false;
    }



  }

  @Override
  public void disabledInit() {
  // set all speeds 0
  intake.set(0);
  flywheelLeft.set(0);

  driveLeftA.set(0);
  driveLeftB.set(0);
  flywheelRight.set(0);
  //intakePivot.set(0);

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
