package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Banque;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Fichier;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Produit;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.SousCategories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Statistique;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Transaction;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.User;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.FichierEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.ProduitEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.SousCategotiesEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.StatistiqueEJBLocal;
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
	Fichier fichier;
	User user = new User();

	@EJB
	TransactionEJBLocal transactionEJBLocal;

	@EJB
	BanqueEJBLocal banqueEJBLocal;

	@EJB
	ProduitEJBLocal produitEJBLocal;

	@EJB
	FichierEJBLocal fichierEJBLocal;

	@EJB
	UserEJBLocal userEJBLocal;

	@EJB
	SousCategotiesEJBLocal sousCategotiesEJBLocal;

	@EJB
	StatistiqueEJBLocal statistiqueEJBLocal;

	public void traiterFichier(InputStream in, String fileName)
			throws IOException {
		produits = produitEJBLocal.findall();
		banque = banqueEJBLocal.findById(idBanque);
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		user = (User) session.getAttribute("currentUser");
		try {
			switch (idBanque) {
			case 1:
			case 2:
			case 3: {
				String tab[] = fileName.split("_");
				if (tab[0].trim().equals(banque.getNom().toUpperCase()) == false)
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Attention : Le fichier selectionn� ne correspond pas a la banque "
											+ banque.getNom(),
									"Le fichier selectionn� ne correspond pas a la banque "
											+ banque.getNom()));
				else {
					SimpleDateFormat formatter1 = new SimpleDateFormat(
							"ddMMyyyy");
					Date date_fichier = formatter1
							.parse(tab[1].substring(0, 8));
					traitement_Fichier_STB_BH_BNA(in, fileName, date_fichier);
				}
			}
				;
				break;
			case 4: {
				String tab[] = fileName.split("_");
				if (tab[0].trim().equals(banque.getNom().toUpperCase()) == false)
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Attention : Le fichier selectionn� ne correspond pas a la banque "
											+ banque.getNom(),
									"Le fichier selectionn� ne correspond pas a la banque "
											+ banque.getNom()));
				else {
					SimpleDateFormat formatter1 = new SimpleDateFormat(
							"ddMMyyyy");
					Date date_fichier = formatter1
							.parse(tab[1].substring(0, 8));
					traitement_Fichier_ATB(in, fileName, date_fichier);
				}
			}
				break;
			case 5: {
				String tab[] = fileName.split("_");
				if (tab[0].trim().equals("TT")) {
					SimpleDateFormat formatter1 = new SimpleDateFormat(
							"ddMMyyyy");
					Date date_fichier = formatter1.parse(tab[3]);
					traitement_Fichier_SMT(in, fileName, date_fichier);
				} else
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Attention : Le fichier selectionn� ne correspond pas a la banque "
											+ banque.getNom(),
									"Le fichier selectionn� ne correspond pas a la banque "
											+ banque.getNom()));
			}
				break;
			default:
				;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	void traitement_Fichier_STB_BH_BNA(InputStream in, String fileName,
			Date date_fichier) {
		transactions = new ArrayList<Transaction>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-y");
		fichier = fichierEJBLocal.findByNom(fileName);
		if (fichier == null) {
			fichier = new Fichier();
			fichier.setBanque(banque);
			fichier.setNom(fileName);
			Calendar calendar = Calendar.getInstance();
			fichier.setDate_traitement(calendar.getTime());
			fichier.setEtat_traitement("ECHEC");
			fichier.setDate_fichier(date_fichier);
			fichier.setUser(user);
			fichierEJBLocal.add(fichier);
			fichier = fichierEJBLocal.findByNom(fichier.getNom());
			List<SousCategories> sousCategories = sousCategotiesEJBLocal
					.findAll();

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
						tr.setProduit(null);
						for (Produit produit : produits) {
							if (produit.getLibelle().equals(prod.trim())) {
								tr.setProduit(produit);
							}
						}
						tr.setFichier(fichier);
						tr.setEtat(etat);
						tr.setId_transaction(id_trans);
						tr.setDate(date_trans);
						tr.setMontant(montant);
						tr.setTel_benef(tel_benef);
						transactions.add(tr);
					}
				}
				Boolean prod_OK = true;
				for (Transaction transaction : transactions) {
					if (transaction.getProduit() == null) {
						prod_OK = false;

					}
				}
				if (prod_OK) {
					List<Statistique> statistiques = statistiqueEJBLocal
							.findByIdBanqueJour(date_fichier, idBanque);
					if (!(statistiques.size() > 0)) {
						for (SousCategories sousCategories2 : sousCategories) {
							Statistique statistique = new Statistique();
							statistique.setSousCategories(sousCategories2);
							statistique.setMontantTransactions(0);
							statistique.setNbrTransactions(0);
							statistique.setBanque(banque);
							statistique.setJournee(date_fichier);
							statistiqueEJBLocal.add(statistique);
						}
					}

					if (transactionEJBLocal.addListe(transactions)) {
						fichier.setEtat_traitement("TRAITE");
						fichierEJBLocal.update(fichier);
						br.close();
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage("Successful", "Le fichier "
										+ fichier.getNom()
										+ " est trait� avec succ�s."));
					} else {
						br.close();
						fichierEJBLocal.remove(fichier);
						statistiques = new ArrayList<Statistique>();
						statistiques = statistiqueEJBLocal.findByIdBanqueJour(
								date_fichier, banque.getId());
						for (Statistique statistique : statistiques) {
							statistiqueEJBLocal.delete(statistique);
						}
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Format erron� : Le contenu du fichier  "
												+ fichier.getNom()
												+ " est erron�.",
										"Le contenu du fichier  "
												+ fichier.getNom()
												+ " est erron�."));

					}

				} else {
					fichierEJBLocal.remove(fichier);
					List<Statistique> statistiques = statistiqueEJBLocal
							.findByIdBanqueJour(date_fichier, banque.getId());
					for (Statistique statistique : statistiques) {
						statistiqueEJBLocal.delete(statistique);
					}
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Produit introuvable : Une des transactions contient un produit non valide.Veuillez verifier",
											"Une des transactions contient un produit non valide.Veuillez verifier"));

				}
			} catch (Exception e) {
				fichierEJBLocal.remove(fichier);
				List<Statistique> statistiques = statistiqueEJBLocal
						.findByIdBanqueJour(date_fichier, banque.getId());
				for (Statistique statistique : statistiques) {
					statistiqueEJBLocal.delete(statistique);
				}
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Format erron� : Le contenu du fichier  "
										+ fichier.getNom() + " est erron�.",
								"Le contenu du fichier  " + fichier.getNom()
										+ " est erron�."));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"D�j� trait� : Le fichier " + fichier.getNom()
									+ " est d�j� trait�.", "Le fichier "
									+ fichier.getNom() + " est d�j� trait�."));
		}
	}

	void traitement_Fichier_ATB(InputStream in, String fileName,
			Date date_fichier) {
		transactions = new ArrayList<Transaction>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		fichier = fichierEJBLocal.findByNom(fileName);
		if (fichier == null) {
			fichier = new Fichier();
			fichier.setBanque(banque);
			fichier.setNom(fileName);
			Calendar calendar = Calendar.getInstance();
			fichier.setDate_traitement(calendar.getTime());
			fichier.setEtat_traitement("ECHEC");
			fichier.setDate_fichier(date_fichier);
			fichier.setUser(user);
			fichierEJBLocal.add(fichier);
			fichier = fichierEJBLocal.findByNom(fichier.getNom());
			List<SousCategories> sousCategories = sousCategotiesEJBLocal
					.findAll();

			try {
				InputStream ips = in;
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;
				while ((ligne = br.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(ligne, "|");
					while (st.hasMoreTokens()) {
						String etat = st.nextToken();
						String id_trans = st.nextToken();
						String tel_source = st.nextToken();
						StringBuffer sb = new StringBuffer(st.nextToken());
						System.err.println(sb.toString());
						sb.replace(8, 14, "0000000");
						System.err.println(sb.toString());
						Date date_trans = formatter.parse(sb.toString());
						System.err.println(date_trans);
						Double montant = Double.parseDouble(st.nextToken());
						montant = montant / 1000;
						String prod = st.nextToken().toUpperCase();
						tr = new Transaction();
						tr.setProduit(null);
						for (Produit produit : produits) {
							if (produit.getRef() == Integer.parseInt(prod
									.trim())) {
								tr.setProduit(produit);
							}
						}
						tr.setFichier(fichier);
						tr.setEtat(etat);
						tr.setId_transaction(id_trans);
						tr.setDate(date_trans);
						tr.setMontant(montant);
						tr.setTel_source(tel_source);
						transactions.add(tr);
					}
				}
				br.close();
				Boolean prod_OK = true;
				for (Transaction transaction : transactions) {
					if (transaction.getProduit() == null) {
						prod_OK = false;
					}
				}

				if (prod_OK) {
					List<Statistique> statistiques = statistiqueEJBLocal
							.findByIdBanqueJour(date_fichier, idBanque);
					if (!(statistiques.size() > 0)) {
						for (SousCategories sousCategories2 : sousCategories) {
							Statistique statistique = new Statistique();
							statistique.setSousCategories(sousCategories2);
							statistique.setMontantTransactions(0);
							statistique.setNbrTransactions(0);
							statistique.setBanque(banque);
							statistique.setJournee(date_fichier);
							statistiqueEJBLocal.add(statistique);
						}
					}

					if (transactionEJBLocal.addListe(transactions)) {
						fichier.setEtat_traitement("TRAITE");
						fichierEJBLocal.update(fichier);
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage("Successful Le fichier "
										+ fichier.getNom()
										+ " est trait� avec succ�s.",
										"Le fichier " + fichier.getNom()
												+ " est trait� avec succ�s."));
					} else {

						fichierEJBLocal.remove(fichier);
						statistiques = new ArrayList<Statistique>();
						statistiques = statistiqueEJBLocal.findByIdBanqueJour(
								date_fichier, banque.getId());
						for (Statistique statistique : statistiques) {
							statistiqueEJBLocal.delete(statistique);
						}
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Format erron� : Le contenu du fichier  "
												+ fichier.getNom()
												+ " est erron�.",
										"Le contenu du fichier  "
												+ fichier.getNom()
												+ " est erron�."));

					}
				} else {
					fichierEJBLocal.remove(fichier);
					List<Statistique> statistiques = statistiqueEJBLocal
							.findByIdBanqueJour(date_fichier, banque.getId());
					for (Statistique statistique : statistiques) {
						statistiqueEJBLocal.delete(statistique);
					}
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Produit introuvable : Une des transactions contient un produit non valide.Veuillez verifier",
											"Une des transactions contient un produit non valide.Veuillez verifier"));

				}
			} catch (Exception e) {
				fichierEJBLocal.remove(fichier);
				List<Statistique> statistiques = statistiqueEJBLocal
						.findByIdBanqueJour(date_fichier, banque.getId());
				for (Statistique statistique : statistiques) {
					statistiqueEJBLocal.delete(statistique);
				}
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Format erron� : Le contenu du fichier  "
										+ fichier.getNom() + " est erron�.",
								"Le contenu du fichier  " + fichier.getNom()
										+ " est erron�."));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"D�j� trait� : Le fichier " + fichier.getNom()
									+ " est d�j� trait�. ", "Le fichier "
									+ fichier.getNom() + " est d�j� trait�."));
		}
	}

	void traitement_Fichier_SMT(InputStream in, String fileName,
			Date date_fichier) {
		transactions = new ArrayList<Transaction>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Produit produit_SMT_Facture = new Produit();
		fichier = fichierEJBLocal.findByNom(fileName);
		if (fichier == null) {
			fichier = new Fichier();
			fichier.setBanque(banque);
			fichier.setNom(fileName);
			Calendar calendar = Calendar.getInstance();
			fichier.setDate_traitement(calendar.getTime());
			fichier.setEtat_traitement("ECHEC");
			fichier.setDate_fichier(date_fichier);
			fichier.setUser(user);
			fichierEJBLocal.add(fichier);
			fichier = fichierEJBLocal.findByNom(fichier.getNom());
			List<SousCategories> sousCategories = sousCategotiesEJBLocal
					.findAll();
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
						String etat = ligne.substring(0, 1);
						String id_trans = ligne.substring(1, 14);
						StringBuffer sb = new StringBuffer(ligne.substring(14,
								28));
						sb.replace(8, 14, "0000000");
						Date date_trans = formatter.parse(sb.toString());
						String tel = ligne.substring(43, 55);
						Double montant = Double.parseDouble(ligne.substring(55,
								67));
						montant = montant / 1000;
						tr = new Transaction();
						tr.setFichier(fichier);
						tr.setEtat(etat);
						tr.setId_transaction(id_trans);
						tr.setDate(date_trans);
						tr.setMontant(montant);
						tr.setTel_source(tel);
						tr.setProduit(produit_SMT_Facture);
						transactions.add(tr);
					}
					br.close();

					List<Statistique> statistiques = statistiqueEJBLocal
							.findByIdBanqueJour(date_fichier, idBanque);
					if (!(statistiques.size() > 0)) {
						for (SousCategories sousCategories2 : sousCategories) {
							Statistique statistique = new Statistique();
							statistique.setSousCategories(sousCategories2);
							statistique.setMontantTransactions(0);
							statistique.setNbrTransactions(0);
							statistique.setBanque(banque);
							statistique.setJournee(date_fichier);
							statistiqueEJBLocal.add(statistique);
						}
					}

					if (transactionEJBLocal.addListe(transactions)) {
						fichier.setEtat_traitement("TRAITE");
						fichierEJBLocal.update(fichier);
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage("Successful Le fichier "
										+ fichier.getNom()
										+ " est trait� avec succ�s. ",
										"Le fichier " + fichier.getNom()
												+ " est trait� avec succ�s."));

					} else {
						fichierEJBLocal.remove(fichier);

						statistiques = statistiqueEJBLocal.findByIdBanqueJour(
								date_fichier, banque.getId());
						for (Statistique statistique : statistiques) {
							statistiqueEJBLocal.delete(statistique);
						}
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Format erron� : Le contenu du fichier  "
												+ fichier.getNom()
												+ " est erron�. ",
										"Le contenu du fichier  "
												+ fichier.getNom()
												+ " est erron�."));
					}
				} catch (Exception e) {
					fichierEJBLocal.remove(fichier);
					List<Statistique> statistiques = statistiqueEJBLocal
							.findByIdBanqueJour(date_fichier, banque.getId());
					for (Statistique statistique : statistiques) {
						statistiqueEJBLocal.delete(statistique);
					}
					FacesContext.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Format erron� : Le contenu du fichier  "
													+ fichier.getNom()
													+ " est erron�. ",
											"Le contenu du fichier  "
													+ fichier.getNom()
													+ " est erron�."));
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
						String etat = ligne.substring(0, 1);
						String id_trans = ligne.substring(1, 14);
						StringBuffer sb = new StringBuffer(ligne.substring(14,
								28));
						sb.replace(8, 14, "0000000");
						Date date_trans = formatter.parse(sb.toString());
						String tel = ligne.substring(43, 55);
						if (tel.charAt(4) == '9')
							produit = produit_SMT_MOBILE;
						else if (tel.charAt(4) == '4')
							produit = produit_SMT_ELISSA;
						else if (tel.charAt(4) == '7')
							produit = produit_SMT_FIXE;
						Double montant = Double.parseDouble(ligne.substring(55,
								67));
						montant = montant / 1000;
						tr = new Transaction();
						tr.setFichier(fichier);
						tr.setEtat(etat);
						tr.setId_transaction(id_trans);
						tr.setDate(date_trans);
						tr.setMontant(montant);
						tr.setTel_benef(tel);
						tr.setProduit(produit);
						transactions.add(tr);
					}
					br.close();
					List<Statistique> statistiques = statistiqueEJBLocal
							.findByIdBanqueJour(date_fichier, idBanque);
					if (!(statistiques.size() > 0)) {
						for (SousCategories sousCategories2 : sousCategories) {
							Statistique statistique = new Statistique();
							statistique.setSousCategories(sousCategories2);
							statistique.setMontantTransactions(0);
							statistique.setNbrTransactions(0);
							statistique.setBanque(banque);
							statistique.setJournee(date_fichier);
							statistiqueEJBLocal.add(statistique);
						}
					}

					if (transactionEJBLocal.addListe(transactions)) {
						fichier.setEtat_traitement("TRAITE");
						fichierEJBLocal.update(fichier);
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage("Successful Le fichier "
										+ fichier.getNom()
										+ " est trait� avec succ�s.",
										"Le fichier " + fichier.getNom()
												+ " est trait� avec succ�s."));
					} else {
						fichierEJBLocal.remove(fichier);
						statistiques = statistiqueEJBLocal.findByIdBanqueJour(
								date_fichier, banque.getId());
						for (Statistique statistique : statistiques) {
							statistiqueEJBLocal.delete(statistique);
						}
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Format erron� : Le contenu du fichier  "
												+ fichier.getNom()
												+ " est erron�. ",
										"Le contenu du fichier  "
												+ fichier.getNom()
												+ " est erron�."));
					}
				} catch (Exception e) {
					fichierEJBLocal.remove(fichier);
					List<Statistique> statistiques = statistiqueEJBLocal
							.findByIdBanqueJour(date_fichier, banque.getId());
					for (Statistique statistique : statistiques) {
						statistiqueEJBLocal.delete(statistique);
					}
					FacesContext.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Format erron� : Le contenu du fichier  "
													+ fichier.getNom()
													+ " est erron�. ",
											"Le contenu du fichier  "
													+ fichier.getNom()
													+ " est erron�."));
				}
			}

		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"D�j� trait� : Le fichier " + fichier.getNom()
									+ " est d�j� trait�. ", "Le fichier "
									+ fichier.getNom() + " est d�j� trait�."));
		}
	}

	public String BackAcceuil() {
		return "/admin/index?faces-redirect=true";
	}

	public int getIdBanque() {
		return idBanque;
	}

	public void setIdBanque(int idBanque) {
		this.idBanque = idBanque;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
