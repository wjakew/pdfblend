/**
 * @author Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.pdfblend.server.components;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Logo component
 */
public class LogoComponent extends VerticalLayout {
    

    /**
     * Constructor
     */
    public LogoComponent() {

        H1 title = new H1("blend.");
        title.addClassName("title");
        add(title);
        add(new H5("the safest way to merge pdf files"));

        setHeight("100%");
        setWidth("100%");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}
