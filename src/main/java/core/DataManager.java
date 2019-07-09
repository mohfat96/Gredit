package core;

import files.Config;
import files.util.*;
import gui.sample.AlertWindow;
import gui.sample.Element;
import gui.sample.LineWithHandles;
import gui.sample.NoteKey;

import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swp.omr.InvalidPictureMatrixException;

import swp.omr.Recognition;
import swp.omr.RecognitionResult;
import swp.omr.lines.LineObject;
import swp.omr.recognition.struct.Node;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * This class is used to manage all data currently present in opened projects
 * @author Simon Raffeck
 * @version 1.0
 */



public class DataManager {
    private static final Logger logger = LogManager.getLogger();
    private static DataManager INSTANCE;
    private Config config;
    private TabPane usedTabs;
    private static Recognition recognition;
    private RecognitionResult result;
    private List<Stave> configStaves = new ArrayList<>();
    private double imageX=0;
    private double imageY=0;
    private ArrayList<Element> notes=new ArrayList<>();
    private ArrayList<Element> lines=new ArrayList<>();
    private ArrayList<Element> keys=new ArrayList<>();
    private HashMap<Image,String> paths = new HashMap<>();

    /**
     * Provides access to the used Instance of DataManager
     * @return The current Instance of the Datamanager
     */
    public static DataManager getInstance() {
        return INSTANCE;
    }

    /**
     * Constructor for a new Datamanager Instance
     * @param configFile Path to the used Config-File or Picture
     */
    public DataManager(Path configFile) {
        recognition = new Recognition();
        if (INSTANCE != null) {
            throw new IllegalStateException("Duplicate Initialization");
        }
        INSTANCE = this;
        loadConfigfromPath(configFile);

    }

    /**
     * Constructor for a new Datamanager Instance, with an empty Config. Used especially for catching Exceptions during the Initialization
     */
    public DataManager(){
        recognition = new Recognition();
        if (INSTANCE != null) {
            throw new IllegalStateException("Duplicate Initialization");
        }
        INSTANCE = this;
        this.config=new Config();
    }

    /**
     * Initializes a Config-File from the specified path
     * @param configFile Path of the Config-File
     */
    public void loadConfigfromPath(Path configFile){
        String path = configFile.toAbsolutePath().toString();

        if (path.endsWith(".yml") || path.endsWith(".yaml")) {
            logger.info("Using the Project found at {}", configFile.toAbsolutePath().toString());
            try {
                this.config = ConfigParser.parseYamlFile(configFile);
                setUpStaves();
            }catch(Exception e){
                showErrorDialog("The config you tried to load has caused a problem: \n" +
                        e.getMessage());
            }
        } else {
            try {
                result = recognition.recognize(path);
                this.config = buildConfigFromResult(result, path);
                setUpStaves();
                logger.info("--------Done loading-----");
                logger.error(this.config.toString());
            } catch (InvalidPictureMatrixException e) {
                showErrorDialog("The picture you tried to load has caused a problem: \n" +
                        e.getMessage());
            }
        }

    }
    private void setUpStaves(){
        configStaves.addAll(config.staves);
        for (Stave stave:configStaves
             ) {
            stave.setImage();
            paths.put(stave.getImage(),stave.path);
        }
    }

    private Config buildConfigFromResult(RecognitionResult result, String image) {
        Config config = new Config();
        List<Node> noteResult = result.nodes();
        List<LineObject> lineResult = result.lines();
        Optional<Node> keyResult = result.key();
        logger.info("Content of Result: Notes:{} | Lines:{}  | Keys:{}",noteResult.size(),lineResult.size(),keyResult.isPresent());
        ArrayList<Note> notes = new ArrayList<>();
        for (Node n : noteResult
                ) {
            Note note = new Note();
            logger.info("Analyzing note: top:{} left:{} right:{} bottom:{}",n.top(),n.left(),n.right(),n.bottom());
            Coordinate coord = new Coordinate();
            coord.setX((n.left()+n.right()) / 2);
            coord.setY((n.top()+n.bottom()) / 2);
            note.setCoordinate(coord);
            notes.add(note);

        }
        ArrayList<Line> lines = new ArrayList<>();
        for (LineObject l : lineResult
                ) {
            Line line = new Line();
            Coordinate end = new Coordinate();
            end.setX(l.getEnd().x);
            end.setY(l.getEnd().y);
            Coordinate start = new Coordinate();
            start.setX(l.getStart().x);
            start.setY(l.getStart().y);
            line.setEnd(end);
            line.setStart(start);
            line.setWidth(end.getX() - start.getX());
            lines.add(line);
        }
        ArrayList<Key> keys = new ArrayList<>();
        if (keyResult.isPresent()) {
            Node k = keyResult.get();
            Key key = new Key();
            Coordinate coord = new Coordinate();
            coord.setX((k.left()+k.right()) / 2);
            coord.setY((k.top()+k.bottom()) / 2);
            key.setCoordinate(coord);
            keys.add(key);
        }
        ArrayList<Stave> staves = new ArrayList<>();
        Stave stave = new Stave();
        stave.setNotes(notes);
        stave.setLines(lines);
        stave.setKeys(keys);
        stave.setPath(image);
        staves.add(stave);
        config.setStaves(staves);
        return config;
    }

