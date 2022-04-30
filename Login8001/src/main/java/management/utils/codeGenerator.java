package management.utils;

public class codeGenerator {
    public static String getlinkNo() {
        String linkNo = "";
        // 用字符数组的方式随机
        String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] m = model.toCharArray();
        for (int j = 0; j < 6; j++) {
            char c = m[(int) (Math.random() * 36)];
            linkNo = linkNo + c;
        }
        return linkNo;
    }
}
