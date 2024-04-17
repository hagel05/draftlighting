package hagelbrand.draftlighting.service;

import hagelbrand.draftlighting.clients.ESPNSportsCoreClient;
import hagelbrand.draftlighting.clients.SmartThingsClient;
import hagelbrand.draftlighting.config.ESPNConfig;
import hagelbrand.draftlighting.config.SmartThingsConfig;
import hagelbrand.draftlighting.model.PickResponse;
import hagelbrand.draftlighting.model.espn.Draft.Draft;
import hagelbrand.draftlighting.model.espn.Draft.Picks;
import hagelbrand.draftlighting.model.espn.Draft.Rounds;
import hagelbrand.draftlighting.model.espn.Team.Team;
import hagelbrand.draftlighting.util.TeamColorUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
@Service
public class DraftLightingService {
    // TODO: it would be better to have a context to pass around, but this works for now
    ESPNConfig espnConfig;
    SmartThingsConfig smartThingsConfig;
    public DraftLightingService(ESPNConfig espnConfig, SmartThingsConfig smartThingsConfig) {
        this.espnConfig = espnConfig;
        this.smartThingsConfig = smartThingsConfig;
    }
    @Async
    public void simulateDraft(int year, ArrayList<String> deviceIds) throws IOException, InterruptedException {
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        SmartThingsClient smartThingsClient = new SmartThingsClient(smartThingsConfig);
        TeamColorUtil teamColorUtil = new TeamColorUtil();
        int overallPick = 1;
        PickResponse pick = espnSportsCoreClient.getSpecificOverallPick(year, overallPick);
        while(pick != null) {
            String teamabv = pick.getTeam().getAbbreviation();
            System.out.println(teamabv);
            if(teamabv.equalsIgnoreCase("lv")) {
                smartThingsClient.turnOff();
            } else {
                Map<String, Integer> color = teamColorUtil.getPrimaryColorByTeamName(teamabv);
                for (String device : deviceIds) {
                    smartThingsClient.setColor(color.get("hue"), color.get("saturation"), device);
                }
            }
            Thread.sleep(5000);
            overallPick++;
            pick = espnSportsCoreClient.getSpecificOverallPick(year, overallPick);
        }
    }

    @Async
    public void startDraft(int year, ArrayList<String> deviceIds) throws IOException, InterruptedException {
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        SmartThingsClient smartThingsClient = new SmartThingsClient(smartThingsConfig);
        PickResponse lastOnTheClockPick = null;
        String teamabv = "";
        PickResponse lastPick = espnSportsCoreClient.getLastPick(year);
        while (lastPick.getPick().getStatus().getName().equalsIgnoreCase("ON_THE_CLOCK")) {
            PickResponse onTheClockPick = espnSportsCoreClient.getOnTheClockPick(year);
            if(!onTheClockPick.equals(lastOnTheClockPick)) {
                lastOnTheClockPick = onTheClockPick;
                teamabv = lastOnTheClockPick.getTeam().getAbbreviation();
                TeamColorUtil teamColorUtil = new TeamColorUtil();
                Map<String, Integer> color = teamColorUtil.getPrimaryColorByTeamName(teamabv);
                for (String device : deviceIds) {
                    smartThingsClient.setColor(color.get("hue"), color.get("saturation"), device);
                }
            }
            Thread.sleep(5000);
            lastPick = espnSportsCoreClient.getLastPick(year);
        }
    }

    public void setLightToOnTheClockTeamColor(int year) throws IOException, InterruptedException {
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        PickResponse pick = espnSportsCoreClient.getOnTheClockPick(year);
        // TODO: Make this take an actual team url to just call the api
        Team team = pick.getTeam();
        System.out.println(team.displayName);
        SmartThingsClient smartThingsClient = new SmartThingsClient(smartThingsConfig);
        Color color = new Color(Integer.parseInt(team.getColor(), 16));
        int intValue = Integer.parseInt(team.getColor(), 16);

        // Convert RGB to HSL
        float[] hslValues = new float[3];
        Color.RGBtoHSB((intValue >> 16) & 0xFF, (intValue >> 8) & 0xFF, intValue & 0xFF, hslValues);

        System.out.println(hslValues[0] + " " + hslValues[1]);
        // smartThingsClient.setColor(Math.round(hslValues[0] * 100), Math.round(hslValues[1] * 100));
        smartThingsClient.setColor(69, 100, "088796e1-4072-4528-a854-1a1d8b9c097d");
    }

    public void setLightToPickColor(int year, int overallPick) throws IOException, InterruptedException {
        // TODO:  This isn't entirely functionally correct.  The Zigbee spec discusses how to convert this to the correct values.
        /**
         * From those docs: This cluster provides an interface for changing the color of a light. Color is specified according to the
         * Commission Internationale de l'Éclairage (CIE) specification CIE 1931 Color Space, [I1]. Color control is
         * carried out in terms of x,y values, as defined by this specification.
         **/
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        // TODO: Make this take an actual team url to just call the api
        Team team = espnSportsCoreClient.getTeamForSpecificPick(year, overallPick);
        System.out.println(team.displayName);
        SmartThingsClient smartThingsClient = new SmartThingsClient(smartThingsConfig);
        Color color = new Color(Integer.parseInt(team.getColor(), 16));
        int intValue = Integer.parseInt(team.getColor(), 16);

        // Convert RGB to HSL
        float[] hslValues = new float[3];
        Color.RGBtoHSB((intValue >> 16) & 0xFF, (intValue >> 8) & 0xFF, intValue & 0xFF, hslValues);

        System.out.println(hslValues[0] + " " + hslValues[1]);
        float red = (float) (color.getRed() / 255.0);
        float green = (float) (color.getGreen() / 255.0);
        float blue = (float) (color.getBlue() / 255.0);
        red = (red > 0.04045f) ? (float) Math.pow((red + 0.055f) / (1.0f + 0.055f), 2.4f) : (red / 12.92f);
        green = (green > 0.04045f) ? (float) Math.pow((green + 0.055f) / (1.0f + 0.055f), 2.4f) : (green / 12.92f);
        blue = (blue  > 0.04045f) ? (float) Math.pow((blue  + 0.055f) / (1.0f + 0.055f), 2.4f) : (blue  / 12.92f);
        float X = red * 0.649926f + green * 0.103455f + blue * 0.197109f;
        float Y = red * 0.234327f + green * 0.743075f + blue * 0.022598f;
        float Z = red * 0.0000000f + green * 0.053077f + blue * 1.035763f;

        float x = X / (X + Y + Z);

        float y = Y / (X + Y + Z);

        System.out.println(x + " " + y);


        System.out.println(color.getRed() / 255.0 + " " +  color.getGreen() / 255.0 + " " + color.getBlue() / 255.0);
        float hue = hslValues[0] / (hslValues[0] + hslValues[1] + hslValues[2]);
        float sat = hslValues[0] / (hslValues[0] + hslValues[1] + hslValues[2]);
        System.out.println(hue + " " + sat);
        // TODO: Figure out what to actually set these values as
        // smartThingsClient.setColor(Math.round(hslValues[0] * 100), Math.round(hslValues[1] * 100));
        smartThingsClient.setColor(69, 100, "088796e1-4072-4528-a854-1a1d8b9c097d");
    }

}