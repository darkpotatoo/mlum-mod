package me.darkpotatoo.mlumm.client.grf;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EscapeRoomLocator {
    // TODO: do this properly

    private HashMap<String, int[]> roomBounds = new HashMap<>();

    public void initRooms() {
        roomBounds.put("ex", new int[]{-10, -10, 10, 10}); // room named "ex" from coords x=-10, z=-10 to x=10, z=10
    }

    private String getRoomFromCoords(int[] coords) throws Exception {
        for (String room : roomBounds.keySet()) {
            int[] bounds = roomBounds.get(room);
            if (coords[0] >= bounds[0] && coords[0] <= bounds[2] && coords[1] >= bounds[1] && coords[1] <= bounds[3]) {
                return room;
            }
        }
        return "none";
    }

    public int[] getCoordsFromMessage(String message) {
        Matcher matcher = Pattern.compile("-?\\d+").matcher(message);
        int x = 0, y = 0, z = 0;
        int index = 0;

        while (matcher.find() && index < 3) {
            int value = Integer.parseInt(matcher.group());
            if (index == 0) x = value;
            else if (index == 1) y = value;
            else if (index == 2) z = value;
            index++;
        }
        return new int[] {x, y, z};
    }
}
