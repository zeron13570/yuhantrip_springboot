package com.yuhan.yuhantrip_springboot.ImageDB;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageLoader {
    public static void loadImage(int id, String outputPath) throws IOException {
        String sql = "SELECT image FROM image_data WHERE id = ?";

        try (Connection conn = ImageDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                try (InputStream is = rs.getBinaryStream("image");
                     FileOutputStream fos = new FileOutputStream(outputPath)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, bytesRead);
                    }

                    System.out.println("Image loaded from database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            loadImage(1, "C:\\Users\\user\\Desktop\\test_db\\ttttt\\image.jpg"); // 저장한 이미지 불러올 경로 및 이름
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
