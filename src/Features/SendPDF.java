package Features;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import org.apache.log4j.BasicConfigurator;

import java.io.File;
import java.io.IOException;


public class SendPDF {

    public static final String DEST = "out/export_graphs.pdf";
    public static final String[] IMAGES = {
            "src/recent.jpg",
            "src/average.jpg",
            "src/super.jpg",
            "src/barchart.jpg",
            "src/piechart.jpg"
    };

    public static void main(String args[]) throws IOException {
        BasicConfigurator.configure();
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SendPDF().createPdf(DEST);
    }

    public static void createPdf(String dest) throws IOException {


        PdfWriter writer = new PdfWriter(dest);


        PdfDocument pdf = new PdfDocument(writer);


        Document document = new Document(pdf, PageSize.A4.rotate());

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);


        Paragraph p = new Paragraph("Power Generation in the UK");
        p.setFont(font).setFontSize(14);
        ;

        writer.setCompressionLevel(0);


        document.add(p);
        Text title = new Text("Most Recent Energy Output Data");
        Paragraph p1 = new Paragraph().add(title);


        p1.setFont(font).setFontSize(14);
        ;

        document.add(p1);


        Paragraph p2 = new Paragraph("General Averages of Energy Output Over the Years");
        p2.setFont(font).setFontSize(14);
        ;

        Paragraph p3 = new Paragraph("Line graph of energy output over the years");

        p3.setFont(font).setFontSize(14);
        ;
        Paragraph p4 = new Paragraph("Bar chart of energy output over the years");
        p4.setFont(font).setFontSize(14);
        ;
        Paragraph p5 = new Paragraph("Pie chart of energy output over the years");
        p5.setFont(font).setFontSize(14);
        ;


        for (int i = 0; i < IMAGES.length; i++) {
            Image image = new Image(ImageDataFactory.create(IMAGES[i]));
            image.scaleAbsolute(800.12f, 500.43f);
            image.setMargins(40.0f, 40.0f, 40.0f, 40.0f);

            if (i < IMAGES.length - 1) {
                pdf.addNewPage();
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                if (i == 0) {
                    document.add(p2);
                }
                if (i == 1) {
                    document.add(p3);
                }
                if (i == 2) {
                    document.add(p4);
                }
                if (i == 3) {
                    document.add(p5);
                }

            }
            // Notice that now it is not necessary to set image position,
            // because images are not overlapped while adding.
            image.setFixedPosition(i + 1, 20.0f, 0);


            document.add(image);
        }


        document.close();
    }


}
