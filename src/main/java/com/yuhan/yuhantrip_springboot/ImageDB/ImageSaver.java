package com.yuhan.yuhantrip_springboot.ImageDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImageSaver {
    public static void saveImage(String name, File imageFile) {
        String sql = "INSERT INTO image_data (name, image) VALUES (?, ?)";

        try (Connection conn = ImageDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(imageFile)) {

            pstmt.setString(1, name);
            pstmt.setBinaryStream(2, fis, (int) imageFile.length());
            pstmt.executeUpdate();

            System.out.println("Image saved to database.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File image = new File("C:\\Users\\user\\Desktop\\test_db\\test1.png"); // 저장할 이미지 경로 및 이름
        saveImage("MyImage", image);
    }
}
