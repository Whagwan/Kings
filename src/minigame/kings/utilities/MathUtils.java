package minigame.kings.utilities;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MathUtils {

    public static boolean isInteger(Object object) {
        try {
            Integer.parseInt(object.toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isDouble(Object object) {
        try {
            Double.parseDouble(object.toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }


    static public Random random = new Random();

    static public final int random(int range) {
        return random.nextInt(range + 1);
    }

    static public final int random(int start, int end) {
        return start + random.nextInt(end - start + 1);
    }

    static public final boolean randomBoolean() {
        return random.nextBoolean();
    }

    static public final boolean randomBoolean(float chance) {
        return MathUtils.random() < chance;
    }

    static public final float random() {
        return random.nextFloat();
    }

    static public final float random(float range) {
        return random.nextFloat() * range;
    }

    static public final float random(float start, float end) {
        return start + random.nextFloat() * (end - start);
    }

    public static boolean probability(int percentage) {

        Random i = new Random();
        int value = 1;

        int val = value + i.nextInt(100);
        List<Integer> list = new ArrayList<>();

        for (int x = 1; x <= percentage; x++) {
            list.add(x);
        }

        if (list.contains(val)) {
            return true;
        }

        return false;
    }

}
