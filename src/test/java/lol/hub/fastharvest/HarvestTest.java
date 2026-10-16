package lol.hub.fastharvest;

import lol.hub.testutils.Area;
import lol.hub.testutils.Bot;
import lol.hub.testutils.Pos;
import lol.hub.testutils.TestServer;
import lol.hub.testutils.TestServerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestServerExtension.class)
class HarvestTest {

    @ParameterizedTest
    @ValueSource(strings = {"wooden_hoe", "stone_hoe", "copper_hoe", "iron_hoe", "golden_hoe", "diamond_hoe", "netherite_hoe"})
    void hoeHarvestsRipeWheat(String hoe, TestServer server, Area area) {
        // Wheat needs farmland under it; the area's floor is stone.
        Pos wheat = area.at(4, 1, 4);
        server.command("setblock " + area.at(4, 0, 4) + " farmland");
        server.command("setblock " + wheat + " wheat[age=7]");
        Bot bot = area.bot();
        bot.hold("minecraft:" + hoe);

        bot.use(wheat);

        assertEquals(1, bot.inventory().mainHand().damage(), "hoe damage");
        assertEquals("0", server.block(wheat).state().get("age"), "wheat age");
        // One wheat stack, plus a seed on a coin flip.
        Pos corner = area.at(3, 0, 3);
        int drops = server.check("if entity @e[type=item,x=" + corner.x() + ",y=" + corner.y() + ",z=" + corner.z() + ",dx=2,dy=3,dz=2]").count();
        assertTrue(drops == 1 || drops == 2, "item entities at the wheat: " + drops);
    }
}
