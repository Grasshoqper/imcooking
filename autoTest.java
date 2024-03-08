package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Robot extends TimedRobot {
  // motors
  CANSparkMax intake = new CANSparkMax(4, MotorType.kBrushless);
  CANSparkMax flywheelLeft = new CANSparkMax(10, MotorType.kBrushless);
  CANSparkMax driveLeftA = new CANSparkMax(2, MotorType.kBrushed);
  CANSparkMax driveLeftB = new CANSparkMax(1, MotorType.kBrushed);
  CANSparkMax flywheelRight = new CANSparkMax(6, MotorType.kBrushless);
  CANSparkMax intakePivot = new CANSparkMax(7, MotorType.kBrushless);
  CANSparkMax driveRightA = new CANSparkMax(9, MotorType.kBrushed);
  CANSparkMax driveRightB = new CANSparkMax(5, MotorType.kBrushed);
  
  // controllers
  XboxController driveControllerA = new XboxController(0);
  XboxController driveControllerB = new XboxController(1);

  // encoders
  private RelativeEncoder intakePivotEncoder;
  private RelativeEncoder flywheelEncoder;

  Encoder driveLeftEncoder;
  Encoder driveRightEncoder;

  // drive
  AHRS navx;


  @Override 
  public void robotInit() {
  // directionals
  intake.setInverted(false);
  flywheelLeft.setInverted(true);
  driveLeftA.setInverted(true);
  flywheelRight.setInverted(false);
  intakePivot.setInverted(false);
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
  intakePivotEncoder = intakePivot.getEncoder();
  flywheelEncoder = flywheelLeft.getEncoder();

  flywheelEncoder.setPosition(0);
  intakePivotEncoder.setPosition(0);
  
  driveRightEncoder = new Encoder(0,1, false, EncodingType.k4X);
  driveLeftEncoder = new Encoder(2, 3, true, EncodingType.k4X);
  driveRightEncoder.setDistancePerPulse(4);
  driveLeftEncoder.setDistancePerPulse(4);



  // setting slave motors
  driveLeftB.follow(driveLeftA);
  driveRightB.follow(driveRightA);

  // drive



  
  // auto 
 

  navx = new AHRS(SPI.Port.kMXP); 

  }
  @Override
  public void robotPeriodic() {
   

  }


  

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

  
  @Override
  public void teleopPeriodic() {
    // encoders
    SmartDashboard.putNumber("Intake Position", intakePivotEncoder.getPosition());
    SmartDashboard.putNumber("Flywheel Position", flywheelEncoder.getPosition());
    
    // drive
    double forward = -driveControllerA.getLeftY();
    double turn = -driveControllerA.getRightX();
    double driveLeftPower = forward - turn;
    double driveRightPower = forward + turn;
    driveLeftA.set(driveLeftPower);
    driveRightA.set(driveRightPower);

    SmartDashboard.putNumber("Drive Position", navx.getAngle());
    SmartDashboard.putNumber("Left Drive", driveLeftEncoder.getDistance());
    SmartDashboard.putNumber("Right Drive", driveRightEncoder.getDistance());
    // get sensor position


   

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
