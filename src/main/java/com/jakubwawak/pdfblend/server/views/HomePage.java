/**
 * @author Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.pdfblend.server.views;

import com.jakubwawak.pdfblend.server.components.LogoComponent;
import com.jakubwawak.pdfblend.server.components.UploadComponent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route("/home")
@RouteAlias("/")
public class HomePage extends VerticalLayout {

    HorizontalLayout mainLayout;

    VerticalLayout leftLayout;
    VerticalLayout rightLayout;

    LogoComponent logoComponent;

    Button start_button;

    UploadComponent uploadComponent;

    public HomePage() {

        addClassName("home-page");

        mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        leftLayout = new VerticalLayout();
        leftLayout.setSizeFull();
        leftLayout.setAlignItems(Alignment.CENTER);
        leftLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        rightLayout = new VerticalLayout();
        rightLayout.setSizeFull();
        rightLayout.setAlignItems(Alignment.CENTER);
        rightLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        // create components
        logoComponent = new LogoComponent();
        uploadComponent = new UploadComponent(this);

        start_button = new Button("Create a Merge",VaadinIcon.PLAY.create(),this::start_button_action);
        start_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);


        // add components to layouts
        leftLayout.add(logoComponent);
        rightLayout.add(start_button,uploadComponent);


        // add to main layout
        mainLayout.add(leftLayout,rightLayout);

        // set visibility
        uploadComponent.setVisible(false);

        
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(mainLayout);
        add(new H5("by Jakub Wawak"));
    }

    /**
     * Function for starting the upload process
     * @param ex
     */
    private void start_button_action(ClickEvent<Button> ex){
        uploadComponent.setVisible(true);
        start_button.setVisible(false);
    }

    public void reset_page(){
        uploadComponent.setVisible(false);
        start_button.setVisible(true);
    }
    
}

