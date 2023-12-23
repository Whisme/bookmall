package org.book.bookmall.utils;
import java.util.Random;
import java.util.UUID;
/**
 * ??id????
 * <p>Title: IDUtils</p>
 * <p>Description: </p>
 *
 */
public class IDUtils {
    /**
     * ?????
     */
    public static String genImageName() {
        //??????????????
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //???????
        Random random = new Random();
        int end3 = random.nextInt(999);
        //?????????0
        String str = millis + String.format("%03d", end3);

        return str;
    }
    /**
     * ??id??
     */
    public static long genItemId() {
        //??????????????
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //???????
        Random random = new Random();        int end2 = random.nextInt(99);
        //?????????0
        String str = millis + String.format("%02d", end2);
        long id = new Long(str);
        return id;
    }
    /**
     * ?????
     *
     * @return
     */
    public static String genOrderId() {
        //??????????????
        //long millis = System.currentTimeMillis();
        long millis = System.nanoTime();
        //???????
        Random random = new Random();
        int end2 = random.nextInt(99);
        //?????????0
        String str = millis + String.format("%02d", end2);
        return str;
    }

    public static String genShortUUID() {
        String[] split = UUID.randomUUID().toString().split("-");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(split[i]);
        }
        return sb.toString();
    }
}