    public Config getConfig() {
        return config;
    }

    public TabPane getUsedTabs() {
        return usedTabs;
    }

    public void setUsedTabs(TabPane usedTabs) {
        this.usedTabs = usedTabs;
    }

    public void showErrorDialog(String message){
        AlertWindow.error(message);
    }
    private Note convertToConfig(gui.sample.Note tabNote){
        Note configNote = new Note();
        Coordinate coord = new Coordinate();
        coord.setX(tabNote.getX()-imageX+gui.sample.Note.getNoteWidth()/2);
        coord.setY(tabNote.getY()-imageY+gui.sample.Note.getNoteHeight()/2);
        configNote.setCoordinate(coord);
        return configNote;
    }
    private Line convertToConfig(LineWithHandles tabLine){
        Line configLine = new Line();
        Coordinate start = new Coordinate();
        start.setX(tabLine.getStartX()-imageX);
        start.setY(tabLine.getStartY()-imageY);
        Coordinate end = new Coordinate();
        end.setX(tabLine.getEndX()-imageX);
        end.setY(tabLine.getEndY()-imageY);
        configLine.setStart(start);
        configLine.setEnd(end);
        configLine.setWidth(end.getX()-start.getX());
        logger.info("Creating line with: {}",configLine.toString());
        return configLine;
    }
    private Key convertToConfig(gui.sample.NoteKey tabKey){
        Key configKey = new Key();
        Coordinate coord = new Coordinate();
        coord.setX(tabKey.getX()-imageX + NoteKey.getWidth()/2);
        coord.setY(tabKey.getY()-imageY + NoteKey.getHeight()/2);
        configKey.setCoordinate(coord);
        configKey.setType(tabKey.getConfigType());
        logger.info("Key created with {}",configKey.getCoordinate().toString());
        return configKey;
    }

    /**
     * Saves the current state of all Elements in all tabs
     */
    public void saveAll(){
        save(this.notes,this.lines,this.keys);
    }

    /**
     * Saves the current state of the selected Tab
     */
    public ArrayList<Stave> save(ArrayList<Element> notes,ArrayList<Element> lines,ArrayList<Element> keys){
        setUpStaves();
        ArrayList<Stave> result = new ArrayList<>();
        Stave stave = new Stave();
        ArrayList<Note> configNotes = new ArrayList<>();
        ArrayList<Line> configLines = new ArrayList<>();
        ArrayList<Key> configKeys = new ArrayList<>();
        for (Element e :notes
                ) {
            configNotes.add(addNotesFromTabs((gui.sample.Note)e,e.getImage()));
        }
        for (Element e :lines
                ) {
            configLines.add(addLinesFromTabs((gui.sample.LineWithHandles)e,e.getImage()));
        }
        for (Element e :keys
                ) {
            configKeys.add(addKeysFromTabs((gui.sample.NoteKey)e,e.getImage()));
        }
        stave.setNotes(configNotes);
        stave.setLines(configLines);
        stave.setKeys(configKeys);
        stave.setPath(paths.get(notes.get(0).getImage()));
        result.add(stave);
        return result;
    }

    private Note addNotesFromTabs(gui.sample.Note note, Image image){
        Note con = convertToConfig(note);
        //sortIntoStaves(con,image,"n");
        return con;
    }
    private Line addLinesFromTabs(LineWithHandles line, Image image){
        Line con = convertToConfig(line);
        //sortIntoStaves(con,image,"l");
        return con;
    }
    private Key addKeysFromTabs(gui.sample.NoteKey key, Image image){
        Key con = convertToConfig(key);
        //sortIntoStaves(con,image,"k");
        return con;
    }
    public void remove(Element element){
        if (element instanceof gui.sample.Note){
            this.notes.remove(element);
        }
        if (element instanceof LineWithHandles){
            this.lines.remove(element);
        }
        if (element instanceof NoteKey){
            this.keys.remove(element);
        }
    }

    private void sortIntoStaves(Object con,Image image,String type){
        for (Stave stave:configStaves
             )
            if (checkImages(stave.getImage(), image)) {
                switch (type) {
                    case "n":
                        if (!stave.notes.contains(con)) {
                            stave.notes.add((Note) con);
                        }
                        return;
                    case "l":
                        if (!stave.lines.contains(con)) {
                            stave.lines.add((Line) con);
                        }
                        return;
                    case "k":
                        if (!stave.keys.contains(con)) {
                            stave.keys.add((Key) con);
                        }
                        return;
                }
                return;
            }

    }

    public List<Stave> getConfigStaves() {
        return configStaves;
    }
    private boolean checkImages(Image image1, Image image2){
        for (int i = 0; i < image1.getWidth(); i++)
        {
            for (int j = 0; j < image1.getHeight(); j++)
            {
                if (image1.getPixelReader().getArgb(i, j) != image2.getPixelReader().getArgb(i, j)) return false;
            }
        }
        return true;
    }

    public void setImageX(double imageX) {
        this.imageX = imageX;
    }

    public void setImageY(double imageY) {
        this.imageY = imageY;
    }

    public ArrayList<Element> getNotes() {
        return notes;
    }

    public ArrayList<Element> getLines() {
        return lines;
    }

    public ArrayList<Element> getKeys() {
        return keys;
    }
}
