package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.User;
import java.io.*;

public class UserService {
    private final String userFile = "src/main/resources/data/users.txt";

    public void saveUser(User user) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true));
        writer.write(user.getFirstName() + "," + user.getLastName() + "," + user.getEmail() + "," +
                user.getGender() + "," + user.getNic() + "," + user.getAddress() + "," +
                user.getMobile() + "," + user.getPassword());
        writer.newLine();
        writer.close();
    }

    public User findUserByEmailAndPassword(String email, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(userFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] p = line.split(",");
            if (p.length == 8 && p[2].equals(email) && p[7].equals(password)) {
                reader.close();
                return new User(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
            }
        }
        reader.close();
        return null;
    }

    public User findUserByEmail(String email) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(userFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] p = line.split(",");
            if (p.length == 8 && p[2].equals(email)) {
                reader.close();
                return new User(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
            }
        }
        reader.close();
        return null;
    }
}
