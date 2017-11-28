package com.shankeerthan;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;

public class Main extends Application {
    private Canvas imageDisplay;
    private Stage stage;
    private  double scaleTemMax;
    private double scaleTemMin;
    private double interestRangeMax;
    private double interestRangeMin;
    private int unit;
    private int colorPallete;

    public Main(){
        scaleTemMax=35;
        scaleTemMin=25;
        interestRangeMax=32;
        interestRangeMin=28;
        unit=Values.CELSIUS;
        colorPallete=Values.FUSION;

        imageDisplay =new Canvas(); //HAVE TO handle initial size of canvas later point
        imageDisplay.getStyleClass().add("imagecanvas");
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        //Get reference of primaryStage to use in code further
        stage=primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        //Border Pane is root for this software
        BorderPane root =new BorderPane();
        root.getStyleClass().add("scene");
        root.getStylesheets().add("file:StyleSheet/style1.css");
        addNodesToRoot(root);
        Scene imageScene =new Scene(root,Values.DEFAULT_SCENE_WIDTH,Values.DEFAULT_SCENE_HEIGHT);

        //HAVE TO set tile with image name and app name set logo of the software
        styleStage(primaryStage);
        primaryStage.setScene(imageScene);
        primaryStage.show();
    }

    private void addNodesToRoot(BorderPane root){
        /*
        Center: Canvas contains imaged Thremal image and two left and right arrow to jump
                previous and next image
        Bottom :Simple HBOX to show images in folder
        Left  :Nothing
        Right : Like Tool bar contains all functionalities
         */

        //CEnter
        root.setCenter(imageDisplay);

        //Bottom
        HBox imagePositionBox =new HBox();
        imagePositionBox.setMinHeight(Values.IMAGE_POSITION_BOX_HEIGHT);
        designBottomBox(imagePositionBox);
        root.setBottom(imagePositionBox);
        //Right
        VBox rightBox =new VBox();
        rightBox.setMinWidth(Values.RIGHT_BAR_WIDTH);
        designRightBox(rightBox);
        root.setRight(rightBox);

        //Top
        HBox topBox=new HBox();
        topBox.setMinHeight(Values.TOP_BAR_HEIGHT);
        designTopBar(topBox,rightBox);
        root.setTop(topBox);


    }
    private void styleStage(Stage stage){
        stage.initStyle(StageStyle.DECORATED);
    }
    private void designTopBar(HBox container , VBox vBox){
        //Menu button
        Button smallMenuButton =new Button();
        handleSmallMenuButton(smallMenuButton,container);
        Image menuIcon =new Image("file:"+"Icons/menu.png");
        ImageView menuIconView =new ImageView(menuIcon);
        smallMenuButton.setGraphic(new ImageView(menuIcon));

        //Canvas to show name of the image
        Canvas nameCanvas =new Canvas();
        nameCanvas.setWidth(Values.DEFAULT_NAME_CANVAS_WIDTH);
        nameCanvas.setHeight(Values.DEFAULT_TOP_BAR_NODES_HEIGHT);

        //Zoom Selecton Button
        Button zoomSelectionButton=new Button();
        handleZoomSelectionButton(zoomSelectionButton,container);
        Image zoomSelectionIcon =new Image("file:"+"Icons/menu.png");
        zoomSelectionButton.setGraphic(new ImageView(zoomSelectionIcon));

        //Color pallete Drop down
        ComboBox colorPallete =new ComboBox();

        handleComboBox(colorPallete);
        colorPallete.getStyleClass().add("combobox");
        colorPallete.getItems().addAll("Fusion","Ironbow 1","Ironbow 2","Rainbow","Rain","Sepia","Glowbow","White Hot","Black Hot","Ice and Fire","Color 1","Color 2");


        //zoom  button
        Button zoomButton =new Button();
        Image zoomIcon =new Image("file:"+"Icons/zoom.png");
        zoomButton.setGraphic(new ImageView(zoomIcon));

        //Rotate left button
        Button rotateLeftButton=new Button();
        Image rotateLeftIcon =new Image("file:"+"Icons/rotate_left.png" );
        rotateLeftButton.setGraphic(new ImageView(rotateLeftIcon));

        //Rotate right button
        Button rotateRightButton=new Button();
        Image rotateRightIcon =new Image("file:"+"Icons/rotate_right.png");
        rotateRightButton.setGraphic(new ImageView(rotateRightIcon));

        //Crop button
        Button cropButton =new Button();
        Image cropImageIcon=new Image("file:"+ "Icons/crop.png");
        cropButton.setGraphic(new ImageView(cropImageIcon));

        //ShowHide side bar  Button
        Button showSidebarButton =new Button();
        handleShowBarButton(showSidebarButton , vBox);
        Image showSidebarIcon=new Image("file:"+ "Icons/show_sidebar.png");
        showSidebarButton.setGraphic(new ImageView(showSidebarIcon));


        //Hide side bar button
        Button hideSidebarButton =new Button();
        handleHideBarButton(hideSidebarButton,vBox);
        Image hideSidebarImageIcon = new Image("file:"+"Icons/hide_sidebar.png");
        hideSidebarButton.setGraphic(new ImageView(hideSidebarImageIcon));

        //About us button
        Button aboutUsButton= new Button();
        Image aboutUsIcon =new Image("file:"+"Icons/about_us.png");
        aboutUsButton.setGraphic(new ImageView(aboutUsIcon));


        container.setSpacing(Values.TOP_BOX_SPACING);
        container.getChildren().add(smallMenuButton);
        container.getChildren().add(nameCanvas);
        container.getChildren().add(zoomSelectionButton);
        container.getChildren().add(colorPallete);
        container.getChildren().add(zoomButton);
        container.getChildren().add(rotateRightButton);
        container.getChildren().add(rotateLeftButton);
        container.getChildren().add(cropButton);
        container.getChildren().add(showSidebarButton);
        container.getChildren().add(hideSidebarButton);
        container.getChildren().add(aboutUsButton);
    }



