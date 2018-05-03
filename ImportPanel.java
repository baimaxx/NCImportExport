package nc.ui.so.commission.ace.view;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.generator.IdGenerator;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.trade.excelimport.Uif2ImportablePanel;
import nc.ui.uif2.UIState;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ExtendedAggregatedValueObject;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.so.commission.AggConnission;
import nc.vo.so.commission.Commission_b;

public class ImportPanel extends Uif2ImportablePanel {
	
	private BillManageModel model;
	static String title = "mytitle";
	static String funCode = "40060142";	
	static String configFilePath = "nc/ui/so/commission/ace/view/import_config.xml";
	
	/**
	 * 必须在无参构造函数中完成界面初始化，这几个参数是私有的，一般在子类构造函数中写死
	 * 导入流程：系统会按照顺序调用addNew(),setValue(),save(),cancel()这几个方法
	 * @param title 随便写
	 * @param funCode	功能注册编码
	 * @param configFilePath	xml配置文件路径
	 */
	public ImportPanel() {
		super(title,funCode,configFilePath);
	}
	
	public void addNew() {
		model.initModel(null);
		model.setUiState(UIState.EDIT);	//设置单据为编辑态，如果不需要在界面上动态展示字段赋值的过程，可以去掉
	}
	
	/** 这里通过将eavo的值设值到界面上，在save()中再从界面取出该单据的聚合VO。也可以不用这个思路，
	 * 自己写VO转换类，实现IVOprocessor接口，将ExtendedAggregatedValueObject 转换为该单据的聚合VO
	 * @param obj 是导入进来的初始数据，实际是 ExtendedAggregatedValueObject 类对象，可强转
	 */
	public void setValue(Object obj) {
		//将VO的数据正确设到界面上
		ExtendedAggregatedValueObject eavo = (ExtendedAggregatedValueObject)obj;
		getBillcardPanelEditor().getBillCardPanel().getBillData().setImportBillValueVO(eavo, true, true);
	}
	
	/**第一步：setValue()方法中已经将eavo赋值到界面上，这里可以直接从界面上取出本单据的聚合VO
	 *对VO做进一步的处理和判断，通过setStatus()方法将主表、子表VO分别都设置成VOStatus.NEW，表示新增，
	 *如果是修改，则为VOStatus.UPDATED 
	 *通过IPFBusiAction接口调用单据动作脚本，脚本文件名规则：如：'N_TC01_SAVEBASE'，TC01为单据类型编码
	 */
	public void save() throws Exception {
//		IdGenerator idGenerator = new SequenceGenerator();//主键生成器
		
		AggConnission vo = (AggConnission) getBillcardPanelEditor().getValue();
		vo.getParentVO().setApprovestatus(-1);
		vo.getParentVO().setStatus(VOStatus.NEW);
		
		for (CircularlyAccessibleValueObject bodyvo : vo.getChildrenVO()){
			bodyvo.setStatus(VOStatus.NEW);
		}
		
		IPFBusiAction busiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		busiAction.processAction("SAVEBASE", "TC01", null, vo, null, null);
		
 		model.setUiState(UIState.NOT_EDIT);//最后将单据设置为非编辑状态，如果不需要在界面上动态展示字段赋值的过程，可以去掉
	}
	
	public void Cancel(){
		super.cancel();
	}

	protected String getAddActionBeanName() {
		return null;
	}

	protected String getAppModelBeanName() {
		return null;
	}

	protected String getBillCardEditorBeanName() {
		return null;
	}

	protected String getCancelActionBeanName() {
		return null;
	}

	protected String getSaveActionBeanName() {
		return null;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
	}
}

