package hagelbrand.draftlighting.util;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class TeamColorUtil {
    public Map<String, Integer> color = new HashMap<>();

    public Map<String, Integer> getPrimaryColorByTeamName(String teamName) {
        System.out.println(teamName.toLowerCase());
        switch(teamName.toLowerCase()) {
            case "ari":
            case "atl":
            case "kc":
            case "sf":
            case "tb":
                color.put("hue", 0);
                color.put("saturation", 100);
                break;
            case "bal":
            case "min":
                color.put("hue", 74);
                color.put("saturation", 100);
                break;
            case "buf":
            case "lar":
            case "nyg":
                color.put("hue", 61);
                color.put("saturation", 100);
                break;
            case "car":
            case "det":
            case "hou":
            case "lac":
                color.put("hue", 56);
                color.put("saturation", 100);
                break;
            case "chi":
                color.put("hue", 69);
                color.put("saturation", 100);
                break;
            case "cin":
            case "cle":
            case "den":
                color.put("hue", 10);
                color.put("saturation", 95);
                break;
            case "gb":
            case "nyj":
            case "phi":
                color.put("hue", 35);
                color.put("saturation", 100);
                break;
            case "ind":
                color.put("hue", 65);
                color.put("saturation", 100);
                break;
            case "jax":
                color.put("hue", 53);
                color.put("saturation", 100);
                break;
            case "lv":
                break;
            case "mia":
                color.put("hue", 45);
                color.put("saturation", 99);
                break;
            case "ne":
            case "sea":
                color.put("hue", 58);
                color.put("saturation", 100);
                break;
            case "no":
                color.put("hue", 11);
                color.put("saturation", 44);
                break;
            case "pit":
            case "wsh":
                color.put("hue", 12);
                color.put("saturation", 100);
                break;
            case "ten":
            case "dal":
                color.put("hue", 58);
                color.put("saturation", 67);
                break;
            default:
                color.put("hue", 0);
                color.put("saturation", 0);
        }
        return color;
    }

}
