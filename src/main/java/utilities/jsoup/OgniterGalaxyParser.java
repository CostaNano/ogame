package utilities.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.database._HSQLDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarndt on 9/21/16.
 */
public class OgniterGalaxyParser {

    public static final String  UNIVERSE_REGX   = "{universe}",
                                GALAXY_REGX     = "{galaxy}",
                                SYSTEM_REGX     = "{system}";

    public static final String UNIVERSE_LINK = "http://en.ogniter.org/en/"+UNIVERSE_REGX+"/galaxy/"+GALAXY_REGX+"/"+SYSTEM_REGX;

    public void parseUniverse(int universe, int galaxy, int system) throws IOException {
        String link = getLink(universe,galaxy,system);
        Elements doc = Jsoup.connect(link).get().select("table").get(0).select("tr");

        List<String> columnNames = new ArrayList<>();
        List<PlanetPlayer> planetPlayers = new ArrayList<>();
        int index = 0;
        for(Element e : doc){
            if(index++ == 0)
                parseColumnNames(columnNames, e);
            else
                planetPlayers.add(new PlanetPlayer(e));
        }

        planetPlayers.forEach(a->insertPlanetPlayer(a));

    }

    private void insertPlanetPlayer(PlanetPlayer planetPlayer) {
        if(planetPlayer.getPlanetName() == null)
            return;
        if(planetPlayer.getPlanetName().trim().isEmpty())
            return;


        String planetQuery = null, playerQuery = null;
        try {
            playerQuery = "insert into player(player_name,alliance_name,player_link,alliance_link,player_rank,player_status)" +
                    " values("+
                            "'"+planetPlayer.getPlayerName()+"',"+
                            "'"+planetPlayer.getAllianceName()+"',"+
                            "'"+planetPlayer.getPlayerLink()+"',"+
                            "'"+planetPlayer.getAllianceLink()+"',"+
                            ""+planetPlayer.getPlayerRank()+","+
                            "'"+planetPlayer.getPlayerStatus()+
                    "');";


            _HSQLDB.executeQuery(playerQuery);

        } catch (Exception e){
            if(e.getMessage().contains("unique constraint")){
                playerQuery = "update player set " +
                            "alliance_name = " + "'"+planetPlayer.getAllianceName()+"',"+
                            "player_link = " + "'"+planetPlayer.getPlayerLink()+"',"+
                            "alliance_link = " + "'"+planetPlayer.getAllianceLink()+"',"+
                            "player_rank = " + ""+planetPlayer.getPlayerRank()+","+
                            "player_status = " + "'"+planetPlayer.getPlayerStatus()+"'"+
                                " WHERE player_name = '"+planetPlayer.getPlayerName()+"'"+
                        ";";


                try {
                    _HSQLDB.executeQuery(playerQuery);
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            else
                e.printStackTrace();

            System.out.println(playerQuery);
        }

        try {
            planetQuery = "insert into planet(player_name,planet_name,moon_name,moon_size,coordinates)" +
                            " values("+
                            "'"+planetPlayer.getPlayerName()+"',"+
                            "'"+planetPlayer.getPlanetName()+"',"+
                            "'"+planetPlayer.getMoonName()+"',"+
                            "'"+planetPlayer.getMoonSize()+"',"+
                            "'"+planetPlayer.getCoordinates().getCoordinates()+
                            "');";
            _HSQLDB.executeQuery(planetQuery);

        } catch (Exception e){
            if(e.getMessage().contains("unique constraint")){
                playerQuery = "update planet set " +
                        "planet_name = " + "'"+planetPlayer.getPlanetName()+"',"+
                        "moon_name = " + "'"+planetPlayer.getMoonName()+"',"+
                        "moon_size = " + "'"+planetPlayer.getMoonSize()+"',"+
                        "coordinates = " + "'"+planetPlayer.getCoordinates().getCoordinates()+
                            " WHERE player_name = '"+planetPlayer.getPlayerName()+
                                "' and planet_name = " + "'"+planetPlayer.getPlanetName()+"'"+
                        ";";


                try {
                    _HSQLDB.executeQuery(playerQuery);
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            else
                e.printStackTrace();
            System.out.println(planetQuery);
        }
    }

    private void parseColumnNames(List<String> columnNames, Element e) {
        for(Element el : e.select("th"))
            columnNames.add(el.text());
    }

    private String getLink(int universe, int galaxy, int system) {
        return UNIVERSE_LINK.replace(UNIVERSE_REGX,universe+"")
                            .replace(GALAXY_REGX,galaxy+"")
                            .replace(SYSTEM_REGX,system+"");
    }

}