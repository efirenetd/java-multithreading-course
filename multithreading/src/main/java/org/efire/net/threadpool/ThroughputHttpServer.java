package org.efire.net.threadpool;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {

    public static final String INPUT_FILE = "word_book.txt";
    public static final int NUM_OF_THREADS = 1;

    public static void main(String[] args) throws IOException {
        final var classLoader = ThroughputHttpServer.class.getClassLoader();
        final var file = new File(classLoader.getResource(INPUT_FILE).getFile());
        final var text = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        startServer(text);
    }

    private static void startServer(String text) throws IOException {
        var httpServer = HttpServer.create(new InetSocketAddress(8000),0);
        httpServer.createContext("/search", new WordCountHandler(text));
        var executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        httpServer.setExecutor(executorService);
        httpServer.start();
    }

    private static class WordCountHandler implements HttpHandler {
        private String text;
        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //Sample HTTP Request - http://localhost:8000/search?word=who
            var keyValue = exchange.getRequestURI().getQuery().split("=");
            String action = keyValue[0];
            System.out.println(action);
            String word = keyValue[1];
            System.out.println(word);
            if (!action.equals("word")) {
                System.out.println("return 400");
                exchange.sendResponseHeaders(400, 0);
                return;
            }
            var count = countWord(word);
            var response = Long.toString(count).getBytes();
            exchange.sendResponseHeaders(200, response.length);

            try (var responseBody = exchange.getResponseBody()) {
                responseBody.write(response);
            }
        }

        private long countWord(String word) {
            long count = 0;
            int index = 0;
            while(index >=0) {
                index = text.indexOf(word, index);
                if (index >= 0) {
                    count++;
                    index++;
                }
            }
            return count;
        }
    }
}
