package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.expression.impl.ThisExpressionResolver;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Axis;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Banque;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Categories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.SousCategories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Statistique;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.CategoriesLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.SousCategotiesEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.StatistiqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.TransactionEJBLocal;

@ManagedBean
@SessionScoped
public class StatistiqueCtr {

	private PieChartModel pieModel1 = new PieChartModel();
	private PieChartModel pieModel2 = new PieChartModel();
	private PieChartModel pieModel3 = new PieChartModel();
	private PieChartModel pieModel4 = new PieChartModel();
	private PieChartModel pieModelBanqueSousCatMontant = new PieChartModel();
	private PieChartModel pieModelBanqueSousCatNombre = new PieChartModel();
	private PieChartModel pieModelBanqueSousCatMontantpourcent = new PieChartModel();
	private PieChartModel pieModelBanqueSousCatNombrepourcent = new PieChartModel();

	private BarChartModel barModel = new BarChartModel();

	private List<Banque> banques = new ArrayList<Banque>();
	private Date start;
	private Date end;
	private int idBanque;
	private List<SousCategories> sousCategories = new ArrayList<SousCategories>();
	private List<Categories> categories = new ArrayList<Categories>();

	private LineChartModel lineModel1 = new LineChartModel();
	private String monthSelected;

	@EJB
	BanqueEJBLocal banqueEJBlocal;

	@EJB
	TransactionEJBLocal transactionEJBLocal;

	@EJB
	SousCategotiesEJBLocal sousCategotiesEJBLocal;

	@EJB
	CategoriesLocal categoriesLocal;

	@EJB
	StatistiqueEJBLocal statistiqueEJBLocal;

	private void createPieModelBanqueSousCat() {
		pieModelBanqueSousCatMontant = new PieChartModel();
		pieModelBanqueSousCatNombre = new PieChartModel();
		pieModelBanqueSousCatMontantpourcent = new PieChartModel();
		pieModelBanqueSousCatNombrepourcent = new PieChartModel();

		sousCategories = sousCategotiesEJBLocal.findAll();
		Banque banque = banqueEJBlocal.findById(idBanque);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(end);
		calendar.add(calendar.HOUR_OF_DAY, +23);
		calendar.add(calendar.MINUTE, 59);
		calendar.add(calendar.SECOND, 59);
		end = calendar.getTime();

		for (SousCategories sousCategories : sousCategories) {
			Object[] object = transactionEJBLocal.calculStatBanqueSousCat(
					start, end, idBanque, sousCategories.getId());
			System.err.println(object[0] + "  " + object[1]);
			if (object[1] == null) {
				pieModelBanqueSousCatMontant
						.set(sousCategories.getLibelle(), 0);
				pieModelBanqueSousCatMontantpourcent.set(
						sousCategories.getLibelle(), 0);
				pieModelBanqueSousCatNombre.set(sousCategories.getLibelle(), 0);
				pieModelBanqueSousCatNombrepourcent.set(
						sousCategories.getLibelle(), 0);

			} else {
				pieModelBanqueSousCatMontant.set(sousCategories.getLibelle(),
						Double.parseDouble("" + object[1]));
				pieModelBanqueSousCatMontantpourcent.set(
						sousCategories.getLibelle(),
						Double.parseDouble("" + object[1]));
				pieModelBanqueSousCatNombre.set(sousCategories.getLibelle(),
						Double.parseDouble("" + object[0]));
				pieModelBanqueSousCatNombrepourcent.set(
						sousCategories.getLibelle(),
						Double.parseDouble("" + object[0]));
			}
		}
		pieModelBanqueSousCatMontant.setTitle("Statistiques de la banque "
				+ banque.getNom() + " exprimés en montant");
		pieModelBanqueSousCatMontant.setLegendPosition("w");

		pieModelBanqueSousCatNombre.setTitle("Statistiques de la banque "
				+ banque.getNom() + " en nombre de transactions");
		pieModelBanqueSousCatNombre.setLegendPosition("w");

		pieModelBanqueSousCatMontantpourcent
				.setTitle("Statistiques des Sous categories pour la banque "
						+ banque.getNom() + " en montant");
		pieModelBanqueSousCatMontantpourcent.setLegendPosition("e");
		pieModelBanqueSousCatMontantpourcent.setFill(false);
		pieModelBanqueSousCatMontantpourcent.setShowDataLabels(true);
		pieModelBanqueSousCatMontantpourcent.setDiameter(150);
		pieModelBanqueSousCatNombrepourcent
				.setTitle("Statistiques des Sous categories pour la banque "
						+ banque.getNom() + " en nombre de transactions");
		pieModelBanqueSousCatNombrepourcent.setLegendPosition("e");
		pieModelBanqueSousCatNombrepourcent.setFill(false);
		pieModelBanqueSousCatNombrepourcent.setShowDataLabels(true);
		pieModelBanqueSousCatNombrepourcent.setDiameter(150);

	}

