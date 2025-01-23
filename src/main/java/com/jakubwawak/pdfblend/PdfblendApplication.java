/**
 * @author Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.pdfblend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;
/**
 * Web application for blending PDF files
 */
@SpringBootApplication
@EnableVaadin({"com.jakubwawak"})
@Theme(value="pdfblend")
public class PdfblendApplication extends SpringBootServletInitializer implements AppShellConfigurator{

	public static String version = "0.0.1";
	public static String build = "bpdf23012024REV1";

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(PdfblendApplication.class, args);
	}

}
