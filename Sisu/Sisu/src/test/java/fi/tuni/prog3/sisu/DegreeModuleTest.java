package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for the subclasses of DegreeModule
 */
class DegreeModuleTest {

    /**
     * Tests that Course is formed correctly
     * @throws IOException failure reading from API
     */
    @Test
    void testCourse() throws IOException {
        Course course = Course.fromAPI("otm-94ffcfc5-0db4-4507-b475-63f290639e04", false);
        assertEquals("MATH.MA.110", course.getCode());
        assertEquals(5, course.getMinCredits());
        assertEquals("uta-ykoodi-47926", course.getGroupId());
        assertEquals("Introduction to Analysis", course.getName());
    }

    /**
     * Tests that StudyModule is formed correctly
     * @throws IOException failure reading from API
     */
    @Test
    void testStudyModule() throws IOException {
        Module module = Module.fromAPI("otm-7cb70915-2d95-494b-8f7f-b081daccaf0a", false);

        assertEquals(StudyModule.class, module.getClass());

        StudyModule studyModule = (StudyModule) module;

        assertEquals("COMM-A01", studyModule.getCode());
        assertEquals(40, studyModule.getMinCredits());
        assertEquals("otm-7cb70915-2d95-494b-8f7f-b081daccaf0a", studyModule.getGroupId());
        assertEquals("Intermediate Studies in Communications Engineering", studyModule.getName());
        assertEquals(CompositeRule.class, studyModule.getRule().getClass());
        assertEquals(27, studyModule.getChildren().size());
    }

    /**
     * Tests that GroupingModule is formed correctly
     * @throws IOException failure reading from API
     */
    @Test
    void testGroupingModule() throws IOException {
        Module module = Module.fromAPI("otm-7ca282c4-4e88-4732-a3e3-573d35d33863", false);

        assertEquals(GroupingModule.class, module.getClass());

        GroupingModule groupingModule = (GroupingModule) module;

        assertEquals("", groupingModule.getCode());
        assertEquals(-1, groupingModule.getMinCredits());
        assertEquals("otm-7ca282c4-4e88-4732-a3e3-573d35d33863", groupingModule.getGroupId());
        assertEquals("Free Choice Study Modules", groupingModule.getName());
        assertEquals(CompositeRule.class, groupingModule.getRule().getClass());
        assertEquals(0, groupingModule.getChildren().size());
    }

    /**
     * Tests that DegreeProgramme is formed correctly
     * @throws IOException failure reading from API
     */
    @Test
    void testDegreeProgramme() throws IOException {
        Module module = Module.fromAPI("otm-d729cfc3-97ad-467f-86b7-b6729c496c82", false);

        assertEquals(DegreeProgramme.class, module.getClass());

        DegreeProgramme degreeProgramme = (DegreeProgramme) module;

        assertEquals("TSTK", degreeProgramme.getCode());
        assertEquals(180, degreeProgramme.getMinCredits());
        assertEquals("otm-fa02a1e7-4fe1-43e3-818b-810d8e723531", degreeProgramme.getGroupId());
        assertEquals("Bachelor's Programme in Computing and Electrical Engineering", degreeProgramme.getName());
        assertEquals(CompositeRule.class, degreeProgramme.getRule().getClass());
        assertEquals(2, degreeProgramme.getChildren().size());
    }
}