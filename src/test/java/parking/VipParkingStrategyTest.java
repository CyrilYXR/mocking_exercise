package parking;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VipParkingStrategyTest {

    @Mock
    private CarDao carDao;
    @InjectMocks
    private VipParkingStrategy vipParkingStrategy = new VipParkingStrategy();

	@Test
    public void testPark_givenAVipCarAndAFullParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {

	    /* Exercise 4, Write a test case on VipParkingStrategy.park()
	    * With using Mockito spy, verify and doReturn */

        ParkingLot parkingLotSecond = mock(ParkingLot.class);
        when(parkingLotSecond.isFull()).thenReturn(true);
        when(parkingLotSecond.getName()).thenReturn("Second");
        List<ParkingLot> parkingLots = Arrays.asList(parkingLotSecond);

        Car car = new Car("A");

        Receipt receipt = new Receipt();
        receipt.setParkingLotName("Second");
        receipt.setCarName("A");

        VipParkingStrategy vipParkingStrategy = spy(VipParkingStrategy.class);
        doReturn(true).when(vipParkingStrategy).isAllowOverPark(car);
        Receipt receiptResult = vipParkingStrategy.park(parkingLots, car);

        Assert.assertEquals(receipt.getParkingLotName(), receiptResult.getParkingLotName());
        Assert.assertEquals(receipt.getCarName(), receiptResult.getCarName());

    }

    @Test
    public void testPark_givenCarIsNotVipAndAFullParkingLog_thenGiveNoSpaceReceipt() {

        /* Exercise 4, Write a test case on VipParkingStrategy.park()
         * With using Mockito spy, verify and doReturn */

    }

    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsVipCar_thenReturnTrue(){

        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
        when(carDao.isVip(any())).thenReturn(true);
        boolean allowOverPark = vipParkingStrategy.isAllowOverPark(new Car("AAA"));
        Assert.assertTrue(allowOverPark);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsVipCar_thenReturnFalse(){

        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
        when(carDao.isVip(any())).thenReturn(true);
        boolean allowOverPark = vipParkingStrategy.isAllowOverPark(new Car("BBB"));
        Assert.assertFalse(allowOverPark);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsNotVipCar_thenReturnFalse(){
        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsNotVipCar_thenReturnFalse() {
        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
    }

    private Car createMockCar(String carName) {
        Car car = mock(Car.class);
        when(car.getName()).thenReturn(carName);
        return car;
    }
}
