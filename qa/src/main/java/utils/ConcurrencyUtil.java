package utils;

import exceptions.ConcurrencyException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrencyUtil {
  private static List<Future<Boolean>> invokeTasks(
      List<Callable<Boolean>> tasks, int concurrentThreadCount) throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(concurrentThreadCount);
    List<Future<Boolean>> results = executor.invokeAll(tasks);
    executor.shutdown();
    return results;
  }

  private static void validateConcurrentResults(
      List<Future<Boolean>> results, int expectedSuccessCount)
      throws ConcurrencyException, InterruptedException {
    int resultSuccessCounter = 0;
    for (int i = 0; i < results.size(); i++) {
      try {
        if (Boolean.TRUE.equals(results.get(i).get())) {
          resultSuccessCounter++;
        }
      } catch (ExecutionException e) {
        // do nothing
      }
    }
    if (resultSuccessCounter != expectedSuccessCount) {
      throw new ConcurrencyException(
          String.format(
              "Actual jobs passed : %s , Expected jobs to be passed : %s",
              resultSuccessCounter, expectedSuccessCount));
    }
  }

  public static void executeTasksConcurrently(
      Callable<Boolean> task, int concurrentThreadCount, int expectedSuccessCount)
      throws ConcurrencyException, InterruptedException {
    List<Callable<Boolean>> tasks = Collections.nCopies(concurrentThreadCount, task);
    List<Future<Boolean>> results = invokeTasks(tasks, concurrentThreadCount);
    validateConcurrentResults(results, expectedSuccessCount);
  }
}
