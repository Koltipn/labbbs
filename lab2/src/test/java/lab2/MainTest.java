package lab2;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testProgramRunsSuccessfully() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("2\n3\n".getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> Main.main(new String[]{}));

        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            fail("Программа зависла и не завершилась за 10 секунд");
        } finally {
            executor.shutdownNow();
        }

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertNotNull(output, "Вывод не должен быть null");
        assertFalse(output.isEmpty(), "Вывод не должен быть пустым");
        assertTrue(output.contains("finished calculating for"),
                "Ожидалось сообщение о завершении потоков");
    }

    @Test
    public void testCorrectNumberOfSteps() throws Exception {
        int threadCount = 2;
        int steps = 4;

        ByteArrayInputStream inputStream = new ByteArrayInputStream((threadCount + "\n" + steps + "\n").getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> Main.main(new String[]{}));

        try {
            future.get(10, TimeUnit.SECONDS);  // Ждём 10 секунд
        } catch (TimeoutException e) {
            future.cancel(true);
            fail("Программа зависла и не завершилась за 10 секунд");
        } finally {
            executor.shutdownNow();
        }

        System.setOut(originalOut);
        String output = outputStream.toString();

        Pattern pattern = Pattern.compile("Thread \\d+ \\(ID: \\d+\\): \\[(#+)]");
        Matcher matcher = pattern.matcher(output);
        int matchedThreads = 0;

        while (matcher.find()) {
            String progress = matcher.group(1);
            assertEquals(steps, progress.length(),
                    "Некорректное количество шагов: " + progress);
            matchedThreads++;
        }

        assertEquals(threadCount, matchedThreads, "Не все потоки выполнили нужное количество шагов");
    }
}
