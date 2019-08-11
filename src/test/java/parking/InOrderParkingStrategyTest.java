package parking;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class InOrderParkingStrategyTest {

	@Test
    public void testCreateReceipt_givenACarAndAParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {

	    /* Exercise 1, Write a test case on InOrderParkingStrategy.createReceipt()
	    * With using Mockito to mock the input parameter */
	    //given
        ParkingLot parkingLot = mock(ParkingLot.class);
        Car car = mock(Car.class);
        when(parkingLot.getName()).thenReturn("parkingLot Name");
        when(car.getName()).thenReturn("car Name");
        InOrderParkingStrategy inOrderParkingStrategy = new InOrderParkingStrategy();
        Receipt receipt = new Receipt();
        receipt.setCarName("car Name");
        receipt.setParkingLotName("parkingLot Name");
        Assert.assertEquals(receipt.getCarName(), inOrderParkingStrategy.createReceipt(parkingLot, car).getCarName());
        Assert.assertEquals(receipt.getParkingLotName(), inOrderParkingStrategy.createReceipt(parkingLot, car).getParkingLotName());

    }

    @Test
    public void testCreateNoSpaceReceipt_givenACar_thenGiveANoSpaceReceipt() {

        /* Exercise 1, Write a test case on InOrderParkingStrategy.createNoSpaceReceipt()
         * With using Mockito to mock the input parameter */

    }

    @Test
    public void testPark_givenNoAvailableParkingLot_thenCreateNoSpaceReceipt(){

	    /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for no available parking lot */
        ParkingLot parkingLot = mock(ParkingLot.class);
        Car car = mock(Car.class);

        when(parkingLot.isFull()).thenReturn(true);
        when(car.getName()).thenReturn("CT car");

        InOrderParkingStrategy inOrderParkingStrategy = spy(InOrderParkingStrategy.class);
        inOrderParkingStrategy.park(Arrays.asList(parkingLot), car);

        Mockito.verify(inOrderParkingStrategy, times(1)).createNoSpaceReceipt(car);
        Mockito.verify(inOrderParkingStrategy, times(0)).createReceipt(parkingLot, car);

    }

    @Test
    public void testPark_givenThereIsOneParkingLotWithSpace_thenCreateReceipt(){

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot */


    }

    @Test
    public void testPark_givenThereIsOneFullParkingLot_thenCreateReceipt(){

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot but it is full */

    }

    @Test
    public void testPark_givenThereIsMultipleParkingLotAndFirstOneIsFull_thenCreateReceiptWithUnfullParkingLot(){

        /* Exercise 3: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for multiple parking lot situation */
        ParkingLot parkingLotFirst = mock(ParkingLot.class);
        when(parkingLotFirst.isFull()).thenReturn(true);
        ParkingLot parkingLotSecond = new ParkingLot("Second", 1);
        List<ParkingLot> parkingLots = Arrays.asList(parkingLotFirst, parkingLotSecond);
        Car car = new Car("CAR_001");

        Receipt receipt = new Receipt();
        receipt.setParkingLotName("Second");
        receipt.setCarName("CAR_001");

        InOrderParkingStrategy inOrderParkingStrategy = spy(InOrderParkingStrategy.class);
        Receipt receiptResult = inOrderParkingStrategy.park(parkingLots, car);

        Mockito.verify(inOrderParkingStrategy, times(0)).createNoSpaceReceipt(car);
        Assert.assertEquals(receipt.getParkingLotName(), receiptResult.getParkingLotName());
        Assert.assertEquals(receipt.getCarName(), receiptResult.getCarName());
    }


}
