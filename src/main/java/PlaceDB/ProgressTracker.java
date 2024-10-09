package PlaceDB;

import java.io.*;
// 데이터 가져오던중 중간에 멈출경우를 위한 코드
public class ProgressTracker {
    private static final String PROGRESS_FILE = "progress.dat";

    public static int loadCurrentIndex() {
        File file = new File(PROGRESS_FILE);
        if (!file.exists()) {
            return 0; // 파일이 없으면 처음부터 시작
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            return dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void saveCurrentIndex(int index) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(PROGRESS_FILE))) {
            dos.writeInt(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
