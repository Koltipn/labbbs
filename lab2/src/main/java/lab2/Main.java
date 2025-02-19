package lab2;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number threads: ");
        int threadCount = scanner.nextInt();
        System.out.print("Enter long calculate (number of steps): ");
        int calculationLength = scanner.nextInt();

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Random random = new Random();

        for (int i = 0; i < threadCount; i++) {
            int threadNumber = i + 1;
            executor.execute(() -> {
                long threadId = Thread.currentThread().getId();
                long startTime = System.currentTimeMillis();
                StringBuilder progressBar = new StringBuilder("[");

                for (int j = 0; j < calculationLength; j++) {
                    progressBar.append("#");
                    if (j + 1 == calculationLength) {
                        progressBar.append("]");
                    }
                    System.out.printf("Thread %d (ID: %d): %s%n", threadNumber, threadId, progressBar);
                    try {
                        Thread.sleep(100 + random.nextInt(200));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                long endTime = System.currentTimeMillis();
                System.out.printf("Thread %d (ID: %d) finished calculating for %d ms%n",
                        threadNumber, threadId, (endTime - startTime));
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
