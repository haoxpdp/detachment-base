package cn.detachment.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author haoxp
 * @date 20/11/3
 */
public class FileReaderUtil {

    public List<String> readLines(String path) throws IOException {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

}
