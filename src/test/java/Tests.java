import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static ru.netology.entity.Country.*;

public class Tests {
    //Task 1
    @Test
    public void isLanguageRuBecauseOfIp() {
        //arrange
        String expected = "RUSSIA";

        GeoService geoService = new GeoServiceImpl();//Mockito.mock(GeoServiceImpl.class);
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);

        Mockito.when(localizationService.locale(USA)).thenReturn("RUSSIA");
        Mockito.when(localizationService.locale(RUSSIA)).thenReturn("RUSSIA");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");

        //act
        String res = messageSender.send(headers);
        //assert
        Assertions.assertEquals(expected, res);
    }

    //task 2
    @Test
    public void isLanguageEngBecauseOfIp() {
        //arrange
        String expected = "USA";

        GeoService geoService = new GeoServiceImpl();//Mockito.mock(GeoServiceImpl.class);
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);

        Mockito.when(localizationService.locale(USA)).thenReturn("USA");
        Mockito.when(localizationService.locale(RUSSIA)).thenReturn("USA");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");

        //act
        String res = messageSender.send(headers);
        //assert
        Assertions.assertEquals(expected, res);
    }

    //task 3]
    @MethodSource("byIpTestParam")
    @ParameterizedTest
    public void byIpTest(String inputStr) {
        //arrange

        GeoService geoService = new GeoServiceImpl();
        Location location = null;
        String mainNumber = inputStr.split("\\.")[0];
        switch (mainNumber) {
            case ("96"): {
                location = new Location("New York", USA, null, 0);
                break;
            }
            case ("127"): {
                location = new Location(null, null, null, 0);
                break;

            }
            case ("172"): {
                location = new Location("Moscow", RUSSIA, null, 0);
                break;
            }
        }
        //act
        Location resLocation = geoService.byIp(inputStr);
        //assert
        Assertions.assertEquals(resLocation.getCountry(), location.getCountry());

    }

    public static Stream<String> byIpTestParam() {
        return Stream.of("96.44.183.149",
                "127.0.0.1",
                "172.0.32.11");
    }

    //test4
    @MethodSource
    @ParameterizedTest
    public void localeTest(Country country) {
        //arrange
        String expected = null;
        switch (country) {
            case RUSSIA:
                expected = "Добро пожаловать";
                break;
            case USA:
                expected = "Welcome";
                break;
        }
        LocalizationService localizationService = new LocalizationServiceImpl();
        //act
        String result = localizationService.locale(country);
        //assert
        Assertions.assertEquals(expected, result);
    }

    public static Stream<Country> localeTest() {
        return Stream.of(RUSSIA,
                USA);
    }


}
