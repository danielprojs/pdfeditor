package pdfeditor;


import com.itextpdf.text.DocumentException;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import javax.xml.bind.annotation.XmlRootElement;

@Path("/")
public class RunPDFEditor {
  public RunPDFEditor() {
    super();
  }
  
  @XmlRootElement
  private static class CustomInfo {
      public String bk_name;
      public String name;
      public String addr;
      public String city;
      public String state;
      public String zip;
      public int month;
      public int year;
      public String chkAcct;
      public String svAcct;
      public double amount;
  }

  @GET
  @Produces("text/plain")
  @Path("/hello")
  public String hello(){
      return "Hello World JDeveloper!!!";          
  }
  
  @GET
  //@Consumes({MediaType.APPLICATION_JSON})
  @Produces("application/pdf")
  @Path("edit")
  public Response getChPDF(@QueryParam("bk_name") String bk_name, 
                           @QueryParam("name") String name,
                           @QueryParam("addr") String addr,
                           @QueryParam("city") String city,
                           @QueryParam("state") String state,
                           @QueryParam("zip") String zip,
                           @QueryParam("month") int month,
                           @QueryParam("year") int year,
                           @QueryParam("chkAcct") String chkAcct,
                           @QueryParam("svAcct") String svAcct,
                           @QueryParam("amount") double amount){
    //String name = "Firstname Lastname";
    PDFeditor pe = new PDFeditor();
    //ByteArrayOutputStream baos = pe.modifyPDF("ch", name, "123 Somewhere Ave","Monterey Park", "ca", "91755", 1, 0, "00003265", "000000215698541", 1223456.8);
    ByteArrayOutputStream baos = pe.modifyPDF(bk_name, name, addr, city, state, zip, month, year, chkAcct, svAcct, amount);
    ResponseBuilder response = Response.ok(baos.toByteArray());
    response.header("Content-Disposition", "attachment; filename=" + name + ".pdf");
    return response.build();
  }

  public static void main(String[] args) {
    PDFeditor pe = new PDFeditor();
    String name = "Firstname Lastname";
    //System.out.println(pe.getDateRange(13, 2014));
//    ByteArrayOutputStream baos = pe.modifyPDF("ew", name, "123 Somewhere Ave","Monterey Park", "ca", "91755",0,0,"","", 1223456.8);
//    
//    try{
//    OutputStream outputStream = new FileOutputStream (name + ".pdf"); 
//    baos.writeTo(outputStream);
//    } catch (IOException e) {
//       e.printStackTrace();
//    } 
  }
}
