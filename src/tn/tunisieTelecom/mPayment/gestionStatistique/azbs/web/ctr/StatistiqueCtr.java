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
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.SousCategories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.SousCategotiesEJBLocal;
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

	private List<Banque> banques = new ArrayList<Banque>();
	private Date start;
	private Date end;
	private int idBanque;
	private List<SousCategories> sousCategories = new ArrayList<SousCategories>();
	private LineChartModel lineModel1 = new LineChartModel();
	private String monthSelected;

	@EJB
	BanqueEJBLocal banqueEJBlocal;

	@EJB
	TransactionEJBLocal transactionEJBLocal;

	@EJB
	SousCategotiesEJBLocal sousCategotiesEJBLocal;

	private void createPieModelBanqueSousCat() {
		pieModelBanqueSousCatMontant = new PieChartModel();
		pieModelBanqueSousCatNombre = new PieChartModel();

		sousCategories = sousCategotiesEJBLocal.findAll();

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
				pieModelBanqueSousCatNombre.set(sousCategories.getLibelle(), 0);

			} else {
				pieModelBanqueSousCatMontant.set(sousCategories.getLibelle(),
						Double.parseDouble("" + object[1]));
				pieModelBanqueSousCatNombre.set(sousCategories.getLibelle(),
						Double.parseDouble("" + object[0]));

			}
			pieModelBanqueSousCatMontant
					.setTitle("Statistiques périodique des banques");
			pieModelBanqueSousCatMontant.setLegendPosition("w");

			pieModelBanqueSousCatNombre
					.setTitle("Statistiques périodique des banques");
			pieModelBanqueSousCatNombre.setLegendPosition("w");
		}
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
			System.err.println(banque.getNom());
		}

		pieModel1.setTitle("Statistiques périodique des banques");
		pieModel1.setLegendPosition("w");

		pieModel2.setTitle("Statistiques périodique des banques en %");
		pieModel2.setLegendPosition("e");
		pieModel2.setFill(false);
		pieModel2.setShowDataLabels(true);
		pieModel2.setDiameter(150);
		pieModel3.setTitle("en nombre");
		pieModel3.setLegendPosition("w");
		pieModel4.setTitle("en nombre %");
		pieModel4.setLegendPosition("e");
		pieModel4.setFill(false);
		pieModel4.setShowDataLabels(true);
		pieModel4.setDiameter(150);

	}

	public void doStatistiquesBancaire() {
		createPieModel();
	}

	public void doStatistiqueMensuelle() {

		createLineModels();

	}

	private void createLineModels() {
		lineModel1 = initLinearModel();
		lineModel1.setTitle("Linear Chart");
		lineModel1.setLegendPosition("e");
		org.primefaces.model.chart.Axis yAxis = lineModel1.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(4000);
		org.primefaces.model.chart.Axis xAxis = lineModel1.getAxis(AxisType.X);
		xAxis.setMin(1);
		xAxis.setMax(31);

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

		for (SousCategories sousCategories : sousCategories) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int nbr_jour_mois = calendar
					.getActualMaximum(Calendar.DAY_OF_MONTH);
			LineChartSeries series1 = new LineChartSeries();
			series1.setLabel(sousCategories.getLibelle());
			Date d = calendar.getTime();

			for (int i = 1; i <= nbr_jour_mois; i++) {
				// series1.set(i,
				// transactionEJBLocal.calculChiffreAffaireTT_journalier());

				series1.set(i, transactionEJBLocal.calculStatMensuelle(d,
						sousCategories.getId()));
				calendar.add(calendar.DATE, +1);
				d = calendar.getTime();

			}

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

	// public PieChartModel getPieModelBanqueSousCatMontant() {
	// return pieModelBanqueSousCatMontant;
	// }
	//
	// public void setPieModelBanqueSousCatMontant(
	// PieChartModel pieModelBanqueSousCatMontant) {
	// this.pieModelBanqueSousCatMontant = pieModelBanqueSousCatMontant;
	// }
	//
	// public PieChartModel getPieModelBanqueSousCatNombre() {
	// return pieModelBanqueSousCatNombre;
	// }
	//
	// public void setPieModelBanqueSousCatNombre(
	// PieChartModel pieModelBanqueSousCatNombre) {
	// this.pieModelBanqueSousCatNombre = pieModelBanqueSousCatNombre;
	// }

}
