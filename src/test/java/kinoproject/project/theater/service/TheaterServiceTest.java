package kinoproject.project.theater.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import kinoproject.project.theater.dto.TheaterRequest;
import kinoproject.project.theater.enity.Theater;
import kinoproject.project.theater.repository.TheaterRepository;
import kinoproject.security.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TheaterServiceTest {

    @Autowired
    TheaterRepository theaterRepository;


    TheaterService theaterService;

    @BeforeAll
    public static void iniSetUp() {
        System.setProperty(TestUtils.h2UrlName, TestUtils.h2UrlValue);
        System.setProperty(TestUtils.h2UsernameName, TestUtils.h2UsernameValue);
        System.setProperty(TestUtils.h2PassName, TestUtils.h2PassValue);
        System.setProperty(TestUtils.tokenSecretName, TestUtils.tokenSecretValue);
    }

    @AfterAll
    public static void tearDown() {
        System.clearProperty(TestUtils.h2UrlName);
        System.clearProperty(TestUtils.h2UsernameName);
        System.clearProperty(TestUtils.h2PassName);
        System.clearProperty(TestUtils.tokenSecretName);
    }

    Theater t1, t2;

    @BeforeEach
    void setUp() {
        theaterService = new TheaterService(theaterRepository);
    }

    @Test
    void testGetAllTheaters(){
        Theater t1 = Theater.builder()
                        .seatCount(400).rowLength(10).build();
        theaterRepository.save(t1);
        assertEquals(1, theaterService.getAllTheaters().size());

        Theater t2 = Theater.builder()
                .seatCount(400).rowLength(10).build();
        theaterRepository.save(t2);
        assertEquals(2, theaterService.getAllTheaters().size());
    }

    @Test
    void testAddTheater(){
        Theater t1 = Theater.builder()
                .seatCount(400).rowLength(10).build();
        TheaterRequest theaterRequest = new TheaterRequest(t1);
        theaterService.addTheater(theaterRequest);
        assertEquals(1, theaterService.getAllTheaters().size());
    }
}