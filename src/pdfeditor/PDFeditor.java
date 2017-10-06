package pdfeditor;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.FontFactory;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.ColumnText;


import java.io.ByteArrayOutputStream;
import java.io.IOException;


import java.text.NumberFormat;

import java.util.Calendar;
import java.util.Random;

public class PDFeditor {
  public PDFeditor() {
  }
  
  public ByteArrayOutputStream modifyPDF(String bk_name, String name, String address, String city, String state, String zip, 
                                         int month, int year, String checkingAcct, String savingAcct, double amount){
    
    
    if(bk_name.equalsIgnoreCase("ch")){
      return modifyChPDF(name, address, city, state, zip, month, year, checkingAcct, savingAcct, amount);
    }
    else if(bk_name.equalsIgnoreCase("ew")){
      return modifyEWPDF(name, address, city, state, zip, month, year, savingAcct, amount);
    }
    return null;
  }
  
  public ByteArrayOutputStream modifyEWPDF(String name, String address, String city, String state, String zip, 
                                           int month, int year, String savingAcct, double amount){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int min = 1246850;
    int max = 9574689;
    
    Random rand = new Random();
    String savingAccount = savingAcct;
    String date_range = getDateRange(month, year);
    
    
    try{

      PdfReader pdfReader = new PdfReader("resources/templates/ew.pdf");

      if (savingAcct.isEmpty() || savingAcct.length()!=15){
        int saving = rand.nextInt((max - min) + 1) + min;
        savingAccount = "" + saving;
      }
      
      PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);
      PdfContentByte content = pdfStamper.getOverContent(1);

      NumberFormat fmt = NumberFormat.getCurrencyInstance(); 
      
      FontFactory.register("resources/fonts/calibri.ttf", "calibri");
      Font calibri = FontFactory.getFont("calibri");
      calibri.setSize(9);
      
      //name
      float x = 100;
      float y = 603;
      Chunk textAsChunk = new Chunk(name.toUpperCase(), calibri);
      textAsChunk.setBackground(BaseColor.WHITE,0,0,60,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), x, y ,0);
      
