import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.afplib.afplib.AfplibFactory;
import org.afplib.afplib.BDT;
import org.afplib.afplib.Comment;
import org.afplib.afplib.EDT;
import org.afplib.base.Triplet;
import org.afplib.io.AfpInputStream;
import org.afplib.io.AfpOutputStream;
import org.eclipse.emf.common.util.EList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorld {

	public static void main(String[] args) {
		try (AfpOutputStream aout = new AfpOutputStream(new FileOutputStream("hello.afp"))) {

			BDT bdt = AfplibFactory.eINSTANCE.createBDT();
			bdt.setDocName("HelloWorld");
			Comment comment = AfplibFactory.eINSTANCE.createComment();
			comment.setComment("Hello THis is My first AFPLib Program");
			bdt.getTriplets().add(comment);

			aout.writeStructuredField(bdt);

			EDT edt = AfplibFactory.eINSTANCE.createEDT();
			edt.setDocName("Hello World");
			aout.writeStructuredField(edt);

		} catch (IOException e) {
			e.printStackTrace();
		}

		try (AfpInputStream ain = new AfpInputStream(new FileInputStream("hello.afp"))) {

			BDT bdt = (BDT) ain.readStructuredField();
			EDT edt = (EDT) ain.readStructuredField();

			System.out.println(bdt.getDocName());
			System.out.println(bdt.getReserved());
			System.out.println(bdt.getTriplets().size());
			EList<Triplet> test = bdt.getTriplets();
			System.out.println(test);
			System.out.println(((Comment) bdt.getTriplets().get(0)).getComment());

//			System.out.println(edt.getDocName());
			GeneratePdf(bdt, edt);
			System.out.println("PDF created.");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void GeneratePdf(BDT bdt, EDT edt) {
		// created PDF document instance
		Document doc = new Document();
		try {
			// generate a PDF at the specified location
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("sample.pdf"));
			doc.addAuthor("UmaMahesh");
			doc.addCreationDate();
			doc.addCreator("Mahesh");
			doc.addTitle("Setting Attribute");
			doc.addSubject("Setting attributes to the PDF files.");

			// opens the PDF
			doc.open();
			// adds paragraph to the PDF file
			doc.add(new Paragraph(bdt.getDocName()));
			doc.add(new Paragraph(((Comment) bdt.getTriplets().get(0)).getComment()));
			doc.add(new Paragraph("HSBC Bank (USA)"));
			doc.add(new Paragraph("                                       "));
			doc.add(new Paragraph("Account Holder Name: Rachel Weisz"));
			doc.add(new Paragraph("Account Number: xxx-xxx-xxx-234"));
			doc.add(new Paragraph("Branch:  Los Angeles"));
			doc.add(new Paragraph("Branch Code: 18743"));
			doc.add(new Paragraph("Mobile Number: +1 (xxxx)-xxx-456"));
			doc.add(new Paragraph("Address: P.O. Box 1421, PC 111, CPO, New York (USA)"));
			doc.add(new Paragraph("Debit Card Number: xxxx-xxxx-xxxx-0987"));
			doc.add(new Paragraph("e-mail: rachel@gmial.com"));
			doc.add(new Paragraph("Toll Free Number: 18000xxxxx"));

			// close the PDF file
			doc.close();
			// closes the writer
			writer.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
