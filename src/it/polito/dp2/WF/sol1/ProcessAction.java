package it.polito.dp2.WF.sol1;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.util.WFAttributes;
import it.polito.dp2.WF.sol1.util.WFElements;

/**
 * This is a concrete implementation of abstract class {@link AbstractActionReader} (that implements the interface ActionReader).<BR>
 * Another implementation of that abstract class is {@link SimpleAction}<br>
 * This implementation is based on JAXP framework.
 *
 * @see {@link ActionReader}, {@link AbstractActionReader}, {@link SimpleActionR}
 * @author Luca
 */
public class ProcessAction extends AbstractActionReader implements ProcessActionReader {

	private WorkflowReader nextWorkflow;

	public ProcessAction(Element action, WorkflowReader workflow) throws SAXException {
		super(action, workflow);
	
		if(action==null) return;	//safety lock
		
		Element root = (Element) action.getParentNode().getParentNode();
		NodeList workflows = root.getElementsByTagName(WFElements.WORKFLOW);
		NodeList processes = root.getElementsByTagName(WFElements.PROCESS);
		
		Element processAction = (Element)action.getElementsByTagName(WFElements.PROCESS_ACTION).item(0);
		
		if(processAction == null) return;	//safety lock
		
		String wfName = processAction.getAttribute(WFAttributes.ACTION_PROCESS_NEXT);
		
		//for each action I'm looking for its data
		for(int i=0; i<workflows.getLength(); i++) {
			Element wf = (Element)workflows.item(i);
			if( wf.getAttribute(WFAttributes.WORKFLOW_NAME).equals(wfName) ) {
				nextWorkflow = new ConcreteWorkflowReader(wf, processes);		
				break;
			}
		}
		
	}

	@Override
	public WorkflowReader getActionWorkflow() {
		return this.nextWorkflow;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\n\t\tNextWorkflow: "+nextWorkflow.getName();
	}

}
