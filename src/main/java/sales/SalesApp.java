package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

	private SalesDao salesDao;
	private SalesReportDao salesReportDao;

	public SalesApp() {
		salesDao = new SalesDao();
		salesReportDao = new SalesReportDao();
	}

	public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {

		List<String> headers = null;
		
		List<SalesReportData> filteredReportDataList = new ArrayList<SalesReportData>();
		
		if (salesId == null) {
			return;
		}
		
		Sales sales = salesDao.getSalesBySalesId(salesId);

		if (checkDate(sales)) return;

		List<SalesReportData> reportDataList = salesReportDao.getReportData(sales);

		filteredReportDataList = getFilteredReportDataList(isSupervisor, reportDataList);

		List<SalesReportData> tempList = getTempList(maxRow, reportDataList);
		filteredReportDataList = tempList;

		SalesActivityReport report = getSalesActivityReport(isNatTrade, reportDataList);

		uploadDocument(report);

	}

	protected void uploadDocument(SalesActivityReport report) {
		EcmService ecmService = new EcmService();
		ecmService.uploadDocument(report.toXml());
	}

	protected SalesActivityReport getSalesActivityReport(boolean isNatTrade, List<SalesReportData> reportDataList) {
		List<String> headers;
		if (isNatTrade) {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		} else {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
		}

		return this.generateReport(headers, reportDataList);
	}

	protected List<SalesReportData> getTempList(int maxRow, List<SalesReportData> reportDataList) {
		List<SalesReportData> tempList = new ArrayList<>();
		for (int i=0; i < reportDataList.size() && i < maxRow; i++) {
			tempList.add(reportDataList.get(i));
		}
		return tempList;
	}

	protected List<SalesReportData> getFilteredReportDataList(boolean isSupervisor, List<SalesReportData> reportDataList) {
		List<SalesReportData> filteredReportDataList = new ArrayList<>();
		for (SalesReportData data : reportDataList) {
			if ("SalesActivity".equalsIgnoreCase(data.getType())) {
				if (data.isConfidential()) {
					if (isSupervisor) {
						filteredReportDataList.add(data);
					}
				}else {
					filteredReportDataList.add(data);
				}
			}
		}
		return filteredReportDataList;
	}

	protected boolean checkDate(Sales sales) {
		Date today = new Date();
		if (today.after(sales.getEffectiveTo())
				|| today.before(sales.getEffectiveFrom())){
			return true;
		}
		return false;
	}

	protected SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
		// TODO Auto-generated method stub
		return null;
	}

}
