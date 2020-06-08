package com.rauluka.stormtrooper.topology;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

import static com.rauluka.stormtrooper.model.Planet.*;

public class Main {
  private static final String RECRUITMENT_HEADQUARTERS_SPOUT_ID = "RecruitmentHeadquarters";
  private static final String TRAINING_DISPATCHER_BOLT_ID = "TrainingDispatcher";
  private static final String TATOOINE_MISSION_BOLT_ID = "TatooineTrainingMission";
  private static final String HOTH_MISSION_BOLT_ID = "HothTrainingMission";
  private static final String ENDOR_MISSION_BOLT_ID = "EndorTrainingMission";
  private static final String STORMTROOPER_MANAGEMENT_TOPOLOGY_NAME = "StormtrooperManagementTopology";
  private static final int TRAINING_MISSION_PARALLELISM_HINT = 1;

  public static void main(String[] args) {
    TopologyBuilder topologyBuilder = new TopologyBuilder();

    topologyBuilder.setSpout(RECRUITMENT_HEADQUARTERS_SPOUT_ID, new RecruitmentSpout());

    topologyBuilder.setBolt(TRAINING_DISPATCHER_BOLT_ID, new TrainingDispatcherBolt())
        .shuffleGrouping(RECRUITMENT_HEADQUARTERS_SPOUT_ID);

    topologyBuilder.setBolt(TATOOINE_MISSION_BOLT_ID, new TrainingMissionBolt(), TRAINING_MISSION_PARALLELISM_HINT)
        .shuffleGrouping(TRAINING_DISPATCHER_BOLT_ID, TATOOINE.name());
    topologyBuilder.setBolt(HOTH_MISSION_BOLT_ID, new TrainingMissionBolt(), TRAINING_MISSION_PARALLELISM_HINT)
        .shuffleGrouping(TRAINING_DISPATCHER_BOLT_ID, HOTH.name());
    topologyBuilder.setBolt(ENDOR_MISSION_BOLT_ID, new TrainingMissionBolt(), TRAINING_MISSION_PARALLELISM_HINT)
        .shuffleGrouping(TRAINING_DISPATCHER_BOLT_ID, ENDOR.name());

    StormTopology topology = topologyBuilder.createTopology();

    Config conf = new Config();
    conf.setNumWorkers(2);
    try {
      StormSubmitter.submitTopology(STORMTROOPER_MANAGEMENT_TOPOLOGY_NAME, conf, topology);
    } catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
      e.printStackTrace();
    }

  }
}
