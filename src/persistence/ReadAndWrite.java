package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadAndWrite {
    private final String path = "./src/files/";

    public ArrayList<String> readFile(String url) throws FileNotFoundException {
        ArrayList<String> scores = new ArrayList<>();
        File file = new File(path + url + ".txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            scores.add(data);
        }
        scanner.close();
        return scores;
    }

    public void writeFile(String url, ArrayList<String> text) throws IOException {
        FileWriter myWriter = new FileWriter(path + url + ".txt");
        for (String s : text) {
            myWriter.write(s + "\n");
        }
        myWriter.close();
    }
}
