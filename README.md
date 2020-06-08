# stormtrooper-streams
Apache Storm topology that recruits, dispatch &amp; find spies among Stormtroopers ;) Used as demo app during "The Storm Trooper Way of Processing Streams" presentation.

# How to build
Simply run mvn clean install and jar with your topology is ready to use.

# How to run a cluster on your local machine
1. Download Zookeeper: https://zookeeper.apache.org/releases.html
2. Extract it and copy conf/zoo_sample.cfg as conf/zoo.cfg or copy zoo.cfg from this repository cluster-config/zoo.cfg
to conf directory of your Zookeeper.
3.Run zookeeper, in your zookeeper directory go to bin and run:
 ```
./zkServer.sh start
```
It will run on 2181 port (default one).
4. Download Apache Storm 2.1.0: https://storm.apache.org/downloads.html
5. Extract it and copy storm.yaml from this repository cluster-config/storm.yaml to conf directory of your Apache Storm.
6. In your Apache Storm directory go to bin and run:
```
./storm nimbus
./storm supervisor
./storm-ui
```
7. You should be able to reach Storm UI on localhost:8081, or 8080 if you used default config.
8. Congrats! Your cluster is running :)

# How to deploy your topology
1. After building your topology & running a cluster go to bin directory of your Apache Storm and run:
```
./storm jar path-to-your-topology-jar.jar com.rauluka.stormtrooper.topology.Main
```
2. In your Apache Storm dir go to logs and you look through them. You should see logs similar to this:
```
2020-06-08 18:27:03.271 c.r.s.t.RecruitmentSpout pool-10-thread-1 [INFO] Number of trained Stormtroopers: 25822766
2020-06-08 18:27:04.085 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: 9788c5d4-041f-4566-bf9b-9243605a292d
2020-06-08 18:27:04.571 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: 6b189ef6-0934-4598-8400-d6fc75846150
2020-06-08 18:27:05.882 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: e8fc5373-ad8f-42b1-95cf-179565c65314
2020-06-08 18:27:08.084 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: 19c1c112-2f01-4836-b706-9c07573dcfa7
2020-06-08 18:27:08.732 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: d98705a4-8bc5-4af0-ae3b-e171d184c511
2020-06-08 18:27:12.177 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: 033912ca-8269-4b66-af97-38b30a9fe8d1
2020-06-08 18:27:12.782 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: b58a9899-3a0e-4a2c-b3ec-b16c38b76758
2020-06-08 18:27:13.271 c.r.s.t.RecruitmentSpout pool-10-thread-1 [INFO] Number of trained Stormtroopers: 26428274
2020-06-08 18:27:13.440 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: 7a66f39c-a849-485c-a70a-aa51f09ff35f
2020-06-08 18:27:15.816 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: a6c36e04-83e8-4f12-8400-53725d3d8c8a
2020-06-08 18:27:16.341 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: d0e7ecb0-9e37-4310-87b8-147c3cbd1a6c
2020-06-08 18:27:17.236 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: 35501f92-a0ae-4a33-b1c9-21fbf2df2d24
2020-06-08 18:27:21.549 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: 0d04ea50-f9be-477b-b779-692f7ddc3865
2020-06-08 18:27:22.279 c.r.s.t.RecruitmentSpout Thread-16-RecruitmentHeadquarters-executor[3, 3] [INFO] NOOOOOOOO! Alert! Alert! Spy id: f0cae4dd-d639-4cfb-84e4-64ad7d03d5a6
2020-06-08 18:27:23.271 c.r.s.t.RecruitmentSpout pool-10-thread-1 [INFO] Number of trained Stormtroopers: 26980545
```

# Story behind topology
Stormtroopers have a legendary ability to miss their targets. Topology consists of:
1. Spout that produces Stormtroopers. Every Stormtrooper has an academy grade (missed the whole point is the best grade) and preferred planet to patrol.
2. Stormtroopers go to TrainingDispatcherBolt where good students (who miss a lot) are dispatched to their preferred planet. Other students are randomly
dispatched to a planet. We have 3 planets available, so TrainingDispatcherBolt produces 3 streams, one for each planet.
3. Finally Stormtroopers land on a planet (TrainingMissionBolt) & they try to accomplish a mission. They try to hit the target 1000 times, if they miss enough
they accomplished mission, tuple is ack and Spout get ACK signal (Strormtrooper is successful). If they managed to hit the target it means
they may not be real Stormtroopers, they may be spies. Tuple is failed and Spout gets fail signal and runs an alarm (you can see it in logs).
4. Random parameters are set in such a way we should get millions of successful Stormtroopers & tens of spies ;)