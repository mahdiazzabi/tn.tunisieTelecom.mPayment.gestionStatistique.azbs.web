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

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Transaction;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.ProduitEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.TransactionEJBLocal;


@ManagedBean
@SessionScoped
public class transactionCtr {
	
	private File file;
	int idBanque ;
	Transaction tr ;
	List<Transaction> transactions = new ArrayList<Transaction>();
	
	
	@EJB
	TransactionEJBLocal transactionEJBLocal;
	
	@EJB
	BanqueEJBLocal banqueEJBLocal;
	
	@EJB
	ProduitEJBLocal produitEJBLocal;
	
	
	void traitement_Fichier_STB_BH_BNA(){
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
						
						
				}	
				
				
			}
			
			br.close(); 
		} catch (Exception e) {

			System.out.println("ERREUR");

			System.out.println(e.getMessage());
		}
		

	}
	void traitement_Fichier_ATB(){
		
	}
	void traitement_Fichier_SMT(){
	
	}

	public void traiterFichier() throws IOException {
		System.out.println("OKKKKKKKK");
		System.out.println(file.getPath());
		
		try {
			switch(idBanque){
			case 1 : case 2 :  case 3 : traitement_Fichier_STB_BH_BNA();break;
			case 4 : traitement_Fichier_ATB();break;
			case 5 : traitement_Fichier_SMT();break;
			default:;
		}
		
		
		} catch (Exception e) {
			// TODO: handle exception
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
