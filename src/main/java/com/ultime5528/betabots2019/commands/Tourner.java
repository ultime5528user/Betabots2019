/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.betabots2019.commands;

import com.ultime5528.betabots2019.robot.Constants;
import com.ultime5528.betabots2019.subsystems.BasePilotableOkto;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Tourner extends CommandBase {
  private BasePilotableOkto basePilotable;

  private TrapezoidProfile profile;

  private final TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(
      Math.toDegrees(Constants.Drive.MAX_TURN_RAD_PAR_SEC),
      Math.toDegrees(Constants.Drive.MAX_ACCEL_TURN_RAD_PAR_SEC2));
  private TrapezoidProfile.State goal;
  private TrapezoidProfile.State current = new TrapezoidProfile.State(0, 0);

  private Translation2d goalposition;

  public Tourner(BasePilotableOkto basePilotable, Translation2d goal) {
    this.basePilotable = basePilotable;
    this.goalposition = goal;
    var currentdistance = basePilotable.getTranslation()
    goal = new TrapezoidProfile.State(angle, 0);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {

    current.position = basePilotable.getAngle();
    current.velocity = basePilotable.getVitesseGyro();

    profile = new TrapezoidProfile(constraints, goal, current);

    TrapezoidProfile.State setpoint = profile.calculate(TimedRobot.kDefaultPeriod);

    basePilotable.oktoDrive(0, 0, Math.toRadians(setpoint.velocity));
  }

  @Override
  public boolean isFinished() {
    return Math.abs(goal.position - current.position) < Constants.Drive.ANGLE_THRESHOLD;
  }

  @Override
  public void end(boolean interrupted) {
    basePilotable.stop();
  }
}
