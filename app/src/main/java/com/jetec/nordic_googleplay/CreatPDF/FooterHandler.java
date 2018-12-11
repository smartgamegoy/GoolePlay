package com.jetec.nordic_googleplay.CreatPDF;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;

import java.io.IOException;

public class FooterHandler implements IEventHandler {

    private Document doc;
    private PdfFont font;
    private int i = 1, page;

    public FooterHandler(Document doc, int page) throws IOException {
        this.doc = doc;
        this.page = page;
        font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UTF16-H", true);
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfCanvas canvas_footer = new PdfCanvas(docEvent.getPage());
        Rectangle pageSize = docEvent.getPage().getPageSize();
        canvas_footer.beginText();
        canvas_footer.moveText((pageSize.getRight() - doc.getRightMargin() - (pageSize.getLeft() + doc.getLeftMargin())) / 2 + doc.getLeftMargin() - 5, (pageSize.getBottom() + doc.getBottomMargin() - 5))
                .setFontAndSize(font, 5)
                .showText(String.valueOf(i) + " / " + String.valueOf(page))
                .endText()
                .release();
        this.i++;
    }
}
