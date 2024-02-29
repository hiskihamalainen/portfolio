package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains a test for saving and loading student info.
 */
public class SaveAndLoadTest {

    /**
     * Tests saving and loading student info.
     * @throws IOException issue reading file
     */
    @Test
    void testSaveAndLoad() throws IOException {
        Student student = new Student("Matti Meikäläinen", "123456ABC", 2020);

        try {
            student.setDegree(DegreeProgramme.fromAPI("otm-d729cfc3-97ad-467f-86b7-b6729c496c82", false));
        } catch (IOException e) {
            throw new RuntimeException("a");
        }

        student.saveToJson();

        String filename = System.getProperty("user.dir") + "/students/123456ABC.json";
        File file = new File(filename);

        assertTrue(file.exists() && !file.isDirectory());

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Rule.class, new AbstractClassAdapter());
        builder.registerTypeAdapter(DegreeModule.class, new AbstractClassAdapter());

        Gson gson = builder.create();

        FileReader reader = new FileReader(file);
        Student student1 = gson.fromJson(reader, Student.class);
        reader.close();

        assertEquals("Matti Meikäläinen", student1.getName());
        assertEquals("123456ABC", student1.getStudentNumber());

        assertTrue(file.delete());
    }
}
