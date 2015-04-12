package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class FileUploadController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UploadedFile file;

	@ManagedProperty(value = "#{transactionCtr}")
	private transactionCtr transaction;

	public void upload() {
		try {
			
			transaction.traiterFichier(file.getInputstream(), file.getFileName() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {

		this.file = file;
	}

	public void setTransaction(transactionCtr transaction) {
		this.transaction = transaction;
	}

}