	private void createPieModel() {
		pieModel1 = new PieChartModel();
		pieModel2 = new PieChartModel();
		pieModel3 = new PieChartModel();
		pieModel4 = new PieChartModel();

		banques = banqueEJBlocal.findAll();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(end);
		calendar.add(calendar.HOUR_OF_DAY, +23);
		calendar.add(calendar.MINUTE, 59);
		calendar.add(calendar.SECOND, 59);
		end = calendar.getTime();
		for (Banque banque : banques) {
			Object[] object = transactionEJBLocal.calculStatBanque(start, end,
					banque.getId());
			if (object[1] == null) {
				pieModel1.set(banque.getNom(), 0);
				pieModel2.set(banque.getNom(), 0);
				pieModel3.set(banque.getNom(), 0);
				pieModel4.set(banque.getNom(), 0);

			} else {
				pieModel1.set(banque.getNom(),
						Double.parseDouble("" + object[1]));
				pieModel2.set(banque.getNom(),
						Double.parseDouble("" + object[1]));
				pieModel3.set(banque.getNom(),
						Double.parseDouble("" + object[0]));
				pieModel4.set(banque.getNom(),
						Double.parseDouble("" + object[0]));
			}
		}

		pieModel1.setTitle("Statistiques des banques exprimés en montant");
		pieModel1.setLegendPosition("w");

		pieModel2.setTitle("Statistiques des banques exprimés en montant");
		pieModel2.setLegendPosition("e");
		pieModel2.setFill(false);
		pieModel2.setShowDataLabels(true);
		pieModel2.setDiameter(150);
		pieModel3
				.setTitle("Statistiques des banques exprimés en nombre de transactions");
		pieModel3.setLegendPosition("w");
		pieModel4
				.setTitle("Statistiques des banques exprimés en nombre de transactions");
		pieModel4.setLegendPosition("e");
		pieModel4.setFill(false);
		pieModel4.setShowDataLabels(true);
		pieModel4.setDiameter(150);

	}

	public void doStatistiquesParBanque() {
		createPieModelBanqueSousCat();
	}

	public void doStatistiquesBancaire() {
		createPieModel();
	}

	public void doStatistiqueMensuelle() {

		createLineModels();

	}

	public void doStatistiquesParCategorie() {
		barModel = createBarModel();
		barModel.setTitle("Diagramme à bandes périodique par Catégorie");
		barModel.setLegendPosition("ne");


	}

	private BarChartModel createBarModel() {
		BarChartModel model = new BarChartModel();

		categories = categoriesLocal.findAll();

		Double montant = 0.0;
		Double max = 0.0;

		
		
		for (Categories categories : categories) {
			ChartSeries serieCat = new ChartSeries();
			serieCat.setLabel(categories.getLibelle());
			System.err.println("------------------------------------");
			System.err.println(categories.getLibelle());
			List<Object[]> statistiques = statistiqueEJBLocal
					.statistiquesCategories(start, end, categories.getId());

			for (Object[] statistique : statistiques) {
				System.err.println((Date) statistique[0] + "   "
						+ (Double) statistique[1]);
				
				 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				 String dat = dateFormat.format((Date) statistique[0]);
				serieCat.set( dat , (Double) statistique[1] );
				
				montant = (Double) statistique[1];
				if (montant > max) {
					max = montant;
				}

			}
			model.addSeries(serieCat);

		}
		org.primefaces.model.chart.Axis xAxis = model.getAxis(AxisType.X);
		xAxis.setLabel("Période");

		org.primefaces.model.chart.Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel("Montant de l'activité");
		yAxis.setMin(0);
		yAxis.setMax(max + 500);
		yAxis.setTickCount(7);

		return model;

	}

	private void createLineModels() {
		lineModel1 = initLinearModel();
		lineModel1.setTitle("Evolution des ventes pour le mois : "
				+ monthSelected);
		lineModel1.setLegendPosition("e");
	}

