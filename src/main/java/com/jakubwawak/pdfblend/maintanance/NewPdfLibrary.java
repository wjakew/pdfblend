/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.pdfblend.maintanance;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class NewPdfLibrary {

    /**
     * Merges the provided list of PDF InputStreams into a single PDF OutputStream.
     *
     * @param inputPdfList List of InputStreams representing the PDF files to merge.
     * @param outputStream OutputStream where the merged PDF will be written.
     * @throws Exception if an error occurs during merging.
     */
    public void merge(List<InputStream> inputPdfList, OutputStream outputStream) throws Exception {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte pageContentByte = writer.getDirectContent();

        for (InputStream pdf : inputPdfList) {
            PdfReader pdfReader = null; // Declare outside the try block
            try {
                // Ensure the InputStream is at the beginning
                if (pdf.markSupported()) {
                    pdf.mark(1024); // Mark the current position
                }

                pdfReader = new PdfReader(pdf);
                int totalPages = pdfReader.getNumberOfPages();
                for (int page = 1; page <= totalPages; page++) {
                    document.newPage();
                    PdfImportedPage importedPage = writer.getImportedPage(pdfReader, page);
                    pageContentByte.addTemplate(importedPage, 0, 0);
                }
            } catch (Exception e) {
                System.out.println("Error processing PDF: " + e.getMessage());
                // Optionally log the error or notify the user
            } finally {
                if (pdfReader != null) {
                    pdfReader.close(); // Close the PdfReader in the finally block
                }
                // Reset the InputStream if it was marked
                if (pdf.markSupported()) {
                    pdf.reset(); // Reset to the marked position
                }
            }
        }

        document.close(); // Close the document after processing all PDFs
    }
} 