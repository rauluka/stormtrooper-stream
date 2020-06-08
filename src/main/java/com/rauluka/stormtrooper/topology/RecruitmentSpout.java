package com.rauluka.stormtrooper.topology;

import com.rauluka.stormtrooper.model.Stormtrooper;
import com.rauluka.stormtrooper.model.StormtrooperAcademy;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RecruitmentSpout implements IRichSpout {
  private static final Logger LOGGER = LoggerFactory.getLogger(RecruitmentSpout.class);

  private StormtrooperAcademy stormtrooperAcademy;
  private SpoutOutputCollector outputCollector;
  private AtomicInteger successfulStormtroopersCounter;
  private ScheduledExecutorService reportsExecutor;

  @Override
  public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
    this.outputCollector = spoutOutputCollector;
    this.stormtrooperAcademy = new StormtrooperAcademy();
    this.successfulStormtroopersCounter = new AtomicInteger(0);

    this.reportsExecutor = Executors.newSingleThreadScheduledExecutor();
    reportsExecutor.scheduleAtFixedRate(() -> LOGGER.info("Number of trained Stormtroopers: {}",
        successfulStormtroopersCounter.get()), 1, 10, TimeUnit.SECONDS);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declare(new Fields(FIELD_NAMES.STORMTROOPER_FIELD_NAME));
  }

  @Override
  public void nextTuple() {
    Stormtrooper brandNewStormtrooper = stormtrooperAcademy.educateStormtrooper();
    outputCollector.emit(new Values(brandNewStormtrooper), brandNewStormtrooper.getId());
  }

  @Override
  public void ack(Object o) {
    UUID stormTrooperId = (UUID) o;
    // Stormtrooper trained successfully!
    successfulStormtroopersCounter.incrementAndGet();
  }

  @Override
  public void fail(Object o) {
    // Stormtrooper training failed.
    UUID stormTrooperId = (UUID) o;
    LOGGER.info("NOOOOOOOO! Alert! Alert! Spy id: {}", stormTrooperId);
  }

  @Override
  public void close() {
    reportsExecutor.shutdown();
  }

  @Override
  public void activate() {

  }

  @Override
  public void deactivate() {

  }

  @Override
  public Map<String, Object> getComponentConfiguration() {
    return null;
  }
}
