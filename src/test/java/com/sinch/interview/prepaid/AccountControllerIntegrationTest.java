package com.sinch.interview.prepaid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class AccountControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void returnsSeededAccountWithRecentMessages() throws Exception {
        mockMvc.perform(get("/v1/accounts/acct-high-volume"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("acct-high-volume"))
                .andExpect(jsonPath("$.availableBalanceCents").value(1000000))
                .andExpect(jsonPath("$.reservedBalanceCents").value(500))
                .andExpect(jsonPath("$.recentMessages.length()").value(3));
    }

    @Test
    void returns404WhenAccountMissing() throws Exception {
        mockMvc.perform(get("/v1/accounts/does-not-exist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Account Not Found"));
    }
}
