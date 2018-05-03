package nc.ui.so.commission.ace.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import nc.funcnode.ui.AbstractFunclet;
import nc.itf.trade.excelimport.IImportableEditor;
import nc.itf.trade.excelimport.ImportableInfo;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.scmbd.excelimport.util.ImportInfoTrans;
import nc.ui.trade.excelimport.DataImporter;
import nc.ui.trade.excelimport.ExcelImportInfo;
import nc.ui.trade.excelimport.ExcelImporter;
import nc.ui.trade.excelimport.ImportProgressDlg;
import nc.ui.uif2.editor.IBillCardPanelEditor;
import nc.ui.uif2.excelimport.DefaultUIF2ImportableEditor;
import nc.ui.uif2.excelimport.ImportAction;
import nc.vo.scmbd.excelimport.TransFieldType;

import org.flexdock.util.SwingUtility;

public class SImportAction extends ImportAction {

	private static final long serialVersionUID = -8420536946163359804L;

	// 下面几个变量对应XML中的property
	private IImportableEditor importableEditor;
	private BillManageModel model;
	private ShowUpableBillForm billForm;

	public SImportAction() {}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// 获取上级面板
		final JComponent parent = billForm.getModel().getContext().getEntranceUI();
		ImportableInfo info = new ImportableInfo();
		if ((info != null) && (!info.isImportable())) {
			MessageDialog.showErrorDlg(parent, "Error","check ImportAction.class");
			return;
		}
		// 这两行执行选择excel文件的对话框，读入excel数据
		final ExcelImporter i = new ExcelImporter();
		final ExcelImportInfo importInfo = i.importFromExcel(parent,
				importableEditor.getInputItems());

		if (importInfo == null) {
			return;
		}

		// 可在此处插入语句，对excel读进来的数据（ExcelImportInfo）进行处理
		// 将excel读入的参照字段的编码或名称，翻译成主键
		new ImportInfoTrans().transRefDoc(importInfo,
				getIBillCardPanelEditor(),TransFieldType.TransByCodeAndName);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DataImporter importer = new DataImporter(importableEditor,
						importInfo, i.getLogFileName());

				ImportProgressDlg dlg = new ImportProgressDlg(parent, importer);
				dlg.setModal(false);
				SwingUtility.centerOnScreen(dlg);
				dlg.setVisible(true);
				dlg.start();
				dlg.setFunclet((AbstractFunclet) billForm.getModel()
						.getContext().getEntranceUI());
			}
		});
	}

	public IImportableEditor getImportableEditor() {
		return importableEditor;
	}

	public void setImportableEditor(IImportableEditor importableEditor) {
		this.importableEditor = importableEditor;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
	}

	public ShowUpableBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(ShowUpableBillForm billForm) {
		this.billForm = billForm;
	}

	private IBillCardPanelEditor getIBillCardPanelEditor() {
		return ((DefaultUIF2ImportableEditor) this.getImportableEditor())
				.getBillcardPanelEditor();
	}
}
