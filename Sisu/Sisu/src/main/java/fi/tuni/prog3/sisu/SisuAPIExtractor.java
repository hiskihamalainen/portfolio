package fi.tuni.prog3.sisu;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.util.Pair;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * A class which provides static methods for extracting data from the Sisu API.
 */
public class SisuAPIExtractor {

    /**
     * Return a list of DegreeProgramme names and ids from API.
     * @return list of DegreeProgramme names and ids.
     * @throws IOException failed to read data from API.
     */
    public static ArrayList<Pair<String, String>> getDegreeProgrammeIdsAndNames() throws IOException {

        URL url = new URL("https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");

        InputStreamReader reader = new InputStreamReader(url.openStream());
        Gson gson = new Gson();
        JsonObject jo = gson.fromJson(reader, JsonObject.class);
        JsonArray arr = jo.get("searchResults").getAsJsonArray();

        ArrayList<Pair<String, String>> res = new ArrayList<>();

        for (var elem : arr) {
            String id = elem.getAsJsonObject().get("id").getAsString();
            String name = elem.getAsJsonObject().get("name").getAsString();

            res.add(new Pair<>(id, name));
        }

        return res;
    }

    /**
     * Reads data from given url to a JsonObject.
     * @param urlString url to read from.
     * @param isSearch url returns a search result (which is an array).
     * @return a JsonObject obtained from given url.
     * @throws IOException failed to read data from API.
     */
    private static JsonObject getJsonObjectFromApi(String urlString, boolean isSearch) throws IOException {

        URL url = new URL(urlString);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        Gson gson = new Gson();

        if (!isSearch) {
            return gson.fromJson(reader, JsonObject.class);
        } else {
            return gson.fromJson(reader, JsonArray.class).get(0).getAsJsonObject();
        }

    }

    /**
     * Returns a JsonObject containing module data obtained from the API using a "normal" id.
     * @param id id of the module.
     * @return a JsonObject containing module data.
     * @throws IOException failed to read data from API.
     */
    public static JsonObject getModuleById(String id) throws IOException {

        return getJsonObjectFromApi("https://sis-tuni.funidata.fi/kori/api/modules/" + id, false);
    }

    /**
     * Returns a JsonObject containing module data obtained from the API using a groupId.
     * @param groupId groupId of the module.
     * @return a JsonObject containing module data.
     * @throws IOException failed to read data from API.
     */
    public static JsonObject getModuleByGroupId(String groupId) throws IOException {

        return getJsonObjectFromApi("https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=" + groupId + "&universityId=tuni-university-root-id", true);
    }

    /**
     * Returns a JsonObject containing course data obtained from the API using a "normal" id.
     * @param id id of the course.
     * @return a JsonObject containing course data.
     * @throws IOException failed to read data from API.
     */
    public static JsonObject getCourseUnitById(String id) throws IOException {

        return getJsonObjectFromApi("https://sis-tuni.funidata.fi/kori/api/course-units/" + id, false);
    }

    /**
     * Returns a JsonObject containing course data obtained from the API using a groupId.
     * @param groupId groupId of the course.
     * @return a JsonObject containing course data.
     * @throws IOException failed to read data from API.
     */
    public static JsonObject getCourseUnitByGroupId(String groupId) throws IOException {

        return getJsonObjectFromApi("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=" + groupId + "&universityId=tuni-university-root-id", true);
    }
}
