package movement;

import core.Settings;
import core.SettingsError;
import core.Coord;
import input.WKTReader;

import movement.map.SimMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class StationaryPOI extends MapBasedMovement{

    /**
     * 从 `ArrayList` 中 按`okMapNodeTypes`的顺序从`MapBasedMovement.mapFile+index`中
     * 读取 initial position 的点。
     */
    private static List<Coord> poiLists;
    private ArrayList<Integer> okMapNodeTypes;

    public StationaryPOI(Settings settings){
        super(settings);
        StationaryPOI.poiLists = new LinkedList<Coord>();
        this.okMapNodeTypes = new ArrayList<Integer>();
        constructOKMap();
        readPois();
    }

    public StationaryPOI(StationaryPOI m){
        super(m);
        this.okMapNodeTypes = m.okMapNodeTypes;
    }

    private void constructOKMap(){
        Settings mbm_settings = new Settings(MAP_BASE_MOVEMENT_NS);
        int numofMapFiles = mbm_settings.getInt(NROF_FILES_S);

        // 如果设置了POI的okMaps，则从okMaps中读取节点信息，否则视为每个地图都ok
        int[] superOkMap = getOkMapNodeTypes();
        if (superOkMap != null) {
            for (int i : superOkMap) {
                okMapNodeTypes.add(i);
            }
        } else {
            for (int i = 1; i <= numofMapFiles; i++) {
                okMapNodeTypes.add(i);
            }
        }
    }
    private void readPois(){

        SimMap map = getMap();
        Coord offset = map.getOffset();

        Settings mbm_settings = new Settings(MAP_BASE_MOVEMENT_NS);
        WKTReader reader = new WKTReader();

        File poiFile = null;

        // 按照OkMap的顺序读取POI节点
        for (int index : okMapNodeTypes) {

            // read initial position of from ok map 'index'
            List<Coord> temp = new ArrayList<>();

            try{
                poiFile = new File(mbm_settings.getSetting(FILE_S + index));
                temp = reader.readPoints(poiFile);
            } catch (IOException e){
                throw new SettingsError("Couldn't read Initial Point data from file '" + poiFile);
            }

            if(temp.size() == 0){
                continue;
            }

            for (Coord c : temp) {
                if (map.isMirrored()) { // mirror initial point list if map data is also mirrored
                    c.setLocation(c.getX(), -c.getY()); // flip around X axis
                }
                // translate to match map data
                c.translate(offset.getX(), offset.getY());

                if((map.getNodeByCoord(c))==null){
                    throw new SettingsError("No MapNode in SimMap at location " + c );
                }

                if(!poiLists.contains(c)){
                    poiLists.add(c);
                }
            }
        }
    }


/******************************************************
 * Extend MovementModel
 ******************************************************/

    @Override
    public Coord getInitialLocation(){
        Coord initPos = new Coord(0, 0);

        if(poiLists.size()>0){
            // int index = rng.nextInt(poiLists.size());
            int index = 0;
            initPos = poiLists.get(index);
            poiLists.remove(index);
            this.lastMapNode = getMap().getNodeByCoord(initPos);
        }

        return initPos;
    }

    @Override
    public Path getPath(){
        Path p = new Path(0);
        Coord pos = lastMapNode.getLocation();
        p.addWaypoint(pos);
        return p;
    }

    @Override
    public StationaryPOI replicate(){
        return new StationaryPOI(this);
    }
}
