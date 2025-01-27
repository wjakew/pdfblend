/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.pdfblend.server.windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.jakubwawak.pdfblend.maintanance.FileObject;
import com.jakubwawak.pdfblend.maintanance.MergeEngine;
import com.jakubwawak.pdfblend.maintanance.NewMergeEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Object for creating merge window with options
 */
public class MergeOptionsWindow {

    ArrayList<FileObject> fileCollection;
    public Dialog main_dialog;
    VerticalLayout main_layout;

    private FileObject draggedItem;

    TextField name_field;
    Grid<FileObject> file_grid;

    Button merge_button;

    /**
     * Constructor
     */
    public MergeOptionsWindow(ArrayList<FileObject> fileCollection){
        this.fileCollection = fileCollection;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        draggedItem = null;
        prepareWindow();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        name_field = new TextField("New File Name");
        name_field.setPlaceholder("so_cool_file_name");
        name_field.setValue("blend_merge");
        name_field.setWidthFull();

        file_grid = new Grid<>(FileObject.class,false);
        file_grid.addColumn(FileObject::getFileName).setHeader("File Name");
        file_grid.setItems(fileCollection);
        file_grid.setWidth("100%");
        file_grid.setHeight("80%");
        file_grid.setSortableColumns();
        file_grid.setSelectionMode(Grid.SelectionMode.NONE);
        file_grid.setRowsDraggable(true);

        merge_button = new Button("Merge Files",this::mergebutton_action);
        merge_button.setWidth("100%");

        merge_button.setWidthFull();
        merge_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        merge_button.setIcon(VaadinIcon.FILE.create());

        file_grid.addDragStartListener(
                event -> {
                    // store current dragged item so we know what to drop
                    draggedItem = event.getDraggedItems().get(0);
                    file_grid.setDropMode(GridDropMode.BETWEEN);
                }
        );

        file_grid.addDragEndListener(
                event -> {
                    draggedItem = null;
                    // Once dragging has ended, disable drop mode so that
                    // it won't look like other dragged items can be dropped
                    file_grid.setDropMode(null);
                }
        );

        file_grid.addDropListener(
                event -> {
                    FileObject dropOverItem = event.getDropTargetItem().get();
                    if (!dropOverItem.equals(draggedItem)) {
                        // reorder dragged item the backing gridItems container
                        fileCollection.remove(draggedItem);
                        // calculate drop index based on the dropOverItem
                        int dropIndex =
                                fileCollection.indexOf(dropOverItem) + (event.getDropLocation() == GridDropLocation.BELOW ? 1 : 0);
                        fileCollection.add(dropIndex, draggedItem);
                        file_grid.getDataProvider().refreshAll();
                        Notification.show("Changed "+dropOverItem.fileName+" location!");
                    }
                }
        );
    }

    /**
     * Function for preparing object
     */
    void prepareWindow(){
        prepareComponents();

        main_layout.add(new H6("Your selected files"));
        main_layout.add(file_grid);
        main_layout.add(new H6("You can change the order of files by dragging them"));
        main_layout.add(name_field);
        main_layout.add(new H6("When you ready, merge your files"));
        main_layout.add(merge_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        main_dialog.add(main_layout);
        main_dialog.setHeight("60%");main_dialog.setWidth("60%");
    }

    /**
     * Function for preparing file collection from grid
     * @return ArrayList<FileObject>
     */
    ArrayList<FileObject> prepareFileCollection(){
        List<FileObject> data = (List<FileObject>) ((ListDataProvider<FileObject>)file_grid.getDataProvider()).getItems();
        return new ArrayList<>(data);
    }

    /**
     * merge button_action
     * @param ex
     */
    private void mergebutton_action(ClickEvent<Button> ex){
        String fileName = name_field.getValue();
        if ( !fileName.isBlank() ){
            fileName = fileName.replaceAll(" ","");
            MergeEngine me = new MergeEngine(prepareFileCollection(),fileName);
            File fileToDownload = me.merge();
            if  (fileToDownload != null){
                // Log the file path for debugging
                System.out.println("File to download: " + fileToDownload.getAbsolutePath());
                
                FileDownloaderComponent fdc = new FileDownloaderComponent(fileToDownload);
                main_layout.add(fdc.dialog); // Ensure dialog is added to layout
                fdc.dialog.open();
                main_dialog.close();
            }
            else{
                Notification.show("Error during merge. Check log!");
            }
        }
        else{
            Notification.show("Please enter file name!");
        }
    }

}