	private LineChartModel initLinearModel() {
		LineChartModel model = new LineChartModel();
		sousCategories = sousCategotiesEJBLocal.findAll();
		// Date date = monthSelected;
		// System.err.println(date);
		SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy",
				Locale.ENGLISH);
		Date date = new Date();
		try {
			date = format.parse("01 " + monthSelected);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Double montant = 0.0;
		Double max = 0.0;
		for (SousCategories sousCategories : sousCategories) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int nbr_jour_mois = calendar
					.getActualMaximum(Calendar.DAY_OF_MONTH);
			LineChartSeries series1 = new LineChartSeries();
			series1.setLabel(sousCategories.getLibelle());
			Date d = calendar.getTime();
			for (int i = 1; i <= nbr_jour_mois; i++) {
				montant = transactionEJBLocal.calculStatMensuelle(d,
						sousCategories.getId());
				series1.set(i, montant);
				if (montant > max) {
					max = montant;
				}
				calendar.add(calendar.DATE, +1);
				d = calendar.getTime();
			}
			org.primefaces.model.chart.Axis yAxis = model.getAxis(AxisType.Y);
			yAxis.setMin(0);
			yAxis.setMax(max + 500);
			yAxis.setTickCount(7);
			org.primefaces.model.chart.Axis xAxis = model.getAxis(AxisType.X);
			xAxis.setMin(1);
			xAxis.setMax(31);
			xAxis.setTickCount(31);

			model.addSeries(series1);
		}

		return model;
	}

	// public void doStatistiquesBanqueSousCat() {
	// createPieModelBanqueSousCat();
	// }

	// public void subjectSelectionChanged(final AjaxBehaviorEvent event){
	//
	// doStatistiquesBanqueSousCat();
	// }

	public int getIdBanque() {
		return idBanque;
	}

	public String getMonthSelected() {
		return monthSelected;
	}

	public void setMonthSelected(String monthSelected) {
		this.monthSelected = monthSelected;
	}

	public void setIdBanque(int idBanque) {
		this.idBanque = idBanque;
	}

	public PieChartModel getPieModel3() {
		return pieModel3;
	}

	public void setPieModel3(PieChartModel pieModel3) {
		this.pieModel3 = pieModel3;
	}

	public PieChartModel getPieModel4() {
		return pieModel4;
	}

	public void setPieModel4(PieChartModel pieModel4) {
		this.pieModel4 = pieModel4;
	}

	public List<Banque> getBanques() {
		return banques;
	}

	public void setBanques(List<Banque> banques) {
		this.banques = banques;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public PieChartModel getPieModel1() {
		return pieModel1;
	}

	public void setPieModel1(PieChartModel pieModel1) {
		this.pieModel1 = pieModel1;
	}

	public PieChartModel getPieModel2() {
		return pieModel2;
	}

	public void setPieModel2(PieChartModel pieModel2) {
		this.pieModel2 = pieModel2;
	}

	public LineChartModel getLineModel1() {
		return lineModel1;
	}

	public void setLineModel1(LineChartModel lineModel1) {
		this.lineModel1 = lineModel1;
	}

	public PieChartModel getPieModelBanqueSousCatMontant() {
		return pieModelBanqueSousCatMontant;
	}

	public void setPieModelBanqueSousCatMontant(
			PieChartModel pieModelBanqueSousCatMontant) {
		this.pieModelBanqueSousCatMontant = pieModelBanqueSousCatMontant;
	}

	public PieChartModel getPieModelBanqueSousCatNombre() {
		return pieModelBanqueSousCatNombre;
	}

	public void setPieModelBanqueSousCatNombre(
			PieChartModel pieModelBanqueSousCatNombre) {
		this.pieModelBanqueSousCatNombre = pieModelBanqueSousCatNombre;
	}

	public PieChartModel getPieModelBanqueSousCatMontantpourcent() {
		return pieModelBanqueSousCatMontantpourcent;
	}

	public void setPieModelBanqueSousCatMontantpourcent(
			PieChartModel pieModelBanqueSousCatMontantpourcent) {
		this.pieModelBanqueSousCatMontantpourcent = pieModelBanqueSousCatMontantpourcent;
	}

	public PieChartModel getPieModelBanqueSousCatNombrepourcent() {
		return pieModelBanqueSousCatNombrepourcent;
	}

	public void setPieModelBanqueSousCatNombrepourcent(
			PieChartModel pieModelBanqueSousCatNombrepourcent) {
		this.pieModelBanqueSousCatNombrepourcent = pieModelBanqueSousCatNombrepourcent;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

}
