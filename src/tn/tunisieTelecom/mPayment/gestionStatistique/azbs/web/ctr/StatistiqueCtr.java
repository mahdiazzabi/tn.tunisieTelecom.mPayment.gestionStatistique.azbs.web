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
import org.primefaces.model.chart.DateAxis;
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

	private LineChartModel lineModel1 = new LineChartModel();
	private LineChartModel lineModel2 = new LineChartModel();

	private BarChartModel barModel = new BarChartModel();

	private List<Banque> banques = new ArrayList<Banque>();
	private Date start = new Date();
	private Date end= new Date();
	private int idBanque;
	private List<SousCategories> sousCategories = new ArrayList<SousCategories>();
	private List<Categories> categories = new ArrayList<Categories>();

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

	public void doStatistiquesParCategorie() {
		barModel = createBarModel();
		barModel.setTitle("Diagramme à bandes périodique par Catégorie");
		barModel.setLegendPosition("ne");
	}

	public void doStatistiquesBancaire() {
		createPieModel();
	}

	public void doStatistiquesParBanque() {
		createPieModelBanqueSousCat();
	}

	public void doStatistiqueParSousCat() {

		createLineModels_stat();

	}

	
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

		pieModel1
				.setTitle("Statistiques des banques exprimées en montant total");
		pieModel1.setLegendPosition("w");

		pieModel2
				.setTitle("Statistiques des banques exprimées en montant total");
		pieModel2.setLegendPosition("e");
		pieModel2.setFill(false);
		pieModel2.setShowDataLabels(true);
		pieModel2.setDiameter(150);
		pieModel3
				.setTitle("Statistiques des banques exprimées en nombre de transactions");
		pieModel3.setLegendPosition("w");
		pieModel4
				.setTitle("Statistiques des banques exprimées en nombre de transactions");
		pieModel4.setLegendPosition("e");
		pieModel4.setFill(false);
		pieModel4.setShowDataLabels(true);
		pieModel4.setDiameter(150);

	}

	private void createLineModels_stat() {
		lineModel2 = initLinearModel_stat();
		lineModel2.setTitle("Statistiques par sous-catégories");
		lineModel2.setLegendPosition("e");
	}

	private LineChartModel initLinearModel_stat() {
		LineChartModel model2 = new LineChartModel();
		sousCategories = sousCategotiesEJBLocal.findAll();
		float montant = 0;
		float max = 0;

		for (SousCategories sousCategories : sousCategories) {

			List<Object[]> objects = statistiqueEJBLocal.statSousCat(start,
					end, sousCategories.getId());
			LineChartSeries series1 = new LineChartSeries();
			series1.setLabel(sousCategories.getLibelle());
			for (Object[] objects2 : objects) {
				System.err.println(objects2[0].toString() + "   " + objects2[1]);
				montant = Float.parseFloat(objects2[1]+ "");
				
				System.err.println(sousCategories.getLibelle());
				System.err.println((Date)objects2[0]);
				System.err.println(montant);
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dat = dateFormat.format((Date)objects2[0]);
				
				series1.set(dat, montant);
				if (montant > max) {
					max = montant;
				}

			}
			model2.addSeries(series1);
		}
		org.primefaces.model.chart.Axis xAxis = model2.getAxis(AxisType.X);
		xAxis.setLabel("Période");

		org.primefaces.model.chart.Axis yAxis = model2.getAxis(AxisType.Y);
		yAxis.setLabel("Montant de l'activité");
		yAxis.setMin(0);
		yAxis.setMax(max + 500);
		yAxis.setTickCount(7);
		model2.setZoom(true);
	       
		DateAxis axis = new DateAxis("Période");
        axis.setTickAngle(-50);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datend = dateFormat.format(end);
		String datmin = dateFormat.format(start);
		
		axis.setMin(datmin);
        axis.setMax(datend);
        axis.setTickFormat("%b %#d, %y");
         
        model2.getAxes().put(AxisType.X, axis);
		return model2;
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
				serieCat.set(dat, (Double) statistique[1]);

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


	

	public int getIdBanque() {
		return idBanque;
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

	public LineChartModel getLineModel2() {
		return lineModel2;
	}

	public void setLineModel2(LineChartModel lineModel2) {
		this.lineModel2 = lineModel2;
	}

}
