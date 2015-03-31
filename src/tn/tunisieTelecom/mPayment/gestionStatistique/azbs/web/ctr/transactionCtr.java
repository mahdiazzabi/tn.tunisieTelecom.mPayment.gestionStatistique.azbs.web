package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

import org.primefaces.model.UploadedFile;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Banque;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Transaction;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.ProduitEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.TransactionEJBLocal;


@ManagedBean
@SessionScoped
public class transactionCtr {
	
	private File file ;
	int idBanque ;
	Transaction tr ;
	List<Transaction> transactions ;
	
	
	@EJB
	TransactionEJBLocal transactionEJBLocal;
	
	@EJB
	BanqueEJBLocal banqueEJBLocal;
	
	@EJB
	ProduitEJBLocal produitEJBLocal;
	
	 private static String getFilename(Part part) {  
	        for (String cd : part.getHeader("content-disposition").split(";")) {  
	            if (cd.trim().startsWith("filename")) {  
	                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");  
	                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.  
	            }  
	        }  
	        return null;  
	    }  
	List<Transaction> traitement_Fichier_STB_BH_BNA(){
		transactions =  new ArrayList<Transaction>() ;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-y");
		try {
			    
			InputStream ips=new FileInputStream(file.getName()); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				StringTokenizer st = new StringTokenizer(ligne , "|");
				while (st.hasMoreTokens()) {
						String id_trans = st.nextToken();
						st.nextToken();
						Date date_trans = formatter.parse(st.nextToken());
						String etat = st.nextToken();
						Double montant = Double.parseDouble(st.nextToken());
						String tel_benef = st.nextToken();
						st.nextToken();
						st.nextToken();
						String prod = st.nextToken();
						//tr = new Transaction(id_trans,"", tel_benef, etat,montant,date_trans, produitEJBLocal.findByLibelle(prod).getId(), banqueEJBLocal.findById(idBanque) );
						tr = new Transaction();
						tr.setProduit(produitEJBLocal.findByLibelle(prod));
						tr.setBanque(banqueEJBLocal.findById(idBanque));
						tr.setEtat(etat);
						tr.setId_transaction(id_trans);
						tr.setDate(date_trans);
						tr.setMontant(montant);
						tr.setTel_benef(tel_benef);
						transactions.add(tr);
						
				}	
				
				
			}
			
			br.close(); 
		} catch (Exception e) {

			System.out.println("ERREUR");

			System.out.println(e.getMessage());
		}
		return transactions ;

	}
	List<Transaction> traitement_Fichier_ATB(){

		transactions =  new ArrayList<Transaction>() ;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Banque banque = new Banque() ;
		banque = banqueEJBLocal.findById(idBanque);
		try {
			InputStream ips=new FileInputStream(file.getName()); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				System.out.println(ligne);
				StringTokenizer st = new StringTokenizer(ligne , "|");
				while (st.hasMoreTokens()) {
						String etat = st.nextToken();
						String id_trans = st.nextToken();
						String tel_source = st.nextToken();
						Date date_trans = formatter.parse(st.nextToken());
						Double montant = Double.parseDouble(st.nextToken());
						String prod = st.nextToken();
						tr = new Transaction();
						tr.setProduit(produitEJBLocal.findByref(Integer.parseInt(prod)));
						tr.setBanque(banque);
						tr.setEtat(etat);
						tr.setId_transaction(id_trans);
						tr.setDate(date_trans);
						tr.setMontant(montant);
						tr.setTel_source(tel_source);
						System.out.println(tr.getId());
						System.out.println(tr.getEtat());
						System.out.println(tr.getId_transaction());
						System.out.println(tr.getTel_benef());
						System.out.println(tr.getTel_source());
						System.out.println(tr.getBanque().getNom());
						System.out.println(tr.getDate());
						System.out.println(tr.getMontant());
						System.out.println(tr.getProduit().getLibelle());
						transactions.add(tr);
				}
				
			}	
		} catch (Exception e) {

			System.out.println(e.getMessage());
	}
		return transactions ;
	}
	
	List<Transaction> traitement_Fichier_SMT(){

		transactions =  new ArrayList<Transaction>() ;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Banque banque = new Banque() ;
		banque = banqueEJBLocal.findById(idBanque);
		try {
			InputStream ips=new FileInputStream(file.getName()); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			br.readLine();
			while ((ligne=br.readLine())!=null){
				System.out.println(ligne);
				//etat
				System.out.println(ligne.substring(0, 1));
				String etat = ligne.substring(0, 1);
				//id transaction
				System.out.println(ligne.substring(1, 14));
				String id_trans = ligne.substring(1, 14);
				//date
				System.out.println(ligne.substring(14, 28));
				Date date_trans = formatter.parse(ligne.substring(14, 28));
				
				
				System.out.println(ligne.substring(28, 43));
				
				System.out.println(ligne.substring(43, 55));
				String tel_source = ligne.substring(43, 55);
				
				System.out.println(ligne.substring(55, 67));
				Double montant = Double.parseDouble(ligne.substring(55, 67));			

				tr = new Transaction();
				tr.setBanque(banque);
				tr.setEtat(etat);
				tr.setId_transaction(id_trans);
				tr.setDate(date_trans);
				tr.setMontant(montant);
				tr.setTel_source(tel_source);
				System.out.println(tr.getId());
				System.out.println(tr.getEtat());
				System.out.println(tr.getId_transaction());
				System.out.println(tr.getTel_benef());
				System.out.println(tr.getTel_source());
				System.out.println(tr.getBanque().getNom());
				System.out.println(tr.getDate());
				System.out.println(tr.getMontant());
				System.out.println(tr.getProduit().getLibelle());
				transactions.add(tr);
				}
			
			
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
		return transactions;
	}

	public void traiterFichier() throws IOException {
		System.out.println("OKKKKKKKK");
		
		try {
			switch(idBanque){
			case 1 : case 2 :  case 3 :  transactionEJBLocal.addListe(traitement_Fichier_STB_BH_BNA()) ;break;
			case 4 : transactionEJBLocal.addListe(traitement_Fichier_ATB()) ; break;
			case 5 : case 6 :transactionEJBLocal.addListe( traitement_Fichier_SMT()); break ;
			default:;
		}
		
		
		} catch (Exception e) {
			System.out.println("NOT OKKKKKKKK");
			
			System.out.println(e.getMessage());
		}
		
	}
	
	public int getIdBanque() {
		return idBanque;
	}

	public void setIdBanque(int idBanque) {
		this.idBanque = idBanque;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	
	}
	

	
}
