package com.jetec.nordic_googleplay.CreatPDF;

import android.util.Log;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import java.io.IOException;

public class HeaderHandler implements IEventHandler {

    private Document doc;
    private PdfFont font;
    private String title;

    public HeaderHandler(Document doc, String title) throws IOException {
        this.doc = doc;
        font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UTF16-H",true);
        this.title = title;
    }
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfCanvas canvas_header = new PdfCanvas(docEvent.getPage());
        PdfCanvas canvas_footer = new PdfCanvas(docEvent.getPage());
        Rectangle pageSize = docEvent.getPage().getPageSize();
        canvas_header.beginText();
        canvas_footer.beginText();
        Log.e("footer","pageSize.getBottom() =" + pageSize.getBottom());
        Log.e("footer","畫在這 =" + (pageSize.getBottom() + doc.getBottomMargin()));
        canvas_header.moveText(doc.getLeftMargin(), pageSize.getTop() - doc.getTopMargin() + 5)
                .setFontAndSize(font, 15)
                .showText(title)
                .endText()
                .release();
    }
}
