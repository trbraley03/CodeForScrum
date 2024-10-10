import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataWriter {

    // Path to the user JSON file
    private static final String USER_FILE_PATH = "data/json/users.json";

    /**
     * Method to write a new user to the JSON file. 
     * The method loads existing users, adds a new user, and writes the updated data back to the file.
     * @param userName The username of the user.
     * @param displayName The display name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     */
    public void writeUserData(String userName, String displayName, String email, String password) {
        // Load existing users from the JSON file
        List<String> usersData = loadUsersData(USER_FILE_PATH);
        
        // Create a new user in JSON-like format
        String newUser = createUserJson(userName, displayName, email, password);
        
        // Add the new user to the existing data
        usersData.add(newUser);
        
        // Write the updated data back to the file
        writeToFile(USER_FILE_PATH, usersData);
    }

    /**
     * Helper method to load existing users from the file.
     * @param filePath The path to the JSON file.
     * @return A list of user data strings.
     */
    private List<String> loadUsersData(String filePath) {
        List<String> usersData = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            // Extract users data if file already exists
            if (!content.isEmpty()) {
                usersData.add(content.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + filePath);
            e.printStackTrace();
        }
        return usersData;
    }

    /**
     * Create a JSON-like string for a new user.
     * @param userName The username of the user.
     * @param displayName The display name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A JSON-like string representation of the user.
     */
    private String createUserJson(String userName, String displayName, String email, String password) {
        String uuid = UUID.randomUUID().toString();
        return String.format("{\"uuid\":\"%s\",\"username\":\"%s\",\"displayName\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"progressTrackers\":[]}",
                             uuid, userName, displayName, email, password);
    }

    /**
     * General method for writing user data to a file.
     * @param fileName The name of the file to write to.
     * @param usersData A list of user data strings.
     */
    private void writeToFile(String fileName, List<String> usersData) {
        try (FileWriter file = new FileWriter(fileName, false)) { // 'false' to overwrite the file
            file.write("{\"users\":[\n"); // Start JSON object
            for (int i = 0; i < usersData.size(); i++) {
                file.write(usersData.get(i));
                if (i < usersData.size() - 1) {
                    file.write(",\n"); // Comma between users
                }
            }
            file.write("\n]}"); // End JSON object
            System.out.println("Successfully wrote user data to: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + fileName);
            e.printStackTrace();
        }
    }
}
