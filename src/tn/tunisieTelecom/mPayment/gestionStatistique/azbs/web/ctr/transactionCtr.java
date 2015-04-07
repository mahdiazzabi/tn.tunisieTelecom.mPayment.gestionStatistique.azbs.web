package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Banque;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Fichier;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Produit;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Transaction;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.User;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.FichierEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.ProduitEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.TransactionEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.UserEJBLocal;

@ManagedBean
@SessionScoped
public class transactionCtr {

	int idBanque;
	Transaction tr;
	List<Transaction> transactions;
	List<Produit> produits;
	Banque banque;
	@EJB
	TransactionEJBLocal transactionEJBLocal;

	// @EJB
	// UserEJBLocal userEJBLocal;
	//
	@EJB
	BanqueEJBLocal banqueEJBLocal;

	@EJB
	ProduitEJBLocal produitEJBLocal;

	// @EJB
	// FichierEJBLocal fichierEJBLocal;

	public void traiterFichier(InputStream in, String fileName)
			throws IOException {
		produits = produitEJBLocal.findall();
		banque = banqueEJBLocal.findById(idBanque);
		try {
			switch (idBanque) {
			case 1:
			case 2:
			case 3:
				traitement_Fichier_STB_BH_BNA(in);
				break;
			case 4:
				traitement_Fichier_ATB(in);
				break;
			case 5:
				traitement_Fichier_SMT(in, fileName);
				break;
			default:
				;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	void traitement_Fichier_STB_BH_BNA(InputStream in) {
		transactions = new ArrayList<Transaction>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-y");
		try {
			InputStream ips = in;
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(ligne, "|");
				while (st.hasMoreTokens()) {
					String id_trans = st.nextToken();
					st.nextToken();
					Date date_trans = formatter.parse(st.nextToken());
					String etat = st.nextToken();
					Double montant = Double.parseDouble(st.nextToken());
					String tel_benef = st.nextToken();
					st.nextToken();
					st.nextToken();
					String prod = st.nextToken().toUpperCase();

					tr = new Transaction();
					for (Produit produit : produits) {
						if (produit.getLibelle().equals(prod.trim())) {
							tr.setProduit(produit);
						}
					}
					tr.setBanque(banque);
					tr.setEtat(etat);
					tr.setId_transaction(id_trans);
					tr.setDate(date_trans);
					tr.setMontant(montant);
					tr.setTel_benef(tel_benef);
					transactions.add(tr);
				}
			}
			transactionEJBLocal.addListe(transactions);
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	void traitement_Fichier_ATB(InputStream in) {
		transactions = new ArrayList<Transaction>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			InputStream ips = in;
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				System.out.println(ligne);
				StringTokenizer st = new StringTokenizer(ligne, "|");
				while (st.hasMoreTokens()) {
					String etat = st.nextToken();
					String id_trans = st.nextToken();
					String tel_source = st.nextToken();
					Date date_trans = formatter.parse(st.nextToken());
					Double montant = Double.parseDouble(st.nextToken());
					montant = montant / 1000; // montant intial en millimes =>
												// convertion en dinars
					String prod = st.nextToken().toUpperCase();

					tr = new Transaction();
					for (Produit produit : produits) {
						if (produit.getRef() == Integer.parseInt(prod.trim())) {
							tr.setProduit(produit);
						}
					}
					tr.setBanque(banque);
					tr.setEtat(etat);
					tr.setId_transaction(id_trans);
					tr.setDate(date_trans);
					tr.setMontant(montant);
					tr.setTel_source(tel_source);
					transactions.add(tr);
				}

			}
			transactionEJBLocal.addListe(transactions);
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	void traitement_Fichier_SMT(InputStream in, String fileName) {
		transactions = new ArrayList<Transaction>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Produit produit_SMT_Facture = new Produit();
		if (fileName.toUpperCase().contains("FACT")) {
			for (Produit produit : produits) {
				if (produit.getLibelle().toUpperCase()
						.equals("TTBilling_SMT".toUpperCase())) {
					produit_SMT_Facture = produit;
				}

			}
			try {
				InputStream ips = in;
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;
				br.readLine();
				while ((ligne = br.readLine()) != null) {
					// System.out.println(ligne);
					// etat
					// System.out.println(ligne.substring(0, 1));
					String etat = ligne.substring(0, 1);
					// id transaction
					// System.out.println(ligne.substring(1, 14));
					String id_trans = ligne.substring(1, 14);
					// date
					// System.out.println(ligne.substring(14, 28));
					Date date_trans = formatter.parse(ligne.substring(14, 28));

					// System.out.println(ligne.substring(28, 43));

					// System.out.println(ligne.substring(43, 55));
					String tel = ligne.substring(43, 55);

					// System.out.println(ligne.substring(55, 67));
					Double montant = Double
							.parseDouble(ligne.substring(55, 67));
					montant = montant / 1000; // montant intial en millimes =>

					tr = new Transaction();
					tr.setBanque(banque);
					tr.setEtat(etat);
					tr.setId_transaction(id_trans);
					tr.setDate(date_trans);
					tr.setMontant(montant);
					tr.setTel_source(tel);
					tr.setProduit(produit_SMT_Facture);

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

				transactionEJBLocal.addListe(transactions);
				br.close();

			} catch (Exception e) {

				System.out.println(e.getMessage());
			}
		} else {

			Produit produit_SMT_MOBILE = new Produit();
			Produit produit_SMT_ELISSA = new Produit();
			Produit produit_SMT_FIXE = new Produit();
			for (Produit produit : produits) {
				if (produit.getLibelle().toUpperCase()
						.equals("SMT_MOBILE".toUpperCase())) {
					produit_SMT_MOBILE = produit;
				} else if (produit.getLibelle().toUpperCase()
						.equals("SMT_ELISSA".toUpperCase())) {
					produit_SMT_ELISSA = produit;

				} else if (produit.getLibelle().toUpperCase()
						.equals("SMT_FIXE".toUpperCase())) {
					produit_SMT_FIXE = produit;
				}

			}

			Produit produit = new Produit();
			try {
				InputStream ips = in;
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;

				br.readLine();
				while ((ligne = br.readLine()) != null) {
					// etat
					String etat = ligne.substring(0, 1);
					// id transaction
					String id_trans = ligne.substring(1, 14);
					// date
					Date date_trans = formatter.parse(ligne.substring(14, 28));

					String tel = ligne.substring(43, 55);
					if (tel.charAt(4) == '9')
						produit = produit_SMT_MOBILE;
					else if (tel.charAt(4) == '4')
						produit = produit_SMT_ELISSA;
					else if (tel.charAt(4) == '7')
						produit = produit_SMT_FIXE;
					Double montant = Double
							.parseDouble(ligne.substring(55, 67));
					montant = montant / 1000; // montant intial en millimes =>

					tr = new Transaction();
					tr.setBanque(banque);
					tr.setEtat(etat);
					tr.setId_transaction(id_trans);
					tr.setDate(date_trans);
					tr.setMontant(montant);
					tr.setTel_benef(tel);
					tr.setProduit(produit);
					transactions.add(tr);
				}

				transactionEJBLocal.addListe(transactions);
				br.close();
			} catch (Exception e) {

				System.out.println(e.getMessage());
			}

		}

	}

	List<Transaction> traitement_Fichier_SMT_Facture() {

		transactions = new ArrayList<Transaction>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Banque banque = new Banque();
		banque = banqueEJBLocal.findById(idBanque);
		Produit produit = new Produit();
		produit = produitEJBLocal.findByLibelle("SMT_FACTURE");

		try {
			InputStream ips = new FileInputStream("");
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			br.readLine();
			while ((ligne = br.readLine()) != null) {
				System.out.println(ligne);
				// etat
				System.out.println(ligne.substring(0, 1));
				String etat = ligne.substring(0, 1);
				// id transaction
				System.out.println(ligne.substring(1, 14));
				String id_trans = ligne.substring(1, 14);
				// date
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
				tr.setProduit(produit);
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

	public int getIdBanque() {
		return idBanque;
	}

	public void setIdBanque(int idBanque) {
		this.idBanque = idBanque;
	}

}
