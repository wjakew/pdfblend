/**
 * @author Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.pdfblend.server.components;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Logo component
 */
public class LogoComponent extends VerticalLayout {
    

    /**
     * Constructor
     */
    public LogoComponent() {

        add(new H1("blend."));
        add(new H6("by Jakub Wawak"));

        setHeight("100%");
        setWidth("100%");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}
