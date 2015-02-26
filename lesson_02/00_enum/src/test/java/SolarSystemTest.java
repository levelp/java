/**
 *
 */
public class SolarSystemTest {
    public static void main(String[] args) {
        SolarSystemPlanet planet = SolarSystemPlanet.EARTH;

        needColonization(planet);

        Sex sex = Sex.FEMALE;
        if(sex == Sex.MALE){

        }
    }

    private static boolean needColonization(SolarSystemPlanet planet) {
        switch (planet) {
            case EARTH:
                System.out.println(planet + " - надо подумать :)");
                return false;
            case MARS:
                return true;
            case VENUS:
                return false;
            default:
                return false;
        }
    }
}
