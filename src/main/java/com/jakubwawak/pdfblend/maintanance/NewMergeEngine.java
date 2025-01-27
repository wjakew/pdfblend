/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.pdfblend.maintanance;

import com.vaadin.flow.component.notification.Notification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Object for merging pdf files using NewPdfLibrary
 */
public class NewMergeEngine {

    public ArrayList<FileObject> fileCollection;
    String fileName;

    /**
     * Constructor
     */
    public NewMergeEngine(ArrayList<FileObject> fileCollection, String fileName) {
        this.fileCollection = fileCollection;
        this.fileName = fileName;
    }

    /**
     * Function for merge files
     * @return Integer
     */
    public File merge() {
        if (fileCollection.size() > 0) {
            try {
                List<InputStream> inputPdfList = new ArrayList<>();
                for (FileObject fileObject : fileCollection) {
                    inputPdfList.add(fileObject.fileStream);
                }
                String outputFileName = fileName + ".pdf";
                OutputStream outputStream = new FileOutputStream(outputFileName);
                mergePdfFiles(inputPdfList, outputStream);
                File fileToDownload = new File(outputFileName);
                return fileToDownload;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else {
            Notification.show("Number of files is 0!");
        }
        return null;
    }

    void mergePdfFiles(List<InputStream> inputPdfList, OutputStream outputStream) throws Exception {
        // Use NewPdfLibrary to merge PDFs
        NewPdfLibrary pdfMerger = new NewPdfLibrary();
        pdfMerger.merge(inputPdfList, outputStream);
        
        outputStream.flush();
        outputStream.close();
        Notification.show("Pdf files merged successfully using NewPdfLibrary.");
    }
} 