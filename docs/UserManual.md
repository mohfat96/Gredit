# Gredit

# User Manual

### Group 10

### Mohammad Alkhufash, Simon Rafeck, Alexander Adrio

### July 9, 2018


## Contents

- 1 [Introduction](#1-introduction)
   - 1.1 [Intended Audience](#11-intended-audience)
   - 1.2 [Additional Documents](#12-additional-documents)
   - 1.3 [System Requirements](#13-system-requirements)
      - 1.3.1 [Software](#131-software)
      - 1.3.2 [Hardware](#132-hardware)
- 2 [Overview of Gredit](#2-overview-of-gredit)
   - 2.1 [Intended Use](#21-intended-use)
   - 2.2 [Graphical User Interface (GUI)](#22-graphical-user-interface-gui)
- 3 [Getting Started](#3-getting-started)
   - 3.1 [Launch The Program](#31-launch-the-program)
   - 3.2 [Open File/Add New Tab](#32-open-fileadd-new-tab)
   - 3.3 [Add Elements](#33-add-elements)
   - 3.4 [Select Elements](#34-select-elements)
   - 3.5 [Delete Elements](#35-delete-elements)
   - 3.6 [Move Elements](#36-move-elements)
   - 3.7 [Search Specific Note](#37-search-specific-note)
   - 3.8 [Transcription](#38-transcription)
   - 3.9 [Change Color Of Elements](#39-change-color-of-elements)
   - 3.10 [Save Project](#310-save-project)
   - 3.11 [Export as Image](#311-export-as-image)
   - 3.12 [Close Project/Tab](#312-close-projecttab)
   - 3.13 [Keyboard Shortcuts](#313-keyboard-shortcuts)
   - 3.14 [Support](#314-support)

## 1 Introduction

### 1.1 Intended Audience

This document is intended to be used by any Gredit program user to help
him use the program properly and handle errors if they happen.

### 1.2 Additional Documents

In addition to this document, there isReadme.txtfile that will guide the
user to properly install the program and run it and offer him some other
information as well.

### 1.3 System Requirements

#### 1.3.1 Software

- OS: Windows 10, Ubuntu 16.04 LTS
- Installed and integrated Java(Version 1.8)
- All required Libraries to show a JavaFX window properly.

#### 1.3.2 Hardware

- Sufficient Ram and CPU resources to run a Java program.


## 2 Overview of Gredit

### 2.1 Intended Use

Gredit recognises images of gregorian music sheets and allows the user to
add/delete/edit elements to correct the mistakes that may happen during
image recognition.

### 2.2 Graphical User Interface (GUI)

![Untitled-2](/uploads/5407e2bb836af561c34e5234f34a409d/Untitled-2.png)

```
A Menu Bar
```
```
A.1 File Menu: Contains the menu items ”Open”, ”Save”, ”Save all”,
”Export”, ”Close Tab” and ”Close All”.
A.2 Edit Menu: Contains the menu items ”Insert”, ”Change Color” and
”Delete”.
A.3 Help Menu: Contains the menu items ”Instructions” and ”Short-
cuts”.
A.4 Multiple Selection Bar: Select elements from the same type such
as ”Notes”,”Lines” and ”Notekeys”.
A.5 Search Bar: Search a specific note by typing it’s name.
```
```
B Toolbar
```
```
B.1 Cursor Tool:Select/Move/Delete Elements.
B.2 Line Tool:Insert lines with specified position and length.
```

```
B.3 Note Tool:Insert notes with specified position.
B.4 Note-Key Tool:Insert note-keys with specified position.
B.5 Trash:Delete selected elements
B.6 Color Picker:Change color of selected elements and upcoming ones.
```
```
C Notes Sheet
```
```
C.1 Work Surface:The area where the opened image will be shown on
and the user is able to interact with.
```
D Transcription

- Text-Field: In this field the transcription of the elements will be
    shown, which can be interacted with from the user.
- Refresh Button:A button located above the Text-Field to sync the
    Transcription with the Elements on thework surface


## 3 Getting Started

### 3.1 Launch The Program

To properly launch Gredit and start working on a new project press the run
button in the IDE.

### 3.2 Open File/Add New Tab

If the user has already launched a program he can open a new project in a
new tab. To open a file follow these steps:

1. Go to File menu on the top left corner of the window.
2. Choose ”Open” option and a File-Chooser will appear.
![Filechooser](/uploads/a7944f2547ea72d2260eee168c4e53a0/Filechooser.jpg)
3. Navigate through the folders and choose the file, which you want to
    start working on. The file should be in one of the *supported format.
4. Press ”Open” button or ”Enter” key on the keyboard and the GUI of
    Gredit will be displayed on the screen.

* The supported formats are image files (JPG and PNG) and YAML files
(YAML is the format, in which the user saves his progress in Gredit).
.


### 3.3 Add Elements

Both the image and the result of IRT will be displayed on the work surface.
The user has now full control on the image. He can Add/Remove/Reposition
all elements that have been added from IRT.

```
To add elements follow these steps:
```
1. From the Toolbar choose Line Tool,Note Tool or Note-Key Tool
    to add Notes,Lines or Notekeys respectively. Or you can go to Edit
    Menu and choose ”Insert Line”, ”Insert Note” or ”Insert Notkey”
    options.
2. Move the mouse to where you want Note or Notekeys to be added and
    for Lines where you want the line to start.
3. To add Notes or Notekey.

```
(a) Click the left button on the mouse and the element will be added
immediately at the position of the mouse.
```
4. To add a Line.

```
(a) Press and Hold on the left click and the Line will start from
the position of the mouse.
(b) Now Drag the mouse until you reach the the position you want
the Line to end at.
(c) Release the button.
```
### 3.4 Select Elements

Since selecting elements should be done before deleting. The user should
learn that first. There are some selection skills the user should know:

- The added element is automatically selected until another element is been added afterwards.
- To select one element: Choose the Cursor Tool and press on the
    element with left-click button.
- To select multiple elements: Choose the Cursor Tool and press
    on the element with left-click button while holding Ctrl buttonon
    keyboard by each element you want to select.


- To unselect an Element:Do the same steps as before on the selected
    element and will be unselected.
- To select/unselect all elements from the same type: Use the
    Multiple Selection Bar on the top of the screen. E.g. if you check the
    Notes box all elements from type note on the sheet will be selected.
    And if you uncheck the same box all elements from type notes will get
    Unselected.
- Unselect all: Press with the Cursor Tool selected on anywhere in
    the work surface thats not an element.

### 3.5 Delete Elements

- To delete one element:
    - Press on the Element with the right-click mouse button using the
       Cursor Tool.
- To delete selected elements do one of the following:
    - Press the Trash can from the Toolbar
    - Click delete button on keyboard.
    - Go to Edit Menu and choose ”Delete” option.

### 3.6 Move Elements

The user may have to move/reposition some elements due to misrecognition
from IRT. This can be done simply by Drag & Drop the element to the
new position with the left mouse button.

### 3.7 Search Specific Note

Finding a specific Note may take a while on large projects. Therefore we
implemented a search algorithm to make the process easier. The user can
search a note by typing the Note signature in the Search Bar. If the
searched note has been found, it will be highlighted.

### 3.8 Transcription

- Data on the Work Surface will get transcribed and shown in the Transcription Text-Field.
- The User can interact with the Transcription Text-Field by adding/removing
    text, which leads to add/remove elements in Work Surface.
- To sync the transcription with changes on work surface press Refresh
    Button

### 3.9 Change Color Of Elements

To make it easier to identify elements, the user can make the element stand
out from the image by changing its color. To change elements color

1. Make a selection of elements. see 3.4.
2. Press on Color Picker and color ballet will show up.
3. Choose a new color and all selected and upcoming elements will have
    the new color.

### 3.10 Save Project

The user can save his progress on a project in Yaml-file format by following
these steps:

1. Go to File Menu from Menu Bar.
2. Choose ”Save” Option and a File Chooser will appear.
3. Choose where you want to save the file.
4. Click Save.

The user can Save all tabs at once by following these steps:

1. Go to File Menu from Menu Bar.
2. Choose ”Save All” Option and a File Chooser will appear.
3. Follow steps 3 & 4 from before.

Note: You can skip the first two steps by pressing Alt + S for saving one tab
or Alt + Shift + S for saving all tabs.


### 3.11 Export as Image

The user can export his work as an Image in PNG format by following these
steps:

1. Go to File Menu from Menu Bar.
2. Choose Export Option and a File Chooser will appear.
3. Enter a name for the file and choose where you want to save it.
4. Click Save

Note: You can skip the first two steps by pressing Alt + E on keyboard.

### 3.12 Close Project/Tab

After the user is done with a tab, he can now close it safely by following
these steps:

1. Press on x sign, that located on the notesheet or go to File Menu and
    choose Close.
2. You will be prompted to save the file ,discard changes or get back to
    the project.
3. Press ”Yes” if you want to save the file. See 3.10.
4. Press ”No” if you want to close discarding changes.
5. Press ”Cancel” if you want to get back to the project and not close.

The user Can close all tabs at once by following these steps:

1. Go to File Menu and choose Close all.
2. For each closed tab, you will be prompted to save or discard changes.

Note: You can use the shortcuts Alt + c or Alt + Shift + c on keyboard to
close a tab or all tabs respectively.

### 3.13 Keyboard Shortcuts

To speed up the work flow we offered some shortcuts the user can use. All
the shortcuts can be found in ”Keyboard-Shortcuts” option in Help menu.


### 3.14 Support

For more support contact us:

```
Mohammad Alkhufash: mohammad.alkhufash@stud-mail.uni-wuerzburg.de
```
```
Simon Raffeck: simon.raffeck@stud-mail.uni-wuerzburg.de
```
```
Alexander Adrio: alexander.adrio@stud-mail.uni-wuerzburg.de
```

