package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for SisuAPIExtractor
 */
class SisuAPIExtractorTest {

    /**
     * Tests getDegreeProgrammeIdsAndNames()
     */
    @Test
    void getDegreeProgrammeIds() {
        try {
            var ids = SisuAPIExtractor.getDegreeProgrammeIdsAndNames();
            assertTrue(ids.size() > 200);
            assertEquals("otm-87fb9507-a6dd-41aa-b924-2f15eca3b7ae", ids.get(0).getKey());
            assertEquals("otm-aaa48da1-8f02-47f6-9db6-e0710ae32cd7", ids.get(39).getKey());

        } catch (IOException e) {
            fail("Failed to obtain DegreeProgramme ids");
        }

    }

    /**
     * Tests getting module by id.
     */
    @Test
    void getModuleById() {

        String id1 = "otm-316ac8bf-ff36-4ec0-8997-617976500368";
        String id2 = "otm-a31e4a68-73e1-48c3-a7dd-4efe3c258f86";


        try {
            var a = SisuAPIExtractor.getModuleById(id1);
            var d = SisuAPIExtractor.getModuleById(id2);

            assertEquals(id1, a.get("id").getAsString());
            assertEquals(id2, d.get("id").getAsString());

            var ids = SisuAPIExtractor.getDegreeProgrammeIdsAndNames();
            Collections.shuffle(ids);

            for (var pair : ids.subList(0, 10)) {
                assertEquals(pair.getKey(), SisuAPIExtractor.getModuleById(pair.getKey()).get("id").getAsString());
            }

        } catch (IOException e) {
            fail("failed to read JSON object");
        }

        assertThrows(IOException.class, () -> SisuAPIExtractor.getModuleById("invalidID"));
    }

    /**
     * Tests getting module by group id
     */
    @Test
    void getModuleByGroupId() {

        String id1 = "otm-316ac8bf-ff36-4ec0-8997-617976500368";
        String id2 = "otm-aaa48da1-8f02-47f6-9db6-e0710ae32cd7";
        String id3 = "otm-21df9be6-1e32-4f5b-992f-3e3771fc56ef";
        String id4 = "tut-dp-g-1163";
        String id5 = "tut-dp-g-1105";


        try {
            var a = SisuAPIExtractor.getModuleByGroupId(id1);
            var b = SisuAPIExtractor.getModuleByGroupId(id2);
            var c = SisuAPIExtractor.getModuleByGroupId(id3);
            var d = SisuAPIExtractor.getModuleByGroupId(id4);
            var e = SisuAPIExtractor.getModuleByGroupId(id5);

            assertEquals(id1, a.get("groupId").getAsString());
            assertEquals(id2, b.get("groupId").getAsString());
            assertEquals(id3, c.get("groupId").getAsString());
            assertEquals(id4, d.get("groupId").getAsString());
            assertEquals(id5, e.get("groupId").getAsString());

        } catch (IOException e) {
            fail("failed to read JSON object");
        }

        assertThrows(IOException.class, () -> SisuAPIExtractor.getModuleByGroupId("invalidID"));
    }

    /**
     * Tests getting course unit by id.
     */
    @Test
    void getCourseUnitById() {

        String id1 = "otm-94ffcfc5-0db4-4507-b475-63f290639e04";
        String id2 = "otm-c96d77b3-4eef-4df1-8bd2-4ceece0d48c7";
        String id3 = "otm-b5b6ab3e-4525-4d11-a67b-02b685fb49e4";
        String id4 = "otm-d59a2ea6-51f3-4bc2-8457-64ebd605f675";
        String id5 = "otm-9809534b-8ccf-4cf2-82fc-6c4717417fbe";

        try {
            var a = SisuAPIExtractor.getCourseUnitById(id1);
            var b = SisuAPIExtractor.getCourseUnitById(id2);
            var c = SisuAPIExtractor.getCourseUnitById(id3);
            var d = SisuAPIExtractor.getCourseUnitById(id4);
            var e = SisuAPIExtractor.getCourseUnitById(id5);

            assertEquals(id1, a.get("id").getAsString());
            assertEquals(id2, b.get("id").getAsString());
            assertEquals(id3, c.get("id").getAsString());
            assertEquals(id4, d.get("id").getAsString());
            assertEquals(id5, e.get("id").getAsString());

        } catch (IOException e) {
            fail("failed to read JSON object");
        }

        assertThrows(IOException.class, () -> SisuAPIExtractor.getCourseUnitById("invalidID"));
    }

    /**
     * Tests getting course unit by group id.
     */
    @Test
    void getCourseUnitByGroupId() {

        String id1 = "uta-ykoodi-47926";
        String id2 = "tut-cu-g-45460";
        String id3 = "tut-cu-g-36355";
        String id4 = "tut-cu-g-48243";
        String id5 = "otm-9809534b-8ccf-4cf2-82fc-6c4717417fbe";

        try {
            var a = SisuAPIExtractor.getCourseUnitByGroupId(id1);
            var b = SisuAPIExtractor.getCourseUnitByGroupId(id2);
            var c = SisuAPIExtractor.getCourseUnitByGroupId(id3);
            var d = SisuAPIExtractor.getCourseUnitByGroupId(id4);
            var e = SisuAPIExtractor.getCourseUnitByGroupId(id5);

            assertEquals(id1, a.get("groupId").getAsString());
            assertEquals(id2, b.get("groupId").getAsString());
            assertEquals(id3, c.get("groupId").getAsString());
            assertEquals(id4, d.get("groupId").getAsString());
            assertEquals(id5, e.get("groupId").getAsString());

        } catch (IOException e) {
            fail("failed to read JSON object");
        }

        assertThrows(IOException.class, () -> SisuAPIExtractor.getCourseUnitByGroupId("invalidID"));
    }
}