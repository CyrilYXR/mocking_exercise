package sales;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

	@Mock
	private SalesDao salesDao;

	@Mock
	private SalesReportDao salesReportDao;

	@InjectMocks
	private SalesApp salesApp = spy(SalesApp.class);

	@Test
	public void testGenerateReport() {

		//given
		Sales sales = spy(Sales.class);
		doReturn(new Date(new Date().getTime() - 24*60*60*1000)).when(sales).getEffectiveFrom();
		doReturn(new Date(new Date().getTime() + 24*60*60*1000)).when(sales).getEffectiveTo();
		when(salesDao.getSalesBySalesId("DUMMY")).thenReturn(sales);

		SalesReportData salesReportData = spy(SalesReportData.class);
		doReturn("SalesActivity").when(salesReportData).getType();
		when(salesReportDao.getReportData(sales)).thenReturn(Arrays.asList(new SalesReportData(), salesReportData));

		//when
		SalesActivityReport salesActivityReport = new SalesActivityReport();
		when(salesApp.generateReport(any(), any())).thenReturn(salesActivityReport);
		salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);

		//then
		verify(salesApp).checkDate(sales);
		List<SalesReportData> reportData = salesReportDao.getReportData(sales);
		verify(salesApp).getFilteredReportDataList(false, reportData);
		verify(salesApp).getTempList(1000, reportData);
		verify(salesApp).getSalesActivityReport(false, reportData);
		verify(salesApp).uploadDocument(salesActivityReport);

	}

	@Test
	public void testCheckDate(){
		//given
		Sales sales = spy(Sales.class);
		doReturn(new Date(new Date().getTime() - 24*60*60*1000)).when(sales).getEffectiveFrom();
		doReturn(new Date(new Date().getTime() + 24*60*60*1000)).when(sales).getEffectiveTo();

		//when
		SalesApp salesApp = new SalesApp();
		boolean checkDate = salesApp.checkDate(sales);

		//then
		Assert.assertFalse(checkDate);
	}

	@Test
	public void testGetFilterdReportDataList(){
		//given
		Sales sales = mock(Sales.class);
		SalesReportData salesReportData = mock(SalesReportData.class);
		when(salesReportData.getType()).thenReturn("SalesActivity");
		when(salesReportDao.getReportData(sales)).thenReturn(Arrays.asList(new SalesReportData(), salesReportData));
		List<SalesReportData> filteredReportDataList = new ArrayList<>();

		//when
		SalesApp salesApp = new SalesApp();
		filteredReportDataList = salesApp.getFilteredReportDataList(true, salesReportDao.getReportData(sales));

		//then
		Assert.assertEquals(1, filteredReportDataList.size());
		Assert.assertEquals("SalesActivity", filteredReportDataList.get(0).getType());
	}

	@Test
	public void testGetFilterdReportDataList_should_return_empty_list_when_isSupervisor_is_false_and_confidential_is_false(){
		//given
		Sales sales = mock(Sales.class);
		SalesReportData salesReportData = mock(SalesReportData.class);
		when(salesReportData.getType()).thenReturn("SalesActivity");
		when(salesReportData.isConfidential()).thenReturn(true);
		when(salesReportDao.getReportData(sales)).thenReturn(Arrays.asList(new SalesReportData(), salesReportData));
		List<SalesReportData> filteredReportDataList = new ArrayList<>();

		//when
		SalesApp salesApp = new SalesApp();
		filteredReportDataList = salesApp.getFilteredReportDataList(false, salesReportDao.getReportData(sales));

		//then
		Assert.assertEquals(0, filteredReportDataList.size());
	}

	@Test
	public void testGetTempList_should_return_temp_list_size_is_2(){
		//given
		Sales sales = mock(Sales.class);
		when(salesReportDao.getReportData(sales)).thenReturn(Arrays.asList(new SalesReportData(), new SalesReportData()));

		//when
		SalesApp salesApp = new SalesApp();
		List<SalesReportData> tempList = salesApp.getTempList(10, salesReportDao.getReportData(sales));

		//then
		Assert.assertEquals(2, tempList.size());
	}

	@Test
	public void testGetTempList_should_return_temp_list_size_is_1(){
		//given
		Sales sales = mock(Sales.class);
		when(salesReportDao.getReportData(sales)).thenReturn(Arrays.asList(new SalesReportData(), new SalesReportData()));

		//when
		SalesApp salesApp = new SalesApp();
		List<SalesReportData> tempList = salesApp.getTempList(1, salesReportDao.getReportData(sales));

		//then
		Assert.assertEquals(1, tempList.size());
	}

	@Test
	public void testGetSalesActivityReport(){
		//given
		SalesApp salesApp = spy(SalesApp.class);
		when(salesReportDao.getReportData(any())).thenReturn(Arrays.asList(new SalesReportData(), new SalesReportData()));
		List<String> headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		doReturn(new SalesActivityReport()).when(salesApp).generateReport(any(),any());

		//when
		List<SalesReportData> reportData = salesReportDao.getReportData(new Sales());
		SalesActivityReport salesActivityReport = salesApp.getSalesActivityReport(true, reportData);

		//then
		verify(salesApp, times(1)).generateReport(headers, reportData);
	}

}
