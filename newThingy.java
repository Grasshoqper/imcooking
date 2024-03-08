package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
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
  m_chooser.setDefaultOption("Default Auto", DefaultAuto);
  m_chooser.addOption("My Auto", RightTwoNoteAuto);
  SmartDashboard.putData("Auto choices", m_chooser);

  navx = new AHRS(SPI.Port.kMXP); 

  

  }
  @Override
  public void robotPeriodic() {
    double driveRightDistance = driveRightEncoder.getDistance();
    double driveLeftDistance = driveLeftEncoder.getDistance();

    SmartDashboard.putNumber("Right Drive", driveRightDistance);
    SmartDashboard.putNumber("Left Drive", driveLeftDistance);

    double sensorPosition = intakePivotEncoder.getPosition();

    // calculations
    double error = setpoint - sensorPosition;

    double outputSpeed = kP * error;

    intakePivot.set(outputSpeed); // setting pivot motor to the pid calculations
  }

  private static final String DefaultAuto = "Default";
  private static final String RightTwoNoteAuto = "Left Two Note";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);

    flywheelEncoder.setPosition(0);

  }
  boolean autoPart1 = false;
  boolean autoPart2 = false;
  boolean autoPart3 = false;
  boolean autoPart4 = false;
  double driveAngle;
  double driveSetpoint = 0;
  double driveKP = 0.05;
  @Override
  public void autonomousPeriodic() {
    // right
    double driveRightPosition = driveRightEncoder.getDistance();

    double driveRightError = driveSetpoint - driveRightPosition;

    double leftSideAuto = driveKP * driveRightError;

    // left
    double driveLeftPosition = driveLeftEncoder.getDistance();

    double driveLeftError = driveSetpoint - driveLeftPosition;

    double rightSideAuto = driveKP * driveLeftError;

    

    double driveAngle = navx.getAngle();

    switch (m_autoSelected) {
      case DefaultAuto:
      default:
        double flywheelPositionActive = flywheelEncoder.getPosition();
        
        double revTime = 140;
        double endShoot = 200;
        
    
      if (flywheelPositionActive < endShoot) 
      {
        flywheelLeft.set(1);
        flywheelRight.set(0.95);
      }

      if (flywheelPositionActive < endShoot && flywheelPositionActive > revTime) 
      {
        intake.set(-0.25);
      }

    if (flywheelPositionActive > endShoot) 
    {
        flywheelLeft.set(0);
        flywheelRight.set(0);
        intake.set(0);

        buttonPressed = false;
        boolean autoPart1 = true;
    }

        break;
      case RightTwoNoteAuto:
        flywheelPositionActive = flywheelEncoder.getPosition();
        
        revTime = 140;
        endShoot = 200;
        
    
      if (flywheelPositionActive < endShoot) 
      {
        flywheelLeft.set(1);
        flywheelRight.set(0.95);
      }

      if (flywheelPositionActive < endShoot && flywheelPositionActive > revTime) 
      {
        intake.set(-0.25);
      }

    if (flywheelPositionActive > endShoot) 
    {
        flywheelLeft.set(0);
        flywheelRight.set(0);
        intake.set(0);

        buttonPressed = false;
        autoPart1 = true;
    }
    if (autoPart1) 
    {
      driveLeftA.set(rightSideAuto);
      driveRightA.set(rightSideAuto - 0.4);
      autoPart1 = false;
      autoPart2 = true;

    } 
    if (driveAngle > 44.25 && autoPart2)
    {
      driveLeftA.set(rightSideAuto);
      driveRightA.set(rightSideAuto);
      //setpoint = 0;
      intake.set(0.4);
      autoPart2 = false;
      autoPart3 = true;
    }
        break;
    }





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
    SmartDashboard.putNumber("Encoder Position", intakePivotEncoder.getPosition());
    SmartDashboard.putNumber("Encoder Position", flywheelEncoder.getPosition());
    
    // drive
    double forward = -driveControllerA.getLeftY();
    double turn = -driveControllerA.getRightX();
    double driveLeftPower = forward - turn;
    double driveRightPower = forward + turn;
    driveLeftA.set(driveLeftPower);
    
    driveRightA.set(driveRightPower);
    // get sensor position

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

    
    
  
  // flywheels/speaker
    
    // speaker



   

    // intake
    if (driveControllerA.getLeftTriggerAxis() > 0.25 && !intakeActive)
    {
      intake.set(0.35);
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
