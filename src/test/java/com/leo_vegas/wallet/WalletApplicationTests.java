package com.leo_vegas.wallet;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.leo_vegas.wallet.dto.CreditDto;
import com.leo_vegas.wallet.dto.DebitDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletApplicationTests {

    public static final long PLAYER_ID = 1L;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    public void smokeTest() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/players/" + PLAYER_ID + "/balance",
            String.class)).isEqualTo("{\"amount\":0.0}");
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/players/" + PLAYER_ID + "/transactions",
            String.class)).isEqualTo("[]");

        DebitDto debitDto = new DebitDto();
        debitDto.setAmount(1.0);
        debitDto.setTransactionId("debit");

        ResponseEntity<String> debitResult = this.restTemplate.postForEntity("http://localhost:" + port + "/players/" + PLAYER_ID + "/debit",
            debitDto, String.class);
        assertThat(debitResult.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(debitResult.getBody()).isEqualTo("Player has insufficient funds");

        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(3.0);
        creditDto.setTransactionId("credit");

        ResponseEntity<String> creditResult = this.restTemplate.postForEntity("http://localhost:" + port + "/players/" + PLAYER_ID + "/credit",
            creditDto, String.class);
        assertThat(creditResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(creditResult.getBody()).isNull();

        ResponseEntity<String> debitResult2 = this.restTemplate.postForEntity("http://localhost:" + port + "/players/" + PLAYER_ID + "/debit",
            debitDto, String.class);
        assertThat(debitResult2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(debitResult2.getBody()).isNull();

        ResponseEntity<String> debitResult3 = this.restTemplate.postForEntity("http://localhost:" + port + "/players/" + PLAYER_ID + "/debit",
            debitDto, String.class);
        assertThat(debitResult3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(debitResult3.getBody()).isEqualTo("Duplicate transaction id 'debit'");

        debitDto.setTransactionId("debit2");

        ResponseEntity<String> debitResult4 = this.restTemplate.postForEntity("http://localhost:" + port + "/players/" + PLAYER_ID + "/debit",
            debitDto, String.class);
        assertThat(debitResult4.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(debitResult4.getBody()).isNull();

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/players/" + PLAYER_ID + "/transactions",
            String.class)).isEqualTo("[" +
            "{\"type\":\"credit\",\"amount\":3.0,\"transaction_id\":\"credit\"}," +
            "{\"type\":\"debit\",\"amount\":1.0,\"transaction_id\":\"debit\"}," +
            "{\"type\":\"debit\",\"amount\":1.0,\"transaction_id\":\"debit2\"}" +
            "]");
    }

}
