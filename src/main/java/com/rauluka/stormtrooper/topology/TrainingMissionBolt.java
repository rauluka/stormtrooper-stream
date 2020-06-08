package com.rauluka.stormtrooper.topology;

import com.rauluka.stormtrooper.model.Stormtrooper;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.Map;
import java.util.stream.IntStream;

public class TrainingMissionBolt implements IRichBolt {

  private OutputCollector outputCollector;

  @Override
  public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
    this.outputCollector = outputCollector;
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    // This is the end, no output fields.
  }

  @Override
  public void execute(Tuple tuple) {
    Stormtrooper stormtrooper = (Stormtrooper) tuple.getValueByField(FieldNames.STORMTROOPER_FIELD_NAME);
    if (completeMission(stormtrooper)) {
      outputCollector.ack(tuple);
    } else {
      outputCollector.fail(tuple);
    }
  }

  private boolean completeMission(Stormtrooper stormtrooper) {
    int numberOfMisses = IntStream.range(0, 1000).map(operand -> {
      if (stormtrooper.tryToMissTarget()) {
        return 1;
      }
      return 0;
    }).sum();

    return numberOfMisses >= numberOfMissesToAccomplishMission(stormtrooper);
  }

  private int numberOfMissesToAccomplishMission(Stormtrooper stormtrooper) {
    return stormtrooper.getAcademyGrade().getMissingRate() - stormtrooper.getAcademyGrade().getMargin();
  }

  @Override
  public void cleanup() {

  }

  @Override
  public Map<String, Object> getComponentConfiguration() {
    return null;
  }
}
