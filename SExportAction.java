package nc.ui.so.commission.ace.action;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JComponent;
import nc.itf.trade.excelimport.IImportableEditor;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.trade.excelimport.ExcelImporter;
import nc.ui.trade.excelimport.InputItem;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.excelimport.ExportExcelTemplateAction;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;

public class SExportAction extends ExportExcelTemplateAction{

	private static final long serialVersionUID = -3096375216188415853L;
	
	
	private IImportableEditor importableEditor;
	private BillManageModel model;
	private ShowUpableBillForm billcardPanelEditor;


	
	//根据单据模板，弹出字段选择框
	private ExcelImporter ei = new ExcelImporter();
	public void doAction(ActionEvent e)throws Exception{
		JComponent parent = billcardPanelEditor.getModel().getContext().getEntranceUI();
		ei.setFuncode(billcardPanelEditor.getModel().getContext().getFuncInfo().getFuncode());
		ei.exportExcelTemplate(parent, getInputItems());
		ShowStatusBarMsgUtil.showStatusBarMsg("导出成功！", billcardPanelEditor.getModel().getContext());
	}

	private List<InputItem> getInputItems() {
		List<InputItem> items = getImportableEditor().getInputItems();
//		可在此处添加内容，对即将返回的items进行处理
//		addSpecialItems(items);
		return items;
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
		return billcardPanelEditor;
	}

	public void setBillForm(ShowUpableBillForm billcardPanelEditor) {
		this.billcardPanelEditor = billcardPanelEditor;
	}
	
	
}
