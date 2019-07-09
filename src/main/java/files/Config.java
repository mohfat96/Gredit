package files;

import core.DataManager;
import core.Main;
import files.util.*;
import gui.sample.Element;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.image.WritableImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import javax.imageio.ImageIO;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is used to manage the Elements read out from the input-files
 * and correctly deliver them to the model.
 *
 * @author Simon Raffeck
 * @version 1.0
 * @since 25.04.18
 */

public class Config {
    private static final Logger logger = LogManager.getLogger();

    public List<Stave> staves = new ArrayList<>();
    public double checksum;

    /**
     * This method is used to properly initialize the Config-File read in via the DataManager.
     */
    public void init() {
        for (Stave stave : this.staves) {
            try {
                checkStave(stave);
                checkChecksum();
            } catch (InvalidConfigException e) {
                DataManager.getInstance().showErrorDialog(e.getMessage());
            }

            logger.info("Image for this stave can be found under {}", stave.getPath());
            for (Note note : stave.notes) {
                logger.info("Adding new Note at ({} / {})", note.getCoordinate().getX(), note.getCoordinate().getY());
            }
            for (Line line : stave.lines) {
                logger.info("Adding new Line at ({} / {}) with Width: {}", line.getStart(), line.getEnd(), line.getWidth());
            }
            for (Key key : stave.keys) {
                logger.info("Adding new Key at ({} / {})", key.getCoordinate().getX(), key.getCoordinate().getY());
            }
        }

    }

    public void checkChecksum() throws InvalidConfigException {
        try {
            double checksum = calculateChecksum();
            if (this.checksum != checksum) {
                throw new InvalidConfigException("Someone meddled with the Config");
            }
        } catch (NoSuchAlgorithmException e) {
            DataManager.getInstance().showErrorDialog("A Problem with the checkSum algortihm occured. Please contact the develloper!" +
                    "\n"+new InvalidConfigException(e.getMessage()).getMessage());
        }
    }

    public double calculateChecksum() throws NoSuchAlgorithmException {
        byte[] byteValues = new byte[0];
        for (Stave stave : staves) {
            byteValues = Convertable.concat(byteValues, stave.byteValue());
        }

        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(byteValues);
        byte[] hash = digest.digest();
        return ByteBuffer.wrap(hash).getDouble();
    }

    /**
     * This methods saves the current state of all opened tabs in a designated Yaml-File at the specified path
     * @param path Represents the path where the file will be saved
     * @throws IOException If the file path specifies can not be created
     * @throws NoSuchAlgorithmException If the MD5 algorithm used to calculate the checksum cannot be found
     */
    public void saveAll(String path) throws IOException, NoSuchAlgorithmException {
        this.staves= new ArrayList<>();
        for (Tab tab: DataManager.getInstance().getUsedTabs().getTabs()
             ) {
            this.staves.addAll(DataManager.getInstance().save(getFromTab("n",tab), getFromTab("l",tab), getFromTab("k",tab)));
        }
        logger.info("Content of staves: {}", this.staves.toString());
        this.checksum = calculateChecksum();
        if (path.equals("")) path = Main.resourcesPath + "/backUp.yaml";
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new Representer();
        representer.addClassTag(Config.class, Tag.MAP);
        Yaml yaml = new Yaml(representer, options);
        FileWriter writer = new FileWriter(path);
        yaml.dump(this, writer);
    }

    /**
     * This methods saves the current state of the selected tab in a designated Yaml-File at the specified path
     *
     * @param path Represents the path where the file will be saved
     * @throws IOException If the file path specifies can not be created
     * @throws NoSuchAlgorithmException If the MD5 algorithm used to calculate the checksum cannot be found
     */
    public void save(String path) throws IOException, NoSuchAlgorithmException {
        this.staves =DataManager.getInstance().save(getSelected("n"), getSelected("l"), getSelected("k"));
        logger.info("Content of staves: {}", this.staves.toString());
        this.checksum = calculateChecksum();
        if ("".equals(path)) path = Main.resourcesPath + "/backUp.yaml";
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new Representer();
        representer.addClassTag(Config.class, Tag.MAP);
        Yaml yaml = new Yaml(representer, options);
        FileWriter writer = new FileWriter(path);
        yaml.dump(this, writer);
    }

    private ArrayList<Element> getSelected(String type) {
        ArrayList<Element> selected = new ArrayList<>();
        switch (type) {
            case "n":
                for (Element note : DataManager.getInstance().getNotes()) {
                    if (note.getTab().isSelected()) {
                        selected.add(note);
                    }
                }
                return selected;
            case "l":
                for (Element line : DataManager.getInstance().getLines()) {
                    if (line.getTab().isSelected()) {
                        selected.add(line);
                    }
                }
                return selected;
            case "k":
                for (Element key : DataManager.getInstance().getKeys()) {
                    if (key.getTab().isSelected()) {
                        selected.add(key);
                    }
                }
                return selected;
        }
        return selected;
    }
    private ArrayList<Element> getFromTab(String type, Tab tab) {
        ArrayList<Element> selected = new ArrayList<>();
        switch (type) {
            case "n":
                for (Element note : DataManager.getInstance().getNotes()) {
                    if (note.getTab().equals(tab)) {
                        selected.add(note);
                    }
                }
                return selected;
            case "l":
                for (Element line : DataManager.getInstance().getLines()) {
                    if (line.getTab().equals(tab)) {
                        selected.add(line);
                    }
                }
                return selected;
            case "k":
                for (Element key : DataManager.getInstance().getKeys()) {
                    if (key.getTab().equals(tab)) {
                        selected.add(key);
                    }
                }
                return selected;
        }
        return selected;
    }

    /**
     * Exports the current state of the selected tab as png-Picture
     *
     * @param file File containing the snapchot created from the currently selected tab
     */
    public void export(File file) {
        Tab toSave = DataManager.getInstance().getUsedTabs().getSelectionModel().getSelectedItem();
        WritableImage image = toSave.getContent().snapshot(new SnapshotParameters(), null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            DataManager.getInstance().showErrorDialog("Exporting the Image has caused a Problem: \n" + e.getMessage());
        }
    }

    private void checkStave(Stave stave) throws InvalidConfigException {
        if (stave.lines.size() > 4) {
            throw new InvalidConfigException("A stave with more than 4 lines has been created, be advised");
        }
        if (stave.keys.size() > 1) {
            throw new InvalidConfigException("A stave with more than 1 key has been created, be advised");
        }
    }

    public List<Stave> getStaves() {
        return staves;
    }

    public void setStaves(List<Stave> staves) {
        this.staves = staves;
    }

    @Override
    public String toString() {
        return "Config{" +
                "staves=" + staves +
                ", checksum=" + checksum +
                '}';
    }
}
