package com.rauluka.stormtrooper.topology;

import com.rauluka.stormtrooper.model.AcademyGrade;
import com.rauluka.stormtrooper.model.Planet;
import com.rauluka.stormtrooper.model.Stormtrooper;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import static com.rauluka.stormtrooper.model.AcademyGrade.MISSED_THE_WHOLE_POINT;
import static com.rauluka.stormtrooper.model.AcademyGrade.MISS_BY_A_MILE;

public class TrainingDispatcherBolt implements IRichBolt {
  private OutputCollector outputCollector;
  private HashSet<AcademyGrade> goodStudents;
  private Random assignmentCommission;

  @Override
  public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
    this.outputCollector = outputCollector;
    this.goodStudents = new HashSet<>(Arrays.asList(MISSED_THE_WHOLE_POINT, MISS_BY_A_MILE));
    this.assignmentCommission = new Random();
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    // Create an output stream of Stormtroopers for each planet.
    Arrays.stream(Planet.values())
        .forEach(planet ->
            outputFieldsDeclarer.declareStream(planet.name(), new Fields(FIELD_NAMES.STORMTROOPER_FIELD_NAME)));
  }

  @Override
  public void execute(Tuple tuple) {
    Stormtrooper stormtrooper = (Stormtrooper) tuple.getValueByField(FIELD_NAMES.STORMTROOPER_FIELD_NAME);
    Planet planet = assignTrainingPlanet(stormtrooper);

    outputCollector.emit(planet.name(), tuple, new Values(stormtrooper));
    outputCollector.ack(tuple);
  }

  private Planet assignTrainingPlanet(Stormtrooper stormtrooper) {
    AcademyGrade academyGrade = stormtrooper.getAcademyGrade();
    Planet preferredPlanet = stormtrooper.getPreferredPlanet();

    if (goodStudents.contains(academyGrade)) {
      return preferredPlanet;
    }

    Planet[] missionTargets = Planet.values();

    return missionTargets[assignmentCommission.nextInt(missionTargets.length)];
  }

  @Override
  public void cleanup() {

  }

  @Override
  public Map<String, Object> getComponentConfiguration() {
    return null;
  }
}
