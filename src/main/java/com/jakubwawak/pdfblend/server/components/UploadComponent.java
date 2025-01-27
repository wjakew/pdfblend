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
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
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

        Button uploadButton = new Button("Upload PDF...");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        buffer = new MultiFileMemoryBuffer();
        uploadComponent = new Upload(buffer);
        uploadComponent.setUploadButton(uploadButton);

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
        merge_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        merge_button.setIcon(VaadinIcon.FILE.create());
        merge_button.setWidthFull();

        clear_button = new Button("",this::clearbutton_action);
        clear_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        clear_button.setIcon(VaadinIcon.TRASH.create());


        back_button = new Button("Back");
        back_button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        back_button.setIcon(VaadinIcon.ARROW_LEFT.create());
        back_button.addClickListener(event -> {
            parent.reset_page();
        });

        H6 info_text = new H6("Max single file size: 500MB, max files: 50");
        info_text.getStyle().set("text-align", "center");
        info_text.getStyle().set("color", "black");
        info_text.getStyle().set("font-size", "0.75rem");
        add(info_text);

        add(uploadComponent);


        HorizontalLayout button_layout = new HorizontalLayout(back_button,clear_button,merge_button);
        button_layout.setAlignItems(Alignment.CENTER);
        button_layout.setJustifyContentMode(JustifyContentMode.CENTER);
        button_layout.setWidthFull();

        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(JustifyContentMode.START);
        left_layout.setAlignItems(Alignment.CENTER);
        left_layout.setWidth("80%");
        left_layout.add(back_button);

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(JustifyContentMode.END);
        right_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        right_layout.add();
        right_layout.setWidth("80%");
        clear_button.getStyle().set("margin-right", "10px");
        merge_button.getStyle().set("margin-left", "10px");
        right_layout.add(clear_button,merge_button);
        
        button_layout.add(left_layout,right_layout);

        add(button_layout);
    }

        /**
     * merge_button action
     * @param ex
     */
    private void mergebutton_action(ClickEvent<Button> ex){
        if ( fileCollection.size() > 0 ){
            // TODO: merge files
            MergeOptionsWindow merge_window = new MergeOptionsWindow(fileCollection);
            add(merge_window.main_dialog);
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

