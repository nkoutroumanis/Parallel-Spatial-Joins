
for j in $(seq 1 1 5); do
    for i in 0.012 0.009 0.006 0.003; do
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessBruteForce --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} ROADS 960 1
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessPlaneSweepTwoEpsilon --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} ROADS 960 1
      spark-submit --class gr.ds.unipi.self.processing.GridTimeRealsPrunedCorrectnessPlaneSweep --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} ROADS 960 0.001 LPT1
      if [ "$i" != "0.012" ]; then
          spark-submit --class gr.ds.unipi.self.processing.stripes.GridTimeRealsPrunedCorrectnessPlaneSweepX --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} ROADS 960 0.001 LPT1
      fi
      spark-submit --class gr.ds.unipi.self.sedona.DistanceJoinReal --master yarn --deploy-mode client --jars=./libSedona/* --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} ROADS 276000000 960
      ./yarnState.sh || exit 1
    done
done

for j in $(seq 1 1 5); do
    for i in 0.012 0.009 0.006 0.003; do
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessBruteForce --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} LINEARWATER 960 1
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessPlaneSweepTwoEpsilon --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} LINEARWATER 960 1
      spark-submit --class gr.ds.unipi.self.processing.GridTimeRealsPrunedCorrectnessPlaneSweep --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} LINEARWATER 960 0.001 LPT1
      if [ "$i" != "0.012" ]; then
          spark-submit --class gr.ds.unipi.self.processing.stripes.GridTimeRealsPrunedCorrectnessPlaneSweepX --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} LINEARWATER 960 0.001 LPT1
      fi
      spark-submit --class gr.ds.unipi.self.sedona.DistanceJoinReal --master yarn --deploy-mode client --jars=./libSedona/* --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} LINEARWATER 278000000 960
      ./yarnState.sh || exit 1
    done
done

for j in $(seq 1 1 5); do
    for i in 0.012 0.009 0.006 0.003; do
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessBruteForce --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} AREAWATER 960 1
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessPlaneSweepTwoEpsilon --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} AREAWATER 960 1
      spark-submit --class gr.ds.unipi.self.processing.GridTimeRealsPrunedCorrectnessPlaneSweep --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} AREAWATER 960 0.001 LPT1
      if [ "$i" != "0.012" ]; then
          spark-submit --class gr.ds.unipi.self.processing.stripes.GridTimeRealsPrunedCorrectnessPlaneSweepX --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} AREAWATER 960 0.001 LPT1
      fi
      spark-submit --class gr.ds.unipi.self.sedona.DistanceJoinReal --master yarn --deploy-mode client --jars=./libSedona/* --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} AREAWATER 94000000 960
      ./yarnState.sh || exit 1
    done
done































for j in $(seq 1 1 5); do
    for i in 0.012 0.009 0.006 0.003; do
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessBruteForceCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} ROADS 960 1
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessPlaneSweepTwoEpsilonCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} ROADS 960 1
      spark-submit --class gr.ds.unipi.self.processing.GridTimeRealsPrunedCorrectnessPlaneSweepCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} ROADS 960 0.001 LPT1
      spark-submit --class gr.ds.unipi.self.processing.stripes.GridTimeRealsPrunedCorrectnessPlaneSweepXCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} ROADS 960 0.001 LPT1
      ./yarnState.sh || exit 1
    done
done

for j in $(seq 1 1 5); do
    for i in 0.012 0.009 0.006 0.003; do
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessBruteForceCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} LINEARWATER 960 1
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessPlaneSweepTwoEpsilonCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} LINEARWATER 960 1
      spark-submit --class gr.ds.unipi.self.processing.GridTimeRealsPrunedCorrectnessPlaneSweepCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} LINEARWATER 960 0.001 LPT1
      spark-submit --class gr.ds.unipi.self.processing.stripes.GridTimeRealsPrunedCorrectnessPlaneSweepXCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} LINEARWATER 960 0.001 LPT1
      ./yarnState.sh || exit 1
    done
done

for j in $(seq 1 1 5); do
    for i in 0.012 0.009 0.006 0.003; do
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessBruteForceCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} AREAWATER 960 1
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessPlaneSweepTwoEpsilonCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} AREAWATER 960 1
      spark-submit --class gr.ds.unipi.self.processing.GridTimeRealsPrunedCorrectnessPlaneSweepCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} AREAWATER 960 0.001 LPT1
      spark-submit --class gr.ds.unipi.self.processing.stripes.GridTimeRealsPrunedCorrectnessPlaneSweepXCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} AREAWATER 960 0.001 LPT1
      ./yarnState.sh || exit 1
    done
done

for j in $(seq 1 1 5); do
    for i in 0.012 0.009 0.006 0.003; do
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessBruteForceCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} BUILDINGS 960 1
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessPlaneSweepTwoEpsilonCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} BUILDINGS 960 1
      spark-submit --class gr.ds.unipi.self.processing.GridTimeRealsPrunedCorrectnessPlaneSweepCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} BUILDINGS 960 0.001 LPT1
      spark-submit --class gr.ds.unipi.self.processing.stripes.GridTimeRealsPrunedCorrectnessPlaneSweepXCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} BUILDINGS 960 0.001 LPT1
      ./yarnState.sh || exit 1
    done
done

for j in $(seq 1 1 5); do
    for i in 0.012 0.009 0.006 0.003; do
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessBruteForceCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} PARKS 960 1
      spark-submit --class gr.ds.unipi.self.mrdsj.GridTimeRealsPrunedCorrectnessPlaneSweepTwoEpsilonCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} PARKS 960 1
      spark-submit --class gr.ds.unipi.self.processing.GridTimeRealsPrunedCorrectnessPlaneSweepCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} PARKS 960 0.001 LPT1
      spark-submit --class gr.ds.unipi.self.processing.stripes.GridTimeRealsPrunedCorrectnessPlaneSweepXCounter --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 6g --num-executors 12 --conf spark.network.timeout=1000000s --conf spark.shuffle.io.maxRetries=10 --conf spark.kryoserializer.buffer.max=2047m --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms6g" --conf spark.executor.memoryOverhead=512m ~/Spatial-Joins-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/synth-skewed/pruned/ ${i} PARKS 960 0.001 LPT1
      ./yarnState.sh || exit 1
    done
done




