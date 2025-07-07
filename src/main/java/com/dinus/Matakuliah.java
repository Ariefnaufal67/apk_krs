package com.dinus;

public class Matakuliah {
      private String kodeMk;
      private String namaMk;
	  private int  sks;
      // constructor
      public Matakuliah(String kodeMk, String namaMk,int sks) {
         this.kodeMk = kodeMk;
         this.namaMk = namaMk;
		 this.sks = sks;
      }
	  public String getKodeMk(){
		  return kodeMk;
	  }
	  public String getNamaMk(){
		  return namaMk;
	  }
	  public int  getSks(){
		  return sks;
	  }	  
   }