    private void designBottomBox(HBox container){
        //It has a slider to show position of current image in particular directory
        Slider positionShower =new Slider();
        positionShower.setOrientation(Orientation.HORIZONTAL);
        //sET slider to have width lesser than Stage widdth
        positionShower.setPrefWidth(Values.DEFAULT_SCENE_WIDTH-Values.POSITION_SLIDER_OFFSET);

        container.setPadding(new Insets(Values.POSITION_SLIDER_OFFSET));
        container.getChildren().add(positionShower);
    }

    private void designRightBox(VBox container){

        //Temperature cursor
        Button temperatureCursorButton=new Button();
        temperatureCursorButton.setTooltip(new Tooltip("Temperature Cursor"));
        Image temperatureCursorIcon=new Image("file:"+"Icons/cursor.png");
        temperatureCursorButton.setGraphic(new ImageView(temperatureCursorIcon));


        //Temperature Range Setting
        Button temperatureRangeButton=new Button();
        handleTemperatureRange(temperatureRangeButton);
        temperatureRangeButton.setTooltip(new Tooltip("Set Low and High Temperature"));
        Image temperatureRangeIcon =new Image("file:"+"Icons/tem_range.png");
        temperatureRangeButton.setGraphic(new ImageView(temperatureRangeIcon));

        //Label to show Temperature Unit
        Label temperatureUnitLabel=new Label();
        temperatureUnitLabel.setTooltip(new Tooltip("Unit of Temperature"));

        //Label to show current low point of range
        Label lowPointLabel=new Label();
        lowPointLabel.setTooltip(new Tooltip("Low Point of Temperature Range"));

        //label to show current temperature high point of range
        Label highPointLabel =new Label();
        highPointLabel.setTooltip(new Tooltip("High Point of Temperature Range"));

        //Compare with visual image
        Button comapareButton =new Button();
        comapareButton.setTooltip(new Tooltip("Compare with visual Image"));
        Image compareIcon =new Image("file:"+"Icons/compare.png");
        comapareButton.setGraphic(new ImageView(compareIcon));

        //Print Image
        Button printImageButton=new Button();
        printImageButton.setTooltip(new Tooltip("Print Image"));
        Image printImageIcon=new Image("file:"+"Icons/print.png");
        printImageButton.setGraphic(new ImageView(printImageIcon));

        //Save Image
        Button saveImageButton =new Button();
        saveImageButton.setTooltip(new Tooltip("Save Thermal Image"));
        Image saveImageIcon =new Image("file:"+"Icons/sava.png");
        saveImageButton.setGraphic(new ImageView(saveImageIcon));

        //Open Image
        Button openImageButton =new Button();
        openImageButton.setTooltip(new Tooltip("Open Image"));
        Image openImageIcon =new Image("file:"+"Icons/open_image.png");
        openImageButton.setGraphic(new ImageView(openImageIcon));

        //Open Folder
        Button openFolderbutton=new Button();
        openFolderbutton.setTooltip(new Tooltip("Open Folder of Images"));
        Image openFolderIcon =new Image("file:"+"Icons/open_image_folder.png");
        openFolderbutton.setGraphic(new ImageView(openImageIcon));

        //Copy Image
        Button copyImageButton =new Button();
        copyImageButton.setTooltip(new Tooltip("Copy Image"));
        Image copyImageIcon =new Image("file:"+"Icons/copy.png");
        copyImageButton.setGraphic(new ImageView(copyImageIcon));

        //Properties
        Button propertiesButton=new Button();
        propertiesButton.setTooltip(new Tooltip("Image Properties"));
        Image propertiesIcon =new Image("file:"+"Icons/info.png");
        propertiesButton.setGraphic(new ImageView(propertiesIcon));

        container.setSpacing(Values.SIDE_BOX_SPACING);
        container.setPadding(new Insets(Values.SIDE_BOX_PADDING_TOP_BOTTOM,0,0,0));
        container.getChildren().add(temperatureCursorButton);
        container.getChildren().add(temperatureRangeButton);
        container.getChildren().add(temperatureUnitLabel);
        container.getChildren().add(lowPointLabel);
        container.getChildren().add(highPointLabel);
        container.getChildren().add(comapareButton);
        container.getChildren().add(printImageButton);
        container.getChildren().add(saveImageButton);
        container.getChildren().add(openImageButton);
        container.getChildren().add(openFolderbutton);
        container.getChildren().add(copyImageButton);
        container.getChildren().add(propertiesButton);


    }
    private void handleSmallMenuButton(Button button,Node root){
        /*
        If this button is clicked show a Context
        menu with some options
         */
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ContextMenu contextMenu =new ContextMenu();
                MenuItem loadMenuItem =new MenuItem("Load......");//Load image from pictures directory
                MenuItem saveMenuItem =new MenuItem("Save......");//Save showing image in current directoty
                MenuItem openMenuItem =new MenuItem("Open......");//Open an image from user Selection

                contextMenu.getItems().addAll(loadMenuItem,saveMenuItem,openMenuItem);
                contextMenu.show(root, Side.TOP,Values.SMALL_MENU_DX,Values.SMALL_MENU_DY);

                loadMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                    }
                });
                saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("save");
                    }
                });
                openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser =new FileChooser();
                        fileChooser.setTitle("Open  Thermal Image");
                        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png","*.png"),new
                                FileChooser.ExtensionFilter("jpg","*.jpg"),new FileChooser.ExtensionFilter("jpeg","*.jpeg"));
                        File file =fileChooser.showOpenDialog(stage);
                        if(file!=null){
                            try {
                                final  Image image =new Image(file.toURI().toURL().toExternalForm());
                                imageDisplay.setHeight(image.getHeight());
                                imageDisplay.setWidth(image.getWidth());
                                imageDisplay.getGraphicsContext2D().drawImage(image,0,0);
                                System.out.println(imageDisplay.getWidth()+"  "+imageDisplay.getWidth());
                                 /*
                             Start a new Thread to do imaging
                              */
                                    Thread imagingThread =new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ColorSeparator colorSeparator =new ColorSeparator();
                                            colorSeparator.regionOfInterestDetector(image, Color.WHITE,.1);
                                            colorSeparator.edgeMarker(imageDisplay.getGraphicsContext2D(),Color.RED);
                                        }
                                    });
                                    imagingThread.start();
                                    //imagingThread.join();

                            } catch (MalformedURLException e) {
                                System.out.println(e);
                            }

                        }
                    }
                });

            }
        });
    }
    private void handleZoomSelectionButton(Button button,Node root){
        //If this button is clicked shows vertical slider to user
        //set zoom
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ContextMenu sliderDisplay =new ContextMenu();
                Slider zoomSelection=new Slider();
                zoomSelection.setOrientation(Orientation.VERTICAL);
                CustomMenuItem slider=new CustomMenuItem(zoomSelection);
                sliderDisplay.getItems().add(slider);
                sliderDisplay.show(root,Side.TOP,Values.SLIDER_DX,Values.SLIDER_DY);
            }
        });
    }
    private void handleTemperatureRange(Button button){
        /*
        This method handle when button is clicked it get low point,high point of temperature scale range
        and Range of Interest  And  give of choice to user ues what ever the unit from user
        It has default values
        Temperature Scale Range : 25 - 35 degree celcious
        Interest range          :28   -32 degree celcious
         */
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                final Stage dialog =new Stage();
                stage.setTitle("Adjust Temperature Scale Setting");
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);


                GridPane grid =new GridPane();

                Label selectUnit =new Label("Temperature Unit");

                ComboBox <String> unitSelection =new ComboBox<String>();
                unitSelection.getItems().addAll("Celcious","Frahneit","Kelvin");
                unitSelection.getSelectionModel().selectFirst();


                Label temperatureScale =new Label("Adjust Temperature Scale Range");

                Label highPoint =new Label("High Temperature of Range");
                Label lowPoint =new Label("Low Temperature of Range");

                TextField highPointText =new TextField(Values.HIGH_TEM_SCALE_RANGE);
                TextField lowPointText =new TextField(Values.LOW_TEM_SCALE_RANGE);

                Label regionOfInterest =new Label("Region of Interset Temperature Scale");

                Label highPointOfRegion =new Label("High Temperature");
                Label lowPointOfRegion =new Label("Low Temperature ");

                TextField highPointRegionText =new TextField(Values.HIGH_TEM_REGION);
                TextField lowPonitRegionText =new TextField(Values.LOW_TEM_REGION);

                Button setRanges =new Button("Apply");

                setRanges.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try{
                            scaleTemMax=Double.parseDouble(highPointText.getText());
                            scaleTemMin=Double.parseDouble(lowPointText.getText());
                            interestRangeMax=Double.parseDouble(highPointRegionText.getText());
                            interestRangeMin =Double.parseDouble(lowPonitRegionText.getText());
                            unit=unitSelection.getSelectionModel().getSelectedIndex();
                        }catch (NumberFormatException e){
                            System.out.println(e);
                        }
                    }
                });
                Button reset =new Button("Reset");
                reset.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        highPointRegionText.setText(Values.HIGH_TEM_REGION);
                        lowPonitRegionText.setText(Values.LOW_TEM_REGION);
                        highPointText.setText(Values.HIGH_TEM_SCALE_RANGE);
                        lowPointText.setText(Values.LOW_TEM_SCALE_RANGE);
                    }
                });

                grid.add(selectUnit,0,0);
                grid.add(unitSelection,1,0);
                grid.add(temperatureScale,0,1);
                grid.add(highPoint,0,2);
                grid.add(highPointText,1,2);
                grid.add(lowPoint,0,3);
                grid.add(lowPointText,1,3);
                grid.add(regionOfInterest,0,4);
                grid.add(highPointOfRegion,0,5);
                grid.add(highPointRegionText,1,5);
                grid.add(lowPointOfRegion,0,6);
                grid.add(lowPonitRegionText,1,6);
                grid.add(setRanges,0,7);
                grid.add(reset,1,7);

                grid.setVgap(Values.GRID_VGAP);
                Scene dialogScene = new Scene(grid, 400, 400);
                dialog.setScene(dialogScene);
                dialog.show();

            }
        });
    }
    private void handleComboBox(ComboBox<String> comboBox){
        //Default combobox selection is Fusion
        comboBox.getSelectionModel().selectFirst();

        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                 {
                   colorPallete=comboBox.getSelectionModel().getSelectedIndex();
                     System.out.println(colorPallete);
                }
            }
        });

    }

    private void handleHideBarButton(Button button , VBox vBox){
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("hide");
                TranslateTransition transition = new TranslateTransition();
                transition.setDuration(Duration.seconds(2));
                transition.setNode(vBox);
                transition.setToX(50);
                transition.play();

            }




        } );

    }


    private void handleShowBarButton(Button button, VBox vBox) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Show");
                TranslateTransition transition = new TranslateTransition();
                transition.setDuration(Duration.seconds(1));
                transition.setNode(vBox);
                transition.setToX(-5);
                transition.play();

            }




        } );
    }
}