/**
 * @author Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.pdfblend.server.components;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import com.jakubwawak.pdfblend.maintanance.FileObject;
import com.jakubwawak.pdfblend.server.views.HomePage;
import com.jakubwawak.pdfblend.server.windows.MergeOptionsWindow;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

/**
 * Upload component
 */
public class UploadComponent extends VerticalLayout {
    
    MultiFileMemoryBuffer buffer;
    Upload uploadComponent;

    ArrayList<FileObject> fileCollection;

    Button merge_button;
    Button clear_button;
    Button back_button;

    HomePage parent;

    /**
     * Constructor
     */
    public UploadComponent(HomePage parent) {
        this.parent = parent;

        fileCollection = new ArrayList<>();

        addClassName("upload-component");

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        buffer = new MultiFileMemoryBuffer();
        uploadComponent = new Upload(buffer);
        uploadComponent.setWidth("97%");uploadComponent.setHeight("97%");
        uploadComponent.setDropAllowed(true);
        uploadComponent.setAutoUpload(true);
        uploadComponent.setAcceptedFileTypes("application/pdf", ".pdf");
        uploadComponent.setMaxFiles(50);
        uploadComponent.setMaxFileSize(500 * 1024 * 1024); // 500MB

        uploadComponent.addSucceededListener(event -> {
            // Determine which file was uploaded
            String fileName = event.getFileName();

            // Get input stream specifically for the finished file
            InputStream fileData = buffer
                    .getInputStream(fileName);
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();

            // Do something with the file data
            // processFile(fileData, fileName, contentLength, mimeType);
            fileCollection.add(new FileObject(fileName,fileData, LocalDateTime.now(ZoneId.of("Europe/Warsaw"))));
            Notification.show("Uploaded "+fileName+"!");

        });

        uploadComponent.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();
            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        merge_button = new Button("Merge!",this::mergebutton_action);
        merge_button.addClassName("super-button");
        merge_button.setIcon(VaadinIcon.FILE.create());

        clear_button = new Button("",this::clearbutton_action);
        clear_button.addClassName("standard-button");
        clear_button.setIcon(VaadinIcon.TRASH.create());


        back_button = new Button("Back");
        back_button.addClassName("standard-button");
        back_button.setIcon(VaadinIcon.ARROW_LEFT.create());
        back_button.addClickListener(event -> {
            parent.reset_page();
        });

        add(uploadComponent);
        add(new HorizontalLayout(back_button,clear_button,merge_button));
    }

        /**
     * merge_button action
     * @param ex
     */
    private void mergebutton_action(ClickEvent<Button> ex){
        if ( fileCollection.size() > 0 ){
            // TODO: merge files
            MergeOptionsWindow merge_window = new MergeOptionsWindow(fileCollection);
            merge_window.main_dialog.open();
        }
        else{
            Notification.show("Your file list is empty!");
        }
    }

    /**
     * clearbutton_action
     * @param ex
     */
    private void clearbutton_action(ClickEvent<Button> ex){
        uploadComponent.clearFileList();
        Notification.show("File list cleared!");
    }
}

