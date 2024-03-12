package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
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

  // encodershnbgf
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
  intakePivot.set(0);

  driveRightA.set(0);
  driveRightB.set(0);
  
  // encoders
  intakePivotEncoder = intakePivot.getEncoder();
  flywheelEncoder = flywheelLeft.getEncoder();

  flywheelEncoder.setPosition(0);
  intakePivotEncoder.setPosition(0);
  
  driveRightEncoder = new Encoder(0,1);
  driveLeftEncoder = new Encoder(2, 3);


  // setting slave motors
  driveLeftB.follow(driveLeftA);
  driveRightB.follow(driveRightA);

  // drive



  
  // auto 
  m_chooser.setDefaultOption("Just Shoot", DefaultAuto);
  m_chooser.addOption("Middle Two Note Auto", MiddleTwoNoteAuto);
  SmartDashboard.putData("Auto choices", m_chooser);

  navx = new AHRS(SPI.Port.kMXP); 

  CameraServer.startAutomaticCapture("camera", 0);
  
  }
  
  final double kP = 0.015;

  double setpoint = 0;

  boolean intakeMoving = false;

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

    // intake pivot
    if (driveControllerB.getRightBumper()) // intake down/out
    {
      setpoint = 16.25;
    }
    else if (driveControllerB.getLeftBumper()) // intake up/in
    {
      setpoint = 0.25;
    }
    
    intakePivot.set(outputSpeed); // setting pivot motor to the pid calculations

    // intaking while moving

    double plusSetpoint = setpoint + 1.5;
    double minusSetpoint = setpoint - 1.5;

    if (!intakeMoving && sensorPosition < minusSetpoint || sensorPosition > plusSetpoint)
    {
      intake.set(0.15);
      intakeMoving = true;
    }
    else if (intakeMoving && sensorPosition > minusSetpoint && sensorPosition < plusSetpoint)
    {
      intake.set(0);
      intakeMoving = false;
    }

    // flywheel encoder 
  }

  private static final String DefaultAuto = "Default";
  private static final String MiddleTwoNoteAuto = "Middle Two Note Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);

    flywheelEncoder.setPosition(0);

  }
  boolean speakerSequenceOver = false;
  double driveAngle;
  boolean autoPart2 = false;
  boolean autoPart3 = false;

  final double driveKP = 0.05;

  double driveSetpoint = 0;

  private double revTime = 0.0;
  private double endShoot = 0.0;

  @Override
  public void autonomousPeriodic() {
    
    double drivePosition = driveLeftEncoder.getDistance();

    double driveError = setpoint - drivePosition;

    double driveOutputSpeed = driveKP * driveError;             

    double driveAngle = navx.getAngle();



    switch (m_autoSelected) {
      case MiddleTwoNoteAuto:
    double flywheelPositionActive = flywheelEncoder.getPosition();
    boolean flywheels = true;
    
    driveRightA.set(driveOutputSpeed);
    driveRightB.set(driveOutputSpeed);

    if (flywheels) {
      double flywheelPosition = flywheelEncoder.getPosition();
      revTime = flywheelPosition + 150;
      endShoot = flywheelPosition + 250;
    }
    
    if (flywheelPositionActive < endShoot && flywheels) 
    {
      flywheelLeft.set(1);
      flywheelRight.set(0.95);
    }

    if (flywheelPositionActive > revTime && flywheels) 
    {
      intake.set(-0.25);
      
    }
    if (flywheelPositionActive > endShoot && flywheels) 
    {
      flywheelLeft.set(0);
      flywheelRight.set(0);
      intake.set(0);

      flywheels = false;
      speakerSequenceOver = true;
    }

    if (speakerSequenceOver)
    {
      driveSetpoint = 100;
      speakerSequenceOver = false;
      autoPart2 = true;
    }

    if (autoPart2 && drivePosition > 50) 
    {
      setpoint = 16.25;
    }

    if (autoPart2 && drivePosition > 75)
    {
      intake.set(0.35);
      autoPart2 = false;
      autoPart3 = true;
    }

    if (autoPart3 && drivePosition > 95)
    {
      driveSetpoint = 0;
      setpoint = 0.25;
    } 
    if (autoPart3 && drivePosition < 15)
    {
      flywheels = true;
    }


    if (speakerSequenceOver)



        break;
      case DefaultAuto:
      default:
        flywheelPositionActive = flywheelEncoder.getPosition();
        
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
        
    }
        break;
    }
  }

  @Override
  public void teleopInit() {

  }
  



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
    
    
  
    // intake
    if (driveControllerA.getLeftTriggerAxis() > 0.5 && !intakeActive)
    {
      intake.set(0.35);
      intakeActive = true;
    }
    else if (driveControllerA.getLeftTriggerAxis() < 0.5 && intakeActive)
    {
      intake.set(0);
      intakeActive = false;
    }

    // outake
    if (driveControllerA.getRightTriggerAxis() > 0.5 && !outakeActive) 
    {
      intake.set(-0.5);
      outakeActive = true;
    }
    else if (driveControllerA.getRightTriggerAxis() < 0.5 && outakeActive)
    {
      intake.set(0);
      outakeActive = false;
    }

    double flywheelPositionActive = flywheelEncoder.getPosition();

    if (driveControllerA.getRightBumper() && !buttonPressed) {
      double flywheelPosition = flywheelEncoder.getPosition();
      revTime = flywheelPosition + 150;
      endShoot = flywheelPosition + 250;

      buttonPressed = true;
    }
    
    if (flywheelPositionActive < endShoot) 
    {
      flywheelLeft.set(1);
      flywheelRight.set(0.95);
    }

    if (flywheelPositionActive > revTime && buttonPressed) 
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
    

    if (driveControllerB.getBButtonPressed())
    {
      intake.set(-0.15);
    }
    else if (driveControllerB.getBButtonReleased())
    {
      intake.set(0);
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
  intakePivot.set(0);

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