      //address
      textAsChunk = new Chunk(address.toUpperCase(), calibri);
      textAsChunk.setBackground(BaseColor.WHITE,0,0,60,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), x, y-8 ,0);
      
      //city state zip
      textAsChunk = new Chunk(city.toUpperCase() + " " + state.toUpperCase() + " " + zip, calibri);
      textAsChunk.setBackground(BaseColor.WHITE,0,0,60,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), x, y-16 ,0);
      
      //date
      calibri.setSize(11.5f);
      String beginDate ="STARTING DATE: " + date_range.split(" through ")[0];
      String endDate ="ENDING DATE: " + date_range.split(" through ")[1];
      x=559;
      y=688.6f;
      textAsChunk = new Chunk(beginDate, calibri);
      textAsChunk.setBackground(BaseColor.WHITE,60,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), x, y ,0);
      textAsChunk = new Chunk(endDate, calibri);
      textAsChunk.setBackground(BaseColor.WHITE,60,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), x, y-11 ,0);
      
      //total days in statement period 
      textAsChunk = new Chunk("Total days in statement period: "+getMaxDateOfTheMonth(month, year), calibri);
      textAsChunk.setBackground(BaseColor.WHITE,60,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), x, y-22 ,0);
      
      //account number
      textAsChunk = new Chunk("00-1" + savingAccount, calibri);
      textAsChunk.setBackground(BaseColor.WHITE,20,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), x, y-33 ,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 269, 464 ,0);
      
      //balance
      textAsChunk = new Chunk(fmt.format(amount), calibri);
      textAsChunk.setBackground(BaseColor.WHITE,20,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 269, 452 ,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 269, 440 ,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 521, 465 ,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 521, 431 ,0);
      
      textAsChunk = new Chunk(".00", calibri);
      textAsChunk.setBackground(BaseColor.WHITE,20,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 521, 454 ,0);
      
      textAsChunk = new Chunk("0.00", calibri);
      textAsChunk.setBackground(BaseColor.WHITE,20,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 521, 443 ,0);
      
      x=295;
      y=386;
      textAsChunk = new Chunk("0.00%", calibri);
      textAsChunk.setBackground(BaseColor.WHITE,20,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), x, y ,0);
      
      textAsChunk = new Chunk(fmt.format(amount), calibri);
      textAsChunk.setBackground(BaseColor.WHITE,20,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), x, y-11 ,0);
      
      textAsChunk = new Chunk(getMaxDateOfTheMonth(month, year), calibri);
      textAsChunk.setBackground(BaseColor.WHITE,20,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), x+246, y ,0);
      
      textAsChunk = new Chunk("$0.00", calibri);
      textAsChunk.setBackground(BaseColor.WHITE,20,0,0,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), x+246, y-11 ,0);
      
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 502, 242 ,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 387, 242 ,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 502, 219 ,0);
      ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 387, 219 ,0);
      
      pdfStamper.close();
      
    } catch (IOException e) {
       e.printStackTrace();
    } catch (DocumentException e) {
      e.printStackTrace();
    }
    return baos;
  }
  
  
  
  public ByteArrayOutputStream modifyChPDF(String name, String address, String city, String state, String zip, 
                                           int month, int year, String checkingAcct, String savingAcct, double amount){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    int min = 11246850;
    int max = 99874689;
    
    double checkingAmount = 696.70f;
    double interestRate = 0.0001f;
    
    double interestEarnThisPeriod = amount * interestRate / 12;
    double interestEarnYearToDate = interestEarnThisPeriod * month;
    String date_range = getDateRange(month, year);
    
    if (month ==0){
      interestEarnYearToDate = interestEarnThisPeriod * (Calendar.getInstance().get(Calendar.MONTH)-1);
    }
    
    try {
      PdfReader pdfReader = new PdfReader("resources/templates/ch.pdf");
      
      String checkingAccount = checkingAcct;
      String savingAccount = savingAcct;
      
      Random rand = new Random();
      if (checkingAcct.isEmpty() || checkingAcct.length()!=15){
        int checking = rand.nextInt((max - min) + 1) + min;
        checkingAccount =  "0000001" + checking;
      }
      
      if (savingAcct.isEmpty() || savingAcct.length()!=15){
        int saving = rand.nextInt((max - min) + 1) + min;
        savingAccount = "0000002" + saving;
      }
      
      PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);
      NumberFormat fmt = NumberFormat.getCurrencyInstance();  
            
      
      for(int i=1; i<= pdfReader.getNumberOfPages(); i++){
        PdfContentByte content = pdfStamper.getOverContent(i);
        FontFactory.register("resources/fonts/arial.ttf", "arial");
        Font arial = FontFactory.getFont("arial");
        
        FontFactory.register("resources/fonts/arialbd.ttf", "arialbd");
        Font arialbd = FontFactory.getFont("arialbd");
   
        //date
        arial.setSize(9);
        Chunk textAsChunk = new Chunk(date_range, arial);
        textAsChunk.setBackground(BaseColor.WHITE,0,1,0,5);
        ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 493, 732 ,0);
        
        //primary Account number arialbd
        arialbd.setSize(10);
        Chunk primaryAccountChunk = new Chunk(checkingAccount, arialbd);
        primaryAccountChunk.setBackground(BaseColor.WHITE,3,0,0,2);
        ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(primaryAccountChunk), 429, 720 ,0);
        
        //page 1
        if (i==1){
          //name
          textAsChunk = new Chunk(name.toUpperCase(), arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,60,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 52, 641.5f ,0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 38, 155 ,0);
        
          //address
          textAsChunk = new Chunk(address.toUpperCase(), arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,60,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 52, 630 ,0);
          
          //city state zip
          textAsChunk = new Chunk(city.toUpperCase() + " " + state.toUpperCase() + " " + zip, arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,60,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 52, 618.5f ,0);
          
          //account number
          textAsChunk = new Chunk(checkingAccount, arial);
          textAsChunk.setBackground(BaseColor.WHITE);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 234, 336 ,0);
          arial.setSize(10);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 451, 154.4f ,0);
          
          arial.setSize(9); //saving account number
          textAsChunk = new Chunk(savingAccount, arial);
          textAsChunk.setBackground(BaseColor.WHITE);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 234, 318, 0);
          
          textAsChunk = new Chunk(fmt.format(checkingAmount), arial);
          textAsChunk.setBackground(BaseColor.WHITE,3,0,2,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 473, 336, 0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 550.5f, 336, 0);
          
          
          //amount    
          textAsChunk = new Chunk(fmt.format(amount), arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,2,0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 473, 319, 0);
          
          //amount + interest
          double amountWithInterest = amount + interestEarnThisPeriod;
          textAsChunk = new Chunk(fmt.format(amountWithInterest), arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,2,0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 550.5f, 319, 0);
          
          //total
          arialbd.setSize(9);
          textAsChunk = new Chunk(fmt.format(amount + checkingAmount), arialbd);
          textAsChunk.setBackground(BaseColor.WHITE,5,0,5,0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 473, 302, 0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 473, 270, 0);
          
          //total + interest
          textAsChunk = new Chunk(fmt.format(amountWithInterest + checkingAmount), arialbd);
          textAsChunk.setBackground(BaseColor.WHITE,5,0,5,0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 550.5f, 302, 0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 550.5f, 270, 0);
          
          // date in account balance summary
          arial.setSize(8.5f);
          textAsChunk = new Chunk(getDate(month, year), arial);
          textAsChunk.setBackground(BaseColor.WHITE,4,0,2.5f,0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 255.5f, 240, 0);
        }
        
        //page 2
        else if (i==2){
          //last year
          int lastYear = year - 1;
          if (year == 0){
            lastYear = Calendar.getInstance().get(Calendar.YEAR) -1;
          }
          textAsChunk = new Chunk("" + lastYear, arial);
          textAsChunk.setBackground(BaseColor.WHITE);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 214, 649, 0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 47, 638, 0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 110, 220, 0);
          
          //qualify deposit investment balance
          double amt = amount + 695.00;
          amt = Math.floor(amt);
          textAsChunk = new Chunk(fmt.format(amt) + ")", arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,60,0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 296, 514, 0);
          
          //name 
          textAsChunk = new Chunk(name.toUpperCase(), arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,60,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 35, 391 ,0);
          
          //account number
          arial.setSize(10);
          textAsChunk = new Chunk(savingAccount, arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,6,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 449, 393 ,0);
          arial.setSize(9);
          textAsChunk.setBackground(BaseColor.WHITE);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 236, 220, 0); 
  
          
          //deposits and additions
          textAsChunk = new Chunk(fmt.format(interestEarnThisPeriod).substring(1), arialbd);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,6,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 370, 310 ,0);
          
          //beginning balance
          textAsChunk = new Chunk(fmt.format(amount), arialbd);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,6,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 371, 324 ,0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 533, 161 ,0);
  
          //ending balance
          textAsChunk = new Chunk(fmt.format(amount + interestEarnThisPeriod), arialbd);
          textAsChunk.setBackground(BaseColor.WHITE);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 371, 297 ,0);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 533, 133.5f ,0); 
          textAsChunk = new Chunk(fmt.format(amount + interestEarnThisPeriod), arial);
          textAsChunk.setBackground(BaseColor.WHITE);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 534, 147 ,0);
  
          
          //interest 
          textAsChunk = new Chunk(fmt.format(interestEarnThisPeriod), arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,6,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 366, 262 ,0);
          textAsChunk = new Chunk(fmt.format(interestEarnThisPeriod), arialbd);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,6,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 450, 147 ,0);
          
          //interest year to date
          textAsChunk = new Chunk(fmt.format(interestEarnYearToDate), arial);
          textAsChunk.setBackground(BaseColor.WHITE,0,0,6,1);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_RIGHT, new Phrase(textAsChunk), 366, 249 ,0);
          
          //interest date 
          textAsChunk = new Chunk(getInterestDate(month, year), arial);
          textAsChunk.setBackground(BaseColor.WHITE);
          ColumnText.showTextAligned(content, PdfContentByte.ALIGN_LEFT, new Phrase(textAsChunk), 31, 146.5f ,0);
        }
        //---------------------------------------------------------------------------------------------------
      } //for loop
      pdfStamper.close();
    
    } catch (IOException e) {
       e.printStackTrace();
    } catch (DocumentException e) {
      e.printStackTrace();
    }
    return baos;
  }

  public String getDateRange(int month, int year){
    Calendar c = Calendar.getInstance();
    if (month != 0){
      c.set(Calendar.MONTH, month);
    }
    if (year != 0){
      c.set(Calendar.YEAR, year);
    }
    c.add(Calendar.MONTH, -1);
    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int numDays = c.getActualMaximum(Calendar.DATE);
    
    return monthNames[c.get(Calendar.MONTH)] + " " + "01, " + c.get(Calendar.YEAR) + " through " + monthNames[c.get(Calendar.MONTH)] + " " + numDays + ", " + c.get(Calendar.YEAR);
  }
  
  public String getDate(int month, int year){
    Calendar c = Calendar.getInstance();
    if (month != 0){
      c.set(Calendar.MONTH, month);
    }
    if (year != 0){
      c.set(Calendar.YEAR, year);
    }
    c.add(Calendar.MONTH, -1);
    String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    int numDays = c.getActualMaximum(Calendar.DATE);
    return monthNames[c.get(Calendar.MONTH)] + " " + numDays + ", " + c.get(Calendar.YEAR);
  }
  
  public String getInterestDate(int month, int year){
    Calendar c = Calendar.getInstance();
    if (month != 0){
      c.set(Calendar.MONTH, month);
    }
    if (year != 0){
      c.set(Calendar.YEAR, year);
    }
    c.add(Calendar.MONTH, -1);
    
    //Random generate a date between 5 to 20
    Random rand = new Random();
    int date = rand.nextInt(16) + 5;
    c.set(Calendar.DATE, date);
        
    while (c.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY || c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || c.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){
      date++;
      c.set(Calendar.DATE, date);
    }
    int mon = c.get(Calendar.MONTH) + 1;
    String mm = mon + "";
    String dd = c.get(Calendar.DATE) + "";
    if (mon < 10){
      mm = "0" + mm;
    }
    
    if (c.get(Calendar.DATE) < 10){
      dd = "0" + dd;
    }
    return mm + "/" + dd;
  }
  
  public String getMaxDateOfTheMonth(int month, int year){
    Calendar c = Calendar.getInstance();
    if (month != 0){
      c.set(Calendar.MONTH, month);
    }
    if (year != 0){
      c.set(Calendar.YEAR, year);
    }
    c.add(Calendar.MONTH, -1);
    int numDays = c.getActualMaximum(Calendar.DATE);
    
    return "" + numDays;
  }
  
//  public static void main(String[] args) {
//    PDFeditor pe = new PDFeditor();
//    pe.modifyPDF();
//  kjhkjhlkkjhlkj
//  }
}
