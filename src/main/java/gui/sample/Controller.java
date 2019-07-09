package gui.sample;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import core.DataManager;
import core.Main;
import files.util.Stave;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.geometry.*;
import model.CoreUtils;
import model.NoteName;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML
    public ToggleButton lineBtn;
    public ToggleButton noteBtn;
    public ToggleButton noteKeyBtn;
    public ToggleButton moveBtn;
    public ColorPicker colorPicker;
    public Tooltip t1 = new Tooltip("Insert Line");
    public Tooltip t2 = new Tooltip("Insert Note");
    public Tooltip t3 = new Tooltip("Insert Notekey");
    public Tooltip t4 = new Tooltip("Change Color");
    public MenuItem saveAllMenu;
    public MenuItem saveMenu;
    public MenuItem exportMenu;
    public MenuItem closeMenu;
    public MenuItem closeAllMenu;
    public MenuItem insertLineMenu;
    public MenuItem insertNoteMenu;
    public MenuItem insertKeyMenu;
    public MenuItem changeColorMenu;
    public MenuItem deleteMenu;
    public ContextMenu notekeyChooser;
    public MenuItem cKey;
    public MenuItem fKey;
    public MenuItem shortcutsMenuItem;
    public MenuItem tutorialMenuItem;
    private Tooltip t5 = new Tooltip("Move/Edit Element");
    private Tooltip t6 = new Tooltip("Delete Selected Elements");
    private Tooltip t7 = new Tooltip("Reload Transcription");
    public MenuItem openMenu;
    public AnchorPane sidePane;
    public CheckBox notesSelect;
    public Button trashBtn;
    public Button refresh;
    public CheckBox linesSelect;
    public CheckBox keysSelect;
    public TabPane tabPane;
    public TextField searchTextField;
    public TextField transcription;
    private Selection selected = new Selection();
    private Container lineContainer = new Container();
    final ArrayList<Element> lines = new ArrayList<>();
    private Container noteKeyContainer = new Container();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataManager.getInstance().setUsedTabs(tabPane);
        initializeConfigElements();
        lineBtn.setTooltip(t1);
        noteBtn.setTooltip(t2);
        noteKeyBtn.setTooltip(t3);
        moveBtn.setTooltip(t5);
        trashBtn.setTooltip(t6);
        colorPicker.setTooltip(t4);
        refresh.setTooltip(t7);
        LineWithHandles.setMoveBtn(moveBtn, sidePane);

        searchTextField.setStyle(".textfield");

        searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                //change color to normal and leave
                searchTextField.setStyle(".textfield");
                for (Element e : DataManager.getInstance().getNotes())
                    ((Note) e).undoTemporaryColorization();
                return;
            }
            try {
                searchTextField.setStyle(".textfield");
                //Mark these Notes as found
                NoteName noteToFind = NoteName.getNoteName(newValue);
                List<Note> notes = DataManager.getInstance().getNotes().stream().map(e-> (Note) e)
                        .filter(note -> note.getTab().isSelected()).collect(Collectors.toList());
                for (Note note : notes)
                    if (note.getName().equals(noteToFind))
                        note.colorizeTemporarily(Color.GREEN);
            } catch (Exception exception) {
                searchTextField.setStyle("-fx-background-color: red; -fx-inner-text-color: white;");
                for (Element e : DataManager.getInstance().getNotes())
                    ((Note) e).undoTemporaryColorization();
                AlertWindow.error("Either the stave is in a incorrect format or the search term is incorrect!\nCannot determine note names!");
            }
        }));

        createTranscriptionText();

        transcription.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!transcription.isFocused())
                return;


            if (newValue == null || newValue.isEmpty()) {
                transcription.setStyle(".textfield");
                return;
            }
            String[] input = newValue.trim().split(" ");

            for (String s : input) {
                try {
                    transcription.setStyle(".textfield");
                    NoteName.getNoteName(s);
                } catch (Exception e) {
                    transcription.setStyle("-fx-background-color: red; -fx-inner-text-color: white;");
                    return;
                }
            }

            List<Note> notes = DataManager.getInstance().getNotes().stream().map((e) -> (Note) e)
                    .filter(note -> note.getTab().isSelected())
                    .sorted(Comparator.comparingDouble(Note::getX)).collect(Collectors.toList());
            notes.forEach(Note::undoTemporaryColorization);
            if (notes.size() != input.length) {
                transcription.setStyle("-fx-background-color: red; -fx-inner-text-color: white;");
                return;
            }

            for (int i = 0; i < notes.size(); i++) {
                if (!notes.get(i).getName().equals(NoteName.getNoteName(input[i]))) {
                    CoreUtils.correctNotePosition(notes.get(i), NoteName.getNoteName(input[i]));
                }
            }

        }));

        noteBtn.setOnAction(event -> {

            unselectAll();

            tabPane.setCursor(Cursor.CROSSHAIR);
            lineBtn.setSelected(false);
            noteKeyBtn.setSelected(false);
            moveBtn.setSelected(false);
        });

        lineBtn.setOnAction(event -> {
            unselectAll();

            tabPane.setCursor(Cursor.CROSSHAIR);
            noteBtn.setSelected(false);
            noteKeyBtn.setSelected(false);
            moveBtn.setSelected(false);
        });

        noteKeyBtn.setOnAction(event -> {

            unselectAll();

            tabPane.setCursor(Cursor.CROSSHAIR);
            noteBtn.setSelected(false);
            lineBtn.setSelected(false);
            moveBtn.setSelected(false);
            if (noteKeyBtn.isSelected()) {
                notekeyChooser.show(sidePane, null, noteKeyBtn.getWidth() + 5, noteKeyBtn.getLayoutY());
            }
        });
        moveBtn.setOnAction(event -> {
            tabPane.setCursor(Cursor.DEFAULT);
            noteBtn.setSelected(false);
            noteKeyBtn.setSelected(false);
            lineBtn.setSelected(false);
        });
        openMenu.setOnAction(event -> { // create new Tab
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            fileChooser.setTitle("Open Project");
            FileChooser.ExtensionFilter ymlFormat = new FileChooser.ExtensionFilter("Yaml Files (*.yml)", "*.yml");
            FileChooser.ExtensionFilter jpgFormat = new FileChooser.ExtensionFilter("JPG Files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter pngFormat = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");

            fileChooser.getExtensionFilters().add(pngFormat);
            fileChooser.getExtensionFilters().add(ymlFormat);
            fileChooser.getExtensionFilters().add(jpgFormat);


            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            try {
                String path = file.getPath();
                DataManager.getInstance().loadConfigfromPath(Paths.get(path));
                initializeConfigElements();

            } catch (Exception e) {
                if (e.getMessage() == null){
                    AlertWindow.error("You either didn't choose a file or the file you've chosen is not supported");
                }
                else {
                    AlertWindow.error(e.getMessage());
                }
            }
        });
        closeMenu.setOnAction((ActionEvent event) -> { //close menu handler

            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            if (tab != null) {
                EventHandler<Event> handler = tab.getOnCloseRequest();
                if (null != handler) {
                    handler.handle(null);
                }

            }
        });
        saveAllMenu.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Project");
            FileChooser.ExtensionFilter ymlFormat = new FileChooser.ExtensionFilter("Yaml Files (*.yml)", "*.yml");
            fileChooser.getExtensionFilters().add(ymlFormat);
            File file = fileChooser.showSaveDialog(Main.primaryStage);
            String path = "";
            if (file != null) path = file.getPath();
            if (!path.endsWith(".yml")) path = path + ".yml";
            try {
                DataManager.getInstance().getConfig().saveAll(path);
            } catch (IOException e) {
                DataManager.getInstance().showErrorDialog("Saving all Tabs has caused a problem: \n" + e.getMessage());
                e.printStackTrace();
            }catch(NoSuchAlgorithmException ne){
                DataManager.getInstance().showErrorDialog("Saving all Tabs has caused a problem: \n" + ne.getMessage());
                ne.printStackTrace();
            }
        });
        saveMenu.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Tab");
            FileChooser.ExtensionFilter ymlFormat = new FileChooser.ExtensionFilter("Yaml Files (*.yml)", "*.yml");
            fileChooser.getExtensionFilters().add(ymlFormat);
            File file = fileChooser.showSaveDialog(Main.primaryStage);
            String path = "";
            if (file != null) path = file.getPath();
            if (!path.endsWith(".yml")) path = path + ".yml";
            try {
                DataManager.getInstance().getConfig().save(path);
            } catch (IOException e) {
                DataManager.getInstance().showErrorDialog("Saving the current Tab has caused a problem: \n" + e.getMessage());
                e.printStackTrace();

            }catch(NoSuchAlgorithmException ne){
                DataManager.getInstance().showErrorDialog("Saving all Tabs has caused a problem: \n" + ne.getMessage());
                ne.printStackTrace();
            }
        });
        exportMenu.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Epo Project");
            FileChooser.ExtensionFilter pngFormat = new FileChooser.ExtensionFilter("png Files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(pngFormat);
            File file = fileChooser.showSaveDialog(Main.primaryStage);
            String path = "";
            if (file != null) path = file.getPath();
            if (!path.endsWith(".png")) path = path + ".png";
            file = new File(path);
            DataManager.getInstance().getConfig().export(file);
        });
        closeAllMenu.setOnAction(event -> {
            ObservableList<Tab> tabs = tabPane.getTabs();
            int size = tabs.size();
            while (size > 0) {
                Tab tab = tabs.get(0);
                EventHandler<Event> handler = tab.getOnCloseRequest();
                if (null != handler) {
                    handler.handle(null);
                }

                size--;
            }
        });
        insertNoteMenu.setOnAction(event -> {
            if (!noteBtn.isSelected()) {
                noteBtn.fire();
            }
        });
        insertLineMenu.setOnAction(event -> {
            if (!lineBtn.isSelected()) {
                lineBtn.fire();
            }
        });
        insertKeyMenu.setOnAction(event -> {
            if (!noteKeyBtn.isSelected()) {
                noteKeyBtn.fire();
            }
        });
        changeColorMenu.setOnAction(event -> {
            colorPicker.show();
        });
        deleteMenu.setOnAction(event -> {
            trashBtn.fire();
        });
        colorPicker.setOnAction(event -> { //color picker
            if (selected != null && !selected.isEmpty()) {
                for (Element node : selected) {
                    node.setColor(colorPicker.getValue());
                }
            }
        });
        notesSelect.setOnAction(event -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                AnchorPane anchorPane = (AnchorPane) ((ScrollPane) selectedTab.getContent()).getContent();
                if (notesSelect.isSelected()) {
                    for (Node node : anchorPane.getChildren()) {
                        if (node instanceof Note && !selected.contains((Note) node)) {
                            ((Note) node).select();
                            selected.add((Note) node);
                        }
                    }
                } else {
                    for (Node node : anchorPane.getChildren()) {
                        if (node instanceof Note) {
                            ((Note) node).unselect();
                            selected.remove((Note) node);
                        }
                    }
                }
            }
        });
        linesSelect.setOnAction(event -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                AnchorPane anchorPane = (AnchorPane) ((ScrollPane) selectedTab.getContent()).getContent();
                if (linesSelect.isSelected()) {
                    for (Node node : anchorPane.getChildren()) {
                        if (node instanceof LineWithHandles && !selected.contains((LineWithHandles) node)) {
                            ((LineWithHandles) node).select();
                            selected.add((LineWithHandles) node);
                        }
                    }
                } else {
                    for (Node node : anchorPane.getChildren()) {
                        if (node instanceof LineWithHandles) {
                            ((LineWithHandles) node).unselect();
                            selected.remove((LineWithHandles) node);
                        }
                    }
                }
            }
        });

        keysSelect.setOnAction(event -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {

                if (keysSelect.isSelected()) {
                    if (!noteKeyContainer.isEmpty() && !selected.contains(noteKeyContainer.get())) {
                        noteKeyContainer.get().select();
                        selected.add(noteKeyContainer.get());
                    }
                } else {
                    if (!noteKeyContainer.isEmpty()) {
                        noteKeyContainer.get().unselect();
                        selected.remove(noteKeyContainer.get());
                    }
                }


            }
        });
        refresh.setOnAction(event -> {
            DataManager.getInstance().getNotes().forEach(element -> ((Note)element).undoTemporaryColorization());
            createTranscriptionText();
        });


        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    notesSelect.setSelected(false);
                    linesSelect.setSelected(false);
                    keysSelect.setSelected(false);
                    createTranscriptionText();

                    for (Element node : selected) {
                        node.unselect();
                    }
                    selected.clear();
                    noteKeyContainer.clear();
                    lines.clear();// changing the tab will clear the lines list so it keeps tracking the number of lines.
                    if (t1 != null) { // if there is no new opened tab
                        ScrollPane scrollPane = (ScrollPane) t1.getContent();
                        AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
                        for (Node node : anchorPane.getChildren()) {
                            if (node instanceof LineWithHandles) {
                                lines.add((LineWithHandles) node);
                            }
                            if (node.getStyle().equals("class:Notekey")) {
                                noteKeyContainer.set(NoteKey.getNoteKey((Shape) node));
                            }
                        }
                    }
                }
        );
        trashBtn.setOnAction(event -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                ScrollPane scrollPane = (ScrollPane) selectedTab.getContent();
                AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
                if (!selected.isEmpty()) {
                    for (Element node : selected) {
                        if (node instanceof NoteKey) {
                            noteKeyContainer.clear();
                        }
                        node.delete(lines); //the delete method will clear the lines list as well

                    }
                    selected.clear();
                }
                notesSelect.setSelected(false);
                linesSelect.setSelected(false);
                keysSelect.setSelected(false);
            }
        });


        cKey.setOnAction(event -> {
            NoteKey.setType('c');
        });

        fKey.setOnAction(event -> {
            NoteKey.setType('f');
        });


        shortcutsMenuItem.setOnAction(event -> {
            AlertWindow.info("You can speed up your workflow by using the following keyboard shortcuts:\n" ,
                    "Open a file : Alt + O \n" +
                    "Save all tabs: Alt + Shift+ S\n" +
                    "Save : Alt + S\n" +
                    "Export: Alt + E\n" +
                    "Close tab: Alt + C\n" +
                    "Close all tabs: Alt + Shift + C\n" +
                    "Trash Tool: Delete Button\n" +
                    "Multi. Select: Hold Ctrl button down while selecting with Cursor Tool" , null);
        });

        tutorialMenuItem.setOnAction(event -> {
            ArrayList<Node> nodes = new ArrayList<>();
            String url1 = "https://gitlab2.informatik.uni-wuerzburg.de/softwarepraktikum/SWP18SS_OMR-Postcorrection/blob/master/README.md";
            String url2 = "https://gitlab2.informatik.uni-wuerzburg.de/softwarepraktikum/SWP18SS_OMR-Postcorrection/wikis/usermanual";
            Hyperlink hyperlink = new Hyperlink(url1);
            Hyperlink hyperlink1 = new Hyperlink(url2);
            final WebView browser = new WebView();
            final WebEngine webEngine = browser.getEngine();


            hyperlink.setOnAction(event1 -> {
                openWebpage(url1);
            });

            hyperlink1.setOnAction(event1 -> {
                openWebpage(url2);
            });

            nodes.add(hyperlink);
            nodes.add(hyperlink1);
            AlertWindow.info("For help open the user manual file or the readme file from the following links", "" , nodes);

        });


    }

    public static void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTranscriptionText() {
        StringBuilder builder = new StringBuilder();
        List<Note> notes = DataManager.getInstance().getNotes().stream().map((e) -> (Note) e)
                .filter(note -> note.getTab().isSelected())
                .sorted(Comparator.comparingDouble(Note::getX)).collect(Collectors.toList());
        try {
            for (Note note : notes) {
                builder.append(note.getName());
                builder.append(" ");
            }
            transcription.setStyle(".textfield");
        } catch (Exception e) {
            builder = new StringBuilder(" ");
            AlertWindow.error(e.getMessage());
        }
        transcription.setText(builder.toString().trim());
    }

    /**
     * Initializes all Elememnts represented in the currently read out Config-File
     *
     * @author Simon Raffeck
     */
    private void initializeConfigElements() {
        List<Stave> staves = DataManager.getInstance().getConfig().getStaves();
        for (Stave stave : staves
                ) {
            Tab tab = createTabFromConfig(stave.getImage(), stave);
            tab.setText(new File(stave.path).getName());
            tabPane.getTabs().add(tab);
        }
        DataManager.getInstance().setUsedTabs(tabPane);
    }

    /**
     * Creates a new tab with all Elements represented in the current Config-File's current stave
     *
     * @param image Image used to read out all Elements, also identifier for the stave
     * @param stave the current stave to be read out
     * @return A new Tab
     * @author Simon Raffeck
     */
    private Tab createTabFromConfig(Image image, Stave stave) {

        Tab tab = new Tab("Notesheet " + (tabPane.getTabs().size() + 1));
        tab.setOnCloseRequest(event -> {
            AlertWindow.Pressed pressed =
                    AlertWindow.confirm("Close " + tab.getText(), "Do you want to saveAll your progress before closing?");
            if (pressed == AlertWindow.Pressed.Yes) {
                saveAllMenu.fire();
                selected.clear();
                lineContainer.clear();
                noteKeyContainer.clear();
                tabPane.getTabs().remove(tab);
            } else if (pressed == AlertWindow.Pressed.No) {
                selected.clear();
                lineContainer.clear();
                noteKeyContainer.clear();
                tabPane.getTabs().remove(tab);
            } else {
                if (event != null) event.consume();
            }
        });

        AnchorPane anchorPane = new AnchorPane();
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();


        anchorPane.setMinHeight(0);
        anchorPane.setMinWidth(0);
        anchorPane.setPrefHeight(image.getHeight() + 160);
        anchorPane.setPrefWidth(image.getWidth() + 160);
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);

        imageView.setPreserveRatio(true);

        anchorPane.getChildren().add(imageView);
        anchorPane.setTopAnchor(imageView, 80.0);
        anchorPane.setLeftAnchor(imageView, 80.0);
        anchorPane.setBottomAnchor(imageView, 80.0);
        anchorPane.setRightAnchor(imageView, 80.0);


        scrollPane.setContent(anchorPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        anchorPane.layout();
        anchorPane.applyCss();
        scrollPane.layout();
        scrollPane.applyCss();
        tab.setContent(scrollPane);

        double imageX = imageView.getLayoutX();
        double imageY = imageView.getLayoutY();
        DataManager.getInstance().setImageX(imageX);
        DataManager.getInstance().setImageY(imageY);
        List<files.util.Note> notes = new ArrayList<>(stave.notes);
        for (files.util.Note note : notes
                ) {
            Element node = createNode(note.getCoordinate().getX() + imageX, note.getCoordinate().getY() + imageY, Color.RED, Type.Note, imageView.getBoundsInParent(), image, tab);
            anchorPane.getChildren().add((Note) node);
            selected.add(node);
        }
        List<files.util.Key> keys = new ArrayList<>(stave.keys);
        for (files.util.Key key : keys
                ) {
            NoteKey.setType(key.getType().charAt(0));
            Element node = createNode(key.getCoordinate().getX() + imageX, key.getCoordinate().getY() + imageY, Color.BLUE, Type.NoteKey, imageView.getBoundsInParent(), image, tab);
            ((NoteKey) node).addToParent(anchorPane);
            selected.add(node);
            noteKeyContainer.set(node);
        }
        List<files.util.Line> configLines = new ArrayList<>(stave.lines);
        for (files.util.Line line : configLines
                ) {
            Element node = createNode(line.getStart().getX() + imageX, line.getStart().getY() + imageY, Color.GREENYELLOW, Type.Line, imageView.getBoundsInParent(), image, tab);
            ((LineWithHandles) node).setEndX(line.getEnd().getX() + imageX);
            ((LineWithHandles) node).setEndY(line.getEnd().getY() + imageY);
            lines.add(node);
            selected.add(node);
            anchorPane.getChildren().add((LineWithHandles) node);
            ((LineWithHandles) node).showHandles();
        }

        final Delta start = new Delta();
        final Delta end = new Delta();

        anchorPane.setOnMouseClicked(event1 -> { //tabs listener
            if (imageView.getBoundsInParent().contains(event1.getX(), event1.getY())) {
                if (noteBtn.isSelected()) { // creates a note in position of mouse (minus side panel and the menu bar = 60)
                    Element node = createNode(event1.getX(), event1.getY(), colorPicker.getValue(), Type.Note, imageView.getBoundsInParent(), image, tab);
                    anchorPane.getChildren().add((Note) node);
                    selected.add(node);
                    event1.consume();
                } else if (noteKeyBtn.isSelected() && noteKeyContainer.isEmpty()) {
                    Element node = createNode(event1.getX(), event1.getY(), colorPicker.getValue(), Type.NoteKey, imageView.getBoundsInParent(), image, tab);
                    ((NoteKey) node).addToParent(anchorPane);
                    selected.add(node);
                    noteKeyContainer.set(node);
                    event1.consume();
                }
            }
        });
        anchorPane.setOnMousePressed(event1 -> { //addline if pressed on the background

            if (lineBtn.isSelected() && lines.size() < 4) {
                if (imageView.getBoundsInParent().contains(event1.getX(), event1.getY())) {
                    start.x = event1.getX();
                    start.y = event1.getY();
                    Element line = createNode(start.x, start.y, colorPicker.getValue(), Type.Line, imageView.getBoundsInParent(), image, tab); // create line when mouse pressed
                    lines.add(line); //this arraylist act as a container for line
                    lineContainer.set(line);
                    anchorPane.getChildren().add((LineWithHandles) line);
                    ((LineWithHandles) line).showHandles();
                }
                event1.consume();
            }
        });
        anchorPane.setOnMouseDragged(event1 -> { // set line length of added line when dragged
            if (lineBtn.isSelected() && !lineContainer.isEmpty()) {
                if (event1.getX() < imageView.getBoundsInParent().getMaxX()) {

                    if (event1.getX() < imageView.getBoundsInParent().getMinX()) {
                        end.x = imageView.getBoundsInParent().getMinX();
                        end.y = event1.getY();
                        LineWithHandles line = (LineWithHandles) lineContainer.get();
                        line.setEndX(end.x);
                    } else {
                        end.x = event1.getX();
                        end.y = event1.getY();
                        LineWithHandles line = (LineWithHandles) lineContainer.get();
                        line.setEndX(end.x);
                    }
                } else {
                    end.x = imageView.getBoundsInParent().getMaxX();
                    LineWithHandles line = (LineWithHandles) lineContainer.get();
                    line.setEndX(end.x);
                }
                event1.consume();
            }
        });
        anchorPane.setOnMouseReleased(event1 -> {//if mouse released empty the container that contains the created line

            if (lineBtn.isSelected()) {
                if (!lineContainer.isEmpty()) {
                    selected.add(lineContainer.get());
                }
                //add to selection+
                lineContainer.clear();
            }
            event1.consume();
        });

        scrollPane.setOnMousePressed(event -> {
            if (moveBtn.isSelected()) { //unselect everything if its been pressed on the background
                unselectAll();
                event.consume();
            }
        });
        return tab;
    }

    private Tab createTab(Image image) {

        Tab tab = new Tab("Notesheet " + (tabPane.getTabs().size() + 1));
        tab.setOnCloseRequest(event -> {
            AlertWindow.Pressed pressed =
                    AlertWindow.confirm("Close " + tab.getText(), "Do you want to saveAll your progress before closing?");
            if (pressed == AlertWindow.Pressed.Yes) {
                saveAllMenu.fire();
                selected.clear();
                lineContainer.clear();
                noteKeyContainer.clear();
                tabPane.getTabs().remove(tab);
            } else if (pressed == AlertWindow.Pressed.No) {
                selected.clear();
                lineContainer.clear();
                noteKeyContainer.clear();
                tabPane.getTabs().remove(tab);
            } else {
                if (event != null) event.consume();
            }
        });
        AnchorPane anchorPane = new AnchorPane();
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();


        anchorPane.setMinHeight(0);
        anchorPane.setMinWidth(0);
        anchorPane.setPrefHeight(image.getHeight() + 160);
        anchorPane.setPrefWidth(image.getWidth() + 160);
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);

        imageView.setPreserveRatio(true);

        anchorPane.getChildren().add(imageView);
        anchorPane.setTopAnchor(imageView, 80.0);
        anchorPane.setLeftAnchor(imageView, 80.0);
        anchorPane.setBottomAnchor(imageView, 80.0);
        anchorPane.setRightAnchor(imageView, 80.0);


        scrollPane.setContent(anchorPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        tab.setContent(scrollPane);
        anchorPane.layout();
        anchorPane.applyCss();
        scrollPane.layout();
        scrollPane.applyCss();
        tab.setContent(scrollPane);

        double imageX = imageView.getLayoutX();
        double imageY = imageView.getLayoutY();
        DataManager.getInstance().setImageX(imageX);
        DataManager.getInstance().setImageY(imageY);


        final Delta start = new Delta();
        final Delta end = new Delta();

        anchorPane.setOnMouseClicked(event1 -> { //tabs listener
            if (imageView.getBoundsInParent().contains(event1.getX(), event1.getY())) {
                if (noteBtn.isSelected()) { //creates a note in position of mouse (minus side panel and the menu bar = 60)
                    Element node = createNode(event1.getX(), event1.getY(), colorPicker.getValue(), Type.Note, imageView.getBoundsInParent(), image, tab);
                    anchorPane.getChildren().add((Note) node);
                    selected.add(node);
                    event1.consume();
                } else if (noteKeyBtn.isSelected() && noteKeyContainer.isEmpty()) {
                    Element node = createNode(event1.getX(), event1.getY(), colorPicker.getValue(), Type.NoteKey, imageView.getBoundsInParent(), image, tab);
                    ((NoteKey) node).addToParent(anchorPane);
                    selected.add(node);
                    noteKeyContainer.set(node);
                    event1.consume();
                }
            }
        });
        anchorPane.setOnMousePressed(event1 -> { //addline if pressed on the background

            if (event1.getButton() == MouseButton.PRIMARY && lineBtn.isSelected() && lines.size() < 4) {
                if (imageView.getBoundsInParent().contains(event1.getX(), event1.getY())) {
                    start.x = event1.getX();
                    start.y = event1.getY();
                    Element line = createNode(start.x, start.y, colorPicker.getValue(), Type.Line, imageView.getBoundsInParent(), image, tab); // create line when mouse pressed
                    lines.add(line); //this arraylist act as a container for line
                    lineContainer.set(line);
                    anchorPane.getChildren().add((LineWithHandles) line);
                    ((LineWithHandles) line).showHandles();
                }
                event1.consume();
            }
        });
        anchorPane.setOnMouseDragged(event1 -> { // set line length of added line when dragged
            if (lineBtn.isSelected() && !lineContainer.isEmpty()) {
                if (event1.getX() < imageView.getBoundsInParent().getMaxX()) {

                    if (event1.getX() < imageView.getBoundsInParent().getMinX()) {
                        end.x = imageView.getBoundsInParent().getMinX();
                        end.y = event1.getY();
                        LineWithHandles line = (LineWithHandles) lineContainer.get();
                        line.setEndX(end.x);
                    } else {
                        end.x = event1.getX();
                        end.y = event1.getY();
                        LineWithHandles line = (LineWithHandles) lineContainer.get();
                        line.setEndX(end.x);
                    }
                } else {
                    end.x = imageView.getBoundsInParent().getMaxX();
                    LineWithHandles line = (LineWithHandles) lineContainer.get();
                    line.setEndX(end.x);
                }
                event1.consume();
            }
        });
        anchorPane.setOnMouseReleased(event1 -> {//if mouse released empty the container that contains the created line

            if (lineBtn.isSelected()) {
                if (!lineContainer.isEmpty()) {
                    selected.add(lineContainer.get());
                }
                //add to selection
                lineContainer.clear();
            }
            event1.consume();
        });

        scrollPane.setOnMousePressed(event -> {
            if (moveBtn.isSelected()) { //unselect everything if its been pressed on the background
                unselectAll();
                event.consume();
            }
        });


        return tab;
    }

    private void unselectAll() {
        if (!selected.isEmpty()) {
            for (Element element : selected) {
                element.unselect();
            }
            selected.clear();
            notesSelect.setSelected(false);
            linesSelect.setSelected(false);
            keysSelect.setSelected(false);
        }
    }

    private Element createNode(double x, double y, Paint color, Type type, Bounds bounds, Image img, Tab tab) {
        // creates a node with given color and coordination, and puts listeners on it
        Element node;
        if (type == Type.Note) {                        //creates a node dependent on the given type e.g. 'c' for circle
            node = new Note(x - Note.getNoteWidth() / 2, y - Note.getNoteHeight() / 2, 10, color, img);
            DataManager.getInstance().getNotes().add(node);
            node.setTab(tab);
        } else if (type == Type.Line) {
            node = new LineWithHandles(x, y, x, y, bounds, img);
            ((LineWithHandles) node).setStroke(color);
            ((LineWithHandles) node).setStrokeWidth(3);
            DataManager.getInstance().getLines().add(node);
            node.setTab(tab);
        } else {
            node = new NoteKey(x - NoteKey.getWidth() / 2, y - NoteKey.getHeight() / 2, color, img);
            DataManager.getInstance().getKeys().add(node);
            node.setTab(tab);
            createTranscriptionText();
        }
        final Delta dragDelta = new Delta();
        if (selected != null && !selected.isEmpty()) { //checks if there is a selected circle
            for (Element n : selected) {
                n.unselect();
            }
            notesSelect.setSelected(false);
            linesSelect.setSelected(false);
            keysSelect.setSelected(false);
            selected.clear();
        }
        node.select();//apply selected effect
        (node.get()).setOnMousePressed(event -> {

            if (moveBtn.isSelected() && event.getButton() == MouseButton.SECONDARY) {
                selected.remove(node);
                node.delete(lines);
                if (node instanceof NoteKey) {
                    noteKeyContainer.clear();
                }
                event.consume();
            }
            // record a delta distance for the drag and drop operation.
            if (moveBtn.isSelected() && event.getButton() == MouseButton.PRIMARY) {
                if (event.isControlDown()) { // checks if control button is pressed
                    if (!selected.contains(node)) { // checks if the node is already selected
                        node.select();
                        selected.add(node);
                    } else {
                        node.select();
                        selected.remove(node);
                    }
                    notesSelect.setSelected(false);
                    linesSelect.setSelected(false);
                    keysSelect.setSelected(false);
//                    event.consume();
                } else {
                    if (selected != null && !selected.contains(node)) { // checks if the pressed node is already selected
                        for (Element n : selected) {
                            n.unselect();
                        }
                        node.select();//apply selected effect
                        selected.clear();
                        selected.add(node);
                        notesSelect.setSelected(false);
                        linesSelect.setSelected(false);
                        keysSelect.setSelected(false);
                    }
//                    event.consume();
                }
                dragDelta.x = node.getTranslateX() - event.getSceneX();
                dragDelta.y = node.getTranslateY() - event.getSceneY();
                node.setCursor(Cursor.MOVE);
                event.consume();

            }

        });

        (node.get()).setOnMouseDragged(event -> {

            if (moveBtn.isSelected() && event.getButton() == MouseButton.PRIMARY) {
                if (selected != null) {
                    double maxX = selected.getMaxX();
                    double minX = selected.getMinX();
                    double maxY = selected.getMaxY();
                    double minY = selected.getMinY();

                    double newX = dragDelta.x + event.getSceneX();
                    double newY = dragDelta.y + event.getSceneY();

                    if ((maxX + newX) > bounds.getMaxX()) { //fixes the overflow und underflow while dragging
                        newX = bounds.getMaxX() - maxX;
                    } else {
                        if (minX + newX < bounds.getMinX()) {
                            newX = bounds.getMinX() - minX;
                        }
                    }

                    if ((maxY + newY) > bounds.getMaxY()) {
                        newY = bounds.getMaxY() - maxY;
                    } else {
                        if (minY + newY < bounds.getMinY()) {
                            newY = bounds.getMinY() - minY;
                        }
                    }

                    translation(newX, newY);

                }
                event.consume();

            }


        });

        (node.get()).setOnMouseReleased(event -> {
            // when releasing the mouse btn applies the new Layout
            // to all selected nodes

            if (moveBtn.isSelected()) {
                node.setCursor(Cursor.HAND);

            }

            if (!selected.isEmpty()) {
                for (Element n : selected) {
                    fixPosition(n);
                }
                selected.refreshValues();
            }

        });

        (node.get()).setOnMouseEntered(event -> {

            if (moveBtn.isSelected()) {
                node.setCursor(Cursor.HAND);
            } else {
                node.setCursor(Cursor.CROSSHAIR);
            }

            event.consume();

        });

        return node;
    }

    private void translation(double translateX, double translateY) { //moves all selected elements to the new Position
        for (Element n : selected) {
            if (n instanceof LineWithHandles) {
                ((LineWithHandles) n).hideHandels();
            }
            n.setTranslateX(translateX);
            n.setTranslateY(translateY);
        }

    }

    public void deleteBtnHandler(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            trashBtn.fire();
        }

    }

    private void fixPosition(Element n) { //set the Position of each selected node after translating
        double x = n.getTranslateX();
        double y = n.getTranslateY();
        if (n instanceof LineWithHandles) {
            ((LineWithHandles) n).setStartX(((LineWithHandles) n).getStartX() + x);
            ((LineWithHandles) n).setStartY(((LineWithHandles) n).getStartY() + y);
            ((LineWithHandles) n).setEndX(((LineWithHandles) n).getEndX() + x);
            ((LineWithHandles) n).setEndY(((LineWithHandles) n).getEndY() + y);
            ((LineWithHandles) n).showHandles();
        } else {
            n.setLayoutX(n.getLayoutX() + x);
            n.setLayoutY(n.getLayoutY() + y);
        }
        n.setTranslateX(0);
        n.setTranslateY(0);
    }
}