/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
/**
 *
 * @author AMaroevich
 */
public class HeaderFooter extends PdfPageEventHelper {
    Phrase[] header = new Phrase[2];
    int pagenumber;
    public void onOpenDocument(PdfWriter writer, Document document){
        header[0] = new Phrase("Prueba");
    }
    public void onChapter(PdfWriter writer, Document document,float paragraphPosition, Paragraph title){
        header[1] = new Phrase(title.getContent());
        pagenumber = 1;
    }
    public void onStartPage(PdfWriter writer, Document document){
        pagenumber++;
    }
    public void onEbdPage(PdfWriter writer, Document document){
        Rectangle rect = writer.getBoxSize("art");
        switch(writer.getPageNumber() % 2){
            case 0:
                ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT,header[0],rect.getRight(),rect.getTop(),0);
                break;
            case 1:
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, header[1], rect.getLeft(), rect.getTop(), 0);
                break;
        }
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(String.format("page %d", pagenumber)), (rect.getLeft() + rect.getRight())/2, rect.getBottom() - 18, 0);
        
    }
}
